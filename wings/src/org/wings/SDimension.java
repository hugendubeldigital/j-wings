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

/**
 * TODO: parse units
 */
public class SDimension
{
    public String width = null;
    public String height = null;

    public SDimension() {}

    public SDimension(String width, String height) {
	setWidth(width);
	setHeight(height);
    }

    public void setWidth(String width) {
	if (width != null && !Character.isDigit(width.charAt(0)))
	    throw new IllegalArgumentException(width);
	this.width = width;
    }

    public void setHeight(String height) {
	if (height != null && !Character.isDigit(height.charAt(0)))
	    throw new IllegalArgumentException(height);
	this.height = height;
    }

    public String getWidth() { return width; }
    public String getHeight() { return height; }
}
