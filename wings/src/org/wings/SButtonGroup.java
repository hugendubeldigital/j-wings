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

/*
 * Eine stinknormale ButtonGroup :-). Steckt aber schon ein bisschen
 * Grips dahinter. In HTML werden Gruppen dadurch generiert, dass sie
 * denselben Namen haben. Das ist natuerlich problematisch, da jede
 * {@link SComponent}, insbesondere {@link SCheckBox}, eine
 * eindeutige Bezeichnug als Name hat. Der Trick besteht darin, dass
 * Buttons in dieser ButtonGroup den Namen dieser ButtonGroup als
 * Namen haben, und im Value ihren eigentlichen Namen
 * kodieren. D.h. sie uebernehmen ein wenig Dispatcher Arbeit.
 */
/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
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
