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

import java.awt.Color;
import java.io.IOException;

import javax.swing.ListSelectionModel;

import org.wings.io.Device;
import org.wings.plaf.*;
import org.wings.style.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SDefaultTableRowSelectionRenderer 
    implements STableCellRenderer
{

    public static final ResourceImageIcon DEFAULT_MULTI_SELECTION_ICON =
        new ResourceImageIcon("org/wings/icons/SelectedCheckBox.gif");
    
    public static final ResourceImageIcon DEFAULT_MULTI_NOT_SELECTION_ICON =
        new ResourceImageIcon("org/wings/icons/NotSelectedCheckBox.gif");

    public static final ResourceImageIcon DEFAULT_SINGLE_SELECTION_ICON =
        new ResourceImageIcon("org/wings/icons/SelectedRadioButton.gif");

    public static final ResourceImageIcon DEFAULT_SINGLE_NOT_SELECTION_ICON =
        new ResourceImageIcon("org/wings/icons/NotSelectedRadioButton.gif");

    /** Color to use for the foreground for selected nodes. */
    protected Color foregroundSelectionColor = null;

    /** Color to use for the foreground for non-selected nodes. */
    protected Color foregroundNonSelectionColor = null;

    /** Color to use for the background when a node is selected. */
    protected Color backgroundSelectionColor = null;

    /** Color to use for the background when the node isn't selected. */
    protected Color backgroundNonSelectionColor = null;

    /** Style to use for the foreground for selected nodes. */
    protected Style selectionStyle = null;

    /** Style to use for the foreground for non-selected nodes. */
    protected Style nonSelectionStyle = null;

    /** */
    protected final SLabel selectionIcon = new SLabel();

    /** */
    protected final SAnchor selectionAnchor = new SAnchor();

    protected ResourceImageIcon multiSelectionIcon = DEFAULT_MULTI_SELECTION_ICON;

    protected ResourceImageIcon multiNotSelectionIcon =
        DEFAULT_MULTI_NOT_SELECTION_ICON;

    protected ResourceImageIcon singleSelectionIcon = DEFAULT_SINGLE_SELECTION_ICON;

    protected ResourceImageIcon singleNotSelectionIcon =
        DEFAULT_SINGLE_NOT_SELECTION_ICON; 

    /**
     * TODO: documentation
     *
     */
    public SDefaultTableRowSelectionRenderer() {
        selectionAnchor.add(selectionIcon);
    }

    public SComponent getTableCellRendererComponent(SBaseTable baseTable,
                                                    Object value,
                                                    boolean selected,
                                                    int row,
                                                    int col)
    {

        STable table = (STable)baseTable;
        
        switch ( table.getSelectionMode() ) {
        case ListSelectionModel.SINGLE_SELECTION:
            selectionIcon.setIcon(selected ? singleSelectionIcon :
                                      singleNotSelectionIcon);
            break;
        default:
            selectionIcon.setIcon(selected ? multiSelectionIcon :
                                  multiNotSelectionIcon);
            break;
        } 

        selectionAnchor.setReference("?" + table.getNamePrefix() + "="  + 
                                     table.getSelectionToggleParameter(row,col));

        // style
        if (selected) {
            if (table.getSelectionBackground() == null) {
                selectionAnchor.setBackground(backgroundSelectionColor);
            }
            else {
                selectionAnchor.setBackground(table.getSelectionBackground());
            }
            if (table.getSelectionForeground() == null) {
                selectionAnchor.setForeground(foregroundSelectionColor);
            }
            else {
                selectionAnchor.setForeground(table.getSelectionForeground());
            }
            selectionAnchor.setStyle(selectionStyle);
        }
        else {
            selectionAnchor.setBackground(backgroundNonSelectionColor);
            selectionAnchor.setForeground(foregroundNonSelectionColor);
            selectionAnchor.setStyle(nonSelectionStyle);
        }

        return selectionAnchor;
    }

    /**
     * Sets the color the foreground is drawn with when the cell is selected.
     *
     * @param newColor
     */
    public void setForegroundSelectionColor(Color newColor) {
        foregroundSelectionColor = newColor;
    }

    /**
     * Returns the color the foreground is drawn with when the cell is selected.
     *
     * @return
     */
    public Color getForegroundSelectionColor() {
        return foregroundSelectionColor;
    }

    /**
     * Sets the color the foreground is drawn with when the cell isn't selected.
     *
     * @param newColor
     */
    public void setForegroundNonSelectionColor(Color newColor) {
        foregroundNonSelectionColor = newColor;
    }

    /**
     * Returns the color the foreground is drawn with when the cell isn't selected.
     *
     * @return
     */
    public Color getForegroundNonSelectionColor() {
        return foregroundNonSelectionColor;
    }

    /**
     * Sets the color to use for the background if cell is selected.
     *
     * @param newColor
     */
    public void setBackgroundSelectionColor(Color newColor) {
        backgroundSelectionColor = newColor;
    }


    /**
     * Returns the color to use for the background if cell is selected.
     *
     * @return
     */
    public Color getBackgroundSelectionColor() {
        return backgroundSelectionColor;
    }

    /**
     * Sets the background color to be used for non selected cells.
     *
     * @param newColor
     */
    public void setBackgroundNonSelectionColor(Color newColor) {
        backgroundNonSelectionColor = newColor;
    }

    /**
     * Returns the background color to be used for non selected cells.
     *
     * @return
     */
    public Color getBackgroundNonSelectionColor() {
        return backgroundNonSelectionColor;
    }

    /**
     * Sets the style the cell is drawn with when the cell is selected.
     *
     * @param newStyle
     */
    public void setSelectionStyle(Style newStyle) {
        selectionStyle = newStyle;
    }

    /**
     * Returns the style the cell is drawn with when the cell is selected.
     *
     * @return
     */
    public Style getSelectionStyle() {
        return selectionStyle;
    }

    /**
     * Sets the style the cell is drawn with when the cell isn't selected.
     *
     * @param newStyle
     */
    public void setNonSelectionStyle(Style newStyle) {
        nonSelectionStyle = newStyle;
    }

    /**
     * Returns the style the cell is drawn with when the cell isn't selected.
     *
     * @return
     */
    public Style getNonSelectionStyle() {
        return nonSelectionStyle;
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
