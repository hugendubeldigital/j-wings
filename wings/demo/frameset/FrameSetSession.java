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

package frameset;

import java.text.SimpleDateFormat;

import java.awt.event.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.wings.*;
import org.wings.servlet.*;
import org.wings.session.*;

public class FrameSetSession
    extends SessionServlet{

    private final static SIcon on = new ResourceImageIcon(SCheckBox.class,
                                              "icons/bulb2.gif");
    private final static SIcon off = new ResourceImageIcon(SCheckBox.class, 
                                              "icons/bulb1.gif");

    private SLabel leftLabel = createLabel(null, on);
    private SLabel rightLabel = createLabel(null, on);
  

    public FrameSetSession(Session session, HttpServletRequest req) {
        super(session);
    }

    public void postInit(ServletConfig config)
        throws ServletException{

        getSession().setReloadManager(new FrameSetReloadManager());

        SFrameSet vertical = new SFrameSet(new SFrameSetLayout(null, "50,*"));
        SFrame toolbarFrame = new SFrame("toolbar");
        toolbarFrame.getContentPane().setLayout(new SBorderLayout());
        toolbarFrame.getContentPane().add(new SLabel("This demonstrates, that frames are only reloaded, if any of their contents have changed."), SBorderLayout.NORTH);
        toolbarFrame.getContentPane().add(new TimestampLabel(), SBorderLayout.EAST);
        vertical.add(toolbarFrame);

        SFrameSet horizontal = new SFrameSet(new SFrameSetLayout("300,*", null));
        vertical.add(horizontal);
        SFrame leftFrame = new SFrame("left frame");
        leftFrame.getContentPane().setLayout(new SBorderLayout());
        leftFrame.getContentPane().add(new TimestampLabel(), SBorderLayout.NORTH);
        horizontal.add(leftFrame);
        SFrame rightFrame = new SFrame("right frame");
        rightFrame.getContentPane().setLayout(new SBorderLayout());
        rightFrame.getContentPane().add(new TimestampLabel(), SBorderLayout.NORTH);
        horizontal.add(rightFrame);



        ActionListener changeLeft = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    if ( leftLabel.getIcon()==on )
                        leftLabel.setIcon(off);
                    else
                        leftLabel.setIcon(on);
                }
            };

        ActionListener changeRight = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    if ( rightLabel.getIcon()==on )
                        rightLabel.setIcon(off);
                    else
                        rightLabel.setIcon(on);
                }
            };


        SButton changeLeftButton = createButton("change left");
        changeLeftButton.addActionListener(changeLeft);

        SButton changeRightButton = createButton("change right");
        changeRightButton.addActionListener(changeRight);

        SButton changeBothButton = createButton("change both");
        changeBothButton.addActionListener(changeLeft);
        changeBothButton.addActionListener(changeRight);


        SPanel toolbarPanel = new SPanel(null);
        toolbarPanel.add(changeLeftButton);
        toolbarPanel.add(changeRightButton);
        toolbarPanel.add(changeBothButton);
        toolbarFrame.getContentPane().add(toolbarPanel, SBorderLayout.CENTER);

        // left frame
        SPanel leftFramePanel = new SPanel(new SBorderLayout());
        leftFramePanel.add(leftLabel,  SBorderLayout.WEST);
        SForm leftButtons = createFormButtons("here", changeLeft,
                                              "-->",  changeRight);
        leftFramePanel.add(leftButtons, SBorderLayout.EAST);
        leftFrame.getContentPane().add(leftFramePanel, SBorderLayout.CENTER);

        // right frame
        SPanel rightFramePanel = new SPanel(new SBorderLayout());
        rightFramePanel.add(rightLabel, SBorderLayout.EAST);
        SForm rightButtons = createFormButtons("<--", changeLeft,
                                               "here",  changeRight);
        rightFramePanel.add(rightButtons, SBorderLayout.WEST);
        rightFrame.getContentPane().add(rightFramePanel, SBorderLayout.CENTER);

        setFrame(vertical);
    }

    SButton createButton(String text) {
        SButton b = new SButton(text + "&nbsp;&nbsp;&nbsp;&nbsp;");
        return b;
    }

    SLabel createLabel(String text, SIcon icon) {
        SLabel l = new SLabel(icon);
        return l;
    }

    SForm createFormButtons(String text1, ActionListener listener1,
                            String text2, ActionListener listener2) {
        SForm f = new SForm(new SBorderLayout());
        SButton b;
        b = new SButton(text1);
        b.addActionListener(listener1);
        f.add(b, SBorderLayout.WEST);
        b = new SButton(text2);
        b.addActionListener(listener2);
        f.add(b, SBorderLayout.EAST);
        return f;
    }

    public String getServletInfo() {
        return "FrameSet Demo ($Revision$)";
    }
}

class TimestampLabel extends SLabel {
    private final static SimpleDateFormat dateFormat = 
        new SimpleDateFormat("'rendered&nbsp;at&nbsp;'HH:mm:ss.SSS");
    
    TimestampLabel() {
        setHorizontalAlignment(RIGHT);
    }
        
    public String getText() {
        return dateFormat.format(new java.util.Date());
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
