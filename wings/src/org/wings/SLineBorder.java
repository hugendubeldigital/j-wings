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
import java.awt.Insets;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SLineBorder
    extends SAbstractBorder
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "LineBorderCG";

    int thickness = 1;
    Color lineColor = Color.black;

    /**
     * TODO: documentation
     *
     */
    public SLineBorder() {}

    /**
     * TODO: documentation
     *
     * @param thickness
     */
    public SLineBorder(int thickness) {
        setThickness(thickness);
    }

    /**
     * TODO: documentation
     *
     * @param thickness
     * @param insets
     */
    public SLineBorder(int thickness, Insets insets) {
        setThickness(thickness);
        setInsets(insets);
    }

    /**
     * TODO: documentation
     *
     * @param thickness
     */
    public void setThickness(int thickness) {
        this.thickness = thickness;
    }
    
    public int getThickness() { return thickness; }

    /**
     * Set the lineColor color.
     *
     * @param c the new lineColor color
     */
    public void setLineColor(Color c) {
        lineColor = c;
    }

    /**
     * Return the lineColor color.
     *
     * @return the lineColor color
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this border.
     *
     * @return "LineBorderCG"
     * @see SBorder#getCGClassID
     * @see org.wings.plaf.CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
