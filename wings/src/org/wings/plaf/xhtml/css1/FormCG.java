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

package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class FormCG
    extends org.wings.plaf.xhtml.FormCG
{
    protected void writePrefix(Device d, SContainer c)
        throws IOException
    {
        SForm form = (SForm) c;
        String encodingType = form.getEncodingType();

        d.print("<form method=\"");
        if (form.isPostMethod())
            d.print("post");
        else
            d.print("get");
        d.print("\"");

        Utils.writeStyleAttribute(d, c.getStyle());

        if (encodingType != null)
            d.print(" enctype=\"").print(encodingType).print("\"");

        d.print(" action=\"").print(form.getRequestURL()).
            print("\">\n");

        // the event for the form action
        Utils.writeHiddenComponent(d, form.getNamePrefix(),
                                   form.getUnifiedId() +
                                   SConstants.UID_DIVIDER);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
