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

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SBorderLayout
    implements SLayoutManager
{
    private static final boolean DEBUG = true;

    // nur zu Debug Zwecken, macht den HTML Code uebersichtlicher !!
    /**
     * TODO: documentation
     */
    protected final int id = SComponent.createUnifiedId();

    SComponent north = null;
    SComponent south = null;
    SComponent east = null;
    SComponent west = null;
    SComponent center = null;

    /**
     * TODO: documentation
     */
    public static final String NORTH = "North";
    /**
     * TODO: documentation
     */
    public static final String SOUTH = "South";
    /**
     * TODO: documentation
     */
    public static final String EAST = "East";
    /**
     * TODO: documentation
     */
    public static final String WEST = "West";
    /**
     * TODO: documentation
     */
    public static final String CENTER = "Center";

    int border = 0;

    private SContainer container = null;

    /**
     * TODO: documentation
     *
     */
    public SBorderLayout() {}

    public void addComponent(SComponent c, Object constraint) {
        if ( constraint==null ) {
            center = c;
            return;
        }

        if ( constraint.equals(NORTH) )
            north = c;
        else if ( constraint.equals(SOUTH) )
            south = c;
        else if ( constraint.equals(EAST) )
            east = c;
        else if ( constraint.equals(WEST) )
            west = c;
        else
            center = c;
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void removeComponent(SComponent c) {
        if ( north==c )
            north = null;
        if ( south==c )
            south = null;
        if ( east==c )
            east = null;
        if ( west==c )
            west = null;
        if ( center==c )
            center = null;
    }

    /**
     * TODO: documentation
     *
     * @param pixel
     */
    public void setBorder(int pixel) {
        border = pixel;
    }

    private void appendComponent(Device s, SComponent c) {
        Color backupBackground = c.getBackground();

        SUtil.appendTableCellAttributes(s, c);
        s.append(">\n");

        c.setBackground(null);
        try {
            c.write(s);
        } catch (IOException e) {}
        c.setBackground(backupBackground);

        s.append("\n</TD>");
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
        Color backupForeground = null;
        Color backupBackground = null;

        int cols = 0;
        if ( west!=null ) cols++;
        if ( center!=null ) cols++;
        if ( east!=null ) cols++;

        int rows = 0;
        if ( north!=null ) rows++;
        if ( center!=null ) rows++;
        if ( south!=null ) rows++;

        if ( DEBUG ) {
            s.append("\n\n<!-- BorderLayout ").append(id).append(" -->");
        }

        s.append("\n<TABLE");
        if ( border>0 )
            s.append(" BORDER=").append(border);
        if ( container!=null && container.getBackground()!=null  )
            s.append(" BGCOLOR=#").
                append(SUtil.toColorString(container.getBackground()));
        s.append(">");

        if ( north!=null ) {
            s.append("\n<TR> <TD COLSPAN=").append(cols);
            appendComponent(s, north);
            s.append("</TR>");
        }

        s.append("\n<TR>");
        if ( west!=null ) {
            s.append("<TD");
            appendComponent(s, west);
        }

        if ( center!=null ) {
            s.append("<TD");
            appendComponent(s, center);
        }

        if ( east!=null ) {
            s.append("<TD");
            appendComponent(s, east);
        }
        s.append("</TR>");

        if ( south!=null ) {
            s.append("\n<TR><TD COLSPAN=").append(cols);
            appendComponent(s, south);
            s.append("</TR>");
        }
        s.append("\n</TABLE>");

        if ( DEBUG ) {
            s.append("\n<!-- BorderLayout ").append(id).append(" End -->\n\n");
        }
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
