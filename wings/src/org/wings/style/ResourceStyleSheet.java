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

package org.wings.style;

import java.io.*;
import java.net.*;
import java.util.Set;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class ResourceStyleSheet
    implements StyleSheet
{
    Class baseClass = null;
    String fileName = null;

    /**
     * TODO: documentation
     *
     * @param file
     * @throws IOException
     */
    public ResourceStyleSheet(Class baseClass, String fileName) {
        this.fileName = fileName;
        this.baseClass = baseClass;
    }

    /**
     * TODO: documentation
     *
     * @return
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException {
        return baseClass.getResourceAsStream(fileName);
    }

    public boolean isStable() {
        return true;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Set styleSet() {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        return fileName;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
