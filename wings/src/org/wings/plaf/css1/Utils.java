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

package org.wings.plaf.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.style.*;
import org.wings.io.Device;

/**
 * Utils.java
 *
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public final class Utils
{
    private Utils() {}

    /**
     * Prints an optional attribute. If the String value has a content
     * (value != null && value.length > 0), the attrib is added otherwise
     * it is left out
     */
    void printOptAttr(Device d, String attr, String value)
        throws IOException
    {
        if (value != null && value.length() > 0) {
            d.append(" ");
            d.append(attr);
            d.append(" =\"");
            d.append(value);
            d.append("\"");
        }
    }

    /**
     * Prints an optional attribute. If the integer value is greater than 0,
     * the attrib is added otherwise it is left out
     */
    void printOptAttr(Device d, String attr, int value)
        throws IOException
    {
        if (value > 0) {
            d.append(" ");
            d.append(attr);
            d.append(" =\"");
            d.append(value);
            d.append("\"");
        }
    }

    /**
     * Prints the given style (if it does exist)
     */
    void printStyle(Device d, Style style)
        throws IOException
    {
        String value = null;
        if (style != null) value = style.getID();
        printOptAttr(d, "class", value);
    }

    /**
     * Renders a border prefix
     */
    void renderBorderPrefix(Device d, org.wings.SBorder b)
        throws IOException
    {
        if (b != null)
            b.writePrefix(d);
    }

    /**
     * Renders a border postfix
     */
    void renderBorderPostfix(Device d, org.wings.SBorder b)
        throws IOException
    {
        if (b != null)
            b.writePostfix(d);
    }

    /**
     * Renders a container
     */
    void renderContainer(Device d, SContainer c)
        throws IOException
    {
        SLayoutManager layout = c.getLayout();

        if (layout != null) {
            layout.write(d);
        }
        else {
            for (int i=0; i < c.getComponentCount(); i++)
                c.getComponentAt(i).write(d);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
