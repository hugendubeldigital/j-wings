/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import java.awt.Color;
import java.io.IOException;

import javax.swing.tree.*;

import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SDefaultTreeCellRenderer
    extends SLabel
    implements STreeCellRenderer
{
    /**
     * Create a SDefaultTreeCellRenderer with default properties.
     */
    public SDefaultTreeCellRenderer() {
    }


    public SComponent getTreeCellRendererComponent(STree tree,
                                                   Object value,
                                                   boolean selected,
                                                   boolean expanded,
                                                   boolean leaf,
                                                   int row,
                                                   boolean hasFocus)
    {

        if (value == null || value.toString() == null || value.toString().length() == 0) {
            setText("&nbsp;");
            return this;
        }

        setText(value.toString());
        setToolTipText(value.toString());

        return this;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
