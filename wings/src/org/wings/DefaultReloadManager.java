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

import java.awt.Color;
import java.awt.Font;
import java.beans.*;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

import org.wings.io.Device;
import org.wings.io.StringBufferDevice;
import org.wings.plaf.*;
import org.wings.plaf.ComponentCG;
import org.wings.session.Session;
import org.wings.session.SessionManager;
import org.wings.style.Style;
import org.wings.externalizer.ExternalizeManager;

/**
 * This is the default implementation of the reload manager.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DefaultReloadManager
    implements ReloadManager
{
    Aspect[] aspects = new Aspect[0];

    public void addAspect(int aspect) {
        int count = aspects.length;
        Aspect[] newAspects = new Aspect[count+1];
        System.arraycopy(aspects, 0, newAspects, 0, count);
        newAspects[count] = new Aspect(aspect);
        aspects = newAspects;
    }

    public void markDirty(SComponent component, int aspect) {
        SFrame frame = component.getParentFrame();
        for (int i=0; i < aspects.length; i++)
            if ((aspects[i].aspect & aspect) == aspects[i].aspect)
                aspects[i].dirty.add(frame);
    }

    public SComponent[] getDirtyComponents(int aspect) {
        List components = new LinkedList();
        for (int i=0; i < aspects.length; i++)
            if ((aspects[i].aspect & aspect) == aspects[i].aspect)
                components.addAll(aspects[i].dirty);

        return (SComponent[])components.toArray(new SComponent[components.size()]);
    }

    public void clearDirtyComponents(int aspect) {
        for (int i=0; i < aspects.length; i++)
            if ((aspects[i].aspect & aspect) == aspects[i].aspect)
                aspects[i].dirty.clear();
    }

    static class Aspect {
        public int aspect;
        public List dirty = new LinkedList();

        public Aspect(int aspect) {
            this.aspect = aspect;
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
