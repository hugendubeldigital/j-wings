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

// import javax.swing.event.EventListenerList;

import org.wings.*;
import org.wings.event.*;
import org.wings.plaf.*;

/**
 * Show an dialog.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public class SDialog
    extends SWindow
    implements SGetListener
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "DialogCG";

    /*
     * Alle die es interessiert, wann der OptionPane fertig ist. Z.B. um
     * das Ergebnis zu bekommen.
     */
    /**
     * TODO: documentation
     */
    // protected EventListenerList listenerList = new EventListenerList();

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
    public SDialog() {
        super();
    }

    /**
      * Gets the contentPane object for this dialog.
      * return a {@link org.wings.SContainer}.
      */
    public SContainer getContentPane() {
        return this;
    }

    /**
     * Add a component to this dialog.
     * @deprecated use getContentPane().addComponent(c) instead.
     */
    public SComponent addComponent(SComponent c, Object constraint) {
        return super.addComponent(c, constraint);
        // throw new IllegalArgumentException("use getContentPane().addComponent()");
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
            processEvent(new SWindowEvent(this, SWindowEvent.WINDOW_CLOSING));

            if (frame instanceof SFrame)
                ((SFrame)frame).popDialog(this);
            else
                ((SInternalFrame)frame).popDialog(this);

            processEvent(new SWindowEvent(this, SWindowEvent.WINDOW_CLOSED));
        }
        else
            throw new IllegalStateException("SDialog.frame == null!");
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
     * Shows the dialog
     *
     * @param c the parent component for this dialog
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
            
        processEvent(new SWindowEvent(this, SWindowEvent.WINDOW_OPENED));
    }

    private SWindowEvent event;
    
    public void getPerformed(String name, String value)
    {
        System.out.println("SDialog.getPerformed("+name+","+value+")");
        int id = new Integer(value).intValue();
        switch (id) {
            case SWindowEvent.WINDOW_CLOSED:
                this.hide();
                break;
        }
        event = new SWindowEvent(this, id);
    }

    public void fireIntermediateEvents() {}

    public void fireFinalEvents() {
        processEvent(event);
        event = null;
    }


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
