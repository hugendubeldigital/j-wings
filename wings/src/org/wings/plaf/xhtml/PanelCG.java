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

public class PanelCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.PanelCG
{
    private final static String propertyPrefix = "Panel";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SBorder border = c.getBorder();
        SPanel panel = (SPanel)c;

        //Utils.writeBorderPrefix(d, border);
        writePrefix(d, panel);
        Utils.writeContainerContents(d, panel);
        writePostfix(d, panel);
        //Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SPanel panel)
        throws IOException
    {
    }

    protected void writePostfix(Device d, SPanel panel)
        throws IOException
    {
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
