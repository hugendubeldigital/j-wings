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
import org.wings.date.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class DateChooserExample
    extends SPanel
    implements SConstants
{
    public DateChooserExample() {
        SForm p = new SForm(new SFlowDownLayout());
        final SDateChooser d = new SDateChooser();
        d.setFormComponent(false);
        p.add(d);
        final SButton update = new SButton("update");
        update.setVisible(false);
        p.add(update);
        add(p);

        final SCheckBox b = new SCheckBox("in form");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                d.setFormComponent(b.isSelected());
                update.setVisible(b.isSelected());
            }});

        add(b);
        add(new SSeparator());

        SHRef href =  new SHRef("View Source Code");
        href.setReference("/demo/wingset/" +
                          getClass().getName().substring(getClass().getName().indexOf('.') +1) + ".java");
        add(href);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
