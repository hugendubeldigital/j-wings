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

public final class TableCG
    extends org.wings.plaf.xhtml.TableCG
{
    public void writePrefix(Device d, STable table)
        throws IOException
    {
        String width = table.getWidth();
        Insets borderLines = table.getBorderLines();
        boolean showHorizontalLines = table.getShowHorizontalLines();
        boolean showVerticalLines = table.getShowVerticalLines();
        SDimension intercellPadding = table.getIntercellPadding();
        SDimension intercellSpacing = table.getIntercellSpacing();
        Style style = table.getStyle();

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


        Utils.writeStyleAttribute(d, style);
        d.append(">\n");
    }

    public void writeBody(Device d, STable table)
        throws IOException
    {
        Style style = table.getStyle();

        int originRow = 0;
        int originCol = 0;
        int rowCount = table.getRowCount();
        int colCount = table.getColumnCount();
        Rectangle viewport = table.getViewportSize();
        if (viewport != null) {
            originRow = viewport.y;
            originCol = viewport.x;
            rowCount = viewport.height;
            colCount = viewport.width;
        }

        SCellRendererPane rendererPane = table.getCellRendererPane();
        if (table.isHeaderVisible()) {
            d.append("<tr");
            Utils.writeStyleAttribute(d, "", style, "header");
            d.append(">\n");
            for (int c = originCol; c < colCount; c++) {
                writeHeaderCell(d, table, rendererPane, c);
            }
            d.append("</tr>\n");
        }
        for (int r = originRow; r < rowCount; r++) {
            d.append("<tr");
            if (table.isRowSelected(r))
                Utils.writeStyleAttribute(d, "", style, "selection");
            else
                Utils.writeStyleAttribute(d, "", style, "nonselection");
            d.append(">\n");
            for (int c = originCol; c < colCount; c++) {
                writeCell(d, table, rendererPane, r, c);
            }
            d.append("</tr>\n");
        }
    }

    protected void writeCell(Device d, STable table, int row, int col)
        throws IOException
    {
        table.checkSelectables();

        SComponent comp = null;
        boolean isEditingCell = table.isEditing()
            && row == table.getEditingRow()
            && col == table.getEditingColumn();

        if (isEditingCell)
            comp = table.getEditorComponent();
        else
            comp = table.prepareRenderer(table.getCellRenderer(row, col), row, col);

        d.append("<td");
        Utils.writeStyleAttribute(d, comp.getStyle());
        d.append(">");
        comp.write(d);
        d.append("</td>");
    }

    protected void writeHeaderCell(Device d, STable table, int c)
        throws IOException
    {
        if (c >= table.getModel().getColumnCount()
            && table.getSelectionMode() != SConstants.NO_SELECTION)
            d.append("<th>&nbsp;</th>");
        else {
            SComponent comp = table.prepareHeaderRenderer(c);

            d.append("<th");
            Utils.writeStyleAttribute(d, comp.getStyle());
            d.append(">");
            comp.write(d);
            d.append("</th>");
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
