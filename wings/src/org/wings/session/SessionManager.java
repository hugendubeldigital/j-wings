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

package org.wings.session;

import java.util.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SessionManager
{
    private static final Map sessions = new WeakHashMap();

    /**
     * Get the Session that is currently associated with this Thread.
     *
     * @return the Session
     */
    public static Session getSession() 
    {
    	Session s = (Session)sessions.get(Thread.currentThread());
        return s;
    }

    /**
     * Associate the Session with the current Thread.
     * This method must only be called by the SessionServlet before
     * a request is going to be dispatched.
     *
     * @param session the Session
     */
    public static synchronized void setSession(Session session) {
        synchronized (sessions) {
            sessions.put(Thread.currentThread(), session);
        }
    }

    /**
     * TODO: documentation
     */
    public static final Session removeSession() {
        synchronized (sessions) {
            return (Session)sessions.remove(Thread.currentThread());
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
