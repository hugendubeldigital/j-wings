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

import java.awt.Image;
import java.awt.Color;
import java.io.IOException;

import javax.swing.tree.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;

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
    extends SContainer
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

    /** Icon used to show non-leaf nodes that aren't expanded. */
    transient protected Icon closedIcon = null;

    /** Icon used to show leaf nodes. */
    transient protected Icon leafIcon = null;

    /** Icon used to show non-leaf nodes that are expanded. */
    transient protected Icon openIcon = null;

    /**
     * TODO: documentation
     */
    protected SLabel handle = null;

    /**
     * TODO: documentation
     */
    protected SLabel body = null;

    /**
     * TODO: documentation
     */
    protected SGetAddress handleAddr = null;

    /**
     * TODO: documentation
     */
    protected SGetAddress bodyAddr = null;

    /**
     * Create a SDefaultTreeCellRenderer with default properties.
     */
    public SDefaultTreeCellRenderer() {
        setLayout(null);
        handle = new SLabel();
        body = new SLabel();
        add(handle);
        add(body);
        createDefaultIcons();
    }

    /**
     * TODO: documentation
     *
     */
    protected void createDefaultIcons() {
        CGManager cgManager = getSession().getCGManager();
        if (cgManager == null)
            return;
        setOpenIcon(cgManager.getIcon("Tree.openIcon"));
        setClosedIcon(cgManager.getIcon("Tree.closedIcon"));
        setLeafIcon(cgManager.getIcon("Tree.leafIcon"));
    }

    public SComponent getTreeCellRendererComponent(STree tree,
                                                   Object value,
                                                   boolean selected,
                                                   boolean expanded,
                                                   boolean leaf,
                                                   int row,
                                                   boolean hasFocus)
    {
        handleAddr = tree.getServerAddress();
        handleAddr.add(tree.getNamePrefix() + "=h" + value.hashCode());
        bodyAddr = tree.getServerAddress();
        bodyAddr.add(tree.getNamePrefix() + "=b" + value.hashCode());

        String text = "";

        if (selected) {
            body.setBackground(backgroundSelectionColor);
            body.setForeground(textSelectionColor);
            body.setStyle(textSelectionStyle);
        }
        else {
            body.setBackground(backgroundNonSelectionColor);
            body.setForeground(textNonSelectionColor);
            body.setStyle(textNonSelectionStyle);
        }

        if (value == null) {
            body.setText("&nbsp;");
            return this;
        }

        if (!leaf) {
            if (expanded) {
                if (openIcon == null)
                    text += "-";
                handle.setIcon(openIcon);
            }
            else {
                if (closedIcon == null)
                    text += "+";
                handle.setIcon(closedIcon);
            }
        }
        else
            handle.setIcon(leafIcon);

        String objText = org.wings.plaf.xhtml.Utils.escapeSpecialChars(value.toString());
        body.setText(text + objText );
        handle.setToolTipText(objText);

        return this;
    }

    /**
     * TODO: documentation
     *
     * @param d
     * @throws IOException
     */
    public void write(Device d)
        throws IOException
    {
        d.append("<a href=\"");
        handleAddr.write(d);
        d.append("\">");
        handle.write(d);
        d.append("</a> ");
        d.append("<a href=\"");
        bodyAddr.write(d);
        d.append("\"> ");
        body.write(d);
        d.append("</a>");
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

    /**
     * Sets the icon used to represent non-leaf nodes that are expanded.
     *
     * @param newIcon
     */
    public void setOpenIcon(Icon newIcon) {
        openIcon = newIcon;
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are expanded.
     *
     * @return
     */
    public Icon getOpenIcon() {
        return openIcon;
    }

    /**
     * Sets the icon used to represent non-leaf nodes that are not expanded.
     *
     * @param newIcon
     */
    public void setClosedIcon(Icon newIcon) {
        closedIcon = newIcon;
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are not
     * expanded.
     *
     * @return
     */
    public Icon getClosedIcon() {
        return closedIcon;
    }

    /**
     * Sets the icon used to represent leaf nodes.
     *
     * @param newIcon
     */
    public void setLeafIcon(Icon newIcon) {
        leafIcon = newIcon;
    }

    /**
     * Returns the icon used to represent leaf nodes.
     *
     * @return
     */
    public Icon getLeafIcon() {
        return leafIcon;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
