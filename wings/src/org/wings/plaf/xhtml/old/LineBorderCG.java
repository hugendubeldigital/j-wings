package org.wings.plaf.xhtml.old;

import java.awt.Color;
import java.awt.Insets;
import java.io.IOException;

import org.wings.*;
import org.wings.externalizer.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.session.*;
import org.wings.style.Style;

public final class LineBorderCG
    extends org.wings.plaf.xhtml.LineBorderCG
{
    private final static boolean BLACK = false;
    private final static boolean WHITE = true;
    private final static Insets none = new Insets(0,0,0,0);

    public void writePrefix(Device d, SBorder b)
	throws IOException
    {
	SLineBorder border = (SLineBorder)b;
	int thickness = border.getThickness();
	Color lineColor = border.getLineColor();
	Insets insets = b.getInsets();

	d.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n<tr>");
	writeTD(d, lineColor, 3);
	writeIMG(d, 1, thickness);
	d.append("</td></tr>\n<tr>");
	writeTD(d, lineColor, 1);
	writeIMG(d, thickness, 1);
	d.append("</td>\n<td>");
	if (insets != null && none.equals(insets))
	    d.append("<table border=\"0\" cellpadding=\"")
		.append(insets.left)
		.append("\"><tr><td>\n");
    }

    public void writePostfix(Device d, SBorder b)
	throws IOException
    {
	SLineBorder border = (SLineBorder)b;
	int thickness = border.getThickness();
	Color lineColor = border.getLineColor();
	Insets insets = b.getInsets();

	if (insets != null && none.equals(insets))
	    d.append("\n</td></tr></table>");

	d.append("</td>\n");
	writeTD(d, lineColor, 1);
	writeIMG(d, thickness, 1);
	d.append("</td></tr>\n<tr>");
	writeTD(d, lineColor, 3);
	writeIMG(d, 1, thickness);
	d.append("</td></tr></table>\n");
    }

    public void writeTD(Device d, Color color, int colspan)
	throws IOException
    {
	d.append("<td bgcolor=\"")
	    .append(Utils.toHexString(color))
	    .append("\"");

	if (colspan > 1)
	    d.append(" colspan=\"")
		.append(colspan)
		.append("\">");
	else
	    d.append(">");
    }

    public void writeIMG(Device d, int width, int height)
	throws IOException
    {
	d.append("<img height=\"")
	    .append(height)
	    .append("\" width=\"")
	    .append(width)
	    .append("\">");
    }
}
