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

public final class CheckBoxCG
    extends org.wings.plaf.xhtml.CheckBoxCG
{
    protected void writeAnchorPrefix(Device d, SCheckBox checkBox)
        throws IOException
    {
        String tooltip = checkBox.getToolTipText();

        if (checkBox.isEnabled()) {
            d.print("<a href=\"");
            writeAnchorAddress(d, checkBox);
            d.print("\"");

            if (checkBox.isSelected())
                Utils.writeStyleAttribute(d, "anchor", checkBox.getStyle(), "selection");
            else
                Utils.writeStyleAttribute(d, "anchor", checkBox.getStyle(), "nonselection");

            if (checkBox.getRealTarget() != null)
                d.print(" target=\"").print(checkBox.getRealTarget()).print("\"");

            if (tooltip != null)
                d.print(" title=\"").print(tooltip).print("\"");

            d.print(">");
        }
    }

    protected void writeFormPrefix(Device d, SCheckBox checkBox)
        throws IOException
    {
        d.print("<input type=\"");
        d.print(checkBox.getType());
        d.print("\"");

        if (checkBox.isSelected())
            Utils.writeStyleAttribute(d, "form", checkBox.getStyle(), "selection");
        else
            Utils.writeStyleAttribute(d, "form", checkBox.getStyle(), "nonselection");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
