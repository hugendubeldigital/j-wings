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
import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class LabelExample extends WingSetPane
{
    static final String directions[] = 
    { "nw", "n", "ne",
      "w",        "e",
      "sw", "s", "se" };

    static final SIcon onIcons[];
    static final SIcon offIcons[];
    
    static {
        ClassLoader cl = WingSetSession.class.getClassLoader();
        onIcons  = new SIcon [ directions.length ];
        offIcons = new SIcon [ directions.length ];
        for (int i=0; i < directions.length; ++i) {
            String d = directions[i];
            onIcons[i] = new ResourceImageIcon(cl, "wingset/icons/Label"
                                               + d + "On.gif");
            offIcons[i] = new ResourceImageIcon(cl, "wingset/icons/Label"
                                                + d + "Off.gif");
        }
    }

    public SComponent createExample() {
        SPanel all = new SPanel();
        SGridLayout layout = new SGridLayout(2);
        layout.setWidth(100);
        layout.setRelative(true); // -> make 100 mean '%'
        layout.setCellSpacing(10);
        all.setLayout(layout);

        SIcon icon = new ResourceImageIcon("org/wings/icons/Wait.gif");

        final SLabel testLabel = new SLabel("LabelText");
        testLabel.setHorizontalAlignment(LEFT);
        testLabel.setIcon(icon);
        testLabel.setAttribute("color", "#AA0000");
        testLabel.setAttribute("font-weight", "bold");

        all.add(new SLabel("Control the LabelText's position"));
        all.add(new SLabel("Result"));
        all.add(createRoundRadio(testLabel));
        all.add(testLabel);

        return all;
    }

    private SRadioButton createRadio(SPanel p, String constraint,
                                     SButtonGroup buttonGroup,
                                     String toolTip, int icon) {
        SRadioButton button = new SRadioButton();
        button.setIcon(offIcons[icon]);
        button.setSelectedIcon(onIcons[icon]);
        button.setToolTipText( toolTip );
        p.add(button, constraint);
        buttonGroup.add(button);
        return button;
    }

    SContainer createRoundRadio(final SLabel label) {
        SPanel b = new SPanel(new SGridLayout(3));

        SButtonGroup g = new SButtonGroup();
        final SRadioButton nw = createRadio(b,"p=nw", g, "North West", 0);
        final SRadioButton n  = createRadio(b,"p=n",  g, "North", 1);
        final SRadioButton ne = createRadio(b,"p=ne", g, "North East", 2);
        final SRadioButton w  = createRadio(b,"p=w",  g, "West", 3);
        b.add(new SDummy());
        final SRadioButton e  = createRadio(b,"p=e",  g, "East", 4);
        final SRadioButton sw = createRadio(b,"p=sw", g, "South West", 5);
        final SRadioButton s  = createRadio(b,"p=s",  g, "South", 6);
        final SRadioButton se = createRadio(b,"p=se", g, "South East", 7);

        g.addActionListener (new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    Object button = ((SButtonGroup) ev.getSource())
                        .getSelection();
                    if (button == n) {
                        label.setVerticalTextPosition(TOP);
                        label.setHorizontalTextPosition(CENTER);
                    }
                    else if (button == nw) {
                        label.setVerticalTextPosition(TOP);
                        label.setHorizontalTextPosition(LEFT);
                    }
                    else if (button == ne) {
                        label.setVerticalTextPosition(TOP);
                        label.setHorizontalTextPosition(RIGHT);
                    }
                    else if (button == w) {
                        label.setVerticalTextPosition(CENTER);
                        label.setHorizontalTextPosition(LEFT);
                    }
                    else if (button == e) {
                        label.setVerticalTextPosition(CENTER);
                        label.setHorizontalTextPosition(RIGHT);
                    }
                    else if (button == sw) {
                        label.setVerticalTextPosition(BOTTOM);
                        label.setHorizontalTextPosition(LEFT);
                    }
                    else if (button == se) {
                        label.setVerticalTextPosition(BOTTOM);
                        label.setHorizontalTextPosition(RIGHT);
                    }
                    else if (button == s) {
                        label.setVerticalTextPosition(BOTTOM);
                        label.setHorizontalTextPosition(CENTER);
                    }
                    /*
                    else if (button == cc) {
                        label.setVerticalTextPosition(CENTER);
                        label.setHorizontalTextPosition(CENTER);
                    }
                    */

                }
            });

        n.setSelected(true);

        return b;
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
