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

import org.wings.*; import org.wings.border.*;
import org.wings.border.*;
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
    static void printOptAttr(Device d, String attr, String value)
        throws IOException
    {
        if (value != null && value.length() > 0) {
            d.print(" ");
            d.print(attr);
            d.print(" =\"");
            d.print(value);
            d.print("\"");
        }
    }

    /**
     * Prints an optional attribute. If the integer value is greater than 0,
     * the attrib is added otherwise it is left out
     */
    static void printOptAttr(Device d, String attr, int value)
        throws IOException
    {
        if (value > 0) {
            d.print(" ");
            d.print(attr);
            d.print(" =\"");
            d.print(value);
            d.print("\"");
        }
    }

    /**
     * Prints the given style (if it does exist)
     */
    static void printStyle(Device d, Style style)
        throws IOException
    {
        if (style != null)
            printOptAttr(d, "class", style.getName());
    }

    /**
     * Renders a border prefix
     */
    static void renderBorderPrefix(Device d, SBorder b)
        throws IOException
    {
        if (b != null)
            b.writePrefix(d);
    }

    /**
     * Renders a border postfix
     */
    static void renderBorderPostfix(Device d, SBorder b)
        throws IOException
    {
        if (b != null)
            b.writePostfix(d);
    }

    /**
     * Renders a container
     */
    static void renderContainer(Device d, SContainer c)
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
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
