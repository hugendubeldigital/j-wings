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
 * This is a default implementation of the reload manager. It assumes, that
 * the whole document has to be reloaded with every request.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DefaultReloadManager
    implements ReloadManager
{
    private SFrame frame = null;

    public void markDirty(SComponent component) {
	if (component.getParentFrame() != null)
	    frame = component.getParentFrame();
    }
    public SComponent[] getDirtyComponents() {
	if (frame != null)
	    return new SComponent[] { frame };
	else
	    return null;
    }

    public void clearDirtyComponents() {}

    public SComponent getManagerComponent() {
	return null;
    }

    public String getTarget() {
	return null;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
