package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class HorizontalRuleCG
    extends org.wings.plaf.xhtml.HorizontalRuleCG
{
    protected void writeAttributes(Device d, SHorizontalRule hrule)
        throws IOException
    {
        Utils.writeStyleAttribute(d, hrule.getStyle());
        super.writeAttributes(d, hrule);
    }
}
