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

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class Faces
    extends WingSetPane
{
    static final Face henner = new Face("Henner");
    static final Face armin = new Face("Armin");
    static final Face holger = new Face("Holger");

    static final Face faces[] = {henner, armin};

    static final Random random = new Random();

    public SComponent createExample() {
        SForm form = new SForm(new SBorderLayout());

        SGridLayout layout = new SGridLayout(1);
        layout.setCellPadding(0);
        layout.setCellSpacing(0);
        SPanel picture = new SPanel(layout);
        final SLabel hair = new SLabel();
        picture.add(hair);
        final SLabel eye = new SLabel();
        picture.add(eye);
        final SLabel mouth = new SLabel();
        picture.add(mouth);
        form.add(picture, SBorderLayout.CENTER);

        
        SPanel chooser = new SPanel(new SFlowDownLayout());
        final SComboBox hairs = new SComboBox(faces);
        hairs.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hair.setIcon(((Face)hairs.getSelectedItem()).hair);
                }
            });
        hairs.setSelectedItem(getRandomFace());
        chooser.add(hairs);

        final SComboBox eyes = new SComboBox(faces);
        eyes.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    eye.setIcon(((Face)eyes.getSelectedItem()).eyes);
                }
            });
        eyes.setSelectedItem(getRandomFace());
        chooser.add(eyes);

        final SComboBox mouths = new SComboBox(faces);
        mouths.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mouth.setIcon(((Face)mouths.getSelectedItem()).mouth);
                }
            });
        mouths.setSelectedItem(getRandomFace());
        chooser.add(mouths);

        form.add(chooser, SBorderLayout.WEST);


        form.add(new SButton("OK"), SBorderLayout.SOUTH);


        return form;

    }


    static Face getRandomFace() {
        synchronized ( random ) {
            return faces[random.nextInt(faces.length)];
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
