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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;

import org.wings.session.SessionManager;

/**
 * This class is used to create a multiple-exclusion scope for a set of 
 * buttons. Creating a set of buttons with the same ButtonGroup object means
 * that turning "on" one of those buttons turns off all other buttons in the 
 * group. 
 *
 * <p>A SButtonGroup can be used with any set of objects that inherit from
 * {@link SCheckBox}, because they support the selected state.
 *
 * <p>Initially, all buttons in the group are unselected. Once any button is 
 * selected, one button is always selected in the group. There is no way to
 * turn a button programmatically to "off", in order to clear the button 
 * group.
 *
 * <p><em>Details:</em>The implementation of the button group is a 
 * bit tricky for the HTML generation. In HTML, groups of components are
 * usually formed by giving them all the same name. The problem is, that 
 * any {@link SComponent}, especially the {@link SCheckBox}, have globally 
 * <em>unique</em> names. So this implementation gives all buttons in the
 * group the name of this SButtonGroup, and sets their <em>value</em> to
 * their actual name. So a bit of dispatching is already done here.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 * @see javax.swing.ButtonGroup
 */
public class SButtonGroup
{
    /**
     * TODO: documentation
     */
    public static final String SELECTION_CHANGED = "SelectionChanged";

    /**
     * TODO: documentation
     */
    protected ArrayList buttons = new ArrayList(2);

    /**
     * TODO: documentation
     */
    protected SCheckBox selection = null;

    /* */
    private transient String unifiedId = null;

    /**
     * TODO: documentation
     */
    protected ArrayList actionListener = null;

    /**
     * TODO: documentation
     *
     */
    public SButtonGroup() {}

    /**
     * Return a jvm wide unique id.
     * @return an id
     */
    public final String getUnifiedId() {
        if ( unifiedId==null )
            unifiedId = SessionManager.getSession().createUniqueId();
        return unifiedId;
    }

    /*
     * Konzeptionell richtiger waere hier SAbstractButton. Macht aber keinen
     * Sinn. Macht nur Sinn abstract Checkboxes(eventuell nur RadioButtons ???).
     */
    /**
     * TODO: documentation
     *
     * @param button
     */
    public void add(SCheckBox button) {
        if ( buttons!=null && !buttons.contains(button) ) {
            buttons.add(button);
            if ( selection==null && button.isSelected() )
                selection = button;
            button.setGroup(this);
        }
    }

    /**
     * TODO: documentation
     *
     * @param button
     */
    public void remove(SCheckBox button) {
        if ( button==null )
            return;

        buttons.remove(button);
        if ( button==selection )
            selection=null;
        button.setGroup(null);
    }

    /**
     * TODO: documentation
     *
     */
    public void removeAll() {
        while ( buttons.size()>0 )
            remove((SCheckBox)buttons.get(0));
    }


    /**
     * TODO: documentation
     *
     * @return
     */
    public SCheckBox getSelection() {
        if ( selection!=null && selection.isSelected() )
            return selection;
        else
            return null;
    }

    public void setSelected(SCheckBox b, boolean selected) {
        if ( selected && b!=selection ) {
            SCheckBox oldSelection = selection;
            selection = b;
            selection.setSelected(true);
            if ( oldSelection!=null ) {
                oldSelection.setSelected(false);
            }
            fireActionPerformed();
        }
    }

    /**
     * TODO: documentation
     *
     * @param button
     * @return
     */
    public boolean isSelected(SCheckBox button) {
        return button==getSelection();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Iterator getIterator() {
        return buttons.iterator();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Enumeration getElements() {
        return java.util.Collections.enumeration(buttons);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getNamePrefix() {
        if ( buttons!=null && buttons.size()>0 ) {
            SCheckBox b = (SCheckBox)buttons.get(0);
            if (b.getParentFrame() != null)
                return b.getParentFrame().getEventEpoch() + SConstants.UID_DIVIDER +
                    getUnifiedId(); // + SConstants.UID_DIVIDER;
        }
        return unifiedId + SConstants.UID_DIVIDER;
    }

    /**
     * TODO: documentation
     *
     * @param al
     */
    public void addActionListener(ActionListener al) {
        if ( actionListener==null )
            actionListener = new ArrayList(2);
        actionListener.add(al);
    }

    /**
     * TODO: documentation
     *
     * @param al
     */
    public void removeActionListener(ActionListener al) {
        if ( actionListener==null )
            return;
        actionListener.remove(al);
    }

    /**
     * TODO: documentation
     *
     */
    protected void fireActionPerformed() {
        if ( actionListener!=null &&
             actionListener.size()>0 ) {
            ActionEvent e = new ActionEvent(this,
                                            ActionEvent.ACTION_PERFORMED,
                                            SELECTION_CHANGED);

            for ( int i=0; i<actionListener.size(); i++ )
                ((ActionListener)actionListener.get(i)).actionPerformed(e);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
