/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.util;

import org.wings.session.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Util to access CGManager objects.
 *
 * @author Andreas Baldeau (andreas.baldeau@freiheit.com)
 * @version $Revision$
 */
public class CGObjectUtil {

    private static final Map<String, Object> OBJECT_CACHE = new HashMap<String, Object>();

    /**
     * Lazy loads the specified object.
     *
     * @param name  Name of the object.
     * @param cls  Type of the object.
     * @param <T>  Type of the object.
     *
     * @return The object.
     */
    public static <T> T getObject(final String name, Class<T> cls) {
        Object obj = OBJECT_CACHE.get(name);
        if (obj == null) {
            obj = SessionManager.getSession().getCGManager().getObject(name, cls);
            OBJECT_CACHE.put(name, obj);
        }

        return cls.cast(obj);
    }
}





