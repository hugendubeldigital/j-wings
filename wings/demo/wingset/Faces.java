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

import java.awt.event.*;
import java.util.*;
import org.wings.*;
import org.wings.border.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class Faces
    extends WingSetPane
{

    static final SIcon sel = 
        new ResourceImageIcon("wingset/icons/RadioButtonSelectedIcon.gif");
    static final SIcon nsel = 
        new ResourceImageIcon("wingset/icons/RadioButtonIcon.gif");
    static final SIcon pressed = 
        new ResourceImageIcon("wingset/icons/RadioButtonPressedIcon.gif");
    static final SIcon rollsel = 
        new ResourceImageIcon("wingset/icons/RadioButtonRolloverSelectedIcon.gif");
    static final SIcon rollnsel = 
        new ResourceImageIcon("wingset/icons/RadioButtonRolloverIcon.gif");

    static final Face henner = new Face("Henner");
    static final Face armin = new Face("Armin");
    static final Face holger = new Face("Holger");

    static final Face faces[] = {henner, armin};

    static final Random random = new Random();

    SComboBox hairs;
    SComboBox eyes;
    SComboBox mouths;

    public SComponent createExample() {
        final SLabel hair = new SLabel();
        hair.setImageAbsBottom(true);
        final SLabel eye = new SLabel();
        eye.setImageAbsBottom(true);
        final SLabel mouth = new SLabel();
        mouth.setImageAbsBottom(true);


        SGridLayout layout = new SGridLayout(faces.length+1);
        layout.setCellPadding(0);
        layout.setCellSpacing(0);
        SPanel panel = new SPanel(layout);

        SEmptyBorder nameBorder = new SEmptyBorder(10,10,10,10);
        // name labels
        for ( int i=0; i<faces.length; i++ ) {
            SLabel name = new SLabel(faces[i].name);
            name.setBorder(nameBorder);
            panel.add(name);
        }
        // empty Label for picture
        panel.add(new SLabel());

        SButtonGroup hairGroup = new SButtonGroup();
        hairGroup.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = Integer.parseInt(e.getActionCommand());
                    hair.setIcon(faces[index].hair);
                }
            });
        addButtons(panel, hairGroup, getRandomFaceIndex());
        panel.add(hair);

        SButtonGroup eyeGroup = new SButtonGroup();
        eyeGroup.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = Integer.parseInt(e.getActionCommand());
                    eye.setIcon(faces[index].eyes);
                }
            });
        addButtons(panel, eyeGroup, getRandomFaceIndex());
        panel.add(eye);

        SButtonGroup mouthGroup = new SButtonGroup();
        mouthGroup.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = Integer.parseInt(e.getActionCommand());
                    mouth.setIcon(faces[index].mouth);
                }
            });
        addButtons(panel, mouthGroup, getRandomFaceIndex());
        panel.add(mouth);


        return panel;

    }

    void decorateButton(SRadioButton b) {
        b.setIcon(nsel);
        b.setSelectedIcon(sel);
        b.setRolloverIcon(rollnsel);
        b.setRolloverSelectedIcon(rollsel);
        b.setPressedIcon(pressed);
        b.setHorizontalAlignment(SRadioButton.CENTER);
        b.setVerticalAlignment(SRadioButton.CENTER);
    }

    void addButtons(SContainer c, SButtonGroup group, int selectIndex) {
        for ( int i=0; i<faces.length; i++ ) {
            SRadioButton b = new SRadioButton();
            decorateButton(b);
            b.setActionCommand("" + i);
            
            group.add(b);
            c.add(b);

            if ( i==selectIndex ) {
                b.setSelected(true);
            }
        }
    }

    static int getRandomFaceIndex() {
        synchronized ( random ) {
            return random.nextInt(faces.length);
        }
    }


    static class Face {
        SIcon hair;
        SIcon eyes;
        SIcon mouth;
        String name;

        Face(String name) {
            hair = new ResourceImageIcon("wingset/icons/" + name + "_hair.jpeg");
            eyes = new ResourceImageIcon("wingset/icons/" + name + "_eyes.jpeg");
            mouth = new ResourceImageIcon("wingset/icons/" + name + "_mouth.jpeg");

            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
