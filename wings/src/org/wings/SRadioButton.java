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

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
