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

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class AnchorCG
    extends org.wings.plaf.xhtml.AnchorCG
{
    protected void writePrefix(Device d, SAnchor anchor)
        throws IOException
    {
        String name = anchor.getName();
        String reference = anchor.getReference();

        d.append("<a");

        Utils.writeStyleAttribute(d, anchor.getStyle());

        if (name != null)
            d.append(" name=\"").append(name).append("\"");

        if (reference != null)
            d.append(" href=\"").append(reference).append("\"");

        d.append(">");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */