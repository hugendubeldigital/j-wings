/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.plaf.xhtml.old;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class RadioButtonCG
    extends org.wings.plaf.xhtml.RadioButtonCG
{
    protected void writeAnchorBody(Device d, SCheckBox checkBox)
        throws IOException
    {
        String text = checkBox.getText();
        boolean noBreak = checkBox.isNoBreak();

        SFont font = checkBox.getFont();
        Color foreground = checkBox.getForeground();

        Utils.writeFontPrefix(d, font, foreground);
        if (noBreak)
            d.append("<nobr>");
        d.append((text != null) ? text : "");
        if (noBreak)
            d.append("</nobr>");
        Utils.writeFontPostfix(d, font, foreground);
    }

    protected void writeFormText(Device d, SCheckBox checkBox)
        throws IOException
    {
        SFont font = checkBox.getFont();
        Color foreground = checkBox.getForeground();

        Utils.writeFontPrefix(d, font, foreground);
        d.append(checkBox.getText());
        Utils.writeFontPostfix(d, font, foreground);
    }

    /*
    protected void writeFormIcon(Device d, SCheckBox checkBox)
        throws IOException
    {
        SFont font = checkBox.getFont();

        //Utils.writeFontPrefix(d, font);
        writeFormPrefix(d, checkBox);
        writeFormBody(d, checkBox);
        writeFormPostfix(d, checkBox);
        //Utils.writeFontPostfix(d, font);
    }
    */
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
