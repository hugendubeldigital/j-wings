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

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author
 * @version $Revision$
 */
public class SHorizontalRule
    extends SComponent
{
    private static final String cgClassID = "HorizontalRuleCG";

    /**
     * TODO: documentation
     */
    protected boolean shade = true;

    /**
     * TODO: documentation
     */
    protected int width = 0;

    /**
     * TODO: documentation
     */
    protected int size = 0;

    /**
     * TODO: documentation
     */
    protected int alignment = SConstants.NO_ALIGN;


    /**
     * TODO: documentation
     *
     */
    public SHorizontalRule() {
    }


    /**
     * TODO: documentation
     *
     * @param al
     */
    public void setAlignment(int al) {
        alignment = al;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getAlignment() {
        return alignment;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setSize(int s) {
        size = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setWidth(int s) {
        width = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setShade(boolean s) {
        shade = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean getShade() {
        return shade;
    }

    /**
     * TODO: documentation
     *
     * @return
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
