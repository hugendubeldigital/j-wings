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

import org.wings.session.Session;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ExternalizedInfo
{
    private static long counter = 0;
    
    public long          timestamp;
    public boolean       stable;
    public String        extFileName;
    public Object        extObject;
    public ObjectHandler handler;
    public Session       session;

    public ExternalizedInfo(Object obj, ObjectHandler hdl, Session ses) {
        extObject   = obj;
        handler     = hdl;
        session     = ses;
        stable      = handler.isStable(extObject);
        extFileName = generateFileName() + "." + handler.getExtension(extObject);
        timestamp = System.currentTimeMillis();
    }

    /**
     * TODO: documentation
     *
     */
    final public void touch() {
        if ( !stable )
            timestamp = System.currentTimeMillis();
    }

    final public long lastModified() {
        return timestamp;
    }

    final public boolean isTransient() {
        return !stable;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        return extFileName + "[" + timestamp + "," +
            ( stable ? "stable" : "transient" ) + "]";
    }


    /**
     * generate a file name for the externalized item. Goals: 
     * <ul>
     *   <li> as short as possible to make generated pages with many icons 
     *        not too big. 
     *   <li> Make the filename uniq within the typical cache timeframe
     * </ul>
     */
    protected static final String generateFileName() {
        // FIXME: we probably should have a random prefix per Usersession
        // if icons contain private information

        // ASSUME that caches do not cache for more than
        // one month:
        long maxUniqLifespan = 30 * 24 * 3600; // 1 Month
        long uniqPrefix = (System.currentTimeMillis() / 1000) % maxUniqLifespan;
        // ASSUME that less than 1000 externalized objects are generated per
        // second:
        return Long.toString (uniqPrefix, Character.MAX_RADIX)+
            "_" + (counter++ % 1000);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
