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
import java.io.IOException;
import java.util.*;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * This is a <a href="SBorderLayout.html">SBorderLayout</a>, which occupies the whole browser window.
 * Works perfect on IE, and pretty good on Netscape, Mozilla and Opera *sniff*.
 * The SContainer has a fixed position at 0x0 (upper left corner), so it should only be used to
 * set Layout for <tt>org.wings.SFrame.getContentPane()</tt>!
 * @author <a href="mailto:andre.lison@crosstec.de">Andre Lison</a>
 * @version $Revision$
 */
public class SFullScreenLayout
    extends SBorderLayout
{
    /**
     * @see #getCGClassID
     */
    private static final String _cgClassID = "FullScreenLayoutCG";

    private static final boolean DEBUG = true;

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this layout.
     *
     * @return "BorderLayoutCG"
     * @see SLayoutManager#getCGClassID
     * @see org.wings.plaf.CGDefaults#getCG
     */
    public String getCGClassID() {
        return _cgClassID;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
