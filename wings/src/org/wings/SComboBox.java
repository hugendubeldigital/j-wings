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
 * @see javax.swing.ComboBoxModel
 * @see SListCellRenderer
 *
 * @beaninfo
 *   attribute: isContainer false
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SComboBox
    extends SComponent
    implements SGetListener, ListDataListener, ItemSelectable
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "ComboBoxCG";

    /**
     * The model.
     * @see javax.swing.ComboBoxModel
     */
    protected ComboBoxModel    dataModel;
    
    /**
     * The renderer used for cell rendering each cell.
     * @see SListCellRenderer
     */
    protected SListCellRenderer renderer;
    protected int maximumRowCount = 8;
    protected Object lastSelectedItem = null;
    protected String actionCommand = "comboBoxChanged";
    
    transient protected EventListenerList listenerList = new EventListenerList();
    
    boolean firedActionEventOnContentsChanged = false;
    boolean firingActionEvent = false;

    /**
     * Creates a SComboBox that takes its items from an existing ComboBoxModel.
     *
     * @param model the ComboBoxModel that provides the displayed list of items
     */
    public SComboBox(ComboBoxModel model) {
        setModel(model);
    }

    /** 
     * Creates a SComboBox that contains the elements in the specified array.
     */
    public SComboBox(final Object items[]) {
        setModel(new DefaultComboBoxModel(items));
    }

    /**
     * Creates a SComboBox that contains the elements in the specified Vector.
     */
    public SComboBox(Vector items) {
        setModel(new DefaultComboBoxModel(items));
    }

    /**
     * Creates a SComboBox with a default data model.
     * The default data model is an empty list of objects. 
     * Use <code>addItem</code> to add items.
     */
    public SComboBox() {
        setModel(new DefaultComboBoxModel());
    }

    /**
     * Sets the data model that the SComboBox uses to obtain the list of items.
     *
     * @param model the ComboBoxModel that provides the displayed list of items
     * 
     * @beaninfo
     *        bound: true
     *  description: Model that the combo box uses to get data to display.
     */
    public void setModel(ComboBoxModel model) {
        ComboBoxModel oldModel = dataModel;
        if (dataModel != null)
            dataModel.removeListDataListener(this);
        dataModel = model;
        firePropertyChange("model", oldModel, dataModel);
        dataModel.addListDataListener(this);
    }

    /**
     * Returns the data model currently used by the SComboBox.
     *
     * @return the ComboBoxModel that provides the displayed list of items
     */
    public ComboBoxModel getModel() {
        return dataModel;
    }

    /**
     * Sets the maximum number of rows the SComboBox displays.
     * If shown as a formComponent, this value is used for the <em>size</em>-attribute.
     *
     * @param count <em>size</em>-attribute
     * @beaninfo
     *    preferred: true
     */
    public void setMaximumRowCount(int count) {
        int oldCount = maximumRowCount;
        maximumRowCount = count;
        firePropertyChange("maximumRowCount", oldCount, maximumRowCount);
    }

    /**
     * Returns the <em>size</em>-attribute
     *
     * @return the value used for the <em>size</em>-attribute
     */
    public int getMaximumRowCount() {
        return maximumRowCount;
    }

    /**
     * TODO: Documentation
     *
     * @param newRenderer  the SListCellRenderer that displays the selected item.
     * @beaninfo
     *     expert: true
     *  description: The renderer that generates the item's code
     */
    public void setRenderer(SListCellRenderer newRenderer) {
        SListCellRenderer oldRenderer = renderer;
        renderer = newRenderer;
        firePropertyChange("renderer", oldRenderer, renderer);
    }

    /**
     * TODO: Documentation 
     * @return  the ListCellRenderer that displays the selected item.
     */
    public SListCellRenderer getRenderer() {
        return renderer;
    }

    /** 
     * Sets the selected item in the SComboBox.
     *
     * @param object  the list object to select
     * @beaninfo
     *    preferred:   true
     *    description: Sets the selected item in the SComboBox.
     */
    public void setSelectedItem(Object object) {
        dataModel.setSelectedItem(object);
        fireActionEvent();
    }

    /**
     * Returns the currently selected item.
     *
     * @return  the currently selected list object from the data model
     */
    public Object getSelectedItem() {
        return dataModel.getSelectedItem();
    }

    /**
     * Selects the item at index <code>index</code>.
     *
     * @param index the item to be selected
     *
     * @beaninfo
     *   preferred: true
     *  description: The item at index is selected.
     */
    public void setSelectedIndex(int index) {
        int size = dataModel.getSize();

        if (index == -1 )
            setSelectedItem( null );
        else if (index < -1 || index >= size)
            throw new IllegalArgumentException("setSelectedIndex: " + index + " out of bounds");
        else
            setSelectedItem(dataModel.getElementAt(index));
    }

    /**
     * Returns the index of the currently selected item in the list.
     *
     * @return the selected item in the list or -1 if no item is selected or if
     *         the currently selected item (text field) is not in the list
     */
    public int getSelectedIndex() {
        Object selected = dataModel.getSelectedItem();
        int i,c;
        Object obj;

        for (i=0, c=dataModel.getSize(); i<c; i++) {
            obj = dataModel.getElementAt(i);
            if ((obj == null && selected == null) || (obj != null &&obj.equals(selected)))
                return i;
        }
        return -1;
    }

    /** 
     * Adds an item to the item list.
     *
     * @param object the Object to add to the list
     */
    public void addItem(Object object) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel)dataModel).addElement(object);
    }

    /** 
     * Inserts an item into the item list at a given index. 
     *
     * @param object the Object to add to the list
     * @param index    an int specifying the position at which to add the item
     */
    public void insertItemAt(Object object, int index) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel)dataModel).insertElementAt(object,index);
    }

    /** 
     * Removes an item from the item list.
     * This method works only if the SComboBox uses the default data model.
     * SComboBox uses the default data model when created with the empty constructor
     * and no other model has been set.
     *
     * @param object  the object to remove from the item list
     */
    public void removeItem(Object object) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel)dataModel).removeElement(object);
    }

    /**  
     * Removes the item at <code>index</code>
     *
     * @param index  an int specifying the idex of the item to remove, where 0
     *                 indicates the first item in the list
     */
    public void removeItemAt(int index) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel)dataModel).removeElementAt( index );
    }

    /** 
     * Removes all items from the item list.
     */
    public void removeAllItems() {
        checkMutableComboBoxModel();
        MutableComboBoxModel model = (MutableComboBoxModel)dataModel;
        int size = model.getSize();

        if ( model instanceof DefaultComboBoxModel ) {
            ((DefaultComboBoxModel)model).removeAllElements();
        }
        else {
            for ( int i = 0; i < size; ++i ) {
                Object element = model.getElementAt( 0 );
                model.removeElement( element );
            }
        }
    }

    void checkMutableComboBoxModel() {
        if (!(dataModel instanceof MutableComboBoxModel))
            throw new RuntimeException("Cannot use this method with a non-Mutable data model.");
    }


    /** Selection **/

    /** 
     * Adds an ItemListener. <code>listener</code> will receive an event when
     * the selected item changes.
     *
     * @param listener  the ItemListener that is to be notified
     */
    public void addItemListener(ItemListener listener) {
        listenerList.add(ItemListener.class, listener);
    }

    /** Removes an ItemListener
     *
     * @param listener  the ItemListener to remove
     */
    public void removeItemListener(ItemListener listener) {
        listenerList.remove(ItemListener.class, listener);
    }

    /** 
     * Adds an ActionListener. The listener will receive an action event
     * when the user changed the selection.
     *
     * @param listener  the ActionListener that is to be notified
     */
    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    /** Removes an ActionListener 
     *
     * @param listener  the ActionListener to remove
     */
    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    /** 
     * Sets the action commnand that should be included in the event
     * sent to action listeners.
     *
     * @param command  a string containing the "command" that is sent
     *                  to action listeners. The same listener can then
     *                  do different things depending on the command it
     *                  receives.
     */
    public void setActionCommand(String command) {
        actionCommand = command;
    }

    /** 
     * Returns the action commnand that is included in the event sent to
     * action listeners.
     *
     * @return  the string containing the "command" that is sent
     *          to action listeners.
     */
    public String getActionCommand() {
        return actionCommand;
    }

    /**
     * Notify all listeners that have registered as ItemListeners.
     *  
     * @see EventListenerList
     */
    protected void fireItemStateChanged(ItemEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for ( int i = listeners.length-2; i>=0; i-=2 ) {
            if ( listeners[i]==ItemListener.class ) {
                // Lazily create the event:
                // if (changeEvent == null)
                // changeEvent = new ChangeEvent(this);
                ((ItemListener)listeners[i+1]).itemStateChanged(e);
            }
        }
    }   

    /**
     * Notify all listeners that have registered as ActionListeners.
     *  
     * @see EventListenerList
     */
    protected void fireActionEvent() {
        if ( !firingActionEvent ) {
            firingActionEvent = true;
            ActionEvent e = null;
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for ( int i = listeners.length-2; i>=0; i-=2 ) {
                if ( listeners[i]==ActionListener.class ) {
                    if ( e == null )
                        e = new ActionEvent(this,ActionEvent.ACTION_PERFORMED,getActionCommand());
                    ((ActionListener)listeners[i+1]).actionPerformed(e);
                }
            }
            firingActionEvent = false;
        }
    }

    /**
     * This method is called when the selected item changes.
     */
    protected void selectedItemChanged() {
        if (lastSelectedItem != null) {
            fireItemStateChanged(new ItemEvent(this,ItemEvent.ITEM_STATE_CHANGED,
                                               lastSelectedItem,
                                               ItemEvent.DESELECTED));
        }

        lastSelectedItem = getModel().getSelectedItem();

        if (lastSelectedItem != null)
            fireItemStateChanged(new ItemEvent(this,ItemEvent.ITEM_STATE_CHANGED,
                                               lastSelectedItem,
                                               ItemEvent.SELECTED));
        fireActionEvent();
    }

    /** 
     * Returns an array containing the selected item.
     *
     * @returns an array of Objects containing the selected item
     */
    public Object[] getSelectedObjects() {
        Object selectedObject = getSelectedItem();
        if (selectedObject == null)
            return new Object[0];
        else
            return new Object[] { selectedObject };
    }

    /**
     * This method is public as an implementation side effect. 
     * do not call or override.
     *
     * @see javax.swing.event.ListDataListener
     */
    public void contentsChanged(ListDataEvent e) {
        Object newSelectedItem = dataModel.getSelectedItem();

        if ((lastSelectedItem == null && newSelectedItem != null)
            || (lastSelectedItem != null && !lastSelectedItem.equals(newSelectedItem)))
            selectedItemChanged();
    }

    /**
     * Invoked when items have been added to the internal data model.
     * The "interval" includes the first and last values added. 
     *
     * @see javax.swing.event.ListDataListener
     */
    public void intervalAdded(ListDataEvent e) {
        contentsChanged(e);
    }

    /**
     * Invoked when values have been removed from the data model. 
     * The"interval" includes the first and last values removed. 
     *
     * @see javax.swing.event.ListDataListener
     */
    public void intervalRemoved(ListDataEvent e) {
        contentsChanged(e);
    }


    /* Accessing the model */
    /**
     * Returns the number of items in the list.
     *
     * @return an int equal to the number of items in the list
     */
    public int getItemCount() {
        return dataModel.getSize();
    }

    /**
     * Returns the list item at the specified index.
     *
     * @param index  an int indicating the list position
     *
     * @return the Object at that list position
     */
    public Object getItemAt(int index) {
        return dataModel.getElementAt(index);
    }

    public void setShowAsFormComponent(boolean showAsFormComponent) {}
    public boolean getShowAsFormComponent() { return true; }

    public void getPerformed(String action, String value) {
        try {
            int sel = Integer.parseInt(value);
            setSelectedIndex(sel);
        }
        catch (Exception e) {
            System.err.println("Cannot parse expected integer");
            e.printStackTrace();
        }
    }


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
     * @return "ComboBoxCG"
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
