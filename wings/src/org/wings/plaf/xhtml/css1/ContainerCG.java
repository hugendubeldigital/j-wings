package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class ContainerCG
    extends org.wings.plaf.xhtml.ContainerCG
{
    protected void writePrefix(Device d, SContainer c)
        throws IOException
    {
	d.append("\n<span");
	Utils.writeStyleAttribute(d, c.getStyle());
	d.append(">");
    }

    protected void writePostfix(Device d, SContainer c)
        throws IOException
    {
	d.append("</span>\n");
    }
}
