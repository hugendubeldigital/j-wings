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
import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.externalizer.ExternalizeManager;

public class TableCG
    implements org.wings.plaf.TableCG, SConstants
{
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
        boolean showGrid = table.getShowGrid();
        boolean showHorizontalLines = table.getShowHorizontalLines();
        boolean showVerticalLines = table.getShowVerticalLines();
        Dimension intercellPadding = table.getIntercellPadding();
        Dimension intercellSpacing = table.getIntercellSpacing();

        d.append("<table");
        if (width != null && width.trim().length() > 0)
            d.append(" width=\"").append(width).append("\"");

        if ( borderLines!=null ) {
            d.append("border frame=");
            if ( borderLines.top>0 || borderLines.bottom>0 ) {
                if ( borderLines.bottom<=0 )
                    d.append("\"above\" ");
                else if ( borderLines.top<=0 ) 
                    d.append("\"below\" ");
                else if ( borderLines.left>0 && borderLines.right>0 )
                    d.append("\"box\" ");
                else 
                    d.append("\"hsides\" ");
            } else if ( borderLines.left>0 || borderLines.right>0 )
                if ( borderLines.left<=0 ) 
                    d.append("\"rhs\" ");
                else if ( borderLines.right<=0 ) 
                    d.append("\"lhs\" ");
                else
                    d.append("\"vsides\" ");
        }


        if ( intercellSpacing!=null )
            d.append("cellspacing=").append(intercellSpacing.width).append(" "); 

        if ( intercellPadding!=null )
            d.append("cellpadding=").append(intercellPadding.width).append(" "); 

        if ( showGrid ) {
            if ( borderLines==null )
                d.append("border frame=\"box\" ");
            if ( showHorizontalLines ) 
                d.append("rules=\"rows\" ");
            else if ( showVerticalLines ) 
                d.append("rules=\"cols\" ");
            else 
                d.append("rules=\"all\" ");
        } else
            d.append("rules=\"none\" border=0");
    
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
