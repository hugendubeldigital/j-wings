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
import java.util.*;

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
    private SFrameSet vertical;
    private SFrameSet horizontal;
    private SFrame toolbarFrame;
    private SFrame menuFrame;
    private SFrame mainFrame;
    private ReloadManagerFrame reloadManagerFrame;

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
        vertical = new SFrameSet(new SFrameSetLayout(null, "60,*"));
        toolbarFrame = new SFrame("toolbar");
        vertical.add(toolbarFrame);

        horizontal = new SFrameSet(new SFrameSetLayout("210,*", null));
        vertical.add(horizontal);
        menuFrame = new SFrame("menu frame");
        horizontal.add(menuFrame);
        mainFrame = new SFrame("main frame");
        horizontal.add(mainFrame);

        vertical.setBaseTarget("_top");
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

        SButton menuModifier = new SButton("reload menu frame");
        menuModifier.addActionListener(menuIncrement);

        SButton mainModifier = new SButton("reload main frame");
        mainModifier.addActionListener(mainIncrement);

        SButton bothModifier = new SButton("reload both frames");
        bothModifier.addActionListener(menuIncrement);
        bothModifier.addActionListener(mainIncrement);

        SForm form = new SForm();
        form.add(menuModifier);
        form.add(mainModifier);
        form.add(bothModifier);
        toolbar.getContentPane().add(form);

        menu.getContentPane().setLayout(null);
        menu.getContentPane().add(menuButton);

        main.getContentPane().setLayout(null);
        main.getContentPane().add(mainButton);

        menuButton.addActionListener(menuDecrement);
        mainButton.addActionListener(mainDecrement);

        final SCheckBox toggleReloadManager = new SCheckBox("use reload manager frame");
        toggleReloadManager.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (toggleReloadManager.isSelected())
                        installReloadManagerFrame();
                    else
                        uninstallReloadManagerFrame();
                }
            });
        toolbar.getContentPane().add(toggleReloadManager);
    }

    void installReloadManagerFrame() {
        // add reload manager frame
        vertical.setLayout(new SFrameSetLayout(null, "30,*,10"));
        reloadManagerFrame = new ReloadManagerFrame();
        vertical.add(reloadManagerFrame);

        // set base target
        toolbarFrame.setBaseTarget("frame" + reloadManagerFrame.getUnifiedId());
        menuFrame.setBaseTarget("frame" + reloadManagerFrame.getUnifiedId());
        mainFrame.setBaseTarget("frame" + reloadManagerFrame.getUnifiedId());

        DynamicResource targetResource
            = reloadManagerFrame.getDynamicResource(DynamicCodeResource.class);
        // set target resource
        toolbarFrame.setTargetResource(targetResource.getId());
        menuFrame.setTargetResource(targetResource.getId());
        mainFrame.setTargetResource(targetResource.getId());
    }

    void uninstallReloadManagerFrame() {
        Set dirtyResources = new HashSet();
        dirtyResources.add(vertical.getDynamicResource(DynamicCodeResource.class));
        reloadManagerFrame.setDirtyResources(dirtyResources);

        vertical.remove(reloadManagerFrame);
        vertical.setLayout(new SFrameSetLayout(null, "30,*"));
        reloadManagerFrame = null;

        toolbarFrame.setBaseTarget("_top");
        menuFrame.setBaseTarget("_top");
        mainFrame.setBaseTarget("_top");

        toolbarFrame.setTargetResource(null);
        menuFrame.setTargetResource(null);
        mainFrame.setTargetResource(null);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse response)
        throws ServletException, IOException
    {
        if (reloadManagerFrame != null) {
            Set dirtyResources = new HashSet(getSession().getReloadManager().getDirtyResources());
            reloadManagerFrame.setDirtyResources(dirtyResources);
            getSession().getReloadManager().clear();
        }
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
