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
import java.text.*;

/**
 * TODO: parse units
 */
public class SDimension
{
    public String width = null;
    public String height = null;
    private int iwidth = -1;
    private int iheight = -1;

    public SDimension() {}

    public SDimension(String width, String height) {
        setWidth(width);
        setHeight(height);
    }

    /**
      * Construct a new dimension.
	  * The value is converted to "<value>px".
      * @param width the width. If value is less than <code>0</code> width is set to <code>null</code>.
      * @param height the height. If value is less than <code>0</code> height is set to <code>null</code>.
      * @see #setSize(int,int)
      */
    public SDimension(int width, int height) {
        setSize(width,height);
    }

    public void setWidth(String width) {
        if (width != null && !Character.isDigit(width.charAt(0)))
            throw new IllegalArgumentException(width);
        this.width = width;
        iwidth=-1;
    }

    public void setHeight(String height) {
        if (height != null && !Character.isDigit(height.charAt(0)))
            throw new IllegalArgumentException(height);
        this.height = height;
        iheight=-1;
    }

    /**
      * Set width in pixel.
      * This appends "px" to the integer value.
      * @param width if -1 set {@link SDimension#width} to <code>null</code>
      */
    public void setWidth(int width) {
        this.iwidth= width;
        if ( width > -1 )
            this.width = width+"px";
        else
            this.height=null;
    }

    /**
      * Set height in pixel.
      * This appends "px" to the integer value.
      * @param height if -1 set {@link SDimension#height} to <code>null</code>
      */
    public void setHeight(int height) {
        this.iheight= height;
        if ( height > -1 )
            this.height = height+"px";
        else
            this.height=null;
    }

	/**
	  * Extract number from string.
	  * @return extracted integer. f.e.: "120px" becomes 120
	  */
    protected int getInt(String size) {
        try {
            return new DecimalFormat().parse(size,new ParsePosition(0)).intValue();
        }
        catch(Exception ex)
        {
            System.err.println("Can not parse ["+size+"]");
            return -1;
        }
    }

    public String getWidth() { return width; }
    public String getHeight() { return height; }
        
    /**
      * Get just the width as number without trailing 
      * unit.
      */
    public int getIntWidth() {
        if (iwidth==-1)
            iwidth=getInt(width);
        return iwidth;
    }
        
    /**
      * Get just the height as number without trailing 
      * unit.
      */
    public int getIntHeight() {
        if (iheight==-1)
            iheight=getInt(height);
        return iheight;
    }
	
    /**
      * Checks whether two dimension objects have equal values.
      * @param obj - the reference object with which to compare.
      * @return true if this object is the same as the obj argument; false otherwise.
      */
    public boolean equals(Object obj) {
        return width.equals(((SDimension) obj).width) && height.equals(((SDimension) obj).height);
    }
    
    /**
      * Set the size of this Dimension object to the specified width and height
      * and append "px" to both values.
	  * @see #setHeight(int)
	  * @see #setWidth(int)
      */
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }
}
