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

import java.io.*;

import org.wings.*;
import org.wings.io.*;
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

    /**
     * TODO: documentation
     *
     */
    public SInternalFrame() {
        setLayout(new SStackLayout());
        super.addComponent(getContentPane(), null);
    }


    /**
     * Use getContentPane().addComponent(c) instead.
     */
    public final SComponent addComponent(SComponent c, Object constraint) {
        throw new IllegalArgumentException("use getContentPane().addComponent()");
    }

    /**
     * Use getContentPane().removeComponent(c) instead.
     */
    public final boolean removeComponent(SComponent c) {
        throw new IllegalArgumentException("use getContentPane().removeComponent()");
    }

    /**
     * TODO: documentation
     */
    public final void pushDialog(SDialog dialog) {
        super.addComponent(dialog, null);
        dialog.setFrame(this);
    }

    /**
     * TODO: documentation
     */
    public final SDialog popDialog() {
        int count = getComponentCount();
        if (count <= 1)
            throw new IllegalStateException("there's no dialog left!");

        SDialog dialog = (SDialog)getComponent(count - 1);
        super.removeComponent(dialog);
        dialog.setFrame((SFrame)null);
        return dialog;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SContainer getContentPane() {
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

    private class SStackLayout extends SAbstractLayoutManager
    {
        private SContainer container = null;

        public SStackLayout() {}

        public void updateCG() {}
        public void addComponent(SComponent c, Object constraint) {}
        public void removeComponent(SComponent c) {}

        public SComponent getComponentAt(int i) {
            return (SComponent)getComponent(i);
        }

        public void setContainer(SContainer c) {
            container = c;
        }

        /**
         * Allways write code for the topmost component.
         *
         * @param s
         * @throws IOException
         */
        public void write(Device s)
            throws IOException
        {
            int topmost = getComponentCount() - 1;
            SComponent comp = (SComponent)getComponent(topmost);
            comp.write(s);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
