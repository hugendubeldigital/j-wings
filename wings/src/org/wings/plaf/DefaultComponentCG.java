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

package org.wings.plaf;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;

public class DefaultComponentCG implements ComponentCG
{
    public void installCG(SComponent c) {
    }

    public void uninstallCG(SComponent c) {
    }

    public void write(Device d, SComponent c) throws IOException {
        c.appendBorderPrefix(d);
        c.appendPrefix(d);
        c.appendBody(d);
        c.appendPostfix(d);
        c.appendBorderPostfix(d);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
