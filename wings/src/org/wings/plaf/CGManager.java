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

package org.wings.plaf;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

import org.wings.*;
import org.wings.session.*;
import org.wings.style.*;

public class CGManager
{
    private LookAndFeel lookAndFeel;

    public Object get(String key) {
        return getDefaults().get(key);
    }

    public Object getObject(String key, Class clazz) {
        return getDefaults().get(key, clazz);
    }

    public ComponentCG getCG(SComponent target) {
        return (ComponentCG)getDefaults().get(target.getCGClassID(), ComponentCG.class);
    }

    public LayoutCG getCG(SLayoutManager target) {
        return (LayoutCG)getDefaults().get(target.getCGClassID(), LayoutCG.class);
    }

    public BorderCG getCG(SBorder target) {
        return (BorderCG)getDefaults().get(target.getCGClassID(), BorderCG.class);
    }

    public SFont getFont(String key) {
        return (SFont)getDefaults().get(key, SFont.class);
    }

    public Color getColor(String key) {
        return (Color)getDefaults().get(key, Color.class);
    }

    public Style getStyle(String key) {
        return (Style)getDefaults().get(key, Style.class);
    }

    public StyleSheet getStyleSheet(String key) {
        return (StyleSheet)getDefaults().get(key, StyleSheet.class);
    }

    public Icon getIcon(String key) {
        return (Icon)getDefaults().get(key, Icon.class);
    }

    private CGDefaults defaults = null;

    public void setLookAndFeelDefaults(CGDefaults defaults) {
        this.defaults = defaults;
    }
    public CGDefaults getLookAndFeelDefaults() { return defaults; }

    public CGDefaults getDefaults() {
        return defaults;
    }


    /**
     * Returns the current default look and feel, or null.
     *
     * @return the current default look and feel, or null.
     * @see #setLookAndFeel
     */
    public LookAndFeel getLookAndFeel() {
        return lookAndFeel;
    }


    /**
     * Set the current default look and feel by name.
     *
     * @param name the name of a LookAndFeel
     * @see #getLookAndFeel
     */
    public void setLookAndFeel(String name) {
        LookAndFeel lookAndFeel = LookAndFeelFactory.getLookAndFeel(name);
        setLookAndFeel(lookAndFeel);
    }

    /**
     * Set the current default look and feel using a LookAndFeel object.
     *
     * @param newLookAndFeel the LookAndFeel object
     * @see #getLookAndFeel
     */
    public void setLookAndFeel(LookAndFeel newLookAndFeel) {
        lookAndFeel = newLookAndFeel;

        if (newLookAndFeel != null) {
            setLookAndFeelDefaults(new CGDefaults(newLookAndFeel.getDefaults()));
        }
        else {
            setLookAndFeelDefaults(null);
        }

        // have the session fire a propertyChangeEvent regarding the new lookAndFeel
        if (SessionManager.getSession() != null)
            ((PropertyService)SessionManager.getSession())
                .setProperty("lookAndFeel", newLookAndFeel.getName());
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
