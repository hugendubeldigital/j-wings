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

import org.wings.*; import org.wings.border.*;
import org.wings.event.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

import org.wings.util.CGUtil;

public class DialogCG
    extends org.wings.plaf.xhtml.DialogCG
{

    public void write(Device d, SComponent c)
        throws IOException
    {
        SDialog dialog = (SDialog)c;
        int cols = 0;

        String text = dialog.getTitle();
        if (text == null)
            text = "Dialog";

        RequestURL addr;

        d.print("<table border=\"0\" width=\"100%\"><tr>\n");
        d.print("<td align=\"center\" valign=\"middle\">\n");
        d.print("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"");
        CGUtil.writeSize(d, dialog);
        d.print("><tr>\n");

        d.print("<td color=\"#ffffff\" bgcolor=\"#5555aa\" class=\"frametitle\">&nbsp;<b>")
            .print(text)
            .print("</b></td>");
        cols ++;

        d.print("</tr>\n");

        d.print("<tr><td colspan=\"" + cols)
            .print("\" class=\"frameborder\">\n");

        writePrefix(d, dialog);
        org.wings.plaf.xhtml.Utils.writeContainerContents(d, dialog);
        writePostfix(d, dialog);
        d.print("</td></tr>\n</table>\n");
        d.print("</td></tr>\n</table>\n");
    }

    protected void writePrefix(Device d, SDialog dialog)
        throws IOException
    {
        String encodingType = dialog.getEncodingType();

        d.print("<form method=\"");
        if (dialog.isPostMethod())
            d.print("post");
        else
            d.print("get");
        d.print("\"");

        Utils.writeStyleAttribute(d, dialog.getStyle());

        if (encodingType != null)
            d.print(" enctype=\"").print(encodingType).print("\"");

        d.print(" action=\"").print(dialog.getRequestURL()).
            print("\">\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
