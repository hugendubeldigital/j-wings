package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class DesktopPaneCG
    extends org.wings.plaf.xhtml.DesktopPaneCG
{
    protected void writePrefix(Device d, SDesktopPane c)
        throws IOException
    {
	d.append("\n<span");
	Utils.writeStyleAttribute(d, c.getStyle());
	d.append(">\n");
    }

    protected void writePostfix(Device d, SDesktopPane c)
        throws IOException
    {
	d.append("</span>\n");
    }
}
