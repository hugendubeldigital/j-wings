/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package wingset;

import org.wings.*;
import org.wings.border.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class BorderExample
        extends WingSetPane {
    private static final SIcon WAIT_ICON = new SResourceIcon("org/wings/icons/Wait.gif");

    static final SBorder raised = new SBevelBorder(SBevelBorder.RAISED, new Insets(10, 0, 10, 20));
    static final SBorder lowered = new SBevelBorder(SBevelBorder.LOWERED, new Insets(10, 0, 10, 20));
    static final SBorder line = new SLineBorder(2, new Insets(10, 0, 10, 20));
    static final SBorder titled = new STitledBorder(new SEtchedBorder(SEtchedBorder.LOWERED, new Insets(10, 0, 10, 20)), "This is a title");

    public SComponent createExample() {
        SPanel p1 = new SPanel();
        try {
            SLayoutManager layout1 = createResourceTemplate("/templates/BorderExample.thtml");
            p1.setLayout(layout1);
        } catch (Exception e) {
            p1.add(new SLabel("Sorry, can't find " + "/templates/BorderExample.thtml"
                    + " in resources"));
        }
        SPanel p = p1;

        final SLabel borderLabel = new SLabel(WAIT_ICON);
        borderLabel.setBackground(new Color(222, 222, 222));
        p.add(borderLabel, "BorderExample");

        SPanel buttons = new SPanel();
        buttons.setLayout(new SGridLayout(2, 2));
        SButtonGroup group = new SButtonGroup();
        final SRadioButton rb = new SRadioButton("Raised");
        group.add(rb);
        buttons.add(rb);
        final SRadioButton lb = new SRadioButton("Lowered");
        group.add(lb);
        buttons.add(lb);
        final SRadioButton lineb = new SRadioButton("Line");
        group.add(lineb);
        buttons.add(lineb);
        final SRadioButton eb = new SRadioButton("Title");
        group.add(eb);
        buttons.add(eb);

        group.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SButtonGroup g = (SButtonGroup) e.getSource();
                Object button = g.getSelection();
                if (button == rb) {
                    borderLabel.setText("Raised Border");
                    borderLabel.setBorder(raised);
                } else if (button == lb) {
                    borderLabel.setText("Lowered Border");
                    borderLabel.setBorder(lowered);
                } else if (button == lineb) {
                    borderLabel.setText("Line Border");
                    borderLabel.setBorder(line);
                } else if (button == eb) {
                    borderLabel.setText("Titled Border");
                    borderLabel.setBorder(titled);
                }
            }
        });

        rb.setSelected(true); // default: raised
        p.add(buttons, "Modifier");

        return p;
    }
}


