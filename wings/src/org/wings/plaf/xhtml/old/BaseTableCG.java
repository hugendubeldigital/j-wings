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

public final class BaseTableCG
    extends org.wings.plaf.xhtml.BaseTableCG
{
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

        if (baseTable.getShowGrid()) {
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

    protected void writeCell(Device d, SBaseTable baseTable,
                             SCellRendererPane rendererPane, int row, int col)
        throws IOException
    {
        SComponent comp = baseTable.prepareRenderer(baseTable.getCellRenderer(row, col), row, col);

        d.append("<td");
        SUtil.appendTableCellAttributes(d, comp);
        d.append(">");
        rendererPane.writeComponent(d, comp, baseTable);
        d.append("</td>");
    }

    protected void writeHeaderCell(Device d, SBaseTable baseTable,
                                   SCellRendererPane rendererPane, int c)
        throws IOException
    {
        SComponent comp = baseTable.prepareHeaderRenderer(c);

        d.append("<th");
        //Color bg = comp.getBackground();
        SUtil.appendTableCellAttributes(d, comp);
        d.append(">");

        //comp.setBackground(null);
        rendererPane.writeComponent(d, comp, baseTable);
        d.append("</th>");
        //comp.setBackground(bg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
