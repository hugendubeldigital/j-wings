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
 * This implementation assumes, that the granularity of separately reloadable
 * components is the frame. May be, there will be better possibilities in the
 * future - DHTML, updating the DOM with JavaScript ???
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class FrameSetReloadManager
    implements ReloadManager
{
    private final Set dirtyFrames = new HashSet();
    private ReloadManagerFrame reloadManagerFrame = null;

    public void markDirty(SComponent component) {
	if (component.getParentFrame() != null)
	    dirtyFrames.add(component.getParentFrame());
    }

    public SComponent[] getDirtyComponents() {
	return (SComponent[])dirtyFrames.toArray(new SFrame[dirtyFrames.size()]);
    }

    public void clearDirtyComponents() {
	dirtyFrames.clear();
    }

    public SComponent getManagerComponent() {
	if (reloadManagerFrame == null)
	    reloadManagerFrame = new ReloadManagerFrame();
	return reloadManagerFrame;
    }

    public String getTarget() {
	return "frame" + ((ReloadManagerFrame)getManagerComponent()).getUnifiedIdString();
    }
}