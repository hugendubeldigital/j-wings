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
import org.wings.*;

public class TextAreaCG
    extends org.wings.plaf.AbstractCG
    implements org.wings.plaf.TextAreaCG
{
    private final static String propertyPrefix = "TextArea";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    private StringBuffer buffer = new StringBuffer();

    public void write(Device d, SComponent c)
        throws IOException
    {
        STextArea textArea = (STextArea)c;

        writePrefix(d, textArea);
        writeBody(d, textArea);
        writePostfix(d, textArea);
    }

    public void writePrefix(Device d, STextArea textArea)
        throws IOException
    {
        d.append("<textarea name=\"")
            .append(textArea.getNamePrefix())
            .append("\"");
        if ( Utils.hasSpanAttributes( textArea ) )
         {
            d.append( " style=\"" );
            Utils.writeSpanAttributes( d, textArea );
            d.append( "\"" );
         }

    }

    public void writeBody(Device d, STextArea textArea)
        throws IOException
    {
        if (!textArea.isEditable())
            d.append(" readonly=\"readonly\"");

        d.append(" cols=\"").append(textArea.getColumns());
        d.append("\" rows=\"").append(textArea.getRows()).
            append("\"");

        switch(textArea.getLineWrap()) {
        case 1:
            d.append(" wrap=\"virtual\"");
            break;
        case 2:
            d.append(" wrap=\"physical\"");
        }
        d.append(">");

        String text = textArea.getText();
        if (text != null) 
            Utils.quote(d, text);
    }

    public void writePostfix(Device d, STextArea textArea)
        throws IOException
    {
        d.append("\n</textarea>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
