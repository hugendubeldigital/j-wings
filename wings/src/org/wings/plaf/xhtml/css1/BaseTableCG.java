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

package org.wings.plaf.xhtml.css1;

import java.awt.*;
import java.io.IOException;
import java.util.*;

import org.wings.*;
import org.wings.externalizer.ExternalizeManager;
import org.wings.io.*;
import org.wings.util.CGUtil;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.*;

public final class BaseTableCG
    extends org.wings.plaf.xhtml.BaseTableCG
{
    private Style style;
    public void setStyle(Style style) {
        this.style = style;
    }
    public Style getStyle() { return style; }

    public void writePrefix(Device d, SBaseTable table)
        throws IOException
    {
        String width = table.getWidth();
        Insets borderLines = table.getBorderLines();
        boolean showHorizontalLines = table.getShowHorizontalLines();
        boolean showVerticalLines = table.getShowVerticalLines();
        SDimension intercellPadding = table.getIntercellPadding();
        SDimension intercellSpacing = table.getIntercellSpacing();

        d.append("<table");
        CGUtil.writeSize( d, table );

        int thickness = 0;
        if (borderLines != null) {
            int lines
                = ((borderLines.left   > 0) ? LEFT   : 0)
                + ((borderLines.right  > 0) ? RIGHT  : 0)
                + ((borderLines.top    > 0) ? TOP    : 0)
                + ((borderLines.bottom > 0) ? BOTTOM : 0);

            if (lines != 0) {
                String border = (String)frameMap.get(new Byte((byte)lines));
                if (border == null)
                    border = "box";

                d.append(" frame=\"")
                    .append(border)
                    .append("\"");

                if (borderLines.top > 0)
                    thickness = borderLines.top;
                else if (borderLines.bottom > 0)
                    thickness = borderLines.bottom;
                else if (borderLines.left > 0)
                    thickness = borderLines.left;
                else
                    thickness = borderLines.right;
            }
        }

        //if (thickness == 0 && showHorizontalLines || showVerticalLines)
        //    thickness = 1;

        if (showHorizontalLines && showVerticalLines)
            d.append(" rules=\"all\"");
        else if (showVerticalLines) 
            d.append(" rules=\"cols\"");
        else if (showHorizontalLines)
            d.append(" rules=\"rows\"");
        else
            d.append(" rules=\"none\"");

        if (thickness > 0)
            d.append(" border=\"")
                .append(thickness)
                .append("\"");

        if (intercellSpacing != null && intercellSpacing.width != null)
            d.append(" cellspacing=\"")
                .append(intercellSpacing.width)
                .append("\""); 

        if (intercellPadding != null && intercellPadding.width != null)
            d.append(" cellpadding=\"")
                .append(intercellPadding.width)
                .append("\""); 

        if (style != null)
            style.write(d);

        d.append(">\n");
    }

    public void writeBody(Device d, SBaseTable baseTable)
        throws IOException
    {
        Style style = baseTable.getStyle();

        int originRow = 0;
        int originCol = 0;
        int rowCount = baseTable.getRowCount();
        int colCount = baseTable.getColumnCount();
        Rectangle viewport = baseTable.getViewportSize();
        if (viewport != null) {
            originRow = viewport.y;
            originCol = viewport.x;
            rowCount = viewport.height;
            colCount = viewport.width;
        }

        SCellRendererPane rendererPane = baseTable.getCellRendererPane();
        if (baseTable.isHeaderVisible()) {
            d.append("<tr>\n");
            for (int c = originCol; c < colCount; c++) {
                writeHeaderCell(d, baseTable, rendererPane, c);
            }
            d.append("</tr>\n");
        }



        for (int r = originRow; r < rowCount; r++) {
            d.append("<tr>\n");
            for (int c = originCol; c < colCount; c++) {
                writeCell(d, baseTable, rendererPane, r, c);
            }
            d.append("</tr>\n");
        }
    }

    protected void writeCell(Device d, SBaseTable baseTable,
                             SCellRendererPane rendererPane, int row, int col)
        throws IOException
    {
        SComponent comp = baseTable.prepareRenderer(baseTable.getCellRenderer(row, col), row, col);
        Style style = baseTable.getStyle();

        d.append("<td");
        if (style != null)
            style.write(d);
        d.append(">");

        rendererPane.writeComponent(d, comp, baseTable);
        d.append("</td>");
    }

    protected void writeHeaderCell(Device d, SBaseTable baseTable,
                                   SCellRendererPane rendererPane, int c)
        throws IOException
    {
        SComponent comp = baseTable.prepareHeaderRenderer(c);
        Style style = baseTable.getHeaderStyle();

        d.append("<th");
        if (style != null)
            style.write(d);
        d.append(">");

        rendererPane.writeComponent(d, comp, baseTable);
        d.append("</th>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
