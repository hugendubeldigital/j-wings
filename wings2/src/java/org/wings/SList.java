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

import org.wings.plaf.ListCG;
import org.wings.style.AttributeSet;
import org.wings.style.CSSStyleSheet;
import org.wings.style.Style;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <em>CAVEAT</em>
 * A list in a form has special implications to take care of:
 * Problem with a form request
 * is, that we should fire the selection change events not until the states
 * of all components are consistent. Unfortunately we cannot avoid events
 * before that
 * entirely. Problem is, that we use Swing Models for selection and they
 * don't know anything about asynchronous state change. They will fire their
 * events just after we set a state. But inside a form we have to change
 * many states of many components, all at once. And events should arise
 * first, after we set the new state of all components. So as a trade-off we
 * decided to use the setValueIsAdjusting in the ListSelectionModel as an
 * indicator, 
 * if components are consistent. That is, if you get an SelectionEvent with
 * getValueIsAdjusting true, you cannot be sure, that component states are
 * consistent, so don't use that events. But you will get an event without
 * isValueAdjusting. You can work with that event. If you want to avoid that
 * problem, just use the selection events from the list itself, register your
 * listener at SList rather than at the ListSelectionModel...
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
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SList
    extends SComponent
    implements Scrollable, LowLevelEventListener, ClickableRenderComponent, ListDataListener
{
    public static final String SELECTOR_SELECTION = "SELECTION";

    /**
     * The preferred extent of the list.
     */
    private int visibleRowCount = 8;

    /**
     *
     */
    private SListSelectionModel selectionModel;

    /**
     *
     */
    private ListModel dataModel;

    /**
     *
     */
    private SListCellRenderer cellRenderer;

    /**
     *
     */
    private ListSelectionHandler selectionHandler;

    /**
     * which extent of the component should be rendered
     */
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
     * Construct a SList that displays the elements in the specified model.
     */
    public SList(ListModel dataModel)
    {
        if (dataModel == null) {
            throw new IllegalArgumentException("dataModel must not be null");
        }

        if (this.dataModel != null) this.dataModel.removeListDataListener(this);
        this.dataModel = dataModel;
        this.dataModel.addListDataListener(this);
        selectionModel = createSelectionModel();
    }


    /**
     * Construct a SList that displays the elements in the specified
     * array.
     */
    public SList(final Object[] listData)
    {
        this ( new AbstractListModel() {
                public int getSize() {
                    return listData.length;
                }
                public Object getElementAt(int i) {
                    return listData[i];
                }
            });
    }


    /**
     * Construct a SList that displays the elements in the specified
     * Vector.
     */
    public SList(final List listData) {
        this ( new AbstractListModel() {
                public int getSize() {
                    return listData.size();
                }
                public Object getElementAt(int i) {
                    return listData.get(i);
                }
            });
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

    /**
     * Returns the cell renderer.
     *
     * @return the ListCellRenderer
     * @see #setCellRenderer
     */
    public final SListCellRenderer getCellRenderer() {
        return cellRenderer;
    }

    /**
     * Sets the renderer that's used to write out each cell in the list.
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
        reloadIfChange(ReloadManager.RELOAD_CODE, oldValue, cellRenderer);
    }


    /**
     * Return the background color.
     * @return the background color
     */
    public Color getSelectionBackground() {
        return dynamicStyles == null || dynamicStyles.get(SELECTOR_SELECTION) == null ? null : CSSStyleSheet.getBackground((AttributeSet)dynamicStyles.get(SELECTOR_SELECTION));
    }

    /**
     * Set the foreground color.
     * @param color the new foreground color
     */
    public void setSelectionBackground(Color color) {
        setAttribute(SELECTOR_SELECTION, Style.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
    }

    /**
     * Return the foreground color.
     * @return the foreground color
     */
    public Color getSelectionForeground() {
        return dynamicStyles == null || dynamicStyles.get(SELECTOR_SELECTION) == null ? null : CSSStyleSheet.getForeground((AttributeSet)dynamicStyles.get(SELECTOR_SELECTION));
    }

    /**
     * Set the foreground color.
     * @param color the new foreground color
     */
    public void setSelectionForeground(Color color) {
        setAttribute(SELECTOR_SELECTION, Style.COLOR, CSSStyleSheet.getAttribute(color));
    }

    /**
     * Set the font.
     * @param font the new font
     */
    public void setSelectionFont(SFont font) {
        setAttributes(SELECTOR_SELECTION, CSSStyleSheet.getAttributes(font));
    }

    /**
     * Return the font.
     * @return the font
     */
    public SFont getSelectionFont() {
        return dynamicStyles == null || dynamicStyles.get(SELECTOR_SELECTION) == null ? null : CSSStyleSheet.getFont((AttributeSet)dynamicStyles.get(SELECTOR_SELECTION));
    }

    /**
     * Return the preferred number of visible rows. If rendered as a form
     * component it is used for the size-attribute.
     *
     * @return the preferred number of rows to display
     * @see #setVisibleRowCount
     */
    public final int getVisibleRowCount() {
        return visibleRowCount;
    }

    /**
     * Set the preferred number of rows in the list that can be displayed
     * without a scollbar.
     * <p>
     * The default value of this property is 8.
     *
     * @param visibleRowCount  the preferred number of visible rows
     * @see #getVisibleRowCount
     * @beaninfo
     *       bound: true
     * description: The preferred number of cells that can be displayed without a scrollbar.
     */
    public void setVisibleRowCount(int visibleRowCount) {
        if ( this.visibleRowCount!=visibleRowCount ) {
            this.visibleRowCount = Math.max(0, visibleRowCount);
            reload(ReloadManager.RELOAD_CODE);
            //firePropertyChange("visibleRowCount", oldValue, visibleRowCount);
        }
    }


    /**
     * --- ListModel Support ---
     */


    /**
     * Returns the data model that holds the items.
     *
     * @return the ListModel
     * @see #setModel
     */
    public ListModel getModel() {
        return dataModel;
    }

    /**
     * Sets the model
     *
     * @param model  the ListModel that provides the list of items
     * @see #getModel
     * @beaninfo
     *       bound: true
     * description: The object that contains the data to be shownin the list.
     */
    public void setModel(ListModel model) {
        if (model == null) {
            throw new IllegalArgumentException("model must be non null");
        }
        if ( isDifferent(dataModel, model) ) {
            clearSelection();
            dataModel = model;
            dataModel.addListDataListener(this);
            reload(ReloadManager.RELOAD_CODE);
        }
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
     * A convenience method that constructs a ListModel from a List
     * and then applies setModel to it.
     *
     * @param listData a Vector containing the items to display in the list
     * @see #setModel
     */
    public void setListData(final List listData) {
        setModel(new AbstractListModel() {
                public int getSize() {
                    return listData.size();
                }
                public Object getElementAt(int i) {
                    return listData.get(i);
                }
            });
    }

    /**
     * creates the default selection model. It uses the swing
     * DefaultListSelectionModel, and wraps some methods to support 
     * {@link SConstants#NO_SELECTION}
     */
    protected SListSelectionModel createSelectionModel() {
        return new SDefaultListSelectionModel();
    }


    /**
     * Returns the current selection model. If selection mode is 
     * {@link SConstants#NO_SELECTION} it return <em>null</em>
     *
     * @return the ListSelectionModel that implements list selections. 
     * If selection mode is {@link SConstants#NO_SELECTION} it return
     * <em>null</em> 
     * @see #setSelectionModel
     * @see ListSelectionModel
     */
    public SListSelectionModel getSelectionModel() {
        return selectionModel;
    }


    /**
     * This method notifies all ListSelectionListeners that
     * the selection model has changed.
     *
     * @see #addListSelectionListener
     * @see #removeListSelectionListener
     * @see EventListenerList
     */
    protected void fireSelectionValueChanged(int firstIndex, int lastIndex,
                                             boolean isAdjusting)
    {
        Object[] listeners = getListenerList();
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


    /**
     * A handler that forwards ListSelectionEvents from the selectionModel
     * to the SList ListSelectionListeners.
     */
    private final class ListSelectionHandler 
        implements ListSelectionListener, Serializable 
    {

        public void valueChanged(ListSelectionEvent e) {
            fireSelectionValueChanged(e.getFirstIndex(),
                                      e.getLastIndex(),
                                      e.getValueIsAdjusting());
            reload(ReloadManager.RELOAD_CODE);
        }
    }


    /**
     * Add a listener to the list that's notified each time a change
     * to the selection occurs.
     *
     * @param listener A ListSelectionListener to be added
     * @see #getSelectionModel
     */
    public void addListSelectionListener(ListSelectionListener listener) {
        if (selectionHandler == null) {
            selectionHandler = new ListSelectionHandler();
            getSelectionModel().addListSelectionListener(selectionHandler);
        }

        addEventListener(ListSelectionListener.class, listener);
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
        removeEventListener(ListSelectionListener.class, listener);
    }


    /**
     * Returns an array of all the <code>ListSelectionListener</code>s added
     * to this JList with addListSelectionListener().
     *
     * @return all of the ListSelectionListener added 
     * @since 1.4
     */
    public ListSelectionListener[] getListSelectionListeners() {
        return (ListSelectionListener[])getListeners(
                ListSelectionListener.class);
    }


    /**
     * Set the selectionModel for the list.
     * The selection model keeps track of which items are selected.
     *
     * @see #getSelectionModel
     * @beaninfo
     *       bound: true
     * description: The selection model, recording which cells are selected.
     */
    public void setSelectionModel(SListSelectionModel selectionModel) {
        if (selectionModel == null) {
            throw new IllegalArgumentException("selectionModel must be non null");
        }

        if (selectionHandler != null) {
            this.selectionModel.removeListSelectionListener(selectionHandler);
            selectionModel.addListSelectionListener(selectionHandler);
        }

        SListSelectionModel oldValue = this.selectionModel;
        this.selectionModel = selectionModel;
        firePropertyChange("selectionModel", oldValue, selectionModel);
    }


    /**
     * Allow / permit multiple selection
     * <ul>
     * <li> <code>SINGLE_SELECTION</code>
     *   Only one list index can be selected at a time.
     * <li> <code>MULTIPLE_INTERVAL_SELECTION</code>
     *   Multiple items can be selected.
     * </ul>
     *
     * @param selectionMode single or multiple selections
     *
     * @see #getSelectionMode
     * @beaninfo
     * description: The selection mode
     *        enum: SINGLE_SELECTION            ListSelectionModel.SINGLE_SELECTION
     *              MULTIPLE_INTERVAL_SELECTION ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
     */
    public void setSelectionMode(int selectionMode) {
        selectionModel.setSelectionMode(selectionMode);
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
     * @return The smallest selected cell index.
     * @see ListSelectionModel#getMinSelectionIndex
     * @see #addListSelectionListener
     */
    public int getMinSelectionIndex() {
        return getSelectionModel().getMinSelectionIndex();
    }


    /**
     * @return The largest selected cell index.
     * @see ListSelectionModel#getMaxSelectionIndex
     * @see #addListSelectionListener
     */
    public int getMaxSelectionIndex() {
        return getSelectionModel().getMaxSelectionIndex();
    }


    /**
     * @return True if the specified index is selected.
     * @see ListSelectionModel#isSelectedIndex
     * @see #setSelectedIndex
     * @see #addListSelectionListener
     */
    public boolean isSelectedIndex(int index) {
        return getSelectionModel().isSelectedIndex(index);
    }


    /**
     * @return True if nothing is selected
     * @see ListSelectionModel#isSelectionEmpty
     * @see #clearSelection
     * @see #addListSelectionListener
     */
    public boolean isSelectionEmpty() {
        return getSelectionModel().isSelectionEmpty();
    }


    /**
     * @see ListSelectionModel#clearSelection
     * @see #isSelectionEmpty
     * @see #addListSelectionListener
     */
    public void clearSelection() {
        if (!getSelectionModel().isSelectionEmpty()) {
            getSelectionModel().clearSelection();
            reload(ReloadManager.RELOAD_CODE);
        }
    }


    /**
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
     * @param index0 The first index to remove from the selection
     * @param index1 The last index to remove from the selection
     * @see ListSelectionModel#removeSelectionInterval
     * @see #setSelectionInterval
     * @see #addSelectionInterval
     * @see #addListSelectionListener
     */
    public void removeSelectionInterval(int index0, int index1) {
        getSelectionModel().removeSelectionInterval(index0, index1);
    }


    /**
     * @param b the value for valueIsAdjusting
     * @see ListSelectionModel#setValueIsAdjusting
     */
    public void setValueIsAdjusting(boolean b) {
        getSelectionModel().setValueIsAdjusting(b);
    }

    /**
     * @return the value of valueIsAdjusting
     * @see ListSelectionModel#getValueIsAdjusting
     */
    public boolean getValueIsAdjusting() {
        return getSelectionModel().getValueIsAdjusting();
    }


    /**
     * Return an array of all of the selected indices.
     *
     * @return all selected indices.
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
     * Select some cells.
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
     * Return the values of the selected cells.
     * Returns only the selected elements which are in the model.
     * If the selection model indices a selection outside the the datamodel it is ignored 
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
            if (sm.isSelectedIndex(i) && i<dm.getSize() ) {
                rvTmp[n++] = dm.getElementAt(i);
            }
        }
        Object[] rv = new Object[n];
        System.arraycopy(rvTmp, 0, rv, 0, n);
        return rv;
    }


    /**
     * A convenience method that returns the first selected index.
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
     * or null, if nothing is selected.
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
     * Selects the specified object.
     *
     * @param anObject      the Object to be selected     
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
            setOrderType( null );
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

    public void fireIntermediateEvents() {
        getSelectionModel().fireDelayedIntermediateEvents();
    }

    public void fireFinalEvents() {
        // fire selection events...
        getSelectionModel().fireDelayedFinalEvents();
    }

    public boolean checkEpoch() {
        return true;
    }

    /*
     * Implement {@link LowLevelEventListener} interface.
     * @param action the name
     * @param value the value
     */
    public void processLowLevelEvent(String action, String[] values) {
        super.processLowLevelEvent(action, values);

        // delay events...
        getSelectionModel().setDelayEvents(true);

        getSelectionModel().setValueIsAdjusting(true);
        // in a form, we only get events for selected items, so for every
        // selected item, which is not in values, deselect it...
        if (getShowAsFormComponent()) {

            ArrayList selectedIndices = new ArrayList();
            for ( int i=0; i<values.length; i++ ) {


                if ( values[i].length()<2 ) continue; // false format

                String indexString = values[i].substring(1);
                try {
                    int index = Integer.parseInt(indexString);
                    
                    // in a form all parameters are select parameters...
                    if ( values[i].charAt(0)=='a' ) {
                        selectedIndices.add(new Integer(index));
                        addSelectionInterval(index, index);
                    }
                } catch (Exception ex) {
                    // ignore, this is not the correct format...
                }
            }
            // remove all selected indices, which are not explicitely selected
            // by a parameter
            for ( int i=0; i<getModel().getSize(); i++ ) {
                if ( isSelectedIndex(i) && 
                     !selectedIndices.contains(new Integer(i)) ) {
                        removeSelectionInterval(i, i);
                }
            }
        } else {

            for ( int i=0; i<values.length; i++ ) {

                if ( values[i].length()<2 ) continue; // false format

                // first char is select/deselect operator
                String indexString = values[i].substring(1);
                try {
                    int index = Integer.parseInt(indexString);
                    
                    if ( values[i].charAt(0)=='a' ) {
                        addSelectionInterval(index, index);
                    } else if ( values[i].charAt(0)=='r' ) {
                        removeSelectionInterval(index, index);
                    } // else ignore, this is not the correct format...
                } catch (Exception ex) {
                }

            }
        }
        getSelectionModel().setValueIsAdjusting(false);


        getSelectionModel().setDelayEvents(false);

        SForm.addArmedComponent(this);
    }

    //-- Scrollable interface
    /**
     * Return the scrollable viewport size.
     *
     * @return the scrollable viewport dimension
     */
    public Rectangle getScrollableViewportSize() {
        return new Rectangle(0, 0, 1, dataModel.getSize());
    }

    /**
     * Set the visible viewport size.
     * @param d the visible viewport size
     */
    public void setViewportSize(Rectangle d) {
        if ( isDifferent(viewport, d) ) {
            viewport = d;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * Return the visible viewport size.
     *
     * @return the visible viewport size
     */
    public final Rectangle getViewportSize() { return viewport; }

    public Dimension getPreferredExtent() {
        return new Dimension(1, Math.min(getVisibleRowCount(), getModel().getSize()));
    }
    //--- /Scrollable Interface

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
     */
    public void removeCellRendererPane() {
        cellRendererPane.setParent(null);
        cellRendererPane = null;
    }

    public void setCG(ListCG cg) {
        super.setCG(cg);
    }

    public String getToggleSelectionParameter(int index) {
        return isSelectedIndex(index) ? getDeselectionParameter(index) : 
            getSelectionParameter(index);
    }

    public String getSelectionParameter(int index) {
        return "a" + Integer.toString(index);
    }

    public String getDeselectionParameter(int index) {
        return "r" + Integer.toString(index);
    }

    /****
     * Changes of the List Model should reflect in a reload if possible 
     **/
    
    public void contentsChanged(javax.swing.event.ListDataEvent e) {
      reload(ReloadManager.RELOAD_CODE);
    }
    
    public void intervalAdded(javax.swing.event.ListDataEvent e) {
      reload(ReloadManager.RELOAD_CODE);
    }
    
    public void intervalRemoved(javax.swing.event.ListDataEvent e) {
      reload(ReloadManager.RELOAD_CODE);
    }
    
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
