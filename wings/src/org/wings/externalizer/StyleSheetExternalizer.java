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

package org.wings.externalizer;

import java.io.InputStream;

import org.wings.style.StyleSheet;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class StyleSheetExternalizer
    implements Externalizer
{

    private static final Class[] SUPPORTED_CLASSES = { StyleSheet.class };
    private static final String[] SUPPORTED_MIME_TYPES = { "text/css" };

    public String getExtension(Object obj) {
        return "css";
    }

    public String getMimeType(Object obj) {
        return "text/css";
    }

    public boolean isFinal(Object obj) {
        return ((StyleSheet)obj).isFinal();
    }

    public int getLength(Object obj) {
        return -1;
    }

    public void write(Object obj, java.io.OutputStream out)
        throws java.io.IOException
    {
        InputStream in = ((StyleSheet)obj).getInputStream();
        byte[] buffer = new byte[2000];
        while ( in.available() > 0 ) {
            int count = in.read(buffer);
            if ( count > 0 )  // read sometimes returns -1 at the end...
                out.write(buffer, 0, count);
        }
        in.close();
    }

    public Class[] getSupportedClasses() {
        return SUPPORTED_CLASSES;
    }

    public String[] getSupportedMimeTypes() {
        return SUPPORTED_MIME_TYPES;
    }

    public java.util.Set getHeaders(Object obj) { return null; }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */