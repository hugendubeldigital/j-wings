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
 * The reload manager interface.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public interface ReloadManager
{
    /**
     * Mark a <code>component</code> dirty.
     * Frames that container dirty components have to be reloaded.
     * @param component the dirty component
     */
    void markDirty(SComponent component);

    /**
     * Return an array of all dirty components.
     * @return an array of all dirty components
     */
    SComponent[] getDirtyComponents();
    /**
     * Clear dirts components list.
     */
    void clearDirtyComponents();

    /**
     * Get the reload manager component, that is the target of all links.
     */
    SComponent getManagerComponent();

    /**
     * The key to the reload manager component
     */
    String getTarget();
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
