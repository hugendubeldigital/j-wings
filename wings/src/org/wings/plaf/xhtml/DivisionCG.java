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

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class DivisionCG
    extends org.wings.plaf.AbstractCG
    implements org.wings.plaf.DivisionCG
{
    private final static String propertyPrefix = "Division";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SBorder border = c.getBorder();
        SDivision division = (SDivision)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, division);
        Utils.writeContainerContents(d, division);
        writePostfix(d, division);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SDivision division)
        throws IOException
    {
	d.append("\n<div>\n");
    }

    protected void writePostfix(Device d, SDivision division)
        throws IOException
    {
	d.append("\n</div>\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
