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

import java.awt.Color;
import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class ParagraphCG
    implements org.wings.plaf.ParagraphCG
{
    private final static String propertyPrefix = "Paragraph";

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
        SBorder border = c.getBorder();
        SParagraph paragraph = (SParagraph)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, paragraph);
        Utils.writeContainerContents(d, paragraph);
        writePostfix(d, paragraph);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SParagraph paragraph)
        throws IOException
    {
        d.append("<p");
        writeAttributes(d, paragraph);
        d.append(">");
    }

    protected void writeAttributes(Device d, SParagraph paragraph)
        throws IOException
    {
        int alignment = paragraph.getAlignment();

        if ( alignment == SParagraph.RIGHT_ALIGN )
            d.append(" align=right");
        else if ( alignment == SParagraph.CENTER_ALIGN )
            d.append(" align=center");
        else if ( alignment == SParagraph.BLOCK_ALIGN )
            d.append(" align=justify");
    }

    protected void writePostfix(Device d, SParagraph paragraph)
        throws IOException
    {
        d.append("</p>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
