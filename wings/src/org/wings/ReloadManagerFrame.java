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
import org.wings.externalizer.*;
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
public class ReloadManagerFrame
    extends SFrame
{
    public ReloadManagerFrame() {
	getSession().getDispatcher().setTarget(getUnifiedIdString());
    }

    public final SContainer getContentPane() {
        return null; // heck :-)
    }

    /**
     * This frame stays invisible
     */
    public SComponent addComponent(SComponent c, Object constraint) {
	throw new IllegalArgumentException("Adding Components is not allowed");
    }

    /**
     * This frame stays invisible
     */
    public boolean removeComponent(SComponent c) {
	throw new IllegalArgumentException("Does not have Components");
    }

    /**
     * Sets the parent FrameSet container.
     *
     * @param p the container
     */
    public void setParent(SContainer p) {
	if (!(p instanceof ReloadManagerFrame))
	    throw new IllegalArgumentException("The ReloadManagerFrame can only be added to SFrameSets.");

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
     * Set server address.
     */
    protected void setServerAddress(SGetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setLayout(SLayoutManager l) {
	throw new IllegalArgumentException("No LayoutManager allowed");
    }

    public void write(Device d) throws IOException {
	ExternalizeManager externalizer = getSession().getExternalizeManager();

	d.append("<head><title>ReloadManager</title>\n");
	d.append("<script language=\"javascript\">\n");
	d.append("function reload() {\n");
	SComponent[] components = getReloadManager().getDirtyComponents();
	SFrameSet toplevel = null;
	for (int i=0; i < components.length; i++)
	    if (components[i].getParent() == null)
		toplevel = (SFrameSet)components[i];
	if (toplevel != null) {
	    System.err.println("reload the whole frameset");
	    d.append("parent.location='");
	    d.append(toplevel.getServerAddress());
	    d.append("';\n");
	}
	else {
	    for (int i=0; i < components.length; i++) {
		String src = externalizer.externalize(((SFrame)components[i]).show(), "text/html");
		d.append("parent.frame");
		d.append(components[i].getUnifiedIdString());
		d.append(".location='");
		d.append(src);
		d.append("';\n");
	    }
	}
	d.append("}\n");
	d.append("</script>\n");
	d.append("</head>\n");
	d.append("<body onload=\"reload()\"></body>");
	getReloadManager().clearDirtyComponents();
    }

    private ReloadManager reloadManager = null;
    protected ReloadManager getReloadManager() {
	if (reloadManager == null)
	    reloadManager = getSession().getReloadManager();
	return reloadManager;
    }

    public void updateCG() {}
}
