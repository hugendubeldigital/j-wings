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

import java.io.IOException;
import javax.swing.UIManager;

import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SCellRendererPane
    extends SContainer
{
    /**
     * Construct a CellRendererPane object.
     */
    public SCellRendererPane() {
        super();
        setLayout(null);
        setVisible(false);
    }

    /**
     * Shouldn't be called.
     */
    public void write(Device d) {
    }

    /**
     * If the specified component is already a child of this then we don't
     * bother doing anything - stacking order doesn't matter for cell
     * renderer components (CellRendererPane doesn't paint anyway).<
     */
    public SComponent addComponent(SComponent c, Object constraints) {
        if ( c.getParent() == this ) {
            return null;
        }
        else {
            return super.addComponent(c, constraints);
        }
    }

    /**
     * Write a cell renderer component c to device d. Before the component
     * is drawn it's reparented to this (if that's neccessary).
     * The Container p is the component we're actually drawing on, typically it's
     * equal to this.getParent().
     */
    public void writeComponent(Device d, SComponent c, SContainer p)
        throws IOException
    {
        if ( c == null ) {
            return;
        }

        if ( c.getParent() != this ) {
            this.add(c);
        }

        c.write(d);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
