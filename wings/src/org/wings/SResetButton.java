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

/**
 * TODO: documentation
 *
 * @author Dominik Bartenstein
 * @version $Revision$
 */
public class SResetButton
    extends SAbstractButton
{
    private static final String cgClassID = "ResetButtonCG";

    /**
     * TODO: documentation
     *
     * @param text
     */
    public SResetButton(String text) {
        super(text);
    }

    /**
     * TODO: documentation
     *
     */
    public SResetButton() {
        this("RESET");
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setType(String t) {
        super.setType(SConstants.RESET_BUTTON);
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "ResetButtonCG"
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
