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

import java.util.*;
import org.wings.style.DynamicStyleSheetResource;

/**
 * This is the default implementation of the reload manager.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DefaultReloadManager
    implements ReloadManager
{
    /**
     * a set of all resources, manged by this ReloadManager, that are marked
     * dirty.
     */
    protected final Set dirtyResources;

    public DefaultReloadManager() {
        dirtyResources = new HashSet();
    }
    
    public void reload(SComponent component, int aspect) {
        SFrame parent = component.getParentFrame();

        if (parent == null)
            return;

        switch (aspect) {
        case RELOAD_CODE: 
            markDirty(parent.getDynamicResource(DynamicCodeResource.class));
            break;
        case RELOAD_STYLE: 
            markDirty(parent.getDynamicResource(DynamicStyleSheetResource.class));
            break;
        case RELOAD_SCRIPT: 
            // TODO
            //reloadManager.markDirty(parent.getDynamicResource(DynamicScriptResource.class));
            break;
        }
    }

    public synchronized void markDirty(DynamicResource d) {
        //new Exception().printStackTrace(System.err);
        dirtyResources.add(d);
    }

    public Set getDirtyResources() {
        return dirtyResources;
    }

    public synchronized void clear() {
        dirtyResources.clear();
    }

    public synchronized void invalidateResources() {
        //new Exception().printStackTrace(System.err);

        //Set frames = new HashSet();
        Iterator it = dirtyResources.iterator();
        while (it.hasNext()) {
            DynamicResource resource = (DynamicResource)it.next();
            resource.invalidate();
            it.remove();
            //frames.add(resource.getFrame());
        }
        /*
        it = frames.iterator();
        while (it.hasNext()) {
            SFrame frame = (SFrame)it.next();
            frame.invalidate();
        }
        */
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
