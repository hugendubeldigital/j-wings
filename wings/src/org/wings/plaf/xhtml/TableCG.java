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

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.externalizer.ExternalizeManager;

public class TableCG
    implements org.wings.plaf.TableCG, SConstants
{
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
    private final static String selectionPropertyPrefix = "TableSelection";
    private final static String nonSelectionPropertyPrefix = "TableNonSelection";
    private final static String headerPropertyPrefix = "TableHeader";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent c) {
        STable table = (STable)c;
        c.setStyle(c.getSession().getCGManager().
                   getStyle(getPropertyPrefix() + ".style"));
        table.setCellRendererPane(new SCellRendererPane());
        installCellRenderer(table);
    }

    public void uninstallCG(SComponent c) {
        STable table = (STable)c;
        table.removeCellRendererPane();
        uninstallCellRenderer(table);
    }

    protected void installCellRenderer(STable table) {
        STableCellRenderer defaultRenderer = table.getDefaultRenderer();
        if (defaultRenderer == null
            || defaultRenderer instanceof SDefaultTableCellRenderer) {
            defaultRenderer = new SDefaultTableCellRenderer();
            configureDefaultRenderer(table, (SDefaultTableCellRenderer)defaultRenderer);
            table.setDefaultRenderer(defaultRenderer);
        }
        STableCellRenderer headerRenderer = table.getHeaderRenderer();
        if (headerRenderer == null
            || headerRenderer instanceof SDefaultTableCellRenderer) {
            headerRenderer = new SDefaultTableCellRenderer();
            configureHeaderRenderer(table, (SDefaultTableCellRenderer)headerRenderer);
            table.setHeaderRenderer(headerRenderer);
        }
    }

    protected void uninstallCellRenderer(STable table) {
        STableCellRenderer defaultRenderer = table.getDefaultRenderer();
        if (defaultRenderer != null
            && defaultRenderer instanceof SDefaultTableCellRenderer)
            table.setDefaultRenderer(null);
        STableCellRenderer headerRenderer = table.getHeaderRenderer();
        if (headerRenderer != null
            && headerRenderer instanceof SDefaultTableCellRenderer)
            table.setHeaderRenderer(null);
    }

    protected void configureDefaultRenderer(STable table, SDefaultTableCellRenderer cellRenderer) {
        CGManager cgManager = table.getSession().getCGManager();
        cellRenderer.setCellSelectionStyle(cgManager.getStyle(selectionPropertyPrefix + ".style"));
        cellRenderer.setCellNonSelectionStyle(cgManager.getStyle(nonSelectionPropertyPrefix + ".style"));
    }

    protected void configureHeaderRenderer(STable table, SDefaultTableCellRenderer cellRenderer) {
        CGManager cgManager = table.getSession().getCGManager();
        cellRenderer.setCellNonSelectionStyle(cgManager.getStyle(headerPropertyPrefix + ".style"));
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
        String width = table.getWidth();
        Insets borderLines = table.getBorderLines();
        boolean showHorizontalLines = table.getShowHorizontalLines();
        boolean showVerticalLines = table.getShowVerticalLines();
        Dimension intercellPadding = table.getIntercellPadding();
        Dimension intercellSpacing = table.getIntercellSpacing();

        d.append("<table");
        if (width != null)
            d.append(" width=\"").append(width).append("\"");

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

        if (thickness == 0 && showHorizontalLines || showVerticalLines)
            thickness = 1;

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

        if (intercellSpacing != null)
            d.append(" cellspacing=\"")
                .append(intercellSpacing.width)
                .append("\""); 

        if (intercellPadding != null)
            d.append(" cellpadding=\"")
                .append(intercellPadding.width)
                .append("\""); 

        d.append(">\n");
    }

    public void writePostfix(Device d, STable table)
        throws IOException
    {
        d.append("</table>\n");
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
            d.append("<tr>\n");
            for (int c = originCol; c < colCount; c++) {
                writeHeaderCell(d, table, rendererPane, c);
            }
            d.append("</tr>\n");
        }
        for (int r = originRow; r < rowCount; r++) {
            d.append("<tr>\n");
            for (int c = originCol; c < colCount; c++) {
                writeCell(d, table, rendererPane, r, c);
            }
            d.append("</tr>\n");
        }
    }

    protected void writeCell(Device d, STable table, SCellRendererPane rendererPane,
                             int row, int col)
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
        Utils.appendTableCellAttributes(d, comp);
        d.append(">");

        rendererPane.writeComponent(d, comp, table);
        d.append("</td>");
    }

    protected void writeHeaderCell(Device d, STable table, SCellRendererPane rendererPane,
                                   int c)
        throws IOException
    {
        if (c >= table.getModel().getColumnCount()
            && table.getSelectionMode() != SConstants.NO_SELECTION)
            d.append("<th>&nbsp;</th>");
        else {
            SComponent comp = table.prepareHeaderRenderer(c);
            d.append("<th>");
            rendererPane.writeComponent(d, comp, table);
            d.append("</th>");
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
