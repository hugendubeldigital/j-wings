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

import org.wings.*;
import org.wings.style.*;
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
        'c' , 'd' , 'e' , 'f'};


    private Utils() {
    }

    public static void writeBorderPrefix(Device d, SBorder border)
        throws IOException
    {
        if (border != null)
            border.writePrefix(d);
    }

    public static void writeBorderPostfix(Device d, SBorder border)
        throws IOException
    {
        if (border != null)
            border.writePostfix(d);
    }

    public static void writeContainerContents(Device d, SContainer c)
        throws IOException
    {
        SLayoutManager layout = c.getLayout();

        if (layout != null) {
          layout.write(d);
        }
        else {
            for (int i=0; i < c.getComponentCount(); i++)
              c.getComponentAt(i).write(d);
        }
    }

    public static void writeHiddenComponent(Device d, String name, String value)
        throws IOException
    {
        d.append("<input type=\"hidden\" name=\"")
            .append(name).append("\" value=\"")
            .append(value).append("\" />"); 
    }


    public static String escapeSpecialChars(String text) {
        StringBuffer escaped = new StringBuffer(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
            case '&' : escaped.append("&amp;"); break;
            case '<' : escaped.append("&lt;"); break;
            case '>' : escaped.append("&gt;"); break;
            case '"' : escaped.append("&quot;"); break;
            case '\n': escaped.append("<br />"); break; // HTML formatting
            default:   escaped.append(c);
            }
        }
        return escaped.toString();
    }

    /**
     * writes an {X|HT}ML quoted string according to RFC 1866.
     * '"', '<', '>', '&'  become '&quot;', '&lt;', '&gt;', '&amp;'
     */
    // not optimized yet.
    public static void quote(Device d, String s) throws IOException {
	int len = s.length();
	char c;
	for (int pos = 0; pos < len; ++pos) {
	    switch ((c = s.charAt(pos))) {
	    case '&': d.print("&amp;"); break;
	    case '"': d.print("&quot;");break;
	    case '<': d.print("&lt;");  break;
	    case '>': d.print("&gt;");  break;
	    default: d.print(c);
	    }
	}
    }

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

    public static String toColorString(java.awt.Color c) {
        return toColorString(c.getRGB());
    }

    public static void appendTableCellAlignment(Device s, SComponent c) {
        switch (c.getHorizontalAlignment()) {
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
    
        switch (c.getVerticalAlignment()) {
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

    public static void appendTableCellColors(Device s, SComponent c) {
        // if (c.getForeground()!=null)
        //     s.append(" COLOR=#").
        //         append(toColorString(c.getForeground()));

        if (c.getBackground()!=null)
            s.append(" bgcolor=#").
                append(toColorString(c.getBackground()));
    }

    public static void appendTableCellSpan(Device s, SComponent c) {
    }


    public static void appendTableCellAttributes(Device s, SComponent c) {
        appendTableCellColors(s, c);
        appendTableCellAlignment(s,c);
        appendTableCellSpan(s,c);
    }

    public static void appendIcon(Device d, SIcon icon, String align) {
        if ( icon==null )
            return;

        d.append("<img src=\"").
            append(icon.getURL()).
            append("\"");
        if (align != null)
            d.append(" valign=\"").append(align).append("\"");
        if ( icon.getIconWidth() > 0)
            d.append(" width=\"").append(icon.getIconWidth()).append("\"");
        if ( icon.getIconHeight() > 0)
            d.append(" height=\"").append(icon.getIconHeight()).append("\"");
        d.append(" border=\"0\">");

    }

    public static void appendBlindIcon(Device d, SIcon icon, int height, int width) {
        d.append("<img src=\"").
            append(icon.getURL()).
            append("\"").
            append(" width=\"").append(width).append("\"").
            append(" height=\"").append(height).append("\"").
            append(" border=\"0\">");

    }
    
    
    /**
      * Test, if either background-, foreground color, font or border is set.
      * @return false, if no attribute was set, true otherwise.
      */
    public static boolean hasSpanAttributes(SComponent component)
     {
         
         return component != null && 
             (component.getBackground() != null || 
               component.getForeground() != null ||
               component.getFont() != null ||
               component.getBorder() != null ||
               component.getPreferredSize() != null)
             ;
     }
    
    /**
     * Write all span-attributes except the <i>class</i>.
     * @return null, if not attribute was set, otherwise the attributes 
     *         in css syntax otherwise.
     */
    public static void writeSpanAttributes(Device d, SComponent component)
    	throws IOException
     {
        if (!hasSpanAttributes(component))
            return;
            
        java.awt.Color bgcolor = component.getBackground();
        java.awt.Color fgcolor = component.getForeground();
        SFont font = component.getFont();
        SBorder border = component.getBorder();
        SDimension dim = component.getPreferredSize();
        
        if (bgcolor != null) d.append("background-color:#").append(toColorString(bgcolor)).append(";");
        if (fgcolor != null) d.append("font-color:#").append(toColorString(fgcolor)).append(";");
        if (font != null) {
            int style = font.getStyle();
            d.append("font-size:").append(font.getSize()).append("pt;");
            d.append("font-style:").append((style & java.awt.Font.ITALIC) > 0 ?"italic;":"normal;");
            d.append("font-weight:").append((style & java.awt.Font.BOLD) > 0 ?"bold;":"normal;");
            d.append("font-family:").append(font.getFace()).append(";");
        }
        
        if (border != null)
            border.writeSpanAttributes(d);

        if (dim != null) {
            if (dim.width != null) d.append("width:").append(dim.width).append(";");
            if (dim.height != null) d.append("height:").append(dim.height).append(";");
        }
     }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
