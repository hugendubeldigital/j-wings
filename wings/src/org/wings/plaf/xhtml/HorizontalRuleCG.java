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

public class HorizontalRuleCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.HorizontalRuleCG
{
    private final static String propertyPrefix = "HorizontalRule";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SHorizontalRule hrule = (SHorizontalRule)c;

        d.append("\n<hr");
        writeAttributes(d, hrule);
        d.append(" />\n");
    }

    protected void writeAttributes(Device d, SHorizontalRule hrule)
        throws IOException
    {
        int width = hrule.getWidth();
        int size = hrule.getSize();
        int alignment = hrule.getAlignment();
        boolean shade = hrule.getShade();

        if ( width > 0 )
            d.append(" width=\"").append(width).append("\"");

        if ( alignment == SHorizontalRule.RIGHT_ALIGN )
            d.append(" align=\"right\"");
        else if ( alignment == SHorizontalRule.CENTER_ALIGN )
            d.append(" align=\"center\"");
        else if ( alignment == SHorizontalRule.BLOCK_ALIGN )
            d.append(" align=\"justify\"");

        if ( size > 0 )
            d.append(" size=\"").append(size).append("\"");

        if ( !shade )
            d.append(" noshade");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
