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

import javax.swing.Action;

import org.wings.plaf.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SButton extends SAbstractButton
{
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
     * @param text
     */
    public SButton(Action action) {
        super(action);
    }

    /**
     * TODO: documentation
     *
     */
    public SButton() {
        super();
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public SButton(SIcon i) {
        super();
        setIcon(i);
    }

    public SButton(String text, SIcon i) {
        super(text);
        setIcon(i);
    }

    /**
     * 
     */
    public void setSelected(boolean b) {
        // set in group as selected
        if ( getGroup()!=null ) {
            getGroup().setSelected(this, b);
        }

        // button is never in a selected state...
        super.setSelected(false);
    }

    /**
     * in form components the parameter value of an button is the button
     * text. So just toggle selection, in process request, if it is a request
     * for me.
     */
    protected boolean parseSelectionToggle(String toggleParameter) {
        return true;
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */





