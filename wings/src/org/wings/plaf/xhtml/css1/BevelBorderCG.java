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

	d.append("<span style=\"border-style: ")
	    .append(borderStyle);
	if (insets != null && !none.equals(insets))
	    d.append("; padding: ")
		.append(insets.top).append(" ")
		.append(insets.right).append(" ")
		.append(insets.bottom).append(" ")
		.append(insets.left)
		.append(borderStyle)
		.append("\">");
    }

    public void writePostfix(Device d, SBorder b)
	throws IOException
    {
	SBevelBorder border = (SBevelBorder)b;
	int bevelType = border.getBevelType();
	Insets insets = b.getInsets();

	if (insets != null && none.equals(insets))
	    d.append("\n</td></tr></table>");

	d.append("</td>\n");
	writeTD(d, bevelType != SBevelBorder.RAISED, 1);
	writeIMG(d);
	d.append("</td></tr>\n<tr>");
	writeTD(d, bevelType == SBevelBorder.RAISED, 1);
	d.append("</td>");
	writeTD(d, bevelType != SBevelBorder.RAISED, 2);
	writeIMG(d);
	d.append("</td></tr></table>\n");
    }

    public void writeTD(Device d, boolean color, int colspan)
	throws IOException
    {
	if (color == WHITE)
	    d.append("<td bgcolor=\"white\"");
	else
	    d.append("<td bgcolor=\"BLACK\"");
	if (colspan > 1)
	    d.append(" colspan=\"")
		.append(colspan)
		.append("\">");
	else
	    d.append(">");
    }

    public void writeIMG(Device d)
	throws IOException
    {
	d.append("<img height=\"1\" width=\"1\">");
    }
}
