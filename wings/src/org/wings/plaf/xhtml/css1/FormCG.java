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

import org.wings.*;
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

        d.append("<form method=\"");
        if (form.getMethod())
            d.append("post");
        else
            d.append("get");
        d.append("\"");

        Utils.writeStyleAttribute(d, c.getStyle());

        if (encodingType != null)
            d.append(" enctype=\"").append(encodingType).append("\"");

        d.append(" action=\"").append(form.getRequestURL()).
            append("\">\n");

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
 * End:
 */
