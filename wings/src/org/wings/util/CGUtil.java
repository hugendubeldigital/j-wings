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

package org.wings.util;

import java.awt.Dimension;
import java.io.IOException;

import org.wings.*;
import org.wings.io.*;

public class CGUtil
{
    /**
     * Write <i>width</i> and <i>height</i> argument to component tag.
     */
    public static void writeSize(Device d, SComponent c)
	throws IOException
    {
	SDimension dim = c.getPreferredSize();
	
	if (dim != null) {
	    if (dim.width != null)
		d.append(" width=\"").append(dim.width).append("\"");
	    if (dim.height != null)
		d.append(" height=\"").append(dim.height).append("\"");
	}
    }
    
    public static void writeSize(Device d, SAbstractLayoutManager lm)
        throws IOException
    {
        SDimension dim = lm.getPreferredSize();
	
        if (dim != null) {
	    if (dim.width != null) 
		d.append(" width=\"").append(dim.width).append("\"");
	    if (dim.height != null) 
		d.append(" height=\"").append(dim.height).append("\"");
	}
    }
}