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
import javax.swing.ComboBoxModel;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class ComboBoxCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.ComboBoxCG
{
    private final static String propertyPrefix = "ComboBox";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SComboBox comboBox = (SComboBox)c;
        writeFormPrefix(d, comboBox);
        writeFormBody(d, comboBox);
        writeFormPostfix(d, comboBox);
    }

    public void writeFormPrefix(Device d, SComboBox comboBox)
        throws IOException
    {
        d.append("<select name=\"");
        d.append(comboBox.getNamePrefix());
        d.append("\" size=\"1\">\n");
    }

    public void writeFormBody(Device d, SComboBox comboBox)
        throws IOException
    {
        ComboBoxModel model = comboBox.getModel();
        int size = model.getSize();

        if (model != null) {
            SListCellRenderer cellRenderer = comboBox.getRenderer();
            SCellRendererPane rendererPane = comboBox.getCellRendererPane();
            int selectedIndex = comboBox.getSelectedIndex();

            for (int i=0; i < size; i++) {
                Object o = model.getElementAt(i);

                d.append("<option value=\"").append(i).append("\"");
                if (i == selectedIndex)
                    d.append(" selected=\"selected\">");
                else
                    d.append(">");

                SComponent renderer
                    = cellRenderer.getListCellRendererComponent(comboBox, o, false, i);
                //rendererPane.writeComponent(d, renderer, comboBox);

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

    protected void writeFormPostfix(Device d, SComboBox comboBox)
        throws IOException
    {
        d.append("</select>\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
