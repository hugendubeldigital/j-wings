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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import javax.swing.event.EventListenerList;
import javax.swing.Action;

import org.wings.plaf.*;
import org.wings.io.Device;
import org.wings.style.Style;

/**
 * An abstract class, which compounds icon and text. It is the base class for 
 * {@link SAbstractButton} and {@link SClickable}. It supports 7 different icon
 * states. 
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class SAbstractIconTextCompound
    extends SComponent
{

    public static final int ICON_COUNT = 7;
    public static final int DISABLED_ICON = 0;
    public static final int DISABLED_SELECTED_ICON = 1;
    public static final int ENABLED_ICON = 2;
    public static final int SELECTED_ICON = 3;
    public static final int ROLLOVER_ICON = 4;
    public static final int ROLLOVER_SELECTED_ICON = 5;
    public static final int PRESSED_ICON = 6;

    /** the text the button is showing */
    private String text;

    /** selection state */
    private boolean selected = false;

    /**
     * The icon to be displayed
     */
    private SIcon icon;

    /**
     * TODO: documentation
     */
    private SIcon disabledIcon;

    /**
     * TODO: documentation
     */
    private SIcon selectedIcon;

    /**
     * TODO: documentation
     */
    private SIcon pressedIcon;

    /**
     * TODO: documentation
     */
    private SIcon disabledSelectedIcon;

    /**
     * TODO: documentation
     */
    private SIcon rolloverIcon;

    /**
     * TODO: documentation
     */
    private SIcon rolloverSelectedIcon;

    /**
     * TODO: documentation
     */
    private Style selectionStyle;


    /**
     * TODO: documentation
     */
    private int verticalTextPosition = CENTER;

    /**
     * TODO: documentation
     */
    private int horizontalTextPosition = RIGHT;

    /**
     * TODO: documentation
     */
    private int iconTextGap = 0;

    /**
     * TODO: documentation
     */
    private boolean alignText = false;

    /**
     * TODO: documentation
     */
    private boolean escapeSpecialChars = true;

    /**
     * If the text of the button should not be wrapped, set this to true. This
     * inserts a &lt;NOBREAK&gt; Tag around the label
     */
    protected boolean noBreak = false;

    /**
     *
     */
    private boolean imageAbsBottom = false;

    /**
     * Create a button with given text.
     * @param text the button text
     */
    public SAbstractIconTextCompound(String text) {
        setText(text);
    }

    /**
     * Creates a new submit button
     */
    public SAbstractIconTextCompound() {
        this("");
    }

    /**
     * TODO: documentation
     *
     * @param textPosition
     */
    public void setHorizontalTextPosition(int textPosition) {
        horizontalTextPosition = textPosition;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final int getHorizontalTextPosition() {
        return horizontalTextPosition;
    }

    /**
     * TODO: documentation
     *
     * @param textPosition
     */
    public void setVerticalTextPosition(int textPosition) {
        verticalTextPosition = textPosition;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final int getVerticalTextPosition() {
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
    public final int getIconTextGap() {
        return iconTextGap;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setIcon(SIcon i) {
        if ( isDifferent(icon, i) ) {
            icon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final SIcon getIcon() {
        return icon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setPressedIcon(SIcon i) {
        if ( isDifferent(pressedIcon, i) ) {
            pressedIcon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final SIcon getPressedIcon() {
        return pressedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setRolloverIcon(SIcon i) {
        if ( isDifferent(rolloverIcon, i) ) {
            rolloverIcon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final SIcon getRolloverIcon() {
        return rolloverIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setRolloverSelectedIcon(SIcon i) {
        if ( isDifferent(rolloverSelectedIcon, i) ) {
            rolloverSelectedIcon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final SIcon getRolloverSelectedIcon() {
        return rolloverSelectedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setSelectedIcon(SIcon i) {
        if ( isDifferent(selectedIcon, i) ) {
            selectedIcon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final SIcon getSelectedIcon() {
        return selectedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledSelectedIcon(SIcon i) {
        if ( isDifferent(disabledSelectedIcon, i) ) {
            disabledSelectedIcon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final SIcon getDisabledSelectedIcon() {
        return disabledSelectedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledIcon(SIcon i) {
        if ( isDifferent(disabledIcon, i) ) {
            disabledIcon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SIcon getDisabledIcon() {
        if(disabledIcon == null) {
            /**** TODO
                  if(icon != null && icon instanceof ImageIcon)
                  disabledIcon = new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon)icon).getImage()));
            ***/
        }
        return disabledIcon;
    }

    /**
     * If the text of the button should not be wrapped, set this to true. This
     * inserts a &lt;NOBREAK&gt; Tag around the label
     * @see #isNoBreak()
     * @param b
     */
    public void setNoBreak(boolean b) {
        noBreak = b;
    }

    /**
     * Test, if "noBreak" is set for this button.
     * @see #setNoBreak(boolean)
     * @return true, if nobreak is set, false otherwise.
     */
    public final boolean isNoBreak() {
        return noBreak;
    }

    /**
     * @param selectionStyle
     */
    public void setSelectionStyle(Style selectionStyle) {
        this.selectionStyle = selectionStyle;
    }

    /**
     * @return
     */
    public final Style getSelectionStyle() { 
        return selectionStyle; 
    }
    /**
     * Sets the label of the button.
     *
     * @param t
     */
    public void setText(String t) {
        if ( isDifferent(text, t) ) {
            text = t;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final boolean isSelected() {
        return selected;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public void setSelected(boolean b) {
        selected = b;

        reload(ReloadManager.RELOAD_CODE | ReloadManager.RELOAD_STYLE);
    }

    /**
     *
     * @param t
     */
    public void setImageAbsBottom(boolean t) {
        imageAbsBottom = t;
    }

    /**
     *
     * @param t
     */
    public boolean isImageAbsBottom() {
        return imageAbsBottom;
    }



    /**
     * Sets the proper icons for buttonstatus enabled resp. disabled.
     */
    public void setIcons(SIcon[] icons) {
        setIcon(icons[ENABLED_ICON]);
        setDisabledIcon(icons[DISABLED_ICON]);
        setDisabledSelectedIcon(icons[DISABLED_SELECTED_ICON]);
        setRolloverIcon(icons[ROLLOVER_ICON]);
        setRolloverSelectedIcon(icons[ROLLOVER_SELECTED_ICON]);
        setPressedIcon(icons[PRESSED_ICON]);
        setSelectedIcon(icons[SELECTED_ICON]);
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */







