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

import org.wings.Javascript;

/**
 * Externalize a Javascript file.
 * @see org.wings.Javascript
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public class JavascriptObjectHandler
    implements ObjectHandler
{
    /**
      * Get the file extension.
      * @return <code>js</code>
      */
    public String getExtension(Object obj) {
        return "js";
    }

    /**
      * The mime-type.
      * @return <code>application/x-javascript</code>
      */
    public String getMimeType(Object obj) {
        return "application/x-javascript";
    }

    /**
      * Assume external Javascript files are always stable
      * @return true
      */
    public boolean isStable(Object obj) {
        return true;
    }

    public void write(Object obj, java.io.OutputStream out)
        throws java.io.IOException
    {
        InputStream in = ((Javascript)obj).getInputStream();
        if (in == null)
            throw new java.io.IOException("Javascript.jsfile is null!");
        byte[] buffer = new byte[2000];
        while ( in.available() > 0 ) {
            int count = in.read(buffer);
            if ( count > 0 )  // read sometimes returns -1 at the end...
                out.write(buffer, 0, count);
        }
        in.close();
    }

    /**
      * Get the class supported by this handler.
      * @return <code>org.wings.Javascript.class</code>
      */
    public Class getSupportedClass() {
        return Javascript.class;
    }

    /**
      * No headers!
      * @return <code>null</code>
      */
	public java.util.Set getHeaders(Object obj) { return null; }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
