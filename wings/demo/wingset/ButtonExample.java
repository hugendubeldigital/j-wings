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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ButtonExample
    extends SPanel
{
    SForm form = new SForm();
    SPanel panel= new SPanel();
    Icon icon = SUtil.makeIcon(SButton.class, "icons/Warn.gif");
    Icon disabledIcon = SUtil.makeIcon(SButton.class, "icons/WarnDis.gif");

    public ButtonExample() {
        super(new SFlowDownLayout());

        add(createExample());
        add(new SSeparator());

        SHRef href = new SHRef("View Source Code");
        href.setReference("http://www.mercatis.de/~armin/WingSet/" +
                          getClass().getName() + ".java");
        add(href);
    }

    SContainer createExample() {
        SContainer cont = new SContainer(new SGridLayout(2,2));

        SPanel panel = new SPanel(new SGridLayout(2,1));
        panel.add(new SLabel("<H4>Buttons not in a form</H4>"));
        panel.add(createButtonExample());
        cont.add(panel);


        panel = new SPanel(new SGridLayout(2,1));
        panel.add(new SLabel("<H4>Image Buttons not in a form</H4>"));
        panel.add(new SLabel("<H5>TestBC and TestBR are disabled</H5>"));
        panel.add(createImageButtonExample());
        cont.add(panel);


        SForm form = new SForm(new SGridLayout(2,1));
        form.add(new SLabel("<H4>Buttons in a form</H4>"));
        panel.add(new SLabel("<H5>TestBC and TestBR are disabled</H5>"));
        form.add(createButtonExample());
        cont.add(form);

        form = new SForm(new SGridLayout(2,1));
        form.add(new SLabel("<H4>Image Buttons in a form</H4>"));
        form.add(createImageButtonExample());
        cont.add(form);

        return cont;
    }


    SContainer createButtonExample() {
        SPanel erg = new SPanel(new SFlowDownLayout());

        erg.add(createTextButtonExample());

        return erg;
    }


    SContainer createTextButtonExample() {
        SPanel text = new SPanel();

        final SLabel pressed = new SLabel("no Button pressed");

        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pressed.setText("Button \"" + e.getActionCommand() + "\" pressed");
            }
        };

        for ( int i=0; i<3; i++ ) {
            SButton b = new SButton("text " + (i+1));
            // b.setBorder(new SLineBorder(1));
            b.addActionListener(action);
            text.add(b);
        }
        text.add(new SLabel("<BR>"));
        text.add(pressed);

        return text;
    }

    SContainer createImageButtonExample() {

        SButton[] buttons = new SButton[9];
        buttons[0] = new SButton("testTL");
        buttons[1] = new SButton("testTC");
        buttons[2] = new SButton("testTR");
        buttons[3] = new SButton("testCL");
        buttons[4] = new SButton("testCC");
        buttons[5] = new SButton("testCR");
        buttons[6] = new SButton("testBL");
        buttons[7] = new SButton("testBC");
        buttons[8] = new SButton("testBR");

        for ( int i=0; i<buttons.length; i++ ) {
            buttons[i].setIcon(icon);
            buttons[i].setDisabledIcon(disabledIcon);
            buttons[i].setToolTipText("Button " + i);
        }

        buttons[4].setIcon((Icon)null);
        //buttons[4].setIcon("http://194.95.24.168/~armin/WingSet/swing-64.gif");

        buttons[0].setVerticalTextPosition(SConstants.TOP);
        buttons[0].setHorizontalTextPosition(SConstants.LEFT);

        buttons[1].setVerticalTextPosition(SConstants.TOP);
        buttons[1].setHorizontalTextPosition(SConstants.CENTER);

        buttons[2].setVerticalTextPosition(SConstants.TOP);
        buttons[2].setHorizontalTextPosition(SConstants.RIGHT);

        buttons[3].setVerticalTextPosition(SConstants.CENTER);
        buttons[3].setHorizontalTextPosition(SConstants.LEFT);

        buttons[4].setVerticalTextPosition(SConstants.CENTER);
        buttons[4].setHorizontalTextPosition(SConstants.CENTER);

        buttons[5].setVerticalTextPosition(SConstants.CENTER);
        buttons[5].setHorizontalTextPosition(SConstants.RIGHT);

        buttons[6].setVerticalTextPosition(SConstants.BOTTOM);
        buttons[6].setHorizontalTextPosition(SConstants.LEFT);

        buttons[7].setSelected(true);
        buttons[7].setEnabled(false);
        buttons[7].setVerticalTextPosition(SConstants.BOTTOM);
        buttons[7].setHorizontalTextPosition(SConstants.CENTER);

        buttons[8].setSelected(false);
        buttons[8].setEnabled(false);
        buttons[8].setVerticalTextPosition(SConstants.BOTTOM);
        buttons[8].setHorizontalTextPosition(SConstants.RIGHT);

        SPanel erg = new SPanel(new SFlowDownLayout());

        SGridLayout grid = new SGridLayout(3,3);
        grid.setBorder(1);
        SPanel b = new SPanel(grid);

        final SLabel pressed = new SLabel("no Button pressed");

        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pressed.setText("Button \"" + e.getActionCommand() + "\" pressed");
            }
        };

        for ( int i=0; i<buttons.length; i++ ) {
            buttons[i].addActionListener(action);
        }

        for ( int i=0; i<buttons.length; i++ )
            b.add(buttons[i]);

        erg.add(b);
        erg.add(pressed);

        return erg;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
