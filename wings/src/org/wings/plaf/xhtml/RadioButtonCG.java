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

import java.io.IOException;

import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.util.*;

public class RadioButtonCG
    extends CheckBoxCG
    implements org.wings.plaf.RadioButtonCG
{
    private final static String propertyPrefix = "RadioButton";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent component) {
        component.setStyle(component.getSession().getCGManager().
                           getStyle(getPropertyPrefix() + ".style"));
    }

    public void uninstallCG(SComponent c) {
    }

    protected void writeAnchorPrefix(Device d, SCheckBox checkBox)
        throws IOException
    {
        String tooltip = checkBox.getToolTipText();

        if (checkBox.isEnabled() && !checkBox.isSelected()) {
            d.append("<a href=\"");
            writeAnchorAddress(d, checkBox);
            d.append("\"");

            if (checkBox.getRealTarget() != null)
                d.append(" target=\"").append(checkBox.getRealTarget()).append("\"");

            if (tooltip != null)
                d.append(" title=\"").append(tooltip).append("\"");

            d.append(">");
        }
    }

    protected void writeAnchorBody(Device d, SCheckBox checkBox)
        throws IOException
    {
        String text = checkBox.getText();
        boolean noBreak = checkBox.isNoBreak();

        if (text == null)
            text = "";
        d.append((noBreak) ? StringUtil.replace(text, " ", "&nbsp;") : text);
    }

    protected void writeAnchorPostfix(Device d, SCheckBox checkBox)
        throws IOException
    {
        if (checkBox.isEnabled() && !checkBox.isSelected())
            d.append("</a>\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
