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

import java.awt.Insets;
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
public class SAbstractBorder
    implements SBorder
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "DefaultBorderCG";

    /**
     * The code generation delegate, which is responsible for
     * the visual representation of a border.
     */
    protected transient BorderCG cg;

    protected Insets insets = new Insets(0, 0, 0, 0);

    /**
     * TODO: documentation
     *
     */
    public SAbstractBorder() {
	updateCG();
    }

    /**
     * TODO: documentation
     *
     */
    public SAbstractBorder(Insets insets) {
	this();
        setInsets(insets);
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this border.
     *
     * @return "DefaultBorderCG"
     * @see SBorder#getCGClassID
     * @see org.wings.plaf.CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }

    protected void setCG(BorderCG newCG) {
        cg = newCG;
    }

    /**
     * Return the look and feel delegate.
     *
     * @return the componet's cg
     */
    public BorderCG getCG() {
        return cg;
    }

    /**
     * Notification from the CGFactory that the L&F
     * has changed.
     *
     * @see SComponent#updateCG
     */
    public void updateCG() {
        setCG((BorderCG)SessionManager.getSession().getCGManager().getCG(this));
    }

    /**
     * TODO: documentation
     *
     * @param insets
     */
    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Insets getInsets() { return insets; }

    /**
     * TODO: documentation
     *
     * @param d
     */
    public void writePrefix(Device d)
        throws IOException
    {
	cg.writePrefix(d, this);
    }

    /**
     * TODO: documentation
     *
     * @param d
     */
    public void writePostfix(Device d)
        throws IOException
    {
	cg.writePostfix(d, this);
    }
    
    /**
      * Write border attributes for span element.
      */
    public void writeSpanAttributes( Device d )
    	throws IOException
     {
		cg.writeSpanAttributes( d, this );
     }
     
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
