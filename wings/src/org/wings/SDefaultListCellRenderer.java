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

import org.wings.style.Style;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SDefaultListCellRenderer
    extends SLabel
    implements SListCellRenderer
{

    /** Style to use for the foreground for selected nodes. */
    protected Style selectionStyle = null;

    /** Style to use for the foreground for non-selected nodes. */
    protected Style nonSelectionStyle = null;

    /**
     * Create a SDefaultListCellRenderer with default properties.
     */
    public SDefaultListCellRenderer() {}


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




    public SComponent getListCellRendererComponent(SComponent list,
                                                   Object value,
                                                   boolean selected,
                                                   int index)
    {
        setText(null);
        setIcon(null);

        if (value == null)
            setText("");
        else if (value instanceof SIcon)
            setIcon((SIcon)value);
        else if ( value instanceof SComponent ) {
            SComponent result = (SComponent)value;

            if ( selected && selectionStyle!=null ) {
                result.setStyle(selectionStyle);
            } else {
                result.setStyle(nonSelectionStyle);
            }
            
            return result;
        } 
        else {
            setText(value.toString());
        }

        if ( selected && selectionStyle!=null ) {
            setStyle(selectionStyle);
        } else {
            setStyle(nonSelectionStyle);
        }

        return this;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
