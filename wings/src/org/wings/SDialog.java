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

import javax.swing.JOptionPane;

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

    /*
     * Alle die es interessiert, wann der OptionPane fertig ist. Z.B. um
     * das Ergebnis zu bekommen.
     */
    /**
     * TODO: documentation
     */
    protected ArrayList actionListener = new ArrayList(2);

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

    /**
     * TODO: documentation
     *
     * @param al
     */
    public void addActionListener(ActionListener al) {
        if ( al!=null && !actionListener.contains(al) )
            actionListener.add(al);
    }

    /**
     * TODO: documentation
     *
     * @param al
     */
    public void removeActionListener(ActionListener al) {
        actionListener.remove(al);
    }

    /*
     * Feuert einen ActionEvent an alle registrierten Listener.
     * Als Command dient der Zustand des OptionPanes
     * ({@link #getState()}
     */
    /**
     * TODO: documentation
     *
     * @param state
     */
    protected void fireActionPerformed(String state) {
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, state);

        for ( int i=0; i<actionListener.size(); i++ ) {
            ((ActionListener)actionListener.get(i)).actionPerformed(e);
        }
    }

    /**
     * TODO: documentation
     *
     */
    public void dispose() {
        removeAll();
    }

    /**
     * TODO: documentation
     *
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

        fireActionPerformed("hide");
        actionListener.clear();
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

        while (frame != null && !(frame instanceof SFrame || frame instanceof SInternalFrame) )
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
