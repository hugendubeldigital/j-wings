package org.wings.plaf.xhtml;

import java.io.IOException;

import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;


public class DefaultBorderCG
    implements org.wings.plaf.BorderCG
{
    public void writePrefix(Device d, SBorder b)
	throws IOException
    {
	d.append("<table><tr><td cellpadding=\"")
	    .append(b.getInsets().left)
	    .append("\">");
    }

    public void writePostfix(Device d, SBorder b)
	throws IOException
    {
	d.append("</td></tr></table>");
    }
}
