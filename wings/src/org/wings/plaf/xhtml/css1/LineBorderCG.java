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

import java.awt.Color;
import java.awt.Insets;
import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class LineBorderCG
    extends org.wings.plaf.xhtml.LineBorderCG
{
    private final static Insets none = new Insets(0,0,0,0);

    public void writePrefix(Device d, SBorder b)
        throws IOException
    {
        SLineBorder border = (SLineBorder)b;
        int thickness = border.getThickness();
        Color color   = border.getColor();
        Insets insets = b.getInsets();
        
        d.append("<div style=\"border: solid ")
            .append(thickness)
            .append("px; color: ")
            .append(Utils.toColorString(color));
        if (insets != null && !none.equals(insets))
            d.append("; padding: ")
                .append(insets.top).append(" ")
                .append(insets.right).append(" ")
                .append(insets.bottom).append(" ")
                .append(insets.left)
                .append("\">");
        else
            d.append("\">");
    }
    
    public void writePostfix(Device d, SBorder b)
        throws IOException
    {
        d.append("</div>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
