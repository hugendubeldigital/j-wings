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

    static final SIcon xeyes = 
        new ResourceImageIcon("wingset/icons/xeyes.gif");

    static final Face henner = new Face("Henner");
    static final Face armin = new Face("Armin");
    //    static final Face holger = new Face("Holger");

    static final Random random = new Random();

    static final int maxFaces = 10;

    ArrayList faces;

    SEmptyBorder nameBorder;

    SGridLayout layout;

    SPanel facePanel;

    SButtonGroup hairGroup;
    SButtonGroup eyeGroup;
    SButtonGroup mouthGroup;

    public SComponent createExample() {
        SPanel panel = new SPanel(new SBorderLayout());

        panel.add(createSwitcher(), SBorderLayout.CENTER);

        SPanel toolbar = new SPanel();

        SButton shuffle = new SButton(xeyes);
        shuffle.setToolTipText("shuffle");
        shuffle.setVerticalTextPosition(SButton.BOTTOM);
        shuffle.setHorizontalTextPosition(SButton.CENTER);
        shuffle.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    shuffle();
                }
            });
        toolbar.add(shuffle);

        panel.add(toolbar, SBorderLayout.SOUTH);
        
        return panel;
    }

    public SComponent createSwitcher() {
        nameBorder = new SEmptyBorder(10,10,10,10);


        faces = new ArrayList();

        layout = new SGridLayout(4, faces.size()+1);
        layout.setCellPadding(0);
        layout.setCellSpacing(0);
        facePanel = new SPanel(layout);

        final SLabel hair = new SLabel();
        hair.setImageAbsBottom(true);
        final SLabel eye = new SLabel();
        eye.setImageAbsBottom(true);
        final SLabel mouth = new SLabel();
        mouth.setImageAbsBottom(true);


        facePanel.add(new SLabel());
        facePanel.add(hair);
        facePanel.add(eye);
        facePanel.add(mouth);

        hairGroup = new SButtonGroup();
        hairGroup.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = Integer.parseInt(e.getActionCommand());
                    hair.setIcon(getFace(index).hair);
                }
            });

        eyeGroup = new SButtonGroup();
        eyeGroup.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = Integer.parseInt(e.getActionCommand());
                    eye.setIcon(getFace(index).eyes);
                }
            });

        mouthGroup = new SButtonGroup();
        mouthGroup.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = Integer.parseInt(e.getActionCommand());
                    mouth.setIcon(getFace(index).mouth);
                }
            });

        addFace(henner);
        addFace(armin);
        //        addFace(holger);

        shuffle();

        return facePanel;
    }

    protected void shuffle() {
        shuffle(hairGroup);
        shuffle(eyeGroup);
        shuffle(mouthGroup);
    }

    protected void shuffle(SButtonGroup g) {
        int selIndex = getRandomFaceIndex();
        for ( Iterator iter=g.iterator(); iter.hasNext(); ) {
            if ( selIndex==0 ) {
                g.setSelected((SRadioButton)iter.next(), true);
                return;
            }
            iter.next();
            selIndex--;
        }
    }

    protected Face getFace(int index) {
        return (Face)faces.get(index);
    }

    public void addFace(Face f) {
        if ( faces.size()>maxFaces ) 
            return;

        layout.setColumns(faces.size()+2);

        SLabel name = new SLabel(f.name);
        name.setBorder(nameBorder);
        facePanel.add(name, faces.size() + 0*(faces.size()+2));


        // hair
        SRadioButton hair = new SRadioButton();
        decorateButton(hair);
        hair.setActionCommand("" + faces.size());
        hairGroup.add(hair);
        facePanel.add(hair, faces.size() + 1*(faces.size()+2));

        // eye
        SRadioButton eye = new SRadioButton();
        decorateButton(eye);
        eye.setActionCommand("" + faces.size());
        eyeGroup.add(eye);
        facePanel.add(eye, faces.size() + 2*(faces.size()+2));

        SRadioButton mouth = new SRadioButton();
        decorateButton(mouth);
        mouth.setActionCommand("" + faces.size());
        mouthGroup.add(mouth);
        facePanel.add(mouth, faces.size() + 3*(faces.size()+2));

        faces.add(f);

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

    int getRandomFaceIndex() {
        synchronized ( random ) {
            return random.nextInt(faces.size());
        }
    }


    static class Face {
        SIcon hair;
        SIcon eyes;
        SIcon mouth;
        String name;

        Face() {
        }

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
