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
public class SFlowLayout
    implements SLayoutManager
{
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
    public SFlowLayout() {
    }

    /**
     * TODO: documentation
     *
     * @param alignment
     */
    public SFlowLayout(int alignment) {
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

    /*
     * Sets the alignment. Use one of the following types:
     * <UL>
     * <LI> {@link SConstants#LEFT_ALIGN}
     * <LI> {@link SConstants#CENTER_ALIGN}
     * <LI> {@link SConstants#RIGHT_ALIGN}
     * </UL>
     */
    /**
     * TODO: documentation
     *
     * @param a
     */
    public void setAlignment(int a) {
        align = a;
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
        if ( components.size()>0 ) {
            switch (align) {
            case SConstants.RIGHT_ALIGN:
                s.append("\n<DIV ALIGN=RIGHT>");
                break;
            case SConstants.CENTER_ALIGN:
                s.append("\n<DIV ALIGN=CENTER>");
                break;
            }
            int count = 0;
            for ( int i=0; i<components.size(); i++ ) {
                SComponent comp = (SComponent)components.get(i);
                if ( comp.isVisible() ) {
                    if ( orientation==SConstants.VERTICAL && count>0 )
                        s.append("<BR>\n");
                    ((SComponent)components.get(i)).write(s);
                    count++;
                }
            }
            switch (align) {
            case SConstants.RIGHT_ALIGN:
            case SConstants.CENTER_ALIGN:
                s.append("\n</DIV>");
                break;
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
