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

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * TODO: documentation
 *
 * @author
 * @version $Revision$
 */
public class ListSelectionSupport
{
    protected Object source;

    /**
     * The selection mode. This might by one of the following values:
     * <UL>
     * <LI> {@link SConstants#NO_SELECTION}
     * <LI> {@link SConstants#SINGLE_SELECTION}
     * <LI> {@link SConstants#MULTIPLE_SELECTION}
     * </UL>
     */
    protected int selectionMode = SConstants.NO_SELECTION;

    /**
     * TODO: documentation
     */
    protected ArrayList selectionListener = new ArrayList(2);

    /**
     * TODO: documentation
     *
     * @param source
     */
    public ListSelectionSupport(SComponent source) {
        this.source = source;
    }

    /*
     * Sets the selection mode. Use one of the following values:
     * <UL>
     * <LI> {@link SConstants#NO_SELECTION}
     * <LI> {@link SConstants#SINGLE_SELECTION}
     * <LI> {@link SConstants#MULTIPLE_SELECTION}
     * </UL>
     */
    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setSelectionMode(int s) {
        // keep this in mind!
        //clearSelection();

        selectionMode = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getSelectionMode() {
        return selectionMode;
    }

    /**
     * TODO: documentation
     *
     * @param lsl
     */
    public void addListSelectionListener(ListSelectionListener lsl) {
        selectionListener.add(lsl);
    }

    /**
     * TODO: documentation
     *
     * @param lsl
     */
    public void removeListSelectionListener(ListSelectionListener lsl) {
        selectionListener.remove(lsl);
    }

    /*
     * Feuert einen ListSelectionEvent, indem der Event in die Event
     * Queue des aktuellen Threads gestellt wird.
     */
    /**
     * TODO: documentation
     *
     * @param index
     */
    protected void fireSelectionPerformed(int index) {
        ListSelectionEvent e = new ListSelectionEvent(source, index, index, false);

        for (int i=0; i<selectionListener.size(); i++)
            SForm.fireListSelectionEvent((ListSelectionListener)selectionListener.get(i), e);
    }

    /*
     * Feuert eine ListSelectionEvent unter Umgehung der Event Queue des
     * aktuellen Threads. Das wird immer dann benoetigt, wenn ein
     * ListSelectionEvent durch einen anderen Event getriggert wird
     * (z.B. ActionEvent einer Checkbox bei Table Selektion)
     */
    /**
     * TODO: documentation
     *
     * @param index
     */
    protected void fireSelectionPerformedImmediately(int index) {
        ListSelectionEvent e = new ListSelectionEvent(source, index, index, false);

        for (int i=0; i<selectionListener.size(); i++)
            ((ListSelectionListener)selectionListener.get(i)).valueChanged(e);
    }
}


/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
