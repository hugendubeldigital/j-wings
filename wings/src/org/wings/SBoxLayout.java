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

import java.io.IOException;
import java.util.*;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SBoxLayout
    extends SAbstractLayoutManager
{
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;

    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "BoxLayoutCG";

    /**
     * TODO: documentation
     */
    protected ArrayList components = new ArrayList(2);

    /**
     * TODO: documentation
     */
    protected int orientation = SConstants.HORIZONTAL;
    /**
     * TODO: documentation
     */
    protected int align = SConstants.LEFT_ALIGN;

    /**
     * TODO: documentation
     *
     */
    public SBoxLayout() {}

    /**
     * TODO: documentation
     *
     * @param alignment
     */
    public SBoxLayout(int alignment) {
        setAlignment(alignment);
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
     * @return
     */
    public List getComponents() {
        return components;
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

    /*
     * Sets the orientation. Use one of the following types:
     * <UL>
     * <LI> {@link SConstants#HORIZONTAL}
     * <LI> {@link SConstants#VERTICAL}
     * </UL>
     */
    /**
     * TODO: documentation
     *
     * @param o
     */
    public void setOrientation(int o) {
        orientation = o;
    }
    public int getOrientation() { return orientation; }

    /*
     * Sets the alignment. Use one of the following types:
     * <UL>
     * <LI> {@link SConstants#LEFT_ALIGN}
     * <LI> {@link SConstants#CENTER_ALIGN}
     * <LI> {@link SConstants#RIGHT_ALIGN}
     * </UL>
     *
     * @param a
     */
    public void setAlignment(int a) {
        align = a;
    }
    public int getAlignment() { return align; }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this layout.
     *
     * @return "FlowLayoutCG"
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
