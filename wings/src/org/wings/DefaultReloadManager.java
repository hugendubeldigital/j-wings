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

package org.wings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This is the default implementation of the reload manager.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DefaultReloadManager
    implements ReloadManager
{

    protected final HashMap dirtyResources = new HashMap();

    public void markDirty(DynamicResource d) {
        HashSet resources = (HashSet)dirtyResources.get(d.getClass());

        if ( resources==null ) {
            resources = new HashSet();
            dirtyResources.put(d.getClass(), resources);
        }

        resources.add(d);
    }

    public Set getDirtyResources(Class resourceType) {
        return (HashSet)dirtyResources.get(resourceType);
    }

    public void clear(Class resourceType) {
        Set s = getDirtyResources(resourceType);
        if ( s!=null )
            s.clear();
    }

    public void clear() {
        dirtyResources.clear();
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
