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


import org.wings.plaf.*;

/**
 * <form>
 * <b>Radiobuttons:</b>
 * <p><input type="radio" name="1" value="1" checked>One</p>
 * <p><input type="radio" name="1" value="2">Two</p>
 * <p><input type="radio" name="1" value="3">Three</p>
 * </form>
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SRadioButton
    extends SAbstractButton
{
    private static final String cgClassID = "RadioButtonCG";

    /**
     * TODO: documentation
     *
     */
    public SRadioButton() {
        setType(RADIOBUTTON);
    }

    /**
     * TODO: documentation
     *
     * @param label
     */
    public SRadioButton(String label) {
        super(label, RADIOBUTTON);
    }

    /**
     * TODO: documentation
     *
     * @param selected
     */
    public SRadioButton(boolean selected) {
        this();
        setSelected(selected);
    }

    public String getLowLevelEventId() {
        if ( getGroup()!=null && isRenderedAsFormInput() ) {
            return getGroup().getComponentId();
        } else {
            return super.getLowLevelEventId();
        } // end of if ()
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setType(String t) {
        if ( !RADIOBUTTON.equals(t) )
            throw new IllegalArgumentException("type change not supported, type is fix: radiobutton");
        
        super.setType(t);
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(RadioButtonCG cg) {
        super.setCG(cg);
    }

    protected boolean isRenderedAsFormInput() {
        return getShowAsFormComponent() &&
            getIcon()==null;
    }

    public void processLowLevelEvent(String action, String[] values) {
        System.out.println("got command " + action);

        for (int i=0; i<values.length; i++) {
            System.out.println("got event " + i + " " + values[i] + 
                               " parsed " + parseSelectionToggle(values[i])); 
        } // end of for (int i=0; i<; i++)
        



        boolean origSelected = isSelected();

        if ( isRenderedAsFormInput() ) {
            System.out.println("rendered as form input");

            if ( getGroup()==null ) {
                // one hidden and one checked event from the form says select
                // it, else deselect it (typically only the hidden event)
                setSelected(values.length==2);
            } else {
                int eventCount = 0;
                for ( int i=0; i<values.length; i++) {
                    // check the count of events, which are for me - with a
                    // button group, the value is my component id, if a event is
                    // for me   
                    if ( getComponentId().equals(values[i]) ) {
                        eventCount++;
                    } // end of if ()
                } // end of for (int i=0; i<; i++)
                // one hidden and one checked event from the form says select
                // it, else deselect it (typically only the hidden event)
                setSelected(eventCount==2);
            } // end of if ()
            System.out.println("set selection to " + isSelected());
        } else {
            if ( getGroup()!=null ) {
                getGroup().setDelayEvents(true);
                setSelected(parseSelectionToggle(values[0]));
                getGroup().setDelayEvents(false);
            } else {
                setSelected(parseSelectionToggle(values[0]));
            } // end of else
        }        
 
        if ( isSelected()!=origSelected ) {
            // got an event, that is a select...
            SForm.addArmedComponent(this);
        } // end of if ()
    }

    /**
     * in form components the parameter value of an button is the button
     * text. So just toggle selection, in process request, if it is a request
     * for me.
     */
    protected boolean parseSelectionToggle(String toggleParameter) {
	// a button/image in a form has no value, so just toggle selection...
	if ( getShowAsFormComponent() ) {
            System.out.println("form component");
	    return !isSelected();
	} // end of if ()

	if ( "1".equals(toggleParameter) )
	    return true;
	else if ( "0".equals(toggleParameter) )
	    return false;
	
	
	// don't change...
	return isSelected();
    }

    public String getSelectionParameter() {
        if ( getGroup()!=null && isRenderedAsFormInput() ) {
            return getComponentId();
        } else {
            return "1";
        } 
    }
  
    public String getDeselectionParameter() {
        if ( getGroup()!=null && isRenderedAsFormInput() ) {
            return getComponentId();
        } else {
            return "0";
        } 
    }


}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
