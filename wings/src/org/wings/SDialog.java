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
    protected SRootContainer root = null;

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
        if (root != null) {
            root.popDialog();
        }
    }

    /**
     * sets the root container in which this dialog is to be displayed.
     */
    protected void setFrame(SRootContainer f) {
        root = f;
    }

    /**
     * shows this dialog in the given frame. If the component given is
     * not a frame, then it is shown in the frame, the component resides
     * in.
     *
     * @param c
     */
    public void show(SComponent c) {
        SContainer frame = null;
        if (c instanceof SContainer)
            frame = (SContainer)c;
        else
            frame = c.getParent();

        // find RootContainer
        while (frame != null && !(frame instanceof SRootContainer)) {
            frame = frame.getParent();
        }
        
        if (frame == null) {
            throw new IllegalArgumentException("Component has no root container");
        }
        root = (SRootContainer) frame;

        root.pushDialog(this);
    }

    public void processRequest(String name, String[] values) {}

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
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
