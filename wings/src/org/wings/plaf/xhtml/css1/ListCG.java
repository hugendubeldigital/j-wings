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

import java.awt.Color;
import java.io.IOException;
import javax.swing.ListModel;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class ListCG
    extends org.wings.plaf.xhtml.ListCG
{
    public void writeFormPrefix(Device d, SList list)
        throws IOException
    {
        int visibleRows = list.getVisibleRowCount();
        int selectionMode = list.getSelectionMode();

        d.append("<select name=\"");
        d.append(list.getNamePrefix());
        d.append("\"");

        d.append(" size=\"").append(visibleRows);
        d.append("\"");

        if (selectionMode == SConstants.MULTIPLE_SELECTION)
            d.append(" multiple=\"multiple\"");

        Utils.writeStyleAttribute(d, list.getStyle());

        d.append(">\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
