/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SGridLayout implements SLayoutManager
{
    private static final boolean DEBUG = true;

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

    boolean headerBold = false;
    boolean usePercent = false;
    boolean useAbsolute = false;

    int percent=0;
    int absolute=0;

    int cellPadding = -1;
    int cellSpacing = -1;

    private SContainer container = null;

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

    /**
     * TODO: documentation
     *
     * @param r
     */
    public void setRows(int r) {
        rows = r;
    }

    /**
     * TODO: documentation
     *
     * @param p
     */
    public void setPadding(int p) {
        cellPadding = p;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setSpacing(int s) {
        cellSpacing = s;
    }

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
    public SComponent getComponentAt(int i) {
        return (SComponent)components.get(i);
    }

    /**
     * TODO: documentation
     *
     * @param pixel
     */
    public void setBorder(int pixel) {
        border = pixel;
    }

    /**
     * TODO: documentation
     *
     * @param s
     * @throws IOException
     */
    public void write(Device s)
        throws IOException
    {
        // mit diesem Ansatz ist es moeglich nur die Anzahl der Zeilen
        // anzugeben.
        if ( DEBUG ) {
            s.append("\n\n<!-- GridLayout ").append(id).append(" -->");
        }
        s.append("\n<TABLE");
        if ( cellSpacing>=0 )
            s.append(" CELLSPACING=").append(cellSpacing);
        else
            s.append(" CELLSPACING=0");

        if ( cellPadding>=0 )
            s.append(" CELLPADDING=").append(cellPadding);

        if ( useAbsolute )
            s.append(" WIDTH=").append(percent);
        else if ( usePercent )
            s.append(" WIDTH=").append(percent).append("%");

        if ( border>0 )
            s.append(" BORDER=").append(border);
        else
            s.append(" BORDER=0");

        if ( container!=null && container.getBackground()!=null  )
            s.append(" BGCOLOR=#").
                append(SUtil.toColorString(container.getBackground()));

        s.append(">\n");

        int cols = this.cols;
        if ( cols<=0 )
            cols = components.size()/rows;

        boolean firstRow = true;

        int col = 0;
        for ( Iterator iter = components.iterator();
              iter.hasNext(); ) {

            if ( col==0 )
                s.append("<TR>");
            else if ( col%cols==0 && iter.hasNext() ) {
                s.append("</TR>\n<TR>");
                firstRow = false;
            }

            if ( firstRow && headerBold )
                s.append("<TH");
            else
                s.append("<TD");
            SComponent c = (SComponent)iter.next();

            Color backupBackground = c.getBackground();
            SUtil.appendTableCellAttributes(s, c);

            s.append(">");

            c.setBackground(null);

            c.write(s);

            c.setBackground(backupBackground);

            if ( firstRow && headerBold )
                s.append("</TH>");
            else
                s.append("</TD>");

            col++;

            if ( !iter.hasNext() )
                s.append("</TR>\n");
        }

        // Einfachste Loesung, hier muss aber immer vorher bekannt sein,
        // wieviele Zeilen an Komponenten kommen. Der obere Ansatz ist der
        // bessere.
        //     for ( int i=0; i<rows; i++ ) {
        //       s.append("<TR>");
        //       for ( int j=0; j<cols; j++ ) {
        // 	s.append("<TD>");
        // 	getComponentAt(i*cols+j).write(s);
        // 	s.append("</TD>");

        //       }
        //       s.append("</TR>");
        //     }
        s.append("</TABLE>");

        if ( DEBUG ) {
            s.append("\n<!-- GridLayout ").append(id).append(" End -->\n\n");
        }
    }

    /**
     * TODO: documentation
     *
     * @param percentage
     */
    public void setWidthRelative(int percentage) {
        usePercent = true;
        percent = percentage;
    }

    /**
     * TODO: documentation
     *
     * @param abs
     */
    public void setWidthAbsolute(int abs) {
        useAbsolute = true;
        absolute = abs;
    }

    /**
     * TODO: documentation
     *
     * @param b
     */
    public void setHeaderBold(boolean b) {
        headerBold = b;
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setContainer(SContainer c) {
        container = c;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
