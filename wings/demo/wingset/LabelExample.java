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

import javax.swing.Icon;

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class LabelExample
    extends SPanel
    implements SConstants
{
    public LabelExample() {
        add(createLabelExample());

        add(new SSeparator());

        SHRef href =  new SHRef("View Source Code");
        href.setReference("http://www.mercatis.de/~armin/WingSet/" +
                          getClass().getName() + ".java");
        add(href);
    }

    SPanel createLabelExample() {
        SPanel p = new SPanel();

        SGridLayout layout = new SGridLayout(3);
        layout.setBorder(1);
        p.setLayout(layout);

        Icon icon = SUtil.makeIcon(SConstants.class, "icons/Wait.gif");

        SLabel testLabelTL = new SLabel("Text oben links");
        SLabel testLabelTC = new SLabel("Text oben mitte");
        SLabel testLabelTR = new SLabel("Text oben rechts");
        SLabel testLabelCL = new SLabel("Text mitte links");
        SLabel testLabelCC = new SLabel("Text mitte mitte");
        SLabel testLabelCR = new SLabel("Text mitte rechts");
        SLabel testLabelBL = new SLabel("Text unten links");
        SLabel testLabelBC = new SLabel("Text unten mitte");
        SLabel testLabelBR = new SLabel("Text unten rechts");

        testLabelTL.setIcon(icon);
        testLabelTL.setVerticalTextPosition(SConstants.TOP);
        testLabelTL.setHorizontalTextPosition(SConstants.LEFT);

        testLabelTC.setIcon(icon);
        testLabelTC.setVerticalTextPosition(SConstants.TOP);
        testLabelTC.setHorizontalTextPosition(SConstants.CENTER);

        testLabelTR.setIcon(icon);
        testLabelTR.setVerticalTextPosition(SConstants.TOP);
        testLabelTR.setHorizontalTextPosition(SConstants.RIGHT);

        testLabelCL.setIcon(icon);
        testLabelCL.setVerticalTextPosition(SConstants.CENTER);
        testLabelCL.setHorizontalTextPosition(SConstants.LEFT);

        testLabelCC.setIcon(icon);
        testLabelCC.setVerticalTextPosition(SConstants.CENTER);
        testLabelCC.setHorizontalTextPosition(SConstants.CENTER);

        testLabelCR.setIcon(icon);
        //testLabelCR.setIcon("http://194.95.24.168/~armin/WingSet/swing-64.gif");
        testLabelCR.setVerticalTextPosition(SConstants.CENTER);
        testLabelCR.setHorizontalTextPosition(SConstants.RIGHT);

        testLabelBL.setIcon(icon);
        testLabelBL.setVerticalTextPosition(SConstants.BOTTOM);
        testLabelBL.setHorizontalTextPosition(SConstants.LEFT);

        testLabelBC.setIcon(icon);
        testLabelBC.setVerticalTextPosition(SConstants.BOTTOM);
        testLabelBC.setHorizontalTextPosition(SConstants.CENTER);

        testLabelBR.setIcon(icon);
        testLabelBR.setVerticalTextPosition(SConstants.BOTTOM);
        testLabelBR.setHorizontalTextPosition(SConstants.RIGHT);


        p.add(testLabelTL);
        p.add(testLabelTC);
        p.add(testLabelTR);
        p.add(testLabelCL);
        p.add(testLabelCC);
        p.add(testLabelCR);
        p.add(testLabelBL);
        p.add(testLabelBC);
        p.add(testLabelBR);

        return p;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
