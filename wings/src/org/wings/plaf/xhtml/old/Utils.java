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

package org.wings.plaf.xhtml.old;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.plaf.*;
import org.wings.io.Device;

/**
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

    public static void writeFontPrefix(Device d, SFont font)
        throws IOException
    {
        writeFontPrefix(d, font, null);
    }

    public static void writeFontPrefix(Device d, SFont font, Color color)
        throws IOException
    {
        if (font == null && color == null)
            return;

        String face = null;
        int style = PLAIN;
        int size = Integer.MIN_VALUE;
        if (font != null) {
            face = font.getFace();
            style = font.getStyle();
            size = font.getSize();
        }
        d.append("<font");

        if (face != null)
            d.append(" face=\"").append(face).append("\"");

        if (size > Integer.MIN_VALUE) {
            d.append(" size=");
            if (size > 0)
                d.append("+");
            d.append(size);
        }

        if (color != null)
            d.append(" color=#").append(toHexString(color));

        d.append(">");

        if ((style & ITALIC) != 0)
            d.append("<i>");
        if ((style & BOLD) != 0)
            d.append("<b>");
    }

    public static void writeFontPostfix(Device d, SFont font)
        throws IOException
    {
        writeFontPostfix(d, font, null);
    }

    public static void writeFontPostfix(Device d, SFont font, Color color)
        throws IOException
    {
        if (font == null && color == null)
            return;

        int style = PLAIN;
        if (font != null)
            style = font.getStyle();

        if ((style & BOLD) != 0)
            d.append("</b>");
        if ((style & ITALIC) != 0)
            d.append("</i>");
        d.append("</font>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
