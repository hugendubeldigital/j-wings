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

public final class ButtonCG
    extends org.wings.plaf.xhtml.ButtonCG
{
    protected void writeAnchorPrefix(Device d, SButton button)
        throws IOException
    {
        String tooltip = button.getToolTipText();

        if (button.isEnabled()) {
            d.append("<a href=\"");
            writeAnchorAddress(d, button);
            d.append("\"");

            if (button.isSelected())
                Utils.writeStyleAttribute(d, "anchor", button.getStyle(), "selection");
            else
                Utils.writeStyleAttribute(d, "anchor", button.getStyle(), "nonselection");

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
        d.append("<input type=\"submit\"");

        if (button.isSelected())
            Utils.writeStyleAttribute(d, "form", button.getStyle(), "selection");
        else
            Utils.writeStyleAttribute(d, "form", button.getStyle(), "nonselection");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
