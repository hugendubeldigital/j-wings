package org.wings.plaf.xhtml.old;

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

	d.append("<table border=\"0\" cellpadding=\"")
	    .append(insets.left)
	    .append("\"><tr><td>\n");
    }

    public void writePostfix(Device d, SBorder b)
	throws IOException
    {
	d.append("\n</td></tr></table>");
    }
}
