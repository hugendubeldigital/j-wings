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

import java.awt.Color;
import java.io.IOException;
import javax.swing.ListModel;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class ListCG
    extends org.wings.plaf.xhtml.ListCG
{
    public void writeFormPrefix(Device d, SList list)
        throws IOException
    {
        SFont font = list.getFont();
        Color foreground = list.getForeground();
        org.wings.plaf.xhtml.old.Utils.writeFontPrefix(d, font, foreground);

        super.writeFormPrefix(d, list);
    }

    public void writeFormPostfix(Device d, SList list)
        throws IOException
    {
        super.writeFormPostfix(d, list);

        SFont font = list.getFont();
        Color foreground = list.getForeground();
        org.wings.plaf.xhtml.old.Utils.writeFontPostfix(d, font, foreground);
    }

    public void writeAnchorBody(Device d, SList list)
        throws IOException
    {
        ListModel model = list.getModel();
        int size = model.getSize();
        SListCellRenderer cellRenderer = list.getCellRenderer();

        SFont font = list.getFont();
        Color foreground = list.getForeground();

        if (model != null) {
            SCellRendererPane rendererPane = list.getCellRendererPane();

            for (int i=0; i < size; i++) {
                Object o = model.getElementAt(i);
                boolean selected = list.isSelectedIndex(i);

                d.append("<li>");
                org.wings.plaf.xhtml.old.Utils.writeFontPrefix(d, font, foreground);

                SComponent renderer
                    = cellRenderer.getListCellRendererComponent(list, o, selected, i);
                rendererPane.writeComponent(d, renderer, list);

                org.wings.plaf.xhtml.old.Utils.writeFontPostfix(d, font, foreground);
                d.append("</li>\n");
            }
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
