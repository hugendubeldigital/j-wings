/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.plaf.xhtml;

import java.io.IOException;

import java.awt.Color;
import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*; import org.wings.border.*;

public class FileChooserCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.FileChooserCG
{
    private final static String propertyPrefix = "FileChooser";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    private StringBuffer buffer = new StringBuffer();

    public void write(Device d, SComponent c)
        throws IOException
    {
        SFileChooser fileChooser = (SFileChooser)c;

        writePrefix(d, fileChooser);
        writeBody(d, fileChooser);
        writePostfix(d, fileChooser);
    }

    public void writePrefix(Device d, SFileChooser fileChooser)
        throws IOException
    {
        d.append("<input type=\"file\"");
    }

    public void writeBody(Device d, SFileChooser fileChooser)
        throws IOException
    {
        d.append(" name=\"")
            .append(fileChooser.getNamePrefix())
            .append("\"");

        int columns = fileChooser.getColumns();
        int maxColumns = fileChooser.getMaxColumns();
        String fileNameFilter = fileChooser.getFileNameFilter();

        if (columns > 0)
            d.append(" size=\"")
                .append(columns)
                .append("\"");

        if (maxColumns > 0)
            d.append(" maxlength=\"")
                .append(maxColumns)
                .append("\"");

        if (fileNameFilter != null)
            d.append(" accept=\"")
                .append(fileNameFilter)
                .append("\"");
    }

    public void writePostfix(Device d, SFileChooser fileChooser)
        throws IOException
    {
        d.append("/>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
