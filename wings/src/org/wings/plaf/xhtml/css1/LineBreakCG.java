package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class LineBreakCG
    extends org.wings.plaf.xhtml.LineBreakCG
{
    protected void writeAttributes(Device d, SLineBreak lineBreak)
        throws IOException
    {
        Utils.writeStyleAttribute(d, lineBreak.getStyle());
        super.writeAttributes(d, lineBreak);
    }
}
