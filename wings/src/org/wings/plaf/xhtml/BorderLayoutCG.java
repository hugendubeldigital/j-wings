/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://j-wings.org).
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
        
        d.print("\n<table cellpadding=\"0\" cellspacing=\"0\"");
        // CGUtil.writeSize( d, container );
        
        if ( Utils.hasSpanAttributes( container ) ) {
            d.print(" style=\"");
            Utils.writeSpanAttributes( d, (SComponent) container );
            d.print("\" ");
        }
        
        if (border > 0)
            d.print(" border=\"").print(border).print("\"");
/*        if (container != null && container.getBackground() != null) {
            d.print(" bgcolor=\"#")
                .print(Utils.toColorString(container.getBackground())).print("\"");
        }
*/        
	    d.print(">");
        
        if (north != null) {
            d.print("\n<tr><td height=\"1\" colspan=\"").print(cols).print("\"");
            Utils.printTableCellAlignment(d, north);
            d.print(">");
            writeComponent(d, north);
            d.print("</td></tr>");
        }
        d.print("\n<tr>");
        
        if (west != null) {
            d.print("<td width=\"1\"");
            Utils.printTableCellAlignment(d, west);
            d.print(">");
            writeComponent(d, west);
            d.print("</td>");
        }
        
        if (center != null) {
            d.print("<td");
            Utils.printTableCellAlignment(d, center);
            d.print(">");
            writeComponent(d, center);
            d.print("</td>");
        }
        
        if (east != null) {
            d.print("<td width=\"1\"");
            Utils.printTableCellAlignment(d, east);
            d.print(">");
            writeComponent(d, east);
            d.print("</td>");
        }
        d.print("</tr>\n");
	
        if (south != null) {
            d.print("\n<tr><td height=\"1\" colspan=\"").print(cols).print("\"");
            Utils.printTableCellAlignment(d, south);
            d.print(">");
            writeComponent(d, south);
            d.print("</td></tr>");
        }
        d.print("\n</table>");
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
