package org.wings.plaf.xhtml.css1;

import java.awt.*;
import java.io.IOException;

import org.wings.*;
import org.wings.externalizer.ExternalizeManager;
import org.wings.io.*;
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
        Style style = table.getStyle();

        d.append("<table");
        if (width != null && width.trim().length() > 0)
            d.append(" width=\"").append(width).append("\"");
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
            Utils.writeStyleAttribute(d, style, "header");
            d.append(">\n");
            for (int c = originCol; c < colCount; c++) {
                writeHeaderCell(d, table, rendererPane, c);
            }
            d.append("</tr>\n");
        }
        for (int r = originRow; r < rowCount; r++) {
            d.append("<tr");
            if (table.isRowSelected(r))
                Utils.writeStyleAttribute(d, style, "selection");
            else
                Utils.writeStyleAttribute(d, style, "nonselection");
            d.append(">\n");
            for (int c = originCol; c < colCount; c++) {
                writeCell(d, table, rendererPane, r, c);
            }
            d.append("</tr>\n");
        }
    }

    /*
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
    */
}
