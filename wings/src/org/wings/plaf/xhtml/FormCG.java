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
import org.wings.*; import org.wings.border.*;

public class FormCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.FormCG
{
    private final static String propertyPrefix = "Form";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SBorder border = c.getBorder();
        SForm form = (SForm)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, form);
        Utils.writeContainerContents(d, form);
        writePostfix(d, form);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SForm form)
        throws IOException
    {
        String encodingType = form.getEncodingType();

        d.print("<form method=\"");
        if (form.isPostMethod())
            d.print("post");
        else
            d.print("get");
        d.print("\"");

        if (encodingType != null)
            d.print(" enctype=\"").print(encodingType).print("\"");

        d.print(" action=\"");
        form.getRequestURL().write(d);
        d.print("\">\n");

        // the event for the form action
        Utils.writeHiddenComponent(d, form.getNamePrefix(),
                                   form.getUnifiedId() +
                                   SConstants.UID_DIVIDER);
    }

    protected void writePostfix(Device d, SForm form)
        throws IOException
    {
        d.print("</form>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
