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
import java.util.logging.*;
import javax.swing.tree.*;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.style.Style;
import org.wings.util.CGUtil;

public class TreeCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.TreeCG
{
    private final static SIcon BLIND_ICON = 
        new ResourceImageIcon("org/wings/icons/blind.gif");

    private final static String propertyPrefix = "Tree";

    private final static String nodePropertyPrefix = "TreeNode";

    private SIcon openIcon;
    private SIcon closedIcon;
    private SIcon leafIcon;
    private SIcon hashMark;


    public void installCG(SComponent component) {
        super.installCG(component);
        CGManager cg = component.getSession().getCGManager();
        openIcon = cg.getIcon("TreeCG.collapseControlIcon");
        closedIcon = cg.getIcon("TreeCG.expandControlIcon");
        leafIcon = cg.getIcon("TreeCG.leafControlIcon");
        hashMark = cg.getIcon("TreeCG.hash");
    }

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

        Rectangle viewport = tree.getViewportSize();
        if (viewport != null) {
            start = viewport.y;
            count = viewport.height;
        }

        int depth = tree.getMaximumExpandedDepth(); // - ( ( tree.isRootVisible() )?0:1 );

        d.print("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"");
        CGUtil.writeSize( d, tree );
        if ( Utils.hasSpanAttributes( tree ) ) {
            d.print( " style=\"" );
            Utils.writeSpanAttributes( d, tree );
            d.print( "\"" );
        }
        d.print(">");
        
        for (int i=start; i < count; i++)
            writeTreeNode(tree, d, tree.getPathForRow(i), depth, i);
		
        // expandable last row to fit preferred table size on IE
        d.print("<tr><td colspan=\"");
        d.print(depth);
        d.print("\"></td></tr>");
        d.print("</table>");
    }

    protected final boolean isLastChild(Object pathComponent) {
        TreeNode node = (TreeNode)pathComponent;

        TreeNode parent = node.getParent();
        if ( parent==null )
            return true;

        return parent.getChildAt(parent.getChildCount()-1)==node;
    }

    public void writeTreeNode(STree tree, Device d, TreePath path, int depth,
                              int row)
        throws IOException
    {
        int nodeIndentDepth = tree.getNodeIndentDepth();

        Object node = path.getLastPathComponent();
        STreeCellRenderer cellRenderer = tree.getCellRenderer();

        boolean isLeaf = tree.getModel().isLeaf(node);
        boolean isExpanded = tree.isExpanded(path);
        boolean isSelected = tree.isPathSelected(path);

        SComponent renderer = cellRenderer.getTreeCellRendererComponent(tree, node,
                                                                        isSelected,
                                                                        isExpanded,
                                                                        isLeaf, row,
                                                                        false);

        RequestURL selectionAddr = tree.getRequestURL();
        selectionAddr.addParameter(tree.getSelectionParameter(node));


        d.print("<tr height=\"1\">");
        for (int i=((tree.isRootVisible())?0:1); i<path.getPathCount()-1; i++) {
            d.print("<td width=\"").print(nodeIndentDepth).print("\"");
            if ( hashMark!=null && !isLastChild(path.getPathComponent(i)) ) {
                d.print(" style=\"background-image:url(").print(hashMark.getURL()).
                    print(")\"");
            }
            d.print(">");
            Utils.appendBlindIcon(d, BLIND_ICON, 1, tree.getNodeIndentDepth());
            d.print("</td>");
        }

        d.print("\n<td nowrap colspan=\"" + (depth - (path.getPathCount()-1)) + "\">");

        // render control icons
        if ( !(isLeaf && leafIcon==null) ) {
            // in most applications, the is no need to render a control icon for
            // a leaf. 
            d.print("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"");
            /*            
                          if ( Utils.hasSpanAttributes( tree ) ) {
                          d.print( " style=\"" );
                          Utils.writeSpanAttributes( d, tree );
                          d.print( "\"" );
                          }*/
            d.print("><tr><td nowrap>");
        }

        if (isLeaf) {
            Utils.appendIcon(d, leafIcon, null);
        } else {
            RequestURL expansionAddr = tree.getRequestURL();
            expansionAddr.addParameter(tree.getExpansionParameter(node));
            d.print("<a href=\"").print(expansionAddr.toString()).print("\">");

            if (isExpanded) {
                if (openIcon == null)
                    d.print("-");
                else
                    Utils.appendIcon(d, openIcon, null);
            } else {
                if (closedIcon == null)
                    d.print("+");
                else
                    Utils.appendIcon(d, closedIcon, null);
            }
            d.print("</a>");
        } 

        if ( !(isLeaf && leafIcon==null) ) {
            d.print("</td><td nowrap>");
        }

        SCellRendererPane rendererPane = tree.getCellRendererPane();
        if ( renderer instanceof ClickableRenderComponent ) {
            ((ClickableRenderComponent)renderer).setEventURL(selectionAddr);
            rendererPane.writeComponent(d, renderer, tree);
        } else {
            d.print("<a href=\"").print(selectionAddr.toString()).print("\"");

            Style cellStyle = isSelected ? 
                tree.getSelectionStyle() : tree.getStyle();
            
            if (cellStyle != null)
                cellStyle.write(d);
            
            d.print(">");
            
            rendererPane.writeComponent(d, renderer, tree);
            d.print("</a>");
        }

        if ( renderer instanceof ClickableRenderComponent ) {
            ((ClickableRenderComponent)renderer).setEventURL(null);
        }


        if ( !(isLeaf && leafIcon==null) ) {
            d.print("</td></tr></table>");
        }

        d.print("\n</td><td width=\"100%\"></td></tr>\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
