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

public final class TextAreaCG
    extends org.wings.plaf.xhtml.TextAreaCG
{
    public void writePrefix(Device d, STextArea textArea)
        throws IOException
    {
        SFont font = textArea.getFont();
        Color foreground = textArea.getForeground();
        Utils.writeFontPrefix(d, font, foreground);

        super.writePrefix(d, textArea);
    }

    public void writePostfix(Device d, STextArea textArea)
        throws IOException
    {
        super.writePostfix(d, textArea);

        SFont font = textArea.getFont();
        Color foreground = textArea.getForeground();
        Utils.writeFontPostfix(d, font, foreground);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
