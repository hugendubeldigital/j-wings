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

public final class EmptyBorderCG
    extends org.wings.plaf.xhtml.EmptyBorderCG
{
    private final static boolean BLACK = false;
    private final static boolean WHITE = true;

    public void writePrefix(Device d, SBorder b)
	throws IOException
    {
	SEmptyBorder border = (SEmptyBorder)b;
	Insets insets = b.getInsets();

	d.print("<div style=\"padding: ")
	    .print(insets.top).print(" ")
	    .print(insets.right).print(" ")
	    .print(insets.bottom).print(" ")
	    .print(insets.left)
	    .print("\">");
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
