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

import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.externalizer.ExternalizeManager;

public class ResetButtonCG
    extends org.wings.plaf.AbstractCG
    implements org.wings.plaf.ResetButtonCG
{
    private final static String propertyPrefix = "ResetButton";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SResetButton resetButton = (SResetButton)c;
        writeFormButton(d, resetButton);
    }

    protected void writeFormButton(Device d, SResetButton resetButton)
        throws IOException
    {
        String text = resetButton.getText();
        writeFormPrefix(d, resetButton);
        writeFormBody(d, resetButton);
        writeFormPostfix(d, resetButton);
    }

    protected void writeFormPrefix(Device d, SResetButton resetButton)
        throws IOException
    {
        d.append("<input type=\"reset\"");
    }

    protected void writeFormBody(Device d, SResetButton resetButton)
        throws IOException
    {
        String text = resetButton.getText();

        //d.append(" name=\"").append(resetButton.getNamePrefix()).append("\"");
        if (text != null)
            d.append(" value=\"").append(text).append("\"");
    }

    protected void writeFormPostfix(Device d, SResetButton resetButton)
        throws IOException
    {
        d.append(" />\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
