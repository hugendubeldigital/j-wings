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

public interface ComponentCG
{
    /**
     * Installs the CG. <p>
     * <b>Note</b>: Be very careful here since this method is called from
     * the SComponent constructor! Don't call any methods which rely on
     * something that will be constructed in a subconstructor later!
     */
    void installCG(SComponent c);

    /**
     * Uninstalls the CG.
     */
    void uninstallCG(SComponent c);

    /**
     * Writes the given component. <p>
     * his method should be called from the write method in SComponent or
     * a subclass.
     */
    void write(Device d, SComponent c) throws IOException;
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
