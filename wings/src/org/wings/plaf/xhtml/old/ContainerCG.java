package org.wings.plaf.xhtml.old;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class ContainerCG
    extends org.wings.plaf.xhtml.ContainerCG
{
    protected void writePrefix(Device d, SContainer container)
        throws IOException
    {
	SFont font = container.getFont();
	Color foreground = container.getForeground();
	Utils.writeFontPrefix(d, font, foreground);
    }

    protected void writePostfix(Device d, SContainer container)
        throws IOException
    {
	SFont font = container.getFont();
	Color foreground = container.getForeground();
	Utils.writeFontPostfix(d, font, foreground);
    }
}
