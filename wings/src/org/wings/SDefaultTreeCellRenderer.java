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
import org.wings.plaf.*;
import org.wings.style.*;

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
    /** Color to use for the foreground for selected nodes. */
    protected Color textSelectionColor = null;

    /** Color to use for the foreground for non-selected nodes. */
    protected Color textNonSelectionColor = null;

    /** Color to use for the background when a node is selected. */
    protected Color backgroundSelectionColor = Color.cyan;

    /** Color to use for the background when the node isn't selected. */
    protected Color backgroundNonSelectionColor = null;

    /** Style to use for the foreground for selected nodes. */
    protected Style textSelectionStyle = null;

    /** Style to use for the foreground for non-selected nodes. */
    protected Style textNonSelectionStyle = null;

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
        if (selected) {
            setBackground(backgroundSelectionColor);
            setForeground(textSelectionColor);
            setStyle(textSelectionStyle);
        }
        else {
            setBackground(backgroundNonSelectionColor);
            setForeground(textNonSelectionColor);
            setStyle(textNonSelectionStyle);
        }

        if (value == null || value.toString() == null || value.toString().length() == 0) {
            setText("&nbsp;");
            return this;
        }

        setText(value.toString());
        setToolTipText(value.toString());

        return this;
    }

    /**
     * Sets the color the text is drawn with when the node is selected.
     *
     * @param newColor
     */
    public void setTextSelectionColor(Color newColor) {
        textSelectionColor = newColor;
    }

    /**
     * Returns the color the text is drawn with when the node is selected.
     *
     * @return
     */
    public Color getTextSelectionColor() {
        return textSelectionColor;
    }

    /**
     * Sets the color the text is drawn with when the node isn't selected.
     *
     * @param newColor
     */
    public void setTextNonSelectionColor(Color newColor) {
        textNonSelectionColor = newColor;
    }

    /**
     * Returns the color the text is drawn with when the node isn't selected.
     *
     * @return
     */
    public Color getTextNonSelectionColor() {
        return textNonSelectionColor;
    }

    /**
     * Sets the color to use for the background if node is selected.
     *
     * @param newColor
     */
    public void setBackgroundSelectionColor(Color newColor) {
        backgroundSelectionColor = newColor;
    }


    /**
     * Returns the color to use for the background if node is selected.
     *
     * @return
     */
    public Color getBackgroundSelectionColor() {
        return backgroundSelectionColor;
    }

    /**
     * Sets the background color to be used for non selected nodes.
     *
     * @param newColor
     */
    public void setBackgroundNonSelectionColor(Color newColor) {
        backgroundNonSelectionColor = newColor;
    }

    /**
     * Returns the background color to be used for non selected nodes.
     *
     * @return
     */
    public Color getBackgroundNonSelectionColor() {
        return backgroundNonSelectionColor;
    }

    /**
     * Sets the style the text is drawn with when the node is selected.
     *
     * @param newStyle
     */
    public void setTextSelectionStyle(Style newStyle) {
        textSelectionStyle = newStyle;
    }

    /**
     * Returns the style the text is drawn with when the node is selected.
     *
     * @return
     */
    public Style getTextSelectionStyle() {
        return textSelectionStyle;
    }

    /**
     * Sets the style the text is drawn with when the node isn't selected.
     *
     * @param newStyle
     */
    public void setTextNonSelectionStyle(Style newStyle) {
        textNonSelectionStyle = newStyle;
    }

    /**
     * Returns the style the text is drawn with when the node isn't selected.
     *
     * @return
     */
    public Style getTextNonSelectionStyle() {
        return textNonSelectionStyle;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
