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

public class ContainerCG
    implements org.wings.plaf.ContainerCG
{
    private final static String propertyPrefix = "Container";

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
        SContainer container = (SContainer)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, container);
        Utils.writeContainerContents(d, container);
        writePostfix(d, container);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SContainer container)
        throws IOException
    {
    }

    protected void writePostfix(Device d, SContainer container)
        throws IOException
    {
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
