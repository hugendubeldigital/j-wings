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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.EventListenerList;

import org.wings.plaf.*;
import org.wings.io.Device;

/*
 * Die Basisklasse aller HTML Komponenten, die Button Funktionalitaet
 * haben. Diese Klasse stellt ein ActionListener Interface zur
 * Verfuegung.
 *
 * Um eine Klassen Hierarchie der verschiedenen Buttno Typen zu erreichen, ist
 * der Aufbau des HTML Renderings etwas komplexer als normal. Zum einen gibt es
 * zu beachten, dass Buttons sowohl in einer Form, als auch als normaler Anchor
 * vorkommen koennen. Deshalb ist der Ablauf HTML Code in den Device zu
 * schreiben etwas anders. Jedoch sind die appendBorder Methoden davon
 * unberuehrt.
 * Ist ein Button deaktiviert, wird er im Grunde als Anchor-Button
 * interpretiert, auch wenn er innerhalb einer Form ist. Ein Button kann auch
 * explizit als Anchor-Button spezifiziert sein, egal ob er innerhalb einer Form
 * ist oder nicht.
 * Ansonsten werden die 3 HTML Code erzeugenden Methoden appendPrefix,
 * appendBody und appendPostfix jeweils aufgesplittet in die Methoden
 * appendAnchorPrefix, appendAnchorBody, appendAnchorPostfix
 * bzw. appendFormPrefix, appendFormBody, appendFormPostfix.
 * Die Methoden, die den Anchor Teil rendern muessen den deaktivierten Button
 * rendern koennen. In den Form Teil kommen nur aktivierte Buttons. Im Form Teil
 * gibt es noch einen Spezialfall, die Methode appendFormBodyAttributes. Diese
 * Methode wird benoetigt um bei Form Buttons zusatzliche Attribute, wie etwa
 * checked, einzufuegen.
 */
/**
 * TODO: documentation
 *
 * @author
 * @version $Revision$
 */
public class SAbstractButton
    extends SComponent
    implements Selectable, RequestListener //SGetListener
{
    /** the text the button is showing */
    protected String text;

    /**
     * button type
     * @see #setType
     */
    protected String type;

    /**
     * TODO: documentation
     */
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * TODO: documentation
    protected ArrayList actionListener = new ArrayList(2);
    */

    /**
     * TODO: documentation
     */
    protected String actionCommand = null;

    /**
     * If the button change the content of another Frame or should show a new
     * Frame, set the target.
     */
    protected String realTarget = null;

    /**
     * If this is set to false, the button is always rendered as anchor
     * button. Else the button is rendered as anchor button if it is disabled or if
     * it is not inside a Form.
     */
    private boolean showAsFormComponent = true;

    /**
     * Because the foreground color is reseted after a &lt;A&gt; Tag the
     * foreground Color must be set inside the  &lt;A&gt; Tag. So it must be backed
     * up and rendered by hand instead of the super class.
     */
    protected Color foregroundBackup = null;

    /**
     * If the text of the button should not be wrapped, set this to true. This
     * inserts a &lt;NOBREAK&gt; Tag around the label
     */
    protected boolean noBreak = false;

    /*
     * Erzeugt einen Submit Button mit dem angebenen Text.
     */
    /**
     * TODO: documentation
     *
     * @param text
     */
    public SAbstractButton(String text) {
        this(text, SConstants.SUBMIT_BUTTON);
    }

    /*
     * Erzeugt eine Button mit dem angebenen Text vom angebenen Typ.
     * @see #setType
     */
    public SAbstractButton(String text, String type) {
        setText(text);
        setType(type);
    }

    /*
     * Erzeugt einen Submit Button mit dem Text "SUBMIT"
     */
    /**
     * TODO: documentation
     *
     */
    public SAbstractButton() {
        this("SUBMIT");
    }

    /**
     * If the text of the button should not be wrapped, set this to true. This
     * inserts a &lt;NOBREAK&gt; Tag around the label
     *
     * @param b
     */
    public void setNoBreak(boolean b) {
        noBreak = b;
    }
    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isNoBreak() {
        return noBreak;
    }

    /**
     * TODO: documentation
     *
     * @param showAsFormComponent
     */
    public void setShowAsFormComponent(boolean showAsFormComponent) {
        this.showAsFormComponent = showAsFormComponent;
    }

    public boolean getShowAsFormComponent() {
        return showAsFormComponent && getResidesInForm();
    }

    /*
     * Dies macht nur Sinn, wenn Button ein AnchorButton ist und nicht
     * in einer Form !!!
     */
    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setRealTarget(String t) {
        realTarget = t;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getRealTarget() {
        return realTarget;
    }

    /**
     * TODO: documentation
     *
     * @param ac
     */
    public void setActionCommand(String ac) {
        actionCommand = ac;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getActionCommand() {
        return actionCommand;
    }

    /**
     * TODO: documentation
     *
     * @param listener
     */
    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    /**
     * TODO: documentation
     *
     * @param listener
     */
    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    /**
     * Fire a ActionEvent at each registered listener.
     */
    protected void fireActionPerformed() {
        if (actionCommand == null)
            actionCommand = getText();
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand);
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == ActionListener.class) {
                ((ActionListener)listeners[i+1]).actionPerformed(e);
            }
        }
    }

    /**
     * Sets the button type. Use one of the following types:
     * <UL>
     * <LI> {@link SConstants#SUBMIT_BUTTON}
     * <LI> {@link SConstants#RESET_BUTTON}
     * <LI> {@link SConstants#CHECKBOX}
     * <LI> {@link SConstants#RADIOBUTTON}
     * </UL>
     *
     * @param t
     */
    public void setType(String t) {
        type = t;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the label of the button.
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

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isSelected() {
        return false;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setSelected(boolean s) {
    }

    public void getPerformed(String action, String value) {
        SForm.addArmedComponent(this);
    }

    public void fireIntermediateEvents() {
        // TODO: fire item events
    }

    public void fireFinalEvents() {
        fireActionPerformed();
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
