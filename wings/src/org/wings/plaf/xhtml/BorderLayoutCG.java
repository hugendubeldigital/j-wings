package org.wings.plaf.xhtml;

import java.io.IOException;
import java.util.*;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class BorderLayoutCG implements LayoutCG
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

        d.append("\n<table");
        if (border > 0)
            d.append(" border=\"").append(border).append("\"");
        if (container != null && container.getBackground() != null)
            d.append(" bgcolor=\"#").
                append(SUtil.toColorString(container.getBackground())).append("\">");
	else
	    d.append(">");

        if (north != null) {
            d.append("\n<tr> <td colspan=\"").append(cols).append("\">");
            writeComponent(d, north);
            d.append("</td></tr>");
        }
        d.append("\n<tr>");

        if (west != null) {
            d.append("<td>");
            writeComponent(d, west);
            d.append("</td>");
        }

        if (center != null) {
            d.append("<td>");
            writeComponent(d, center);
            d.append("</td>");
        }

        if (east != null) {
            d.append("<td>");
            writeComponent(d, east);
            d.append("</td>");
        }
        d.append("</tr>\n");
	
        if (south != null) {
            d.append("\n<tr><td colspan=\"").append(cols).append("\">");
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
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
