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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

import org.wings.util.Timer;
import org.wings.session.Session;
import org.wings.session.SessionManager;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ExternalizeManager
    implements ActionListener
{
    /**
     * TODO: documentation
     */
    protected final Map handlerByClass = new HashMap();
    protected final Map handlerByMimeType = new HashMap();

    private static ExternalizeManager sharedInstance = null;

    /**
     * TODO: documentation
     */
    protected Externalizer externalizer = null;

    /**
     * TODO: documentation
     */
    protected static long DESTROYER_SLEEP = 5 * 60 * 1000; // 5 min


    /**
     * TODO: documentation
     *
     */
    public ExternalizeManager() {
        Timer sessionDestroyer = new Timer(DESTROYER_SLEEP, this);
        sessionDestroyer.start();
    }


    /**
     * Sets the externalizer. Should be only called once.
     * If already an externalizer was set, the old externalizer
     * is told cleanAll() and the new one will then be used.
     */
    public void setExternalizer( Externalizer ext ) {
        if ( externalizer != null )
            externalizer.cleanAll();
        externalizer = ext;
    }

    /**
     * TODO: documentation
     *
     */
    protected void finalize() {
        externalizer.cleanAll();
    }

    /**
     * TODO: documentation
     *
     * @return
     * @throws java.io.IOException
     */
    public String externalize( Object obj )
        throws java.io.IOException
    {
        String name = null;
        ObjectHandler handler = getObjectHandler( obj.getClass() );
        if ( obj != null && handler != null ) {
            Session session = SessionManager.getSession();
            name = externalizer.externalize(obj, handler, session);
        }
        return name;
    }

    public String externalize(Object obj, String mimeType)
        throws java.io.IOException
    {
        String name = null;
        ObjectHandler handler = getObjectHandler(mimeType);
        if (obj != null && handler != null) {
            Session session = SessionManager.getSession();
            name = externalizer.externalize(obj, handler, session);
        }
        return name;
    }

    /**
     * Adds an object handler. If an object handler is already
     * registered for one class, it will be replaced.
     */
    public void addObjectHandler(ObjectHandler handler)
    {
        if ( handler != null ) {
            Class c = handler.getSupportedClass();
            if ( c != null )
                handlerByClass.put( c, handler );

            String mimeType = handler.getMimeType(null);
            if (mimeType != null)
                handlerByMimeType.put(mimeType, handler);
        }
    }

    public void addObjectHandler(ObjectHandler handler, String mimeType)
    {
        if (handler != null && mimeType != null)
	    handlerByMimeType.put(mimeType, handler);
    }

    /**
     * returns an object handler for a class
     *
     * @return
     */
    public ObjectHandler getObjectHandler(Class c)
    {
        ObjectHandler handler = null;
        if ( c != null ) {
            handler = (ObjectHandler) handlerByClass.get(c);
            if ( handler == null )
                handler = getObjectHandler( c.getSuperclass() );
        }
        return handler;
    }

    /**
     * returns an object handler for a mime type
     */
    public ObjectHandler getObjectHandler(String mimeType)
    {
        ObjectHandler handler = null;
        if (mimeType != null && mimeType.length() > 0) {
            handler = (ObjectHandler)handlerByMimeType.get(mimeType);
            if (handler == null)
                handler = getObjectHandler(mimeType.substring(0, mimeType.indexOf('/')));
        }
        return handler;
    }

    /**
     * this is for the session destroyer
     */
    public void actionPerformed( ActionEvent evt ) {
        externalizer.clean();
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
