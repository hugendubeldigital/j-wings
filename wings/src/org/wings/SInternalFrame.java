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

import org.wings.*;
import org.wings.plaf.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SInternalFrame
    extends SContainer
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "InternalFrameCG";

    /**
     * TODO: documentation
     */
    protected SContainer contentPane = new SContainer();

    /*
     * Der Container fuer einen OptionPane, falls vorhanden.
     */
    /**
     * TODO: documentation
     */
    protected final SContainer optionPaneContainer =
        new SContainer();

    /*
     * der aktuell aktive OptionPane
     */
    /**
     * TODO: documentation
     */
    protected SDialog optionPane = null;

    /*
     * Das Layout des Frames.
     */
    /**
     * TODO: documentation
     */
    protected final SCardLayout card = new SCardLayout();

    /*
     * Key fuer das SCardLayout. um den OptionPane sichtbar zu machen
     */
    /**
     * TODO: documentation
     */
    protected final String OPTION_PANEL = "option";

    /*
     * Key fuer das SCardLayout. um den ContentPane sichtbar zu machen
     */
    /**
     * TODO: documentation
     */
    protected final String CONTENT_PANEL = "content";

    /**
     * TODO: documentation
     *
     */
    public SInternalFrame() {
        super();
        setLayout(card);

        super.addComponent(getContentPane(), CONTENT_PANEL);
        super.addComponent(optionPaneContainer, OPTION_PANEL);
        card.show(this, CONTENT_PANEL);
    }

    /**
     * Macht den OptionPane sichtbar.
     */
    public final void showOptionPane() {
        if ( optionPane!=null ) {
            card.show(this, OPTION_PANEL);
            optionPane.setFrame(this);
        }
    }

    /*
     * Macht den ContentPane sichtbar.
     */
    /**
     * TODO: documentation
     */
    public final void showContentPane() {
        card.show(this, CONTENT_PANEL);
        if ( optionPane!=null )
            optionPane.setFrame((SFrame)null);
    }

    /*
     * Setzt den OptionPane neu.
     */
    /**
     * TODO: documentation
     */
    public final void setOptionPane(SDialog c) {
        optionPane = c;
        if ( optionPane!=null ) {
            optionPaneContainer.removeAll();
            optionPaneContainer.add(optionPane);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SContainer getContentPane() {
        if ( contentPane==null )
            contentPane = new SContainer();
        return contentPane;
    }

    /**
     * TODO: documentation
     *
     */
    public void dispose() {
        SDesktopPane desktop = (SDesktopPane)getParent();
        desktop.remove(this);
    }

    /**
     * TODO: documentation
     *
     * @param visible
     */
    public void setVisible(boolean visible) {
        if (visible)
            show();
        else
            hide();
    }

    /**
     * TODO: documentation
     *
     */
    public void show() {
        super.setVisible(true);
        SDesktopPane desktop = (SDesktopPane)getParent();
        if (desktop == null)
            throw new IllegalStateException("no parent; SInternalFrame requires a SDesktop as parent.");
        desktop.moveToFront(this);
    }

    /**
     * TODO: documentation
     *
     */
    public void hide() {
        super.setVisible(false);
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "InternalFrameCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
