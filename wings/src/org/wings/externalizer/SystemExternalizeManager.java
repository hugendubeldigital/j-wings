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

import java.util.Map;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.Collections;

/**
 * This singleton externalizes 
 * {#link AbstractExternalizeManager#GLOBAL global} scope. Every object
 * externalized by the SystemExternalizeManager (global scope) is available over
 * the life time of the servlet container and is not garbage collected.
 *
 * Created: Sat Nov 10 15:49:15 2001
 *
 * @author <a href="mailto:armin@hyperion.intranet.mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */

public class SystemExternalizeManager extends AbstractExternalizeManager
{
    /**
     * TODO: documentation
     */
    private static final boolean DEBUG = true;

    /**
     * singleton implementation
     */
    private static final SystemExternalizeManager sharedInstance = new SystemExternalizeManager();
  
    /**
     * TODO: documentation
     */
    protected final Map externalized = Collections.synchronizedMap( new HashMap() );

    /**
     * To generate the identifier for a externalized object. This is overwritten
     * to generate negative numbers. This is necessary to distinguish between
     * object externalized by the SystemExternalizeManager and the
     * {@link ExternalizeManager}
     */
    private long counter = -1;

  
    /**
     * 
     */
    private SystemExternalizeManager () {
        super(null);
    }

    /**
     * To generate the identifier for a externalized object. This is overwritten
     * to generate negative numbers. This is necessary to distinguish between
     * object externalized by the SystemExternalizeManager and the
     * {@link ExternalizeManager}
     */
    protected synchronized final long getNextIdentifier() {
        return counter--;
    }

    protected void storeExternalizedInfo(String identifier,
                                         ExternalizedInfo extInfo) {
        debug("store identifier " + identifier);

        externalized.put(identifier, extInfo);
   }

    public ExternalizedInfo getExternalizedInfo(String identifier) {
        return (ExternalizedInfo)externalized.get(identifier);
    }

    protected final void removeExternalizedInfo(String identifier) {
        externalized.remove(identifier);
    }

    /**
     * get the singleton instance
     */
    public static SystemExternalizeManager getSharedInstance() {
        return sharedInstance;
    }
    
    private static final void debug(String msg) {
        if (DEBUG) {
            org.wings.util.DebugUtil.printDebugMessage(ExternalizeManager.class, msg);
        }
    }
  
}// SystemExternalizeManager

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */

