package org.wings.plaf.xhtml;

import java.io.IOException;
import java.util.*;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class CardLayoutCG implements LayoutCG
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
	SCardLayout layout = (SCardLayout)l;
        SComponent c = layout.getVisibleComponent();

        if (c != null)
            c.write(d);
    }
}
