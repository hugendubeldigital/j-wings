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

import java.io.IOException;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*; import org.wings.border.*;
import org.wings.util.CGUtil;

public final class OptionPaneCG
    extends org.wings.plaf.xhtml.OptionPaneCG
{
    private SIcon close;

    public void installCG(SComponent component) {
        super.installCG(component);
        close = component.getSession().getCGManager().getIcon("InternalFrameCG.closeIcon");
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SOptionPane dialog = (SOptionPane)c;
        int cols = 0;

        String text = dialog.getTitle();
        if (text == null)
            text = "Dialog";

        d.append("<table border=\"0\" width=\"100%\"><tr>\n");
        d.append("<td align=\"center\" valign=\"middle\">\n");
        d.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"");
        CGUtil.writeSize(d, dialog);
        d.append("><tr>\n");

        d.append("<td color=\"#ffffff\" bgcolor=\"#5555aa\" class=\"frametitle\">&nbsp;<b>")
            .append(text)
            .append("</b></td>");
        cols ++;

        if (dialog.isCancelable() && close != null) {
            /*
             * this is a non-functioning hack for demonstration purposes.
             */
            SButton closeButton = new SButton(close);
            closeButton.setToolTipText("close");
            closeButton.setParent(c.getParent());
            d.append("<td bgcolor=\"#dedede\" width=\"16\" class=\"framebutton\">");
            closeButton.write(d);
            d.append("</td>");
            cols ++;
        }

        d.append("</tr>\n");

        d.append("<tr><td colspan=\"" + cols)
            .append("\" class=\"frameborder\">\n");

        writePrefix(d, dialog);
        org.wings.plaf.xhtml.Utils.writeContainerContents(d, dialog);
        writePostfix(d, dialog);
        d.append("</td></tr>\n</table>\n");
        d.append("</td></tr>\n</table>\n");
    }

    protected void writePrefix(Device d, SDialog dialog)
        throws IOException
    {
        String encodingType = dialog.getEncodingType();

        d.append("<form method=\"");
        if (dialog.isPostMethod())
            d.append("post");
        else
            d.append("get");
        d.append("\"");

        Utils.writeStyleAttribute(d, dialog.getStyle());

        if (encodingType != null)
            d.append(" enctype=\"").append(encodingType).append("\"");

        d.append(" action=\"").append(dialog.getRequestURL()).
            append("\">\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
