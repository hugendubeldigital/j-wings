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


import org.wings.DynamicResource;
import org.wings.io.OutputStreamDevice;
import java.io.*;
import java.util.Set;

import org.wings.RequestURL;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DynamicResourceExternalizer
    implements Externalizer
{
    private static final Class[] SUPPORTED_CLASSES = { DynamicResource.class };

    public String getExtension(Object obj) {
        if (obj != null)
            return ((DynamicResource)obj).getExtension();
        else 
            return "";
    }

    public String getMimeType(Object obj) {
        if (obj != null)
            return ((DynamicResource)obj).getMimeType();
        else 
            return "unknown";
    }

    public int getLength(Object obj) {
        return -1;
    }

    public boolean isFinal(Object obj) {
        return true;
    }

    public String getEpoch(Object obj) {
        return ((DynamicResource)obj).getEpoch();
    }

    public void write(Object obj, OutputStream out)
        throws IOException
    {
        ((DynamicResource)obj).write(new OutputStreamDevice(out));
    }

    public Class[] getSupportedClasses() {
        return SUPPORTED_CLASSES;
    }

    public String[] getSupportedMimeTypes() {
        return null;
    }

    public Set getHeaders(Object obj) { return null; }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
