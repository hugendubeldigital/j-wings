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

import java.io.*;
import org.wings.plaf.*;
import org.wings.io.Device;

/*
 * Wenn keine Ausgabe gewuenscht ist, und nur eine Komponente zum Fuellen
 * (v.B. beim GridLayout benoetigt wird, dann ist ein SDummy das richtige. Ein
 * SDummy hat auch kein Parent, so dass es mehrmals einem Container
 * hinzugefuegt werden kann.
 */
/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public final class SDummy
    extends SComponent
{
    /**
     * TODO: documentation
     */
    public static final SDummy DUMMY = new SDummy();

    /**
     * TODO: documentation
     *
     */
    public SDummy() {
    }

    /**
     * TODO: documentation
     *
     * @param d
     * @throws IOException
     */
    public void write(Device d)
        throws IOException
    {
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
