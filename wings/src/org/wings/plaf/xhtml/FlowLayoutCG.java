package org.wings.plaf.xhtml;

import java.io.IOException;
import java.util.*;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class FlowLayoutCG implements LayoutCG
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
	SFlowLayout layout = (SFlowLayout)l;
	List components = layout.getComponents();
	int orientation = layout.getOrientation();
	int alignment = layout.getAlignment();

        if (components.size() > 0) {
            switch (alignment) {
            case SConstants.RIGHT_ALIGN:
                d.append("\n<div align=\"right\">");
                break;
            case SConstants.CENTER_ALIGN:
                d.append("\n<div align=\"center\">");
                break;
            }

            int count = 0;
            for (int i=0;  i < components.size(); i++) {
                SComponent comp = (SComponent)components.get(i);
                if (comp.isVisible()) {
                    if (orientation == SConstants.VERTICAL && count > 0)
                        d.append("<br>\n");
                    ((SComponent)components.get(i)).write(d);
                    count++;
                }
            }

            switch (alignment) {
            case SConstants.RIGHT_ALIGN:
            case SConstants.CENTER_ALIGN:
                d.append("\n</div>");
                break;
            }

        }
    }
}
