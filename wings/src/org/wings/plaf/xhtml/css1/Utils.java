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
import org.wings.style.*;
import org.wings.io.Device;

/**
 * Utils.java
 *
 *
 * Created: Thu Oct 28 16:23:53 1999
 *
 * @author Holger Engels
 * @version $Revision$
 */
public final class Utils implements SConstants
{
    final static char[] hexDigits = {
        '0' , '1' , '2' , '3' , '4' , '5' ,
        '6' , '7' , '8' , '9' , 'a' , 'b' ,
        'c' , 'd' , 'e' , 'f'
    };

    private Utils() {
    }

    public static String toHexString(int rgb) {
        char[] buf = new char[6];
        int digits = 6;
        do {
            buf[--digits] = hexDigits[rgb & 15];
            rgb >>>= 4;
        } while (digits!=0);

        return new String(buf);
    }

    public static String toHexString(java.awt.Color c) {
        return toHexString(c.getRGB());
    }

    public static void writeStyleAttribute(Device d, String prefix, Style style)
        throws IOException
    {
        if (style == null)
            return;
        String id = style.getID();
        if (id != null)
            writeStyleAttribute(d, prefix + id);
    }

    public static void writeStyleAttribute(Device d, String prefix, Style style, String postfix)
        throws IOException
    {
        if (style == null)
            return;
        String id = style.getID();
        if (id != null)
            writeStyleAttribute(d, prefix + id + postfix);
    }

    public static void writeStyleAttribute(Device d, Style style, String postfix)
        throws IOException
    {
        if (style == null)
            return;
        String id = style.getID();
        if (id != null)
            writeStyleAttribute(d, id + postfix);
    }

    public static void writeStyleAttribute(Device d, Style style)
        throws IOException
    {
        if (style == null)
            return;
        writeStyleAttribute(d, style.getID());
    }

    public static void writeStyleAttribute(Device d, String id)
        throws IOException
    {
        if (id == null)
            return;

        d.append(" class=\"");
        d.append(id);
        d.append("\"");
    }

    public static void writeSpanWithStyleAttributePrefix(Device d, Style style)
        throws IOException
    {
        if (style == null)
            return;

        d.append("<span");
        Utils.writeStyleAttribute(d, style);
        d.append(">");
    }

    public static void writeSpanWithStyleAttributePostfix(Device d, Style style)
        throws IOException
    {
        if (style == null)
            return;

        d.append("</span>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
