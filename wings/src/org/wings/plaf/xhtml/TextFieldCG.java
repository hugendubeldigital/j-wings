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

import java.awt.Color;
import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class TextFieldCG
    implements org.wings.plaf.TextFieldCG
{
    private final static String propertyPrefix = "TextField";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent component) {
        component.setStyle(component.getSession().getCGManager().
                           getStyle(getPropertyPrefix() + ".style"));
    }

    public void uninstallCG(SComponent c) {
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        STextField textField = (STextField)c;

        d.append("<input type=\"text\"");
        d.append(" size=\"").append(textField.getColumns());
        d.append("\" maxlength=\"").append(textField.getMaxColumns());
        d.append("\"");

        if (!textField.isEditable())
            d.append(" readonly=\"1\"");

        String text = textField.getText();
        if (text != null) {
            d.append(" value=\"");
            int pos=-1, lastpos=0;
            while ((pos = text.indexOf("\"", pos+1)) != -1) {
                d.append(text.substring(lastpos, pos));
                d.append("&quot;");
                lastpos = pos+1;
            }
            d.append(text.substring(lastpos));
            d.append("\"");
        }

        if (textField.isEnabled()) {
            d.append(" name=\"");
            d.append(textField.getNamePrefix());
            d.append("\"");
        }

        d.append(" />");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
