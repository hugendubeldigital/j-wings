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
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SLineBorder
    implements SBorder
{
    int width = 1;

    /**
     * TODO: documentation
     *
     */
    public SLineBorder() {
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public SLineBorder(int i) {
        setThickness(i);
    }


    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setThickness(int i) {
        width = i;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void appendPrefix(Device s) {
        if ( width > 0 )
            s.append("<TABLE BORDER=").append(width).append(">").append("<TR><TD>");
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void appendPostfix(Device s) {
        if ( width > 0 )
            s.append("</TD></TR>").append("</TABLE>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
