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

import java.awt.Color;
import java.io.IOException;
import javax.swing.Icon;

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
public class SDefaultTableCellRenderer
    extends SContainer
    implements STableCellRenderer
{
    /** Color to use for the foreground for selected nodes. */
    protected Color foregroundSelectionColor = null;

    /** Color to use for the foreground for non-selected nodes. */
    protected Color foregroundNonSelectionColor = null;

    /** Color to use for the background when a node is selected. */
    protected Color backgroundSelectionColor = null;

    /** Color to use for the background when the node isn't selected. */
    protected Color backgroundNonSelectionColor = null;

    /** Style to use for the foreground for selected nodes. */
    protected Style cellSelectionStyle = null;

    /** Style to use for the foreground for non-selected nodes. */
    protected Style cellNonSelectionStyle = null;

    /** Icon used to render the edit button. */
    transient protected Icon editIcon = null;

    /** Label to use for the content. */
    protected SLabel contents = null;

    /** Label to use for the content. */
    protected SLabel edit = null;

    /**
     * TODO: documentation
     */
    protected SGetAddress addr = null;

    /**
     * TODO: documentation
     *
     */
    public SDefaultTableCellRenderer() {
        setLayout(null);
        contents = new SLabel();
        edit = new SLabel();
        add(contents);
        add(edit);
        createDefaultIcons();
    }

    /**
     * TODO: documentation
     *
     */
    protected void createDefaultIcons() {
        CGManager cgManager = getSession().getCGManager();
        if (cgManager == null)
            return;
        setEditIcon(cgManager.getIcon("Table.editIcon"));
    }

    public SComponent getTableCellRendererComponent(SBaseTable baseTable,
                                                    Object value,
                                                    boolean selected,
                                                    int row,
                                                    int col)
    {
        addr = baseTable.getServerAddress();
        addr.add(baseTable.getNamePrefix() + "=" + row + ":" + col);

        contents.setText((value == null) ? "&nbsp;" : value.toString());

        if (baseTable instanceof STable) {
            STable table = (STable)baseTable;
            if (selected) {
                if (table.getSelectionBackground() == null) {
                    setBackground(backgroundSelectionColor);
                    contents.setBackground(backgroundSelectionColor);
                }
                else {
                    setBackground(table.getSelectionBackground());
                    contents.setBackground(table.getSelectionBackground());
                }
                if (table.getSelectionForeground() == null) {
                    setForeground(foregroundSelectionColor);
                    contents.setForeground(foregroundSelectionColor);
                }
                else {
                    setForeground(table.getSelectionForeground());
                    contents.setForeground(table.getSelectionForeground());
                }
                setStyle(cellSelectionStyle);
                contents.setStyle(cellSelectionStyle);
            }
            else {
                setBackground(backgroundNonSelectionColor);
                setForeground(foregroundNonSelectionColor);
                contents.setBackground(backgroundNonSelectionColor);
                contents.setForeground(foregroundNonSelectionColor);
                setStyle(cellNonSelectionStyle);
                contents.setStyle(cellNonSelectionStyle);
            }
            if (table.isCellEditable(row, col))
                return this;
        }

        // only the Label...
        return contents;
    }

    /**
     * TODO: documentation
     *
     * @param d
     * @throws IOException
     */
    public void write(Device d)
        throws IOException
    {
        d.append("<table width=\"100%\" cellpadding=\"0\"><tr>");
        d.append("<td");
        SUtil.appendTableCellAttributes(d, contents);
        d.append(">");
        contents.write(d);
        d.append("</td>");

        d.append("<td align=\"right\"><a href=\"");
        d.append(addr);
        d.append("\">");
        edit.write(d);
        d.append("</a></td></tr></table>");
    }

    /**
     * TODO: documentation
     *
     * @param escape
     */
    public void setEscapeSpecialChars(boolean escape) {
        contents.setEscapeSpecialChars(escape);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean getEscapeSpecialChars() {
        return contents.getEscapeSpecialChars();
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
    public void setCellSelectionStyle(Style newStyle) {
        cellSelectionStyle = newStyle;
    }

    /**
     * Returns the style the cell is drawn with when the cell is selected.
     *
     * @return
     */
    public Style getCellSelectionStyle() {
        return cellSelectionStyle;
    }

    /**
     * Sets the style the cell is drawn with when the cell isn't selected.
     *
     * @param newStyle
     */
    public void setCellNonSelectionStyle(Style newStyle) {
        cellNonSelectionStyle = newStyle;
    }

    /**
     * Returns the style the cell is drawn with when the cell isn't selected.
     *
     * @return
     */
    public Style getCellNonSelectionStyle() {
        return cellNonSelectionStyle;
    }

    /**
     * Sets the icon used to render the edit button.
     *
     * @param newIcon
     */
    public void setEditIcon(Icon newIcon) {
        editIcon = newIcon;
        edit.setIcon(editIcon);
    }

    /**
     * Returns the icon used to render the edit button
     *
     * @return
     */
    public Icon getEditIcon() {
        return editIcon;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
