/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.xhtml;

import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SGridBagLayout;
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.awt.*;
import java.io.IOException;

public class GridBagLayoutCG
        implements LayoutCG {
    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
            throws IOException {
        SGridBagLayout layout = (SGridBagLayout) l;
        SContainer container = layout.getContainer();

        boolean header = layout.getHeader();
        int cellSpacing = layout.getCellSpacing();
        int cellPadding = layout.getCellPadding();
        int border = layout.getBorder();

        SGridBagLayout.Grid grid = layout.getGrid();
        if (grid.cols == 0) {
            return;
        }

        d.print("\n<table ");
        if (Utils.hasSpanAttributes(container)) {
            d.print(" style=\"");
            Utils.writeSpanAttributes(d, container);
            d.print("\" ");
        }

        if (cellSpacing >= 0)
            d.print(" cellspacing=\"").print(cellSpacing).print("\"");
        else
            d.print(" cellspacing=\"0\"");

        if (cellPadding >= 0)
            d.print(" cellpadding=\"").print(cellPadding).print("\"");
        else
            d.print(" cellpadding=\"0\"");

        // CGUtil.writeSize( d, container );

        if (border > 0)
            d.print(" border=\"").print(border).print("\"");
        else
            d.print(" border=\"0\"");

        /*
        if (container != null && container.getBackground() != null)
            d.print(" bgcolor=\"#").
                print(Utils.toColorString(container.getBackground())).print("\"");
        */
        d.print(">\n");


        for (int row = grid.firstRow; row < grid.rows; row++) {
            d.print("<tr>\n");
            for (int col = grid.firstCol; col < grid.cols; col++) {
                SComponent comp = grid.grid[col][row];
                if (comp == null) {
                    if (row == grid.firstRow && header) {
                        d.print("<th></th>\n");
                    } else {
                        d.print("<td></td>\n");
                    }
                } else {
                    GridBagConstraints c = layout.getConstraints(comp);
                    if ((c.gridx == SGridBagLayout.LAST_CELL || c.gridx == col) &&
                            (c.gridy == SGridBagLayout.LAST_CELL || c.gridy == row)) {
                        if (row == grid.firstRow && header) {
                            d.print("<th");
                        } else {
                            d.print("<td");
                        }

                        Utils.printTableCellAttributes(d, comp);

                        int gridwidth = c.gridwidth;
                        if (gridwidth == GridBagConstraints.RELATIVE) {
                            gridwidth = grid.cols - col - 1;
                        } else if (gridwidth == GridBagConstraints.REMAINDER) {
                            gridwidth = grid.cols - col;
                        }
                        if (gridwidth > 1) {
                            d.print(" colspan=" + gridwidth);
                        }

                        int gridheight = c.gridheight;
                        if (gridheight == GridBagConstraints.RELATIVE) {
                            gridheight = grid.rows - row - 1;
                        } else if (gridheight == GridBagConstraints.REMAINDER) {
                            gridheight = grid.rows - row;
                        }
                        if (gridheight > 1) {
                            d.print(" rowspan=" + gridheight);
                        }
                        if (c.weightx > 0 && grid.colweight[row] > 0) {
                            d.print(" width=\"" +
                                    (int) (100 * c.weightx / grid.colweight[row]) +
                                    "%\"");
                        }
                        if (c.weighty > 0 && grid.rowweight[col] > 0) {
                            d.print(" height=\"" +
                                    (int) (100 * c.weighty / grid.rowweight[col]) +
                                    "%\"");
                        }

                        d.print(">");
                        comp.write(d);

                        if (row == grid.firstRow && header) {
                            d.print("</th>\n");
                        } else {
                            d.print("</td>\n");
                        }
                    }
                }
            }
            d.print("</tr>\n");
        }
        d.print("</table>\n");
    }
}


