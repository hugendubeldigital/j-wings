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
public final class Utils
    implements SConstants
{
    final static char[] hexDigits = {
        '0' , '1' , '2' , '3' , '4' , '5' ,
        '6' , '7' , '8' , '9' , 'a' , 'b' ,
        'c' , 'd' , 'e' , 'f'
    };

    private Utils() {
    }

    public static String toColorString(int rgb) {
        char[] buf = new char[6];
        int digits = 6;
        do {
            buf[--digits] = hexDigits[rgb & 15];
            rgb >>>= 4;
        } while (digits!=0);

        return new String(buf);
    }

    public static String toColorString(java.awt.Color c) {
        return toColorString(c.getRGB());
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
        d.append("\"; ");
    }

	/**
      * Write component class and background-color.
      * @param d the device to write to
      * @param component the component to get style and bg-color from
      */
    public static void writeSpanWithStyleAttributePrefix(Device d, SComponent component )
        throws IOException
    {
    	Style style = component.getStyle();
        boolean hasAttr = org.wings.plaf.xhtml.Utils.hasSpanAttributes( component );
        if ( style == null && ! hasAttr )
            return;

        d.append("<span " );
        if ( hasAttr )
         {
         	d.append( "style=\"");
			org.wings.plaf.xhtml.Utils.writeSpanAttributes( d, component );
        	d.append( "\" " );
		 }
        writeStyleAttribute( d, style );
        d.append(">");
    }


	/**
      * Write ending <tt>&ls;/span&gt;</tt>-attribute, if
      * component has either style or background-color defined.
      * @param d the device to write to
      * @param component the component to get style and bg-color from
      */
    public static void writeSpanWithStyleAttributePostfix(Device d, SComponent component )
        throws IOException
    {
        if ( component.getStyle() == null && component.getBackground() == null )
            return;

        d.append("</span>");
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

    public static void writeDivWithStyleAttributePrefix(Device d, Style style)
        throws IOException
    {
        if (style == null)
            return;
 
        d.append("<div");
        Utils.writeStyleAttribute(d, style);
        d.append(">");
    }
 
    public static void writeDivWithStyleAttributePostfix(Device d, Style style)
        throws IOException
    {
        if (style == null)
            return;
 
        d.append("</div>");
    }                                                                                                             
    static void writeHiddenComponent(Device d, String name, String value)
        throws IOException
    {
        d.append("<input type=\"hidden\" name=\"").
	  append(name).append("\" value=\"").
	  append(value).append("\" />\n");
    }
    
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
