/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import org.wings.plaf.TextAreaCG;

/**
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class STextArea        extends STextComponent {

    /**
     * Text wrapping behaviour for {@link STextArea#setLineWrap(int)}: Don't wrap.
     */
    public final static int NO_WRAP = 0;
    /**
     * Text wrapping behaviour for {@link STextArea#setLineWrap(int)}: Wrap at width.
     */
    public final static int VIRTUAL_WRAP = 1;
    /**
     * Text wrapping behaviour for {@link STextArea#setLineWrap(int)}: Wrap at physical input box borders.
     */
    public final static int PHYSICAL_WRAP = 2;

    private int rows = 0;

    private int columns = 0;

    /**
     * allowed parameters
     * are off(0), virtual(1), physical(2)
     * default value is off
     */
    private int lineWrap = NO_WRAP;
    
 
    /**
     * Creates a TextArea with defaults
     */
    public STextArea() {
        this(null,0,0);
    }

    /**
     * Creates a textArea with the given text
     * default values, no rows no columns no warp
     * @param text the text to display
     **/
    public STextArea(String text) {
        this(text, 0, 0);
    }

    /**
     * Creates a new empty TextArea with the specified number of rows and columns.
     * @param rows is the number of rows to display
     * @param columns is the number of columns to display
     **/
    public STextArea(int rows, int columns) {
        this(null, rows, columns);
    }
          

    /**
     * Creates a new TextArea with the specified text and number of rows and columns.
     * @param text is the text to display
     * @param rows is the number of rows to display
     * @param columns is the number of columns to display
     **/
    public STextArea(String text, int rows, int columns) {
        super();
        this.setText(text);
        this.setRows(rows);
        this.setColumns(columns);
    }
    
 
    /**
     * Set the number of visble rows
     * @param r the number of visble rows
     */
    public void setRows(int r) {
        int oldRows = rows;
        rows = r;
        if (oldRows != rows)
            reload();
    }

    /**
     * Get the number of visble rows
     * @return number of visble rows
     */
    public int getRows() {
        return rows;
    }


    /**
     * sets the Number of visible columns
     * @param c the number of columns
     */
    public void setColumns(int c) {
        int oldColumns = columns;
        columns = c;
        if (columns != oldColumns)
            reload();
    }

    
    /**
     * Current number of columns to display
     * @return the number of columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Valid values are {@link #NO_WRAP} {@link #VIRTUAL_WRAP} and {@link #PHYSICAL_WRAP}
     * @param lw
     */
    public void setLineWrap(int lw) {
        if (lw >= 0 && lw < 3)
            lineWrap = lw;
    }


    /**
     * @return one of the LineWraps, NO_WRAP, VIRTUAL_WRAP, PHYSICAL_WRAP
     */
    public int getLineWrap() {
        return lineWrap;
    }

    
    public void setCG(TextAreaCG cg) {
        super.setCG(cg);
    }
}


