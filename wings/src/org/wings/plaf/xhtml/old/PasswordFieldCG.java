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

public final class PasswordFieldCG
    extends org.wings.plaf.xhtml.PasswordFieldCG
{
    private StringBuffer buffer = new StringBuffer();

    public void write(Device d, SComponent c)
        throws IOException
    {
        SPasswordField passwordField = (SPasswordField)c;
        SFont font = passwordField.getFont();
        Color foreground = passwordField.getForeground();
        //Color background = passwordField.getBackground();

        Utils.writeFontPrefix(d, font, foreground);

        d.append("<input type=\"password\"");
        d.append(" size=\"").append(passwordField.getColumns());
        d.append("\" maxlength=\"").append(passwordField.getMaxColumns());
        d.append("\"");

        if (!passwordField.isEditable())
            d.append(" readonly=\"1\"");

        String text = passwordField.getText();
        if (text != null) {
            int pos=-1, lastpos=0;
            buffer.setLength(0);
            while ((pos = text.indexOf("\"", pos+1)) != -1) {
                buffer.append(text.substring(lastpos, pos));
                buffer.append("&quot;");
                lastpos = pos+1;
            }
            if (buffer.length() > 0) {
                buffer.append(text.substring(lastpos));
                text = buffer.toString();
            }

            d.append(" value=\"").append(text).append("\"");
        }

        if (passwordField.isEnabled()) {
            d.append(" name=\"");
            d.append(passwordField.getNamePrefix());
            d.append("\"");
        }

        d.append(" />");

        Utils.writeFontPostfix(d, font, foreground);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
