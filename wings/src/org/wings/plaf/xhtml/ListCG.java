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

import java.awt.Color;
import java.io.IOException;
import javax.swing.ListModel;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class ListCG
    extends org.wings.plaf.AbstractCG
    implements org.wings.plaf.ListCG
{
    private final static String propertyPrefix = "List";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SList list = (SList)c;

        if (list.getShowAsFormComponent())
            writeFormList(d, list);
        else
            writeAnchorList(d, list);
    }

    protected void writeFormList(Device d, SList list)
        throws IOException
    {
        writeFormPrefix(d, list);
        writeFormBody(d, list);
        writeFormPostfix(d, list);
    }

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

        //if (submitOnChange)
        //    d.append(" onChange=\"submit()\"");

        d.append(">\n");
    }

    public void writeFormBody(Device d, SList list)
        throws IOException
    {
        ListModel model = list.getModel();
        int size = model.getSize();

        if (model != null) {
            SListCellRenderer cellRenderer = list.getCellRenderer();

            for (int i=0; i < size; i++) {
                Object o = model.getElementAt(i);
                boolean selected = list.isSelectedIndex(i);

                d.append("<option value=\"").append(i).append("\"");
                if (selected)
                    d.append(" selected=\"selected\">");
                else
                    d.append(">");

                SComponent renderer
                    = cellRenderer.getListCellRendererComponent(list, o, false, i);
                
                /*
                 * Hack: remove all tags, because in form selections, this
                 * does look ugly.
                 */
                StringBufferDevice device = getStringBufferDevice();
                renderer.write(device);
                char[] chars = device.toString().toCharArray();
                int pos = 0;
                for (int c=0; c < chars.length; c++) {
                    switch (chars[c]) {
                    case '<':
                        if (c > pos)
                            d.print(chars, pos, c - 1);
                        break;
                    case '>':
                        pos = c+1;
                    }
                }
                if (chars.length > pos)
                    d.print(chars, pos, chars.length - 1);

                d.append("</option>\n");
            }
        }
    }

    private StringBufferDevice stringBufferDevice = null;
    protected StringBufferDevice getStringBufferDevice() {
        if (stringBufferDevice == null)
            stringBufferDevice = new StringBufferDevice();
        stringBufferDevice.reset();
        return stringBufferDevice;
    }

    protected void writeFormPostfix(Device d, SList list)
        throws IOException
    {
        d.append("</select>\n");
        Utils.writeHiddenComponent(d, list.getNamePrefix(), "-1");
    }

    protected void writeAnchorList(Device d, SList list)
        throws IOException
    {
        writeAnchorPrefix(d, list);
        writeAnchorBody(d, list);
        writeAnchorPostfix(d, list);
    }

    public void writeAnchorPrefix(Device d, SList list)
        throws IOException
    {
        String orderType = list.getOrderType();
        int start = list.getStart();

        d.append("<");
        writeType(d, list);
        if (orderType != null)
            d.append(" type=\"").append(orderType).append("\"");
        if (start > 0)
            d.append(" start=\"").append(start).append("\"");
        d.append(">\n");
    }

    public void writeAnchorBody(Device d, SList list)
        throws IOException
    {
        ListModel model = list.getModel();
        int size = model.getSize();
        SListCellRenderer cellRenderer = list.getCellRenderer();

        if (model != null) {
            SCellRendererPane rendererPane = list.getCellRendererPane();

            for (int i=0; i < size; i++) {
                Object o = model.getElementAt(i);
                boolean selected = list.isSelectedIndex(i);

                d.append("<li>");
                SComponent renderer
                    = cellRenderer.getListCellRendererComponent(list, o, selected, i);
                rendererPane.writeComponent(d, renderer, list);
                d.append("</li>\n");
            }
        }
    }

    public void writeAnchorPostfix(Device d, SList list)
        throws IOException
    {
        d.append("</");
        writeType(d, list);
        d.append(">\n");
    }

    protected void writeType(Device d, SList list) {
        d.append(list.getType());
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
