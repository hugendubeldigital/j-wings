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

import java.awt.*;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.io.IOException;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SScrollPane
    extends SContainer
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "ScrollPaneCG";

    /**
     * The element which should be scrolled.
     */
    protected Scrollable scrollable;

    protected int horizontalExtent = 10;
    protected int verticalExtent = 10;

    /**
     * Sets the new viewport of the scrollable
     */
    AdjustmentListener adjustmentListener;

    /**
     * TODO: documentation
     *
     * @param c
     */
    public SScrollPane(SComponent c) {
        add(c);
    }

    public AdjustmentListener getAdjustmentListener() {
        if (adjustmentListener == null) {
            adjustmentListener = new AdjustmentListener() {
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        if (scrollable != null) {
                            Adjustable adjustable = e.getAdjustable();
                            Rectangle viewport = scrollable.getViewportSize();

                            if (adjustable.getOrientation() == Adjustable.HORIZONTAL) {
                                viewport.x     = adjustable.getValue();
                                viewport.width = Math.min(adjustable.getMaximum(),
                                                          adjustable.getValue() +
                                                          adjustable.getVisibleAmount());
                            }
                            else {
                                viewport.y      = adjustable.getValue();
                                viewport.height = Math.min(adjustable.getMaximum(),
                                                           adjustable.getValue() +
                                                           adjustable.getVisibleAmount());
                            }
                            scrollable.setViewportSize(viewport);
                        }
                    }
                };
        }
        return adjustmentListener;
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    protected void setScrollable(SComponent c) {
        if (c instanceof Scrollable && c != null) {
            scrollable = (Scrollable)c;
            Rectangle viewport = new Rectangle(0, 0, horizontalExtent, verticalExtent);
            scrollable.setViewportSize(viewport);
        }
        else {
            scrollable = null;
        }
    }

    public Scrollable getScrollable() { return scrollable; }

    public SComponent addComponent(SComponent c, Object constraint) {
        SComponent ret;
        if (c instanceof Scrollable) {
            removeComponent((SComponent)scrollable);
            ret = super.addComponent(c, constraint, 0);
            setScrollable(ret);
        }
        else {
            ret = super.addComponent(c, constraint);
        }
        return ret;
    }


    /**
     * TODO: documentation
     *
     * @param s
     */
    public void write(Device d)
        throws IOException
    {
        if (visible)
            cg.write(d, this);
    }

    /**
     * TODO: documentation
     *
     * @param horizontalExtent
     */
    public void setHorizontalExtent(int horizontalExtent) {
        this.horizontalExtent = horizontalExtent;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getHorizontalExtent() {
        return horizontalExtent;
    }

    /**
     * TODO: documentation
     *
     * @param verticalExtent
     */
    public void setVerticalExtent(int verticalExtent) {
        this.verticalExtent = verticalExtent;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getVerticalExtent() {
        return verticalExtent;
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "ContainerCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(ScrollPaneCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
