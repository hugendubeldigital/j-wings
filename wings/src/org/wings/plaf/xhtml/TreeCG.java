package org.wings.plaf.xhtml;

import java.io.IOException;

import java.awt.Color;
import java.io.IOException;
import javax.swing.tree.*;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class TreeCG implements org.wings.plaf.TreeCG
{
    private final static String propertyPrefix = "Tree" + ".";
    private final static String nodePropertyPrefix = "TreeNode";
    
    protected String getPropertyPrefix() {
        return propertyPrefix;
    }
    
    public void installCG(SComponent component) {
	STree tree = (STree)component;
	component.setStyle(component.getSession().getCGManager().getStyle(propertyPrefix + "style"));
	tree.add(new SCellRendererPane());
	installCellRenderer(tree);
    }
    public void uninstallCG(SComponent component) {
	STree tree = (STree)component;
	SCellRendererPane rendererComponent = getCellRendererPane(tree);
	tree.remove(rendererComponent);
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
    
    protected SCellRendererPane getCellRendererPane(STree tree) {
	int count = tree.getComponentCount();
	for (int i=0; i < count; i++) {
	    SComponent component = tree.getComponent(i);
	    if (component instanceof SCellRendererPane)
		return (SCellRendererPane)component;
	}
	return null;
    }
    
    public void write(Device d, SComponent component)
        throws IOException
    {
	STree tree = (STree)component;
	
	int depth = tree.getMaximumExpandedDepth();
	d.append("<table>");
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
	    d.append("<td width=" + nodeIndentDepth + "></td>");
	d.append("\n<td colspan=" + (depth - (path.getPathCount()-1)) + ">");
	
	TreeNode node = (TreeNode)path.getLastPathComponent();
	STreeCellRenderer cellRenderer = tree.getCellRenderer();
	SComponent renderer = cellRenderer.getTreeCellRendererComponent(tree, node,
									tree.isPathSelected(path),
									tree.isExpanded(path),
									node.isLeaf(), 0,
									false);
	SCellRendererPane rendererPane = getCellRendererPane(tree);
	rendererPane.writeComponent(d, renderer, tree);
	
	d.append("\n</td></tr>\n");
    }
}
