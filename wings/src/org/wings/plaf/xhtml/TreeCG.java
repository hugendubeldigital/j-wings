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

package org.wings.plaf.xhtml;

import java.io.IOException;

import java.awt.Color;
import java.io.IOException;
import javax.swing.tree.*;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class TreeCG
    implements org.wings.plaf.TreeCG
{
    private final static String propertyPrefix = "Tree";
    private final static String nodePropertyPrefix = "TreeNode";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent component) {
        STree tree = (STree)component;
        component.setStyle(component.getSession().getCGManager().
                           getStyle(getPropertyPrefix() + ".style"));
        tree.setCellRendererPane(new SCellRendererPane());
        installCellRenderer(tree);
    }

    public void uninstallCG(SComponent component) {
        STree tree = (STree)component;
        tree.removeCellRendererPane();
        uninstallCellRenderer(tree);
    }

    protected void installCellRenderer(STree tree) {
        STreeCellRenderer cellRenderer = tree.getCellRenderer();
        if (cellRenderer == null || cellRenderer instanceof SDefaultTreeCellRenderer) {
            cellRenderer = new SDefaultTreeCellRenderer();
            configureCellRenderer(tree, (SDefaultTreeCellRenderer)cellRenderer);
            tree.setCellRenderer(cellRenderer);
        }
    }

    protected void uninstallCellRenderer(STree tree) {
        STreeCellRenderer cellRenderer = tree.getCellRenderer();
        if (cellRenderer != null && cellRenderer instanceof SDefaultTreeCellRenderer)
            tree.setCellRenderer(null);
    }

    protected void configureCellRenderer(STree tree, SDefaultTreeCellRenderer cellRenderer) {
        CGManager cgManager = tree.getSession().getCGManager();
        cellRenderer.setTextSelectionStyle(cgManager.getStyle(nodePropertyPrefix + "Selection.style"));
        cellRenderer.setTextNonSelectionStyle(cgManager.getStyle(nodePropertyPrefix + "NonSelection.style"));
    }

    public void write(Device d, SComponent component)
        throws IOException
    {
        STree tree = (STree)component;

        int depth = tree.getMaximumExpandedDepth();
        d.append("<table cellpadding=\"0\">");
        for (int i=0; i < tree.getRowCount(); i++)
            writeTreeNode(tree, d, tree.getPathForRow(i), depth);
        d.append("</table>");
    }

    public void writeTreeNode(STree tree, Device d, TreePath path, int depth)
        throws IOException
    {
        int nodeIndentDepth = tree.getNodeIndentDepth();
        d.append("<tr>");
        for ( int i=0; i<path.getPathCount()-1; i++ )
            d.append("<td width=\"" + nodeIndentDepth + "\"></td>");
        d.append("\n<td colspan=\"" + (depth - (path.getPathCount()-1)) + "\">");

        TreeNode node = (TreeNode)path.getLastPathComponent();
        STreeCellRenderer cellRenderer = tree.getCellRenderer();
        SComponent renderer = cellRenderer.getTreeCellRendererComponent(tree, node,
                                                                        tree.isPathSelected(path),
                                                                        tree.isExpanded(path),
                                                                        node.isLeaf(), 0,
                                                                        false);
        SCellRendererPane rendererPane = tree.getCellRendererPane();
        rendererPane.writeComponent(d, renderer, tree);

        d.append("\n</td></tr>\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
