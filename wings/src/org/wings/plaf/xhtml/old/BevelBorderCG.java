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

import java.awt.Insets;
import java.io.IOException;
import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.externalizer.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.session.*;
import org.wings.style.Style;

public final class BevelBorderCG
    extends org.wings.plaf.xhtml.BevelBorderCG
{
    private final static boolean BLACK = false;
    private final static boolean WHITE = true;
    private final static Insets none = new Insets(0,0,0,0);

    Icon transIcon;

    public BevelBorderCG() {
        transIcon = LookAndFeel.makeIcon(TabbedPaneCG.class, "/org/wings/icons/transdot.gif");
    }

    public void writePrefix(Device d, SBorder b)
        throws IOException
    {
        SBevelBorder border = (SBevelBorder)b;
        int bevelType = border.getBevelType();
        Insets insets = b.getInsets();

        d.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n<tr>");
        writeTD(d, bevelType == SBevelBorder.RAISED, 3);
        writeIMG(d);
        d.append("</td></tr>\n<tr>");
        writeTD(d, bevelType == SBevelBorder.RAISED, 1);
        writeIMG(d);
        d.append("</td>\n<td>");
        if (insets != null && none.equals(insets))
            d.append("<table border=\"0\" cellpadding=\"")
                .append(insets.left)
                .append("\"><tr><td>\n");
    }

    public void writePostfix(Device d, SBorder b)
        throws IOException
    {
        SBevelBorder border = (SBevelBorder)b;
        int bevelType = border.getBevelType();
        Insets insets = b.getInsets();

        if (insets != null && none.equals(insets))
            d.append("\n</td></tr></table>");

        d.append("</td>\n");
        writeTD(d, bevelType != SBevelBorder.RAISED, 1);
        writeIMG(d);
        d.append("</td></tr>\n<tr>");
        writeTD(d, bevelType == SBevelBorder.RAISED, 1);
        d.append("</td>");
        writeTD(d, bevelType != SBevelBorder.RAISED, 2);
        writeIMG(d);
        d.append("</td></tr></table>\n");
    }

    public void writeTD(Device d, boolean color, int colspan)
        throws IOException
    {
        if (color == WHITE)
            d.append("<td bgcolor=\"white\"");
        else
            d.append("<td bgcolor=\"BLACK\"");
        if (colspan > 1)
            d.append(" colspan=\"")
                .append(colspan)
                .append("\">");
        else
            d.append(">");
    }

    public void writeIMG(Device d)
        throws IOException
    {
        String transAdr = null;

        ExternalizeManager ext = SessionManager.getSession().getExternalizeManager();
        if (ext != null) {
            transAdr = ext.externalize(transIcon);
        }

        d.append("<img src=\"")
            .append(transAdr)
            .append("\" height=\"1\" width=\"1\">");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
