/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.externalizer;

import org.wings.io.Device;

import java.io.IOException;
import java.util.Collection;

/**
 * Externalizer Interface
 * <p/>
 * The {@link ExternalizeManager} uses a Externalizer to deliver an
 * external representation of a java object to the output device (usually
 * an HTTP connection).
 * A SFrame'es external representation would be HTML, an Images content the
 * GIF-byte stream, for instance.
 * <p/>
 * <p>An Externalizer must be
 * {@link ExternalizeManager#addExternalizer(Externalizer) registered} at the
 * {@link ExternalizeManager} of the current
 * {@link org.wings.session.Session Session} to work seamlessly.
 * <p/>
 * Each Externalizer supports one or more classes it is able to externalize.
 *
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface Externalizer {
    /**
     * Returns the file extension of the given object. Some (old) browsers use
     * this information instead of the mime type. This is especially necessary
     * if delivering anything different than HTML.
     */
    String getExtension(Object obj);

    /**
     * returns the mime type of the given object.
     */
    String getMimeType(Object obj);

    /**
     * Returns the externalized length of this Object. This value is set as
     * content length in the HttpServletResponse. If it return -1 no content
     * length is set.
     */
    int getLength(Object obj);

    /**
     * Returns true if the object is final, false if transient. It is used to
     * control the caching in the browser.
     */
    boolean isFinal(Object obj);

    /**
     * Writes the given object into the given Device.
     */
    void write(Object obj, Device out)
            throws IOException;

    /**
     * Returns the supported classes. The {@link ExternalizeManager}
     * chooses the Externalizer (if not specified as parameter) by objects
     * class.
     */
    Class[] getSupportedClasses();

    /**
     * Returns the supported mime types. The {@link ExternalizeManager}
     * chooses the Externalizer by mime type (if specified as parameter)
     */
    String[] getSupportedMimeTypes();

    /**
     * Get additional http-headers.
     * Returns <tt>null</tt>, if there are no additional headers to be set.
     *
     * @param obj get headers for this object
     * @return Set of {@link java.util.Map.Entry} (key-value pairs)
     */
    Collection getHeaders(Object obj);
}


