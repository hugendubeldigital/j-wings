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
import org.wings.style.*;

public final class ButtonCG
    extends org.wings.plaf.xhtml.ButtonCG
{
    protected void writeAnchorPrefix(Device d, SButton button)
        throws IOException
    {
        String tooltip = button.getToolTipText();
        Style style = button.getStyle();
        String id = button.getUnifiedId();

        if (button.isEnabled()) {
            d.append("<a href=\"");
            writeAnchorAddress(d, button);
            d.append("\" id=\"");
            d.append(id);
            d.append(button.getSession().getSuffixManager().nextSuffix(id, d));
            d.append("\"");

            if (button.getRealTarget() != null)
                d.append(" target=\"").append(button.getRealTarget()).append("\"");

            if (tooltip != null)
                d.append(" title=\"").append(tooltip).append("\"");

            d.append(">");
        }

        String inline = (button.isSelected()) ? "color:#990000" : null;
        boolean spanWritten = writeSpan(d, true, button, inline);

        if (button.isSelected())
            d.append(" style=\"color:#990000\"");
    }

    protected void writeFormPrefix(Device d, SButton button)
        throws IOException
    {
        Style style = button.getStyle();
        String id = button.getUnifiedId();

        d.append("<input type=\"submit\" id=\"");
        d.append(id);
        d.append(button.getSession().getSuffixManager().nextSuffix(id, d));
        d.append("\"");

        if (style != null)
            style.write(d);

        if (button.isSelected())
            d.append(" style=\"color:#990000\"");
    }

    boolean writeSpan(final Device d, boolean writeSpan, final SComponent component, final String inline)
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

        if (inline != null) {
            if (!spanWritten) {
                d.append("<span");
                spanWritten = true;
            }
            d.append(" style=\"");
            d.append(inline);
            d.append("\"");
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
