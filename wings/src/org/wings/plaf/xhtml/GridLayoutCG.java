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

import org.wings.*; 
import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.util.CGUtil;

public class GridLayoutCG
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
        SGridLayout layout = (SGridLayout)l;
        SContainer container = layout.getContainer();
        List components = layout.getComponents();
        SDimension dim = layout.getPreferredSize();
      
        boolean header = layout.getHeader();
        boolean relative = layout.isRelative();
        int width = layout.getWidth();
        int cellSpacing = layout.getCellSpacing();
        int cellPadding = layout.getCellPadding();
        int border = layout.getBorder();

        int cols = layout.getColumns();
        int rows = layout.getRows();
        
        d.print("\n<table ");
        if (width >= 0 || Utils.hasSpanAttributes( container ) ) {
            d.print("style=\"");
            if (width >= 0) {
              if ((dim == null) || (dim.getWidth() == null)) {
                d.print("width:").print(width);
                if (relative) d.print("%");
                d.print(";");
              }
            } 
            Utils.writeSpanAttributes( d, (SComponent) container );
            d.print("\" ");
        }

        if (cellSpacing >= 0)
            d.print(" cellspacing=\"").print(cellSpacing).print("\"");
        else
            d.print(" cellspacing=\"0\"");

        if (cellPadding >= 0)
            d.print(" cellpadding=\"").print(cellPadding).print("\"");
        else
            d.print(" cellpadding=\"0\"");
        
        // CGUtil.writeSize( d, container );

        if (border > 0)
            d.print(" border=\"").print(border).print("\"");
        else
            d.print(" border=\"0\"");

       /* if (container != null && container.getBackground() != null)
            d.print(" bgcolor=\"#").
                print(Utils.toColorString(container.getBackground())).print("\"");
*/
        d.print(">\n");

        if (cols <= 0)
            cols = components.size() / rows;

        boolean firstRow = true;

        int col = 0;
        for (Iterator iter = components.iterator(); iter.hasNext();) {
            if (col == 0)
                d.print("<tr>");
            else if (col%cols == 0 && iter.hasNext()) {
                d.print("</tr>\n<tr>");
                firstRow = false;
            }

            SComponent c = (SComponent)iter.next();

            if (firstRow && header) 
                d.print("<th");          
            else 
                d.print("<td");

            Utils.printTableCellAlignment(d, c);
            if (c instanceof SContainer && c.isVisible() && Utils.hasSpanAttributes(c)) {
               // Adapt inner styles (esp. width of containers)
               // maybe better restrict to dimension styles only?
               d.print(" style=\"");
               Utils.writeAttributes(d,  c);
               d.print("\"");                        
                       
               // Some containers (like SPanel) do not support 
               // background colors, hence we render the background 
               // of them using this surrounding gridlayout cell
               // Utils.printTableCellColors(d, c);
               
            }
            d.print(">");            

            c.write(d);

            if (firstRow && header)
                d.print("</th>");
            else
                d.print("</td>");

            col++;

            if (!iter.hasNext())
                d.print("</tr>\n");
        }

        d.print("</table>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
