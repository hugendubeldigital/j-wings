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
package org.wings;

import org.wings.plaf.TreeCG;
import org.wings.tree.SDefaultTreeSelectionModel;
import org.wings.tree.STreeCellRenderer;
import org.wings.tree.STreeSelectionModel;

import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class STree
        extends SComponent
        implements LowLevelEventListener, Scrollable {
    private final static Logger logger = Logger.getLogger("org.wings");
    private int nodeIndentDepth;

    /**
     * Creates and returns a sample TreeModel. Used primarily for beanbuilders.
     * to show something interesting.
     *
     * @return the default TreeModel
     */
    protected static TreeModel getDefaultTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("STree");
        DefaultMutableTreeNode parent;

        parent = new DefaultMutableTreeNode("colors");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("blue"));
        parent.add(new DefaultMutableTreeNode("violet"));
        parent.add(new DefaultMutableTreeNode("red"));
        parent.add(new DefaultMutableTreeNode("yellow"));

        parent = new DefaultMutableTreeNode("sports");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("basketball"));
        parent.add(new DefaultMutableTreeNode("soccer"));
        parent.add(new DefaultMutableTreeNode("football"));
        parent.add(new DefaultMutableTreeNode("hockey"));

        parent = new DefaultMutableTreeNode("food");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("hot dogs"));
        parent.add(new DefaultMutableTreeNode("pizza"));
        parent.add(new DefaultMutableTreeNode("ravioli"));
        parent.add(new DefaultMutableTreeNode("bananas"));
        return new DefaultTreeModel(root);
    }

    protected TreeModel model;

    transient protected TreeModelListener treeModelListener;

    protected STreeCellRenderer renderer;

    protected STreeSelectionModel selectionModel;

    /**
     * store here all delayed expansion events
     */
    private ArrayList delayedExpansionEvents;

    /**
     * store here expansion paths that will be processed after procession the
     * request.
     */
    protected final ArrayList requestedExpansionPaths = new ArrayList();

    protected AbstractLayoutCache treeState = new VariableHeightLayoutCache();

    /**
     * Implementation of the  {@link Scrollable} interface.
     */
    protected Rectangle viewport;

    /**
     * used to forward the selection to the selection Listeners of the tree
     */
    private final TreeSelectionListener forwardSelectionEvent =
            new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    fireTreeSelectionEvent(e);
                }
            };

    public STree(TreeModel model) {
        super();
        setModel(model);
        setRootVisible(true);
        setSelectionModel(new SDefaultTreeSelectionModel());
    }

    public STree() {
        this(getDefaultTreeModel());
    }

    public void addTreeSelectionListener(TreeSelectionListener tsl) {
        addEventListener(TreeSelectionListener.class, tsl);
    }

    public void removeTreeSelectionListener(TreeSelectionListener tsl) {
        removeEventListener(TreeSelectionListener.class, tsl);
    }

    protected void fireTreeSelectionEvent(TreeSelectionEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeSelectionListener.class) {
                ((TreeSelectionListener) listeners[i + 1]).valueChanged(e);
            }
        }
        reload();
    }

    /**
     * Adds a listener for <code>TreeWillExpand</code> events.
     *
     * @param tel a <code>TreeWillExpandListener</code> that will be notified
     *            when a tree node will be expanded or collapsed (a "negative
     *            expansion")
     */
    public void addTreeWillExpandListener(TreeWillExpandListener tel) {
        addEventListener(TreeWillExpandListener.class, tel);
    }

    /**
     * Removes a listener for <code>TreeWillExpand</code> events.
     *
     * @param tel the <code>TreeWillExpandListener</code> to remove
     */
    public void removeTreeWillExpandListener(TreeWillExpandListener tel) {
        removeEventListener(TreeWillExpandListener.class, tel);
    }


    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the <code>path</code> parameter.
     *
     * @param path the <code>TreePath</code> indicating the node that was
     *             expanded
     * @see EventListenerList
     */
    public void fireTreeWillExpand(TreePath path, boolean expand)
            throws ExpandVetoException {

        // Guaranteed to return a non-null array
        Object[] listeners = getListenerList();
        TreeExpansionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeWillExpandListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeExpansionEvent(this, path);

                if (expand) {
                    ((TreeWillExpandListener) listeners[i + 1]).
                            treeWillExpand(e);
                } else {
                    ((TreeWillExpandListener) listeners[i + 1]).
                            treeWillCollapse(e);
                }
            }
        }
    }

    public void addTreeExpansionListener(TreeExpansionListener tel) {
        addEventListener(TreeExpansionListener.class, tel);
    }

    public void removeTreeExpansionListener(TreeExpansionListener tel) {
        removeEventListener(TreeExpansionListener.class, tel);
    }


    private static class DelayedExpansionEvent {
        TreeExpansionEvent expansionEvent;
        boolean expansion;

        DelayedExpansionEvent(TreeExpansionEvent e, boolean b) {
            expansionEvent = e;
            expansion = b;
        }

    }

    protected void addDelayedExpansionEvent(TreeExpansionEvent e,
                                            boolean expansion) {
        if (delayedExpansionEvents == null) {
            delayedExpansionEvents = new ArrayList();
        }

        delayedExpansionEvents.add(new DelayedExpansionEvent(e, expansion));
    }

    protected void fireDelayedExpansionEvents() {
        if (delayedExpansionEvents != null &&
                !getSelectionModel().getDelayEvents()) {
            for (int i = 0; i < delayedExpansionEvents.size(); i++) {
                DelayedExpansionEvent e =
                        (DelayedExpansionEvent) delayedExpansionEvents.get(i);

                fireTreeExpansionEvent(e.expansionEvent, e.expansion);
            }
            delayedExpansionEvents.clear();
        }
    }


    /**
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param path the TreePath indicating the node that was expanded
     * @see EventListenerList
     */
    protected void fireTreeExpanded(TreePath path) {
        fireTreeExpansionEvent(new TreeExpansionEvent(this, path), true);
    }

    protected void fireTreeExpansionEvent(TreeExpansionEvent e, boolean expansion) {
        if (getSelectionModel().getDelayEvents()) {
            addDelayedExpansionEvent(e, expansion);
        } else {
            // Guaranteed to return a non-null array
            Object[] listeners = getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == TreeExpansionListener.class) {
                    if (expansion)
                        ((TreeExpansionListener) listeners[i + 1]).treeExpanded(e);
                    else
                        ((TreeExpansionListener) listeners[i + 1]).treeCollapsed(e);
                }
            }
        }
    }

    /**
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param path the TreePath indicating the node that was collapsed
     * @see EventListenerList
     */
    public void fireTreeCollapsed(TreePath path) {
        fireTreeExpansionEvent(new TreeExpansionEvent(this, path), false);
    }

    protected void processRequestedExpansionPaths() {
        getSelectionModel().setDelayEvents(true);

        for (int i = 0; i < requestedExpansionPaths.size(); i++) {
            try {
                TreePath path = (TreePath) requestedExpansionPaths.get(i);
                togglePathExpansion(path);
            } catch (ExpandVetoException ex) {
                // do not expand...
            }
        }
        requestedExpansionPaths.clear();
        getSelectionModel().setDelayEvents(false);
    }

    public void fireIntermediateEvents() {
        processRequestedExpansionPaths();

        getSelectionModel().fireDelayedIntermediateEvents();
    }

    public void fireFinalEvents() {
        fireDelayedExpansionEvents();
        getSelectionModel().fireDelayedFinalEvents();
    }

    public boolean checkEpoch() {
        return true;
    }


    public void setRootVisible(boolean rootVisible) {
        treeState.setRootVisible(rootVisible);
    }


    public boolean isRootVisible() {
        return treeState.isRootVisible();
    }


    public void setModel(TreeModel m) {
        if (model != null && treeModelListener != null)
            model.removeTreeModelListener(treeModelListener);
        model = m;
        treeState.setModel(m);

        if (model != null) {
            if (treeModelListener == null)
                treeModelListener = createTreeModelListener();

            if (treeModelListener != null)
                model.addTreeModelListener(treeModelListener);

            // Mark the root as expanded, if it isn't a leaf.
            if (!model.isLeaf(model.getRoot()))
                treeState.setExpandedState(new TreePath(model.getRoot()), true);
        }
    }


    public TreeModel getModel() {
        return model;
    }


    public int getRowCount() {
        return treeState.getRowCount();
    }


    public TreePath getPathForRow(int row) {
        return treeState.getPathForRow(row);
    }


    protected int fillPathForAbsoluteRow(int row, Object node, ArrayList path) {
        // and check if it is the 
        if (row == 0) {
            return 0;
        } // end of if ()

        for (int i = 0; i < model.getChildCount(node); i++) {
            path.add(model.getChild(node, i));
            row = fillPathForAbsoluteRow(row - 1, model.getChild(node, i), path);
            if (row == 0) {
                return 0;
            } // end of if ()
            path.remove(path.size() - 1);
        }
        return row;
    }

    public TreePath getPathForAbsoluteRow(int row) {
        // fill path in this array
        ArrayList path = new ArrayList(10);

        path.add(model.getRoot());
        fillPathForAbsoluteRow(row, model.getRoot(), path);

        return new TreePath(path.toArray());
    }

    /**
     * Sets the tree's selection model. When a null value is specified
     * an empty electionModel is used, which does not allow selections.
     *
     * @param selectionModel the TreeSelectionModel to use, or null to
     *                       disable selections
     * @beaninfo bound: true
     * description: The tree's selection model.
     * @see TreeSelectionModel
     */
    public void setSelectionModel(STreeSelectionModel selectionModel) {
        if (this.selectionModel != null)
            this.selectionModel.removeTreeSelectionListener(forwardSelectionEvent);

        if (selectionModel != null)
            selectionModel.addTreeSelectionListener(forwardSelectionEvent);

        if (selectionModel == null)
            this.selectionModel = SDefaultTreeSelectionModel.NO_SELECTION_MODEL;
        else
            this.selectionModel = selectionModel;
    }

    /**
     * Returns the model for selections. This should always return a
     * non-null value. If you don't want to allow anything to be selected
     * set the selection model to null, which forces an empty
     * selection model to be used.
     *
     * @see #setSelectionModel
     */
    public STreeSelectionModel getSelectionModel() {
        return selectionModel;
    }

    /**
     * Returns JTreePath instances representing the path between index0
     * and index1 (including index1).
     *
     * @param index0 an int specifying a display row, where 0 is the
     *               first row in the display
     * @param index0 an int specifying a second display row
     * @return an array of TreePath objects, one for each node between
     *         index0 and index1, inclusive
     */
    protected TreePath[] getPathBetweenRows(int index0, int index1) {
        int newMinIndex = Math.min(index0, index1);
        int newMaxIndex = Math.max(index0, index1);

        TreePath[] selection = new TreePath[newMaxIndex - newMinIndex + 1];

        for (int i = newMinIndex; i <= newMaxIndex; i++) {
            selection[i - newMinIndex] = getPathForRow(i);
        }

        return selection;
    }

    /**
     * Selects the node identified by the specified path.  If any
     * component of the path is hidden (under a collapsed node), it is
     * exposed (made viewable).
     *
     * @param path the TreePath specifying the node to select
     */
    public void setSelectionPath(TreePath path) {
        getSelectionModel().setSelectionPath(path);
    }

    /**
     * Selects the nodes identified by the specified array of paths.
     * If any component in any of the paths is hidden (under a collapsed
     * node), it is exposed (made viewable).
     *
     * @param paths an array of TreePath objects that specifies the nodes
     *              to select
     */
    public void setSelectionPaths(TreePath[] paths) {
        getSelectionModel().setSelectionPaths(paths);
    }

    /**
     * Selects the node at the specified row in the display.
     *
     * @param row the row to select, where 0 is the first row in
     *            the display
     */
    public void setSelectionRow(int row) {
        int[] rows = {row};
        setSelectionRows(rows);
    }

    /**
     * Selects the nodes corresponding to each of the specified rows
     * in the display.
     *
     * @param rows an array of ints specifying the rows to select,
     *             where 0 indicates the first row in the display
     */
    public void setSelectionRows(int[] rows) {
        if (rows == null)
            return;

        TreePath paths[] = new TreePath[rows.length];
        for (int i = 0; i < rows.length; i++) {
            paths[i] = getPathForRow(i);
        }

        setSelectionPaths(paths);
    }

    /**
     * Adds the node identified by the specified TreePath to the current
     * selection. If any component of the path isn't visible, it is
     * made visible.
     *
     * @param path the TreePath to add
     */
    public void addSelectionPath(TreePath path) {
        getSelectionModel().addSelectionPath(path);
    }

    /**
     * Adds each path in the array of paths to the current selection.  If
     * any component of any of the paths isn't visible, it is
     * made visible.
     *
     * @param paths an array of TreePath objects that specifies the nodes
     *              to add
     */
    public void addSelectionPaths(TreePath[] paths) {
        getSelectionModel().addSelectionPaths(paths);
    }

    /**
     * Adds the path at the specified row to the current selection.
     *
     * @param row an int specifying the row of the node to add,
     *            where 0 is the first row in the display
     */
    public void addSelectionRow(int row) {
        int[] rows = {row};
        addSelectionRows(rows);
    }

    /**
     * Adds the paths at each of the specified rows to the current selection.
     *
     * @param rows an array of ints specifying the rows to add,
     *             where 0 indicates the first row in the display
     */
    public void addSelectionRows(int[] rows) {
        if (rows != null) {
            int numRows = rows.length;
            TreePath[] paths = new TreePath[numRows];

            for (int counter = 0; counter < numRows; counter++)
                paths[counter] = getPathForRow(rows[counter]);
            addSelectionPaths(paths);
        }
    }

    /**
     * Returns the last path component in the first node of the current
     * selection.
     *
     * @return the last Object in the first selected node's TreePath,
     *         or null if nothing is selected
     * @see TreePath#getLastPathComponent
     */
    public Object getLastSelectedPathComponent() {
        Object obj = null;
        TreePath selPath = getSelectionModel().getSelectionPath();
        if (selPath != null) {
            obj = selPath.getLastPathComponent();
        }
        return obj;
    }

    /**
     * Returns the path to the first selected node.
     *
     * @return the TreePath for the first selected node, or null if
     *         nothing is currently selected
     */
    public TreePath getSelectionPath() {
        return getSelectionModel().getSelectionPath();
    }

    /**
     * Returns the paths of all selected values.
     *
     * @return an array of TreePath objects indicating the selected
     *         nodes, or null if nothing is currently selected.
     */
    public TreePath[] getSelectionPaths() {
        return getSelectionModel().getSelectionPaths();
    }

    /**
     * Returns all of the currently selected rows.
     *
     * @return an array of ints that identifies all currently selected rows
     *         where 0 is the first row in the display
     */
    public int[] getSelectionRows() {
        return getSelectionModel().getSelectionRows();
    }

    /**
     * Returns the number of nodes selected.
     *
     * @return the number of nodes selected
     */
    public int getSelectionCount() {
        return selectionModel.getSelectionCount();
    }

    /**
     * Gets the first selected row.
     *
     * @return an int designating the first selected row, where 0 is the
     *         first row in the display
     */
    public int getMinSelectionRow() {
        return getSelectionModel().getMinSelectionRow();
    }

    /**
     * Gets the last selected row.
     *
     * @return an int designating the last selected row, where 0 is the
     *         first row in the display
     */
    public int getMaxSelectionRow() {
        return getSelectionModel().getMaxSelectionRow();
    }

    /**
     * Returns the row index of the last node added to the selection.
     *
     * @return an int giving the row index of the last node added to the
     *         selection, where 0 is the first row in the display
     */
    public int getLeadSelectionRow() {
        return getSelectionModel().getLeadSelectionRow();
    }

    /**
     * Returns the path of the last node added to the selection.
     *
     * @return the TreePath of the last node added to the selection.
     */
    public TreePath getLeadSelectionPath() {
        return getSelectionModel().getLeadSelectionPath();
    }

    /**
     * Returns true if the item identified by the path is currently selected.
     *
     * @param path a TreePath identifying a node
     * @return true if the node is selected
     */
    public boolean isPathSelected(TreePath path) {
        return getSelectionModel().isPathSelected(path);
    }

    /**
     * Returns true if the node identitifed by row is selected.
     *
     * @param row an int specifying a display row, where 0 is the first
     *            row in the display
     * @return true if the node is selected
     */
    public boolean isRowSelected(int row) {
        return getSelectionModel().isRowSelected(row);
    }

    /**
     * Removes the nodes between index0 and index1, inclusive, from the
     * selection.
     *
     * @param index0 an int specifying a display row, where 0 is the
     *               first row in the display
     * @param index0 an int specifying a second display row
     */
    public void removeSelectionInterval(int index0, int index1) {
        TreePath[] paths = getPathBetweenRows(index0, index1);
        this.getSelectionModel().removeSelectionPaths(paths);
    }

    /**
     * Removes the node identified by the specified path from the current
     * selection.
     *
     * @param path the TreePath identifying a node
     */
    public void removeSelectionPath(TreePath path) {
        getSelectionModel().removeSelectionPath(path);
    }

    /**
     * Removes the nodes identified by the specified paths from the
     * current selection.
     *
     * @param paths an array of TreePath objects that specifies the nodes
     *              to remove
     */
    public void removeSelectionPaths(TreePath[] paths) {
        getSelectionModel().removeSelectionPaths(paths);
    }

    /**
     * Removes the path at the index <code>row</code> from the current
     * selection.
     *
     * @param row the row identifying the node to remove
     */
    public void removeSelectionRow(int row) {
        int[] rows = {row};
        removeSelectionRows(rows);
    }

    public void removeSelectionRows(int[] rows) {
        TreePath[] paths = new TreePath[rows.length];
        for (int i = 0; i < rows.length; i++)
            paths[i] = getPathForRow(rows[i]);
        removeSelectionPaths(paths);
    }

    public int getMaximumExpandedDepth() {
        int max = 0;
        for (int i = 0; i < getRowCount(); i++)
            max = Math.max(max, getPathForRow(i).getPathCount());
        return max;
    }

    /**
     * Expand this tree row.
     * If tree is inside a {@link SScrollPane} try to
     * adjust pane, so that as much as possible new
     * nodes are visible.
     *
     * @param p the TreePath to expand
     */
    public void expandRow(TreePath p) {
        treeState.setExpandedState(p, true);
        /*
          if ( viewport != null )
          {
          int ccount = model.getChildCount( p.getLastPathComponent() );
          if ( ccount + 1 <= viewport.height )
          viewport.y += ccount;
          else
          viewport.y = treeState.getRowForPath( p );
			
          }
        */
        fireTreeExpanded(p);
        reload();
    }

    public void expandRow(int row) {
        expandRow(getPathForRow(row));
    }

    public void collapseRow(TreePath p) {
        treeState.setExpandedState(p, false);
        fireTreeCollapsed(p);
        reload();
    }

    public void collapseRow(int row) {
        collapseRow(getPathForRow(row));
    }

    public boolean isVisible(TreePath path) {
        if (path != null) {
            TreePath parentPath = path.getParentPath();

            if (parentPath != null)
                return isExpanded(parentPath);

            // Root.
            return true;
        }

        return false;
    }

    public boolean isExpanded(TreePath path) {
        return treeState.isExpanded(path);
    }

    protected void togglePathSelection(TreePath path) {
        if (path != null) {
            if (isPathSelected(path)) {
                removeSelectionPath(path);
            } else {
                addSelectionPath(path);
            }
        }
    }

    protected void togglePathExpansion(TreePath path)
            throws ExpandVetoException {
        if (path != null) {
            if (treeState.isExpanded(path)) {
                fireTreeWillExpand(path, false);
                collapseRow(path);
            } else {
                fireTreeWillExpand(path, true);
                expandRow(path);
            }
        }
    }

    /**
     * This is for plafs only!
     * With this parameter the tree expands the given node
     */
    public String getExpansionParameter(int row, boolean absolute) {
        return (absolute ? "j" : "h") + row;
    }

    /**
     * This is for plafs only!
     * With this parameter the tree selects the given node
     * ({@link #processLowLevelEvent})
     */
    public String getSelectionParameter(int row, boolean absolute) {
        return (absolute ? "a" : "b") + row;
    }


    public void processLowLevelEvent(String action, String[] values) {
        super.processLowLevelEvent(action, values);

        getSelectionModel().setDelayEvents(true);
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() < 2) continue; // incorrect format

            int row = Integer.parseInt(values[i].substring(1));

            if (row < 0) continue; // row not found...

            if (values[i].charAt(0) == 'b') {
                TreePath path = getPathForRow(row);
                //selection
                if (path != null) {
                    togglePathSelection(path);
                }
            } else if (values[i].charAt(0) == 'h') {
                TreePath path = getPathForRow(row);
                //selection
                if (path != null) {
                    requestedExpansionPaths.add(path);
                }
            } else if (values[i].charAt(0) == 'a') {
                TreePath path = getPathForAbsoluteRow(row);
                //selection
                if (path != null) {
                    togglePathSelection(path);
                }
            } else if (values[i].charAt(0) == 'j') {
                TreePath path = getPathForAbsoluteRow(row);
                //selection
                if (path != null) {
                    requestedExpansionPaths.add(path);
                }
            }
        }
        SForm.addArmedComponent(this);
        getSelectionModel().setDelayEvents(false);
    }

    public void setNodeIndentDepth(int depth) {
        nodeIndentDepth = depth;
    }

    public int getNodeIndentDepth() {
        return nodeIndentDepth;
    }

    public void setCellRenderer(STreeCellRenderer x) {
        renderer = x;
    }

    public STreeCellRenderer getCellRenderer() {
        return renderer;
    }

    /**
     * Creates an instance of TreeModelHandler.
     */
    protected TreeModelListener createTreeModelListener() {
        return new TreeModelHandler();
    }


    /**
     * Listens to the model and updates the expandedState accordingly
     * when nodes are removed, or changed.
     */
    protected class TreeModelHandler implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
            if (e == null)
                return;
            treeState.treeNodesChanged(e);
            reload();
        }

        public void treeNodesInserted(TreeModelEvent e) {
            if (e == null)
                return;
            treeState.treeNodesInserted(e);
            reload();
        }

        public void treeStructureChanged(TreeModelEvent e) {
            if (e == null)
                return;
            treeState.treeStructureChanged(e);
            reload();
        }

        public void treeNodesRemoved(TreeModelEvent e) {
            if (e == null)
                return;
            treeState.treeNodesRemoved(e);
            reload();
        }
    }

    public void setParent(SContainer p) {
        super.setParent(p);
        if (getCellRendererPane() != null)
            getCellRendererPane().setParent(p);
    }

    protected void setParentFrame(SFrame f) {
        super.setParentFrame(f);
        if (getCellRendererPane() != null)
            getCellRendererPane().setParentFrame(f);
    }


    // do not initalize with null!
    private SCellRendererPane cellRendererPane = new SCellRendererPane();


    public SCellRendererPane getCellRendererPane() {
        return cellRendererPane;
    }

    /**
     * Returns the maximum size of this tree.
     *
     * @return maximum size
     */
    public Rectangle getScrollableViewportSize() {
        return new Rectangle(0, 0, 1, getRowCount());
    }

    /*
     * Setzt den anzuzeigenden Teil
     */
    public void setViewportSize(Rectangle d) {
        Rectangle oldViewport = viewport;
        viewport = d;
        if ((viewport == null && oldViewport != null) ||
                (viewport != null && !viewport.equals(oldViewport)))
            reload();
    }

    public Rectangle getViewportSize() {
        return viewport;
    }

    public Dimension getPreferredExtent() {
        return null;
    }

    public void setCG(TreeCG cg) {
        super.setCG(cg);
    }
}


