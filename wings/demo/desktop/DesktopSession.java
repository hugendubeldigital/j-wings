/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of the wingS demo (http://wings.mercatis.de).
 *
 * The wingS demo is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package desktop;

import java.awt.event.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.*;

import org.wings.*;
import org.wings.io.Device;
import org.wings.io.ServletDevice;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wings.util.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class DesktopSession
    extends SessionServlet
    implements SConstants
{
    SDesktopPane desktop;
    SInternalFrame frame1, frame2, frame3;

    public DesktopSession(Session session) {
        super(session);
        System.out.println("new DeskTopSession");
    }

    public void postInit(ServletConfig config) {
        initGUI();
    }

    void initGUI() {
        SContainer contentPane = getFrame().getContentPane();

        desktop = new SDesktopPane();
        contentPane.add(desktop);

        frame1 = new SInternalFrame();
        frame2 = new SInternalFrame();
        frame3 = new SInternalFrame();

        frame1.getContentPane().add(new SLabel(getStory()));
        frame2.getContentPane().add(new SLabel(getStory()));
        frame3.getContentPane().add(new SLabel(getStory()));

        Icon icon = new ResourceImageIcon(getClass(), "/org/wings/icons/penguin.gif");
        frame1.setIcon(icon);
        frame2.setIcon(icon);
        frame3.setIcon(icon);

        desktop.add(frame1);
        desktop.add(frame2);
        desktop.add(frame3);
    }

    protected String getStory() {
        return "Ein Philosoph ist jemand, der in einem absolut dunklen Raum " +
            "mit verbundenen Augen nach einer Schwarzen Katze sucht, die garnicht " +
            "da ist. Ein Theologe ist jemand der genau das gleiche macht und ruft: " +
            "\"ich hab sie!\"";
    }

    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "Desktop ($Revision$)";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
