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

import java.util.ArrayList;

import org.wings.plaf.*;
import org.wings.io.Device;
import org.wings.externalizer.ExternalizeManager;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SMenu extends SMenuItem {
    private static final String cgClassID = "MenuCG";

    private boolean fActive = false;

    private boolean fKeepOpen = false;

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
    public SMenu(SIcon i) {
        super(i);
    }

    public SMenu(String text, SIcon icon) {
        super(text, icon);
    }


    /**
     * Set this menu opened or not.
     */
    public void setActive(boolean active) {
        if (active != fActive) {
            fActive = active;
            reload(ReloadManager.RELOAD_CODE);
        }
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
        menuitem.setParentFrame(getParentFrame());
        fItems.add(menuitem);
    }

    /**
     * Add a menu item to this menu.
     */
    public void add(SComponent menuitem) {
        menuitem.setParentFrame(getParentFrame());
        fItems.add(menuitem);
    }

    public void setParentFrame(SFrame f) {
        super.setParentFrame(f);
        for ( int i = 0; i < fItems.size(); i++ )
            ((SComponent) fItems.get( i )).setParentFrame( f );
    }

    /**
     * Add a menu item to this menu.
     */
    public void add(String menuitem) {
        this.add(new SMenuItem(menuitem));
    }


    public SComponent getMenuComponent(int pos) {
        return (SComponent)fItems.get(pos);
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
     * removes a specific menu item component.
     */
    public void remove(SComponent comp) {
        fItems.remove(comp);
    }

    /**
     * Define, if Menu should left open or not, after user
     * clicked menuitem. Normal behaviour is false, that means
     * that the menu closes, after user selected a menuitem.
     */
    public void setKeepOpen(boolean keepOpen)
    {
        this.fKeepOpen = keepOpen;
    }

    /**
     * Close menu when an item was klicked.
     */
    class MenuItemAction implements ActionListener {
        public MenuItemAction(SMenu menu) {
            fMenu = menu;
        }

        public void actionPerformed(ActionEvent e) {
            if (fKeepOpen) return;
            SMenuItem menuitem = (SMenuItem)e.getSource();
            fMenu.setActive(false);
        }

        private SMenu fMenu = null;
    }

    protected final ArrayList fItems = new ArrayList();
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
