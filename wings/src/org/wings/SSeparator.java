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

/**
 * this is the same as a SHorizontalRule
 *
 * @see SHorizontalRule
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SSeparator
    extends SHorizontalRule
{
    private static final String cgClassID = "SeparatorCG";

    /**
     * creates a new separator
     */
    public SSeparator() {
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(SeparatorCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
