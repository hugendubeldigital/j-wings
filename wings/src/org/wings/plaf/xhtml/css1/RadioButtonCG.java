/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
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

public final class RadioButtonCG
    extends org.wings.plaf.xhtml.RadioButtonCG
{
    protected void writeAnchorPrefix(Device d, SCheckBox checkBox)
        throws IOException
    {
        String tooltip = checkBox.getToolTipText();

        if (checkBox.isEnabled() && !checkBox.isSelected()) {
            d.append("<a href=\"").append(generateAnchorAddress(checkBox)).append("\"");

            Utils.writeStyleAttribute(d, "anchor", checkBox.getStyle());

            if (checkBox.getRealTarget() != null)
                d.append(" target=\"").append(checkBox.getRealTarget()).append("\"");

            if (tooltip != null)
                d.append(" title=\"").append(tooltip).append("\"");

            d.append(">");
        }
    }

    protected void writeFormPrefix(Device d, SCheckBox checkBox)
        throws IOException
    {
        d.append("<input type=\"");
        d.append(checkBox.getType());
        d.append("\"");
        Utils.writeStyleAttribute(d, "form", checkBox.getStyle());
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
