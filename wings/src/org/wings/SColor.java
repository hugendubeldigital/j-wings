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

package org.wings;
import java.awt.color.ColorSpace;


/**
 * Enhanced version of {@link java.awt.Color}.
 * @author <a href=mailto:andre@lison.de>Andre Lison</a>
 * @version $Revision$
 */
public class SColor
    extends java.awt.Color
{

    final static int[] fromHexDigits = {
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,1,2,3,4,5,6,7,8,9,0,0,0,0,0,0,
    0,10,11,12,13,14,15,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,10,11,12,13,14,15};

    final static char[] toHexDigits = {
        '0' , '1' , '2' , '3' , '4' , '5' ,
        '6' , '7' , '8' , '9' , 'a' , 'b' ,
        'c' , 'd' , 'e' , 'f'};

    /**
     * Creates an opaque sRGB color with the specified red, green, 
     * and blue values in the range (0 - 255).  
     * The actual color used in rendering depends
     * on finding the best match given the color space 
     * available for a given output device.  
     * Alpha is defaulted to 255.
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     */
    public SColor(int r, int g, int b) {
        super(r,g,b);
    }

    /**
     * Creates an sRGB color with the specified red, green, blue, and alpha
     * values in the range (0 - 255).
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getAlpha
     * @see #getRGB
     */
    public SColor(int r, int g, int b, int a) {
        super(r,g,b,a);
    }

    /**
     * Creates an opaque sRGB color with the specified combined RGB value
     * consisting of the red component in bits 16-23, the green component
     * in bits 8-15, and the blue component in bits 0-7.  The actual color
     * used in rendering depends on finding the best match given the
     * color space available for a particular output device.  Alpha is
     * defaulted to 255.
     * @param rgb the combined RGB components
     * @see java.awt.image.ColorModel#getRGBdefault
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     */
    public SColor(int rgb) {
        super(rgb);
    }

    /**
     * Creates an sRGB color with the specified combined RGBA value consisting
     * of the alpha component in bits 24-31, the red component in bits 16-23,
     * the green component in bits 8-15, and the blue component in bits 0-7.
     * If the <code>hasalpha</code> argument is <code>false</code>, alpha
     * is defaulted to 255.
     * @param rgba the combined RGBA components
     * @param hasalpha <code>true</code> if the alpha bits are valid;
     * <code>false</code> otherwise
     * @see java.awt.image.ColorModel#getRGBdefault
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getAlpha
     * @see #getRGB
     */
    public SColor(int rgba, boolean hasalpha) {
        super(rgba,hasalpha);
    }

    /**
     * Creates an opaque sRGB color with the specified red, green, and blue
     * values in the range (0.0 - 1.0).  Alpha is defaulted to 1.0.  The
     * actual color used in rendering depends on finding the best
     * match given the color space available for a particular output
     * device.
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     */
    public SColor(float r, float g, float b) {
        super(r,g,b);
    }

    /**
     * Creates an sRGB color with the specified red, green, blue, and
     * alpha values in the range (0.0 - 1.0).  The actual color
     * used in rendering depends on finding the best match given the
     * color space available for a particular output device.
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getAlpha
     * @see #getRGB
     */
    public SColor(float r, float g, float b, float a) {
        super(r,g,b,a);
    }

    /**
     * Creates a color in the specified <code>ColorSpace</code>
     * with the color components specified in the <code>float</code>
     * array and the specified alpha.  The number of components is
     * determined by the type of the <code>ColorSpace</code>.  For 
     * example, RGB requires 3 components, but CMYK requires 4 
     * components.
     * @param cspace the <code>ColorSpace</code> to be used to
     *			interpret the components
     * @param components an arbitrary number of color components
     *                      that is compatible with the 
     * @param alpha alpha value
     * @throws IllegalArgumentException if any of the values in the 
     *         <code>components</code> array or <code>alpha</code> is 
     *         outside of the range 0.0 to 1.0
     * @see #getComponents
     * @see #getColorComponents
     */
    public SColor(ColorSpace cspace, float components[], float alpha) {
        super(cspace,components,alpha);
    }

    /**
      * Construct a new color from hex-values.
      * @see #convertHex(String)
      */
    public SColor(String color) {
        super(convertHex(color));
    }
    
    /**
      * Creates a rgb value from hex value.
      * @param color f.e. <code>C9FFFa</code>
      * @exception IllegalArgumentException when color string has length < 6
      *   or contains invalid characters. Not all invalid characters
      *   are recognized!
      * @return RGB value as int
      */
    protected static int convertHex(String color)
    {
        byte[] ba = color.getBytes();
        if (ba.length < 6)
            throw new IllegalArgumentException(
                "Hex color string has to be always 6 characters wide!");
        int cvalue = 0;
        int i = 0;
        try
        {
            for (; i<6; i++) {
                cvalue <<= 4;
                cvalue += fromHexDigits[ba[i]];
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException(
                "Invalid character \""+((char)ba[i])+"\" in hex string found");
        }
        return cvalue;
    }
    
    /**
      * Get hex value for this color
      * @return hex string, f.e. <code>C9FFFa</code>
      */
    public String getHex() {
        int rgb = getRGB();
        char[] buf = new char[6];
        int digits = 6;
        do {
            buf[--digits] = toHexDigits[rgb & 15];
            rgb >>>= 4;
        }
        while (digits!=0);

        return new String(buf);
    }
    
    public static void main(String[] args) {
        String c = "00FFFA";
        SColor sc = new SColor(c);
        System.out.println(c+" = "+sc.getHex());
    }
}
