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

package org.wings.plaf.xhtml.old;

import java.awt.*;
import java.io.IOException;
import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.externalizer.ExternalizeManager;

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
        Dimension intercellPadding = table.getIntercellPadding();
        Dimension intercellSpacing = table.getIntercellSpacing();

        d.append("<table");

        if (width != null && width.trim().length() > 0)
            d.append(" width=\"").append(width).append("\"");

        if (borderLines != null) {
            d.append(" border=\"1\" frame=\"");
            if ( borderLines.top>0 || borderLines.bottom>0 ) {
                if ( borderLines.bottom<=0 )
                    d.append("above\"");
                else if ( borderLines.top<=0 )
                    d.append("below\"");
                else if ( borderLines.left>0 && borderLines.right>0 )
                    d.append("box\"");
                else
                    d.append("hsides\"");
            }
            else if ( borderLines.left>0 || borderLines.right>0 ) {
                if ( borderLines.left<=0 )
                    d.append("rhs\"");
                else if ( borderLines.right<=0 )
                    d.append("lhs\"");
                else
                    d.append("vsides\"");
            }
        }

        if (intercellSpacing != null)
            d.append(" cellspacing=\"").append(intercellSpacing.width).append("\"");

        if (intercellPadding != null)
            d.append(" cellpadding=\"").append(intercellPadding.width).append("\"");

        if (table.getShowGrid()) {
            if (borderLines == null)
                d.append(" border=\"1\" frame=\"box\"");
            if (showHorizontalLines)
                d.append(" rules=\"rows\"");
            else if (showVerticalLines)
                d.append(" rules=\"cols\"");
            else
                d.append(" rules=\"all\"");
        }
        else
            d.append(" rules=\"none\" border=\"0\"");

        d.append(">\n");
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
        SUtil.appendTableCellAttributes(d, comp);
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

            d.append("<th");
            SUtil.appendTableCellAttributes(d, comp);
            d.append(">");
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
