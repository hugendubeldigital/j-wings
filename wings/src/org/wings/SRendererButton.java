/*
  $Id$
  (c) Copyright 2002 mercatis information systems GmbH

  Part of e-lib 
 
  This file contains unpublished, proprietary trade secret information of
  mercatis information systems GmbH. Use, transcription, duplication and
  modification are strictly prohibited without prior written consent of
  mercatis information systems GmbH.
  See http://www.mercatis.de
*/

package org.wings;

import java.awt.event.ActionEvent;
import javax.swing.Action;


/**
 * <!--
 * SRendererButton.java
 * Created: Tue Sep  3 19:05:11 2002
 * -->
 *
 * This is a button for usage in a renderer (e.g {@link STableCellRenderer}). 
 * The special purpose of this button is
 * to fire an action event with action commands set at render time. So it is
 * possible to use the same button in a renderer multiple times and just set the
 * action command. 
 * A usage example is to implement a table header renderer with buttons for
 * sorting columns. Set as action command the column to sort...
 *
 * <p><b>(c)2002 <a href="http://www.mercatis.de">mercatis information systems GmbH</a></b></p>
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SRendererButton extends SButton  {
    
    /**
     * TODO: documentation
     *
     * @param text
     */
    public SRendererButton(String text) {
        super(text);
    }

    /**
     * TODO: documentation
     *
     * @param text
     */
    public SRendererButton(Action action) {
        super(action);
    }

    /**
     * TODO: documentation
     *
     */
    public SRendererButton() {
        super();
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public SRendererButton(SIcon i) {
        super(i);
    }

    public SRendererButton(String text, SIcon i) {
        super(text, i);
    }
    
    public String getSelectionParameter() {
        return getActionCommand()!=null ? getActionCommand() : "1";
    }

    
    protected String actionCommandToFire;

    /**
     * Fire an ActionEvent at each registered listener.
     */
    protected void fireActionPerformed() {
	if ( actionCommandToFire!=null ) {
	    fireActionEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
					    actionCommandToFire));
	    actionCommandToFire = null;
	} else {
	    super.fireActionPerformed();
	} /// end of if ()
    }

    public void processLowLevelEvent(String action, String[] values) {

	// a button can have only one event per request...
        if (  values.length>0 ) {
	    // set the action command to fire at fireFinalEvents.
	    actionCommandToFire = values[0];
            SForm.addArmedComponent(this);
        }
    }

}// SRendererButton

/*
   $Log$
   Revision 1.1  2002/09/03 17:17:09  ahaaf
   a button for usage in a renderer, look at the java doc comments

*/









