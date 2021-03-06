/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of the wingS demo (http://j-wings.org).
 *
 * The wingS demo is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package wingset;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ScrollPaneExample
    extends WingSetPane
{

    public SComponent createExample() {
        SPanel p = new SPanel();

        SLabel label = new SLabel("<html><h4>Table in a ScrollPane</h4>");
        p.add(label);

        // table.setShowGrid(true);
        STable table = new STable(new TableExample.ROTableModel(15, 15));
        table.setDefaultRenderer(new TableExample.MyCellRenderer());
        SScrollPane scroller = new SScrollPane(table);
        scroller.getHorizontalScrollBar().setBlockIncrement(3);
        scroller.getVerticalScrollBar().setBlockIncrement(3);
        p.add(scroller);
        return p;
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
