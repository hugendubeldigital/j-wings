package org.wings.plaf.xhtml;

import java.awt.*;
import java.io.IOException;
import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.externalizer.ExternalizeManager;


public class BaseTableCG implements org.wings.plaf.BaseTableCG, SConstants
{
    private final static String propertyPrefix = "BaseTable.";
    private final static String headerPropertyPrefix = "BaseTableHeader.";
    private final static String cellPropertyPrefix = "BaseTableCell.";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent c) {
        SBaseTable baseTable = (SBaseTable)c;
        c.setStyle(c.getSession().getCGManager().getStyle(propertyPrefix + "style"));
        baseTable.setCellRendererPane(new SCellRendererPane());
        installCellRenderer(baseTable);
    }

    public void uninstallCG(SComponent c) {
        SBaseTable baseTable = (SBaseTable)c;
        baseTable.removeCellRendererPane();
        uninstallCellRenderer(baseTable);
    }

    protected void installCellRenderer(SBaseTable baseTable) {
        STableCellRenderer defaultRenderer = baseTable.getDefaultRenderer();
        if (defaultRenderer == null
            || defaultRenderer instanceof SDefaultTableCellRenderer) {
            defaultRenderer = new SDefaultTableCellRenderer();
            configureDefaultRenderer(baseTable, (SDefaultTableCellRenderer)defaultRenderer);
            baseTable.setDefaultRenderer(defaultRenderer);
        }
        STableCellRenderer headerRenderer = baseTable.getHeaderRenderer();
        if (headerRenderer == null
            || headerRenderer instanceof SDefaultTableCellRenderer) {
            headerRenderer = new SDefaultTableCellRenderer();
            configureHeaderRenderer(baseTable, (SDefaultTableCellRenderer)headerRenderer);
            baseTable.setHeaderRenderer(headerRenderer);
        }
    }
    protected void uninstallCellRenderer(SBaseTable baseTable) {
        STableCellRenderer defaultRenderer = baseTable.getDefaultRenderer();
        if (defaultRenderer != null
            && defaultRenderer instanceof SDefaultTableCellRenderer)
            baseTable.setDefaultRenderer(null);
        STableCellRenderer headerRenderer = baseTable.getHeaderRenderer();
        if (headerRenderer != null
            && headerRenderer instanceof SDefaultTableCellRenderer)
            baseTable.setHeaderRenderer(null);
    }

    protected void configureDefaultRenderer(SBaseTable baseTable, SDefaultTableCellRenderer cellRenderer) {
        CGManager cgManager = baseTable.getSession().getCGManager();
        cellRenderer.setCellNonSelectionStyle(cgManager.getStyle(cellPropertyPrefix + "style"));
    }

    protected void configureHeaderRenderer(SBaseTable baseTable, SDefaultTableCellRenderer cellRenderer) {
        CGManager cgManager = baseTable.getSession().getCGManager();
        cellRenderer.setCellNonSelectionStyle(cgManager.getStyle(headerPropertyPrefix + "style"));
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SBaseTable baseTable = (SBaseTable)c;

        writePrefix(d, baseTable);
        writeBody(d, baseTable);
        writePostfix(d, baseTable);
    }

    public void writePrefix(Device d, SBaseTable baseTable)
        throws IOException
    {
        String width = baseTable.getWidth();
        Insets borderLines = baseTable.getBorderLines();
        boolean showHorizontalLines = baseTable.getShowHorizontalLines();
        boolean showVerticalLines = baseTable.getShowVerticalLines();
        Dimension intercellPadding = baseTable.getIntercellPadding();
        Dimension intercellSpacing = baseTable.getIntercellSpacing();

        d.append("<table");
        if (width != null && width.trim().length() > 0)
            d.append(" width=\"").append(width).append("\"");
        d.append(">\n");
    }

    public void writePostfix(Device d, SBaseTable baseTable)
        throws IOException
    {
        d.append("</table>\n");
    }

    public void writeBody(Device d, SBaseTable baseTable)
        throws IOException
    {
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

        d.append("<td>");
        rendererPane.writeComponent(d, comp, baseTable);
        d.append("</td>");
    }

    protected void writeHeaderCell(Device d, SBaseTable baseTable,
                                   SCellRendererPane rendererPane, int c)
        throws IOException
    {
        SComponent comp = baseTable.prepareHeaderRenderer(c);

        d.append("<th>");
        rendererPane.writeComponent(d, comp, baseTable);
        d.append("</th>");
    }
}
