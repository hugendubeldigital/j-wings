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

import org.wings.Resource;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public class ResourceExternalizer
    implements Externalizer
{
    private static final Class[] SUPPORTED_CLASSES = { Resource.class };

    public String getExtension(Object obj) {
        if ( obj!=null )
            return ((Resource)obj).getExtension();
        else 
            return "";
    }

    public String getMimeType(Object obj) {
        if ( obj!=null )
            return ((Resource)obj).getMimeType();
        else 
            return "unknown";
    }

    public int getLength(Object obj) {
        if ( obj!=null )
            return ((Resource)obj).getLength();
        return -1;
    }

    public boolean isFinal(Object obj) {
        return true;
    }

    public void write(Object obj, java.io.OutputStream out)
        throws java.io.IOException
    {
        ((Resource)obj).write(out);
    }

    public Class[] getSupportedClasses() {
        return SUPPORTED_CLASSES;
    }

    public String[] getSupportedMimeTypes() {
        return null;
    }

    public java.util.Set getHeaders(Object obj) { return null; }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
