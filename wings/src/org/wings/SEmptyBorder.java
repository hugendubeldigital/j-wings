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
public class SEmptyBorder
    extends SAbstractBorder
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "EmptyBorderCG";

    /**
     * TODO: documentation
     *
     * @param insets
     */
    public SEmptyBorder(Insets insets) {
        setInsets(insets);
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this border.
     *
     * @return "EmptyBorderCG"
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
