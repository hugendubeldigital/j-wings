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
import java.util.*;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SBorderLayout
    extends SAbstractLayoutManager
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "BorderLayoutCG";

    private static final boolean DEBUG = true;

    /**
     * TODO: documentation
     */
    protected final int id = SComponent.createUnifiedId();

    Map components = new HashMap(5);

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

    /**
     * TODO: documentation
     *
     */
    public SBorderLayout() {}

    public void addComponent(SComponent c, Object constraint) {
        if (constraint == null)
            constraint = CENTER;

        components.put(constraint, c);
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void removeComponent(SComponent c) {
        if (c == null)
            return;

        Iterator iterator = components.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            if (c.equals(entry.getValue())) {
                iterator.remove();
                break;
            }
        }
    }

    public Map getComponents() {
        return components;
    }

    /**
     * Set the thickness of the border.
     * Default is 0, which means no border.
     *
     * @param pixel thickness of the border
     */
    public void setBorder(int pixel) {
        border = pixel;
    }
    public int getBorder() { return border; }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this layout.
     *
     * @return "BorderLayoutCG"
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
