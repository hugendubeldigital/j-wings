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
 * This is a grid layout
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

    private static final boolean DEBUG = true;

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

    private SContainer container = null;

    /**
     * creats a new grid layout with the given number of columns
     *
     * @param cols number of columns
     */
    public SGridLayout(int cols) {
        setColumns(cols);
    }

    /**
     * creats a new grid layout with the given number of columns and rows
     *
     * @param rows number of rows
     * @param cols number of columns
     */
    public SGridLayout(int rows, int cols) {
        setRows(rows);
        setColumns(cols);
    }

    /**
     * sets the number of columns
     *
     * @param c number of columns
     */
    public void setColumns(int c) {
        cols = c;
    }

    /**
     * returns the number of columns
     *
     * @return number of columns
     */
    public int getColumns() { return cols; }

    /**
     * sets the number of rows
     *
     * @param r number of rows
     */
    public void setRows(int r) {
        rows = r;
    }

    /**
     * returns the number of rows
     *
     * @returns number of rows
     */
    public int getRows() { return rows; }

    public void addComponent(SComponent c, Object constraint) {
        components.add(c);
    }

    public void removeComponent(SComponent c) {
        components.remove(c);
    }

    /**
     * returns a list of all components
     * @return all components
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
