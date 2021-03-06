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

import java.awt.Color;

import org.wings.plaf.*;
import javax.swing.Action;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SToggleButton extends SAbstractButton {

    private static final String cgClassID = "ToggleButtonCG";

    /**
     * TODO: documentation
     *
     * @param text
     */
    public SToggleButton(String text) {
        super(text);
    }

    /**
     * Creates a button where properties are taken from the
     * Action supplied.
     *
     * @param a the Action used to specify the new button
     */
    public SToggleButton(Action action) {
        super(action);
    }

    /**
     * TODO: documentation
     *
     */
    public SToggleButton() {
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(ToggleButtonCG cg) {
        super.setCG(cg);
    }

    public void processLowLevelEvent(String action, String[] values) {
        boolean origSelected = isSelected();

        if (getGroup() != null) {
            getGroup().setDelayEvents(true);
            setSelected(parseSelectionToggle(values[0]));
            getGroup().setDelayEvents(false);
        } else {
            setSelected(parseSelectionToggle(values[0]));
        } // end of else


        if (isSelected() != origSelected) {
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
        if (getShowAsFormComponent()) {
            return !isSelected();
        } // end of if ()

        if ("1".equals(toggleParameter))
            return true;
        else if ("0".equals(toggleParameter))
            return false;


        // don't change...
        return isSelected();
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
