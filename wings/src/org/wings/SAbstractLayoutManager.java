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
import java.io.IOException;

import org.wings.io.Device;
import org.wings.plaf.*;
import org.wings.session.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public abstract class SAbstractLayoutManager
    implements SLayoutManager
{
    private static final boolean DEBUG = true;

    /**
     * TODO: documentation
     */
    protected final int id = SComponent.createUnifiedId();

    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "DefaultLayoutCG";

    /**
     * The code generation delegate, which is responsible for
     * the visual representation of a layout.
     */
    protected transient LayoutCG cg;

    /** The container using this layout */
    protected SContainer container;

    /**
     * Preferred size of component in pixel.
     */
    protected SDimension preferredSize = null;
    

    protected SAbstractLayoutManager() {
	updateCG();
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this layout.
     *
     * @return "LayoutCG"
     * @see SLayoutManager#getCGClassID
     * @see org.wings.plaf.CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }

    protected void setCG(LayoutCG newCG) {
        cg = newCG;
    }

    /**
     * Return the look and feel delegate.
     *
     * @return the componet's cg
     */
    public LayoutCG getCG() {
        return cg;
    }

    /**
     * Notification from the CGFactory that the L&F
     * has changed.
     *
     * @see SComponent#updateCG
     */
    public void updateCG() {
        setCG((LayoutCG)SessionManager.getSession().getCGManager().getCG(this));
    }

    public void write(Device d)
	throws IOException
    {
	String name = null;
	
        if (DEBUG) {
	    name = getClass().getName();
	    name = name.substring(name.lastIndexOf('.') + 1);
	    
            d.append("\n\n<!-- ")
		.append(name)
		.append(" ").append(id)
		.append(" -->");
	}

	cg.write(d, this);
	
        if (DEBUG)
            d.append("\n<!-- /")
		.append(name)
		.append(" ").append(id)
		.append(" -->\n\n");
    }

    public void setContainer(SContainer c) {
	container = c;
    }
    public SContainer getContainer() { return container; }

    /**
     * Set the preferred size of the receiving {@link SLayoutManager} in pixel.
     * It is not guaranteed that the {@link SLayoutManager} accepts this property because of
     * missing implementations in the {@link SLayoutManager } cg or html properties.
     * If <i>width</i> or <i>height</i> is zero, it is ignored and the browser
     * defines the size.
     * @see #getPreferredSize
     */
    public final void setPreferredSize(SDimension preferredSize) {
     	this.preferredSize = preferredSize;
    }
    
    /**
     * Get the preferred size of this {@link SLayoutManager }.
     * @see #setPreferredSize
     */
    public final SDimension getPreferredSize() { return this.preferredSize; }
}
