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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import java.util.Vector;
import java.net.URL;
import javax.swing.*;

import java.util.ArrayList;

import org.wings.plaf.*;
import org.wings.io.Device;
import org.wings.externalizer.ExternalizeManager;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:andre.lison@crosstec.de">Andre Lison</a>
 * @author Dominik Bartenstein
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SMenu extends SButton
{
    private static final String cgClassID = "MenuCG";
    private boolean fActive = false;

    /**
     * TODO: documentation
     *
     * @param text
     */
    public SMenu(String text) {
        super(text);
    }

    /**
     * TODO: documentation
     *
     */
    public SMenu() {
        super();
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public SMenu(Icon i) {
        super(i);
        setIcon(i);
    }


    /**
     * Set this menu opened or not.
     */
    public void setActive(boolean active) {
        fActive = active;
    }

    /**
     * Is this menu opened or not
     */
    public boolean isActive() {
        return fActive;
    }

    /**
     * Add a menu item to this menu.
     */
    public void add(SMenuItem menuitem) {
        menuitem.addActionListener(new MenuItemAction(this));
        fItems.add(menuitem);
    }

    /**
     * Add a menu item to this menu.
     */
    public void add(SComponent menuitem) {
        fItems.add(menuitem);
    }

    public void setParentFrame(SFrame f) {
        super.setParentFrame(f);
        for ( int i = 0; i < fItems.size(); i++ )
            ((SComponent) fItems.elementAt( i )).setParentFrame( f );
    }

    /**
     * Add a menu item to this menu.
     */
    public void add(String menuitem) {
        this.add(new SMenuItem(menuitem));
    }


    public SComponent getMenuComponent(int pos) {
        return (SComponent)fItems.elementAt(pos);
    }

    /**
     * Return the number of items on the menu, including separators.
     */
    public int getMenuComponentCount() {
        return fItems.size();
    }

    /**
     * Remove all {@link SMenuItem} from this menu.
     */
    public void removeAll() {
        fItems.clear();
    }

    /**
     * Removes the menu item at specified index from the menu.
     */
    public void remove(int pos) {
        fItems.remove(pos);
    }

    /**
     * Close menu when an item was klicked.
     */
    class MenuItemAction implements ActionListener {
        public MenuItemAction(SMenu menu) {
            fMenu = menu;
        }

        public void actionPerformed(ActionEvent e) {
            SMenuItem menuitem = (SMenuItem)e.getSource();
            fMenu.setActive(false);
        }

        private SMenu fMenu = null;
    }

    protected Vector fItems = new Vector();
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
