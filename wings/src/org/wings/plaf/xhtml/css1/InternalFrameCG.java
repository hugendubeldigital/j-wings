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

public class InternalFrameCG
    extends org.wings.plaf.xhtml.InternalFrameCG
{
    private SIcon iconify;
    private SIcon deiconify;
    private SIcon maximize;
    private SIcon unmaximize;
    private SIcon close;


    public void installCG(SComponent component) {
        super.installCG(component);
        CGManager cg = component.getSession().getCGManager();
        iconify    = cg.getIcon("InternalFrameCG.iconifyIcon");
        deiconify  = cg.getIcon("InternalFrameCG.deiconifyIcon");
        maximize   = cg.getIcon("InternalFrameCG.maximizeIcon");
        unmaximize = cg.getIcon("InternalFrameCG.unmaximizeIcon");
        close      = cg.getIcon("InternalFrameCG.closeIcon");
    }


    public void write(Device d, SComponent c)
        throws IOException
    {
        SInternalFrame frame = (SInternalFrame)c;
        int cols = 0;

        String text = frame.getTitle();
        if (text == null)
            text = "wingS";

        RequestURL addr;

        // left icon..
        d.print("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\"><tr>");

        if (frame.getIcon() != null) {
            d.print("<td bgcolor=\"#dedede\" width=\"16\" class=\"framebutton\"><img src=\"")
                .print(frame.getIcon().getURL())
                .print("\" width=\"16\" height=\"16\" border=\"0\" /></td>");
            cols ++;
        }
        
        // main window bar .. (width=480: hack necessary for opera, netscape 4)
        d.print("<td width=\"480\" color=\"#ffffff\" bgcolor=\"#5555aa\" class=\"frametitle\">&nbsp;<b>")
            .print(text)
            .print("</b></td>");
        cols ++;

        // optional icons.
        if (frame.isIconifyable() && !frame.isIconified() && iconify != null) {
            addr = frame.getRequestURL();
            addr.addParameter(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_ICONIFIED);

            d.print("<td bgcolor=\"#dedede\" width=\"16\" class=\"framebutton\"><a href=\"")
                .print(addr)
                .print("\"><img src=\"")
                .print(iconify.getURL())
                .print("\" width=\"16\" height=\"16\" border=\"0\" /></a></td>");
            cols ++;
        }

        if (frame.isIconified() && deiconify != null) {
            addr = frame.getRequestURL();
            addr.addParameter(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_DEICONIFIED);

            d.print("<td bgcolor=\"#dedede\" width=\"16\" class=\"framebutton\"><a href=\"")
                .print(addr)
                .print("\"><img src=\"")
                .print(deiconify.getURL())
                .print("\" width=\"16\" height=\"16\" border=\"0\" /></a></td>");
            cols ++;
        }

        if (frame.isMaximizable() 
            && !frame.isMaximized() 
            && !frame.isIconified()
            && maximize != null)     {
            addr = frame.getRequestURL();
            addr.addParameter(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_MAXIMIZED);

            d.print("<td bgcolor=\"#dedede\" width=\"16\" class=\"framebutton\"><a href=\"")
                .print(addr)
                .print("\"><img src=\"")
                .print(maximize.getURL())
                .print("\" width=\"16\" height=\"16\" border=\"0\" /></a></td>");
            cols ++;
        }

        if (frame.isMaximized() && unmaximize != null) {
            addr = frame.getRequestURL();
            addr.addParameter(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_UNMAXIMIZED);

            d.print("<td bgcolor=\"#dedede\" width=\"16\" class=\"framebutton\"><a href=\"")
                .print(addr)
                .print("\"><img src=\"")
                .print(unmaximize.getURL())
                .print("\" width=\"16\" height=\"16\" border=\"0\" /></a></td>");
            cols ++;
        }

        if (frame.isClosable() && close != null) {
            addr = frame.getRequestURL();
            addr.addParameter(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_CLOSED);

            d.print("<td bgcolor=\"#dedede\" width=\"16\" class=\"framebutton\"><a href=\"")
                .print(addr)
                .print("\"><img src=\"")
                .print(close.getURL())
                .print("\" width=\"16\" height=\"16\" border=\"0\" /></a></td>");
            cols ++;
        }

        d.print("</tr>\n");

        // write the actual content.
        if (!frame.isIconified()) {
            d.print("<tr><td colspan=\"" + cols)
                .print("\" class=\"frameborder\">\n");
            org.wings.plaf.xhtml.Utils.writeContainerContents(d, frame);
            d.print("</td></tr>\n");
        }

        d.print("</table>\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
