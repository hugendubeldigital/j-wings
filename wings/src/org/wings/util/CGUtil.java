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
	public static void writeSize( Device d, SComponent c )
        throws IOException
     {
        Dimension dim = c.getPreferredSize();
        Dimension dimp = c.getPreferredPercentageSize();

        if ( dim != null )
         {
            if ( dim.width != 0 ) d.append(" width=\"").append(dim.width).append("\"");
            if ( dim.height != 0 ) d.append(" height=\"").append(dim.height).append("\"");
		 }
		else
        if ( dimp != null )
         {
            if ( dimp.width != 0 ) d.append(" width=\"").append(dimp.width).append("%\"");
            if ( dimp.height != 0 ) d.append(" height=\"").append(dimp.height).append("%\"");
		 }
		else
		 {
         	/* for compatibility with older versions */
			if ( c instanceof STable )
             {
        		String width = ((STable) c).getWidth();
        		if (width != null)
            		d.append(" width=\"").append(width).append("\"");
             }
         }
    }

	public static void writeSize( Device d, SAbstractLayoutManager lm )
        throws IOException
     {
        Dimension dim = lm.getPreferredSize();
        Dimension dimp = lm.getPreferredPercentageSize();

        if ( dim != null )
         {
            if ( dim.width != 0 ) d.append(" width=\"").append(dim.width).append("\"");
            if ( dim.height != 0 ) d.append(" height=\"").append(dim.height).append("\"");
		 }
		else
        if ( dimp != null )
         {
            if ( dimp.width != 0 ) d.append(" width=\"").append(dimp.width).append("%\"");
            if ( dimp.height != 0 ) d.append(" height=\"").append(dimp.height).append("%\"");
		 }
     }
 }
