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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import java.net.URL;
import javax.swing.*;

import java.util.ArrayList;

import org.wings.plaf.*;
import org.wings.io.Device;
import org.wings.externalizer.ExternalizeManager;

/**
 * TODO: documentation
 *
 * @author Dominik Bartenstein
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SButton extends SAbstractButton
{
    private static final String cgClassID = "ButtonCG";

    /**
     * TODO: documentation
     */
    protected Icon icon = null;

    /**
     * TODO: documentation
     */
    protected String iconAddress = null;

    /**
     * TODO: documentation
     */
    protected Icon disabledIcon = null;

    /**
     * TODO: documentation
     */
    protected String disabledIconAddress = null;

    /**
     * TODO: documentation
     */
    protected int verticalTextPosition = CENTER;
    /**
     * TODO: documentation
     */
    protected int horizontalTextPosition = RIGHT;

    /**
     * TODO: documentation
     */
    protected  int iconTextGap = 1;

    /**
     * TODO: documentation
     *
     * @param text
     */
    public SButton(String text) {
        super(text);
    }

    /**
     * TODO: documentation
     *
     */
    public SButton() {
        super(null);
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public SButton(Icon i) {
        super(null);
        setIcon(i);
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
    public int getHorizontalTextPosition() {
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
    public int getVerticalTextPosition() {
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
    public int getIconTextGap() {
        return iconTextGap;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setIcon(Icon i) {
        icon = i;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setIcon(URL i) {
        if ( i!=null)
            setIcon(i.toString());
    }

    /**
     * TODO: documentation
     *
     * @param url
     */
    public void setIcon(String url) {
        iconAddress = url;
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
     * @return
     */
    public String getIconAddress() {
        return iconAddress;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledIcon(Icon i) {
        disabledIcon = i;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledIcon(URL i) {
        if ( i!=null)
            setDisabledIcon(i.toString());
    }

    /**
     * TODO: documentation
     *
     * @param url
     */
    public void setDisabledIcon(String url) {
        disabledIconAddress = url;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Icon getDisabledIcon() {
        if(disabledIcon == null)
            if(icon != null && icon instanceof ImageIcon)
                disabledIcon = new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon)icon).getImage()));
        return disabledIcon;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getDisabledIconAddress() {
        return disabledIconAddress;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    protected boolean showIcon() {
        return ( icon!=null || iconAddress!=null );
    }


    private Action action;
    private PropertyChangeListener actionPropertyChangeListener;

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
	setActionCommand((a != null ? (String)a.getValue(Action.ACTION_COMMAND_KEY) : null));
	setText((a != null ? (String)a.getValue(Action.NAME) : null));
	setIcon((a != null ? (Icon)a.getValue(Action.SMALL_ICON) : null));
	setEnabled((a != null ? a.isEnabled() : true));
 	setToolTipText((a != null ? (String)a.getValue(Action.SHORT_DESCRIPTION) : null));	
    }

    protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
        return new ButtonActionPropertyChangeListener(this, a);
    }

    private static class ButtonActionPropertyChangeListener
        extends AbstractActionPropertyChangeListener
    {
	ButtonActionPropertyChangeListener(SButton b, Action a) {
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
                    Icon icon = (Icon)e.getNewValue();
                    button.setIcon(icon);
                }
                else if (e.getPropertyName().equals(Action.ACTION_COMMAND_KEY)) {
                    String actionCommand = (String)e.getNewValue();
                    button.setActionCommand(actionCommand);
                }
            }
        }
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "ButtonCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
