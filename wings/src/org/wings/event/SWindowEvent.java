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

import org.wings.*;
import java.awt.AWTEvent;

/**
 * Used to notify {@link org.wings.event.SWindowListener}'s about window-changes.
 * Not all Swing-actions are currently implemented!
 * @see org.wings.event.SWindowListener
 * @see org.wings.SWindow#addWindowListener(SWindowListener)
 * @see org.wings.SFrame
 * @see org.wings.SDialog
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public class SWindowEvent
    extends java.awt.AWTEvent
{
    /**
     * The first number in the range of ids used for window events.
     */
    public static final int WINDOW_FIRST        = 200;

    /**
     * The last number in the range of ids used for window events.
     */
    public static final int WINDOW_LAST         = 206;

    /**
     * The window opened event.  This event is delivered only
     * the first time a window is made visible.
     */
    public static final int WINDOW_OPENED	= WINDOW_FIRST; // 200

    /**
     * The "window is closing" event. This event is delivered when
     * the user attempts to close the window from the window's system menu.  
     * If the program does not explicitly hide or dispose the window
     * while processing this event, the window close operation will be
     * cancelled.
     */
    public static final int WINDOW_CLOSING	= 1 + WINDOW_FIRST; //Event.WINDOW_DESTROY

    /**
     * The window closed event. This event is delivered after
     * the window has been closed as the result of a call to dispose.
     */
    public static final int WINDOW_CLOSED	= 2 + WINDOW_FIRST;

    /**
     * The window iconified event. This event is delivered when
     * the window has been changed from a normal to a minimized state.
     * For many platforms, a minimized window is displayed as
     * the icon specified in the window's iconImage property.
     * @see java.awt.Frame#setIconImage
     */
    public static final int WINDOW_ICONIFIED	= 3 + WINDOW_FIRST; //Event.WINDOW_ICONIFY

    /**
     * The window deiconified event type. This event is delivered when
     * the window has been changed from a minimized to a normal state.
     */
    public static final int WINDOW_DEICONIFIED	= 4 + WINDOW_FIRST; //Event.WINDOW_DEICONIFY

    /**
     * The window activated event type. This event is delivered
     * when the window becomes the user's active window, which means
     * that the window (or one of its subcomponents) will receive 
     * keyboard events.
     */
    public static final int WINDOW_ACTIVATED	= 5 + WINDOW_FIRST;

    /**
     * The window deactivated event type. This event is delivered
     * when a window is no longer the user's active window, which
     * means keyboard events will no longer be delivered to the
     * window or its subcomponents.
     */
    public static final int WINDOW_DEACTIVATED	= 6 + WINDOW_FIRST;

    /**
     * Constructs a ComponentEvent object.
     * @param aSource the Component object that originated the event
     * @param aId an integer indicating the type of event
     */
    public SWindowEvent(SWindow aSource, int aId) {
        super(aSource, aId);
    }

    /**
     * Returns the originator of the event.
     * @return the SWindow object that originated the event
     */
    public SComponent getWindow() {
        return (SComponent) source;
    }

    public String toString() {
        return "SWindowEvent[source=" + source + "; " + paramString() + "]";
    }

    /**
     * Returns a string representing the state of this event. This 
     * method is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between implementations.
     * The returned string may be empty but may not be <tt>null</tt>.
     * @return a string representation of this event.          
     */
    public String paramString() {
        switch (id) {
        case WINDOW_ACTIVATED:
            return  "WINDOW_ACTIVATED";
        case WINDOW_CLOSED:
            return "WINDOW_CLOSED";
        case WINDOW_CLOSING:
            return "WINDOW_CLOSING";
        case WINDOW_DEACTIVATED:
            return "WINDOW_DEACTIVATED";
        case WINDOW_DEICONIFIED:
            return "WINDOW_DEICONIFIED";
        case WINDOW_ICONIFIED:
            return "WINDOW_ICONIFIED";
        case WINDOW_OPENED:
            return "WINDOW_OPENED";
        default:
            return "unknown type";
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
