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

import java.awt.Color;
import java.awt.Insets;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SBevelBorder
    extends SAbstractBorder
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "BevelBorderCG";

    public static final int RAISED = 0;
    public static final int LOWERED = 1;

    int bevelType = RAISED;

    /**
     * TODO: documentation
     *
     */
    public SBevelBorder() {}

    /**
     * TODO: documentation
     *
     * @param bevelType
     */
    public SBevelBorder(int bevelType) {
        setBevelType(bevelType);
    }

    /**
     * TODO: documentation
     *
     * @param bevelType
     * @param insets
     */
    public SBevelBorder(int bevelType, Insets insets) {
        setBevelType(bevelType);
        setInsets(insets);
    }

    /**
     * TODO: documentation
     *
     * @param bevelType
     */
    public void setBevelType(int bevelType) {
        this.bevelType = bevelType;
    }
    
    public int getBevelType() { return bevelType; }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this border.
     *
     * @return "BevelBorderCG"
     * @see SBorder#getCGClassID
     * @see org.wings.plaf.CGDefaults#getCG
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
