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

public final class ButtonCG
    extends org.wings.plaf.xhtml.ButtonCG
{
    protected void writeAnchorPrefix(Device d, SButton button)
        throws IOException
    {
        String tooltip = button.getToolTipText();
        Style style = button.getStyle();

        if (button.isEnabled()) {
            d.append("<a href=\"");
            writeAnchorAddress(d, button);
            d.append("\"");

            if (style != null)
                style.write(d);

            if (button.isSelected())
                d.append(" style=\"color:#990000\"");

            if (button.getRealTarget() != null)
                d.append(" target=\"").append(button.getRealTarget()).append("\"");

            if (tooltip != null)
                d.append(" title=\"").append(tooltip).append("\"");

            d.append(">");
        }
    }

    protected void writeFormPrefix(Device d, SButton button)
        throws IOException
    {
        Style style = button.getStyle();

        d.append("<input type=\"submit\"");

        if (style != null)
            style.write(d);

        if (button.isSelected())
            d.append(" style=\"color:#990000\"");
    }

    boolean writeSpan(final Device d, boolean writeSpan, final SComponent component)
        throws IOException
    {
        boolean spanWritten = !writeSpan;
        final AttributeSet attributes = component.getAttributes();
        final Style style = component.getStyle();

        if (attributes.size() > 0) { // script listeners
            if (!spanWritten) {
                d.append("<span");
                spanWritten = true;
            }
            d.append(" id=\"s_")
                .append(component.getUnifiedId())
                .append("\"");
        }

        if (style != null) {
            if (!spanWritten) {
                d.append("<span");
                spanWritten = true;
            }
            style.write(d);
        }

        if (spanWritten && writeSpan)
            d.append(">");
        return spanWritten;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
