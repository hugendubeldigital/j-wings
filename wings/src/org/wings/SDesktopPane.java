/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import java.io.IOException;
import java.util.*;

import javax.swing.SingleSelectionModel;
import javax.swing.DefaultSingleSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.wings.*;
import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SDesktopPane
    extends SContainer
    implements SConstants
{
    SStackLayout stack = new SStackLayout();

    /**
     * TODO: documentation
     *
     */
    public SDesktopPane() {
        super();
        super.setLayout(stack);
    }

    /**
     * TODO: documentation
     *
     * @param l
     */
    public void setLayout(SLayoutManager l) {}

    /**
     * Returns the currently selected component for this desktopPane.
     * Returns null if there is no component on the stack.
     *
     * @return the top most component
     */
    public SInternalFrame getVisibleFrame() {
        return (SInternalFrame)getComponent(getComponentCount()-1);
    }

    /**
     * Adds a <i>component</i> with a tab title defaulting to
     * the name of the component.
     * Cover method for insertTab().
     * @param component The component to be displayed when this tab is clicked.
     */
    public SComponent add(SComponent component) {
        super.addComponent(component, component.getNamePrefix());
        return component;
    }

    /**
     * Adds a <i>component</i> to the tabbed pane.  If constraints
     * is a String or an Icon, it will be used for the tab title,
     * otherwise the component's name will be used as the tab title.
     * Cover method for insertTab().
     * @param component The component to be displayed when this tab is clicked.
     * @constraints the object to be displayed in the tab
     */
    public void add(SComponent component, Object constraints) {
        super.addComponent(component, constraints);
    }

    /**
     * Removes the tab at <i>index</i>.
     * After the component associated with <i>index</i> is removed,
     * its visibility is reset to true to ensure it will be visible
     * if added to other containers.
     * @param index the index of the tab to be removed
     *
     * @see #addTab
     * @see #insertTab
     */
    public void remove(int index) {
        super.remove(index);
    }

    /**
     * Removes the tab which corresponds to the specified component.
     *
     * @param component the component to remove from the tabbedpane
     */
    public void remove(SComponent component) {
        super.remove(component);
    }

    /**
     * Removes all the tabs from the tabbedpane.
     *
     * @see #addTab
     * @see #removeTabAt
     */
    public void removeAll() {
        removeAll();
    }

    /**
     * Sets the position for the specified component.
     * @param c         the Component to set the layer for
     * @param position  an int specifying the position, where
     *                  0 is the topmost position and
     *                  -1 is the bottommost position
     */
    public void setPosition(SComponent c, int position)  {
        components.remove(c);
        components.add(position, c);

        //remove(c);
        //add(c, null, position);
    }

    /**
     * Returns the index of the specified Component.
     * This is the absolute index, ignoring layers.
     * Index numbers, like position numbers, have the topmost component
     * at index zero. Larger numbers are closer to the bottom.
     *
     * @param c  the Component to check
     * @return an int specifying the component's index
     */
    public int getIndexOf(SComponent c) {
        int i, count;

        count = getComponentCount();
        for(i = 0; i < count; i++) {
            if(c == getComponent(i))
                return i;
        }
        return -1;
    }
    /**
     * Moves the component to the top of the components in it's current layer
     * (position 0).
     *
     * @param c the Component to move
     * @see #setPosition(Component, int)
     */
    public void moveToFront(SComponent c) {
        setPosition(c, 0);
    }

    /**
     * Moves the component to the bottom of the components in it's current layer
     * (position -1).
     *
     * @param c the Component to move
     * @see #setPosition(Component, int)
     */
    public void moveToBack(SComponent c) {
        setPosition(c, getComponentCount());
    }

    /**
     * Get the position of the component.
     *
     * @param c  the Component to check
     * @return an int giving the component's position, where 0 is the
     *         topmost position and the highest index value = the count
     *         count of components minus 1
     *
     * @see #getComponentCountInLayer
     */
    public int getPosition(SComponent c) {
        return getIndexOf(c);
    }

    private class SStackLayout extends SAbstractLayoutManager
    {
        private SContainer container = null;

        /**
         * TODO: documentation
         *
         */
        public SStackLayout() {
        }

        /**
         * TODO: documentation
         *
         */
        public void addComponent(SComponent c, Object constraint) {
        }

        /**
         * TODO: documentation
         *
         * @param c
         */
        public void removeComponent(SComponent c) {}

        /**
         * TODO: documentation
         *
         * @param i
         * @return
         */
        public SComponent getComponentAt(int i) {
            return (SComponent)getComponent(i);
        }

        /**
         * TODO: documentation
         *
         * @param c
         */
        public void setContainer(SContainer c) {
            container = c;
        }

        /**
         * TODO: documentation
         *
         * @param s
         * @throws IOException
         */
        public void write(Device s)
            throws IOException
        {
            int componentCount = getComponentCount();
            for (int i=0; i<componentCount; i++) {
                SComponent comp = (SComponent)getComponent(i);
                if (comp.isVisible()) {
                    comp.write(s);
                    return;
                }
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
