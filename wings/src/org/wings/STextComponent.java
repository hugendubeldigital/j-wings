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

import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import java.util.ArrayList;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author Dominik Bartenstein
 * @version $Revision$
 */
public abstract class STextComponent
    extends SComponent
    implements SGetListener
{
    private boolean editable = true;

    /**
     * TODO: documentation
     */
    protected String text = null;

    /**
     * TODO: documentation
     */
    protected ArrayList textListener = new ArrayList(2);


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
        editable = ed;
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
        text = t;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getText() {
        return text;
    }

    public void getPerformed(String action, String value) {
        if ( isEditable() && isEnabled() ) {
            // System.out.println("getPerformed " + action + " : " + value);
            if ( value != null )
                value = value.trim();
            if ( getText() == null || !getText().equals( value ) ) {
                setText( value );
                fireTextEvent();
            }
        }
    }

    /**
     * TODO: documentation
     *
     * @param al
     */
    public void addTextListener(TextListener al) {
        textListener.add(al);
    }

    /**
     * TODO: documentation
     *
     * @param al
     */
    public void removeTextListener(TextListener al) {
        textListener.remove(al);
    }

    /**
     * TODO: documentation
     *
     */
    protected void fireTextEvent() {
        TextEvent e = new TextEvent(this, TextEvent.TEXT_VALUE_CHANGED);

        for ( int i=0; i<textListener.size(); i++ )
            ((TextListener)textListener.get(i)).textValueChanged(e);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
