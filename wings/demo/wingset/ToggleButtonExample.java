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
public class ToggleButtonExample
    extends SPanel
{
    SForm form = new SForm();
    SPanel panel= new SPanel();
    javax.swing.Icon icon = null;

    public ToggleButtonExample() {
        super(new SFlowDownLayout());

        add(createExample());
        add(new SSeparator());

        SHRef href = new SHRef("View Source Code");
        href.setReference("/demo/wingset/" +
                          getClass().getName().substring(getClass().getName().indexOf('.') +1) + ".java");
        add(href);
    }


    SContainer createExample() {
        SContainer cont = new SPanel(new SFlowDownLayout());

        cont.add(new SLabel("<h4>ToggleButtones not in a ButtonGroup</h4>"));
        cont.add(createToggleButtonExample());

        cont.add(new SSeparator());

        cont.add(new SLabel("<h4>Image ToggleButtones in a ButtonGroup</h4>"));
        cont.add(createGroupToggleButtonExample());

        return cont;
    }


    SContainer createGroupToggleButtonExample() {
        SPanel text = new SPanel(new SFlowLayout());

        SButtonGroup group = new SButtonGroup();
        for ( int i=0; i<3; i++ ) {
            SToggleButton b = new SToggleButton("toggle " + (i+1));
            group.add(b);
            text.add(b);
            text.add(new SLabel("&nbsp;&nbsp;"));
        }

        return text;
    }

    SContainer createToggleButtonExample() {
        SPanel text = new SPanel(new SFlowLayout());

        for ( int i=0; i<3; i++ ) {
            SToggleButton b = new SToggleButton("toggle " + (i+1));
            text.add(b);
            text.add(new SLabel("&nbsp;&nbsp;"));
        }

        return text;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
