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

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @see javax.swing.ListModel
 * @see SDefaultListModel
 * @see javax.swing.ListSelectionModel
 * @see SListCellRenderer
 *
 * @beaninfo
 *   attribute: isContainer false
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SList
    extends SComponent
    implements Scrollable, SGetListener
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "ListCG";

    private boolean showAsFormComponent = true;
    private int visibleRowCount = 8;
    private Color selectionForeground;
    private Color selectionBackground;

    private ListSelectionModel selectionModel;
    private ListModel dataModel;
    private SListCellRenderer cellRenderer;
    private ListSelectionListener selectionListener;

    /**
     * Need this for determination, which selections have changed.
     */
    protected boolean[] selection = null;

    /**
     * Need this for determination, which selections have changed.
     */
    protected boolean[] oldSelection = null;

    private boolean hidden = true;

    protected EventListenerList listenerList = new EventListenerList();
    private Rectangle viewport = null;

    /**
     * <li type="...">
     */
    protected String type = SConstants.UNORDERED_LIST;

    /**
     * <li type="...">
     */
    protected String orderType = null;

    /**
     * <li start="...">
     */
    protected int start = 0;

    /**
     * Construct a SList that displays the elements in the specified,
     * non-null model.  All SList constructors delegate to this one.
     */
    public SList(ListModel dataModel)
    {
        if (dataModel == null) {
            throw new IllegalArgumentException("dataModel must not be null");
        }

        this.dataModel = dataModel;
        selectionModel = createSelectionModel();
    }


    /**
     * Construct a SList that displays the elements in the specified
     * array.  This constructor just delegates to the ListModel
     * constructor.
     */
    public SList(final Object[] listData)
    {
        this ( new AbstractListModel() {
            public int getSize() {
                return listData.length;
            }
            public Object getElementAt(int i) {
                return listData[i];
            } } );
    }


    /**
     * Construct a SList that displays the elements in the specified
     * Vector.  This constructor just delegates to the ListModel
     * constructor.
     */
    public SList(final Vector listData) {
        this ( new AbstractListModel() {
            public int getSize() {
                return listData.size();
            }
            public Object getElementAt(int i) {
                return listData.elementAt(i);
            } } );
    }


    /**
     * Constructs a SList with an empty model.
     */
    public SList() {
        this ( new AbstractListModel() {
            public int getSize() {
                return 0;
            }
            public Object getElementAt(int i) {
                return "No Data Model";
            } } );
    }

    public void setShowAsFormComponent(boolean showAsFormComponent) {
        this.showAsFormComponent = showAsFormComponent;
    }

    public boolean getShowAsFormComponent() {
        return showAsFormComponent && getResidesInForm();
    }

    /**
     * Returns the object that renders the list items.
     *
     * @return the ListCellRenderer
     * @see #setCellRenderer
     */
    public SListCellRenderer getCellRenderer() {
        return cellRenderer;
    }

    /**
     * Sets the renderer that's used to write out each cell in the list.
     * <p>
     * The default value of this property is provided by the ListCG.
     * <p>
     * This is a JavaBeans bound property.
     *
     * @param cellRenderer the SListCellRenderer that paints list cells
     * @see #getCellRenderer
     * @beaninfo
     *       bound: true
     * description: The component used to draw the cells.
     */
    public void setCellRenderer(SListCellRenderer cellRenderer) {
        SListCellRenderer oldValue = this.cellRenderer;
        this.cellRenderer = cellRenderer;
        //firePropertyChange("cellRenderer", oldValue, cellRenderer);
    }


    /**
     * Returns the foreground color.
     *
     * @return the Color object for the foreground property
     * @see #setSelectionForeground
     * @see #setSelectionBackground
     */
    public Color getSelectionForeground() {
        return selectionForeground;
    }


    /**
     * Set the foreground color for selected cells.  Cell renderers
     * can use this color to render text and graphics for selected
     * cells.
     * <p>
     * The default value of this property is defined by the look
     * and feel implementation.
     * <p>
     * This is a JavaBeans bound property.
     *
     * @param selectionForeground  the Color to use in the foreground
     *                             for selected list items
     * @see #getSelectionForeground
     * @see #setSelectionBackground
     * @see #setForeground
     * @see #setBackground
     * @see #setFont
     * @beaninfo
     *       bound: true
     * description: The foreground color of selected cells.
     */
    public void setSelectionForeground(Color selectionForeground) {
        Color oldValue = this.selectionForeground;
        this.selectionForeground = selectionForeground;
        //firePropertyChange("selectionForeground", oldValue, selectionForeground);
    }


    /**
     * Returns the background color for selected cells.
     *
     * @return the Color used for the background of selected list items
     * @see #setSelectionBackground
     * @see #setSelectionForeground
     */
    public Color getSelectionBackground() {
        return selectionBackground;
    }


    /**
     * Set the background color for selected cells.  Cell renderers
     * can use this color to the fill selected cells.
     * <p>
     * The default value of this property is defined by the look
     * and feel implementation.
     * <p>
     * This is a JavaBeans bound property.
     *
     * @param selectionBackground  the Color to use for the background
     *                             of selected cells
     * @see #getSelectionBackground
     * @see #setSelectionForeground
     * @see #setForeground
     * @see #setBackground
     * @see #setFont
     * @beaninfo
     *       bound: true
     * description: The background color of selected cells.
     */
    public void setSelectionBackground(Color selectionBackground) {
        Color oldValue = this.selectionBackground;
        this.selectionBackground = selectionBackground;
        //firePropertyChange("selectionBackground", oldValue, selectionBackground);
    }


    /**
     * Return the preferred number of visible rows. If rendered as a form
     * component it is used for the size-attribute.
     *
     * @return an int indicating the preferred number of rows to display
     *         without using a scrollbar
     * @see #setVisibleRowCount
     */
    public int getVisibleRowCount() {
        return visibleRowCount;
    }

    /**
     * Set the preferred number of rows in the list that can be displayed
     * without a scollbar.
     * <p>
     * The default value of this property is 8.
     * <p>
     * This is a JavaBeans bound property.
     *
     * @param visibleRowCount  an int specifying the preferred number of
     *                         visible rows
     * @see #getVisibleRowCount
     * @beaninfo
     *       bound: true
     * description: The preferred number of cells that can be displayed without a scrollbar.
     */
    public void setVisibleRowCount(int visibleRowCount) {
        int oldValue = this.visibleRowCount;
        this.visibleRowCount = Math.max(0, visibleRowCount);
        //firePropertyChange("visibleRowCount", oldValue, visibleRowCount);
    }


    /**
     * --- ListModel Support ---
     */


    /**
     * Returns the data model that holds the list of items displayed
     * by the SList component.
     *
     * @return the ListModel that provides the displayed list of items
     * @see #setModel
     */
    public ListModel getModel() {
        return dataModel;
    }

    /**
     * Sets the model that represents the contents or "value" of the
     * list and clears the list selection after notifying PropertyChangeListeners.
     * <p>
     * This is a JavaBeans bound property.
     *
     * @param model  the ListModel that provides the list of items for display
     * @see #getModel
     * @beaninfo
     *       bound: true
     *   attribute: visualUpdate true
     * description: The object that contains the data to be drawn by this SList.
     */
    public void setModel(ListModel model) {
        if (model == null) {
            throw new IllegalArgumentException("model must be non null");
        }
        ListModel oldValue = dataModel;
        dataModel = model;
        //firePropertyChange("model", oldValue, dataModel);
        clearSelection();
    }


    /**
     * A convenience method that constructs a ListModel from an array of Objects
     * and then applies setModel to it.
     *
     * @param listData an array of Objects containing the items to display
     *                 in the list
     * @see #setModel
     */
    public void setListData(final Object[] listData) {
        setModel(new AbstractListModel() {
            public int getSize() {
                return listData.length;
            }
            public Object getElementAt(int i) {
                return listData[i];
            } } );
    }


    /**
     * A convenience method that constructs a ListModel from a Vector
     * and then applies setModel to it.
     *
     * @param listData a Vector containing the items to display in the list
     * @see #setModel
     */
    public void setListData(final Vector listData) {
        setModel(new AbstractListModel() {
            public int getSize() {
                return listData.size();
            }
            public Object getElementAt(int i) {
                return listData.elementAt(i);
            } } );
    }


    /**
     * --- ListSelectionModel delegations and extensions ---
     */


    /**
     * Returns an instance of DefaultListSelectionModel.  This
     * method is used by the constructor to initialize the
     * selectionModel property.
     *
     * @return The ListSelectionModel used by this SList.
     * @see #setSelectionModel
     * @see DefaultListSelectionModel
     */
    protected ListSelectionModel createSelectionModel() {
        return new DefaultListSelectionModel();
    }


    /**
     * Returns the value of the current selection model. The selection
     * model handles the task of making single selections, selections
     * of contiguous ranges, and non-contiguous selections.
     *
     * @return the ListSelectionModel that implements list selections
     * @see #setSelectionModel
     * @see ListSelectionModel
     */
    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }


    /**
     * This method notifies SList ListSelectionListeners that
     * the selection model has changed.  It's used to forward
     * ListSelectionEvents from the selectionModel to the
     * ListSelectionListeners added directly to the SList.
     *
     * @see #addListSelectionListener
     * @see #removeListSelectionListener
     * @see EventListenerList
     */
    protected void fireSelectionValueChanged(int firstIndex, int lastIndex,
                                             boolean isAdjusting)
    {
        Object[] listeners = listenerList.getListenerList();
        ListSelectionEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListSelectionListener.class) {
                if (e == null) {
                    e = new ListSelectionEvent(this, firstIndex, lastIndex,
                                               isAdjusting);
                }
                ((ListSelectionListener)listeners[i+1]).valueChanged(e);
            }
        }
    }


    /* A ListSelectionListener that forwards ListSelectionEvents from
     * the selectionModel to the SList ListSelectionListeners.  The
     * forwarded events only differ from the originals in that their 
     * source is the SList instead of the selectionModel itself.
     */
    private class ListSelectionHandler implements ListSelectionListener, Serializable 
    {
        public void valueChanged(ListSelectionEvent e) {
            fireSelectionValueChanged(e.getFirstIndex(),
                                      e.getLastIndex(),
                                      e.getValueIsAdjusting());
        }
    }


    /**
     * Add a listener to the list that's notified each time a change
     * to the selection occurs.  Listeners added directly to the SList
     * will have their ListSelectionEvent.getSource() == this SList
     * (instead of the ListSelectionModel).
     *
     * @param listener The ListSelectionListener to add.
     * @see #getSelectionModel
     */
    public void addListSelectionListener(ListSelectionListener listener) {
        if (selectionListener == null) {
            selectionListener = new ListSelectionHandler();
            getSelectionModel().addListSelectionListener(selectionListener);
        }

        listenerList.add(ListSelectionListener.class, listener);
    }


    /**
     * Remove a listener from the list that's notified each time a
     * change to the selection occurs.
     *
     * @param listener The ListSelectionListener to remove.
     * @see #addListSelectionListener
     * @see #getSelectionModel
     */
    public void removeListSelectionListener(ListSelectionListener listener) {
        listenerList.remove(ListSelectionListener.class, listener);
    }


    /**
     * Set the selectionModel for the list to a non-null ListSelectionModel
     * implementation. The selection model handles the task of making single
     * selections, selections of contiguous ranges, and non-contiguous
     * selections.
     * <p>
     * This is a JavaBeans bound property.
     *
     * @return selectionModel  the ListSelectionModel that implements
     *                         list selections
     * @see #getSelectionModel
     * @beaninfo
     *       bound: true
     * description: The selection model, recording which cells are selected.
     */
    public void setSelectionModel(ListSelectionModel selectionModel) {
        if (selectionModel == null) {
            throw new IllegalArgumentException("selectionModel must be non null");
        }

        /* Remove the forwarding ListSelectionListener from the old
         * selectionModel, and add it to the new one, if neccessary.
         */
        if (selectionListener != null) {
            this.selectionModel.removeListSelectionListener(selectionListener);
            selectionModel.addListSelectionListener(selectionListener);
        }

        ListSelectionModel oldValue = this.selectionModel;
        this.selectionModel = selectionModel;
        //firePropertyChange("selectionModel", oldValue, selectionModel);
    }


    /**
     * Determines whether single-item or multiple-item
     * selections are allowed.
     * The following selectionMode values are allowed:
     * <ul>
     * <li> <code>SINGLE_SELECTION</code>
     *   Only one list index can be selected at a time.  In this
     *   mode the setSelectionInterval and addSelectionInterval
     *   methods are equivalent, and they only the first index
     *   argument is used.
     * <li> <code>SINGLE_INTERVAL_SELECTION</code>
     *   One contiguous index interval can be selected at a time.
     *   In this mode setSelectionInterval and addSelectionInterval
     *   are equivalent.
     * <li> <code>MULTIPLE_INTERVAL_SELECTION</code>
     *   In this mode, there's no restriction on what can be selected.
     * </ul>
     *
     * @param selectionMode an int specifying the type of selections
     *        that are permissible
     * @see #getSelectionMode
     * @beaninfo
     * description: The selection mode.
     *        enum: SINGLE_SELECTION            ListSelectionModel.SINGLE_SELECTION
     *              SINGLE_INTERVAL_SELECTION   ListSelectionModel.SINGLE_INTERVAL_SELECTION
     *              MULTIPLE_INTERVAL_SELECTION ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
     */
    public void setSelectionMode(int selectionMode) {
        getSelectionModel().setSelectionMode(selectionMode);
    }

    /**
     * Returns whether single-item or multiple-item selections are allowed.
     * @return The value of the selectionMode property.
     * @see #setSelectionMode
     */
    public int getSelectionMode() {
        return getSelectionModel().getSelectionMode();
    }


    /**
     * Returns the first index argument from the most recent addSelectionInterval
     * or setSelectionInterval call.
     * This is a convenience method that just delegates to the selectionModel.
     *
     * @return The index that most recently anchored an interval selection.
     * @see ListSelectionModel#getAnchorSelectionIndex
     * @see #addSelectionInterval
     * @see #setSelectionInterval
     * @see #addListSelectionListener
     */
    public int getAnchorSelectionIndex() {
        return getSelectionModel().getAnchorSelectionIndex();
    }


    /**
     * Returns the second index argument from the most recent addSelectionInterval
     * or setSelectionInterval call.
     * This is a convenience method that just  delegates to the selectionModel.
     *
     * @return The index that most recently ended a interval selection.
     * @see ListSelectionModel#getLeadSelectionIndex
     * @see #addSelectionInterval
     * @see #setSelectionInterval
     * @see #addListSelectionListener
     * @beaninfo
     * description: The lead selection index.
     */
    public int getLeadSelectionIndex() {
        return getSelectionModel().getLeadSelectionIndex();
    }


    /**
     * Returns the smallest selected cell index.
     * This is a convenience method that just delegates to the selectionModel.
     *
     * @return The smallest selected cell index.
     * @see ListSelectionModel#getMinSelectionIndex
     * @see #addListSelectionListener
     */
    public int getMinSelectionIndex() {
        return getSelectionModel().getMinSelectionIndex();
    }


    /**
     * Returns the largest selected cell index.
     * This is a convenience method that just delegates to the selectionModel.
     *
     * @return The largest selected cell index.
     * @see ListSelectionModel#getMaxSelectionIndex
     * @see #addListSelectionListener
     */
    public int getMaxSelectionIndex() {
        return getSelectionModel().getMaxSelectionIndex();
    }


    /**
     * Returns true if the specified index is selected.
     * This is a convenience method that just delegates to the selectionModel.
     *
     * @return True if the specified index is selected.
     * @see ListSelectionModel#isSelectedIndex
     * @see #setSelectedIndex
     * @see #addListSelectionListener
     */
    public boolean isSelectedIndex(int index) {
        return getSelectionModel().isSelectedIndex(index);
    }


    /**
     * Returns true if nothing is selected
     * This is a convenience method that just delegates to the selectionModel.
     *
     * @return True if nothing is selected
     * @see ListSelectionModel#isSelectionEmpty
     * @see #clearSelection
     * @see #addListSelectionListener
     */
    public boolean isSelectionEmpty() {
        return getSelectionModel().isSelectionEmpty();
    }


    /**
     * Clears the selection - after calling this method isSelectionEmpty()
     * will return true.
     * This is a convenience method that just delegates to the selectionModel.
     *
     * @see ListSelectionModel#clearSelection
     * @see #isSelectionEmpty
     * @see #addListSelectionListener
     */
    public void clearSelection() {
        getSelectionModel().clearSelection();
    }


    /**
     * Select the specified interval.  Both the anchor and lead indices are
     * included.  It's not neccessary for anchor to be less than lead.
     * This is a convenience method that just delegates to the selectionModel.
     *
     * @param anchor The first index to select
     * @param lead The last index to select
     * @see ListSelectionModel#setSelectionInterval
     * @see #addSelectionInterval
     * @see #removeSelectionInterval
     * @see #addListSelectionListener
     */
    public void setSelectionInterval(int anchor, int lead) {
        getSelectionModel().setSelectionInterval(anchor, lead);
    }


    /**
     * Set the selection to be the union of the specified interval with current
     * selection.  Both the anchor and lead indices are
     * included.  It's not neccessary for anchor to be less than lead.
     * This is a convenience method that just delegates to the selectionModel.
     *
     * @param anchor The first index to add to the selection
     * @param lead The last index to add to the selection
     * @see ListSelectionModel#addSelectionInterval
     * @see #setSelectionInterval
     * @see #removeSelectionInterval
     * @see #addListSelectionListener
     */
    public void addSelectionInterval(int anchor, int lead) {
        getSelectionModel().addSelectionInterval(anchor, lead);
    }


    /**
     * Set the selection to be the set difference of the specified interval
     * and the current selection.  Both the anchor and lead indices are
     * removed.  It's not neccessary for anchor to be less than lead.
     * This is a convenience method that just delegates to the selectionModel.
     *
     * @param anchor The first index to remove from the selection
     * @param lead The last index to remove from the selection
     * @see ListSelectionModel#removeSelectionInterval
     * @see #setSelectionInterval
     * @see #addSelectionInterval
     * @see #addListSelectionListener
     */
    public void removeSelectionInterval(int index0, int index1) {
        getSelectionModel().removeSelectionInterval(index0, index1);
    }


    /**
     * Sets the data model's isAdjusting property true, so that
     * a single event will be generated when all of the selection
     * events have finished (for example, when the mouse is being
     * dragged over the list in selection mode).
     *
     * @param b the boolean value for the property value
     * @see ListSelectionModel#setValueIsAdjusting
     */
    public void setValueIsAdjusting(boolean b) {
        getSelectionModel().setValueIsAdjusting(b);
    }

    /**
     * Returns the value of the data model's isAdjusting property.
     * This value is true if multiple changes are being made.
     *
     * @return true if multiple selection-changes are occuring, as
     *         when the mouse is being dragged over the list
     * @see ListSelectionModel#getValueIsAdjusting
     */
    public boolean getValueIsAdjusting() {
        return getSelectionModel().getValueIsAdjusting();
    }


    /**
     * Return an array of all of the selected indices in increasing
     * order.
     *
     * @return All of the selected indices, in increasing order.
     * @see #removeSelectionInterval
     * @see #addListSelectionListener
     */
    public int[] getSelectedIndices() {
        ListSelectionModel sm = getSelectionModel();
        int iMin = sm.getMinSelectionIndex();
        int iMax = sm.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0)) {
            return new int[0];
        }

        int[] rvTmp = new int[1+ (iMax - iMin)];
        int n = 0;
        for(int i = iMin; i <= iMax; i++) {
            if (sm.isSelectedIndex(i)) {
                rvTmp[n++] = i;
            }
        }
        int[] rv = new int[n];
        System.arraycopy(rvTmp, 0, rv, 0, n);
        return rv;
    }


    /**
     * Select a single cell.
     *
     * @param index The index of the one cell to select
     * @see ListSelectionModel#setSelectionInterval
     * @see #isSelectedIndex
     * @see #addListSelectionListener
     * @beaninfo
     * description: The index of the selected cell.
     */
    public void setSelectedIndex(int index) {
        getSelectionModel().setSelectionInterval(index, index);
    }


    /**
     * Select a set of cells.
     *
     * @param indices The indices of the cells to select
     * @see ListSelectionModel#addSelectionInterval
     * @see #isSelectedIndex
     * @see #addListSelectionListener
     */
    public void setSelectedIndices(int[] indices) {
        ListSelectionModel sm = getSelectionModel();
        sm.clearSelection();
        for(int i = 0; i < indices.length; i++) {
            sm.addSelectionInterval(indices[i], indices[i]);
        }
    }


    /**
     * Return an array of the values for the selected cells.
     * The returned values are sorted in increasing index order.
     *
     * @return the selected values
     * @see #isSelectedIndex
     * @see #getModel
     * @see #addListSelectionListener
     */
    public Object[] getSelectedValues() {
        ListSelectionModel sm = getSelectionModel();
        ListModel dm = getModel();

        int iMin = sm.getMinSelectionIndex();
        int iMax = sm.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0)) {
            return new Object[0];
        }

        Object[] rvTmp = new Object[1+ (iMax - iMin)];
        int n = 0;
        for(int i = iMin; i <= iMax; i++) {
            if (sm.isSelectedIndex(i)) {
                rvTmp[n++] = dm.getElementAt(i);
            }
        }
        Object[] rv = new Object[n];
        System.arraycopy(rvTmp, 0, rv, 0, n);
        return rv;
    }


    /**
     * A convenience method that returns the first selected index.
     * Returns -1 if there is no selected item.
     *
     * @return The first selected index.
     * @see #getMinSelectionIndex
     * @see #addListSelectionListener
     */
    public int getSelectedIndex() {
        return getMinSelectionIndex();
    }


    /**
     * A convenience method that returns the first selected value
     * or null, if the selection is empty.
     *
     * @return The first selected value.
     * @see #getMinSelectionIndex
     * @see #getModel
     * @see #addListSelectionListener
     */
    public Object getSelectedValue() {
        int i = getMinSelectionIndex();
        return (i == -1) ? null : getModel().getElementAt(i);
    }


    /**
     * Selects the specified object from the list.
     *
     * @param anObject      the Object to select     
     * @param shouldScroll  true if the list should scroll to display
     *                      the selected object
     */
    public void setSelectedValue(Object anObject) {
        if(anObject == null)
            setSelectedIndex(-1);
        else if(!anObject.equals(getSelectedValue())) {
            int i,c;
            ListModel dm = getModel();
            for(i=0,c=dm.getSize();i<c;i++)
                if(anObject.equals(dm.getElementAt(i))){
                    setSelectedIndex(i);

                    //if(shouldScroll)
                    //    ensureIndexIsVisible(i);
                    return;
                }
            setSelectedIndex(-1);
        }
    }


    /*
     * Sets the list type. Use one of the following types:
     * <UL>
     *  <LI>{@link SConstants#ORDERED_LIST}
     *  <LI>{@link SConstants#UNORDERED_LIST}
     *  <LI>{@link SConstants#MENU_LIST}
     *  <LI>{@link SConstants#DIR_LIST}
     * </UL>
     * null sets default list.
     *
     * @param t the type
     */
    public void setType(String t) {
        if ( t != null )
            type = t;
        else
            type = SConstants.UNORDERED_LIST;
    }

    /**
     * Return the type.
     *
     * @return the type;
     */
    public String getType() {
        return type;
    }

    /**
     * <li type="...">
     *
     * @param t
     */
    public void setOrderType(String t) {
        orderType = t;
    }

    /**
     * <li type="...">
     *
     * @return
     */
    public String getOrderType() {
        return orderType;
    }

    /*
     * <li type="...">
     * <code>null</code> is default style.
     */
    public void setType(String[] t) {
        if ( t == null ) {
            setType( (String) null );
            setOrderType( (String) null );
        }
        else if ( t.length == 2 ) {
            setType(t[0]);
            setOrderType(t[1]);
        }
    }

    /**
     * <li start="...">
     * TODO: documentation
     *
     * @param s
     */
    public void setStart(int s) {
        start = s;
    }

    /**
     * <li start="...">
     *
     * @return
     */
    public int getStart() {
        return start;
    }


    private void syncSelection() {
	if (dataModel == null)
            return;

        if (selection == null || dataModel.getSize() != selection.length)
            selection = new boolean[dataModel.getSize()];

        for (int i=0; i < selection.length; i++)
            selection[i] = false;
    }

    /**
     * Commit selection changes to the model.
     * The model will generate events.
     */
    protected void fireEvents() {
        setValueIsAdjusting(true);
        for (int i=0; i < selection.length; i++) {
            if (selection[i] != isSelectedIndex(i)) {
                if (selection[i])
                    addSelectionInterval(i, i);
                else
                    removeSelectionInterval(i, i);
            }
        }
        setValueIsAdjusting(false);
    }

    /*
     * Implement GetListener interface.
     * @param action the name
     * @param value the value
     */
    public void getPerformed(String action, String value) {
        try {
            int sel = Integer.parseInt(value);

            if (getShowAsFormComponent()) {
                if (hidden)
                    syncSelection();
                
                if (sel < 0) {
                    hidden = true;
                    fireEvents();
                }
                else {
                    selection[sel] = true;
                    hidden = false;
                }
            }
            else {
                if (isSelectedIndex(sel))
                    removeSelectionInterval(sel, sel);
                else
                    addSelectionInterval(sel, sel);
            }
        }
        catch (Exception e) {
            System.err.println("Cannot parse expected integer");
            e.printStackTrace();
        }
    }

    /**
     * Return the scrollable viewport size, i.e. the number of entries
     * in the model.
     *
     * @return the scrollable viewport dimension
     */
    public Dimension getScrollableViewportSize() {
        return new Dimension(1, dataModel.getSize());
    }

    /**
     * Set the visible viewport size.
     * @param d the visible viewport size
     */
    public void setViewportSize(Rectangle d) {
        viewport = d;
    }

    /**
     * Return the visible viewport size.
     *
     * @return the visible viewport size
     */
    public Rectangle getViewportSize() { return viewport; }


    public void setParent(SContainer p) {
        super.setParent(p);
        if ( getCellRendererPane() != null )
            getCellRendererPane().setParent(p);
    }

    protected void setParentFrame(SFrame f) {
        super.setParentFrame(f);
        if ( getCellRendererPane() != null )
            getCellRendererPane().setParentFrame(f);
    }


    // do not initalize with null!
    private SCellRendererPane cellRendererPane;

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setCellRendererPane(SCellRendererPane c) {
        cellRendererPane=c;
        cellRendererPane.setParent(getParent());
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SCellRendererPane getCellRendererPane() {
        return cellRendererPane;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public void removeCellRendererPane() {
        cellRendererPane.setParent(null);
        cellRendererPane = null;
    }


    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "ListCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
