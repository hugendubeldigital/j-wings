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

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

import java.awt.Image;
import javax.swing.ImageIcon;

import org.wings.ResourceImageIcon;
import org.wings.session.Session;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public abstract class AbstractExternalizer
    implements Externalizer
{
    /**
     * TODO: documentation
     */
    public static final boolean DEBUG = true;

    /**
     * TODO: documentation
     */
    protected static final long EXTERNALIZE_TIMEOUT = 15 * 60 * 1000; // 15 min

    /**
     * Take care of browsers which have a slow link: they probably don't get
     * the file before it is removed. So add a grace period here
     */
    protected static long SLOW_LINK_GRACE_PERIOD = 2 * 60 * 1000; // 2 Min

    /**
     * TODO: documentation
     */
    protected static final Map externalized =
        Collections.synchronizedMap( new HashMap() );

    /**
     * TODO: documentation
     */
    protected static final ArrayList deleteQueue = new ArrayList(2);

    /**
     * returns the full url to an externalized object
     */
    protected abstract String getExternalizedURL(ExternalizedInfo info);

    /**
     * externalizes an object
     */
    protected abstract void doExternalize(ExternalizedInfo info)
        throws java.io.IOException;

    /**
     * removes an externalized object
     */
    protected abstract void doDelete(ExternalizedInfo info);


    public String externalize(Object obj,
                              ObjectHandler handler,
                              Session session)
        throws java.io.IOException
    {
        if ( obj == null || handler == null )
            return null;

        ExternalizedInfo info = getInfo(obj);
        if ( info == null ) {
            info = new ExternalizedInfo(obj, handler, session);
            doExternalize(info);
            putInfo(obj, info);
        }
        else
            info.touch();

        return getExternalizedURL(info);
    }

    /**
     * TODO: documentation
     *
     */
    public void clean() {
        for ( Iterator en = getExternalizedIterator(); en.hasNext(); ) {
            Object key = en.next();
            ExternalizedInfo info = getInfo(key);

            if ( !info.stable &&
                 info.timestamp + EXTERNALIZE_TIMEOUT <
                 System.currentTimeMillis() ) {
                deleteQueue.add(info);
                en.remove();
            }
        }

        processDeleteQueue(false);
        System.gc();
    }

    /**
     * TODO: documentation
     *
     */
    public void cleanAll() {
        for ( Iterator en = getExternalizedIterator(); en.hasNext(); ) {
            Object key = en.next();
            ExternalizedInfo info = getInfo(key);
            deleteQueue.add(info);
            en.remove();
        }

        processDeleteQueue(true);
        System.gc();
    }

    /**
     * Processes the delete queue. To avoid problems with slow links, it
     * only deletes files which are already longer than SLOW_LINK_GRACE_PERIOD
     * in the delete queue. To delete the files, it calls doDelete which must
     * be implemented by the realization of an externalizer.
     *
     * @param forceDelete ignores the grace time and deletes all files in the
     *        delete queue.
     */
    public void processDeleteQueue(boolean forceDelete) {
        // count backward to avoid getting in trouble with
        // removed elements from the vector..
        for ( int i = deleteQueue.size() - 1; i >= 0; i-- ) {
            ExternalizedInfo info = (ExternalizedInfo) deleteQueue.get(i);

            if ( forceDelete || info.timestamp + EXTERNALIZE_TIMEOUT
                 + SLOW_LINK_GRACE_PERIOD < System.currentTimeMillis() ) {
                debug("remove externalized: " + info.extFileName);
                doDelete(info);
                deleteQueue.remove(i);
            }
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    protected ExternalizedInfo getInfo(Object obj) {
        return (ExternalizedInfo)externalized.get(obj);
    }

    protected void putInfo(Object obj, ExternalizedInfo info) {
        externalized.put(obj, info);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    protected Iterator getExternalizedIterator() {
        return externalized.keySet().iterator();
    }


    /**
     * TODO: documentation
     */
    protected final void debug( String mesg ) {
        if ( DEBUG )
            org.wings.util.DebugUtil.printDebugMessage(getClass(), mesg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
