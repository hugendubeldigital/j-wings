package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class TabbedPaneCG
    extends org.wings.plaf.xhtml.TabbedPaneCG
{
    protected void writePrefix(Device d, SContainer c)
        throws IOException
    {
        d.append("\n<span");
        Utils.writeStyleAttribute(d, c.getStyle());
        d.append(">\n");
    }

    protected void writePostfix(Device d, SContainer c)
        throws IOException
    {
        d.append("</span>\n");
    }
}
