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

import java.awt.Insets;
import java.io.IOException;

import javax.swing.Icon;

import org.wings.*; 
import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class DefaultBorderCG
    implements org.wings.plaf.BorderCG
{
    protected final static Insets NO_BORDER = new Insets(0,0,0,0);


    public void writePrefix(Device d, SBorder b)
	throws IOException
    {
	d.print("<div style=\"");

        writeSpanAttributes(d, b);
        
        d.print("\">");
    }

    public void writePostfix(Device d, SBorder b)
	throws IOException
    {
	d.print("</div>");
    }

    public void writeSpanAttributes( Device d, SBorder border )
        throws IOException
    {
	Insets insets = border.getInsets();

        if (insets != null && !NO_BORDER.equals(insets)) {
            d.print("padding:")
                .print(insets.top).print("px ")
                .print(insets.right).print("px ")
                .print(insets.bottom).print("px ")
                .print(insets.left).print("px;");
        }
    }

    protected void writeBorderStyle( Device d, SBorder border, String style ) 
        throws IOException 
    {
        d.print("border:")
            .print(style)
            .print(" ")
            .print(border.getThickness())
            .print("px;color:")
            .print(Utils.toColorString(border.getColor()))
            .print(";");
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */


