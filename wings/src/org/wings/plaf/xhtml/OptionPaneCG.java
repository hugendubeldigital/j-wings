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

import java.awt.Color;
import java.io.IOException;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class OptionPaneCG
    extends org.wings.plaf.AbstractCG
    implements org.wings.plaf.OptionPaneCG
{
    private final static String propertyPrefix = "OptionPane";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SBorder border = c.getBorder();
        SOptionPane optionPane = (SOptionPane)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, optionPane);
        Utils.writeContainerContents(d, optionPane);
        writePostfix(d, optionPane);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SOptionPane optionPane)
        throws IOException
    {
        String encodingType = optionPane.getEncodingType();

        d.append("<form method=\"");
        if (optionPane.getMethod())
            d.append("post");
        else
            d.append("get");
        d.append("\"");

        if (encodingType != null)
            d.append(" enctype=\"").append(encodingType).append("\"");

        d.append(" action=\"").append(optionPane.getServerAddress()).
            append("\">\n");
    }

    protected void writePostfix(Device d, SOptionPane optionPane)
        throws IOException
    {
        d.append("</form>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
