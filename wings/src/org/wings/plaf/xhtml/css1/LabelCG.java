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
import java.awt.Color;
import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class LabelCG
    extends org.wings.plaf.xhtml.LabelCG
{
    protected void writeText(Device d, SLabel label)
        throws IOException
    {
        String text = label.getText();
        if (text == null) return;

        final boolean noBreak = label.isNoBreak();
        if (noBreak || text.trim().length() > 0) {
            final boolean escape   = label.isEscapeSpecialChars();
            final SFont font       = label.getFont();
            final Color foreground = label.getForeground();
            final Color background = label.getBackground();

            Utils.writeSpanWithStyleAttributePrefix(d, label );
            // override additional settings
            Utils.writeFontPrefix(d, font, foreground);

            if (escape)
                text = org.wings.plaf.xhtml.Utils.escapeSpecialChars(text);

            if (noBreak) {
                text=org.wings.plaf.xhtml.Utils.escapeSpace(text);
                d.append("<nobr>");
            }
            d.append(text);

            if (noBreak)
                d.append("</nobr>");
            Utils.writeFontPostfix(d, font, foreground);
            Utils.writeSpanWithStyleAttributePostfix(d, label);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
