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

import java.util.ArrayList;
import java.util.ListIterator;
import java.awt.Color;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.Action;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * An abstract class, which compounds icon and text. It is the base class for 
 * {@link SAbstractButton} and {@link SClickable}. It supports 7 different icon
 * states. 
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class SAbstractIconTextCompound
    extends SComponent implements ItemSelectable
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
    private String selectionStyle;


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
     * All item listeners.
     */
    protected ArrayList itemListeners;

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
	 * Adds a ItemListener to the button.
	 * @see #removeItemListener(ItemListener)
	 */
	public void addItemListener(ItemListener il) {
	    if (il == null) return;
		if ( itemListeners == null ) {
            itemListeners = new ArrayList();
        }

        itemListeners.add( il );	    
	}

	/**
	 * Returns the selected items or null if no items are selected.
	 */
	public Object[] getSelectedObjects() {
	    return selected?new Object[] {this}:null;
	}

	/**
	 * Remove the given itemListener from list of
	 * item listeners.
	 * @see #addItemListener(ItemListener)
	 */
	public void removeItemListener(ItemListener il) {
	    if (il == null || itemListeners == null) return;
        int index = itemListeners.indexOf( il );
        if ( index == -1 ) return;
        itemListeners.remove( index );
        return;
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
        reloadIfChange(ReloadManager.RELOAD_CODE, icon, i);
        icon = i;
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
        reloadIfChange(ReloadManager.RELOAD_CODE, pressedIcon, i);
        pressedIcon = i;
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
        reloadIfChange(ReloadManager.RELOAD_CODE, rolloverIcon, i);
        rolloverIcon = i;
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
        reloadIfChange(ReloadManager.RELOAD_CODE, rolloverSelectedIcon, i);
        rolloverSelectedIcon = i;
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
        reloadIfChange(ReloadManager.RELOAD_CODE, selectedIcon, i);
        selectedIcon = i;
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
        reloadIfChange(ReloadManager.RELOAD_CODE, disabledSelectedIcon, i);
        disabledSelectedIcon = i;
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
        reloadIfChange(ReloadManager.RELOAD_CODE, disabledIcon, i);
        disabledIcon = i;
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
    public void setSelectionStyle(String selectionStyle) {
        this.selectionStyle = selectionStyle;
    }

    /**
     * @return
     */
    public final String getSelectionStyle() { 
        return selectionStyle; 
    }
    /**
     * Sets the label of the button.
     *
     * @param t
     */
    public void setText(String t) {
        reloadIfChange(ReloadManager.RELOAD_CODE, text, t);
        text = t;
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
     * Toggle the selection. If the new selection
     * is different to the old selection 
     * an {@link ItemEvent} is raised.
     */
    public void setSelected(boolean b) {
        if (b != selected) {
        	selected = b;
        	fireItemStateChanged(new ItemEvent(
        		this, 
        		ItemEvent.ITEM_STATE_CHANGED,
        		this, 
        		b?ItemEvent.SELECTED:ItemEvent.DESELECTED));
        }
		else
			return;

        // this leads to problems, don't know why...
        //        reload(ReloadManager.RELOAD_CODE |
        //        ReloadManager.RELOAD_STYLE);
        reload(ReloadManager.RELOAD_CODE);
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
    
    /**
     * Reports a selection change.
     * @param ie report this event to all listeners
     * @see java.awt.event.ItemListener
     * @see java.awt.ItemSelectable
     */
    protected void fireItemStateChanged( ItemEvent ie)
    {
        if ( itemListeners == null ) return;
        for ( ListIterator it = itemListeners.listIterator(); it.hasNext(); )
            ((ItemListener) it.next()).itemStateChanged(ie);
    }



}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */







