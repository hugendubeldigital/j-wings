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

import java.util.List;
import java.util.ArrayList;
import javax.swing.event.*;

import org.wings.event.*;

/**
 * Base class for SFrame and SDialog
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public abstract class SWindow
    extends SContainer
{
    private String title;
    
    /**
      * Hold all registered {@link org.wings.event.SWindowListener}'s.
      */
    protected EventListenerList listenerList = new EventListenerList();

    /**
      * Construct a new SWindow.
      */
    public SWindow() {
        super();
    }

    
    /**
      * Construct a new SWindow and set layout to <code>layout</code>
      * @param layout set this layout
      */
    public SWindow(SLayoutManager layout) {
        super(layout);
    }
    
    /**
     * Adds the specified window listener to receive window events from
     * this window.
     * If l is null, no exception is thrown and no action is performed.
     *
     * @param l the window listener
     */ 
    public synchronized void addWindowListener(SWindowListener l) {
        listenerList.add(SWindowListener.class, l);
    }
    
    /**
     * Removes the specified window listener so that it no longer
     * receives window events from this window.
     * If l is null, no exception is thrown and no action is performed.
     *
     * @param l the window listener
     */ 
    public synchronized void removeWindowListener(SWindowListener l) {
        listenerList.remove(SWindowListener.class, l);
    }

    /**
     * Set the title of this window
     *
     * @param t the title string
     */
    public void setTitle(String t) {
        if ( t==null )
            title = "";
        else
            title = t;
    }

    /**
     * Get the title of this window
     *
     * @return the title string
     */
    public String getTitle() { return title; }

    /**
     * Processes events on this window.
     * @param e the event
     */
    protected void processEvent(SWindowEvent e) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] != SWindowListener.class) continue;
            switch(e.getID()) {
                case SWindowEvent.WINDOW_ACTIVATED:
                    ((SWindowListener)listeners[i+1]).windowActivated(e);
                    break;
                case SWindowEvent.WINDOW_CLOSED:
                    ((SWindowListener)listeners[i+1]).windowClosed(e);
                    break;
                case SWindowEvent.WINDOW_CLOSING:
                    ((SWindowListener)listeners[i+1]).windowClosing(e);
                    break;
                case SWindowEvent.WINDOW_DEACTIVATED:
                    ((SWindowListener)listeners[i+1]).windowDeactivated(e);
                    break;
                case SWindowEvent.WINDOW_DEICONIFIED:
                    ((SWindowListener)listeners[i+1]).windowDeiconified(e);
                    break;
                case SWindowEvent.WINDOW_ICONIFIED:
                    ((SWindowListener)listeners[i+1]).windowIconified(e);
                    break;
                case SWindowEvent.WINDOW_OPENED:
                    ((SWindowListener)listeners[i+1]).windowOpened(e);
                    break;
            }
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
