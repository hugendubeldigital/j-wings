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
 * @author Dominik Bartenstein
 * @version $Revision$
 */
public class SDivision
    extends SContainer
{
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
     * TODO: documentation
     *
     * @param s
     */
    public void appendPrefix(Device s) {
        s.append("<DIV");

        if ( alignment==SConstants.RIGHT_ALIGN )
            s.append(" ALIGN=RIGHT");
        else if ( alignment==SConstants.CENTER_ALIGN )
            s.append(" ALIGN=CENTER");
        else if ( alignment==SConstants.BLOCK_ALIGN )
            s.append(" ALIGN=JUSTIFY");

        s.append(">\n");
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void appendPostfix(Device s) {
        s.append("</DIV>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
