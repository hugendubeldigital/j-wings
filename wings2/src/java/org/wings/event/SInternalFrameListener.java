/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.event;

import java.util.EventListener;

/**
 * The listener interface for receiving internal frame events.
 *
 * @author Holger Engels
 * @version $Revision$
 * @see javax.swing.event.InternalFrameListener
 */
public interface SInternalFrameListener extends EventListener {

    /**
     * Invoked when a internal frame has been opened.
     *
     * @see org.wings.SInternalFrame#show
     */
    public void internalFrameOpened(SInternalFrameEvent e);

    /**
     * Invoked when an internal frame has been closed.
     *
     * @see org.wings.SInternalFrame#setClosed
     */
    public void internalFrameClosed(SInternalFrameEvent e);

    /**
     * Invoked when an internal frame is iconified.
     *
     * @see org.wings.SInternalFrame#setIcon
     */
    public void internalFrameIconified(SInternalFrameEvent e);

    /**
     * Invoked when an internal frame is de-iconified.
     *
     * @see org.wings.SInternalFrame#setIcon
     */
    public void internalFrameDeiconified(SInternalFrameEvent e);

    /**
     * Invoked when an internal frame is maximized.
     *
     * @see org.wings.SInternalFrame#setMaximized
     */
    public void internalFrameMaximized(SInternalFrameEvent e);

    /**
     * Invoked when an internal frame is un-maximized.
     *
     * @see org.wings.SInternalFrame#setMaximized
     */
    public void internalFrameUnmaximized(SInternalFrameEvent e);
}


