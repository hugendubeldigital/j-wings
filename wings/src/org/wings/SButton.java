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
import javax.swing.Action;

import java.util.ArrayList;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SButton extends SAbstractButton
{
    private static final String cgClassID = "ButtonCG";

    /**
     * TODO: documentation
     */
    protected SIcon icon = null;

    /**
     * TODO: documentation
     */
    protected SIcon disabledIcon = null;

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
    public SButton(SIcon i) {
        super(null);
        setIcon(i);
    }

    public SButton(String text, SIcon i) {
        super(text);
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
    public void setIcon(SIcon i) {
        SIcon oldIcon = icon;
        icon = i;
        if ((icon == null && oldIcon != null) ||
            icon != null && !icon.equals(oldIcon))
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SIcon getIcon() {
        return icon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledIcon(SIcon i) {
        SIcon oldDisabledIcon = disabledIcon;
        disabledIcon = i;
        if ((disabledIcon == null && oldDisabledIcon != null) ||
            disabledIcon != null && !disabledIcon.equals(oldDisabledIcon))
            reload(ReloadManager.RELOAD_CODE);
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

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(ButtonCG cg) {
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
