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

public class SeparatorCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.SeparatorCG
{
    private final static String propertyPrefix = "Separator";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SSeparator separator = (SSeparator)c;

        d.append("\n<hr");
        writeAttributes(d, separator);
        d.append(" />\n");
    }

    protected void writeAttributes(Device d, SSeparator separator)
        throws IOException
    {
        int width = separator.getWidth();
        int size = separator.getSize();
        int alignment = separator.getAlignment();
        boolean shade = separator.getShade();

        if ( width > 0 )
            d.append(" width=\"").append(width).append("\"");

        if ( alignment == SSeparator.RIGHT_ALIGN )
            d.append(" align=\"right\"");
        else if ( alignment == SSeparator.CENTER_ALIGN )
            d.append(" align=\"center\"");
        else if ( alignment == SSeparator.BLOCK_ALIGN )
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
 * End:
 */
