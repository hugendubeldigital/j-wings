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

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.util.*;

/**
 * RadionButton CG is more or less the CheckBoxCG. The only difference is,
 * that it is not possible to click on an already checked radio button.
 */
public class RadioButtonCG
    extends CheckBoxCG
    implements org.wings.plaf.RadioButtonCG
{
    private final static String propertyPrefix = "RadioButton";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    protected void writeAnchorPrefix(Device d, SAbstractButton button)
        throws IOException
    {
        String tooltip = button.getToolTipText();

        if (button.isEnabled() && !button.isSelected()) {
            d.print("<a href=\"");
            writeAnchorAddress(d, button);
            d.print("\"");

            if (button.getEventTarget() != null)
                d.print(" target=\"").print(button.getEventTarget()).print("\"");

            if (tooltip != null)
                d.print(" title=\"").print(tooltip).print("\"");

            d.print(">");
        }
    }

    protected void writeAnchorPostfix(Device d, SAbstractButton button)
        throws IOException
    {
        if (button.isEnabled() && !button.isSelected())
            d.print("</a>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
