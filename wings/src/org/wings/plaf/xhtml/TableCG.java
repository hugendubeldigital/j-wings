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

import java.awt.*;
import java.io.IOException;
import java.util.*;
import javax.swing.Icon;
import javax.swing.ListSelectionModel;

import org.wings.*;
import org.wings.io.*;
import org.wings.util.CGUtil;
import org.wings.plaf.*;
import org.wings.externalizer.ExternalizeManager;

public class TableCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.TableCG, SConstants
{

    protected static final SIcon EDIT_ICON = 
        new ResourceImageIcon("org/wings/icons/Pencil.gif");

    protected static final STableCellRenderer DEFAULT_ROW_SELECTION_RENDERER = 
        new SDefaultTableRowSelectionRenderer();

    protected static final byte LEFT   = 1;
    protected static final byte RIGHT  = 2;
    protected static final byte TOP    = 4;
    protected static final byte BOTTOM = 8;

    protected static final byte VOID   = 0;
    protected static final byte VSIDES = LEFT + RIGHT;
    protected static final byte HSIDES = TOP + BOTTOM;
    protected static final byte BOX    = VSIDES + HSIDES;

    protected static final Map frameMap = new HashMap(8);
    static {
        frameMap.put(new Byte(LEFT),   "lhs");
        frameMap.put(new Byte(RIGHT),  "rhs");
        frameMap.put(new Byte(TOP),    "above");
        frameMap.put(new Byte(BOTTOM), "below");
        frameMap.put(new Byte(VOID),   "void");
        frameMap.put(new Byte(VSIDES), "vsides");
        frameMap.put(new Byte(HSIDES), "hsides");
        frameMap.put(new Byte(BOX),    "box");
    }

    private final static String propertyPrefix = "Table";

    protected SIcon editIcon = EDIT_ICON;

    public void setEditIcon(SIcon i) {
        editIcon = i;
    }

    public SIcon getEditIcon() {
        return editIcon;
    }

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        STable table = (STable)c;

        writePrefix(d, table);
        writeBody(d, table);
        writePostfix(d, table);
    }

    public void writePrefix(Device d, STable table)
        throws IOException
    {
        Insets borderLines = table.getBorderLines();
        boolean showHorizontalLines = table.getShowHorizontalLines();
        boolean showVerticalLines = table.getShowVerticalLines();
        SDimension intercellPadding = table.getIntercellPadding();
        SDimension intercellSpacing = table.getIntercellSpacing();

        d.print("<table");
        CGUtil.writeSize(d, table);

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

        if (thickness == 0 && showHorizontalLines || showVerticalLines)
            thickness = 1;

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

        d.print(">\n");
    }

    public void writePostfix(Device d, STable table)
        throws IOException
    {
        d.print("</table>\n");
    }

    public void writeBody(Device d, STable table)
        throws IOException
    {
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
            d.print("<tr>\n");

            boolean selectionWritten = table.getRowSelectionColumn()<0;
            for (int c = originCol; c < colCount; c++) {

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


        for (int r = originRow; r < rowCount; r++) {
            d.print("<tr>\n");

            boolean selectionWritten = table.getRowSelectionColumn()<0;

            for (int c = originCol; c < colCount; c++) {

                if ( !selectionWritten && 
                     c>=table.getRowSelectionColumn() ) {
                    writeRowSelection(d, table, rendererPane, r);
                    selectionWritten = true;
                }

                writeCell(d, table, rendererPane, r, c);
            }

            if ( !selectionWritten ) {
                writeRowSelection(d, table, rendererPane, r);
            }

            d.print("</tr>\n");
        }

    }

    protected void writeCell(Device d, STable table, 
                             SCellRendererPane rendererPane,
                             int row, int col)
        throws IOException
    {
        SComponent comp = null;
        boolean isEditingCell = table.isEditing()
            && row == table.getEditingRow()
            && col == table.getEditingColumn();

        if (isEditingCell)
            comp = table.getEditorComponent();
        else
            comp = table.prepareRenderer(table.getCellRenderer(row, col), 
                                         row, col);

        d.print("<td");
        Utils.appendTableCellAttributes(d, comp);
        d.print(">");

        if ( !isEditingCell && table.isCellEditable(row, col) ) {
            RequestURL editAddr = table.getRequestURL();
            editAddr.addParameter(table.getNamePrefix() + "=" + 
                                  table.getEditParameter(row, col));
            
            if ( comp instanceof ClickableRenderComponent ) {
                ((ClickableRenderComponent)comp).setEventParam(editAddr.toString());
            } else {
                d.print("<a href=\"").print(editAddr.toString()).
                    print("\">");
                Utils.appendIcon(d, editIcon, null);
                d.print("</a>&nbsp;");
            }
        }

        rendererPane.writeComponent(d, comp, table);

        if ( comp instanceof ClickableRenderComponent ) {
            ((ClickableRenderComponent)comp).setEventParam(null);
        }
        d.print("</td>");
    }

    protected void writeDefaultFormRowSelection(Device d, STable table, 
                                                SCellRendererPane rendererPane,
                                                int row)
        throws IOException
    {

        d.print("<td>");

        d.print("<input type=\"hidden\"");
        d.print(" name=\"").print(table.getNamePrefix()).print("\"").
            print(" value=\"").print(table.getDeselectParameter(row,-1)).print("\"");
        d.print(">");


        d.print("<input type=\"");

        switch ( table.getSelectionMode() ) {
        case ListSelectionModel.SINGLE_SELECTION:
            d.print("radio");
            break;
        default:
            d.print("checkbox");
            break;
        } 

        d.print("\" ").
            print("name=\"").print(table.getNamePrefix()).print("\"").
            print("value=\"").print(table.getSelectionToggleParameter(row,-1)).print("\"");

        if ( table.isRowSelected(row) )
            d.print(" checked=\"checked\"");

        d.print(">");
        d.print("</td>");
    }

    protected void writeRowSelection(Device d, STable table, 
                                     SCellRendererPane rendererPane,
                                     int row)
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
        d.print("<td");
        Utils.appendTableCellAttributes(d, comp);
        d.print(">");

        rendererPane.writeComponent(d, comp, table);

        d.print("</td>");
    }

    protected void writeHeaderCell(Device d, STable table, 
                                   SCellRendererPane rendererPane, int col)
        throws IOException
    {


        SComponent comp = table.prepareHeaderRenderer(col);
        d.print("<th>");
        rendererPane.writeComponent(d, comp, table);
        d.print("</th>");
    }

    protected void writeEmptyHeaderCell(STable table, Device d)
        throws IOException
    {
        d.print("<th>&nbsp;</th>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
