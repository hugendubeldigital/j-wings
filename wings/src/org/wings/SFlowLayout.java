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
 * This is a flow layout
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SFlowLayout
    extends SAbstractLayoutManager
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "FlowLayoutCG";

    /**
     * TODO: documentation
     */
    protected final List components;

    /**
     * TODO: documentation
     */
    protected int orientation;
    /**
     * TODO: documentation
     */
    protected int align;

    /**
     * creates a new flow layout with horizontal orientation and
     * left alignment
     */
    public SFlowLayout() {
        components = new ArrayList(2);
        setOrientation(SConstants.HORIZONTAL);
        setAlignment(SConstants.LEFT_ALIGN);
    }

    /**
     *
    /**
     * creates a new flow layout with the given alignment
     *
     * @param alignment the alignment
     */
    public SFlowLayout(int alignment) {
        this();
        setAlignment(alignment);
    }

    public void addComponent(SComponent c, Object constraint, int index) {
        components.add(index, c);
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
     * returns the component at the given position
     *
     * @param i position
     * @return component
     */
    public SComponent getComponentAt(int i) {
        return (SComponent)components.get(i);
    }

    /**
     * Sets the orientation. Use one of the following types:
     *
     * @param o One of the following constants:
     *          {@link <code>SConstants#HORIZONTAL</code>} or
     *          {@link <code>SConstants#VERTICAL</code>}
     */
    public void setOrientation(int o) {
        orientation = o;
    }

    /**
     * returns the orientation
     * @return orientation
     */
    public int getOrientation() { return orientation; }

    /**
     * Sets the alignment. Use one of the following types:
     *
     * @param a One of the following constants:
     *          {@link <code>SConstants#LEFT_ALIGN</code>},
     *          {@link <code>SConstants#CENTER_ALIGN</code>} or
     *          {@link <code>SConstants#RIGHT_ALIGN</code>}
     */
    public void setAlignment(int a) {
        align = a;
    }

    /**
     * returns the alignment
     * @return alignment
     */
    public int getAlignment() { return align; }

    public String getCGClassID() {
        return cgClassID;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
