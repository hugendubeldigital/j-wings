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

import java.awt.Insets;
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

        SGridLayout layout = new SGridLayout(2);
        p.setLayout(layout);

        Icon icon = SUtil.makeIcon(SConstants.class, "icons/Wait.gif");

        SLabel raised = new SLabel("raised");
        raised.setBorder(new SBevelBorder(SBevelBorder.RAISED));
        SLabel lowered = new SLabel("lowered");
        lowered.setBorder(new SBevelBorder(SBevelBorder.LOWERED));
        SLabel line = new SLabel("line");
        line.setBorder(new SLineBorder(3));
        SLabel empty = new SLabel("empty");
        empty.setBorder(new SEmptyBorder(new Insets(3,3,3,3)));
        
        p.add(raised);
        p.add(lowered);
        p.add(line);
        p.add(empty);

        return p;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
