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

import java.awt.Color;
import java.awt.Rectangle;

import java.io.IOException;
import javax.swing.tree.*;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.util.CGUtil;

public class TreeCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.TreeCG
{
    // wie kann statisch auf die Defaults zugreifen??
    private final static SIcon OPEN_ICON = 
        new ResourceImageIcon("org/wings/icons/TreeOpen.gif");
    private final static SIcon CLOSED_ICON = 
        new ResourceImageIcon("org/wings/icons/TreeClosed.gif");
    private final static SIcon LEAF_ICON = 
        new ResourceImageIcon("org/wings/icons/TreeLeaf.gif");

    private final static SIcon BLIND_ICON = 
        new ResourceImageIcon("org/wings/icons/blind.gif");

    private final static String propertyPrefix = "Tree";

    private final static String nodePropertyPrefix = "TreeNode";

    private SIcon openIcon = OPEN_ICON;
    private SIcon closedIcon = CLOSED_ICON;
    private SIcon leafIcon = LEAF_ICON;


    /**
     * Sets the icon used to represent non-leaf nodes that are expanded.
     *
     * @param newIcon
     */
    public void setOpenIcon(SIcon newIcon) {
        openIcon = newIcon;
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are expanded.
     *
     * @return
     */
    public SIcon getOpenIcon() {
        return openIcon;
    }

    /**
     * Sets the icon used to represent non-leaf nodes that are not expanded.
     *
     * @param newIcon
     */
    public void setClosedIcon(SIcon newIcon) {
        closedIcon = newIcon;
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are not
     * expanded.
     *
     * @return
     */
    public SIcon getClosedIcon() {
        return closedIcon;
    }

    /**
     * Sets the icon used to represent leaf nodes.
     *
     * @param newIcon
     */
    public void setLeafIcon(SIcon newIcon) {
        leafIcon = newIcon;
    }

    /**
     * Returns the icon used to represent leaf nodes.
     *
     * @return
     */
    public SIcon getLeafIcon() {
        return leafIcon;
    }

    
    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent component)
        throws IOException
    {
        STree tree = (STree)component;
        SBorder border = tree.getBorder();

        int start = 0;
        int count = tree.getRowCount();
        java.awt.Color bgcolor = tree.getBackground();
        Rectangle viewport = tree.getViewportSize();
        if (viewport != null) {
            start = viewport.y;
            count = viewport.height;
        }

        int depth = tree.getMaximumExpandedDepth(); // - ( ( tree.isRootVisible() )?0:1 );
        d.append("<table border=\"0\" cellpadding=\"0\"");
        CGUtil.writeSize( d, tree );
        if ( Utils.hasSpanAttributes( tree ) ) {
            d.append( " style=\"" );
            Utils.writeSpanAttributes( d, tree );
            d.append( "\"" );
        }
        d.append(">");
        
        for (int i=start; i < count; i++)
            writeTreeNode(tree, d, tree.getPathForRow(i), depth);
		
        // expandable last row to fit preferred table size on IE
        d.append("<tr><td colspan=\"");
        d.append(depth);
        d.append("\"></td></tr>");
        d.append("</table>");
    }

    public void writeTreeNode(STree tree, Device d, TreePath path, int depth)
        throws IOException
    {
        int nodeIndentDepth = tree.getNodeIndentDepth();
        d.append("<tr height=\"1\">");
        for (int i=((tree.isRootVisible())?0:1); i<path.getPathCount()-1; i++)
         {
            d.append("<td width=\"" + nodeIndentDepth + "\">");
            Utils.appendBlindIcon(d, BLIND_ICON, 1, tree.getNodeIndentDepth());
            d.append("</td>");
         }
        d.append("\n<td nowrap colspan=\"" + (depth - (path.getPathCount()-1)) + "\">");

        TreeNode node = (TreeNode)path.getLastPathComponent();
        STreeCellRenderer cellRenderer = tree.getCellRenderer();

        boolean isLeaf = tree.getModel().isLeaf(node);
        boolean isExpanded = tree.isExpanded(path);

        SComponent renderer = cellRenderer.getTreeCellRendererComponent(tree, node,
                                                                        tree.isPathSelected(path),
                                                                        isExpanded,
                                                                        isLeaf, 0,
                                                                        false);

        RequestURL selectionAddr = tree.getRequestURL();
        selectionAddr.addParameter(tree.getSelectionParameter(node));

        if ( isLeaf ) {
            Utils.appendIcon(d, leafIcon, null);
        } else {
            RequestURL expansionAddr = tree.getRequestURL();
            expansionAddr.addParameter(tree.getExpansionParameter(node));
            d.append("<a href=").append(expansionAddr.toString()).append(">");
            if (isExpanded) {
                if (openIcon == null)
                    d.append("-");
                else
                    Utils.appendIcon(d, openIcon, null);
            } else {
                if (closedIcon == null)
                    d.append("+");
                else
                    Utils.appendIcon(d, closedIcon, null);
            }
            d.append("</a>");
        } 
        d.append("&nbsp;");
        
        d.append("<a href=").append(selectionAddr.toString()).append(">");
        SCellRendererPane rendererPane = tree.getCellRendererPane();
        rendererPane.writeComponent(d, renderer, tree);
        d.append("</a>");

        d.append("\n</td><td width=\"100%\"></td></tr>\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
