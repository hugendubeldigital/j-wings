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

/**
 * This is the base class for all components which have a button
 * functionality. This class handles ActionListener notification.
 *
 * @author
 * @version $Revision$
 */
public abstract class SAbstractButton
    extends SComponent
    implements Selectable, RequestListener
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

    /**
     * Create a button with given text.
     * @param text the button text
     */
    public SAbstractButton(String text) {
        this(text, SConstants.SUBMIT_BUTTON);
    }

    /**
     * Creates a new Button with the given Text and the given Type.
     *
     * @param text the button text
     * @param type the button type
     * @see #setType
     */
    public SAbstractButton(String text, String type) {
        setText(text);
        setType(type);
    }

    /**
     * Creates a new submit button with thh text "SUBMIT"
     */
    public SAbstractButton() {
        this("SUBMIT");
    }

    /**
     * If the text of the button should not be wrapped, set this to true. This
     * inserts a &lt;NOBREAK&gt; Tag around the label
     * @see #isNoBreak()
     * @param b
     */
    public void setNoBreak(boolean b) {
        noBreak = b;
    }

    /**
     * Test, if "noBreak" is set for this button.
     * @see #setNoBreak(boolean)
     * @return true, if nobreak is set, false otherwise.
     */
    public boolean isNoBreak() {
        return noBreak;
    }

    /**
     * Set display mode (href or form-component).
     * An AbstractButton can appear as HTML-Form-Button or as 
     * HTML-HREF. If button is inside a {@link org.wings.SFrom} the default
     * is displaying it as html form button.
     * Setting <i>showAsFormComponent</i> to <i>false</i> will
     * force displaying as href even if button is inside 
     * a form.
     * @param showAsFormComponent if true, display as link, if false as html form component.
     */
    public void setShowAsFormComponent(boolean showAsFormComponent) {
        this.showAsFormComponent = showAsFormComponent;
    }

	/**
      * Test, what display method is set.
      * @see #setShowAsFormComponent(boolean)
      * @return treu, if displayed as link, false when displayed as html form component.
      */
    public boolean getShowAsFormComponent() {
        return showAsFormComponent && getResidesInForm();
    }

    /**
     * This is only usefull if the button is an anchor button (and not a
     * form button).
     *
     * @param t the real target
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
     * Fire an ActionEvent at each registered listener.
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
        String oldText = text;
        text = t;
        if ((text == null && oldText != null) ||
            text != null && !text.equals(oldText))
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

    public void processRequest(String action, String[] values) {
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
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
