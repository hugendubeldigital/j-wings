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
package org.wings.dnd;

import java.util.List;

import org.wings.event.SComponentDropListener;

/**
 * @author ole
 *
 */
public interface DropTarget {
    /** 
     * add a DropListener to this Component.
     * Don't forget to register the component with the @link{DragAndDropManager}
     * @param listener the listener to add
     */
    public void addComponentDropListener(SComponentDropListener listener);
    /**
     * get the List of DropListeners
     * @return the list of listeners
     */
    public List getComponentDropListeners();
}
