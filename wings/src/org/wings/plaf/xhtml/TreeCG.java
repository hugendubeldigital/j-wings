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
    private final static String propertyPrefix = "Tree";
    private final static String nodePropertyPrefix = "TreeNode";
	private final SImage blindGif = new SImage( new ResourceImageIcon("org/wings/icons/blind.gif") );
    
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

		blindGif.setWidth( tree.getNodeIndentDepth() );
		blindGif.setHeight( 1 );

        int depth = tree.getMaximumExpandedDepth(); // - ( ( tree.isRootVisible() )?0:1 );
        d.append("<table border=\"0\" cellpadding=\"0\"");
		CGUtil.writeSize( d, tree );
        if ( Utils.hasSpanAttributes( tree ) )
         {
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
            blindGif.write( d );
            d.append("</td>");
		 }
        d.append("\n<td nowrap colspan=\"" + (depth - (path.getPathCount()-1)) + "\">");

        TreeNode node = (TreeNode)path.getLastPathComponent();
        STreeCellRenderer cellRenderer = tree.getCellRenderer();

        SComponent renderer = cellRenderer.getTreeCellRendererComponent(tree, node,
                                                                        tree.isPathSelected(path),
                                                                        tree.isExpanded(path),
                                                                        tree.getModel().isLeaf(node), 0,
                                                                        false);
        SCellRendererPane rendererPane = tree.getCellRendererPane();
        rendererPane.writeComponent(d, renderer, tree);

		d.append("\n</td><td width=\"100%\"></td></tr>\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
