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

package org.wings.plaf.xhtml.old;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class ComboBoxCG
    extends org.wings.plaf.xhtml.ComboBoxCG
{
    public void writeFormPrefix(Device d, SComboBox comboBox)
        throws IOException
    {
        SFont font = comboBox.getFont();
        Color foreground = comboBox.getForeground();
        Utils.writeFontPrefix(d, font, foreground);

        super.writeFormPrefix(d, comboBox);
    }

    protected void writeFormPostfix(Device d, SComboBox comboBox)
        throws IOException
    {
        super.writeFormPostfix(d, comboBox);

        SFont font = comboBox.getFont();
        Color foreground = comboBox.getForeground();
        Utils.writeFontPostfix(d, font, foreground);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
