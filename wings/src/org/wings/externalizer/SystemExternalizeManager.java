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

import java.util.*;
import java.util.logging.*;

/**
 * This singleton externalizes 
 * {#link AbstractExternalizeManager#GLOBAL global} scope. Every object
 * externalized by the SystemExternalizeManager (global scope) is available 
 * over the life time of the servlet container and is not garbage collected.
 *
 * Created: Sat Nov 10 15:49:15 2001
 *
 * @author <a href="mailto:armin@hyperion.intranet.mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */

public class SystemExternalizeManager extends AbstractExternalizeManager
{
    /**
     * singleton implementation
     */
    private static final SystemExternalizeManager sharedInstance = new SystemExternalizeManager();
  
    private static final String MY_PREFIX_TIMESLICE_STRING = "-" + 
        AbstractExternalizeManager.PREFIX_TIMESLICE_STRING;

    /**
     * TODO: documentation
     */
    protected final Map externalized = Collections.synchronizedMap( new HashMap() );

    /**
     * 
     */
    private SystemExternalizeManager () {
        super(null);
    }

    /**
     * get the single system wide instance.
     *
     * @return the SystemExternalizeManager instance.
     */
    public static SystemExternalizeManager getSharedInstance() {
        return sharedInstance;
    }

    /**
     *
     */
    protected String getPrefix() {
        return MY_PREFIX_TIMESLICE_STRING;
    }

    protected void storeExternalizedInfo(String identifier, ExternalizedInfo extInfo) {
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("store identifier " + identifier + " " + extInfo.getObject().getClass());
            logger.finer("flags " + extInfo.getFlags());
        }

        externalized.put(identifier, extInfo);
    }

    public ExternalizedInfo getExternalizedInfo(String identifier) {
        return (ExternalizedInfo)externalized.get(identifier);
    }

    protected final void removeExternalizedInfo(String identifier) {
        externalized.remove(identifier);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */

