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

import java.awt.Color;
import java.io.IOException;
import javax.swing.Icon;

import org.wings.*;
import org.wings.externalizer.*;
import org.wings.event.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

import org.wings.util.CGUtil;

public final class DialogCG
    extends org.wings.plaf.xhtml.DialogCG
{
    private Icon close;

    public void installCG(SComponent component) {
        super.installCG(component);
        close = component.getSession().getCGManager().getIcon("InternalFrameCG.closeIcon");
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SDialog dialog = (SDialog)c;
        String iconAdr = null;
        String closeAdr = null;
        int cols = 0;

        String text = dialog.getTitle();
        if (text == null)
            text = "Dialog";

        ExternalizeManager ext = c.getExternalizeManager();
        if (ext != null) {
            try {
                closeAdr = ext.externalize(close);
            }
            catch (java.io.IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
            }
        }

        SGetAddress addr;

        d.append("<table border=\"0\" width=\"100%\" height=\"100%\"><tr>\n");
        d.append("<td align=\"center\" valign=\"center\">\n");
        d.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"");
        CGUtil.writeSize(d, dialog);
        d.append("><tr>\n");

        d.append("<td color=\"#ffffff\" bgcolor=\"#5555aa\" class=\"frametitle\">&nbsp;<b>")
            .append(text)
            .append("</b></td>");
        cols ++;

        if (closeAdr != null) {
            SFrame frame = dialog.getParentFrame();
            addr = frame.getServerAddress();
            addr.add(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_CLOSED);

            d.append("<td bgcolor=\"#dedede\" width=\"16\" class=\"framebutton\"><a href=\"")
                .append(addr)
                .append("\"><img src=\"")
                .append(closeAdr)
                .append("\" width=\"16\" height=\"16\" border=\"0\"></a></td>");
            cols ++;
        }

        d.append("</tr>\n");

        d.append("<tr><td colspan=\"" + cols)
            .append("\" CLASS=\"frameborder\">\n");

        writePrefix(d, dialog);
        org.wings.plaf.xhtml.Utils.writeContainerContents(d, dialog);
        d.append("</td></tr>\n</table>\n");
        d.append("</td></tr>\n</table>\n");
    }

    protected void writePrefix(Device d, SDialog dialog)
        throws IOException
    {
        String encodingType = dialog.getEncodingType();

        d.append("<form method=\"");
        if (dialog.getMethod())
            d.append("post");
        else
            d.append("get");
        d.append("\"");

        Utils.writeStyleAttribute(d, dialog.getStyle());

        if (encodingType != null)
            d.append(" enctype=\"").append(encodingType).append("\"");

        d.append(" action=\"").append(dialog.getServerAddress()).
            append("\">\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
