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

import java.awt.event.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.wings.*;
import org.wings.servlet.*;
import org.wings.session.*;

public class FrameSetSession
    extends SessionServlet{
  
    private int leftCount = 0;
    private int rightCount = 0;

    private SLabel leftLabel = createLabel("" + leftCount);
    private SLabel rightLabel = createLabel("" + rightCount);
  


    public FrameSetSession(Session session, HttpServletRequest req) {
        super(session);
    }

    public void postInit(ServletConfig config)
        throws ServletException{

        getSession().setReloadManager(new FrameSetReloadManager());

        SFrameSet vertical = new SFrameSet(new SFrameSetLayout(null, "50,*"));
        SFrame toolbarFrame = new SFrame("toolbar");
        toolbarFrame.getContentPane().setLayout(new SBorderLayout());
        toolbarFrame.getContentPane().add(new TimestampLabel(), SBorderLayout.EAST);
        vertical.add(toolbarFrame);

        SFrameSet horizontal = new SFrameSet(new SFrameSetLayout("210,*", null));
        vertical.add(horizontal);
        SFrame leftFrame = new SFrame("left frame");
        leftFrame.getContentPane().setLayout(new SBorderLayout());
        leftFrame.getContentPane().add(new TimestampLabel(), SBorderLayout.EAST);
        horizontal.add(leftFrame);
        SFrame rightFrame = new SFrame("right frame");
        rightFrame.getContentPane().setLayout(new SBorderLayout());
        rightFrame.getContentPane().add(new TimestampLabel(), SBorderLayout.EAST);
        horizontal.add(rightFrame);



        ActionListener leftIncrement = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    leftCount++;
                    leftLabel.setText("" + leftCount);
                }
            };
        ActionListener leftDecrement = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    leftCount--;
                    leftLabel.setText("" + leftCount);
                }
            };
        ActionListener rightIncrement = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    rightCount++;
                    rightLabel.setText("" + rightCount);
                }
            };
        ActionListener rightDecrement = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    rightCount--;
                    rightLabel.setText("" + rightCount);
                }
            };


        SButton leftDecrementButton = createButton("decrement left");
        leftDecrementButton.addActionListener(leftDecrement);
        SButton leftIncrementButton = createButton("increment left");
        leftIncrementButton.addActionListener(leftIncrement);

        SButton rightDecrementButton = createButton("decrement right");
        rightDecrementButton.addActionListener(rightDecrement);
        SButton rightIncrementButton = createButton("increment right");
        rightIncrementButton.addActionListener(rightIncrement);

        SButton leftRightIncrementButton = createButton("increment left and right");
        leftRightIncrementButton.addActionListener(leftIncrement);
        leftRightIncrementButton.addActionListener(rightIncrement);

        SPanel toolbarPanel = new SPanel(null);
        toolbarPanel.add(leftIncrementButton);
        toolbarPanel.add(rightIncrementButton);
        toolbarPanel.add(leftRightIncrementButton);
        toolbarFrame.getContentPane().add(toolbarPanel, SBorderLayout.CENTER);

        SPanel leftFramePanel = new SPanel(null);
        leftFramePanel.add(leftDecrementButton);
        leftFramePanel.add(leftLabel);
        leftFrame.getContentPane().add(leftFramePanel, SBorderLayout.CENTER);

        SPanel rightFramePanel = new SPanel(null);
        rightFramePanel.add(rightDecrementButton);
        rightFramePanel.add(rightLabel);
        rightFrame.getContentPane().add(rightFramePanel, SBorderLayout.CENTER);

        setFrame(vertical);
    }

    SButton createButton(String text) {
        SButton b = new SButton(text + "&nbsp;&nbsp;&nbsp;&nbsp;");
        return b;
    }

    SLabel createLabel(String text) {
        SLabel l = new SLabel(text);
        l.setBorder(new SLineBorder());
        return l;
    }

    public String getServletInfo() {
        return "FrameSet Demo ($Revision$)";
    }
}

class TimestampLabel extends SLabel {
    TimestampLabel() {
        setHorizontalAlignment(RIGHT);
    }
        
    public String getText() {
        return "rendered&nbsp;at&nbsp;" + System.currentTimeMillis();
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
