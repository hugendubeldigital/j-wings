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
 * TODO: documentation
 *
 * @author Dominik Bartenstein
 * @version $Revision$
 */
public class SDivision
    extends SContainer
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "DivisionCG";

    /**
     * TODO: documentation
     */
    protected int alignment = SConstants.NO_ALIGN;

    /**
     * TODO: documentation
     *
     */
    public SDivision() {}

    /**
     * TODO: documentation
     *
     * @param al
     */
    public void setAlignment(int al) {
        alignment = al;
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "DivisionCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(DivisionCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
