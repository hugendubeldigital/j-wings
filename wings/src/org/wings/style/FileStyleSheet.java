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
public class FileStyleSheet
    implements StyleSheet
{
    File file = null;
    String name;

    /**
     * TODO: documentation
     *
     * @param file
     * @throws IOException
     */
    public FileStyleSheet(File file) throws IOException {
        this.file = file;
        if ( file != null ) {
            if ( !file.exists() || !file.canRead() )
                throw new IOException(file.getName() + " is not readable");
            name = file.getName();
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException {
        if ( file != null )
            return new FileInputStream(file);
        return null;
    }

    public boolean isFinal() {
        return false;
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
        return name;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
