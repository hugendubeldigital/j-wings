/*
 * $Id$
 * (c) Copyright 2001 wingS development team.
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

import java.io.IOException;
import org.wings.io.Device;

/**
 * A root container.
 * The classes derived from this class ({@link SFrame} and 
 * {@link SInternalFrame}) render in the content pane of this RootContainer.
 *
 * <p>The RootContainer has a stack of components. Ususally, the stack
 * contains only <em>one</em> element, the content pane; this is the bottommost
 * component. When dialogs are added to the RootContainer, then these dialogs
 * are stacked on top of this content pane, and only <em>this</em> dialog is
 * visible then. This emulates the behaviour of modal dialogs in a windowing
 * system.
 *
 * @author <a href="mailto:Engels@mercatis.de">Holger Engels</a>
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @author <a href="mailto:Haaf@mercatis.de">Armin Haaf</a>
 */
public abstract class SRootContainer extends SContainer {
    /**
     * The container for the contentPane.
     */
    protected final SContainer contentPane;

    /**
     * default constructor initializes the stack layout system of this
     * SRootContainer.
     */
    public SRootContainer() {
        contentPane = new SContainer();
	super.setLayout(new SStackLayout());
        super.addComponent(getContentPane(), null);
    }

    /**
     * Push a new dialog on top of the stack. If this RootContainer is
     * rendered, then only this dialog is shown.
     * @param dialog the SDialog that is to be shown on top.
     */
    public void pushDialog(SDialog dialog) {
        super.addComponent(dialog, null);
        int count = getComponentCount();
        System.err.println("pushDialog: " + count);
        dialog.setFrame(this);
        reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * remove the dialog, that is on top of the stack.
     *
     * @return the dialog, that is popped from the stack.
     */
    public SDialog popDialog() {
        int count = getComponentCount();
        if (count <= 1)
            throw new IllegalStateException("there's no dialog left!");

        SDialog dialog = (SDialog)getComponent(count - 1);
        super.removeComponent(dialog);
        dialog.setFrame((SFrame)null);
        System.err.println("popDialog: " + count);

        reload(ReloadManager.RELOAD_CODE);
        return dialog;
    }

    /**
     * @return the number of dialogs that are on the stack currently.
     */
    public int getDialogCount() {
        return getComponentCount() - 1;
    }

    /**
     * returns the content pane of this RootContainer.
     */
    public SContainer getContentPane() {
        return contentPane;
    }

    /**
     * Use getContentPane().addComponent(c) instead.
     */
    public SComponent addComponent(SComponent c, Object constraint) {
        throw new IllegalArgumentException("use getContentPane().addComponent()");
    }

    /**
     * Use getContentPane().removeComponent(c) instead.
     */
    public boolean removeComponent(SComponent c) {
        throw new IllegalArgumentException("use getContentPane().removeComponent()");
    }

    /**
     * a layout manager, that shows only the topmost element.
     */
    private class SStackLayout extends SAbstractLayoutManager {
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
         * Always write code for the topmost component.
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
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
