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

package org.wings.event;

import java.awt.event.*;

/**
 * The listener interface for receiving window events. The class that is
 * interested in processing a window event either implements this interface
 * (and all the methods it contains) or extends the abstract WindowAdapter
 * class (overriding only the methods of interest). The listener object created
 * from that class is then registered with a Window using the winodw's
 * addWindowListener method. When the window's status changes by virtue of
 * being opened, closed, activated or deactivated, iconified or deiconified,
 * the relevant method in the listener object s invoked, and the WindowEvent is
 * passed to it.
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public interface SWindowListener
    extends java.util.EventListener
{

    /**
      * Invoked when the window is set to be the user's active window, 
      * which means the window (or one of its subcomponents) will 
      * receive keyboard events.
      */
    void windowActivated(SWindowEvent e);
    
    /**
      * Invoked when a window has been closed as the result of 
      * calling dispose on the window.
      */
    void windowClosed(SWindowEvent e);
    
    /**
      * Invoked when the user attempts to close the window from the window's 
      * system menu.      
      */
    void windowClosing(SWindowEvent e);
    
    /**
      * Invoked when a window is no longer the user's active window, which
      * means that keyboard events will no longer be delivered to the window 
      * or its subcomponents.
      */
    void windowDeactivated(SWindowEvent e);
    
    /**
      * Invoked when a window is changed from a minimized to a normal state.
      */
    void windowDeiconified(SWindowEvent e);

    /**
      * Invoked when a window is changed from a normal to a minimized state.
      */
    void windowIconified(SWindowEvent e);
    
    /**
      * Invoked the first time a window is made visible.
      */
    void windowOpened(SWindowEvent e);
          
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
