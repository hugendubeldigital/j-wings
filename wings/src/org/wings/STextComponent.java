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

import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import javax.swing.event.EventListenerList;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class STextComponent
    extends SComponent
    implements LowLevelEventListener
{
    private boolean editable = true;

    /**
     * TODO: documentation
     */
    protected String text = null;

    /**
     * TODO: documentation
     *
     */
    public STextComponent() {
        this(null, true);
    }

    /**
     * TODO: documentation
     *
     * @param text
     */
    public STextComponent(String text) {
        this(text, true);
    }

    /**
     * TODO: documentation
     *
     * @param text
     * @param editable
     */
    public STextComponent(String text, boolean editable) {
        setText(text);
        setEditable(editable);
    }


    /**
     * TODO: documentation
     *
     * @param ed
     */
    public void setEditable(boolean ed) {
        boolean oldEditable = editable;
        editable = ed;
        if (editable != oldEditable)
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isEditable() {
        return editable;
    }


    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setText(String t) {
        String oldText = text;
        text = t;
        if ((text == null && oldText != null) ||
            (text != null && !text.equals(oldText)))
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * Appends the given text to the end of the document. Does nothing 
     * if the string is null or empty.
     *
     * @param str the text to append.
     */
    public void append(String str) {
        if (str == null) return;
        StringBuffer buf = new StringBuffer();
        if (text != null) buf.append(text);
        buf.append(str);
        text = buf.toString();
        reload(ReloadManager.RELOAD_CODE);
    }

    public void processLowLevelEvent(String action, String[] values) {
        if ( isEditable() && isEnabled() ) {
            // System.out.println("getPerformed " + action + " : " + value);
            if ( values[0] != null )
                values[0] = values[0].trim();
            if ( getText() == null || !getText().equals( values[0] ) ) {
                setText( values[0] );
                SForm.addArmedComponent(this);
            }
        }
    }

    /**
     * TODO: documentation
     *
     * @param al
     */
    public void addTextListener(TextListener listener) {
        addEventListener(TextListener.class, listener);
    }

    /**
     * TODO: documentation
     *
     * @param al
     */
    public void removeTextListener(TextListener listener) {
        removeEventListener(TextListener.class, listener);
    }

    /**
     * Fire a TextEvent at each registered listener.
     */
    protected void fireTextValueChanged() {
        TextEvent event = null;
        // Guaranteed to return a non-null array
        Object[] listeners = getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == TextListener.class) {
                if ( event==null ) {
                    event = new TextEvent(this, TextEvent.TEXT_VALUE_CHANGED);
                } // end of if ()
                
                ((TextListener)listeners[i+1]).textValueChanged(event);
            }
        }
    }

    public void fireIntermediateEvents() {
        fireTextValueChanged();
    }

    public void fireFinalEvents() {}
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
