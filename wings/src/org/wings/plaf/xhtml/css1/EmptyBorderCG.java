package org.wings.plaf.xhtml.css1;

import java.awt.Insets;
import java.io.IOException;

import org.wings.*;
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

	d.append("<span style=\"padding: ")
	    .append(insets.top).append(" ")
	    .append(insets.right).append(" ")
	    .append(insets.bottom).append(" ")
	    .append(insets.left)
	    .append("\">");
    }

    public void writePostfix(Device d, SBorder b)
	throws IOException
    {
	d.append("</span>");
    }
}
