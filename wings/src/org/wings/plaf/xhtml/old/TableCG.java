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
