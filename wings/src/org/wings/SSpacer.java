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
    protected int spaces = 1;
    protected int alignment = HORIZONTAL;

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
     * @param alignment One of the following constants from
     *        <code>SConstants</code>:
     *        <code>HORIZONTAL</code> or <code>VERTICAL</code>.
     * @see SConstants
     */
    public SSpacer(int spaces, int alignment) {
        setSpaces(spaces);
        setAlignment(alignment);
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
     * Sets the alignment? Why do we need an alignment???
     *
     * @param a
     */
    public void setAlignment(int a) {
        alignment = a;
    }

    /**
     * internal writing function
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
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
