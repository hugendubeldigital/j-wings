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

import org.wings.plaf.*;
import org.wings.style.*;
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

    /**
     * TODO: documentation
     */
    protected SIcon selectedIcon = DEFAULT_SELECTED_ICON;

    /**
     * TODO: documentation
     */
    protected SIcon disabledSelectedIcon = DEFAULT_DISABLED_SELECTED_ICON;

    /**
     * TODO: documentation
     */
    protected String selectedStyle = null;

    /**
     * TODO: documentation
     */
    protected Style disabledSelectedStyle = null;

    /**
     * TODO: documentation
     */
    protected SIcon backupIcon = null;

    /**
     * TODO: documentation
     */
    protected SIcon backupDisabledIcon = null;

    /**
     * TODO: documentation
     */
    protected Style backupStyle = null;

    /**
     * TODO: documentation
     */
    protected Style backupDisabledStyle = null;


    private static ResourceImageIcon DEFAULT_SELECTED_ICON =
        new ResourceImageIcon("org/wings/icons/SelectedCheckBox.gif");

    private static ResourceImageIcon DEFAULT_NOT_SELECTED_ICON =
        new ResourceImageIcon("org/wings/icons/NotSelectedCheckBox.gif");

    private static ResourceImageIcon DEFAULT_DISABLED_SELECTED_ICON =
        new ResourceImageIcon("org/wings/icons/DisabledSelectedCheckBox.gif");

    private static ResourceImageIcon DEFAULT_DISABLED_NOT_SELECTED_ICON =
        new ResourceImageIcon("org/wings/icons/DisabledNotSelectedCheckBox.gif");


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
    public void setSelectedIcon(SIcon i) {
        if ( i!=selectedIcon || i!=null && !i.equals(selectedIcon) ) {
            selectedIcon = i;
            reload();
        } 
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SIcon getSelectedIcon() {
        return selectedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledSelectedIcon(SIcon i) {
        if ( i!=disabledSelectedIcon || 
             i!=null && !i.equals(disabledSelectedIcon) ) {
            disabledSelectedIcon = i;
            reload();
        } 
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SIcon getDisabledSelectedIcon() {
        return disabledSelectedIcon;
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
     * Setzt, falls angehoeriger einer {@link SButtonGroup}, sich
     * selbst als das selektierte Element.
     */
    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setSelected(boolean selected) {
        boolean oldSelected = this.selected;
        this.selected = selected;

        if (group != null)
            group.setSelected(this, selected);
        if (oldSelected != selected)
            reload();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    public void doClick() {
        setSelected(!isSelected());

        fireActionPerformed();
    }

    /**
     * TODO: documentation
     *
     */
    public void backupIcons() {
        backupDisabledIcon = getDisabledIcon();
        backupIcon = getIcon();
    }

    /**
     * TODO: documentation
     *
     */
    public void restoreIcons() {
        setDisabledIcon(backupDisabledIcon);
        setIcon(backupIcon);
    }

    /*
     * Zuerst bekommt die Komponente die Action von der Checkbox (falls
     * diese selektiert wurde). Danach die von der Hidden
     * Komponente. Falls die Selektionsaction nicht kam, wird die
     * Selektion zurueckgesetzt.
     */
    public void processRequest(String action, String[] values) {
        String uid = getUnifiedIdString()+SConstants.UID_DIVIDER;

        if ( getShowAsFormComponent() ) {
            for ( int i=0; i<values.length; i++ ) {
                // the request is for this component only, if value starts with
                // its unified IdString (ButtonGroup)
                if ( values[i].startsWith(uid) ) {
                    if (values[i].endsWith("1")) {
                        setSelected(true);
                        SForm.addArmedComponent(this);
                        return;
                    } else {
                        setSelected(false);
                        SForm.addArmedComponent(this);
                    }
                }
            }
        } else {
            for ( int i=0; i<values.length; i++ ) {
                // the request is for this component only, if value starts with
                // its unified IdString (ButtonGroup)
                if ( values[i].startsWith(uid) ) {
                    setSelected(!isSelected());
                    SForm.addArmedComponent(this);
                    return;
                }
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

    public void fireIntermediateEvents() {
        // TODO: fire item events
        fireActionPerformed();
    }

    public void fireFinalEvents() {}

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(CheckBoxCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
