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
    // Icons
    /** Icon used to show non-leaf nodes that aren't expanded. */
    transient protected SIcon closedIcon;

    /** Icon used to show leaf nodes. */
    transient protected SIcon leafIcon;

    /** Icon used to show non-leaf nodes that are expanded. */
    transient protected SIcon openIcon;

    /**
     * Create a SDefaultTreeCellRenderer with default properties.
     */
    public SDefaultTreeCellRenderer() {
	setHorizontalAlignment(SLabel.LEFT);
	setNoBreak(true);

	setLeafIcon(getDefaultLeafIcon());
	setClosedIcon(getDefaultClosedIcon());
	setOpenIcon(getDefaultOpenIcon());
    }

    /**
      * Returns the default icon, for the current laf, that is used to
      * represent non-leaf nodes that are expanded.
      */
    public SIcon getDefaultOpenIcon() {
        return getSession().getCGManager().getIcon("TreeCG.openIcon");
    }

    /**
      * Returns the default icon, for the current laf, that is used to
      * represent non-leaf nodes that are not expanded.
      */
    public SIcon getDefaultClosedIcon() {
        return getSession().getCGManager().getIcon("TreeCG.closedIcon");
    }

    /**
      * Returns the default icon, for the current laf, that is used to
      * represent leaf nodes.
      */
    public SIcon getDefaultLeafIcon() {
        return getSession().getCGManager().getIcon("TreeCG.leafIcon");
    }

    /**
      * Sets the icon used to represent non-leaf nodes that are expanded.
      */
    public void setOpenIcon(SIcon newIcon) {
	openIcon = newIcon;
    }

    /**
      * Returns the icon used to represent non-leaf nodes that are expanded.
      */
    public SIcon getOpenIcon() {
	return openIcon;
    }

    /**
      * Sets the icon used to represent non-leaf nodes that are not expanded.
      */
    public void setClosedIcon(SIcon newIcon) {
	closedIcon = newIcon;
    }

    /**
      * Returns the icon used to represent non-leaf nodes that are not
      * expanded.
      */
    public SIcon getClosedIcon() {
	return closedIcon;
    }

    /**
      * Sets the icon used to represent leaf nodes.
      */
    public void setLeafIcon(SIcon newIcon) {
	leafIcon = newIcon;
    }

    /**
      * Returns the icon used to represent leaf nodes.
      */
    public SIcon getLeafIcon() {
	return leafIcon;
    }


    public SComponent getTreeCellRendererComponent(STree tree,
                                                   Object value,
                                                   boolean selected,
                                                   boolean expanded,
                                                   boolean leaf,
                                                   int row,
                                                   boolean hasFocus)
    {

        if ( value == null || value.toString() == null || 
             value.toString().length() == 0 ) {

            setText("&nbsp;");
        } else {
            setText(value.toString());
            setToolTipText(value.toString());
        }

	if (!tree.isEnabled()) {
	    setEnabled(false);
	    if (leaf) {
		setDisabledIcon(getLeafIcon());
	    } else if (expanded) {
		setDisabledIcon(getOpenIcon());
	    } else {
		setDisabledIcon(getClosedIcon());
	    }
	}
	else {
	    setEnabled(true);
	    if (leaf) {
		setIcon(getLeafIcon());
	    } else if (expanded) {
		setIcon(getOpenIcon());
	    } else {
		setIcon(getClosedIcon());
	    }
	}

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
