/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package wingset;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.swing.*;

import org.wings.util.*;
import org.wings.*;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wings.externalizer.ExternalizeManager;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class Test
    extends SessionServlet
    implements SConstants
{
    public Test() {
        super( new DefaultSession() );
        System.out.println("I`m starting now");
        System.err.println("I`m starting now");
    }


    public void postInit(ServletConfig config) {
        initGUI();
    }

    void initGUI() {
        SContainer contentPane = getFrame().getContentPane();
        contentPane.setLayout(new SFlowDownLayout());

        Icon sel = SUtil.makeIcon(SCheckBox.class, "icons/bulb2.gif");
        Icon nsel = SUtil.makeIcon(SCheckBox.class, "icons/bulb1.gif");
        Icon dissel = SUtil.makeIcon(SCheckBox.class, "icons/bulb3.gif");
        Icon disnsel = SUtil.makeIcon(SCheckBox.class, "icons/bulb3.gif");

        SCheckBox testCheckBoxTL = new SCheckBox("testTL");
        SCheckBox testCheckBoxTC = new SCheckBox("testTC");
        SCheckBox testCheckBoxTR = new SCheckBox("testTR");
        SCheckBox testCheckBoxCL = new SCheckBox("testCL");
        SCheckBox testCheckBoxCC = new SCheckBox("testCC");
        SCheckBox testCheckBoxCR = new SCheckBox("testCR");
        SCheckBox testCheckBoxBL = new SCheckBox("testBL");
        SCheckBox testCheckBoxBC = new SCheckBox("testBC");
        SCheckBox testCheckBoxBR = new SCheckBox("testBR");

        SCheckBox[] boxes = new SCheckBox[9];
        boxes[0] = testCheckBoxTL;
        boxes[1] = testCheckBoxTC;
        boxes[2] = testCheckBoxTR;
        boxes[3] = testCheckBoxCL;
        boxes[4] = testCheckBoxCC;
        boxes[5] = testCheckBoxCR;
        boxes[6] = testCheckBoxBL;
        boxes[7] = testCheckBoxBC;
        boxes[8] = testCheckBoxBR;

        for ( int i=0; i<boxes.length; i++ ) {
            boxes[i].setIcon(nsel);
            boxes[i].setSelectedIcon(sel);
            boxes[i].setDisabledIcon(disnsel);
            boxes[i].setDisabledSelectedIcon(dissel);
        }

        testCheckBoxTL.setVerticalTextPosition(SConstants.TOP);
        testCheckBoxTL.setHorizontalTextPosition(SConstants.LEFT);

        testCheckBoxTC.setVerticalTextPosition(SConstants.TOP);
        testCheckBoxTC.setHorizontalTextPosition(SConstants.CENTER);

        testCheckBoxTR.setVerticalTextPosition(SConstants.TOP);
        testCheckBoxTR.setHorizontalTextPosition(SConstants.RIGHT);

        testCheckBoxCL.setVerticalTextPosition(SConstants.CENTER);
        testCheckBoxCL.setHorizontalTextPosition(SConstants.LEFT);

        testCheckBoxCC.setVerticalTextPosition(SConstants.CENTER);
        testCheckBoxCC.setHorizontalTextPosition(SConstants.CENTER);

        testCheckBoxCR.setVerticalTextPosition(SConstants.CENTER);
        testCheckBoxCR.setHorizontalTextPosition(SConstants.RIGHT);

        testCheckBoxBL.setVerticalTextPosition(SConstants.BOTTOM);
        testCheckBoxBL.setHorizontalTextPosition(SConstants.LEFT);

        testCheckBoxBC.setSelected(true);
        testCheckBoxBC.setEnabled(false);
        testCheckBoxBC.setVerticalTextPosition(SConstants.BOTTOM);
        testCheckBoxBC.setHorizontalTextPosition(SConstants.CENTER);

        testCheckBoxBR.setSelected(false);
        testCheckBoxBR.setEnabled(false);
        testCheckBoxBR.setVerticalTextPosition(SConstants.BOTTOM);
        testCheckBoxBR.setHorizontalTextPosition(SConstants.RIGHT);

        SPanel erg = new SPanel(new SFlowDownLayout());

        SGridLayout grid = new SGridLayout(3,3);
        grid.setBorder(1);
        SPanel b = new SPanel(grid);

        b.add(testCheckBoxTL);
        b.add(testCheckBoxTC);
        b.add(testCheckBoxTR);
        b.add(testCheckBoxCL);
        b.add(testCheckBoxCC);
        b.add(testCheckBoxCR);
        b.add(testCheckBoxBL);
        b.add(testCheckBoxBC);
        b.add(testCheckBoxBR);

        erg.add(b);

        contentPane.add(b);
    }

    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "Test Servlet ($Revision$)";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
