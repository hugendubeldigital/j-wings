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
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SSpacer
    extends SComponent
    implements SConstants
{
    int spaces = 1;
    int alignment = HORIZONTAL;

    /**
     * TODO: documentation
     *
     */
    public SSpacer() {
    }

    /**
     * TODO: documentation
     *
     * @param spaces
     */
    public SSpacer(int spaces) {
        setSpaces(spaces);
    }

    public SSpacer(int spaces, int alignment) {
        setSpaces(spaces);
        setAlignment(alignment);
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setSpaces(int s) {
        spaces = s;
    }

    /**
     * TODO: documentation
     *
     * @param a
     */
    public void setAlignment(int a) {
        alignment = a;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void appendBody(Device s) {
        for ( int i=0; i<spaces; i++ ) {
            if ( alignment == VERTICAL )
                s.append("<br />");
            else
                s.append("&nbsp;");
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
