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

package frameset;

import java.awt.event.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.wings.util.*;
import org.wings.*;
import org.wings.servlet.*;
import org.wings.session.*;

import org.wings.io.Device;
import org.wings.io.ServletDevice;

/**
 * TODO: documentation
 *
 * @author Holger Engels
 * @version $Revision$
 */
public class FrameSetSession
    extends SessionServlet
{
    private SButton menuButton = new SButton("menu frame");
    private SButton mainButton = new SButton("main frame");

    private int menuCount = 0;
    private int mainCount = 0;

    public FrameSetSession(Session session, HttpServletRequest req) {
        super(session);
    }

    public void postInit(ServletConfig config)
        throws ServletException
    {
        getSession().setReloadManager(new FrameSetReloadManager());
        String dir = config.getInitParameter("FrameSetBaseDir");

        SFrameSet vertical = new SFrameSet(new SFrameSetLayout(null, "30,*"));
        SFrame toolbarFrame = new SFrame("toolbar");
        vertical.add(toolbarFrame);

        SFrameSet horizontal = new SFrameSet(new SFrameSetLayout("210,*", null));
        vertical.add(horizontal);
        SFrame menuFrame = new SFrame("menu frame");
        horizontal.add(menuFrame);
        SFrame mainFrame = new SFrame("main frame");
        horizontal.add(mainFrame);

        setFrame(vertical);

        buildFrames(toolbarFrame, menuFrame, mainFrame);
    }

    protected void buildFrames(SFrame toolbar, SFrame menu, SFrame main) {
        ActionListener menuIncrement = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    menuCount++;
                    menuButton.setText("menu frame: " + menuCount);
                }
            };
        ActionListener mainIncrement = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    mainCount++;
                    mainButton.setText("main frame: " + mainCount);
                }
            };

        ActionListener menuDecrement = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    menuCount--;
                    menuButton.setText("menu frame: " + menuCount);
                }
            };
        ActionListener mainDecrement = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    mainCount--;
                    mainButton.setText("main frame: " + mainCount);
                }
            };

        SButton menuModifier = new SButton("reload menu frame&nbsp;&nbsp;&nbsp;&nbsp;");
        menuModifier.addActionListener(menuIncrement);

        SButton mainModifier = new SButton("reload main frame&nbsp;&nbsp;&nbsp;&nbsp;");
        mainModifier.addActionListener(mainIncrement);

        SButton bothModifier = new SButton("reload both frames&nbsp;&nbsp;&nbsp;&nbsp;");
        bothModifier.addActionListener(menuIncrement);
        bothModifier.addActionListener(mainIncrement);

        toolbar.getContentPane().add(menuModifier);
        toolbar.getContentPane().add(mainModifier);
        toolbar.getContentPane().add(bothModifier);

        menu.getContentPane().setLayout(null);
        menu.getContentPane().add(menuButton);

        main.getContentPane().setLayout(null);
        main.getContentPane().add(mainButton);

        menuButton.addActionListener(menuDecrement);
        mainButton.addActionListener(mainDecrement);
    }

    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "FrameSet ($Revision$)";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
