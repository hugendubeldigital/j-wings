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
    extends SCheckBox
{
    private static final String cgClassID = "RadioButtonCG";

    private static ResourceImageIcon DEFAULT_SELECTED_ICON =
        new ResourceImageIcon(SRadioButton.class, "icons/SelectedRadioButton.gif");

    private static ResourceImageIcon DEFAULT_NOT_SELECTED_ICON =
        new ResourceImageIcon(SRadioButton.class, "icons/NotSelectedRadioButton.gif");

    private static ResourceImageIcon DEFAULT_DISABLED_SELECTED_ICON =
        new ResourceImageIcon(SRadioButton.class, "icons/DisabledSelectedRadioButton.gif");

    private static ResourceImageIcon DEFAULT_DISABLED_NOT_SELECTED_ICON =
        new ResourceImageIcon(SRadioButton.class, "icons/DisabledNotSelectedRadioButton.gif");

    /**
     * TODO: documentation
     */
    protected String label = "";

    /**
     * TODO: documentation
     *
     */
    public SRadioButton() {
        setDefaultIcons();
        setType(RADIOBUTTON);
    }

    /**
     * TODO: documentation
     *
     * @param label
     */
    public SRadioButton(String label) {
        super(label);
        setDefaultIcons();
        setType(RADIOBUTTON);
    }

    /**
     * TODO: documentation
     *
     * @param selected
     */
    public SRadioButton(boolean selected) {
        super(selected);
        setDefaultIcons();
        setType(RADIOBUTTON);
    }

    private void setDefaultIcons() {
        setSelectedIcon(DEFAULT_SELECTED_ICON);
        setDisabledSelectedIcon(DEFAULT_DISABLED_SELECTED_ICON);
        setIcon(DEFAULT_NOT_SELECTED_ICON);
        setDisabledIcon(DEFAULT_DISABLED_NOT_SELECTED_ICON);
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setType(String t) {
        super.setType(SConstants.RADIOBUTTON);
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
