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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
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
    private static final String cgClassID = "ContainerCG";

    /**
     * The name says all I think
     */
    protected SScrollBar horizontalScroller;

    /**
     * The name says all I think
     */
    protected SScrollBar verticalScroller;

    /**
     * The element which should be scrolled.
     */
    protected Scrollable scrollable;

    protected int horizontalExtent = 10;
    protected int verticalExtent = 10;

    /**
     * Sets the new viewport of the scrollable
     */
    AdjustmentListener adjustmentListener = new AdjustmentListener() {
        /**
         * TODO: documentation
         *
         * @param e
         */
        public void adjustmentValueChanged(AdjustmentEvent e) {
            if (scrollable != null) {
                Rectangle newViewport =
                    new Rectangle(horizontalScroller.getValue(),
                                  verticalScroller.getValue(),
                                  Math.min(horizontalScroller.getMaximum(),
                                           horizontalScroller.getValue() +
                                           horizontalScroller.getVisibleAmount()),
                                  Math.min(verticalScroller.getMaximum(),
                                           verticalScroller.getValue() +
                                           verticalScroller.getVisibleAmount()));
                scrollable.setViewportSize(newViewport);
            }
        }
    };

    /**
     * TODO: documentation
     *
     * @param c
     */
    public SScrollPane(SComponent c) {
        super.setLayout(new SBorderLayout());

        initScrollBars();
        add(c);
    }

    /**
     * generates and adds the scrollbars
     */
    protected void initScrollBars() {
        verticalScroller = new SScrollBar(SConstants.VERTICAL);
        super.addComponent(verticalScroller, SBorderLayout.EAST);
        verticalScroller.addAdjustmentListener(adjustmentListener);

        SPanel tmp = new SPanel(new SFlowLayout(CENTER));
        horizontalScroller = new SScrollBar(SConstants.HORIZONTAL);
        tmp.add(horizontalScroller);
        super.addComponent(tmp, SBorderLayout.SOUTH);
        horizontalScroller.addAdjustmentListener(adjustmentListener);
    }

    /**
     * Adjust the scrollbars to the scrollable
     */
    protected void adjustScrollBars() {
        if (scrollable != null) {
            //setScrollBarsVisible(true);

            Dimension scrollableViewport = scrollable.getScrollableViewportSize();
            verticalScroller.setMinimum(0);
            verticalScroller.setVisibleAmount(verticalExtent);
            verticalScroller.setMaximum(scrollableViewport.height);
            verticalScroller.setVisible(verticalExtent < scrollableViewport.height);

            horizontalScroller.setMinimum(0);
            horizontalScroller.setVisibleAmount(horizontalExtent);
            horizontalScroller.setMaximum(scrollableViewport.width);
            horizontalScroller.setVisible(horizontalExtent < scrollableViewport.width);
        }
        else {
            setScrollBarsVisible(false);
        }
    }

    /**
     * TODO: documentation
     *
     * @param b
     */
    protected void setScrollBarsVisible(boolean b) {
        horizontalScroller.setVisible(b);
        verticalScroller.setVisible(b);
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    protected void setScrollable(SComponent c) {
        if ( c instanceof Scrollable && c!=null ) {
            scrollable = (Scrollable)c;
        }
        else {
            scrollable = null;
        }
        adjustScrollBars();
    }

    public SComponent addComponent(SComponent c, Object constraint) {
        removeComponent((SComponent)scrollable);
        SComponent erg = super.addComponent(c, SBorderLayout.CENTER);
        setScrollable(erg);
        return erg;
    }

    /**
     * Only a null layout is allowed. This is because the SContainer tries to
     * set a null layout
     */
    public void setLayout(SLayoutManager l) {
        if ( l != null )
            throw new IllegalArgumentException("Cannot set Layout in SScrollPane");
    }


    /**
     * TODO: documentation
     *
     * @param s
     */
    public void write(Device s)
        throws IOException
    {
        if (visible) {
            adjustScrollBars();
            cg.write(s, this);
        }
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
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
