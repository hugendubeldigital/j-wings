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
import javax.swing.ImageIcon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.style.*;

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

    public LookAndFeel(Properties properties) {
	this.properties = properties;
    }

    /**
     * Return a short string that identifies this look and feel, e.g.
     * "XHTML".
     */
    public String getName() {
        return properties.getProperty("lookandfeel.name");
    }

    /**
     * Return a one line description of this look and feel implementation,
     * e.g. "XHTML Look and Feel".
     */
    public String getDescription() {
        return properties.getProperty("lookandfeel.description");
    }

    /**
     * This method is called once by CGManager.setLookAndFeel to create
     * the look and feel specific defaults table.  Other applications,
     * for example an application builder, may also call this method.
     *
     * @see #initialize
     * @see #uninitialize
     * @see CGManager#setLookAndFeel
     */
    public CGDefaults getDefaults() {
        CGDefaults table = new CGDefaults();
	table.putAll(properties);
        return table;
    }

    /**
     * Return an appropriate Device for code generation.
     * Some lafs can deal with a stream, others rely on a buffered
     * Device, because they produce code that must appear in the header.
     *
     * In fact, this feature was never used, yet. Should we de deprecate it?
     *
     * @return a Device that is suitable for this laf
     */
    public Device createDevice(javax.servlet.ServletOutputStream stream) {
        return null;
    }

    /**
     * CGManager.setLookAndFeel calls this method before the first
     * call (and typically the only call) to getDefaults().
     *
     * @see #uninitialize
     * @see CGManager#setLookAndFeel
     */
    public void initialize() {
    }

    /**
     * CGManager.setLookAndFeel calls this method just before we're
     * replaced by a new default look and feel.   Subclasses may
     * choose to free up some resources here.
     *
     * @see #initialize
     */
    public void uninitialize() {
    }

    /**
     * Utility method that creates an ImageIcon from a resource
     * located realtive to the given base class.
     * @param baseClass
     * @param fileName of gif file
     * @return image icon
     */
    public static ImageIcon makeIcon(Class baseClass, String fileName) {
        return new ResourceImageIcon(baseClass, fileName);
    }

    public static ImageIcon makeIcon(String fileName) {
        return makeIcon(LookAndFeel.class, fileName);
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
    public static StyleSheet makeStyleSheet(String resourceName) {
        return new ResourceStyleSheet(LookAndFeel.class, resourceName);
    }

    /**
     * Utility method that creates an Object of class <code>clazz</code>
     * using the single String arg constructor.
     * @param value object as a string
     * @param value class of the object
     * @return the object
     */
    public static Object makeObject(String value, Class clazz) {
        try {
            System.err.println("makeObject of type " + clazz.getName() + " with " + value);
            if (value.startsWith("new ")) {
                int bracket = value.indexOf("(");
                String name = value.substring("new ".length(), bracket);
                clazz = LookAndFeel.class.forName(name);
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
     * Returns a string that displays and identifies this
     * object's properties.
     *
     * @return a String representation of this object
     */
    public String toString() {
        return "[" + getDescription() + " - " + getClass().getName() + "]";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
