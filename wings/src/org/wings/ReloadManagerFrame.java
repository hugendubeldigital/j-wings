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
 * An invisible frame, that executes a javascript function <code>onload</code>,
 * that reloads all dirty frames.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class ReloadManagerFrame
    extends SFrame
{
    public ReloadManagerFrame() {}

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
	if (!(p == null || p instanceof SFrameSet))
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
     * No LayoutManager allowed.
     */
    public void setLayout(SLayoutManager l) {
	throw new IllegalArgumentException("No LayoutManager allowed");
    }

    private Set dirtyResources;
    public void setDirtyResources(Set dirtyResources) {
        this.dirtyResources = dirtyResources;
    }

    /**
     * Generate a minimal document with a javascript function, that reloads
     * all dirty frames. The list of dirty frames is obtained from the ReloadManager.
     * After the code has been generated, the dirty components list is cleared.
     */
    public void write(Device d) throws IOException {
	ExternalizeManager externalizer = getSession().getExternalizeManager();

	d.append("<head><title>ReloadManager</title>\n");
	d.append("<script language=\"javascript\">\n");
	d.append("function reload() {\n");

        boolean all = false;
        DynamicResource toplevel = null;
        {
            Iterator it = dirtyResources.iterator();
            while (it.hasNext()) {
                DynamicResource resource = (DynamicResource)it.next();
                if (!(resource.getFrame() instanceof ReloadManagerFrame) &&
                    resource.getFrame().getParent() == null) {
                    toplevel = resource;
                    all = true;
                }
            }
        }

        if (all) {
            // reload the _whole_ document
            d.append("parent.location='");
            d.append(toplevel.getURL());
            d.append("';\n");

            // invalidate resources
            Iterator it = dirtyResources.iterator();
            while (it.hasNext()) {
                DynamicResource resource = (DynamicResource)it.next();
                resource.invalidate();
            }
        }
        else {
            Iterator it = dirtyResources.iterator();
            while (it.hasNext()) {
                DynamicResource resource = (DynamicResource)it.next();
                resource.invalidate();

                d.append("parent.frame");
                d.append(resource.getFrame().getUnifiedId());
                d.append(".location='");
                d.append(resource.getURL());
                d.append("';\n");
            }
        }

	d.append("}\n");
	d.append("</script>\n");
	d.append("</head>\n");
	d.append("<body onload=\"reload()\"></body>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
