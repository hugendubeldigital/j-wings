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
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import org.wings.border.SEmptyBorder;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SPageScroller
    extends AbstractAdjustable
{
    /**
     * Default maximum amount of page buttons.
     */
    private static final int DEFAULT_DIRECT_PAGES = 10;

    /**
     *
     */
    private static final SEmptyBorder DEFAULT_DIRECT_BUTTON_BORDER = 
        new SEmptyBorder(5,5,5,5);

    /**
     * Access to the default Icons for buttons
     */
    private static final int FORWARD = 0;
    private static final int BACKWARD = 1;

    /**
     * Icons for both layout modes, both directions and enabled resp. disabled
     **/
    private final static SIcon[][][] DEFAULT_ICONS =
        new SIcon[2][2][SAbstractButton.ICON_COUNT];

    // Initialisiert (laedt) die Default Images
    static {
        String[] postfixes = new String[2];
        for ( int orientation=0; orientation<2; orientation++ ) {
            if ( orientation==SConstants.VERTICAL ) {
                postfixes[BACKWARD] = "Up";
                postfixes[FORWARD] = "Down";
            }
            else {
                postfixes[BACKWARD] = "Left";
                postfixes[FORWARD] = "Right";
            }

            for ( int direction=0; direction<postfixes.length; direction++ ) {
                DEFAULT_ICONS[orientation][direction][SAbstractButton.ENABLED_ICON] =
                    new ResourceImageIcon("org/wings/icons/Scroll" +
                                          postfixes[direction] + ".gif");
                DEFAULT_ICONS[orientation][direction][SAbstractButton.DISABLED_ICON] =
                    new ResourceImageIcon("org/wings/icons/DisabledScroll"
                                          + postfixes[direction] + ".gif");
                DEFAULT_ICONS[orientation][direction][SAbstractButton.PRESSED_ICON] =
                    new ResourceImageIcon("org/wings/icons/PressedScroll"
                                          + postfixes[direction] + ".gif");
                DEFAULT_ICONS[orientation][direction][SAbstractButton.ROLLOVER_ICON] =
                    new ResourceImageIcon("org/wings/icons/RolloverScroll"
                                          + postfixes[direction] + ".gif");
                DEFAULT_ICONS[orientation][direction][SAbstractButton.ROLLOVER_SELECTED_ICON] =
                    new ResourceImageIcon("org/wings/icons/RolloverSelectedScroll"
                                          + postfixes[direction] + ".gif");
            }
        }
    }


    /**
     * Actual amount of page buttons; depends on the number of elemnts in the
     * model and on the models extent.
     * @see setDirectPages()
     */
    protected int directPages = DEFAULT_DIRECT_PAGES;

    /*
     * how to layout the scroller, vertical or horizontal
     * @see #setLayoutMode
     */
    protected int layoutMode;
    
    /** */
    private final SButton forwardPage = new SButton();

    /** */
    private final SButton backwardPage = new SButton();

    /** */
    protected final SButtonGroup buttonGroup = new SButtonGroup();

    /** contains the direct page buttons. Size of this array is extend */
    protected SAbstractButton[] directPageButtons = null;

    /**
     * Icons for backward direction, the icon set of a button consists of 7 icons:
     * enabled, disabled, selected, disabled_selected, rollover,
     * rollover_selected, pressed. we
     * don't need selected here, but we support it
     **/
    private final SIcon[] backwardIcons = new SIcon[SAbstractButton.ICON_COUNT];

    /**
     * Icons for forward direction, the icon set of a button consists of 7 icons:
     * enabled, disabled, selected, disabled_selected, rollover,
     * rollover_selected, pressed. we 
     * don't need selected here, but we support it
     **/
    private final SIcon[] forwardIcons = new SIcon[SAbstractButton.ICON_COUNT];

    /**
     * Icons for both directions, the icon set of a button consists of 7 icons:
     * enabled, disabled, selected, disabled_selected, rollover,
     * rollover_selected, pressed.  
     **/
    private final SIcon[] directIcons = new SIcon[SAbstractButton.ICON_COUNT];

    /**
     * Creates a scrollbar with the specified orientation,
     * value, extent, mimimum, and maximum.
     * The "extent" is the size of the viewable area. It is also known
     * as the "visible amount".
     *
     * @exception IllegalArgumentException if orientation is not one of VERTICAL, HORIZONTAL
     *
     * @see #setOrientation
     * @see #setValue
     * @see #setVisibleAmount
     * @see #setMinimum
     * @see #setMaximum
     */
    public SPageScroller(int orientation, int value, int extent, int min, int max) {
        super(value, extent, min, max);

        setOrientation(orientation);

        setLayout(new SBorderLayout());

        forwardPage.addActionListener(scrollAction);

        backwardPage.addActionListener(scrollAction);

        buttonGroup.addActionListener(scrollAction);

        initScrollers();
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
    public SPageScroller(int orientation) {
        this(orientation, 0, 1, 0, 100);
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
    public SPageScroller() {
        this(SConstants.VERTICAL);
    }

    /**
     * set how to layout components
     * {@link VERTICAL} or {@link HORIZONTAL}
     **/
    public void setLayoutMode(int orientation) {
        switch (orientation) {
        case SConstants.VERTICAL:
        case SConstants.HORIZONTAL:
            layoutMode = orientation;
            resetIcons();
            break;
        default:
            throw new IllegalArgumentException("layout mode must be one of: VERTICAL, HORIZONTAL");
        }
    }

    /**
     * Sets the proper icons for buttonstatus enabled resp. disabled.
     */
    public void resetIcons() {
        for ( int i=0; i<forwardIcons.length; i++ ) {
            forwardIcons[i] = DEFAULT_ICONS[layoutMode][FORWARD][i];
        } 
        forwardPage.setIcons(forwardIcons);

        for ( int i=0; i<backwardIcons.length; i++ ) {
            backwardIcons[i] = DEFAULT_ICONS[layoutMode][BACKWARD][i];
        } 
        backwardPage.setIcons(backwardIcons);

        for ( int i=0; i<directIcons.length; i++ ) {
            directIcons[i] = null;
        }

        if ( directPageButtons!=null) {
            for ( int i=0; i<directPageButtons.length; i++ ) {
                directPageButtons[i].setIcons(directIcons);
            }
        }
    }

    /**
     * TODO: documentation
     */
    protected final ActionListener scrollAction = new ActionListener() {
        /**
         * TODO: documentation
         *
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            if ( e.getSource()==forwardPage ) {
                setValue(getValue()+getExtent());
            }
            else if ( e.getSource()==backwardPage ) {
                setValue(getValue()-getExtent());
            }
            else { // check all Page Buttons
                String command = e.getActionCommand();

                try {
                    int page = Integer.parseInt(command);
                    setValue(page*getExtent());
                } catch ( NumberFormatException ex ) {
                    // ignore action...
                }
            }
        }
    };

    /**
     * Sets the amount of page buttons to <code>count</code>.
     */
    public final int getDirectPages() {
        return directPages;
    }

    /**
     * Sets the amount of page buttons to <code>count</code>.
     * @param count : New amount of page buttons.
     */
    public void setDirectPages(int count) {
        if ( directPages!=count ) {
            directPages = count;
            initScrollers();
        }
    }

    /**
     * TODO: documentation
     *
     */
    protected SAbstractButton createDirectPageButton() {
        SButton result = new SButton();
        result.setBorder(DEFAULT_DIRECT_BUTTON_BORDER);
        return result;
    }

    /**
     * TODO: documentation
     *
     */
    protected void initScrollers() {
        // remove old:
        
        buttonGroup.removeAll();

        directPageButtons = new SAbstractButton[directPages];
        for (int i = 0; i<directPageButtons.length; i++) {
            directPageButtons[i] = createDirectPageButton();
            buttonGroup.add(directPageButtons[i]);
        }

        initLayout();
    }

    /**
     * TODO: documentation
     *
     */
    protected void initLayout() {
        removeAllComponents();

        SPanel middlePanel = null;
        if ( layoutMode==SConstants.VERTICAL) {
            add(backwardPage, SBorderLayout.NORTH);
            add(forwardPage, SBorderLayout.SOUTH);
            middlePanel = new SPanel(new SFlowDownLayout());
        }
        else {
            add(backwardPage, SBorderLayout.WEST);
            add(forwardPage, SBorderLayout.EAST);
            middlePanel = new SPanel(new SFlowLayout());
        }

        add(middlePanel, SBorderLayout.CENTER);

        // Buttons fuer die Seiten einfuegen
        for (int i = 0; i<directPageButtons.length; i++) {
            middlePanel.add(directPageButtons[i]);
        }


        refreshComponents();
    }

    protected String formatDirectPageLabel(int page) {
        return Integer.toString(page+1);
    }

    /**
     * TODO: documentation
     *
     */
    protected void refreshComponents() {
        // lower bound
        backwardPage.setEnabled(getValue() > getMinimum());

        // upper bound: maximum - extent
        forwardPage.setEnabled(getValue() < getMaximum()-getExtent());


        // overall pages 
        int pages = (getMaximum()+(getExtent()-1)-getMinimum())/getExtent();

        int actualPage = (getValue()-getMinimum()+getExtent()-1)/getExtent();

        // prefer forward
        int firstDirectPage = actualPage - (directPages-1)/2;
        firstDirectPage = Math.min(firstDirectPage, pages-directPages);
        firstDirectPage = Math.max(firstDirectPage, 0);

        // reset alle page buttons
        for ( int i=0; i<directPageButtons.length; i++ ) {
            directPageButtons[i].setVisible(false);
        }

        for ( int i=0; 
              i<Math.min(directPageButtons.length, pages-firstDirectPage);
              i++ ) {

            int page = firstDirectPage+i;

            directPageButtons[i].setText(formatDirectPageLabel(page));
            directPageButtons[i].setActionCommand(Integer.toString(page));
            directPageButtons[i].setVisible(true);

            if ( page==actualPage ) {
                directPageButtons[i].setEnabled(false);
                directPageButtons[i].setSelected(true);
            } else {
                directPageButtons[i].setEnabled(true);
                directPageButtons[i].setSelected(false);
            }
            
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */



