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

    protected String title;

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
    }
    /**
     * TODO: documentation
     *
     */
    public SDialog() {}

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setTitle(String t) {
        if ( t==null )
            title = "";
        else
            title = t;
    }
    /**
     * TODO: documentation
     *
     * @return
     */
    public String getTitle() { return title; }

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
        System.err.println("hide");
        if (frame != null) {
            if (frame instanceof SFrame)
                ((SFrame)frame).popDialog();
            else
                ((SInternalFrame)frame).popDialog();
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
            throw new IllegalArgumentException("Component has no parent frame");
        }

        if (frame instanceof SFrame)
            ((SFrame)frame).pushDialog(this);
        else
            ((SInternalFrame)frame).pushDialog(this);
    }

    public void getPerformed(String name, String value) {}

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(DialogCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
