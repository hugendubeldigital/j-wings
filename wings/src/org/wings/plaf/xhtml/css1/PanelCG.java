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
import org.wings.style.*;

public final class PanelCG
    extends org.wings.plaf.xhtml.PanelCG
{
    public void write(Device d, SComponent c)
        throws IOException
    {
        boolean divWritten = writeDiv(d, true, c);
        org.wings.plaf.xhtml.Utils.writeContainerContents(d, (SContainer)c);
        if (divWritten)
            d.append("</div>");
    }

    boolean writeDiv(final Device d, boolean writeDiv, final SComponent component)
        throws IOException
    {
        boolean divWritten = !writeDiv;
        final AttributeSet attributes = component.getAttributes();
        final Style style = component.getStyle();

        if (attributes.size() > 0) { // script listeners
            if (!divWritten) {
                d.append("<div");
                divWritten = true;
            }
            d.append(" id=\"s_")
                .append(component.getUnifiedId())
                .append("\"");
        }

        if (style != null) {
            if (!divWritten) {
                d.append("<div");
                divWritten = true;
            }
            style.write(d);
        }

        if (divWritten && writeDiv)
            d.append(">");
        return divWritten;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
