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
public class RadioButtonExample
    extends SPanel
{
    SForm form = new SForm();
    SPanel panel= new SPanel();
    javax.swing.Icon icon = null;

    public RadioButtonExample() {
        super(new SFlowDownLayout());

        add(createExample());
        add(new SSeparator());

        SHRef href =  new SHRef("View Source Code");
        href.setReference("http://www.mercatis.de/~armin/WingSet/" +
                          getClass().getName() + ".java");
        add(href);
    }


    SContainer createExample() {
        SContainer cont = new SContainer(new SGridLayout(2,2));

        SPanel panel = new SPanel(new SGridLayout(2,1));
        panel.add(new SLabel("<H4>RadioButtons not in a form</H4>"));
        panel.add(createRadioButtonExample());
        cont.add(panel);

        panel = new SPanel(new SGridLayout(2,1));
        panel.add(new SLabel("<H4>Image RadioButtons not in a form</H4>"));
        panel.add(new SLabel("<H5>TestBC and TestBR are disabled</H5>"));
        panel.add(createImageRadioButtonExample());
        cont.add(panel);

        SForm form = new SForm(new SGridLayout(2,1));
        form.add(new SLabel("<H4>RadioButtons in a form</H4>"));
        form.add(createRadioButtonExample());
        form.add(new SButton("submit"));
        cont.add(form);

        form = new SForm(new SGridLayout(2,1));
        form.add(new SLabel("<H4>Image RadioButtons in a form</H4>"));
        panel.add(new SLabel("<H5>TestBC and TestBR are disabled</H5>"));
        form.add(createImageRadioButtonExample());
        form.add(new SButton("submit"));
        cont.add(form);

        return cont;
    }


    SContainer createRadioButtonExample() {
        SPanel erg = new SPanel(new SFlowDownLayout());
        erg.add(createAnchorRadioButtonExample());
        return erg;
    }

    SContainer createAnchorRadioButtonExample() {
        SPanel text = new SPanel();

        SButtonGroup group = new SButtonGroup();
        for ( int i=0; i<3; i++ ) {
            SRadioButton b = new SRadioButton("text " + (i+1));
            group.add(b);
            // b.setBorder(new SLineBorder(1));
            text.add(b);
        }

        return text;
    }

    SContainer createImageRadioButtonExample() {
        SButtonGroup group = new SButtonGroup();

        Icon sel = SUtil.makeIcon(SRadioButton.class, "icons/radioSelected.gif");
        Icon nsel = SUtil.makeIcon(SRadioButton.class, "icons/radio.gif");
        Icon dissel = SUtil.makeIcon(SRadioButton.class, "icons/radioDisabledSelected.gif");
        Icon disnsel = SUtil.makeIcon(SRadioButton.class, "icons/radioDisabled.gif");

        SRadioButton[] boxes = new SRadioButton[9];
        boxes[0] = new SRadioButton("testTL");
        boxes[1] = new SRadioButton("testTC");
        boxes[2] = new SRadioButton("testTR");
        boxes[3] = new SRadioButton("testCL");
        boxes[4] = new SRadioButton("testCC");
        boxes[5] = new SRadioButton("testCR");
        boxes[6] = new SRadioButton("testBL");
        boxes[7] = new SRadioButton("testBC");
        boxes[8] = new SRadioButton("testBR");

        for ( int i=0; i<boxes.length; i++ ) {
            group.add(boxes[i]);
            boxes[i].setToolTipText("RadioButton " + i);
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
