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

package org.wings.plaf;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;

public class DefaultLayoutCG
    implements LayoutCG
{
    public void write(Device d, SLayoutManager l) throws IOException {
	SContainer c = l.getContainer();
	for (int i=0; i < c.getComponentCount(); i++)
	    c.getComponentAt(i).write(d);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
