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
 * TODO: Creates A Modal Dialog
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SDialog extends SForm
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "DialogCG";

    /**
     * The Title of the Dialog Frame
     */
    protected String title;


    /**
     * The parent of the Dialog
     */
    protected SRootContainer root = null;


    /**
     * Creates a non Modal Dialog without parent <code>SFrame</code> or <code>SDialog</code>
     * and without Title
     */
    public SDialog() {
      this((SFrame)null, false);
    }


    /**
     * Creates a non-modal dialog without a title with 
     * specifed parent <code>SFrame</code> as its owner.
     *
     * @param owner the parent <code>SFrame</code> 
     */
    public SDialog(SFrame owner) {
        this(owner, false);
    }

    /**
     * Creates a modal or non-modal dialog without a title and
     * with the specified owner <code>Frame</code>.
     *
     * @param owner the parent <code>SFrame</code> 
     * @param modal  true for a modal dialog, false for one that allows
     *               others windows to be active at the same time
     *               currently not very usefull with Wings
     */
    public SDialog(SFrame owner, boolean modal) {
        this(owner, null, modal);
    }

    /**
     * Creates a non-modal dialog with the specified title and
     * with the specified owner frame.
     *
     * @param owner the parent <code>SFrame</code> 
     * @param title  the <code>String</code> to display as titke
     */
    public SDialog(SFrame owner, String title) {
        this(owner, title, false);     
    }

    /**
     * Creates a modal or non-modal dialog with the specified title 
     * and the specified owner <code>SFrame</code>.  All constructors
     * defer to this one.
     *
     * @param owner the parent <code>SFrame</code> 
     * @param title  the <code>String</code> to display as titke
     * @param modal  true for a modal dialog, false for one that allows
     *               other windows to be active at the same time
     *               currently not very usefull with Wings
     */
    public SDialog(SFrame owner, String title, boolean modal) {
      super();
      setTitle(title);
      setFrame(owner);
    }

    public void setTitle(String t) {
        if ( t==null )
            title = "";
        else
            title = t;
    }
    /**
     * Emits the current Title of the Dialog
     *
     * @return Title of the Dialog
     */
    public String getTitle() { 
      return title; 
    }

    protected void fireActionPerformed(String state) {
        setActionCommand(state);
        //ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, state);
        //fireActionPerformed(e);
        fireActionPerformed();
        //listenerList = new EventListenerList();
    }

    /**
     * Removes all <code>SComponents</code> from the pane
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

    public void setVisible(boolean visible) {
      super.setVisible(visible);
      if (visible) {
        if (root != null) show(root);
      } else {
        if (isVisible()) hide();
      }
    }
    
    /**
     * sets the root container in which this dialog is to be displayed.
     */
    protected void setFrame(SRootContainer f) {
        root = f;
    }

    /**
     * shows this dialog in the given SRootContainer. If the component is
     * not a root container, then the root container the component is in
     * is used.
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
