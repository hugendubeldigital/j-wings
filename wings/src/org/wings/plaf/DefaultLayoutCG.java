package org.wings.plaf;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;

public class DefaultLayoutCG implements LayoutCG
{
    public void write(Device d, SLayoutManager l) throws IOException {
	SContainer c = l.getContainer();
	for (int i=0; i < c.getComponentCount(); i++)
	    c.getComponentAt(i).write(d);
    }
}
