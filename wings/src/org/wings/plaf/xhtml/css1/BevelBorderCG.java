package org.wings.plaf.xhtml.css1;

import java.awt.Insets;
import java.io.IOException;

import org.wings.*;
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

	d.append("<div style=\"border: ")
	    .append(borderStyle)
	    .append(" 1px");
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
