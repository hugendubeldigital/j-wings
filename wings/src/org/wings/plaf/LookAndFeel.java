/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
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
import javax.servlet.ServletOutputStream;
import javax.swing.Icon;
import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.style.*;

/**
 * A Look-and-Feel consists of a bunch of CGs and resource properties.
 * wingS provides a pluggable look-and-feel (laf or plaf) concept similar to that of Swing.
 * A certain plaf implementation adresses normally a specific browser.
 * 
 * @see org.wings.plaf.ComponentCG
 */
public class LookAndFeel
{
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
        if (in == null)
            throw new IOException ("'default.properties' not found in toplevel package in classpath.");
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
     * Create a CG instance.
     * @param className the full qualified class name of the CG
     * @return a new CG instance
     */
    public Object makeCG(String className) {
        System.err.print("LookAndFeel.makeCG(" + className + ")");
        try {
            Class cgClass = Class.forName(className, true, classLoader);
            return cgClass.newInstance();
        }
        catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
        catch (InstantiationException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
        catch (IllegalAccessException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Utility method that creates an Icon from a resource
     * located realtive to the given base class.
     * @param baseClass the ClassLoader of the baseClass will be used
     * @param fileName of the image file
     * @return a newly allocated Icon
     * @deprecated give the <code>classLoader</code> instead the <code>baseClass</code>
     */
    public static SIcon makeIcon(Class baseClass, String fileName) {
        return new ResourceImageIcon(baseClass, fileName);
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
     * Utility method that creates a font from a font spec
     * @param spec attributes of the font
     * @return a font with the given attributes
     */
    public static SFont makeFont(String value) {
        int pos1 = value.indexOf(",", 1);
        String name = value.substring(0, pos1);

        int pos2 = value.indexOf(",", pos1 + 1); 
        String styleString = value.substring(pos1, pos2);
        int style = Font.PLAIN;
        if (styleString.indexOf("ITALIC") != -1)
            style |= Font.ITALIC;
        if (styleString.indexOf("BOLD") != -1)
            style |= Font.BOLD;

        int size = new Integer(value.substring(pos2 + 1)).intValue();

        return new SFont(name, style, size);
    }

    /**
     * Utility method that creates a color from a hex string
     * @param value color as a hex string
     * @return the color
     */
    public static Color makeColor(String value) {
        int r = Integer.parseInt(value.substring(1, 3), 16);
        int g = Integer.parseInt(value.substring(3, 5), 16);
        int b = Integer.parseInt(value.substring(5, 7), 16);
        return new Color(r, g, b);
    }

    /**
     * Utility method that creates a style from a string
     * @param value style as a string
     * @return the style
     */
    public static Style makeStyle(String style) {
        return new Style(style);
    }

    /**
     * Utility method that creates a styleSheet from a string
     * @param value styleSheet as a string
     * @return the style
     */
    public static StyleSheet makeStyleSheet(ClassLoader classLoader, String resourceName) {
        return new ResourceStyleSheet(classLoader, resourceName);
    }

    /**
     * Utility method that creates a styleSheet from a string
     * @param value styleSheet as a string
     * @return the style
     */
    public StyleSheet makeStyleSheet(String resourceName) {
        return new ResourceStyleSheet(classLoader, resourceName);
    }

    /**
     * Utility method that creates an Object of class <code>clazz</code>
     * using the single String arg constructor.
     * @param classLoader the classLoader to be used
     * @param value object as a string
     * @param clazz class of the object
     * @return the object
     */
    public static Object makeObject(ClassLoader classLoader, String value, Class clazz) {
        try {
            System.err.println("makeObject of type " + clazz.getName() + " with " + value);
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
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
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

    class ResourceFactory
        extends CGDefaults
    {
        public ResourceFactory() { super(null); }

        public Object get(String id, Class type) {
            Object value = get(id);
            if (value != null)
                return value;

            String property = properties.getProperty(id);
            if (property == null)
                return null;

            if (ComponentCG.class.isAssignableFrom(type))
                value = makeCG(property);
            else if (LayoutCG.class.isAssignableFrom(type))
                value = makeCG(property);
            else if (BorderCG.class.isAssignableFrom(type))
                value = makeCG(property);
            else if (Icon.class.isAssignableFrom(type))
                value = makeIcon(property);
            else if (SFont.class.isAssignableFrom(type))
                value = makeFont(property);
            else if (Color.class.isAssignableFrom(type))
                value = makeColor(property);
            else if (Style.class.isAssignableFrom(type))
                value = makeStyle(property);
            else if (StyleSheet.class.isAssignableFrom(type))
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
 * End:
 */
