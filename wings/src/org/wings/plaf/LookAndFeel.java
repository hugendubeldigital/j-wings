/*
 * $Id$

 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.plaf;

import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.*;
import javax.servlet.ServletOutputStream;

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.style.*;

/**
 * A Look-and-Feel consists of a bunch of CGs and resource properties.
 * wingS provides a pluggable look-and-feel (laf or plaf) concept similar to that of Swing.
 * A certain plaf implementation normally adresses a specific browser.
 * 
 * @see org.wings.plaf.ComponentCG
 */
public class LookAndFeel
{
    private static Logger logger = Logger.getLogger("org.wings.plaf");

    private static Map wrappers = new HashMap();
    static {
        wrappers.put(Boolean.TYPE, Boolean.class);
        wrappers.put(Character.TYPE, Character.class);
        wrappers.put(Byte.TYPE, Byte.class);
        wrappers.put(Short.TYPE, Short.class);
        wrappers.put(Integer.TYPE, Integer.class);
        wrappers.put(Long.TYPE, Long.class);
        wrappers.put(Float.TYPE, Float.class);
        wrappers.put(Double.TYPE, Double.class);
    }

    protected Properties properties;
    protected ClassLoader classLoader;
    protected CGDefaults defaults;
    protected StyleSheet styleSheet;

    /**
     * Instantiate a laf using the war's classLoader.
     * @param properties the configuration of the laf
     */
    public LookAndFeel(Properties properties) {
	this.properties = properties;
	this.classLoader = getClass().getClassLoader();
        defaults = new ResourceFactory();
    }

    /**
     * Instantiate a laf using the specified classLoader.
     * The properties are read from the classLoader's classpath as a resource with
     * name <i>default.properties</i>.
     * @param classLoader the classLoader that will load the CGs
     */
    public LookAndFeel(ClassLoader classLoader)
        throws IOException
    {
        this.classLoader = classLoader;
        this.properties = new Properties();
        InputStream in = classLoader.getResourceAsStream("default.properties");
        if (in == null) {
            throw new IOException ("'default.properties' not found in toplevel package in classpath.");
        }
        this.properties.load(in);

        defaults = new ResourceFactory();
    }

    /**
     * Return a unique string that identifies this look and feel, e.g.
     * "konqueror"
     */
    public String getName() {
        return properties.getProperty("lookandfeel.name");
    }

    /**
     * Return a one line description of this look and feel implementation,
     * e.g. "Optimized for KDE's Konqueror Browser".
     */
    public String getDescription() {
        return properties.getProperty("lookandfeel.description");
    }

    /**
     * Return the ClassLoader, that is used to load the CGs.
     * @return the ClassLoader
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Return the CGDefaults, that hold the laf's defaults for CGs and resources
     * @return the laf's defaults
     */
    public CGDefaults getDefaults() {
        return defaults;
    }

    /**
     * Return the <code>lookandfeel.stylesheet</code>
     * @return the laf's style sheet
     */
    public StyleSheet getStyleSheet() {
        if (styleSheet == null) {
            styleSheet = new CSSStyleSheet();

            try {
                InputStream in = classLoader.getResourceAsStream(properties.getProperty("lookandfeel.stylesheet"));
                ((CSSStyleSheet)styleSheet).read(in);
            }
            catch (Exception e) {
                logger.log(Level.WARNING, null, e);
            }
        }
        if (styleSheet == null) {
            throw new RuntimeException("a stylsheet is required");
        }

        return styleSheet;
    }

    /**
     * Create a CG instance.
     * @param className the full qualified class name of the CG
     * @return a new CG instance
     */
    public Object makeCG(String className) {
        try {
            Class cgClass = Class.forName(className, true, classLoader);
            return cgClass.newInstance();
        }
        catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, null, e);
        }
        catch (InstantiationException e) {
            logger.log(Level.SEVERE, null, e);
        }
        catch (IllegalAccessException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * Utility method that creates an Icon from a resource
     * located realtive to the given base class.
     * @param classLoader the ClassLoader that should load the icon
     * @param fileName of the image file
     * @return a newly allocated Icon
     */
    public static SIcon makeIcon(ClassLoader classLoader, String fileName) {
        return new ResourceImageIcon(classLoader, fileName);
    }

    /**
     * Utility method that creates an Icon from a resource
     * located realtive to the given base class. Uses the ClassLoader
     * of the LookAndFeel
     *
     * @see LookAndFeel.LookAndFeel(Properties p, ClassLoader cl)
     * @param fileName of the image file
     * @return a newly allocated Icon
     */
    public SIcon makeIcon(String fileName) {
        return makeIcon(classLoader, fileName);
    }

    /**
     * Utility method that creates an AttributeSet from a String
     *
     * @param attributes attributes string
     * @return a newly allocated AttributeSet
     */
    public AttributeSet makeAttributeSet(String string) {
        AttributeSet attributes = new SimpleAttributeSet();
        StringTokenizer tokens = new StringTokenizer(string, ";");
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            int pos = token.indexOf(":");
            if (pos >= 0) {
                attributes.putAttribute(token.substring(0, pos), 
                                        token.substring(pos + 1));
            }
        }
        return attributes;
    }

    /**
     * Utility method that creates a styleSheet from a resource string
     * @param value styleSheet as a string
     * @return the style
     */
    public static Resource makeResource(ClassLoader classLoader, String resourceName) {
        return new ClasspathResource(classLoader, resourceName);
    }

    /**
     * Utility method that creates a styleSheet from a string
     * @param value styleSheet as a string
     * @return the styleSheet
     */
    public Resource makeResource(String resourceName) {
        return makeResource(classLoader, resourceName);
    }

    /**
     * Utility method that creates a stylesheet object from a resource
     * @param resourceName
     * @return the styleSheet
     */
    public StyleSheet makeStyleSheet(String resourceName) {
        try {
            CSSStyleSheet styleSheet = new CSSStyleSheet();
            InputStream in = classLoader.getResourceAsStream(resourceName);
            styleSheet.read(in);
            return styleSheet;
        }
        catch (Exception e) {
            logger.log(Level.WARNING, null, e);
            return null;
        }
    }

    /**
     * Utility method that fetches the style with the specified <code>name</code>
     * from the <code>lookandfeel.stylesheet</code>
     * @param value styleSheet as a string
     * @return the style
     */
    public Style makeStyle(String name) {
        Style style = getStyleSheet().getStyle(name);
        if (style == null)
            logger.warning("the style specification '" + name +
                           "' has no corresponding style definition in the 'lookandfeel.stylesheet'");
        return style;
    }

    /**
     * Utility method that creates an Object of class <code>clazz</code>
     * using the single String arg constructor.
     * @param classLoader the classLoader to be used
     * @param value object as a string
     * @param clazz class of the object
     * @return the object
     */
    public static Object makeObject(ClassLoader classLoader, String value, 
                                    Class clazz) 
    {
        try {
            if (value.startsWith("new ")) {
                int bracket = value.indexOf("(");
                String name = value.substring("new ".length(), bracket);
                clazz = LookAndFeel.class.forName(name, true, classLoader);
                return clazz.newInstance();
            }
            else {
                if (clazz.isPrimitive())
                    clazz = (Class)wrappers.get(clazz);
                Constructor constructor = clazz.getConstructor(new Class[] { String.class });
                return constructor.newInstance(new Object[] { value });
            }
        }
        catch (NoSuchMethodException e) {
            logger.log(Level.SEVERE, clazz.getName() 
                       + " doesn't have a single String arg constructor", e);
            return null;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Utility method that creates an Object of class <code>clazz</code>
     * using the single String arg constructor.
     * @param value object as a string
     * @param clazz class of the object
     * @return the object
     */
    public Object makeObject(String value, Class clazz) {
        return makeObject(classLoader, value, clazz);
    }

    /**
     * Returns a string that displays and identifies this
     * object's properties.
     *
     * @return a String representation of this object
     */
    public String toString() {
        return "[" + getDescription() + " - " + getClass().getName() + "]";
    }

    class ResourceFactory extends CGDefaults {
        public ResourceFactory() { super(null); }
        
        public Object get(String id, Class type) {
            Object value = get(id);
            if (value != null)
                return value;

            String property = properties.getProperty(id);
            if (property == null) {
                put(id, null);
                return null;
            }
            
            if (ComponentCG.class.isAssignableFrom(type)
                || LayoutCG.class.isAssignableFrom(type)
                || BorderCG.class.isAssignableFrom(type)) {
                /*
                 * some CG is requested. We do not check, whether the
                 * value returned actually fulfills
                 * type.isAssignableFrom(value.getClass());
                 */
                value = makeCG(property);
            }
            else if (type.isAssignableFrom(SIcon.class))
                value = makeIcon(property);
            else if (type.isAssignableFrom(Resource.class))
                value = makeResource(property);
            else if (type.isAssignableFrom(AttributeSet.class))
                value = makeAttributeSet(property);
            else if (type.isAssignableFrom(Style.class))
                value = makeStyle(property);
            else if (type.isAssignableFrom(StyleSheet.class))
                value = makeStyleSheet(property);
            else
                value = makeObject(property, type);

            put(id, value);
            return value;
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
