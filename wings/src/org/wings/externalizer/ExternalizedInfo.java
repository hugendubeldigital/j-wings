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
class ExternalizedInfo
{
    private static long counter = 0;

    long          timestamp;
    boolean       stable;
    String        extFileName;
    Object        extObject;
    ObjectHandler handler;
    Session       session;

    public ExternalizedInfo(Object obj, ObjectHandler hdl, Session ses) {
        extObject   = obj;
        handler     = hdl;
        session     = ses;
        stable      = handler.isStable(extObject);
        extFileName = generateFileName() + handler.getExtension(extObject);
        touch();
    }

    /**
     * TODO: documentation
     *
     */
    public void touch() {
        if ( !stable )
            timestamp = System.currentTimeMillis();
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
     * TODO: documentation
     */
    protected static final String generateFileName() {
        // FIXME: we probably should have a random prefix per Usersession
        // if icons contain private information

        // ASSUME that caches do not cache for more than
        // one month:
        long maxUniqLifespan = 30 * 24 * 3600; // 1 Month
        long uniqPrefix = (System.currentTimeMillis() / 1000) % maxUniqLifespan;
        // ASSUME that less than 100 externalized objects are generated per
        // second:
        return Long.toString (uniqPrefix, Character.MAX_RADIX)+
            "_" + (counter++ % 100);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
