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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.event.EventListenerList;

import org.wings.*;
import org.wings.plaf.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SDialog
    extends SForm
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "DialogCG";

    /**
     * TODO: documentation
     */
    public static final String OK_ACTION = "OK";

    /**
     * TODO: documentation
     */
    public static final String CANCEL_ACTION = "CANCEL";

    /**
     * TODO: documentation
     */
    public static final String UNKNOWN_ACTION = "UNKNOWN";

    /*
     * Alle die es interessiert, wann der OptionPane fertig ist. Z.B. um
     * das Ergebnis zu bekommen.
     */
    /**
     * TODO: documentation
     */
    //protected EventListenerList listenerList = new EventListenerList();

    /**
     * TODO: documentation
     */
    protected SContainer frame = null;

    /**
     * TODO: documentation
     *
     * @param layout
     */
    public SDialog(SLayoutManager layout) {
        super(layout);
        setBorder(new SLineBorder());
    }
    /**
     * TODO: documentation
     *
     */
    public SDialog() {
        setBorder(new SLineBorder());
    }

    /*
    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    protected void fireActionPerformed(ActionEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == ActionListener.class) {
                ((ActionListener)listeners[i+1]).actionPerformed(e);
            }
        }

        listenerList = new EventListenerList();
    }
    */
    protected void fireActionPerformed(String state) {
        setActionCommand(state);
        //ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, state);
        //fireActionPerformed(e);
        fireActionPerformed();
        listenerList = new EventListenerList();
    }

    /**
     * TODO: documentation
     *
     */
    public void dispose() {
        removeAll();
    }

    /**
     * Remove this dialog from its frame.
     */
    public void hide() {
        if (frame != null) {
            if (frame instanceof SFrame) {
                ((SFrame)frame).setOptionPane(null);
                ((SFrame)frame).showContentPane();
            }
            else {
                ((SInternalFrame)frame).setOptionPane(null);
                ((SInternalFrame)frame).showContentPane();
            }
        }
    }

    /**
     * sets the frame in which the option pane is displayed
     */
    protected void setFrame(SFrame f) {
        frame = f;
    }

    /**
     * sets the frame in which the option pane is displayed
     */
    protected void setFrame(SInternalFrame f) {
        frame = f;
    }

    /**
     * shows the option pane
     *
     * @param c
     */
    public void show(SComponent c) {
        if (c instanceof SContainer)
            frame = (SContainer)c;
        else
            frame = c.getParent();

        while (frame != null && !(frame instanceof SFrame || frame instanceof SInternalFrame))
            frame = frame.getParent();

        if (frame == null) {
            throw new IllegalArgumentException("No parent Frame");
        }

        if (frame instanceof SFrame) {
            ((SFrame)frame).setOptionPane(this);
            ((SFrame)frame).showOptionPane();
        }
        else {
            ((SInternalFrame)frame).setOptionPane(this);
            ((SInternalFrame)frame).showOptionPane();
        }
    }

    public void getPerformed(String name, String value) {}

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "DialogCG"
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
