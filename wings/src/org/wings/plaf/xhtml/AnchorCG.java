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
import org.wings.*; import org.wings.border.*;

public class AnchorCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.AnchorCG
{
    private final static String propertyPrefix = "Anchor";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SBorder border = c.getBorder();
        SAnchor anchor = (SAnchor)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, anchor);
        Utils.writeContainerContents(d, anchor);
        writePostfix(d, anchor);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SAnchor anchor)
        throws IOException
    {
        String name = anchor.getName();
        String reference = anchor.getReference();

        d.print("<a");

        if (name != null)
            d.print(" name=\"").print(name).print("\"");

        if (reference != null)
            d.print(" href=\"").print(reference).print("\"");

        d.print(">");
    }

    protected void writePostfix(Device d, SAnchor anchor)
        throws IOException
    {
        d.print("</a>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
