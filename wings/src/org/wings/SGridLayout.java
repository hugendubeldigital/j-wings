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
import java.util.*;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SGridLayout
    extends SAbstractLayoutManager
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "GridLayoutCG";

    private static final boolean DEBUG = false;

    // nur zu Debug Zwecken, macht den HTML Code uebersichtlicher !!
    /**
     * TODO: documentation
     */
    protected final int id = SComponent.createUnifiedId();

    /**
     * TODO: documentation
     */
    protected ArrayList components = new ArrayList(2);

    int rows = 0;
    int cols = 0;

    int border = 0;

    boolean header = false;
    boolean relative = false;

    int width = -1;
    int cellPadding = -1;
    int cellSpacing = -1;

    /**
     * TODO: documentation
     *
     * @param cols
     */
    public SGridLayout(int cols) {
        setColumns(cols);
    }

    public SGridLayout(int rows, int cols) {
        setRows(rows);
        setColumns(cols);
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setColumns(int c) {
        cols = c;
    }
    public int getColumns() { return cols; }

    /**
     * TODO: documentation
     *
     * @param r
     */
    public void setRows(int r) {
        rows = r;
    }
    public int getRows() { return rows; }

    public void addComponent(SComponent c, Object constraint) {
        components.add(c);
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void removeComponent(SComponent c) {
        components.remove(c);
    }

    /**
     * TODO: documentation
     *
     * @param i
     * @return
     */
    public List getComponents() {
        return components;
    }

    /**
     * TODO: documentation
     *
     * @param p
     */
    public void setCellPadding(int p) {
        cellPadding = p;
    }
    public int getCellPadding() { return cellPadding; }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setCellSpacing(int s) {
        cellSpacing = s;
    }
    public int getCellSpacing() { return cellSpacing; }

    /**
     * TODO: documentation
     *
     * @param pixel
     */
    public void setBorder(int pixel) {
        border = pixel;
    }
    public int getBorder() { return border; }

    /**
     * TODO: documentation
     *
     * @param percentage
     */
    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() { return width; }

    /**
     * TODO: documentation
     *
     * @param abs
     */
    public void setRelative(boolean relative) {
        this.relative = relative;
    }
    public boolean isRelative() { return relative; }

    /**
     * TODO: documentation
     *
     * @param b
     */
    public void setHeader(boolean b) {
        header = b;
    }
    public boolean getHeader() { return header; }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this layout.
     *
     * @return "GridLayoutCG"
     * @see SLayoutManager#getCGClassID
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
