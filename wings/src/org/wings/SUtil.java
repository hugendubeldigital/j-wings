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

package org.wings;

import java.awt.Color;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author Andreas Gr&uuml;ner
 * @version $Revision$
 */
public final class SUtil
    implements SConstants
{
    final static char[] hexDigits = {
        '0' , '1' , '2' , '3' , '4' , '5' ,
        '6' , '7' , '8' , '9' , 'a' , 'b' ,
        'c' , 'd' , 'e' , 'f'
    };


    /**
     *
     */
    private SUtil() {
    }


    /**
     * TODO: documentation
     * @deprecated Use Utils in plaf
     */
    public static String toColorString(int rgb) {
        char[] buf = new char[6];
        int digits = 6;
        do {
            buf[--digits] = hexDigits[rgb & 15];
            rgb >>>= 4;
        }
        while (digits!=0);

        return new String(buf);
    }

    /**
     * TODO: documentation
     * @deprecated Use Utils in plaf
     */
    public static String toColorString(java.awt.Color c) {
        return toColorString(c.getRGB());
    }

    /**
     * TODO: documentation
     * @deprecated Use Utils in plaf
     */
    public static void appendTableCellAlignment(Device s, SComponent c) {
        switch ( c.getHorizontalAlignment() ) {
        case NO_ALIGN:
            break;
        case CENTER:
            s.append(" align=\"center\"");
            break;
        case LEFT:
            s.append(" align=\"left\"");
            break;
        case RIGHT:
            s.append(" align=\"right\"");
            break;
        case JUSTIFY:
            s.append(" align=\"justify\"");
            break;
        }

        switch ( c.getVerticalAlignment() ) {
        case NO_ALIGN:
        case CENTER:
            break;
        case TOP:
            s.append(" valign=\"top\"");
            break;
        case BOTTOM:
            s.append(" valign=\"bottom\"");
            break;
        case BASELINE:
            s.append(" valign=\"baseline\"");
            break;
        }
    }

    /**
     * TODO: documentation
     * @deprecated Use Utils in plaf
     */
    public static void appendTableCellColors(Device s, SComponent c) {
        /*    if ( c.getForeground()!=null )
         s.append(" COLOR=#").
         append(toColorString(c.getForeground()));*/
        if ( c.getBackground()!=null )
            s.append(" BGCOLOR=#").
                append(toColorString(c.getBackground()));
    }

    /**
     * TODO: documentation
     * @deprecated Use Utils in plaf
     */
    public static void appendTableCellSpan(Device s, SComponent c) {
    }


    /**
     * TODO: documentation
     * @deprecated Use Utils in plaf
     */
    public static void appendTableCellAttributes(Device s, SComponent c) {
        appendTableCellColors(s, c);
        appendTableCellAlignment(s,c);
        appendTableCellSpan(s,c);
    }


    /**
     * TODO: documentation
     * @deprecated Use Utils in plaf
     */
    public static void writeFontPrefix(Device d, SFont font) {
        writeFontPrefix(d, font, null);
    }


    /**
     * TODO: documentation
     * @deprecated Use Utils in plaf
     */
    public static void writeFontPrefix(Device d, SFont font, Color color) {
        String face = null;
        int style = PLAIN;
        int size = Integer.MIN_VALUE;
        if (font != null) {
            font.getFace();
            font.getStyle();
            font.getSize();
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
            d.append(" color=#").append(toColorString(color));

        d.append(">");

        if ((style & ITALIC) != 0)
            d.append("<i>");
        if ((style & BOLD) != 0)
            d.append("<b>");
    }

    /**
     * TODO: documentation
     * @deprecated Use Utils in plaf
     */
    public static void writeFontPostfix(Device d, SFont font) {
        int style = PLAIN;
        if (font != null)
            font.getStyle();

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
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
