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
package org.wings;

import java.awt.event.KeyEvent;

import org.wings.plaf.MenuBarCG;
import org.wings.util.KeystrokeUtil;

import javax.swing.*;

/**
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SMenuItem extends SButton {
    protected SComponent menuParent;
    private KeyStroke accelerator;

    public SMenuItem() {
    }

    public SMenuItem(Action action) {
        super(action);
    }

    public SMenuItem(String text) {
        super(text);
    }

    public SMenuItem(SIcon icon) {
        super(icon);
    }

    public SMenuItem(String text, SIcon icon) {
        super(text, icon);
    }

    final void setParentMenu(SComponent menuParent) {
        this.menuParent = menuParent;
        setParentFrame(menuParent != null ? menuParent.getParentFrame() : null);
    }

    public SComponent getParentMenu() {
        return this.menuParent;
    }

    public void setCG(MenuBarCG cg) {
        super.setCG(cg);
    }
    
    public void setAccelerator(KeyStroke keyStroke) {
        accelerator = keyStroke;
        if (accelerator != null) {
            getInputMap(WHEN_IN_FOCUSED_FRAME).put(accelerator, "item_accelerator");
            getActionMap().put("item_accelerator", getAction());
        }
    }
    
    public KeyStroke getAccelerator() {
        return accelerator;
    }
}


