package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class DivisionCG
    extends org.wings.plaf.xhtml.DivisionCG
{
    protected void writePrefix(Device d, SDivision c)
        throws IOException
    {
	d.append("\n<div");
	Utils.writeStyleAttribute(d, c.getStyle());
	d.append(">\n");
    }
}
