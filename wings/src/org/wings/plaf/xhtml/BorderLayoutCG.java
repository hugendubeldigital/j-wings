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

package org.wings.plaf.xhtml;

import java.io.IOException;
import java.util.*;

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.util.CGUtil;
import org.wings.plaf.*;

public class BorderLayoutCG
    implements LayoutCG
{
    /**
     * TODO: documentation
     *
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
        throws IOException
    {
	SBorderLayout layout = (SBorderLayout)l;
	Map components = layout.getComponents();
	SContainer container = layout.getContainer();
	int border = layout.getBorder();

	SComponent north = (SComponent)components.get(SBorderLayout.NORTH);
	SComponent east = (SComponent)components.get(SBorderLayout.EAST);
	SComponent center = (SComponent)components.get(SBorderLayout.CENTER);
	SComponent west = (SComponent)components.get(SBorderLayout.WEST);
	SComponent south = (SComponent)components.get(SBorderLayout.SOUTH);
	
        int cols = 0;
        if (west != null) cols++;
        if (center != null) cols++;
        if (east != null) cols++;
        
        d.append("\n<table cellpadding=\"0\" cellspacing=\"0\"");
        CGUtil.writeSize( d, container );
        
        if ( Utils.hasSpanAttributes( container ) ) {
            d.append(" style=\"");
            Utils.writeSpanAttributes( d, (SComponent) container );
            d.append("\" ");
        }
        
        if (border > 0)
            d.append(" border=\"").append(border).append("\"");
        if (container != null && container.getBackground() != null) {
            d.append(" bgcolor=\"#")
                .append(Utils.toColorString(container.getBackground())).append("\">");
        }
        else
	    d.append(">");
        
        if (north != null) {
            d.append("\n<tr><td height=\"1\" colspan=\"").append(cols).append("\"");
            Utils.appendTableCellAlignment(d, north);
            d.append(">");
            writeComponent(d, north);
            d.append("</td></tr>");
        }
        d.append("\n<tr>");
        
        if (west != null) {
            d.append("<td width=\"1\"");
            Utils.appendTableCellAlignment(d, west);
            d.append(">");
            writeComponent(d, west);
            d.append("</td>");
        }
        
        if (center != null) {
            d.append("<td");
            Utils.appendTableCellAlignment(d, center);
            d.append(">");
            writeComponent(d, center);
            d.append("</td>");
        }
        
        if (east != null) {
            d.append("<td width=\"1\"");
            Utils.appendTableCellAlignment(d, east);
            d.append(">");
            writeComponent(d, east);
            d.append("</td>");
        }
        d.append("</tr>\n");
	
        if (south != null) {
            d.append("\n<tr><td height=\"1\" colspan=\"").append(cols).append("\"");
            Utils.appendTableCellAlignment(d, south);
            d.append(">");
            writeComponent(d, south);
            d.append("</td></tr>");
        }
        d.append("\n</table>");
    }
    
    protected void writeComponent(Device d, SComponent c)
	throws IOException
    {
        c.write(d);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
