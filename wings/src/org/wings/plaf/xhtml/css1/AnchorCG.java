package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class AnchorCG
    extends org.wings.plaf.xhtml.AnchorCG
{
    protected void writePrefix(Device d, SAnchor anchor)
        throws IOException
    {
        String name = anchor.getName();
        String reference = anchor.getReference();

        d.append("<a");

        Utils.writeStyleAttribute(d, anchor.getStyle());

        if (name != null)
            d.append(" name=\"").append(name).append("\"");

        if (reference != null)
            d.append(" href=\"").append(reference).append("\"");

        d.append(">");
    }
}
