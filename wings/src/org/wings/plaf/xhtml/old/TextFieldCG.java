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

package org.wings.plaf.xhtml.old;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class TextFieldCG
    extends org.wings.plaf.xhtml.TextFieldCG
{
    public void write(Device d, SComponent c)
        throws IOException
    {
        STextField textField = (STextField)c;
        SFont font = textField.getFont();
        Color foreground = textField.getForeground();

        org.wings.plaf.xhtml.Utils.writeFontPrefix(d, font, foreground);

        d.append("<input type=\"text\"");
        d.append(" size=\"").append(textField.getColumns());
        d.append("\" maxlength=\"").append(textField.getMaxColumns());
        d.append("\"");

        if (!textField.isEditable())
            d.append(" readonly=\"1\"");

        String text = textField.getText();
        if (text != null) {
            d.append(" value=\"");
            org.wings.plaf.xhtml.Utils.quote(d, text);
            d.append("\"");
        }

        if (textField.isEnabled()) {
            d.append(" name=\"");
            d.append(textField.getNamePrefix());
            d.append("\"");
        }

        d.append(" />");

        org.wings.plaf.xhtml.Utils.writeFontPostfix(d, font, foreground);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
