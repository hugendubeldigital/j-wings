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

import java.awt.Insets;
import java.awt.event.*;
import javax.swing.Icon;

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class BorderExample
    extends SPanel
    implements SConstants
{
    public BorderExample() {
        add(createBorderExample());

        add(new SSeparator());

        SHRef href =  new SHRef("View Source Code");
        href.setReference("/demo/wingset/" +
                          getClass().getName().substring(getClass().getName().indexOf('.') +1) + ".java");
        add(href);
    }

    SPanel createBorderExample() {
        SPanel p = new SPanel();
        try {
            java.net.URL templateURL = 
                getClass().getResource("/wingset/BorderExample.thtml");
            if( templateURL == null ){
                p.add(new SLabel("Sorry, can't find BorderExample.thtml. Are you using a JAR-File?"));
                return p;
            }
            // you can of course directly give files here.
            STemplateLayout layout = new STemplateLayout( templateURL );
            p.setLayout( layout );
        }
        catch ( java.io.IOException except ) {
            except.printStackTrace();
        }
        
        final SBorder raised  = new SBevelBorder(SBevelBorder.RAISED);
        final SBorder lowered = new SBevelBorder(SBevelBorder.LOWERED);
        final SBorder line    = new SLineBorder(3);
        final SBorder empty   = new SEmptyBorder(new Insets(3,3,3,3));
        
        final SPanel c = new SPanel(new SBorderLayout());

        c.setBackground(new java.awt.Color(180, 180, 255));
        c.add(new SLabel(new ResourceImageIcon(SConstants.class, "icons/Wait.gif")),
              SBorderLayout.NORTH);
        c.add(new SLabel("This is the bordered content"));
        final SLabel borderType = new SLabel("raised");
        c.add(borderType, SBorderLayout.SOUTH);

        p.add(c, "BorderExample");

        SPanel buttons = new SPanel();
        buttons.setLayout(new SGridLayout(2, 2));
        SButtonGroup group = new SButtonGroup();
        final SRadioButton rb = new SRadioButton("Raised");
        group.add(rb); buttons.add(rb);
        final SRadioButton lb = new SRadioButton("Lowered");
        group.add(lb); buttons.add(lb);
        final SRadioButton lineb = new SRadioButton("Line");
        group.add(lineb); buttons.add(lineb);
        final SRadioButton eb = new SRadioButton("Empty");
        group.add(eb); buttons.add(eb);
        
        group.addActionListener (new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SButtonGroup g = (SButtonGroup) e.getSource();
                    Object button = g.getSelection();
                    if (button == rb) {
                        borderType.setText("Raised Border");
                        c.setBorder(raised);
                    }
                    else if (button == lb) {
                        borderType.setText("Lowered Border");
                        c.setBorder(lowered);
                    }
                    else if (button == lineb) {
                        borderType.setText("Line Border");
                        c.setBorder(line);
                    }
                    else if (button == eb) {
                        borderType.setText("No Border");
                        c.setBorder(empty);
                    }
                }
            });

        rb.setSelected(true); // default: raised
        p.add(buttons, "Modifier");

        return p;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
