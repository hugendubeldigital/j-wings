/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://j-wings.org).
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
import javax.swing.text.BadLocationException;

import org.wings.text.*;
import org.wings.event.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class STextComponent
    extends SComponent
    implements LowLevelEventListener, SDocumentListener
{
    /** @see LowLevelEventListener#isEpochChecking() */
    protected boolean epochChecking = true;   
    
    private boolean editable = true;

    private SDocument document;

    /**
     * TODO: documentation
     *
     */
    public STextComponent() {
        this(new DefaultDocument(), true);
    }

    public STextComponent(String text) {
        this(new DefaultDocument(text), true);
    }
    /**
     * TODO: documentation
     *
     * @param document
     */
    public STextComponent(SDocument document) {
        this(document, true);
    }

    /**
     * TODO: documentation
     *
     * @param document
     * @param editable
     */
    public STextComponent(SDocument document, boolean editable) {
        setDocument(document);
        setEditable(editable);
    }

    public SDocument getDocument() {
        return document;
    }

    public void setDocument(SDocument document) {
        if (document == null)
            throw new IllegalArgumentException("null");

        SDocument oldDocument = this.document;
        this.document = document;
        if (oldDocument != null)
            oldDocument.removeDocumentListener(this);
        document.addDocumentListener(this);
        reloadIfChange(ReloadManager.RELOAD_CODE, oldDocument, document);
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
     * @param text
     */
    public void setText(String text) {
        document.setText(text);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getText() {
        return document.getText();
    }

    /**
     * Appends the given text to the end of the document. Does nothing 
     * if the string is null or empty.
     *
     * @param text the text to append.
     */
    public void append(String text) {
        try {
            document.insert(document.getLength(), text);
        }
        catch (BadLocationException e) {}
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
     * @deprecated use DocumentListener instead
     */
    public void addTextListener(TextListener listener) {
        addEventListener(TextListener.class, listener);
    }

    /**
     * @deprecated use DocumentListener instead
     */
    public void removeTextListener(TextListener listener) {
        removeEventListener(TextListener.class, listener);
    }

    public void addDocumentListener(SDocumentListener listener) {
        getDocument().addDocumentListener(listener);
    }

    public void removeDocumentListener(SDocumentListener listener) {
        getDocument().removeDocumentListener(listener);
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

    /** @see LowLevelEventListener#isEpochChecking() */
    public boolean isEpochChecking() {
        return epochChecking;
    }
  
    /** @see LowLevelEventListener#isEpochChecking() */
    public void setEpochChecking(boolean epochChecking) {
        this.epochChecking = epochChecking;
    }

    //-- implement SDocumentListener to notify TextListeners
    public void insertUpdate(SDocumentEvent e) {
        fireTextValueChanged();
        reload(ReloadManager.RELOAD_CODE);
    }

    public void removeUpdate(SDocumentEvent e) {
        fireTextValueChanged();
        reload(ReloadManager.RELOAD_CODE);
    }

    public void changedUpdate(SDocumentEvent e) {
        fireTextValueChanged();
        reload(ReloadManager.RELOAD_CODE);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
