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
public class URLStyleSheet
    implements StyleSheet
{
    URL url = null;
    String name;

    /**
     * TODO: documentation
     *
     * @param url
     */
    public URLStyleSheet(URL url) {
        this.url = url;
        if ( url != null )
            name = url.toString();
    }

    /**
     * TODO: documentation
     *
     * @return
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException {
        if ( url != null )
            return url.openStream();
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
