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

package org.wings.plaf.xhtml;

import java.io.IOException;
import java.util.*;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class BorderLayoutCG
    implements LayoutCG
{
    /**
     * TODO: documentation
     *
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
        throws IOException
    {
	SBorderLayout layout = (SBorderLayout)l;
	Map components = layout.getComponents();
	SContainer container = layout.getContainer();
	int border = layout.getBorder();

	SComponent north = (SComponent)components.get(SBorderLayout.NORTH);
	SComponent east = (SComponent)components.get(SBorderLayout.EAST);
	SComponent center = (SComponent)components.get(SBorderLayout.CENTER);
	SComponent west = (SComponent)components.get(SBorderLayout.WEST);
	SComponent south = (SComponent)components.get(SBorderLayout.SOUTH);
	
        int cols = 0;
        if (west != null) cols++;
        if (center != null) cols++;
        if (east != null) cols++;

        d.append("\n<table cellpadding=\"0\" cellspacing=\"0\"");
        if (border > 0)
            d.append(" border=\"").append(border).append("\"");
        if (container != null && container.getBackground() != null)
            d.append(" bgcolor=\"#").
                append(SUtil.toColorString(container.getBackground())).append("\">");
	else
	    d.append(">");

        if (north != null) {
            d.append("\n<tr><td colspan=\"").append(cols).append("\"");
            writeComponentAlignment(d, north);
            d.append(">");
            writeComponent(d, north);
            d.append("</td></tr>");
        }
        d.append("\n<tr>");

        if (west != null) {
            d.append("<td");
            writeComponentAlignment(d, west);
            d.append(">");
            writeComponent(d, west);
            d.append("</td>");
        }

        if (center != null) {
            d.append("<td");
            writeComponentAlignment(d, center);
            d.append(">");
            writeComponent(d, center);
            d.append("</td>");
        }

        if (east != null) {
            d.append("<td");
            writeComponentAlignment(d, east);
            d.append(">");
            writeComponent(d, east);
            d.append("</td>");
        }
        d.append("</tr>\n");
	
        if (south != null) {
            d.append("\n<tr><td colspan=\"").append(cols).append("\"");
            writeComponentAlignment(d, south);
            d.append(">");
            writeComponent(d, south);
            d.append("</tr>");
        }
        d.append("\n</table>");
    }

    private void writeComponent(Device d, SComponent c)
	throws IOException
    {
        c.write(d);
    }

    // TODO: move this to Utils.java
    protected String[] alignments = { "left", "right", "center", "block", "top", "bottom", "baseline" };

    protected void writeComponentAlignment(Device d, SComponent c)
        throws IOException
    {
        int horizontalAlignment = c.getHorizontalAlignment();
        int verticalAlignment   = c.getVerticalAlignment();

        if (horizontalAlignment > -1)
            d.append(" align=\"")
                .append(alignments[horizontalAlignment])
                .append("\"");
        if (verticalAlignment > -1)
            d.append(" valign=\"")
                .append(alignments[verticalAlignment])
                .append("\"");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
