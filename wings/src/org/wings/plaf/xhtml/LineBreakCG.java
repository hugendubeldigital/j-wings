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

package org.wings.plaf.xhtml;

import java.io.IOException;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class LineBreakCG
    implements org.wings.plaf.LineBreakCG
{
    private final static String propertyPrefix = "LineBreak";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent component) {
        component.setStyle(component.getSession().getCGManager().
                           getStyle(getPropertyPrefix() + ".style"));
    }

    public void uninstallCG(SComponent c) {
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SLineBreak lineBreak = (SLineBreak)c;

        d.append("<br");
        writeAttributes(d, lineBreak);
        d.append(" />\n");
    }

    protected void writeAttributes(Device d, SLineBreak lineBreak)
        throws IOException
    {
        int clear = lineBreak.getClear();

        if ( clear == SLineBreak.CLEAR_ALL )
            d.append(" clear=\"all\"");
        else if ( clear == SLineBreak.CLEAR_LEFT )
            d.append(" clear=\"left\"");
        else if ( clear == SLineBreak.CLEAR_RIGHT )
            d.append(" clear=\"right\"");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
