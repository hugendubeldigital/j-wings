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

package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class DesktopPaneCG
    extends org.wings.plaf.xhtml.DesktopPaneCG
{
    protected void writePrefix(Device d, SDesktopPane c)
        throws IOException
    {
	Utils.writeSpanWithStyleAttributePrefix(d, c.getStyle());
    }

    protected void writePostfix(Device d, SDesktopPane c)
        throws IOException
    {
	Utils.writeSpanWithStyleAttributePostfix(d, c.getStyle());
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
