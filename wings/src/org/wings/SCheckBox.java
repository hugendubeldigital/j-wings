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

package org.wings;

import java.awt.Color;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.Icon;

import org.wings.plaf.*;
import org.wings.io.Device;

/*
 * Checkboxen sind etwas besondere {@link SFormComponent}, denn
 * ihre eigentliche Identitaet ({@link #getUID}) steckt nicht im Name,
 * sondern im Value. Das ist deswegen so, weil in HTML Gruppen
 * durch den selben Namen generiert werden. Das ist natuerlich
 * problematisch. Eine anderes Problem, welches hier auftaucht ist,
 * das HTML immer nur rueckmeldet, wenn eine Checkbox markiert ist.
 * Deshalb wird hintenan immer ein Hidden Form Element gehaengt,
 * welches rueckmeldet, dass die Checkbox bearbeitet wurde.
 */
/**
 * TODO: documentation
 *
 * @author Dominik Bartenstein, <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SCheckBox extends SButton
{
    private static final String cgClassID = "CheckBoxCG";

    /**
     * TODO: documentation
     */
    protected SButtonGroup group = null;

    /** selection state of the checkBox */
    protected boolean selected = false;

    /*
     * Gibt an, ob das letzte getPerformed von dem hidden Element stammt oder
     * nicht !!
     */
    protected boolean hidden=true;

    /**
     * TODO: documentation
     */
    protected Icon selectedIcon = DEFAULT_SELECTED_ICON;

    /**
     * TODO: documentation
     */
    protected String selectedIconAddress = null;

    /**
     * TODO: documentation
     */
    protected Icon disabledSelectedIcon = DEFAULT_DISABLED_SELECTED_ICON;

    /**
     * TODO: documentation
     */
    protected String disabledSelectedIconAddress = null;

    /**
     * TODO: documentation
     */
    protected Icon backupIcon = null;

    /**
     * TODO: documentation
     */
    protected String backupIconAdr = null;

    /**
     * TODO: documentation
     */
    protected Icon backupDisabledIcon = null;

    /**
     * TODO: documentation
     */
    protected String backupDisabledIconAdr = null;


    private static ResourceImageIcon DEFAULT_SELECTED_ICON =
        new ResourceImageIcon("icons/SelectedCheckBox.gif");

    private static ResourceImageIcon DEFAULT_NOT_SELECTED_ICON =
        new ResourceImageIcon("icons/NotSelectedCheckBox.gif");

    private static ResourceImageIcon DEFAULT_DISABLED_SELECTED_ICON =
        new ResourceImageIcon("icons/DisabledSelectedCheckBox.gif");

    private static ResourceImageIcon DEFAULT_DISABLED_NOT_SELECTED_ICON =
        new ResourceImageIcon("icons/DisabledNotSelectedCheckBox.gif");


    /**
     * TODO: documentation
     *
     */
    public SCheckBox() {
        this(false);
    }

    /**
     * TODO: documentation
     *
     * @param text
     */
    public SCheckBox(String text) {
        this(false);
        setText(text);
    }

    /**
     * TODO: documentation
     *
     * @param selected
     */
    public SCheckBox(boolean selected) {
        setSelected(selected);

        setIcon(DEFAULT_NOT_SELECTED_ICON);
        setDisabledIcon(DEFAULT_DISABLED_NOT_SELECTED_ICON);

        setHorizontalTextPosition(NO_ALIGN);
        setVerticalTextPosition(NO_ALIGN);
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setSelectedIcon(Icon i) {
        selectedIcon = i;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setSelectedIcon(URL i) {
        if ( i!=null)
            setSelectedIcon(i.toString());
    }

    /**
     * TODO: documentation
     *
     * @param url
     */
    public void setSelectedIcon(String url) {
        selectedIconAddress = url;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Icon getSelectedIcon() {
        return selectedIcon;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getSelectedIconAddress() {
        return selectedIconAddress;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledSelectedIcon(Icon i) {
        disabledSelectedIcon = i;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledSelectedIcon(URL i) {
        if ( i!=null)
            setDisabledSelectedIcon(i.toString());
    }

    /**
     * TODO: documentation
     *
     * @param url
     */
    public void setDisabledSelectedIcon(String url) {
        disabledSelectedIconAddress = url;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Icon getDisabledSelectedIcon() {
        return disabledSelectedIcon;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getDisabledSelectedIconAddress() {
        return disabledSelectedIconAddress;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SButtonGroup getGroup() {
        return group;
    }

    /**
     * TODO: documentation
     *
     * @param g
     */
    protected void setGroup(SButtonGroup g) {
        group = g;

        if ( group!=g && getDispatcher()!=null ) {
            getDispatcher().unregister(this);
            getDispatcher().register(this);
        }
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setType(String t) {
        if ( t.equals(SConstants.RADIOBUTTON) )
            super.setType(SConstants.RADIOBUTTON);
        else
            super.setType(SConstants.CHECKBOX);
    }


    /*
     * Setzt, falls angehoeriger eine {@link SButtonGroup}, sich
     * selbst als das selektierte Element.
     */
    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setSelected(boolean s) {
        boolean oldSelection = selected;
        selected = s;

        if ( group!=null ) {
            group.setSelected(this, selected);
        }

        if ( oldSelection!=selected ) {
            fireActionPerformed();
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * TODO: documentation
     *
     */
    public void backupIcons() {
        backupDisabledIconAdr = getDisabledIconAddress();
        backupIconAdr = getIconAddress();
        backupDisabledIcon = getDisabledIcon();
        backupIcon = getIcon();
    }

    /**
     * TODO: documentation
     *
     */
    public void restoreIcons() {
        setDisabledIcon(backupDisabledIconAdr);
        setIcon(backupIconAdr);
        setDisabledIcon(backupDisabledIcon);
        setIcon(backupIcon);
    }

    /*
     * Zuerst bekommt die Komponente die Action von der Checkbox (falls
     * diese selektiert wurde). Danach die von der Hidden
     * Komponente. Falls die Selektionsaction nicht kam, wird die
     * Selektion zurueckgesetzt.
     */
    public void getPerformed(String action, String value) {
        // System.out.println("getPerformed " + action + " : " + value);
        // nur falls es wirklich meins ist (bei group ist die action fuer alle
        // CheckBoxes in der Group gleich !!!)
        // System.out.println("getPerformed " + action + ":" + value);

        if ( value.startsWith(getUnifiedIdString()+SConstants.UID_DIVIDER) ) {

            if (!getShowAsFormComponent()) {
                setSelected(!isSelected());
                return;
            }

            if ( value.endsWith("1") ) {
                hidden=false;
                setSelected(true);
            } else {
                if ( hidden )
                    setSelected(false);
                hidden = true;
            }
        }
    }

    /*
     * Abhaengig, ob der Button zu einer Gruppe gehoert, wird deren Name
     * oder der eigene zurueckgegeben.
     */
    /**
     * TODO: documentation
     *
     * @return
     */
    public String getNamePrefix() {
        if ( group==null )
            return super.getNamePrefix();
        else
            return group.getNamePrefix();
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "CheckBoxCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
