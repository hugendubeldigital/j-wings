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
import javax.swing.table.TableModel;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.externalizer.ExternalizeManager;

public class SortTableCG
    implements org.wings.plaf.SortTableCG, SConstants
{
    private final static String propertyPrefix = "SortTable";
    private final static String selectionPropertyPrefix = "TableSelection";
    private final static String nonSelectionPropertyPrefix = "TableNonSelection";
    private final static String headerPropertyPrefix = "SortTableHeader";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent c) {
        SSortTable sortTable = (SSortTable)c;
        c.setStyle(c.getSession().getCGManager().
                   getStyle(getPropertyPrefix() + ".style"));
        sortTable.setCellRendererPane(new SCellRendererPane());
        installCellRenderer(sortTable);
    }

    public void uninstallCG(SComponent c) {
        SSortTable sortTable = (SSortTable)c;
        sortTable.removeCellRendererPane();
        uninstallCellRenderer(sortTable);
    }

    protected void installCellRenderer(SSortTable sortTable) {
        STableCellRenderer defaultRenderer = sortTable.getDefaultRenderer();
        if (defaultRenderer == null
            || defaultRenderer instanceof SDefaultTableCellRenderer) {
            defaultRenderer = new SDefaultTableCellRenderer();
            configureDefaultRenderer(sortTable, (SDefaultTableCellRenderer)defaultRenderer);
            sortTable.setDefaultRenderer(defaultRenderer);
        }
        STableCellRenderer headerRenderer = sortTable.getHeaderRenderer();
        if (headerRenderer == null
            || headerRenderer instanceof SDefaultTableCellRenderer) {
            headerRenderer = new SDefaultTableCellRenderer();
            configureHeaderRenderer(sortTable, (SDefaultTableCellRenderer)headerRenderer);
            sortTable.setHeaderRenderer(headerRenderer);
        }
    }

    protected void uninstallCellRenderer(SSortTable sortTable) {
        STableCellRenderer defaultRenderer = sortTable.getDefaultRenderer();
        if (defaultRenderer != null
            && defaultRenderer instanceof SDefaultTableCellRenderer)
            sortTable.setDefaultRenderer(null);
        STableCellRenderer headerRenderer = sortTable.getHeaderRenderer();
        if (headerRenderer != null
            && headerRenderer instanceof SDefaultTableCellRenderer)
            sortTable.setHeaderRenderer(null);
    }

    protected void configureDefaultRenderer(SSortTable sortTable, SDefaultTableCellRenderer cellRenderer) {
        CGManager cgManager = sortTable.getSession().getCGManager();
        cellRenderer.setCellSelectionStyle(cgManager.getStyle(selectionPropertyPrefix + ".style"));
        cellRenderer.setCellNonSelectionStyle(cgManager.getStyle(nonSelectionPropertyPrefix + ".style"));
    }

    protected void configureHeaderRenderer(SSortTable sortTable, SDefaultTableCellRenderer cellRenderer) {
        CGManager cgManager = sortTable.getSession().getCGManager();
        cellRenderer.setCellNonSelectionStyle(cgManager.getStyle(headerPropertyPrefix + ".style"));
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SSortTable sortTable = (SSortTable)c;

        writePrefix(d, sortTable);
        writeBody(d, sortTable);
        writePostfix(d, sortTable);
    }

    public void writePrefix(Device d, SSortTable sortTable)
        throws IOException
    {
        String width = sortTable.getWidth();
        Insets borderLines = sortTable.getBorderLines();
        boolean showHorizontalLines = sortTable.getShowHorizontalLines();
        boolean showVerticalLines = sortTable.getShowVerticalLines();
        Dimension intercellPadding = sortTable.getIntercellPadding();
        Dimension intercellSpacing = sortTable.getIntercellSpacing();

        d.append("<table");
        if (width != null && width.trim().length() > 0)
            d.append(" width=\"").append(width).append("\"");
        d.append(">\n");
    }

    public void writePostfix(Device d, SSortTable sortTable)
        throws IOException
    {
        d.append("</table>\n");
    }

    public void writeBody(Device d, SSortTable sortTable)
        throws IOException
    {
        int originRow = 0;
        int originCol = 0;
        int rowCount = sortTable.getRowCount();
        int colCount = sortTable.getColumnCount();
        Rectangle viewport = sortTable.getViewportSize();
        if (viewport != null) {
            originRow = viewport.y;
            originCol = viewport.x;
            rowCount = viewport.height;
            colCount = viewport.width;
        }

        SCellRendererPane rendererPane = sortTable.getCellRendererPane();
        if (sortTable.isHeaderVisible()) {
            d.append("<tr>\n");
            for (int c = originCol; c < colCount; c++) {
                writeHeaderCell(d, sortTable, rendererPane, c);
            }
            d.append("</tr>\n");
        }
        for (int r = originRow; r < rowCount; r++) {
            d.append("<tr>\n");
            for (int c = originCol; c < colCount; c++) {
                writeCell(d, sortTable, rendererPane, r, c);
            }
            d.append("</tr>\n");
        }
    }

    protected void writeCell(Device d, SSortTable sortTable, SCellRendererPane rendererPane,
                             int row, int col)
        throws IOException
    {
        sortTable.checkSelectables();

        SComponent comp = null;
        boolean isEditingCell = sortTable.isEditing()
            && row == sortTable.getEditingRow()
            && col == sortTable.getEditingColumn();

        if (isEditingCell)
            comp = sortTable.getEditorComponent();
        else
            comp = sortTable.prepareRenderer(sortTable.getCellRenderer(row, col), row, col);

        d.append("<td>");
        rendererPane.writeComponent(d, comp, sortTable);
        d.append("</td>");
    }

    protected void writeHeaderCell(Device d, SSortTable sortTable,
                                   SCellRendererPane rendererPane,
                                   int c)
        throws IOException
    {
        TableModel model = sortTable.getModel();

        if (c >= model.getColumnCount()
            && sortTable.getSelectionMode() != SConstants.NO_SELECTION)
            d.append("<th>&nbsp;</th>");
        else {
            SComponent comp = sortTable.prepareHeaderRenderer(c);

            d.append("<th>");
            if (model instanceof TableSorter) {
                d.append("<table><tr><th>");
                rendererPane.writeComponent(d, comp, sortTable);
                d.append("</th><th>");

                d.append("<a href=\"").
                    append(sortTable.getServerAddress()
                           .add(sortTable.getNamePrefix()+"=u"+c)).
                    append("\">");
                try {
                    if (SSortTable.DEFAULT_SORT_UP != null) {
                        ExternalizeManager ext = sortTable.getExternalizeManager();
                        d.append("<img src=\"")
                            .append(ext.externalize(SSortTable.DEFAULT_SORT_UP)).
                            append("\" border=\"0\">");
                    } else
                        d.append("u&nbsp;");
                } catch (java.io.IOException e) {
                    d.append("u&nbsp;");
                }
                d.append("</a>");

                d.append("<a href=\"").
                    append(sortTable.getServerAddress()
                           .add(sortTable.getNamePrefix()+"=d"+c)).
                    append("\">");
                try {
                    if (SSortTable.DEFAULT_SORT_DOWN != null) {
                        ExternalizeManager ext = sortTable.getExternalizeManager();
                        d.append("<img src=\"")
                            .append(ext.externalize(SSortTable.DEFAULT_SORT_DOWN)).
                            append("\" border\"=0\">");
                    } else
                        d.append("d&nbsp;");

                } catch (java.io.IOException e) {
                    d.append("d&nbsp;");
                }
                d.append("</a>");
                d.append("</th></tr></table>");
            }
            else
                rendererPane.writeComponent(d, comp, sortTable);

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
