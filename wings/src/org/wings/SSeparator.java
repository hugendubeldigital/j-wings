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
import org.wings.io.Device;

/**
 * This is a separator. Example: <hr>
 *
 * @version $Revision$
 */
public class SSeparator
    extends SComponent
{
    private static final String cgClassID = "SeparatorCG";

    /**
     * shaded rule?
     */
    protected boolean shade = true;

    /**
     * width of the rule
     */
    protected int width = 0;

    /**
     * size of the rule (percent?)
     */
    protected int size = 0;

    /**
     * the aligment
     */
    protected int alignment = SConstants.NO_ALIGN;


    /**
     * Creates s new separator. Shade is enabled, width and size = 0.
     */
    public SSeparator() {
    }


    /**
     * sets the alignment
     *
     * @param al
     */
    public void setAlignment(int al) {
        alignment = al;
    }

    /**
     * returns the alignment
     *
     * @return
     */
    public int getAlignment() {
        return alignment;
    }

    /**
     * sets the size of the rule
     *
     * @param s
     */
    public void setSize(int s) {
        size = s;
    }

    /**
     * returns the size of the rule
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * sets the width of the rule
     *
     * @param s the width
     */
    public void setWidth(int s) {
        width = s;
    }

    /**
     * returns the width of the rule
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * enable or disable shading of the rule
     *
     * @param s shade yes or no
     */
    public void setShade(boolean s) {
        shade = s;
    }

    /**
     * returns the shading
     *
     * @return yes or no
     */
    public boolean getShade() {
        return shade;
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(SeparatorCG cg) {
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
