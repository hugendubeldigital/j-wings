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
 * A horizontal or vertical spacer. The horizontal space is a
 * &amp;nbsp;, the vertical a &lt;br /&gt;
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SSpacer
    extends SComponent
    implements SConstants
{
    private static final String cgClassID = "SpacerCG";

    protected int spaces = 1;
    protected int orientation = HORIZONTAL;

    /**
     * Creates a new horizontal spacer, one space long.
     */
    public SSpacer() {
    }

    /**
     * Creates a new horizontal spacer with the given number of spaces.
     *
     * @param spaces Number of spaces
     */
    public SSpacer(int spaces) {
        setSpaces(spaces);
    }

    /**
     * Creates a new spacer (horizontal or vertical) with the given number
     * of spaces.
     *
     * @param spaces Number of spaces
     * @param orientation One of the following constants from
     *        <code>SConstants</code>:
     *        <code>HORIZONTAL</code> or <code>VERTICAL</code>.
     * @see SConstants
     */
    public SSpacer(int spaces, int orientation) {
        setSpaces(spaces);
        setOrientation(orientation);
    }

    /**
     * Sets the number of spaces.
     *
     * @param s Number of spaces.
     */
    public void setSpaces(int s) {
        spaces = s;
    }

    /**
     * gets the number of spaces.
     *
     * @param s Number of spaces.
     */
    public int getSpaces() {
        return spaces;
    }

    /**
     * sets the orientation of this space: HORIZONTAL or VERTICAL.
     * @param orientation one of SConstants.HORIZONTAL or SConstants.VERTICAL
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     * returns the current orientation; one of SConstants.HORIZONTAL or
     * SConstants.VERTICAL
     */
    public int getOrientation() {
        return orientation;
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(SpacerCG cg) {
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
