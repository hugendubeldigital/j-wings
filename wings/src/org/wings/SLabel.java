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

import java.net.URL;

import javax.swing.*;

import org.wings.plaf.*;
import org.wings.io.Device;
import org.wings.externalizer.ExternalizeManager;

/**
 * A display area for a short text string or an image, or both.
 * You can specify where in the label's display area  the label's contents
 * are aligned by setting the vertical and horizontal alignment.
 * You can also specify the position of the text relative to the image.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SLabel
    extends SComponent
    implements SConstants
{
    private static final String cgClassID = "LabelCG";

    /**
     * The text to be displayed
     */
    protected String text;

    /**
     * The icon to be displayed
     */
    protected Icon icon = null;

    /**
     * TODO: documentation
     */
    protected String iconAddress = null;

    /**
     * TODO: documentation
     */
    protected Icon disabledIcon = null;

    /**
     * TODO: documentation
     */
    protected String disabledIconAddress = null;

    private int verticalTextPosition = CENTER;
    private int horizontalTextPosition = RIGHT;
    private int iconTextGap = 1;
    private boolean noBreak = false;
    private boolean alignText = false;

    // TODO: plaf has to escape the special chars...
    // HEN: I think, this is not necessary here; The label's plaf should 
    // _always_ escape the characters, otherwise the user feels tempted to
    // output formatting information in Labels .. (s)he shouldn't!
    private boolean escapeSpecialChars = false;


    /**
     * Creates a new <code>SLabel</code> instance with the specified text
     * (left alligned) and no icon.
     *
     * @param text The text to be displayed by the label.
     */
    public SLabel(String text) {
        this(text, null, LEFT);
    }

    /**
     * Creates a new <code>SLabel</code> instance with no text and no icon.
     */
    public SLabel() {
        this((String)null);
    }

    /**
     * Creates a new <code>SLabel</code> instance with the specified icon
     * (left alligned) and no text.
     *
     * @param icon The image to be displayed by the label.
     */
    public SLabel(Icon icon) {
        this(icon, LEFT);
    }

    /**
     * Creates a new <code>SLabel</code> instance with the specified icon
     * (alligned as specified) and no text.
     *
     * @param icon The image to be displayed by the label.
     * @param horizontalAlignment One of the following constants defined in
     *        <code>SConstants</code>:
     *        <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>.
     * @see SConstants
     */
    public SLabel(Icon icon, int horizontalAlignment) {
        this(null, icon, horizontalAlignment);
    }

    /**
     * Creates a new <code>SLabel</code> instance with the specified icon
     * and the specified text (left alligned).
     *
     * @param text The text to be displayed by the label.
     * @param icon The image to be displayed by the label.
     */
    public SLabel(String text, Icon icon) {
        setText(text);
        setIcon(icon);
        setHorizontalAlignment(LEFT);
    }

    /**
     * Creates a new <code>SLabel</code> instance with the specified icon
     * and the specified text (alligned as specified).
     *
     * @param text The text to be displayed by the label.
     * @param icon The image to be displayed by the label.
     * @param horizontalAlignment One of the following constants defined in
     *        <code>SConstants</code>:
     *        <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>.
     * @see SConstants
     */
    public SLabel(String text, Icon icon, int horizontalAlignment) {
        setText(text);
        setIcon(icon);
        setHorizontalAlignment(horizontalAlignment);
    }

    /**
     * Creates a new <code>SLabel</code> instance with the specified text
     * (alligned as specified) and no icon.
     *
     * @param text The text to be displayed by the label.
     * @param horizontalAlignment One of the following constants defined in
     *        <code>SConstants</code>:
     *        <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>.
     * @see SConstants
     */
    public SLabel(String text, int horizontalAlignment) {
        this(text, null, horizontalAlignment);
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setAlignText(boolean t) {
        alignText = t;
    }

    /**
     * TODO: documentation
     *
     * @param b
     */
    public void setNoBreak(boolean b) {
        noBreak = b;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isNoBreak() { return noBreak; }

    /**
     * Returns the horizontal position of the lable's text
     *
     * @return the position
     * @see SConstants
     * @see #setHorizontalTextPosition
     */
    public int getHorizontalTextPosition() {
        return horizontalTextPosition;
    }

    /**
     * Sets the horizontal position of the lable's text, relative to its icon.
     * <p>
     * The default value of this property is CENTER.
     *
     * @param textPosition One of the following constants defined in
     *        <code>SConstants</code>:
     *        <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>.
     */
    public void setHorizontalTextPosition(int textPosition) {
        horizontalTextPosition = textPosition;
    }

    /**
     * Sets the vertical position of the lable's text, relative to its icon.
     * <p>
     * The default value of this property is CENTER.
     *
     * @param textPosition One of the following constants defined in
     *        <code>SConstants</code>:
     *        <code>TOP</code>, <code>CENTER</code>, <code>BOTTOM</code>.
     */
    public void setVerticalTextPosition(int textPosition) {
        verticalTextPosition = textPosition;
    }

    /**
     * Returns the vertical position of the label's text
     *
     * @return the position
     * @see SConstants
     * @see #setVerticalTextPosition
     */
    public int getVerticalTextPosition() {
        return verticalTextPosition;
    }

    /**
     * TODO: documentation
     *
     * @param gap
     */
    public void setIconTextGap(int gap) {
        iconTextGap = gap;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getIconTextGap() {
        return iconTextGap;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setIcon(Icon i) {
        Icon oldIcon = icon;
        icon = i;
        if ((icon == null && oldIcon != null) ||
            (icon != null && !icon.equals(oldIcon)))
            reload();
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setIcon(URL i) {
        if ( i!=null)
            setIcon(i.toString());
    }

    /**
     * TODO: documentation
     *
     * @param url
     */
    public void setIcon(String url) {
        String oldIconAddress = iconAddress;
        iconAddress = url;
        if ((iconAddress == null && oldIconAddress != null) ||
            (iconAddress != null && !iconAddress.equals(oldIconAddress)))
            reload();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getIconAddress() {
        return iconAddress;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledIcon(Icon i) {
        Icon oldDisabledIcon = disabledIcon;
        disabledIcon = i;
        if ((disabledIcon == null && oldDisabledIcon != null) ||
            (disabledIcon != null && !disabledIcon.equals(oldDisabledIcon)))
            reload();
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledIcon(URL i) {
        if ( i!=null)
            setDisabledIcon(i.toString());
    }

    /**
     * TODO: documentation
     *
     * @param url
     */
    public void setDisabledIcon(String url) {
        String oldDisabledIconAddress = disabledIconAddress;
        disabledIconAddress = url;
        if ((disabledIconAddress == null && oldDisabledIconAddress != null) ||
            (disabledIconAddress != null && !disabledIconAddress.equals(oldDisabledIconAddress)))
            reload();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Icon getDisabledIcon() {
        if(disabledIcon == null)
            if(icon != null && icon instanceof ImageIcon)
                disabledIcon = new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon)icon).getImage()));
        return disabledIcon;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getDisabledIconAddress() {
        return disabledIconAddress;
    }

    /**
     * Returns the text of the label
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the label. If the value of text is null or an empty
     * string, nothing is displayed.
     *
     * @param t The new text
     */
    public void setText(String t) {
        String oldText = text;
        text = t;
        if ((text == null && oldText != null) ||
            (text != null && !text.equals(oldText)))
            reload();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isEscapeSpecialChars() {
	return escapeSpecialChars;
    }

    /**
     * TODO: documentation
     *
     * @deprecated Never set this - characters are always escaped
     *             in future and BTW, you shouldn't rely on an
     *             HTML output, right ?
     * @param escape
     */
    public void setEscapeSpecialChars(boolean escape) {
	escapeSpecialChars = escape;
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(LabelCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
