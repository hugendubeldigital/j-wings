/*
 * $Id$
 * (c) Copyright 2001 wingS development team.
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
import java.beans.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.swing.Icon;

import org.wings.*;
import org.wings.io.Device;
import org.wings.io.StringBufferDevice;
import org.wings.io.DeviceBuffer;
import org.wings.plaf.*;
import org.wings.style.StyleSheet;
import org.wings.session.Session;
import org.wings.session.SessionManager;

/**
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SFrameSet
    extends SFrame
{
    public SFrameSet() {}

    public SFrameSet(SFrameSetLayout layout) {
	setLayout(layout);
    }

    public final SContainer getContentPane() {
        return null; // heck :-)
    }

    /**
     * Only SFrameSets or SFrames are allowed.
     */
    public SComponent addComponent(SComponent c, Object constraint) {
	if (c == null)
	    return null;

	if (!(c instanceof SFrame))
	    throw new IllegalArgumentException("Only SFrameSets or SFrames are allowed.");

	if (layout != null)
	    layout.addComponent(c, constraint);

	getComponentList().add(c);
	getConstraintList().add(constraint);
	c.setParent(this);

        return c;
	//return super.addComponent(c, constraint);
    }

    /**
     * Only SFrameSets or SFrames are allowed.
     */
    public boolean removeComponent(SComponent c) {
        if (c == null)
            return false;

	if (!(c instanceof SFrame))
	    throw new IllegalArgumentException("Only SFrameSets or SFrames are allowed.");

        if (layout != null)
            layout.removeComponent(c);

        c.setParent(null);

        int index = getComponentList().indexOf(c);
        boolean erg = getComponentList().remove(c);
        if (erg) {
            getConstraintList().remove(index);
        }
        return erg;
	//return super.removeComponent(c);
    }

    /**
     * Sets the parent frameset container.
     *
     * @param p the container
     */
    public void setParent(SContainer p) {
	if (!(p instanceof SFrameSet))
	    throw new IllegalArgumentException("SFrameSets can only be added to SFrameSets.");

        parent = p;
    }

    /**
     * There is no parent frame.
     *
     * @param f the frame
     */
    protected void setParentFrame(SFrame f) {}

    /**
     * There is no parent frame.
     *
     * @return
     */
    public SFrame getParentFrame() {
        return null;
    }

    /**
     * TODO: documentation
     */
    public void setServer(String path) {
	super.setServer(path);
	setServerAddress(serverAddress);
    }

    /**
     * Set server address and propagate it to all frames
     */
    protected void setServerAddress(SGetAddress serverAddress) {
        this.serverAddress = serverAddress;

	// propagate it to all frame(set)s
	Iterator iterator = getComponentList().iterator();
	while (iterator.hasNext()) {
	    Object object = iterator.next();
	    if (object instanceof SFrame)
		((SFrame)object).setServerAddress(serverAddress);
	}
    }

    /**
     * Set the base target and propagate it to all frames
     */
    public void setBaseTarget(String baseTarget) {
        this.baseTarget = baseTarget;

	// propagate it to all frame(set)s
	Iterator iterator = getComponentList().iterator();
	while (iterator.hasNext()) {
	    Object object = iterator.next();
	    if (object instanceof SFrame)
		((SFrame)object).setBaseTarget(baseTarget);
	}
    }

    public void setLayout(SLayoutManager l) {
	if (!(l instanceof SFrameSetLayout))
	    throw new IllegalArgumentException("Only SFrameSetLayout is allowed.");

	super.setLayout(l);
    }

    public void write(Device s) throws IOException {
	layout.write(s);
    }
}