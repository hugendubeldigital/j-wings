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
        d.append("<input type=\"hidden\" name=\"").
	  append(name).append("\" value=\"").
	  append(value).append("\" />\n");
    }


    public static String escapeSpecialChars(String text) {
        StringBuffer escaped = new StringBuffer(text.length());
        for ( int i = 0; i < text.length(); i++ ) {
            char c = text.charAt(i);
            if ( c == '<' )
                escaped.append("&lt;");
            else if ( c == '>' )
                escaped.append("&gt;");
            else if ( c == '\"' )
                escaped.append("&quot;");
            else if ( c == '\n' )
                escaped.append("<br />");
            else
                escaped.append(c);
        }

        return escaped.toString();
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
