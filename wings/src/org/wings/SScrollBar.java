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

import java.awt.Adjustable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.io.Serializable;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.Icon;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SScrollBar
    extends SAbstractAdjustable
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "ScrollBarCG";


    /**
     * Access to the default Icons for buttons
     */
    private static final int FORWARD = 0;
    private static final int BACKWARD = 1;

    // Jeweils 3 Buttons fuer jede Richtung (FORWARD und BACKWARD)
    transient protected SAbstractButton[][] buttons = new SAbstractButton[3][2];

    // 2 Orientierungen, 3 Typen (Unit, Block, Margin), 2 Richtungen (FORWARD,
    // BACKWARD) und jeweils enabled und disabled.
    private final static SIcon[][][][] DEFAULT_ICONS =
        new SIcon[2][3][2][SAbstractButton.ICON_COUNT];

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
                    DEFAULT_ICONS[orientation][style][direction][SAbstractButton.ENABLED_ICON] =
                        new ResourceImageIcon("org/wings/icons/" 
                                              + prefixes[style] + "Scroll"
                                              + postfixes[direction] + ".gif");

                    DEFAULT_ICONS[orientation][style][direction][SAbstractButton.DISABLED_ICON] =
                        new ResourceImageIcon("org/wings/icons/Disabled" 
                                              + prefixes[style] + "Scroll"
                                              + postfixes[direction] + ".gif");

                    DEFAULT_ICONS[orientation][style][direction][SAbstractButton.PRESSED_ICON] =
                        new ResourceImageIcon("org/wings/icons/Pressed" 
                                              + prefixes[style] + "Scroll"
                                              + postfixes[direction] + ".gif");

                    DEFAULT_ICONS[orientation][style][direction][SAbstractButton.ROLLOVER_ICON] =
                        new ResourceImageIcon("org/wings/icons/Rollover" 
                                              + prefixes[style] + "Scroll"
                                              + postfixes[direction] + ".gif");

                    DEFAULT_ICONS[orientation][style][direction][SAbstractButton.ROLLOVER_SELECTED_ICON] =
                        new ResourceImageIcon("org/wings/icons/RolloverSelected" 
                                              + prefixes[style] + "Scroll"
                                              + postfixes[direction] + ".gif");
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
        super(value, extent, min, max);

        initScrollers();

        setOrientation(orientation);
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
                buttons[style][direction].setIcons(DEFAULT_ICONS[orientation][style][direction]);
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

    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        resetIcons();
        initLayout();
    }

    /**
     * TODO: documentation
     */
    protected final ActionListener scrollerAction = new ActionListener() {
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
            buttons[i][FORWARD] = new SButton();
            buttons[i][FORWARD].addActionListener(scrollerAction);
            buttons[i][BACKWARD] = new SButton();
            buttons[i][BACKWARD].addActionListener(scrollerAction);
        }
    }

    /**
     * TODO: documentation
     *
     */
    protected void initLayout()
    {
    /*
	*/
        removeAllComponents();
        SPanel backward = null;
        SPanel forward = null;
        if ( orientation == SConstants.VERTICAL) {
            backward = new SPanel(new SFlowDownLayout() );
            add( backward );

            forward = new SPanel(new SFlowDownLayout() );
            add( forward );
        }
        else {
            backward = new SPanel(new SFlowLayout() );
            add( backward );

            forward = new SPanel(new SFlowLayout() );
            add( forward );
        }

        for ( int i=0; i<buttons.length; i++ )
        {
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
     * TODO: documentation
     *
     */
    protected void refreshComponents() {
        setScrollButtonStatus();
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

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(ScrollBarCG cg) {
        super.setCG(cg);
    }

    public String toString()
    {
        return "SScrollBar[orientation=" +
            ((orientation == SComponent.HORIZONTAL)?"horizontal":"vertical") + "]";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
