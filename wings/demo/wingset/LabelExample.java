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

import java.awt.event.*;
import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class LabelExample
    extends WingSetPane
{
    public SComponent createExample() {
        SPanel all = new SPanel();
        SGridLayout layout = new SGridLayout(2);
        layout.setWidth(100);
        layout.setRelative(true); // -> make 100 mean '%'
        all.setLayout(layout);

        Icon icon = new ResourceImageIcon(SConstants.class, "icons/Wait.gif");

        final SLabel testLabel = new SLabel("LabelText");
        testLabel.setHorizontalAlignment(LEFT);
        testLabel.setIcon(icon);

        all.add(new SLabel("Control the LabelText's position"));
        all.add(new SLabel("Result"));
        all.add(createRoundRadio(testLabel));
        all.add(testLabel);

        return all;
    }

    SContainer createRoundRadio(final SLabel label) {
        SPanel b = createResourceTemplatePanel("/wingset/templates/roundRadio.thtml");

        SButtonGroup g = new SButtonGroup();
        final SRadioButton n = new SRadioButton();
        n.setToolTipText("North");
        b.add(n, "p=n");
        g.add(n);

        final SRadioButton s = new SRadioButton();
        s.setToolTipText("South");
        b.add(s, "p=s");
        g.add(s);

        final SRadioButton w = new SRadioButton();
        w.setToolTipText("West");
        b.add(w, "p=w");
        g.add(w);

        final SRadioButton e = new SRadioButton();
        e.setToolTipText("East");
        b.add(e, "p=e");
        g.add(e);

        final SRadioButton nw = new SRadioButton();
        nw.setToolTipText("North West");
        b.add(nw, "p=nw");
        g.add(nw);

        final SRadioButton ne = new SRadioButton();
        ne.setToolTipText("North East");
        b.add(ne, "p=ne");
        g.add(ne);

        final SRadioButton sw = new SRadioButton();
        sw.setToolTipText("South West");
        b.add(sw, "p=sw");
        g.add(sw);

        final SRadioButton se = new SRadioButton();
        se.setToolTipText("South East");
        b.add(se, "p=se");
        g.add(se);

        final SRadioButton cc = new SRadioButton();
        cc.setToolTipText("Center");
        /*
         * don't place this button in the page; a center/center does not 
         * make sense with layerless HTML
         */
        //b.add(cc, "p=cc");
        g.add(cc);

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
                    else if (button == cc) {
                        label.setVerticalTextPosition(CENTER);
                        label.setHorizontalTextPosition(CENTER);
                    }

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
 * End:
 */
