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

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class BevelBorderCG
    extends org.wings.plaf.xhtml.BevelBorderCG
{
    private final static boolean BLACK = false;
    private final static boolean WHITE = true;
    private final static Insets none = new Insets(0,0,0,0);

    public void writePrefix(Device d, SBorder b)
	throws IOException
    {
	SBevelBorder border = (SBevelBorder)b;
	int bevelType = border.getBevelType();
	Insets insets = b.getInsets();

	String borderStyle = (bevelType == SBevelBorder.RAISED) ? "outset" : "inset";

	d.print("<div style=\"border: ")
	    .print(borderStyle)
	    .print(" 1px");
	if (insets != null && !none.equals(insets))
	    d.print("; padding: ")
		.print(insets.top).print(" ")
		.print(insets.right).print(" ")
		.print(insets.bottom).print(" ")
		.print(insets.left)
		.print("\">");
	else
	    d.print("\">");
    }

    public void writePostfix(Device d, SBorder b)
	throws IOException
    {
	d.print("</div>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
