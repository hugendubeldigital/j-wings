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

package org.wings.externalizer;

/**
 * Object Handler Interface
 *
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public interface ObjectHandler
{
    /**
     * returns the file extension of the given object
     */
    String getExtension( Object obj );

    /**
     * returns the mime type of the given object
     */
    String getMimeType( Object obj );

    /**
     * returns true if the object is stable
     */
    boolean isStable( Object obj );

    /**
     * writes the given object into the given stream
     */
    void write( Object obj, java.io.OutputStream out )
        throws java.io.IOException;

    /**
     * returns the supported class
     */
    Class getSupportedClass();
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
