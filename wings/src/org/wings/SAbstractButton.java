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
import java.beans.*;
import javax.swing.event.EventListenerList;
import javax.swing.Action;

import org.wings.plaf.*;
import org.wings.io.Device;
import org.wings.style.Style;

/**
 * This is the base class for all components which have a button
 * functionality. This class handles ActionListener notification.
 *
 * @author
 * @version $Revision$
 */
public abstract class SAbstractButton
    extends SComponent
    implements RequestListener
{
    public static final String SUBMIT_BUTTON  = "submit";
    public static final String RESET_BUTTON   = "reset";
    public static final String IMAGE_BUTTON   = "image";
    public static final String CHECKBOX       = "checkbox";
    public static final String RADIOBUTTON    = "radio";


    public static final int ICON_COUNT = 7;
    public static final int DISABLED_ICON = 0;
    public static final int DISABLED_SELECTED_ICON = 1;
    public static final int ENABLED_ICON = 2;
    public static final int SELECTED_ICON = 3;
    public static final int ROLLOVER_ICON = 4;
    public static final int ROLLOVER_SELECTED_ICON = 5;
    public static final int PRESSED_ICON = 6;

    /** the text the button is showing */
    private String text;

    /** selection state */
    private boolean selected = false;

    /**
     * The icon to be displayed
     */
    private SIcon icon;

    /**
     * TODO: documentation
     */
    private SIcon disabledIcon;

    /**
     * TODO: documentation
     */
    private SIcon selectedIcon;

    /**
     * TODO: documentation
     */
    private SIcon pressedIcon;

    /**
     * TODO: documentation
     */
    private SIcon disabledSelectedIcon;

    /**
     * TODO: documentation
     */
    private SIcon rolloverIcon;

    /**
     * TODO: documentation
     */
    private SIcon rolloverSelectedIcon;

    /**
     * TODO: documentation
     */
    private Style selectionStyle;


    /**
     * TODO: documentation
     */
    private int verticalTextPosition = CENTER;

    /**
     * TODO: documentation
     */
    private int horizontalTextPosition = RIGHT;

    /**
     * TODO: documentation
     */
    private int iconTextGap = 0;

    /**
     * TODO: documentation
     */
    private boolean alignText = false;

    /**
     * TODO: documentation
     */
    private boolean escapeSpecialChars = true;

    /**
     * button type
     * @see #setType
     */
    private String type = SUBMIT_BUTTON;

    /**
     *
     */
    private SButtonGroup buttonGroup;

    /**
     * TODO: documentation
     */
    protected final EventListenerList listenerList = new EventListenerList();

    /**
     * TODO: documentation
     */
    protected String actionCommand;

    /**
     * If this is set to false, the button is always rendered as anchor
     * button. Else the button is rendered as anchor button if it is disabled or if
     * it is not inside a Form.
     */
    private boolean showAsFormComponent = true;

    /**
     * If the text of the button should not be wrapped, set this to true. This
     * inserts a &lt;NOBREAK&gt; Tag around the label
     */
    protected boolean noBreak = false;

    /**
     * 
     */
    private String eventTarget;

    /**
     * 
     */
    private Action action;

    /**
     * 
     */
    private PropertyChangeListener actionPropertyChangeListener;
    

    /**
     * Create a button with given text.
     * @param text the button text
     */
    public SAbstractButton(String text) {
        setText(text);
    }

    /**
     * @param action
     */
    public SAbstractButton(Action action) {
        setAction(action);
    }

    /**
     * Creates a new Button with the given Text and the given Type.
     *
     * @param text the button text
     * @param type the button type
     * @see #setType
     */
    public SAbstractButton(String text, String type) {
        this(text);
        setType(type);
    }

    /**
     * Creates a new submit button
     */
    public SAbstractButton() {
        this("");
    }

    /**
     * TODO: documentation
     *
     * @param textPosition
     */
    public void setHorizontalTextPosition(int textPosition) {
        horizontalTextPosition = textPosition;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final int getHorizontalTextPosition() {
        return horizontalTextPosition;
    }

    /**
     * TODO: documentation
     *
     * @param textPosition
     */
    public void setVerticalTextPosition(int textPosition) {
        verticalTextPosition = textPosition;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final int getVerticalTextPosition() {
        return verticalTextPosition;
    }

    /**
     * TODO: documentation
     *
     * @param gap
     */
    public void setIconTextGap(int gap) {
        iconTextGap = gap;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final int getIconTextGap() {
        return iconTextGap;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setIcon(SIcon i) {
        if ( isDifferent(icon, i) ) {
            icon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final SIcon getIcon() {
        return icon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setPressedIcon(SIcon i) {
        if ( isDifferent(pressedIcon, i) ) {
            pressedIcon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final SIcon getPressedIcon() {
        return pressedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setRolloverIcon(SIcon i) {
        if ( isDifferent(rolloverIcon, i) ) {
            rolloverIcon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final SIcon getRolloverIcon() {
        return rolloverIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setRolloverSelectedIcon(SIcon i) {
        if ( isDifferent(rolloverSelectedIcon, i) ) {
            rolloverSelectedIcon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final SIcon getRolloverSelectedIcon() {
        return rolloverSelectedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setSelectedIcon(SIcon i) {
        if ( isDifferent(selectedIcon, i) ) {
            selectedIcon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final SIcon getSelectedIcon() {
        return selectedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledSelectedIcon(SIcon i) {
        if ( isDifferent(disabledSelectedIcon, i) ) {
            disabledSelectedIcon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final SIcon getDisabledSelectedIcon() {
        return disabledSelectedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledIcon(SIcon i) {
        if ( isDifferent(disabledIcon, i) ) {
            disabledIcon = i;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SIcon getDisabledIcon() {
        if(disabledIcon == null) {
            /**** TODO
                  if(icon != null && icon instanceof ImageIcon)
                  disabledIcon = new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon)icon).getImage()));
            ***/
        }
        return disabledIcon;
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
    public final boolean isNoBreak() {
        return noBreak;
    }

    /**
     * @param selectionStyle
     */
    public void setSelectionStyle(Style selectionStyle) {
        this.selectionStyle = selectionStyle;
    }

    /**
     * @return
     */
    public final Style getSelectionStyle() { 
        return selectionStyle; 
    }

    /**
     * Set display mode (href or form-component).
     * An AbstractButton can appear as HTML-Form-Button or as 
     * HTML-HREF. If button is inside a {@link org.wings.SForm} the default
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
    public final String getActionCommand() {
        return actionCommand;
    }


    /**
     * TODO: documentation
     *
     * @return
     */
    public final SButtonGroup getGroup() {
        return buttonGroup;
    }

    /**
     * Add this button to a button group. This influences the event-prefix
     * this button reports to the request dispatcher: it will change to
     * the button group's prefix.
     * @param g
     */
    protected void setGroup(SButtonGroup g) {
        if ( isDifferent(buttonGroup, g) ) {
            if (getDispatcher()!=null ) {
                getDispatcher().unregister(this);
                buttonGroup = g;
                getDispatcher().register(this);
            } else {
                buttonGroup = g;
            }
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     *
     */
    public String getNamePrefix() {
        if ( buttonGroup==null )
            return super.getNamePrefix();
        else
            return buttonGroup.getNamePrefix();
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
        fireActionEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                                        actionCommand==null ? getText() : actionCommand));
    }

    /**
     * Fire an ActionEvent at each registered listener.
     */
    protected final void fireActionEvent(ActionEvent e) {
        if ( e==null )
            return;

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
        if ( isDifferent(type, t) ) {
            type = t;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final String getType() {
        return type;
    }

    /**
     * Sets the label of the button.
     *
     * @param t
     */
    public void setText(String t) {
        if ( isDifferent(text, t) ) {
            text = t;
            reload(ReloadManager.RELOAD_CODE);
        }
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
     */
    public void doClick() {
        setSelected(!isSelected());

        fireActionPerformed();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final boolean isSelected() {
        return selected;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public void setSelected(boolean b) {
        if ( selected!=b ) {
            if ( buttonGroup!=null )
                buttonGroup.setSelected(this, b);

            selected = b;

            reload(ReloadManager.RELOAD_CODE | ReloadManager.RELOAD_STYLE);
        }
    }

    /**
     *  store here the state after process request. The request will then be set
     * in {@link fireIntermediateEvents}
     */
    private boolean requestSelection;

    public void processRequest(String action, String[] values) {
        /*
        System.out.print("action " + action);
        for ( int i=0; i<values.length; i++ ) {
            System.out.print(" " + values[i]);
        }
        System.out.println();
        */

        requestSelection = selected; 

        // if event Count == 2, then we are in a form and got a deselect event and
        // and a select event. That is the new state is select. Else select
        // state is in selection
        int eventCount = 0;


        if ( buttonGroup!=null ) {

            // button group prefix is shared, so maybe more than one value is
            // delivered in a form 
            for ( int i=0; i<values.length; i++ ) {

                // with button group the value has a special encoding...
                // this is because in a form the name of a parameter for buttons in
                // a buttongroup must be the same...
                String value = values[i];

                // illegal format
                if ( value.length()<3 ) { continue; }

                // no uid DIVIDER
                // value.charAt(value.length()-2)!=UID_DIVIDER ) { break; }

                // not for me
                if ( !value.startsWith(getUnifiedId()) ) { continue; }

                // last character is indicator, if button should be selected or not
                switch ( value.charAt(value.length()-1) ) {
                case '1':
                    requestSelection = true;
                    eventCount++;
                    break;
                case '0':
                    requestSelection = false;
                    eventCount++;
                    break;
                }
            }
        } else {
            for ( int i=0; i<values.length; i++ ) {
                requestSelection = parseSelectionToggle(values[0]);
                eventCount++;
            }
        }

        if ( eventCount==2 ) 
            requestSelection = true;

        if ( selected!=requestSelection ) {
            SForm.addArmedComponent(this);
        }
    }

    public void fireIntermediateEvents() {
        setSelected(requestSelection);
    }

    public void fireFinalEvents() {
        fireActionPerformed();
    }

    /**
     * @deprecated use {@link getEventTarget}
     */
    public final String getRealTarget() {
        return getEventTarget();
    }

    /**
     * @deprecated use {@link setEventTarget}
     */
    public final void setRealTarget(String t) {
        setEventTarget(t);
    }

    /**
     *
     */
    public final String getEventTarget() {
        return eventTarget;
    }

    /**
     *
     */
    public void setEventTarget(String target) {
        if ( isDifferent(eventTarget, target) ) {
            eventTarget = target;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    protected boolean parseSelectionToggle(String toggleParameter) {
        if ( "1".equals(toggleParameter) )
            return true;
        else if ( "0".equals(toggleParameter) )
            return false;

        // don't change...
        return selected;
    }

    public String getSelectionToggleParameter() {
        return selected ? getDeselectParameter() : getSelectParameter();
    }

    public String getSelectParameter() {
        String result = "1";
        if ( buttonGroup!=null ) {
            result = getUnifiedId() + UID_DIVIDER + result;
        }
        return result;
    }

    public String getDeselectParameter() {
        String result = "0";
        if ( buttonGroup!=null ) {
            result = getUnifiedId() + UID_DIVIDER + result;
        }
        return result;
    }


    // action implementation

    public void setAction(Action a) {
	Action oldValue = getAction();
	if (action == null || !action.equals(a)) {
	    action = a;
	    if (oldValue != null) {
		removeActionListener(oldValue);
		oldValue.removePropertyChangeListener(actionPropertyChangeListener);
		actionPropertyChangeListener = null;
	    }
	    configurePropertiesFromAction(action);
	    if (action != null) {		
		// Don't add if it is already a listener
		if (!isListener(ActionListener.class, action)) {
		    addActionListener(action);
		}
		// Reverse linkage:
		actionPropertyChangeListener = createActionPropertyChangeListener(action);
		action.addPropertyChangeListener(actionPropertyChangeListener);
	    }
	    firePropertyChange("action", oldValue, action);
	}
    }

    private boolean isListener(Class c, ActionListener a) {
	boolean isListener = false;
	Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == c && listeners[i+1] == a) {
                isListener = true;
	    }
	}
	return isListener;
    }

    public Action getAction() {
	return action;
    }

    protected void configurePropertiesFromAction(Action a) {
        // uncomment if compiled against < jdk 1.3
        //	setActionCommand((a != null 
        //                  ? (String)a.getValue(Action.ACTION_COMMAND_KEY) 
        //                  : null));
	setText((a != null ? (String)a.getValue(Action.NAME) : null));
	setIcon((a != null ? (SIcon)a.getValue(Action.SMALL_ICON) : null));
	setEnabled((a != null ? a.isEnabled() : true));
 	setToolTipText((a != null ? (String)a.getValue(Action.SHORT_DESCRIPTION) : null));	
    }

    protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
        return new ButtonActionPropertyChangeListener(this, a);
    }

    private static class ButtonActionPropertyChangeListener
        extends AbstractActionPropertyChangeListener
    {
	ButtonActionPropertyChangeListener(SAbstractButton b, Action a) {
	    super(b, a);
	}

	public void propertyChange(PropertyChangeEvent e) {	    
	    String propertyName = e.getPropertyName();
	    SButton button = (SButton)getTarget();
	    if (button == null) {
		Action action = (Action)e.getSource();
		action.removePropertyChangeListener(this);
            }
            else {
                if (e.getPropertyName().equals(Action.NAME)) {
                    String text = (String)e.getNewValue();
                    button.setText(text);
                }
                else if (e.getPropertyName().equals(Action.SHORT_DESCRIPTION)) {
                    String text = (String)e.getNewValue();
                    button.setToolTipText(text);
                }
                else if (propertyName.equals("enabled")) {
                    Boolean enabled = (Boolean)e.getNewValue();
                    button.setEnabled(enabled.booleanValue());
                }
                else if (e.getPropertyName().equals(Action.SMALL_ICON)) {
                    SIcon icon = (SIcon) e.getNewValue();
                    button.setIcon(icon);
                }
                // uncomment if compiled against jdk < 1.3
                /*                else if (e.getPropertyName().equals(Action.ACTION_COMMAND_KEY)) {
                    String actionCommand = (String)e.getNewValue();
                    button.setActionCommand(actionCommand);
                    }*/
            }
        }
    }

    /**
     * Sets the proper icons for buttonstatus enabled resp. disabled.
     */
    public void setIcons(SIcon[] icons) {
        setIcon(icons[ENABLED_ICON]);
        setDisabledIcon(icons[DISABLED_ICON]);
        setDisabledSelectedIcon(icons[DISABLED_SELECTED_ICON]);
        setRolloverIcon(icons[ROLLOVER_ICON]);
        setRolloverSelectedIcon(icons[ROLLOVER_SELECTED_ICON]);
        setPressedIcon(icons[PRESSED_ICON]);
        setSelectedIcon(icons[SELECTED_ICON]);
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */







