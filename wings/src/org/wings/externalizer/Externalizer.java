/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * a's published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.externalizer;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Externalizer Interface
 *
 * The {@link ExternalizeManager} uses a Externalizer to deliver a java object to a
 * client over a http connection. A Externalizer must be 
 * {@link ExternalizeManager.addExternalizer registered} at the
 * {@link ExternalizeManager} of the actual 
 * {@link org.wings.session.Session Session} to work seamless. 
 *
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface Externalizer
{
    /**
     * returns the file extension of the given object. Some (old) browsers use
     * this information instead of the mime type
     */
    String getExtension( Object obj );

    /**
     * returns the mime type of the given object
     */
    String getMimeType( Object obj );

    /**
     * returns the externalized length of this Object. This value is set as
     * content length in the HttpServletResponse. If it return -1 no content
     * length is set.
     */
    int getLength( Object obj );

    /**
     * returns true if the object is final, false if transient. It is used to
     * control the caching in the browser. 
     */
    boolean isFinal( Object obj );

    /**
     * writes the given object into the given stream. 
     */
    void write( Object obj, OutputStream out )
        throws IOException;

    /**
     * returns the supported classes. The {@link ExternalizeManager} chooses the
     * Externalizer (if not specified as parameter) by objects class.
     */
    Class[] getSupportedClasses();

    /**
     * returns the supported mime types. The {@link ExternalizeManager} chooses the
     * Externalizer by mime type (if specified as parameter)
     */
    String[] getSupportedMimeTypes();
    
    /**
      * Get additional http-headers.
      * Returns <tt>null</tt>, if there are no additional headers to be set.
      * @return Set of {@link java.util.Map.Entry} (key-value pairs)
      * @param obj get headers for this object
      */
    Set getHeaders( Object obj );
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
