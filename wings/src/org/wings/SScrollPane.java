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

    /***
     * the viewportView component
     ***/
    private SComponent viewComponent;
    
    /**
     * store the scrollables viewport. Scrollable is reset to this value, if it
     * is removed from the this scrollpane
     */
    protected Rectangle backupViewport;

    /**
     * The element which should be scrolled.
     */
    protected Scrollable scrollable;

    /**
     *
     */
    protected int horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_AS_NEEDED;

    /**
     *
     */
    protected int verticalScrollBarPolicy = VERTICAL_SCROLLBAR_AS_NEEDED;
    
    /**
     *
     */
    protected Adjustable verticalScrollBar = null;

    /**
     *
     */
    protected Adjustable horizontalScrollBar = null;

    /**
     * 
     */
    protected AdjustmentListener adjustmentListener;

    /**
     *
     */
    protected int horizontalExtent = 10;

    /**
     *
     */
    protected int verticalExtent = 10;

    /**
     * TODO: documentation
     *
     * @param c
     */
    public SScrollPane() {
        super(new SBorderLayout());

        setHorizontalScrollBar(new SScrollBar(SConstants.HORIZONTAL));
        setVerticalScrollBar(new SScrollBar(SConstants.VERTICAL));
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public SScrollPane(SComponent c) {
        this();

        setViewportView(c);
    }

    public AdjustmentListener getAdjustmentListener() {
        if (adjustmentListener == null) {
            adjustmentListener = new AdjustmentListener() {
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        if (scrollable != null) {
                            synchronizeViewport();
                        }
                    }
                };
        }
        return adjustmentListener;
    }

    /**
     * synchronize viewport of scrollable with settings of adjustables
     */
    public void synchronizeAdjustables() {
        if ( scrollable==null ) return;

        Rectangle viewport = scrollable.getViewportSize();
        Rectangle maxViewport = scrollable.getScrollableViewportSize();

        if ( viewport==null ) viewport = maxViewport;

        if ( getHorizontalScrollBar()!=null &&
             getHorizontalScrollBarPolicy()!=HORIZONTAL_SCROLLBAR_NEVER ) {
            adjustAdjustable(getHorizontalScrollBar(), viewport, 
                             maxViewport);
        }        

        if ( getVerticalScrollBar()!=null &&
             getVerticalScrollBarPolicy()!=VERTICAL_SCROLLBAR_NEVER ) {
            adjustAdjustable(getVerticalScrollBar(), viewport, 
                             maxViewport);
        }

    }

    protected void adjustAdjustable(Adjustable adjustable, Rectangle viewport, 
                                    Rectangle maxViewport) {
        if (adjustable.getOrientation() == Adjustable.HORIZONTAL) {
            adjustable.setValue(viewport.x);
            adjustable.setMaximum(maxViewport.x+maxViewport.width);
            adjustable.setMinimum(maxViewport.x);
            adjustable.setVisibleAmount(getHorizontalExtent());
        } else if (adjustable.getOrientation() == Adjustable.VERTICAL) {
            adjustable.setValue(viewport.y);
            adjustable.setMaximum(maxViewport.y+maxViewport.height);
            adjustable.setMinimum(maxViewport.y);
            adjustable.setVisibleAmount(getVerticalExtent());
        }
    }

    /**
     * synchronize viewport of scrollable with settings of adjustables
     */
    public void synchronizeViewport() {
        Rectangle viewport = scrollable.getScrollableViewportSize();
        
        // should never happen. If it happen we got a serious problem, because
        // we cannot determine what to scroll...
        if ( viewport==null ) viewport = new Rectangle();

        if ( getHorizontalScrollBar()!=null &&
             getHorizontalScrollBarPolicy()!=HORIZONTAL_SCROLLBAR_NEVER ) {
            adjustViewport(viewport, getHorizontalScrollBar());
        } 

        if ( getVerticalScrollBar()!=null &&
             getVerticalScrollBarPolicy()!=VERTICAL_SCROLLBAR_NEVER ) {
            adjustViewport(viewport, getVerticalScrollBar());
        }

        scrollable.setViewportSize(viewport);
    }

    protected void adjustViewport(Rectangle viewport, Adjustable adjustable) {
        if (adjustable.getOrientation() == Adjustable.HORIZONTAL) {
            int extent     = getHorizontalExtent();

            viewport.x     = Math.min(adjustable.getMaximum(),
                                      adjustable.getValue());
            viewport.x     = Math.max(adjustable.getMinimum(), 
                                      viewport.x);
            viewport.width = Math.min(adjustable.getMaximum()-viewport.x,
                                      extent);
            viewport.width = Math.max(0, viewport.width);
        }
        else {
            int extent     = getVerticalExtent();

            viewport.y     = Math.min(adjustable.getMaximum(),
                                      adjustable.getValue());
            viewport.y     = Math.max(adjustable.getMinimum(), 
                                      viewport.y);
            viewport.height = Math.min(adjustable.getMaximum()-viewport.y,
                                       extent);
            viewport.height = Math.max(0, viewport.height);
        }    

    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    protected void setScrollable(SComponent c) {
        if ( scrollable!=null ) {
            // reset to original value
            scrollable.setViewportSize(backupViewport);
        }

        if (c instanceof Scrollable && c != null) {
            scrollable = (Scrollable)c;

            // keep original value
            backupViewport = scrollable.getViewportSize();

            synchronizeViewport();
        } else {
            scrollable = null;
        }

        reload(ReloadManager.RELOAD_CODE);
    }

    public final Scrollable getScrollable() { 
        return scrollable; 
    }

    /**
     * only {@link Scrollable scrollables} are allowed here!
     */
    public SComponent addComponent(SComponent c, Object constraint, int index) {
        if (c instanceof Scrollable) {
            remove((SComponent)scrollable);
            SComponent ret = super.addComponent(c, SBorderLayout.CENTER, index);
            setScrollable(ret);
            return ret;
        } else {
            throw new IllegalArgumentException("Only Scrollables are allowed");
        }
    }

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
    public Adjustable getHorizontalScrollBar() {
        return horizontalScrollBar;
    }

    /**
     * Set the horizontal scroll bar.
     * @param sb the scrollbar that controls the viewports horizontal view position
     */
    public void setHorizontalScrollBar(Adjustable sb) {
        setHorizontalScrollBar(sb, SBorderLayout.SOUTH);
    }

    /**
     * Set the horizontal scroll bar.
     * @param constraint the constraint for the {@link LayoutManager} of this 
     * {@link SContainer}. The {@link LayoutManager} is per default 
     * {@link SBorderLayout}. 
     */
    public void setHorizontalScrollBar(Adjustable sb, String constraint) {
        if (horizontalScrollBar!=null) {
            horizontalScrollBar.removeAdjustmentListener(getAdjustmentListener());
            if ( horizontalScrollBar instanceof SComponent )
                remove((SComponent)horizontalScrollBar);
        }
        horizontalScrollBar = sb;

        if ( horizontalScrollBar!=null ) {
            if ( horizontalScrollBar instanceof SComponent ) {
                super.addComponent((SComponent)horizontalScrollBar,
                                   constraint, 
                                   getComponentCount());
            }

            synchronizeAdjustables();
            horizontalScrollBar.addAdjustmentListener(getAdjustmentListener());
        }

        reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * Returns the horizontal scroll bar policy value.
     * @returns the horizontal scrollbar policy. 
     * @see #setHorizontalScrollBarPolicy(int)
     */
    public final int getHorizontalScrollBarPolicy() {
        return horizontalScrollBarPolicy;
    }

    /**
     * Returns the vertical scroll bar.
     * @returns the scrollbar that controls the viewports vertical view position
     */
    public final Adjustable getVerticalScrollBar() {
        return verticalScrollBar;
    }

    /**
     * Set the vertical scroll bar.
     * @param sb the scrollbar that controls the viewports vertical view position
     */
    public void setVerticalScrollBar(Adjustable sb) {
        setVerticalScrollBar(sb, SBorderLayout.EAST);
    }

    /**
     * Set the vertical scroll bar.
     * @param sb the scrollbar that controls the viewports vertical view position
     * @param constraint the constraint for the {@link LayoutManager} of this 
     * {@link SContainer}. The {@link LayoutManager} is per default 
     * {@link SBorderLayout}. 
     */
    public void setVerticalScrollBar(Adjustable sb, String constraint) {
        if (verticalScrollBar!=null) {
            verticalScrollBar.removeAdjustmentListener(getAdjustmentListener());
            if ( verticalScrollBar instanceof SComponent )
                remove((SComponent)verticalScrollBar);
        }

        verticalScrollBar = sb;

        if ( verticalScrollBar!=null ) {
            if ( verticalScrollBar instanceof SComponent ) {
                super.addComponent((SComponent)verticalScrollBar,
                                   constraint,
                                   getComponentCount());
            }

            synchronizeAdjustables();
            verticalScrollBar.addAdjustmentListener(getAdjustmentListener());
        }

        reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * Returns the vertical scroll bar policy value.
     * @returns the vertical scrollbar policy.
     * @see #setVerticalScrollBarPolicy(int)
     */
    public final int getVerticalScrollBarPolicy() {
        return verticalScrollBarPolicy;
    }

    /**
     * Determines when the horizontal scrollbar appears in the scrollpane.
     * The options are:
     * <li><code>SScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED</code></li>
     * <li><code>SScrollPane.HORIZONTAL_SCROLLBAR_NEVER</code></li>
     * <li><code>SScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS</code></li>
     */
    public void setHorizontalScrollBarPolicy(int policy) {
        if ( policy!=horizontalScrollBarPolicy ) {
            horizontalScrollBarPolicy = policy;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * Determines when the vertical scrollbar appears in the scrollpane.
     * The options are:
     * <li><code>SScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED</code></li>
     * <li><code>SScrollPane.VERTICAL_SCROLLBAR_NEVER</code></li>
     * <li><code>SScrollPane.VERTICAL_SCROLLBAR_ALWAYS</code></li>
     */
    public void setVerticalScrollBarPolicy(int policy) {
        if ( policy!=verticalScrollBarPolicy) {
            verticalScrollBarPolicy = policy;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * Set the preferred size for this scrollpane and
     * the scrollbars aswell.
     */
    public void setPreferredSize(SDimension dim) {
        super.setPreferredSize(dim);
        if ( horizontalScrollBar instanceof SComponent ) {
            ((SComponent)horizontalScrollBar).setPreferredSize(dim);
        }
        if ( verticalScrollBar instanceof SComponent ) {
            ((SComponent)verticalScrollBar).setPreferredSize(dim);
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
     public final int getHorizontalExtent() {
         if ( scrollable!=null ) {
             Dimension preferredExtent = scrollable.getPreferredExtent();
             if ( preferredExtent!=null && preferredExtent.width>0 &&
                  preferredExtent.width<horizontalExtent ) {
                 return preferredExtent.width;
             }
         }
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
     public final int getVerticalExtent() {
         if ( scrollable!=null ) {
             Dimension preferredExtent = scrollable.getPreferredExtent();
             if ( preferredExtent!=null && preferredExtent.height>0 &&
                  preferredExtent.height<verticalExtent ) {
                 return preferredExtent.height;
             }
         }

         return verticalExtent;
     }

     /**
     * Sets the viewportComponent
     * if there already exists one, it will be removed first
     *
     * @param view the component to add to the viewport
     */
    public void setViewportView(SComponent view) {
        if (viewComponent != null) {
            remove(viewComponent);
        }
        add(view);
        viewComponent = view;;
    }


}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
