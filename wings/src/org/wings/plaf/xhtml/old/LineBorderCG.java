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

import java.awt.Color;
import java.awt.Insets;
import java.io.IOException;
import javax.swing.Icon;

import org.wings.*;
import org.wings.externalizer.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.session.*;
import org.wings.style.Style;

public final class LineBorderCG
    extends org.wings.plaf.xhtml.LineBorderCG
{
    private final static boolean BLACK = false;
    private final static boolean WHITE = true;
    private final static Insets none = new Insets(0,0,0,0);

    Icon transIcon;

    public LineBorderCG () {
        transIcon = LookAndFeel.makeIcon(TabbedPaneCG.class, "/org/wings/icons/transdot.gif");
    }

    public void writePrefix(Device d, SBorder b)
        throws IOException
    {
        SLineBorder border = (SLineBorder)b;
        int thickness = border.getThickness();
        Color lineColor = border.getLineColor();
        Insets insets = b.getInsets();

        d.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n<tr>");
        writeTD(d, lineColor, 3);
        writeIMG(d, 1, thickness);
        d.append("</td></tr>\n<tr>");
        writeTD(d, lineColor, 1);
        writeIMG(d, thickness, 1);
        d.append("</td>\n<td>");
        if (insets != null && none.equals(insets))
            d.append("<table border=\"0\" cellpadding=\"")
                .append(insets.left)
                .append("\"><tr><td>\n");
    }

    public void writePostfix(Device d, SBorder b)
        throws IOException
    {
        SLineBorder border = (SLineBorder)b;
        int thickness = border.getThickness();
        Color lineColor = border.getLineColor();
        Insets insets = b.getInsets();

        if (insets != null && none.equals(insets))
            d.append("\n</td></tr></table>");

        d.append("</td>\n");
        writeTD(d, lineColor, 1);
        writeIMG(d, thickness, 1);
        d.append("</td></tr>\n<tr>");
        writeTD(d, lineColor, 3);
        writeIMG(d, 1, thickness);
        d.append("</td></tr></table>\n");
    }

    public void writeTD(Device d, Color color, int colspan)
        throws IOException
    {
        d.append("<td bgcolor=\"")
            .append(Utils.toHexString(color))
            .append("\"");

        if (colspan > 1)
            d.append(" colspan=\"")
                .append(colspan)
                .append("\">");
        else
            d.append(">");
    }

    public void writeIMG(Device d, int width, int height)
        throws IOException
    {
        String transAdr = null;

        ExternalizeManager ext = SessionManager.getSession().getExternalizeManager();
        if (ext != null) {
            try {
                transAdr = ext.externalize(transIcon);
            }
            catch (java.io.IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
            }
        }

        d.append("<img src=\"")
            .append(transAdr)
            .append("\" height=\"")
            .append(height)
            .append("\" width=\"")
            .append(width)
            .append("\">");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
