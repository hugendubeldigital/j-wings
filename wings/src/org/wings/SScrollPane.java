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
    implements javax.swing.ScrollPaneConstants
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
    
    protected static int horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_AS_NEEDED;
    protected static int verticalScrollBarPolicy = VERTICAL_SCROLLBAR_AS_NEEDED;
    
    protected SScrollBar verticalScrollBar = null;
    protected SScrollBar horizontalScrollBar = null;

    /**
     * Sets the new viewport of the scrollable
     */
    AdjustmentListener adjustmentListener;

	

    /**
     * TODO: documentation
     *
     * @param c
     */
    public SScrollPane() {

        setHorizontalScrollBar(new SScrollBar(SConstants.HORIZONTAL));
        setVerticalScrollBar(new SScrollBar(SConstants.VERTICAL));
        
        verticalScrollBar.addAdjustmentListener(getAdjustmentListener());
        horizontalScrollBar.addAdjustmentListener(getAdjustmentListener());
        
        // updateCG();
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public SScrollPane(SComponent c) {
        this();
        add(c);
        /*
        horizontalScrollBar = new SScrollBar(SConstants.HORIZONTAL);
        this.addComponent( horizontalScrollBar );

        SScrollBar verticalScrollBar = new SScrollBar(SConstants.VERTICAL);
        this.addComponent( verticalScrollBar );
        */
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

    /**
      * Creates a viewport if neccessary and then sets its view. Applications 
      * that don't provide the view directly to the SScrollPane constructor
      * should use this method to specify the scrollable child that's going
      * to be displayed in the scrollpane, e.g.:
      * <pre><code>
      *     SScrollPane scrollpane = new SScrollPane();
      *     scrollpane.setViewportView(myBigComponentToScroll);
      * </code></pre>
      * @param view the Component to add to the viewport
      */
    public void setViewportView(SComponent view) {
        addComponent(view, "");
    }



    /**
      * Add a new SComponent to this ScrollPane.
      * This behaves like JScrollPane.setViewportView, it replaces
      * the former component in this scrollpane.
      * @param c the component to scroll.
      * It should implement {@link org.wings.Scrollable}
      * @param constraint not used, any object but not <code>null</code>!
      */
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
        if (horizontalScrollBar != null)
            horizontalScrollBar.setVisibleAmount(horizontalExtent);
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
        if (verticalScrollBar != null)
            verticalScrollBar.setVisibleAmount(verticalExtent);
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
    
    /**
      * Returns the horizontal scroll bar.
      * @returns the scrollbar that controls the viewports horizontal view position
      */
	public SScrollBar getHorizontalScrollBar()
     {
		return horizontalScrollBar;
     }

    /**
      * Set the horizontal scroll bar.
      * @param sb the scrollbar that controls the viewports horizontal view position
      */
	public void setHorizontalScrollBar(SScrollBar sb)
     {
		if (horizontalScrollBar!=null) {
        	horizontalScrollBar.removeAdjustmentListener(this.getAdjustmentListener());
        	removeComponent(horizontalScrollBar);
		}
        horizontalScrollBar = sb;
        addComponent(horizontalScrollBar);
        horizontalScrollBar.addAdjustmentListener(this.getAdjustmentListener());
     }

    /**
      * Returns the horizontal scroll bar policy value.
      * @returns the horizontal scrollbar policy.
      * @see #setHorizontalScrollBarPolicy(int)
      */
	public int getHorizontalScrollBarPolicy()
     {
		return horizontalScrollBarPolicy;
     }

    /**
      * Returns the vertical scroll bar.
      * @returns the scrollbar that controls the viewports vertical view position
      */
	public SScrollBar getVerticalScrollBar()
     {
		return verticalScrollBar;
     }
     
    /**
      * Set the vertical scroll bar.
      * @param sb the scrollbar that controls the viewports vertical view position
      */
	public void setVerticalScrollBar(SScrollBar sb)
     {
		if (verticalScrollBar!=null) {
        	verticalScrollBar.removeAdjustmentListener(this.getAdjustmentListener());
        	removeComponent(verticalScrollBar);
		}
        verticalScrollBar = sb;
        addComponent(verticalScrollBar);
        verticalScrollBar.addAdjustmentListener(this.getAdjustmentListener());
     }

    /**
      * Returns the vertical scroll bar policy value.
      * @returns the vertical scrollbar policy.
      * @see #setVerticalScrollBarPolicy(int)
      */
	public int getVerticalScrollBarPolicy()
     {
		return verticalScrollBarPolicy;
     }
     
	/**
      * Determines when the horizontal scrollbar appears in the scrollpane. 
	  * The options are:
      * <li><code>SScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED</code></li>
      * <li><code>SScrollPane.HORIZONTAL_SCROLLBAR_NEVER</code></li>
      * <li><code>SScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS</code></li>
      */
	public void setHorizontalScrollBarPolicy( int policy)
     {
     	horizontalScrollBarPolicy = policy;
     }

	/**
      * Determines when the vertical scrollbar appears in the scrollpane. 
	  * The options are:
      * <li><code>SScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED</code></li>
      * <li><code>SScrollPane.VERTICAL_SCROLLBAR_NEVER</code></li>
      * <li><code>SScrollPane.VERTICAL_SCROLLBAR_ALWAYS</code></li>
      */
	public void setVerticalScrollBarPolicy( int policy)
     {
     	verticalScrollBarPolicy = policy;
     }

    /**
      * Set the preferred size for this scrollpane and
      * the scrollbars aswell.
      */
    public void setPreferredSize(SDimension dim) {
        super.setPreferredSize(dim);
        horizontalScrollBar.setPreferredSize(dim);
        verticalScrollBar.setPreferredSize(dim);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
