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

package wingset;

import javax.swing.Icon;

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class CheckBoxExample
    extends SPanel
{
    SForm form = new SForm();
    SPanel panel= new SPanel();
    javax.swing.Icon icon = null;


    public CheckBoxExample() {
        super(new SFlowDownLayout());

        add(createExample());
        add(new SSeparator());

        SHRef href = new SHRef("View Source Code");
        href.setReference("/demo/wingset/" +
                          getClass().getName().substring(getClass().getName().indexOf('.') +1) + ".java");
        add(href);

    }


    SContainer createExample() {
        SContainer cont = new SContainer(new SGridLayout(2,2));

        SPanel panel = new SPanel(new SGridLayout(2,1));
        panel.add(new SLabel("<h4>CheckBoxes not in a form</h4>"));
        panel.add(createCheckBoxExample());
        cont.add(panel);


        panel = new SPanel(new SGridLayout(2,1));
        panel.add(new SLabel("<h4>Image CheckBoxes not in a form</h4>"));
        panel.add(new SLabel("<h5>TestBC and TestBR are disabled</h5>"));
        panel.add(createImageCheckBoxExample());
        cont.add(panel);

        SForm form = new SForm(new SGridLayout(2,1));
        form.add(new SLabel("<h4>CheckBoxes in a form</h4>"));
        form.add(createCheckBoxExample());
        form.add(new SButton("submit"));
        cont.add(form);

        form = new SForm(new SGridLayout(2,1));
        form.add(new SLabel("<h4>Image CheckBoxes in a form</h4>"));
        panel.add(new SLabel("<h5>TestBC and TestBR are disabled</h5>"));
        form.add(createImageCheckBoxExample());
        form.add(new SButton("submit"));
        cont.add(form);

        return cont;
    }


    SContainer createCheckBoxExample() {
        SPanel erg = new SPanel(new SFlowDownLayout());
        erg.add(createAnchorCheckBoxExample());
        return erg;
    }

    SContainer createAnchorCheckBoxExample() {
        SPanel text = new SPanel();

        for ( int i=0; i<3; i++ ) {
            SCheckBox b = new SCheckBox("text " + (i+1));
            // b.setBorder(new SLineBorder(1));
            text.add(b);
        }

        return text;
    }

    SContainer createImageCheckBoxExample() {
        Icon sel = SUtil.makeIcon(SCheckBox.class, "icons/bulb2.gif");
        Icon nsel = SUtil.makeIcon(SCheckBox.class, "icons/bulb1.gif");
        Icon dissel = SUtil.makeIcon(SCheckBox.class, "icons/bulb3.gif");
        Icon disnsel = SUtil.makeIcon(SCheckBox.class, "icons/bulb3.gif");

        SCheckBox[] boxes = new SCheckBox[9];
        boxes[0] = new SCheckBox("testTL");
        boxes[1] = new SCheckBox("testTC");
        boxes[2] = new SCheckBox("testTR");
        boxes[3] = new SCheckBox("testCL");
        boxes[4] = new SCheckBox("testCC");
        boxes[5] = new SCheckBox("testCR");
        boxes[6] = new SCheckBox("testBL");
        boxes[7] = new SCheckBox("testBC");
        boxes[8] = new SCheckBox("testBR");

        for ( int i=0; i<boxes.length; i++ ) {
            boxes[i].setIcon(nsel);
            boxes[i].setSelectedIcon(sel);
            boxes[i].setDisabledIcon(disnsel);
            boxes[i].setDisabledSelectedIcon(dissel);
            boxes[i].setToolTipText("CheckBox " + i);
        }

        boxes[0].setVerticalTextPosition(SConstants.TOP);
        boxes[0].setHorizontalTextPosition(SConstants.LEFT);

        boxes[1].setVerticalTextPosition(SConstants.TOP);
        boxes[1].setHorizontalTextPosition(SConstants.CENTER);

        boxes[2].setVerticalTextPosition(SConstants.TOP);
        boxes[2].setHorizontalTextPosition(SConstants.RIGHT);

        boxes[3].setVerticalTextPosition(SConstants.CENTER);
        boxes[3].setHorizontalTextPosition(SConstants.LEFT);

        boxes[4].setVerticalTextPosition(SConstants.CENTER);
        boxes[4].setHorizontalTextPosition(SConstants.CENTER);

        boxes[5].setVerticalTextPosition(SConstants.CENTER);
        boxes[5].setHorizontalTextPosition(SConstants.RIGHT);

        boxes[6].setVerticalTextPosition(SConstants.BOTTOM);
        boxes[6].setHorizontalTextPosition(SConstants.LEFT);

        boxes[7].setSelected(true);
        boxes[7].setEnabled(false);
        boxes[7].setVerticalTextPosition(SConstants.BOTTOM);
        boxes[7].setHorizontalTextPosition(SConstants.CENTER);

        boxes[8].setSelected(false);
        boxes[8].setEnabled(false);
        boxes[8].setVerticalTextPosition(SConstants.BOTTOM);
        boxes[8].setHorizontalTextPosition(SConstants.RIGHT);

        SPanel erg = new SPanel(new SFlowDownLayout());

        SGridLayout grid = new SGridLayout(3,3);
        grid.setBorder(1);
        SPanel b = new SPanel(grid);

        for ( int i=0; i<boxes.length; i++ )
            b.add(boxes[i]);

        erg.add(b);

        return erg;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
