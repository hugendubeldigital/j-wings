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
 * This is a an abstract implementation of the <code>SBorder</code>
 * interface.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public abstract class SAbstractBorder
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

    /**
     * the insets
     */
    protected Insets insets = new Insets(0, 0, 0, 0);

    /**
     * border color
     */
    private java.awt.Color fColor = java.awt.Color.black;

    /**
     * border thickness
     */
    private int fThickness = 1;

    /**
     * constructor
     */
    public SAbstractBorder() {
	updateCG();
    }

    /**
     * constructor
     */
    public SAbstractBorder(Insets insets) {
	this();
        setInsets(insets);
    }

    public String getCGClassID() {
        return cgClassID;
    }

    /**
     * Sets the look and feel delegate.
     *
     * @param newGC the new componet's cg
     */
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

    public void updateCG() {
        setCG((BorderCG)SessionManager.getSession().getCGManager().getCG(this));
    }

    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    public Insets getInsets() { return insets; }

    public void writePrefix(Device d) throws IOException {
	cg.writePrefix(d, this);
    }

    public void writePostfix(Device d) throws IOException {
	cg.writePostfix(d, this);
    }

    public void writeSpanAttributes(Device d) throws IOException {
        cg.writeSpanAttributes( d, this );
    }

    public java.awt.Color getColor() {
        return fColor;
    }

    public void setColor(java.awt.Color color) {
        fColor = color;
    }

    public int getThickness() {
        return fThickness;
    }

    public void setThickness(int thickness) {
        fThickness = thickness;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
