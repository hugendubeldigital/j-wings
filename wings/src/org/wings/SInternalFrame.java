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

import javax.swing.*;
import javax.swing.event.*;

import org.wings.*;
import org.wings.event.*;
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
    implements SGetListener
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "InternalFrameCG";

    private boolean iconifyable = true;
    private boolean maximizable = true;
    private boolean closable = true;

    private boolean iconified = false;
    private boolean maximized = false;
    private boolean closed = false;
    
    private java.awt.Dimension normalsize = new java.awt.Dimension();

    /**
     * TODO: documentation
     */
    protected Icon icon = null;

    /**
     * TODO: documentation
     */
    protected String title = null;

    /**
     * TODO: documentation
     */
    protected EventListenerList listenerList = new EventListenerList();

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

    public void setIconifyable(boolean v) {
        boolean old = iconifyable;
        iconifyable = v;
        if (old != iconifyable)
            reload();
    }
    public boolean isIconifyable() { return iconifyable; }

    public void setMaximizable(boolean v) {
        boolean old = maximizable;
        maximizable = v;
        if (old != maximizable)
            reload();
    }
    public boolean isMaximizable() { return maximizable; }

    public void setClosable(boolean v) {
        boolean old = closable;
        closable = v;
        if (old != closable)
            reload();
    }
    public boolean isClosable() { return closable; }

    public void setIconified(boolean v) {
        if (iconified != v) {
            // was iconified, restore normal size
            if (iconified) {
                iconified = false;
                setPreferredSize(
                    new SDimension(normalsize.width, normalsize.height));
            }
            // had normal size, save the size
            else {
                normalsize = new java.awt.Dimension( 
                        getPreferredSize().getIntWidth(),
                        getPreferredSize().getIntHeight());
                setPreferredSize(
                    new SDimension(getPreferredSize().getIntWidth(), 20));
            }
            reload();
        }
        iconified = v;
    }
    public boolean isIconified() { return iconified; }

    public void setMaximized(boolean v) {
        boolean old = maximized;
        maximized = v;
        if (old != maximized)
            reload();

        if (maximized)
            setIconified(false);
    }
    public boolean isMaximized() { return maximized; }

    public void setClosed(boolean v) {
        boolean old = closed;
        closed = v;
        if (old != closed)
            reload();
    }
    public boolean isClosed() { return closed; }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setIcon(Icon i) {
        Icon oldIcon = icon;
        icon = i;
        if ((icon == null && oldIcon != null) ||
            (icon != null && !icon.equals(oldIcon)))
            reload();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setTitle(String t) {
        String oldTitle = title;
        title = t;
        if ((title == null && oldTitle != null) ||
            (title != null && !title.equals(oldTitle)))
            reload();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getTitle() {
        return title;
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
     * @return the number of Dialogs that belong to this SInternalFrame
     */
    public final int getDialogCount() {
        return getComponentCount() - 1;
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
        if (isIconified())
            setIconified(false);
    }

    /**
     * TODO: documentation
     *
     */
    public void hide() {
        super.setVisible(false);
    }

    /**
     * TODO: documentation
     *
     * @param listener
     */
    public void addInternalFrameListener(SInternalFrameListener listener) {
        listenerList.add(SInternalFrameListener.class, listener);
    }

    /**
     * TODO: documentation
     *
     * @param listener
     */
    public void removeInternalFrameListener(SInternalFrameListener listener) {
        listenerList.remove(SInternalFrameListener.class, listener);
    }

    private SInternalFrameEvent event;

    public void getPerformed(String name, String value) {
    System.out.println("SInternalFrame.getPerformed("+name+","+value+")");
    
        // cookie values
        if (value.charAt(0) == 'i') {
            String nvalue = value.substring(2);
            switch(value.charAt(1)) {
                case 'l':
                    int x = 0,y = 0;
                    x = new Integer(nvalue.substring(0,nvalue.indexOf("x"))).intValue();
                    y = new Integer(nvalue.substring(nvalue.indexOf("x")+1)).intValue();
                    setLocation(x,y);
                    break;
                case 's':
                    int w = 0,h = 0;
                    w = new Integer(nvalue.substring(0,nvalue.indexOf("x"))).intValue();
                    h = new Integer(nvalue.substring(nvalue.indexOf("x")+1)).intValue();
                    setPreferredSize(new SDimension(w,h));
                    break;
            }
            return;
        }
    
        switch (new Integer(value).intValue()) {
        case SInternalFrameEvent.INTERNAL_FRAME_CLOSED:
            setClosed(true);
            break;

        case SInternalFrameEvent.INTERNAL_FRAME_ICONIFIED:
            setIconified(true);
            break;

        case SInternalFrameEvent.INTERNAL_FRAME_DEICONIFIED:
            setIconified(false);
            break;

        case SInternalFrameEvent.INTERNAL_FRAME_MAXIMIZED:
            setMaximized(true);
            break;

        case SInternalFrameEvent.INTERNAL_FRAME_UNMAXIMIZED:
            setMaximized(false);
            break;

        default:
            throw new RuntimeException("unknown id: " + value);
        }

        event = new SInternalFrameEvent(this, new Integer(value).intValue());
    }

    public void fireIntermediateEvents() {}

    public void fireFinalEvents() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == InternalFrameListener.class) {
                switch (event.getID()) {
                case SInternalFrameEvent.INTERNAL_FRAME_CLOSED:
                    ((SInternalFrameListener)listeners[i+1]).internalFrameClosed(event);
                    break;
                    
                case SInternalFrameEvent.INTERNAL_FRAME_ICONIFIED:
                    ((SInternalFrameListener)listeners[i+1]).internalFrameIconified(event);
                    break;
                    
                case SInternalFrameEvent.INTERNAL_FRAME_DEICONIFIED:
                    ((SInternalFrameListener)listeners[i+1]).internalFrameDeiconified(event);
                    break;
                    
                case SInternalFrameEvent.INTERNAL_FRAME_MAXIMIZED:
                    ((SInternalFrameListener)listeners[i+1]).internalFrameMaximized(event);
                    break;
                    
                case SInternalFrameEvent.INTERNAL_FRAME_UNMAXIMIZED:
                    ((SInternalFrameListener)listeners[i+1]).internalFrameUnmaximized(event);
                    break;
                }
            }
        }

        event = null;
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

    public void setPreferredSize(SDimension dim) {
        /* if iconified, do not overwrite that size,
           just save it for restoring the original size
        */
        if (!iconified)
            super.setPreferredSize(dim);
        else {
            normalsize.width = dim.getIntWidth();
            normalsize.height = dim.getIntHeight();
        }
        
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

    public void setCG(InternalFrameCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
