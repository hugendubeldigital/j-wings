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

import org.wings.*;
import org.wings.externalizer.ExternalizeManager;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.*;

public final class BaseTableCG
    extends org.wings.plaf.xhtml.BaseTableCG
{
    public void writePrefix(Device d, SBaseTable baseTable)
        throws IOException
    {
        String width = baseTable.getWidth();
        Style style = baseTable.getStyle();

        d.append("<table");
        if (width != null && width.trim().length() > 0)
            d.append(" width=\"").append(width).append("\"");
        Utils.writeStyleAttribute(d, style);
        d.append(">\n");
    }

    public void writeBody(Device d, SBaseTable baseTable)
        throws IOException
    {
        Style style = baseTable.getStyle();

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
            d.append("<tr");
            Utils.writeStyleAttribute(d, style, "header");
            d.append(">\n");
            for (int c = originCol; c < colCount; c++) {
                writeHeaderCell(d, baseTable, rendererPane, c);
            }
            d.append("</tr>\n");
        }
        for (int r = originRow; r < rowCount; r++) {
            d.append("<tr");
            Utils.writeStyleAttribute(d, style, "cell");
            d.append(">\n");
            for (int c = originCol; c < colCount; c++) {
                writeCell(d, baseTable, rendererPane, r, c);
            }
            d.append("</tr>\n");
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
