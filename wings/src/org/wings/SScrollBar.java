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

import java.awt.Adjustable;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.Icon;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SScrollBar
    extends SContainer
    implements Adjustable, SConstants
{
    /**
     * TODO: documentation
     */
    public static final int UNIT = 0;

    /**
     * TODO: documentation
     */
    public static final int BLOCK = 1;

    /**
     * TODO: documentation
     */
    public static final int MARGIN = 2;

    // for access of the icons
    private static final int DISABLED = 0;
    private static final int ENABLED = 1;

    // for access of the icons
    private static final int FORWARD = 0;
    private static final int BACKWARD = 1;

    /**
     * All changes from the model are treated as though the user moved
     * the scrollbar knob.
     */
    private ChangeListener fwdAdjustmentEvents = new ModelListener();

    /**
     * The model that represents the scrollbar's minimum, maximum, extent
     * (aka "visibleAmount") and current value.
     * @see #setModel
     */
    protected BoundedRangeModel model;

    /**
     * @see #setOrientation
     */
    protected int orientation;

    /**
     * @see #setUnitIncrement
     */
    protected int unitIncrement;

    /**
     * @see #setBlockIncrement
     */
    protected int blockIncrement;

    /**
     * TODO: documentation
     */
    protected final ArrayList adjustmentListener = new ArrayList(2);

    // Jeweils 3 Buttons fuer jede Richtung (FORWARD und BACKWARD)
    transient protected SButton[][] buttons = new SButton[3][2];

    // 2 Orientierungen, 3 Typen (Unit, Block, Margin), 2 Richtungen (FORWARD,
    // BACKWARD) und jeweils enabled und disabled.
    private final static ResourceImageIcon[][][][] DEFAULT_ICONS =
        new ResourceImageIcon[2][3][2][2];

    // Initialisiert (laedt) die Default Images
    static {
        String[] prefixes = {"", "Block", "Margin"};
        String[] postfixes = new String[2];
        for ( int orientation=0; orientation<2; orientation++ ) {
            for ( int style=0; style<prefixes.length; style++ ) {
                if ( orientation==SConstants.VERTICAL ) {
                    postfixes[BACKWARD] = "Up";
                    postfixes[FORWARD] = "Down";
                }
                else {
                    postfixes[BACKWARD] = "Left";
                    postfixes[FORWARD] = "Right";
                }

                for ( int direction=0; direction<postfixes.length; direction++ ) {
                    DEFAULT_ICONS[orientation][style][direction][ENABLED] =
                        new ResourceImageIcon("icons/" + prefixes[style] +
                                              "Scroll" + postfixes[direction] +
                                              ".gif");
                    DEFAULT_ICONS[orientation][style][direction][DISABLED] =
                        new ResourceImageIcon("icons/Disabled" + prefixes[style] +
                                              "Scroll" + postfixes[direction] +
                                              ".gif");
                }
            }
        }
    }

    /**
     * Creates a scrollbar with the specified orientation,
     * value, extent, mimimum, and maximum.
     * The "extent" is the size of the viewable area. It is also known
     * as the "visible amount".
     * <p>
     * Note: Use <code>setBlockIncrement</code> to set the block
     * increment to a size slightly smaller than the view's extent.
     * That way, when the user jumps the knob to an adjacent position,
     * one or two lines of the original contents remain in view.
     *
     * @exception IllegalArgumentException if orientation is not one of VERTICAL, HORIZONTAL
     *
     * @see #setOrientation
     * @see #setValue
     * @see #setVisibleAmount
     * @see #setMinimum
     * @see #setMaximum
     */
    public SScrollBar(int orientation, int value, int extent, int min, int max) {
        super(new SBorderLayout());
        this.unitIncrement = 1;
        this.blockIncrement = (extent == 0) ? 1 : extent;
        this.orientation = orientation;
        this.model = new DefaultBoundedRangeModel(value, extent, min, max);
        this.model.addChangeListener(fwdAdjustmentEvents);

        initScrollers();
        checkOrientation(orientation);

        // removeStyle(UNIT);
        removeStyle(BLOCK);
        // removeStyle(MARGIN);
    }

    /**
     * Creates a scrollbar with the specified orientation
     * and the following initial values:
     * <pre>
     * minimum = 0
     * maximum = 100
     * value = 0
     * extent = 10
     * </pre>
     */
    public SScrollBar(int orientation) {
        this(orientation, 0, 10, 0, 100);
    }


    /**
     * Creates a vertical scrollbar with the following initial values:
     * <pre>
     * minimum = 0
     * maximum = 100
     * value = 0
     * extent = 10
     * </pre>
     */
    public SScrollBar() {
        this(SConstants.VERTICAL);
    }

    /**
     * TODO: documentation
     *
     */
    public void resetIcons() {
        for ( int style=0; style<DEFAULT_ICONS[orientation].length; style++ ) {
            for ( int direction=0; direction<DEFAULT_ICONS[orientation][style].length;
                  direction++ ) {
                buttons[style][direction].setIcon(DEFAULT_ICONS[orientation][style][direction][ENABLED]);
                buttons[style][direction].setDisabledIcon(DEFAULT_ICONS[orientation][style][direction][DISABLED]);
            }
        }
    }

    /**
     * TODO: documentation
     *
     * @param style
     */
    public void addStyle(int style) {
        for ( int i=0; i<buttons[style].length; i++ )
            buttons[style][i].setVisible(true);
    }

    /**
     * TODO: documentation
     *
     * @param style
     */
    public void removeStyle(int style) {
        for ( int i=0; i<buttons[style].length; i++ )
            buttons[style][i].setVisible(false);
    }

    private void checkOrientation(int orientation) {
        switch (orientation) {
        case SConstants.VERTICAL:
        case SConstants.HORIZONTAL:
            resetIcons();
            break;
        default:
            throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
        }
    }

    /**
     * TODO: documentation
     */
    transient protected ActionListener scrollerAction = new ActionListener() {
        /**
         * TODO: documentation
         *
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            model.setValueIsAdjusting(true);
            int change = 0;
            if ( e.getSource() == buttons[UNIT][FORWARD] ) {
                change = unitIncrement;
            }
            else if ( e.getSource() == buttons[BLOCK][FORWARD] ) {
                change = blockIncrement;
            }
            else if ( e.getSource() == buttons[MARGIN][FORWARD] ) {
                model.setValue(model.getMaximum()-model.getExtent());
            }
            else if ( e.getSource() == buttons[UNIT][BACKWARD] ) {
                change = -unitIncrement;
            }
            else if ( e.getSource() == buttons[BLOCK][BACKWARD] ) {
                change = -blockIncrement;
            }
            else if ( e.getSource() == buttons[MARGIN][BACKWARD] ) {
                model.setValue(model.getMinimum());
            }

            model.setValue(model.getValue()+change);
            model.setValueIsAdjusting(false);
        }
    };

    /**
     * TODO: documentation
     *
     */
    protected void initScrollers() {
        for ( int i=0; i<buttons.length; i++ ) {
            buttons[i][FORWARD] = new SButton("");
            buttons[i][FORWARD].addActionListener(scrollerAction);
            buttons[i][BACKWARD] = new SButton("");
            buttons[i][BACKWARD].addActionListener(scrollerAction);
        }
        initLayout();
    }

    /**
     * TODO: documentation
     *
     */
    protected void initLayout() {
        removeAllComponents();
        SPanel backward = null;
        SPanel forward = null;
        if ( orientation == SConstants.VERTICAL) {
            backward = new SPanel(new SFlowDownLayout());
            add(backward, SBorderLayout.NORTH);

            forward = new SPanel(new SFlowDownLayout());
            add(forward, SBorderLayout.SOUTH);
        }
        else {
            backward = new SPanel(new SFlowLayout());
            add(backward, SBorderLayout.WEST);

            forward = new SPanel(new SFlowLayout());
            add(forward, SBorderLayout.EAST);
        }

        for ( int i=0; i<buttons.length; i++ ) {
            forward.add(buttons[i][FORWARD]);
            backward.add(buttons[buttons.length-i-1][BACKWARD]);
        }
    }

    /**
     * TODO: documentation
     *
     */
    protected void setScrollButtonStatus() {
        for ( int i=0; i<buttons.length; i++ ) {
            if ( model.getValue() == model.getMinimum() ) {
                buttons[i][BACKWARD].setEnabled(false);
            }
            else {
                buttons[i][BACKWARD].setEnabled(true);
            }

            if ( model.getValue()+model.getExtent() == model.getMaximum() ) {
                buttons[i][FORWARD].setEnabled(false);
            }
            else {
                buttons[i][FORWARD].setEnabled(true);
            }
        }
    }


    /**
     * Returns the component's orientation (horizontal or vertical).
     *
     * @return VERTICAL or HORIZONTAL
     * @see #setOrientation
     * @see java.awt.Adjustable#getOrientation
     */
    public int getOrientation() {
        return orientation;
    }


    /**
     * Set the scrollbar's orientation to either VERTICAL or
     * HORIZONTAL.
     *
     * @exception IllegalArgumentException if orientation is not one of VERTICAL, HORIZONTAL
     * @see #getOrientation
     * @beaninfo
     *    preferred: true
     *        bound: true
     *    attribute: visualUpdate true
     *  description: The scrollbar's orientation.
     *         enum: VERTICAL JScrollBar.VERTICAL
     *               HORIZONTAL JScrollBar.HORIZONTAL
     */
    public void setOrientation(int orientation)
    {
        checkOrientation(orientation);
        this.orientation = orientation;
    }


    /**
     * Returns data model that handles the scrollbar's four
     * fundamental properties: minimum, maximum, value, extent.
     *
     * @see #setModel
     */
    public BoundedRangeModel getModel() {
        return model;
    }


    /**
     * Sets the model that handles the scrollbar's four
     * fundamental properties: minimum, maximum, value, extent.
     *
     * @see #getModel
     * @beaninfo
     *       bound: true
     *       expert: true
     * description: The scrollbar's BoundedRangeModel.
     */
    public void setModel(BoundedRangeModel newModel) {
        if ( model != null ) {
            model.removeChangeListener(fwdAdjustmentEvents);
        }
        model = newModel;
        if ( model != null ) {
            model.addChangeListener(fwdAdjustmentEvents);
        }
    }


    /**
     * Returns the amount to change the scrollbar's value by,
     * given a unit up/down request.  A ScrollBarUI implementation
     * typically calls this method when the user clicks on a scrollbar
     * up/down arrow and uses the result to update the scrollbar's
     * value.   Subclasses my override this method to compute
     * a value, e.g. the change required to scroll up or down one
     * (variable height) line text or one row in a table.
     * <p>
     * The JScrollPane component creates scrollbars (by default)
     * that override this method and delegate to the viewports
     * Scrollable view, if it has one.  The Scrollable interface
     * provides a more specialized version of this method.
     *
     * @param direction is -1 or 1 for up/down respectively
     * @return the value of the unitIncrement property
     * @see #setUnitIncrement
     * @see #setValue
     * @see Scrollable#getScrollableUnitIncrement
     */
    public int getUnitIncrement(int direction) {
        return unitIncrement;
    }


    /**
     * Sets the unitIncrement property.
     * @see #getUnitIncrement
     * @beaninfo
     *   preferred: true
     *       bound: true
     * description: The scrollbar's unit increment.
     */
    public void setUnitIncrement(int unitIncrement) {
        this.unitIncrement = unitIncrement;
    }


    /**
     * Returns the amount to change the scrollbar's value by,
     * given a block (usually "page") up/down request.  A ScrollBarUI
     * implementation typically calls this method when the user clicks
     * above or below the scrollbar "knob" to change the value
     * up or down by large amount.  Subclasses my override this
     * method to compute a value, e.g. the change required to scroll
     * up or down one paragraph in a text document.
     * <p>
     * The JScrollPane component creates scrollbars (by default)
     * that override this method and delegate to the viewports
     * Scrollable view, if it has one.  The Scrollable interface
     * provides a more specialized version of this method.
     *
     * @param direction is -1 or 1 for up/down respectively
     * @return the value of the blockIncrement property
     * @see #setBlockIncrement
     * @see #setValue
     * @see Scrollable#getScrollableBlockIncrement
     */
    public int getBlockIncrement(int direction) {
        return blockIncrement;
    }


    /**
     * Sets the blockIncrement property.
     * @see #getBlockIncrement()
     * @beaninfo
     *   preferred: true
     *       bound: true
     * description: The scrollbar's block increment.
     */
    public void setBlockIncrement(int blockIncrement) {
        this.blockIncrement = blockIncrement;
    }


    /**
     * For backwards compatibility with java.awt.Scrollbar.
     * @see Adjustable#getUnitIncrement
     * @see #getUnitIncrement(int)
     */
    public int getUnitIncrement() {
        return unitIncrement;
    }


    /**
     * For backwards compatibility with java.awt.Scrollbar.
     * @see Adjustable#getBlockIncrement
     * @see #getBlockIncrement(int)
     */
    public int getBlockIncrement() {
        return blockIncrement;
    }


    /**
     * Returns the scrollbar's value.
     * @return the model's value property
     * @see #setValue
     */
    public int getValue() {
        return getModel().getValue();
    }


    /**
     * Sets the scrollbar's value.  This method just forwards the value
     * to the model.
     *
     * @see #getValue
     * @see BoundedRangeModel#setValue
     * @beaninfo
     *   preferred: true
     *       bound: true
     * description: The scrollbar's current value.
     */
    public void setValue(int value) {
        BoundedRangeModel m = getModel();
        m.setValue(value);
    }


    /**
     * Returns the scrollbar's extent, aka its "visibleAmount".  In many
     * scrollbar look and feel implementations the size of the
     * scrollbar "knob" or "thumb" is proportional to the extent.
     *
     * @return the value of the model's extent property
     * @see #setVisibleAmount
     */
    public int getVisibleAmount() {
        return getModel().getExtent();
    }


    /**
     * Set the model's extent property.
     *
     * @see #getVisibleAmount
     * @see BoundedRangeModel#setExtent
     * @beaninfo
     *   preferred: true
     * description: The amount of the view that is currently visible.
     */
    public void setVisibleAmount(int extent) {
        getModel().setExtent(extent);
    }


    /**
     * Returns the minimum value supported by the scrollbar
     * (usually zero).
     *
     * @return the value of the model's minimum property
     * @see #setMinimum
     */
    public int getMinimum() {
        return getModel().getMinimum();
    }


    /**
     * Sets the model's minimum property.
     *
     * @see #getMinimum
     * @see BoundedRangeModel#setMinimum
     * @beaninfo
     *   preferred: true
     * description: The scrollbar's minimum value.
     */
    public void setMinimum(int minimum) {
        getModel().setMinimum(minimum);
    }


    /**
     * The maximum value of the scrollbar is maximum - extent.
     *
     * @return the value of the model's maximum property
     * @see #setMaximum
     */
    public int getMaximum() {
        return getModel().getMaximum();
    }


    /**
     * Sets the model's maximum property.  Note that the scrollbar's value
     * can only be set to maximum - extent.
     *
     * @see #getMaximum
     * @see BoundedRangeModel#setMaximum
     * @beaninfo
     *   preferred: true
     * description: The scrollbar's maximum value.
     */
    public void setMaximum(int maximum) {
        getModel().setMaximum(maximum);
    }


    /**
     * True if the scrollbar knob is being dragged.
     *
     * @return the value of the model's valueIsAdjusting property
     * @see #setValueIsAdjusting
     */
    public boolean getValueIsAdjusting() {
        return getModel().getValueIsAdjusting();
    }


    /**
     * Sets the model's valueIsAdjusting property.  Scrollbar look and
     * feel implementations should set this property to true when
     * a knob drag begins, and to false when the drag ends.  The
     * scrollbar model will not generate ChangeEvents while
     * valueIsAdjusting is true.
     *
     * @see #getValueIsAdjusting
     * @see BoundedRangeModel#setValueIsAdjusting
     * @beaninfo
     *      expert: true
     *       bound: true
     * description: True if the scrollbar thumb is being dragged.
     */
    public void setValueIsAdjusting(boolean b) {
        BoundedRangeModel m = getModel();
        m.setValueIsAdjusting(b);
    }


    /**
     * Sets the four BoundedRangeModel properties after forcing
     * the arguments to obey the usual constraints:
     * <pre>
     * minimum <= value <= value+extent <= maximum
     * </pre>
     * <p>
     *
     * @see BoundedRangeModel#setRangeProperties
     * @see #setValue
     * @see #setVisibleAmount
     * @see #setMinimum
     * @see #setMaximum
     */
    public void setValues(int newValue, int newExtent, int newMin, int newMax)
    {
        BoundedRangeModel m = getModel();
        m.setRangeProperties(newValue, newExtent, newMin, newMax, m.getValueIsAdjusting());
    }


    /**
     * Adds an AdjustmentListener.  Adjustment listeners are notified
     * each time the scrollbar's model changes.  Adjustment events are
     * provided for backwards compatability with java.awt.Scrollbar.
     * <p>
     * Note that the AdjustmentEvents type property will always have a
     * placeholder value of AdjustmentEvent.TRACK because all changes
     * to a BoundedRangeModels value are considered equivalent.  To change
     * the value of a BoundedRangeModel one just sets its value property,
     * i.e. model.setValue(123).  No information about the origin of the
     * change, e.g. it's a block decrement, is provided.  We don't try
     * fabricate the origin of the change here.
     *
     * @param l the AdjustmentLister to add
     * @see #removeAdjustmentListener
     * @see BoundedRangeModel#addChangeListener
     */
    public void addAdjustmentListener(AdjustmentListener l) {
        adjustmentListener.add(l);
    }


    /**
     * Removes an AdjustmentEvent listener.
     *
     * @param l the AdjustmentLister to remove
     * @see #addAdjustmentListener
     */
    public void removeAdjustmentListener(AdjustmentListener l) {
        adjustmentListener.remove(l);
    }


    /*
     * Notify listeners that the scrollbar's model has changed.
     *
     * @see #addAdjustmentListener
     * @see EventListenerList
     */
    protected void fireAdjustmentValueChanged(int id, int type, int value) {
        AdjustmentEvent e = null;
        for ( int i=0; i<adjustmentListener.size(); i++ ) {
            if ( e == null ) {
                e = new AdjustmentEvent(this, id, type, value);
            }
            ((AdjustmentListener)adjustmentListener.get(i)).adjustmentValueChanged(e);
        }
    }


    /**
     * This class listens to ChangeEvents on the model and forwards
     * AdjustmentEvents for the sake of backwards compatibility.
     * Unfortunately there's no way to determine the proper
     * type of the AdjustmentEvent as all updates to the model's
     * value are considered equivalent.
     */
    private class ModelListener implements ChangeListener, Serializable {
        /**
         * TODO: documentation
         *
         * @param e
         */
        public void stateChanged(ChangeEvent e)   {
            int id = AdjustmentEvent.ADJUSTMENT_VALUE_CHANGED;
            int type = AdjustmentEvent.TRACK;
            fireAdjustmentValueChanged(id, type, getValue());
            setScrollButtonStatus();
        }
    }

    /**
     * Enables the component so that the knob position can be changed.
     * When the disabled, the knob position cannot be changed.
     *
     * @param b a boolean value, where true enables the component and
     *          false disables it
     */
    public void setEnabled(boolean x)  {
        super.setEnabled(x);
        SComponent[] children = getComponents();
        for(int i = 0; i < children.length; i++) {
            children[i].setEnabled(x);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
