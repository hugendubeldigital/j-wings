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

package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class HRefCG
    extends org.wings.plaf.xhtml.HRefCG
{
    protected void writeAnchorPrefix(Device d, SHRef hRef)
        throws IOException
    {
        String tooltip = hRef.getToolTipText();

        if (hRef.isEnabled()) {
            d.append("<a href=\"").append(hRef.getReference()).append("\"");

            Utils.writeStyleAttribute(d, hRef.getStyle());

            if (hRef.getRealTarget() != null)
                d.append(" target=\"").append(hRef.getRealTarget()).append("\"");

            if (tooltip != null)
                d.append(" title=\"").append(tooltip).append("\"");

            d.append(">");
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
