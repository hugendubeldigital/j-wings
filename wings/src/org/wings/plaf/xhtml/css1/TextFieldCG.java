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

package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import java.awt.Color;

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class TextFieldCG
    extends org.wings.plaf.xhtml.TextFieldCG
{
    public void write(Device d, SComponent c)
        throws IOException
    {
        STextField textField = (STextField)c;
        Style style = textField.getStyle();

        d.append("<input type=\"text\"");
        d.append(" size=\"").append(textField.getColumns());
        d.append("\" maxlength=\"").append(textField.getMaxColumns());
        d.append("\"");
        Utils.writeStyleAttribute(d, style);

        if (!textField.isEditable())
            d.append(" readonly=\"1\"");
            
        if ( org.wings.plaf.xhtml.Utils.hasSpanAttributes( textField ) )
         {
            d.append( " style=\"" );
            org.wings.plaf.xhtml.Utils.writeSpanAttributes( d, textField );
            d.append( "\"" );
         }


        String text = textField.getText();
        if (text != null) {
            d.append(" value=\"");
            org.wings.plaf.xhtml.Utils.quote(d, text);
            d.append("\"");
        }

        if (textField.isEnabled()) {
            d.append(" name=\"");
            d.append(textField.getNamePrefix());
            d.append("\"");
        }

        d.append(">");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
