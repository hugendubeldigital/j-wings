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

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

/**
  * Write a etched border
  * @author <a href="mailto:andre.lison@crosstec.de">Andre Lison</a>
  * @see #writeBorderStyle(Device,SBorder)
  */
public final class EtchedBorderCG
	extends org.wings.plaf.xhtml.BevelBorderCG
{
    private final static boolean BLACK = false;
    private final static boolean WHITE = true;
    private final static Insets none = new Insets(0,0,0,0);

    public void writePrefix(Device d, SBorder b)
	throws IOException
    {
	SEtchedBorder border = (SEtchedBorder)b;
	int etchedType = border.getEtchedType();
	Insets insets = b.getInsets();

	d.append("<div style=\"border: ");
	writeBorderStyle( d, b );
	d.append(" 1px");
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

	/**
      * "ridge" or "groove"
      */
	protected void writeBorderStyle( Device d, SBorder b )
    	throws IOException
     {
		d.append( ( ((SEtchedBorder) b).getEtchedType() == SEtchedBorder.RAISED) ? "ridge" : "groove" );
     }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
