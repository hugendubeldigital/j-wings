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
import javax.swing.ListSelectionModel;

import org.wings.*;
import org.wings.border.*;
import org.wings.externalizer.ExternalizeManager;
import org.wings.io.*;
import org.wings.util.CGUtil;
import org.wings.util.AnchorRenderStack;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.*;

public final class TableCG
    extends org.wings.plaf.xhtml.TableCG
{

    private Style style;
    public void setStyle(Style style) {
        this.style = style;
    }
    public Style getStyle() { return style; }

    public void writePrefix(Device d, STable table)
        throws IOException
    {
        Insets borderLines = table.getBorderLines();
        boolean showHorizontalLines = table.getShowHorizontalLines();
        boolean showVerticalLines = table.getShowVerticalLines();
        SDimension intercellPadding = table.getIntercellPadding();
        SDimension intercellSpacing = table.getIntercellSpacing();

        d.print("<table");
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

                d.print(" frame=\"")
                    .print(border)
                    .print("\"");

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
            d.print(" rules=\"all\"");
        else if (showVerticalLines) 
            d.print(" rules=\"cols\"");
        else if (showHorizontalLines)
            d.print(" rules=\"rows\"");
        else
            d.print(" rules=\"none\"");

        if (thickness > 0)
            d.print(" border=\"")
                .print(thickness)
                .print("\"");

        if (intercellSpacing != null && intercellSpacing.width != null)
            d.print(" cellspacing=\"")
                .print(intercellSpacing.width)
                .print("\""); 

        if (intercellPadding != null && intercellPadding.width != null)
            d.print(" cellpadding=\"")
                .print(intercellPadding.width)
                .print("\""); 

        if (style != null)
            style.write(d);

        d.print(">\n");
    }

    public void writeBody(Device d, STable table)
        throws IOException
    {
        Style style = table.getStyle();

        int startRow = 0;
        int startCol = 0;
        int endRow = table.getRowCount();
        int endCol = table.getColumnCount();
        Rectangle viewport = table.getViewportSize();
        if (viewport != null) {
            startRow = viewport.y;
            startCol = viewport.x;
            endRow = startRow+viewport.height;
            endCol = startCol+viewport.width;
        }

        SCellRendererPane rendererPane = table.getCellRendererPane();
        if (table.isHeaderVisible()) {
            d.print("<tr>\n");

            boolean selectionWritten = table.getRowSelectionColumn()<0;
            for (int c = startCol; c < endCol; c++) {

                if ( !selectionWritten && 
                     c>=table.getRowSelectionColumn() ) {
                    writeEmptyHeaderCell(table, d);
                    selectionWritten = true;
                }

                writeHeaderCell(d, table, rendererPane, c);
            }
            
            if ( !selectionWritten ) {
                writeEmptyHeaderCell(table, d);
            }

            d.print("</tr>\n");
        }

        for (int r = startRow; r < endRow; r++) {
            d.print("<tr ");

            Style rowStyle = table.isRowSelected(r) ? 
                table.getSelectionStyle() : table.getStyle();
            
            if (rowStyle != null)
                d.print(" class=\"").print(rowStyle.getName()).print("\"");

            d.print(">\n");

            boolean selectionWritten = table.getRowSelectionColumn()<0;

            for (int c = startCol; c < endCol; c++) {

                if ( !selectionWritten && 
                     c>=table.getRowSelectionColumn() ) {
                    writeRowSelection(d, table, rendererPane, r, c);
                    selectionWritten = true;
                }

                writeCell(d, table, rendererPane, r, c);
            }

            if ( !selectionWritten ) {
                writeRowSelection(d, table, rendererPane, r, endCol);
            }

            d.print("</tr>\n");
        }
    }

    protected void writeCell(Device d, STable table,
                             SCellRendererPane rendererPane, int row, int col)
        throws IOException
    {
        SComponent comp = null;
        boolean isEditingCell = table.isEditing()
            && row == table.getEditingRow()
            && col == table.getEditingColumn();

        if (isEditingCell)
            comp = table.getEditorComponent();
        else
            comp = table.prepareRenderer(table.getCellRenderer(row, col), row, col);

        d.print("<td>");

        boolean pushedURL = false;
        if (!isEditingCell && table.isCellEditable(row, col)) {
            RequestURL editAddr = table.getRequestURL();
            editAddr.addParameter(table.getNamePrefix() + "=" + 
                                  table.getEditParameter(row, col));

            if ( comp instanceof ClickableRenderComponent ) {
                AnchorRenderStack.push(editAddr, null);
                pushedURL = true;
            } 
            else {
                d.print("<a href=\"").print(editAddr.toString()).
                    print("\">");
                org.wings.plaf.xhtml.Utils.printIcon(d, editIcon, null);
                d.print("</a>&nbsp;");
            }
        }

        rendererPane.writeComponent(d, comp, table);

        if (pushedURL) {
            AnchorRenderStack.pop();
        }
        
        d.print("</td>");
    }

    protected void writeRowSelection(Device d, STable table, 
                                     SCellRendererPane rendererPane,
                                     int row, int col)
        throws IOException
    {
        STableCellRenderer rowSelectionRenderer =
            table.getRowSelectionRenderer();

        if ( rowSelectionRenderer==null ) {
            if ( table.getResidesInForm() ) {
                writeDefaultFormRowSelection(d, table, rendererPane, row);
                return;
            }
            rowSelectionRenderer = DEFAULT_ROW_SELECTION_RENDERER;
        }

        SComponent comp =  
            rowSelectionRenderer.getTableCellRendererComponent(table,
                                                               table.getSelectionToggleParameter(row, -1),
                                                               table.isRowSelected(row),
                                                               row, -1);

        d.print("<td>");

        RequestURL toggleSelectionAddr = table.getRequestURL();
        toggleSelectionAddr.addParameter(table.getNamePrefix() + "=" + 
                                         table.getSelectionToggleParameter(row,col));

        if ( comp instanceof ClickableRenderComponent ) {
            AnchorRenderStack.push(toggleSelectionAddr, null);
        } else {
            d.print("<a href=\"").
                print(toggleSelectionAddr.toString()).print("\">");
        }
            
        rendererPane.writeComponent(d, comp, table);

        if ( comp instanceof ClickableRenderComponent ) {
            AnchorRenderStack.pop();
        } else {
            d.print("</a>");
        }

        d.print("</td>");
    }

    protected void writeHeaderCell(Device d, STable table,
                                   SCellRendererPane rendererPane,
                                   int c)
        throws IOException
    {
        SComponent comp = table.prepareHeaderRenderer(c);
        
        Style headerStyle = table.getHeaderStyle();
        d.print("<th");
        if (headerStyle != null)
            d.print(" class=\"").print(headerStyle.getName()).print("\"");
        d.print(">");
        rendererPane.writeComponent(d, comp, table);
        d.print("</th>");
    }

    protected void writeEmptyHeaderCell(STable table, Device d)
        throws IOException
    {
        Style headerStyle = table.getHeaderStyle();
        d.print("<th");
        if (headerStyle != null)
            d.print(" class=\"").print(headerStyle.getName()).print("\"");
        d.print(">&nbsp;</th>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
