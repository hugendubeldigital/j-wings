package org.wings.plaf.xhtml.old;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class DivisionCG
    extends org.wings.plaf.xhtml.DivisionCG
{
    protected void writePrefix(Device d, SDivision division)
        throws IOException
    {
	d.append("\n<div>");

	SFont font = division.getFont();
	Color foreground = division.getForeground();
	Utils.writeFontPrefix(d, font, foreground);

	d.append("\n");
    }

    protected void writePostfix(Device d, SDivision division)
        throws IOException
    {
	d.append("\n");

	SFont font = division.getFont();
	Color foreground = division.getForeground();
	Utils.writeFontPostfix(d, font, foreground);

	d.append("</div>\n");
    }
}
