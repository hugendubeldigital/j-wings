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
import java.util.ArrayList;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SFlowDownLayout
    implements SLayoutManager
{
    /**
     * TODO: documentation
     */
    protected ArrayList components = new ArrayList(2);

    /**
     * TODO: documentation
     *
     */
    public SFlowDownLayout() {}

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
     * @param s
     * @throws IOException
     */
    public void write(Device s)
        throws IOException
    {
        for ( int i=0; i<components.size(); i++ ) {
            SComponent comp = (SComponent)components.get(i);
            if ( comp.isVisible() ) {
                comp.write(s);
                s.append("\n<BR>\n");
            }
        }

    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setContainer(SContainer c) {
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
