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
 * SystemExternalizeManager.java
 *
 * Im Gegensatz zum SessionExternalizeManager ist dieser ExtManager
 * dauerhaft. D.h. Objecte, die von allen Sessions benoetigt werden sollten nur
 * einmal und zwar mit dem SystemExternalizeManager externalized werden.
 *
 * Created: Sat Nov 10 15:49:15 2001
 *
 * @author <a href="mailto:armin@hyperion.intranet.mercatis.de">Armin Haaf</a>
 * @version
 */

public class SystemExternalizeManager extends AbstractExternalizeManager
{
    /**
     * TODO: documentation
     */
    private static final boolean DEBUG = true;

    /**
     * TODO: documentation
     *
     */
    private static final SystemExternalizeManager sharedInstance = new SystemExternalizeManager();
  
    /**
     * TODO: documentation
     */
    protected final Map externalized = Collections.synchronizedMap( new HashMap() );

    /**
     * TODO: documentation
     *
     */
    private long counter = -1;

  
    /**
     * TODO: documentation
     *
     */
    private SystemExternalizeManager () {
        super(null);
    }

    /**
     *
     */
    protected synchronized final long getNextIdentifier() {
        return counter--;
    }

    /**
     * TODO: documentation
     *
     */
    protected void storeExternalizedInfo(String identifier,
                                         ExternalizedInfo extInfo) {
        debug("store identifier " + identifier);

        externalized.put(identifier, extInfo);
   }

    /**
     * TODO: documentation
     *
     */
    public ExternalizedInfo getExternalizedInfo(String identifier) {
        return (ExternalizedInfo)externalized.get(identifier);
    }

    /**
     * TODO: documentation
     *
     */
    protected final void removeExternalizedInfo(String identifier) {
        externalized.remove(identifier);
    }

    /**
     * TODO: documentation
     *
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
