package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class DialogCG
    extends org.wings.plaf.xhtml.DialogCG
{
    protected void writePrefix(Device d, SContainer c)
        throws IOException
    {
        SDialog dialog = (SDialog) c;
        String encodingType = dialog.getEncodingType();

        d.append("<form method=\"");
        if (dialog.getMethod())
            d.append("post");
        else
            d.append("get");
        d.append("\"");

        Utils.writeStyleAttribute(d, c.getStyle());

        if (encodingType != null)
            d.append(" enctype=\"").append(encodingType).append("\"");

        d.append(" action=\"").append(dialog.getServerAddress()).
            append("\">\n");
    }
}
