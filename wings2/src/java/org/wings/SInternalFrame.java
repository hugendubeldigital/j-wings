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

import org.wings.event.SInternalFrameEvent;
import org.wings.event.SInternalFrameListener;
import org.wings.plaf.InternalFrameCG;

/**
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SInternalFrame
    extends SRootContainer
    implements LowLevelEventListener
{
    private boolean iconifyable = true;
    private boolean maximizable = true;
    private boolean closable = true;

    private boolean iconified = false;
    private boolean maximized = false;
    private boolean closed = false;

    protected SIcon icon = null;

    protected String title = null;

    public SInternalFrame() {
        super();
    }

    public void setIconifyable(boolean v) {
        reloadIfChange(ReloadManager.RELOAD_CODE, iconifyable, v);
    }
    public boolean isIconifyable() { return iconifyable; }

    public void setMaximizable(boolean v) {
        maximizable = v;
        reloadIfChange(ReloadManager.RELOAD_CODE, maximizable, v);
    }
    public boolean isMaximizable() { return maximizable; }

    public void setClosable(boolean v) {
        closable = v;
        reloadIfChange(ReloadManager.RELOAD_CODE, closable, v);
    }
    public boolean isClosable() { return closable; }

    public void setIconified(boolean v) {
        v &= isIconifyable();
        boolean old = iconified;
        iconified = v;
        if (old != iconified) {
            reload(ReloadManager.RELOAD_CODE);
            if (iconified)
                setMaximized(false);
        }
    }
    public boolean isIconified() { return iconified; }

    public void setMaximized(boolean v) {
        v &= isMaximizable();
        boolean old = maximized;
        maximized = v;
        if (old != maximized) {
            reload(ReloadManager.RELOAD_CODE);
            if (maximized)
                setIconified(false);
        }
    }
    public boolean isMaximized() { return maximized; }

    public void setClosed(boolean v) {
        v &= isClosable();
        boolean old = closed;
        closed = v;
        if (old != closed)
            reload(ReloadManager.RELOAD_CODE);
    }
    public boolean isClosed() { return closed; }

    public void setIcon(SIcon i) {
        if ( i!=icon || i!=null && !i.equals(icon) ) {
            icon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    public SIcon getIcon() {
        return icon;
    }

    public void setTitle(String t) {
        String oldTitle = title;
        title = t;
        if ((title == null && oldTitle != null) ||
            (title != null && !title.equals(oldTitle)))
            reload(ReloadManager.RELOAD_CODE);
    }

    public String getTitle() {
        return title;
    }

    public void dispose() {
        SDesktopPane desktop = (SDesktopPane)getParent();
        desktop.remove(this);
    }

    public void setVisible(boolean visible) {
        if (visible)
            show();
        else
            hide();
    }

    public void show() {
        super.setVisible(true);
        if (isIconified())
            setIconified(false);
    }

    public void hide() {
        super.setVisible(false);
    }

    public void addInternalFrameListener(SInternalFrameListener listener) {
        addEventListener(SInternalFrameListener.class, listener);
    }

    public void removeInternalFrameListener(SInternalFrameListener listener) {
        removeEventListener(SInternalFrameListener.class, listener);
    }

    private SInternalFrameEvent event;

    // LowLevelEventListener interface. Handle own events.
    public void processLowLevelEvent(String name, String[] values) {
        switch (new Integer(values[0]).intValue()) {
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
            throw new RuntimeException("unknown id: " + values[0]);
        }

        event = new SInternalFrameEvent(this, new Integer(values[0]).intValue());
        SForm.addArmedComponent(this); // trigger later invocation of fire*()
    }

    public void fireIntermediateEvents() {}

    public void fireFinalEvents() {
        // Guaranteed to return a non-null array
        Object[] listeners = getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == SInternalFrameListener.class) {
                SInternalFrameListener listener;
                listener = (SInternalFrameListener) listeners[i+1];
                switch (event.getID()) {
                case SInternalFrameEvent.INTERNAL_FRAME_CLOSED:
                    listener.internalFrameClosed(event);
                    break;
                    
                case SInternalFrameEvent.INTERNAL_FRAME_ICONIFIED:
                    listener.internalFrameIconified(event);
                    break;
                    
                case SInternalFrameEvent.INTERNAL_FRAME_DEICONIFIED:
                    listener.internalFrameDeiconified(event);
                    break;
                    
                case SInternalFrameEvent.INTERNAL_FRAME_MAXIMIZED:
                    listener.internalFrameMaximized(event);
                    break;
                    
                case SInternalFrameEvent.INTERNAL_FRAME_UNMAXIMIZED:
                    listener.internalFrameUnmaximized(event);
                    break;
                }
            }
        }

        event = null;
    }

    public boolean checkEpoch() {
        return true;
    }

    public void setCG(InternalFrameCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
