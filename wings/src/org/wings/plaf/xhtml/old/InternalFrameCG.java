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
import javax.swing.Icon;

import org.wings.*;
import org.wings.externalizer.*;
import org.wings.event.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class InternalFrameCG
    extends org.wings.plaf.xhtml.InternalFrameCG
{
    private Icon iconify;
    private Icon deiconify;
    private Icon maximize;
    private Icon unmaximize;
    private Icon close;


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
        String iconAdr = null;
        String iconifyAdr = null;
        String deiconifyAdr = null;
        String maximizeAdr = null;
        String unmaximizeAdr = null;
        String closeAdr = null;
        int cols = 0;

        String text = frame.getTitle();
        if (text == null)
            text = "wingS";

        ExternalizeManager ext = c.getExternalizeManager();
        if (ext != null) {
            try {
                if (frame.getIcon() != null)
                    iconAdr = ext.externalize(frame.getIcon());

                if (frame.isIconified())
                    deiconifyAdr = ext.externalize(deiconify);

                if (frame.isIconifyable() && !frame.isIconified())
                    iconifyAdr = ext.externalize(iconify);

                if (frame.isMaximizable() && !frame.isMaximized())
                    maximizeAdr = ext.externalize(maximize);

                if (frame.isMaximized())
                    unmaximizeAdr = ext.externalize(unmaximize);

                if (frame.isClosable())
                    closeAdr = ext.externalize(close);
            }
            catch (java.io.IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
            }
        }

        SGetAddress addr;

        d.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"1\" width=\"100%\"><tr>");
        if (iconAdr != null) {
            d.append("<td bgcolor=\"#dedede\" width=\"16\"><img src=\"")
                .append(iconAdr)
                .append("\" width=\"16\" height=\"16\" border=\"0\"></a></td>");
            cols ++;
        }

        d.append("<td color=\"#ffffff\" bgcolor=\"#5555aa\">&nbsp;<b>")
            .append(text)
            .append("</b></td>");
        cols ++;

        if (iconifyAdr != null) {
            addr = frame.getServerAddress();
            addr.add(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_ICONIFIED);

            d.append("<td bgcolor=\"#dedede\" width=\"16\"><a href=\"")
                .append(addr)
                .append("\"><img src=\"")
                .append(iconifyAdr)
                .append("\" width=\"16\" height=\"16\" border=\"0\"></a></td>");
            cols ++;
        }

        if (deiconifyAdr != null) {
            addr = frame.getServerAddress();
            addr.add(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_DEICONIFIED);

            d.append("<td bgcolor=\"#dedede\" width=\"16\"><a href=\"")
                .append(addr)
                .append("\"><img src=\"")
                .append(deiconifyAdr)
                .append("\" width=\"16\" height=\"16\" border=\"0\"></a></td>");
            cols ++;
        }

        if (maximizeAdr != null) {
            addr = frame.getServerAddress();
            addr.add(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_MAXIMIZED);

            d.append("<td bgcolor=\"#dedede\" width=\"16\"><a href=\"")
                .append(addr)
                .append("\"><img src=\"")
                .append(maximizeAdr)
                .append("\" width=\"16\" height=\"16\" border=\"0\"></a></td>");
            cols ++;
        }

        if (unmaximizeAdr != null) {
            addr = frame.getServerAddress();
            addr.add(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_UNMAXIMIZED);

            d.append("<td bgcolor=\"#dedede\" width=\"16\"><a href=\"")
                .append(addr)
                .append("\"><img src=\"")
                .append(unmaximizeAdr)
                .append("\" width=\"16\" height=\"16\" border=\"0\"></a></td>");
            cols ++;
        }

        if (closeAdr != null) {
            addr = frame.getServerAddress();
            addr.add(frame.getNamePrefix() + "=" + SInternalFrameEvent.INTERNAL_FRAME_CLOSED);

            d.append("<td bgcolor=\"#dedede\" width=\"16\"><a href=\"")
                .append(addr)
                .append("\"><img src=\"")
                .append(closeAdr)
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
