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
import java.io.IOException;

import org.wings.*;
import org.wings.event.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class InternalFrameCG
    extends org.wings.plaf.xhtml.InternalFrameCG
{
    private SIcon iconify;
    private SIcon deiconify;
    private SIcon maximize;
    private SIcon unmaximize;
    private SIcon close;


    public void installCG(SComponent component) {
        super.installCG(component);

        iconify = component.getSession().getCGManager().getIcon("InternalFrameCG.iconifyIcon");
        deiconify = component.getSession().getCGManager().getIcon("InternalFrameCG.deiconifyIcon");
        maximize = component.getSession().getCGManager().getIcon("InternalFrameCG.maximizeIcon");
        unmaximize = component.getSession().getCGManager().getIcon("InternalFrameCG.unmaximizeIcon");
        close = component.getSession().getCGManager().getIcon("InternalFrameCG.closeIcon");
    }


    public void write(Device d, SComponent c)
        throws IOException
    {
        SInternalFrame frame = (SInternalFrame)c;
        int cols = 0;

        String text = frame.getTitle();
        if (text == null)
            text = "wingS";


        SGetAddress addr;

        d.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"1\" width=\"100%\"><tr>");
        if (frame.getIcon() != null) {
            d.append("<td bgcolor=\"#dedede\" width=\"16\"><img src=\"")
                .append(frame.getIcon().getURL())
                .append("\" width=\"16\" height=\"16\" border=\"0\"></a></td>");
            cols ++;
        }

        d.append("<td color=\"#ffffff\" bgcolor=\"#5555aa\">&nbsp;<b>")
            .append(text)
            .append("</b></td>");
        cols ++;

        if (iconify != null) {
            addr = frame.getServerAddress();
            addr.add(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_ICONIFIED);

            d.append("<td bgcolor=\"#dedede\" width=\"16\"><a href=\"")
                .append(addr)
                .append("\"><img src=\"")
                .append(iconify.getURL())
                .append("\" width=\"16\" height=\"16\" border=\"0\"></a></td>");
            cols ++;
        }

        if (deiconify != null) {
            addr = frame.getServerAddress();
            addr.add(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_DEICONIFIED);

            d.append("<td bgcolor=\"#dedede\" width=\"16\"><a href=\"")
                .append(addr)
                .append("\"><img src=\"")
                .append(deiconify.getURL())
                .append("\" width=\"16\" height=\"16\" border=\"0\"></a></td>");
            cols ++;
        }

        if (maximize != null) {
            addr = frame.getServerAddress();
            addr.add(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_MAXIMIZED);

            d.append("<td bgcolor=\"#dedede\" width=\"16\"><a href=\"")
                .append(addr)
                .append("\"><img src=\"")
                .append(maximize.getURL())
                .append("\" width=\"16\" height=\"16\" border=\"0\"></a></td>");
            cols ++;
        }

        if (unmaximize != null) {
            addr = frame.getServerAddress();
            addr.add(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_UNMAXIMIZED);

            d.append("<td bgcolor=\"#dedede\" width=\"16\"><a href=\"")
                .append(addr)
                .append("\"><img src=\"")
                .append(unmaximize.getURL())
                .append("\" width=\"16\" height=\"16\" border=\"0\"></a></td>");
            cols ++;
        }

        if (close != null) {
            addr = frame.getServerAddress();
            addr.add(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_CLOSED);

            d.append("<td bgcolor=\"#dedede\" width=\"16\"><a href=\"")
                .append(addr)
                .append("\"><img src=\"")
                .append(close.getURL())
                .append("\" width=\"16\" height=\"16\" border=\"0\"></a></td>");
            cols ++;
        }

        d.append("</tr>\n");

        if (!frame.isIconified()) {
            d.append("<tr><td colspan=\"" + cols)
                .append("\">\n");
            org.wings.plaf.xhtml.Utils.writeContainerContents(d, frame);
            d.append("</td></tr>\n");
        }

        d.append("</table>\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
