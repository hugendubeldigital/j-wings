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

/*
 * SLineBreak.java
 * <table border width=50% align=left>
 *     <tr>
 *      <td>Daten;/td>
 *      <td>Daten;/td>
 *   </tr>
 * </table>
 */
/**
 * TODO: documentation
 *
 * @author
 * @version $Revision$
 */
public class SLineBreak
    extends SComponent
{
    private static final String cgClassID = "LineBreakCG";

    /**
     * TODO: documentation
     */
    protected int clear = SConstants.CLEAR_NO;

    /**
     * TODO: documentation
     *
     */
    public SLineBreak() {
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setClear(int c) {
        clear = c;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getClear() {
        return clear;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(LineBreakCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
