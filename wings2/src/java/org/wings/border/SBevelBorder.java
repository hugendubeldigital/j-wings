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

package org.wings.border;

import java.awt.Color;
import java.awt.Insets;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * Draw a beveled border around a component.
 * <span style="border-style: outset; border-width: 3px;">RAISED</span>
 * <span style="border-style: inset; border-width: 3px;">LOWERED</span>
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public class SBevelBorder
    extends SAbstractBorder
{
    public static final int RAISED = 0;
    public static final int LOWERED = 1;

    private int bevelType = RAISED;
    
    /**
     * Creates a new beveled raised border.
     */
    public SBevelBorder() {}

    /**
     * Creates a new beveled border with the given type.
     *
     * @param bevelType one of the following constants:
     *        <code>RAISED</code> or <code>LOWERED</code>
     */
    public SBevelBorder(int bevelType) {
        setBevelType(bevelType);
    }

    /**
     * Creates a new beveled border with the given type and insets.
     *
     * @param bevelType one of the following constants:
     *        <code>RAISED</code> or <code>LOWERED</code>
     * @param padding (space between border and component) around
     */
    public SBevelBorder(int bevelType, Insets insets) {
        super(insets);
        setBevelType(bevelType);
    }

    /**
     * Creates a new beveled border with the given type, insets and
     * border thickness
     *
     * @param bevelType one of the following constants:
     *        <code>RAISED</code> or <code>LOWERED</code>
     * @param insets padding (space between border and component) around
     * @param thickness the thickness of drawn line
     */
    public SBevelBorder(int bevelType, Insets insets, int thickness ) {
        super(Color.black, thickness, insets);
        setBevelType(bevelType);
    }

    /**
     * sets the bevel type.
     *
     * @param bevelType one of the following constants:
     *        <code>RAISED</code> or <code>LOWERED</code>
     */
    public void setBevelType(int bevelType) {
        this.bevelType = bevelType;
    }

    /**
     * returns the bevel type.
     *
     * @return the current bevel type
     * @see #setBevelType
     */
    public int getBevelType() {
        return bevelType;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
