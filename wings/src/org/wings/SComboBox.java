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
 * @see javax.swing.ComboBoxModel
 * @see SListCellRenderer
 * @see SDefaultListCellRenderer
 *
 * @beaninfo
 *   attribute: isContainer false
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SComboBox
    extends SContainer
    implements Scrollable, SGetListener
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "ComboBoxCG";

    protected ComboBoxModel    dataModel;
    protected SListCellRenderer renderer;
    //protected ComboBoxEditor       editor;
    protected int maximumRowCount = 8;
    protected boolean isEditable  = false;
    protected Object lastSelectedItem = null;
    protected String actionCommand = "comboBoxChanged";
    
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
     * @param aModel the ComboBoxModel that provides the displayed list of items
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
     * Determines whether the SComboBox text field is editable.
     *
     * @param editable true indicates that the field is editable
     * 
     * @beaninfo
     *    preferred: true
     *  description: If true, the comboBox is editable
     */
    public void setEditable(boolean editable) {
        boolean changed = editable != isEditable;
        isEditable = editable;
        if (changed)
            firePropertyChange("editable", !editable, editable);
    }

    /**
     * Returns true if the SComboBox is editable.
     * 
     * @return true if the SComboBox is editable, else false
     */
    public boolean isEditable() {
        return isEditable;
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
    public void setRenderer(ListCellRenderer newRenderer) {
        ListCellRenderer oldRenderer = renderer;
        renderer = newRenderer;
        firePropertyChange("renderer", oldRenderer, renderer);
    }

    /**
     * TODO: Documentation 
     * @return  the ListCellRenderer that displays the selected item.
     */
    public ListCellRenderer getRenderer() {
        return renderer;
    }

    /**
     * Sets the editor used to paint and edit the selected item in the SComboBox
     * field. The editor is used only if the receiving SComboBox is editable. 
     * If not editable, the combo box uses the renderer to paint the selected item.
     *  
     * @param newEditor  the ComboBoxEditor that displays the selected item
     * @see #setRenderer
     * @beaninfo
     *    expert: true
     *  description: The editor that combo box uses to edit the current value
     */
    public void setEditor(SComboBoxEditor newEditor) {
        SComboBoxEditor oldEditor = editor;
        
        if ( editor != null )
            editor.removeActionListener(this);
        editor = newEditor;
        if ( editor != null ) {
            editor.addActionListener(this);
        }
        firePropertyChange("editor", oldEditor, editor);
    }

    /**
     * Returns the editor used to paint and edit the selected item in the SComboBox
     * field.
     *  
     * @return the ComboBoxEditor that displays the selected item
     */
    public ComboBoxEditor getEditor() {
        return editor;
    }

    /*
     * Selection
     */
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
     * @param index an int specifying the list item to select, where 0 specifies
     *                the first item in the list
     * @beaninfo
     *   preferred: true
     *  description: The item at index is selected.
     */
    public void setSelectedIndex(int index) {
        int size = dataModel.getSize();

        if (index == -1 )
            setSelectedItem( null );
        else if (index < -1 || index >= size) {
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
            if (obj.equals(selected))
                return i;
        }
        return -1;
    }

    /** 
     * Adds an item to the item list.
     * This method works only if the SComboBox uses the default data model.
     * SComboBox uses the default data model when created with the 
     * empty constructor and no other model has been set.
     *
     * @param object the Object to add to the list
     */
    public void addItem(Object object) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel)dataModel).addElement(object);
    }

    /** 
     * Inserts an item into the item list at a given index. 
     * This method works only if the SComboBox uses the default data model.
     * SComboBox uses the default data model when created with the 
     * empty constructor and no other model has been set.
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
     * This method works only if the SComboBox uses the default data model.
     * SComboBox uses the default data model when created with the 
     * empty constructor and no other model has been set.
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
     * This method works only if the SComboBox uses the default data model.
     * SComboBox uses the default data model when created with the empty constructor
     * and no other model has been set.
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
     * Notify all listeners that have registered interest for
     * notification on this event type.
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
     * Notify all listeners that have registered interest for
     * notification on this event type.
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
     * This method is called when the selected item changes. Its default implementation
     *  notifies the item listeners
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
     * Returns an array containing the selected item. This method is implemented for 
     * compatibility with ItemSelectable.
     *
     * @returns an array of Objects containing one element -- the selected item
     */
    public Object[] getSelectedObjects() {
        Object selectedObject = getSelectedItem();
        if (selectedObject == null)
            return new Object[0];
        else
            return new Object[] { selectedObject };
    }

    /** This method is public as an implementation side effect. 
     *  do not call or override. 
     */
    public void actionPerformed(ActionEvent e) {
        Object newItem = getEditor().getItem();
        getModel().setSelectedItem(newItem);
        fireActionEvent();
    }

    /** This method is public as an implementation side effect. 
     *  do not call or override. 
     *
     * @see javax.swing.event.ListDataListener
     */
    public void contentsChanged(ListDataEvent e) {
        Object newSelectedItem = dataModel.getSelectedItem();

        if ((lastSelectedItem == null && newSelectedItem != null)
            || (lastSelectedItem != null && !lastSelectedItem.equals(newSelectedItem)))
            selectedItemChanged();

        if (!isEditable() && newSelectedItem != null) {
            int i, c;
            boolean resetSelectedItem = true;
            Object o;
            Object selectedItem = dataModel.getSelectedItem();
            
            for (i=0,c=dataModel.getSize();i<c;i++) {
                o = dataModel.getElementAt(i);
                if (o.equals(selectedItem)) {
                    resetSelectedItem = false;
                    break;
                }
            }

            if (resetSelectedItem) {
                if (dataModel.getSize() > 0)
                    setSelectedIndex(0);
                else
                    setSelectedItem(null);
            }
        }
    }

    /**
     * Invoked items have been added to the internal data model.
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
     * @param index  an int indicating the list position, where the first
     *               item starts at zero
     * @return the Object at that list position
     */
    public Object getItemAt(int index) {
        return dataModel.getElementAt(index);
    }

    /**
     * Returns an instance of the default key-selection manager.
     *
     * @return the KeySelectionManager currently used by the list
     * @see #setKeySelectionManager
     */
    protected KeySelectionManager createDefaultKeySelectionManager() {
        return new DefaultKeySelectionManager();
    }


    /**
     * The interface that defines a KeySelectionManager. To qualify as
     * a KeySelectionManager, the class needs to implement the method
     * that identifies the list index given a character and the 
     * combo box data model.
     */
    public interface KeySelectionManager {
        /** Given <code>aKey</code> and the model, returns the row
         *  that should become selected. Return -1 if no match was
         *  found. 
         *
         * @param  aKey  a char value, usually indicating a keyboard key that
         *               was pressed
         * @param aModel a ComboBoxModel -- the component's data model, containing
         *               the list of selectable items 
         * @return an int equal to the selected row, where 0 is the
         *         first item and -1 is none. 
         */
        int selectionForKey(char aKey,ComboBoxModel aModel);
    }

    class DefaultKeySelectionManager implements KeySelectionManager, Serializable {
        public int selectionForKey(char aKey,ComboBoxModel aModel) {
            int i,c;
            int currentSelection = -1;
            Object selectedItem = aModel.getSelectedItem();
            String v;
            String pattern;

            if ( selectedItem != null ) {
                selectedItem = selectedItem.toString();

                for ( i=0,c=aModel.getSize();i<c;i++ ) {
                    if ( selectedItem.equals(aModel.getElementAt(i).toString()) ) {
                        currentSelection  =  i;
                        break;
                    }

                }
            }

            pattern = ("" + aKey).toLowerCase();
            aKey = pattern.charAt(0);

            for ( i = ++currentSelection, c = aModel.getSize() ; i < c ; i++ ) {
                v = aModel.getElementAt(i).toString().toLowerCase();
                if ( v.length() > 0 && v.charAt(0) == aKey )
                    return i;
            }

            for ( i = 0 ; i < currentSelection ; i ++ ) {
                v = aModel.getElementAt(i).toString().toLowerCase();
                if ( v.length() > 0 && v.charAt(0) == aKey )
                    return i;
            }
            return -1;
        }
    }


    /** 
     * See readObject() and writeObject() in JComponent for more 
     * information about serialization in Swing.
     */
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        if ((ui != null) && (getUIClassID().equals(uiClassID))) {
            ui.installUI(this);
        }
    }


    /**
     * Returns a string representation of this SComboBox. This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this SComboBox.
     */
    protected String paramString() {
        String lastSelectedItemString = (lastSelectedItem != null ?
                                             lastSelectedItem.toString() :
                                             "");
        String isEditableString = (isEditable ? "true" : "false");
        String lightWeightPopupEnabledString = (lightWeightPopupEnabled ?
                                                "true" : "false");

        return super.paramString() +
        ",isEditable=" + isEditableString +
        ",lightWeightPopupEnabled=" + lightWeightPopupEnabledString +
        ",maximumRowCount=" + maximumRowCount +
        ",lastSelectedItem=" + lastSelectedItemString;
    }

    private ComboBoxModel dataModel;
    private SListCellRenderer cellRenderer;

    /**
     * Need this for determination, which selections have changed.
     */
    protected boolean[] selection = null;

    /**
     * Need this for determination, which selections have changed.
     */
    protected boolean[] oldSelection = null;

    /**
     * TODO: documentation
     */
    protected boolean hidden = true;

    protected EventListenerList listenerList = new EventListenerList();
    private Rectangle viewport = null;

    /**
     * TODO: documentation
     */
    protected String type = SConstants.UNORDERED_LIST;

    /**
     * TODO: documentation
     */
    protected String orderType = null;

    /**
     * TODO: documentation
     */
    protected int start = 0;

    /**
     * Construct a SComboBox that displays the elements in the specified,
     * non-null model.  All SComboBox constructors delegate to this one.
     */
    public SComboBox(ComboBoxModel dataModel)
    {
        if (dataModel == null) {
            throw new IllegalArgumentException("dataModel must not be null");
        }

        this.dataModel = dataModel;
        selectionModel = createSelectionModel();
    }


    /**
     * Construct a SComboBox that displays the elements in the specified
     * array.  This constructor just delegates to the ComboBoxModel
     * constructor.
     */
    public SComboBox(final Object[] listData)
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
     * Construct a SComboBox that displays the elements in the specified
     * Vector.  This constructor just delegates to the ComboBoxModel
     * constructor.
     */
    public SComboBox(final Vector listData) {
        this ( new AbstractListModel() {
            public int getSize() {
                return listData.size();
            }
            public Object getElementAt(int i) {
                return listData.elementAt(i);
            } } );
    }


    /**
     * Constructs a SComboBox with an empty model.
     */
    public SComboBox() {
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
     * Sets the delegate that's used to paint each cell in the list.  If
     * prototypeCellValue was set then the fixedCellWidth and fixedCellHeight
     * properties are set as well.  Only one PropertyChangeEvent is generated
     * however - for the "cellRenderer" property.
     * <p>
     * The default value of this property is provided by the ComboBoxUI
     * delegate, i.e. by the look and feel implementation.
     * <p>
     * This is a JavaBeans bound property.
     *
     * @param cellRenderer the ListCellRenderer that paints list cells
     * @see #getCellRenderer
     * @beaninfo
     *       bound: true
     *   attribute: visualUpdate true
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
     *   attribute: visualUpdate true
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
     *   attribute: visualUpdate true
     * description: The background color of selected cells.
     */
    public void setSelectionBackground(Color selectionBackground) {
        Color oldValue = this.selectionBackground;
        this.selectionBackground = selectionBackground;
        //firePropertyChange("selectionBackground", oldValue, selectionBackground);
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
     * TODO: documentation
     *
     */
    protected void syncSelection() {
	if (dataModel == null)
            return;

        if (selection == null || dataModel.getSize() != selection.length)
            selection = new boolean[dataModel.getSize()];

        for (int i=0; i < selection.length; i++)
            selection[i] = false;
    }

    /**
     * TODO: documentation
     *
     */
    protected void fireEvents() {
        for (int i=0; i < selection.length; i++) {
            if (selection[i] != isSelectedIndex(i)) {
                if (selection[i])
                    addSelectionInterval(i, i);
                else
                    removeSelectionInterval(i, i);
            }
        }
    }

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
     * TODO: documentation
     *
     * @return
     */
    public Dimension getScrollableViewportSize() {
        return new Dimension(1, dataModel.getSize());
    }

    public void setViewportSize(Rectangle d) {
        viewport = d;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Rectangle getViewportSize() { return viewport; }

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
