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

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.event.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableModel;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListSelectionModel;

import org.wings.externalizer.ExternalizeManager;
import org.wings.io.Device;
import org.wings.plaf.*;
import org.wings.style.*;


/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class STable
    extends SBaseTable
    implements CellEditorListener, RequestListener
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "TableCG";

    /**
     * TODO: documentation
     */
    protected final EventListenerList listenerList = new EventListenerList();

    /** If editing, Component that is handling the editing. */
    transient protected SComponent editorComp;

    /**
     * The object that overwrites the screen real estate occupied by the
     * current cell and allows the user to change those contents.
     */
    transient protected STableCellEditor cellEditor;

    /** Identifies the column of the cell being edited. */
    transient protected int editingColumn;

    /** Identifies the row of the cell being edited. */
    transient protected int editingRow;

    /**
     * TODO: documentation
     */
    protected final HashMap editors = new HashMap();

    /** Icon used for buttons that start editing in a cell. */
    transient protected SIcon editIcon;


    /** The style of selected cells */
    protected Style selectionStyle;

    /** The dynamic attributes of selected cells */
    protected AttributeSet selectionAttributes = new SimpleAttributeSet();

    /**
     * 
     */
    protected ListSelectionModel selectionModel = new DefaultListSelectionModel();

    /**
     * 
     */
    protected STableCellRenderer rowSelectionRenderer = null;

    /**
     * the column where the row selection element should be rendered. If
     * negativ, no selection element is rendered.
     */
    protected int rowSelectionColumn = Integer.MAX_VALUE;


    /**
     * TODO: documentation
     *
     * @param tm
     */
    public STable(TableModel tm){
        super(tm);
        createDefaultEditors();
        createDefaultIcons();
    }

    /**
     * TODO: documentation
     *
     * @param tm
     */
    public void setModel(TableModel tm) {
        super.setModel(tm);
    }


    /**
     * Adds the row from <i>index0</i> to <i>index0</i> inclusive to the current selection.
     */
    public void addRowSelectionInterval(int index0, int index1)
    {
        selectionModel.addSelectionInterval(index0, index1);
    }

    public void setParent(SContainer p) {
        super.setParent(p);

        if (editorComp != null)
            editorComp.setParent(p);
    }

    public void processRequest(String action, String[] values) {
        if ( values.length>1 ) 
            getSelectionModel().setValueIsAdjusting(true);

        for ( int i=0; i<values.length; i++ ) {
            String value = values[i];
            
            if ( value.length()>1 ) {
                
                char modus = value.charAt(0);
                value = value.substring(1);
                
                int colonIndex = value.indexOf(':');
                if ( colonIndex<0 )
                    return;
                
                try {
                    int row = Integer.parseInt(value.substring(0, colonIndex));
                    int col = Integer.parseInt(value.substring(colonIndex + 1));
                    
                    // editor event
                    switch ( modus ) {
                    case 'e':
                        editCellAt(row, col, null);
                        break;
                    case 't':
                        if ( getSelectionModel().isSelectedIndex(row) ) 
                            getSelectionModel().removeSelectionInterval(row, row);
                        else
                            getSelectionModel().addSelectionInterval(row, row);
                        break;
                    case 's':
                        getSelectionModel().addSelectionInterval(row, row);
                        break;
                    case 'd':
                        getSelectionModel().removeSelectionInterval(row, row);
                        break;
                    }
                } catch ( NumberFormatException ex ) {
                    // hier loggen...
                    ex.printStackTrace();
                }
            }
        }
 
       if ( values.length>1 ) 
            getSelectionModel().setValueIsAdjusting(false);
 
    }
        
    public void setRowSelectionRenderer(STableCellRenderer r) {
        rowSelectionRenderer = r;
    }

    public STableCellRenderer getRowSelectionRenderer() {
        return rowSelectionRenderer;
    }

    /**
     * Set a default editor to be used if no editor has been set in
     * a TableColumn. If no editing is required in a table, or a
     * particular column in a table, use the isCellEditable()
     * method in the TableModel interface to ensure that the
     * STable will not start an editor in these columns.
     * If editor is null, remove the default editor for this
     * column class.
     *
     * @see     TableModel#isCellEditable
     * @see     #getDefaultEditor
     * @see     #setDefaultRenderer
     */
    public void setDefaultEditor(Class columnClass, STableCellEditor r) {
        editors.remove(columnClass);
        if (editors != null)
            editors.put(columnClass, r);
    }

    /*
     * Returns the editor to be used when no editor has been set in
     * a TableColumn. During the editing of cells the editor is fetched from
     * a Map of entries according to the class of the cells in the column. If
     * there is no entry for this <I>columnClass</I> the method returns
     * the entry for the most specific superclass. The STable installs entries
     * for <I>Object</I>, <I>Number</I> and <I>Boolean</I> all which can be modified
     * or replaced.
     *
     * @param columnClass
     * @return
     * @see     #setDefaultEditor
     * @see     #getColumnClass
     */
    public STableCellEditor getDefaultEditor(Class columnClass) {
        if (columnClass == null) {
            return null;
        }
        else {
            Object r = editors.get(columnClass);
            if (r != null) {
                return (STableCellEditor)r;
            }
            else {
                return getDefaultEditor(columnClass.getSuperclass());
            }
        }
    }

    //
    // Editing Support
    //

    /**
     * Programmatically starts editing the cell at <I>row</I> and
     * <I>column</I>, if the cell is editable.
     *
     * @param   row                             the row to be edited
     * @param   column                          the column to be edited
     * @exception IllegalArgumentException      If <I>row</I> or <I>column</I>
     *                                          are not in the valid range
     * @return  false if for any reason the cell cannot be edited.
     */
    public boolean editCellAt(int row, int column) {
        return editCellAt(row, column, null);
    }

    /**
     * Programmatically starts editing the cell at <I>row</I> and
     * <I>column</I>, if the cell is editable.
     * To prevent the STable from editing a particular table, column or
     * cell value, return false from the isCellEditable() method in the
     * TableModel interface.
     *
     * @param   row                             the row to be edited
     * @param   column                          the column to be edited
     * @param   e                               event to pass into
     *                                          shouldSelectCell
     * @exception IllegalArgumentException      If <I>row</I> or <I>column</I>
     *                                          are not in the valid range
     * @return  false if for any reason the cell cannot be edited.
     */
    public boolean editCellAt(int row, int column, EventObject e){
        if (isEditing()) {
            // Try to stop the current editor
            if (cellEditor != null) {
                boolean stopped = cellEditor.stopCellEditing();
                if (!stopped)
                    return false;       // The current editor not resigning
            }
        }

        if (!isCellEditable(row, column))
            return false;

        STableCellEditor editor = getCellEditor(row, column);
        if (editor != null) {
            // prepare editor
            editorComp = prepareEditor(editor, row, column);

            if (editor.isCellEditable(e) && editor.shouldSelectCell(e)) {
                editorComp.setParent(getParent());
                //this.add(editorComp);
                setCellEditor(editor);
                setEditingRow(row);
                setEditingColumn(column);
                editor.addCellEditorListener(this);

                return true;
            }
            setValueAt(editor.getCellEditorValue(), row, column);
        }
        return false;
    }

    /**
     * Returns true if the cell at <I>row</I> and <I>column</I>
     * is editable.  Otherwise, setValueAt() on the cell will not change
     * the value of that cell.
     *
     * @param   row      the row whose value is to be looked up
     * @param   column   the column whose value is to be looked up
     * @return  true if the cell is editable.
     * @see #setValueAt
     */
    public boolean isCellEditable(int row, int col) {
        if (col >= super.getColumnCount() || row == -1)
            return false;
        else
            return getModel().isCellEditable(row, col);
    }

    /**
     * Returns  true is the table is editing a cell.
     *
     * @return  true is the table is editing a cell
     * @see     #editingColumn
     * @see     #editingRow
     */
    public boolean isEditing() {
        return (cellEditor == null) ? false : true;
    }

    /**
     * If the receiver is currently editing this will return the Component
     * that was returned from the CellEditor.
     *
     * @return  SComponent handling editing session
     */
    public SComponent getEditorComponent() {
        return editorComp;
    }

    /**
     * This returns the index of the editing column.
     *
     * @return  the index of the column being edited
     * @see #editingRow
     */
    public int getEditingColumn() {
        return editingColumn;
    }

    /**
     * Returns the index of the editing row.
     *
     * @return  the index of the row being edited
     * @see #editingColumn
     */
    public int getEditingRow() {
        return editingRow;
    }

    /**
     * Return the cellEditor.
     *
     * @return the STableCellEditor that does the editing
     * @see #cellEditor
     */
    public STableCellEditor getCellEditor() {
        return cellEditor;
    }

    /**
     * Set the cellEditor variable.
     *
     * @param anEditor  the STableCellEditor that does the editing
     * @see #cellEditor
     */
    protected void setCellEditor(STableCellEditor anEditor) {
        STableCellEditor oldEditor = cellEditor;
        cellEditor = anEditor;
    }

    /**
     * Set the editingColumn variable.
     *
     * @see #editingColumn
     */
    public void setEditingColumn(int aColumn) {
        int oldEditingColumn = editingColumn;
        editingColumn = aColumn;
        if (editingColumn != oldEditingColumn)
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * Set the editingRow variable.
     *
     * @see #editingRow
     */
    public void setEditingRow(int aRow) {
        int oldEditingRow = editingRow;
        editingRow = aRow;
        if (editingRow != oldEditingRow)
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * Return an appropriate editor for the cell specified by this row and
     * column. If the TableColumn for this column has a non-null editor, return that.
     * If not, find the class of the data in this column (using getColumnClass())
     * and return the default editor for this type of data.
     *
     * @param row       the row of the cell to edit, where 0 is the first
     * @param column    the column of the cell to edit, where 0 is the first
     */
    public STableCellEditor getCellEditor(int row, int column) {
        // TableColumn tableColumn = getColumnModel().getColumn(column);
        // STableCellEditor editor = tableColumn.getCellEditor();
        // if (editor == null) {
        STableCellEditor editor = getDefaultEditor(getColumnClass(column));
        // }
        return editor;
    }


    /**
     * Prepares the specified editor using the value at the specified cell.
     *
     * @param editor  the TableCellEditor to set up
     * @param row     the row of the cell to edit, where 0 is the first
     * @param column  the column of the cell to edit, where 0 is the first
     */
    protected SComponent prepareEditor(STableCellEditor r, int row, int col) {
        return r.getTableCellEditorComponent(this,
                                             model.getValueAt(row,col),
                                             isRowSelected(row), // true?
                                             row, col);
    }

    /**
     * Discard the editor object and return the real estate it used to
     * cell rendering.
     */
    public void removeEditor() {
        STableCellEditor editor = getCellEditor();
        if (editor != null) {
            editor.removeCellEditorListener(this);
            //remove(editorComp);
            setCellEditor(null);
            setEditingColumn(-1);
            setEditingRow(-1);
            editorComp = null;
        }
    }


    //
    // Implementing the CellEditorListener interface
    //

    /**
     * Invoked when editing is finished. The changes are saved and the
     * editor object is discarded.
     *
     * @see CellEditorListener
     */
    public void editingStopped(ChangeEvent e) {
        // Take in the new value
        STableCellEditor editor = getCellEditor();
        if (editor != null) {
            Object value = editor.getCellEditorValue();
            setValueAt(value, editingRow, editingColumn);
            removeEditor();
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * Invoked when editing is canceled. The editor object is discarded
     * and the cell is rendered once again.
     *
     * @see CellEditorListener
     */
    public void editingCanceled(ChangeEvent e) {
        removeEditor();
        reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * Creates default cell editors for Objects, numbers, and boolean values.
     */
    protected void createDefaultEditors() {
        editors.clear();

        // Objects
        STextField textField = new STextField();
        setDefaultEditor(Object.class, new SDefaultCellEditor(textField));
        setDefaultEditor(Number.class, new SDefaultCellEditor(textField));

        // Numbers
        //STextField rightAlignedTextField = new STextField();
        //rightAlignedTextField.setHorizontalAlignment(STextField.RIGHT);
        //setDefaultEditor(Number.class, new SDefaultCellEditor(rightAlignedTextField));

        // Booleans
        SCheckBox centeredCheckBox = new SCheckBox();
        //centeredCheckBox.setHorizontalAlignment(JCheckBox.CENTER);
        setDefaultEditor(Boolean.class, new SDefaultCellEditor(centeredCheckBox));
    }


    /**
     * TODO: documentation
     *
     * @return
     */
    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public void setSelectionModel(ListSelectionModel m) {
        selectionModel = m;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getSelectedRowCount() {
        int result = 0;
        for ( int i=getSelectionModel().getMinSelectionIndex(); 
              i<=getSelectionModel().getMaxSelectionIndex(); i++ ) {
            if ( getSelectionModel().isSelectedIndex(i) )
                result++;
        }

        return result;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getSelectedRow() {
        return getSelectionModel().getMinSelectionIndex();
    }

    public int[] getSelectedRows() {
        int[] result = new int[getSelectedRowCount()];

        int index = 0;
        for ( int i=getSelectionModel().getMinSelectionIndex(); 
              i<=getSelectionModel().getMaxSelectionIndex(); i++ ) {
            if ( getSelectionModel().isSelectedIndex(i) )
                result[index++] = i;
        }

        return result;
    }

    /**
     * Deselects all selected columns and rows.
     */
    public void clearSelection() {
        getSelectionModel().clearSelection();
    }


    /**
     * TODO: documentation
     *
     * @param row
     * @return
     */
    public boolean isRowSelected(int row) {
        return getSelectionModel().isSelectedIndex(row);
    }

    /**
     * Sets the selection mode. Use one of the following values:
     * <UL>
     * <LI> {@link ListSelectionModel#NO_SELECTION}
     * <LI> {@link ListSelectionModel#SINGLE_SELECTION}
     * <LI> {@link ListSelectionModel#MULTIPLE_SELECTION}
     * </UL>
     */
    public void setSelectionMode(int s) {
        getSelectionModel().setSelectionMode(s);
    }

    /**
     * TODO: documentation
     * @return
     * <UL>
     * <LI> {@link ListSelectionModel#NO_SELECTION}
     * <LI> {@link ListSelectionModel#SINGLE_SELECTION}
     * <LI> {@link ListSelectionModel#MULTIPLE_SELECTION}
     * </UL>
     */
    public int getSelectionMode() {
        return getSelectionModel().getSelectionMode();
    }

    /**
     * TODO: documentation
     *
     * @param listener
     */
    public void addSelectionListener(ListSelectionListener listener) {
        getSelectionModel().addListSelectionListener(listener);
    }

    /**
     * TODO: documentation
     *
     * @param listener
     */
    public void removeSelectionListener(ListSelectionListener listener) {
        getSelectionModel().removeListSelectionListener(listener);
    }

    public void fireIntermediateEvents() {
    }

    public void fireFinalEvents() {
    }

    /**
     * TODO: documentation
     *
     * @param e
     */
    public void tableChanged(TableModelEvent e) {
        // kill active editors
        editingCanceled(null);

        if (e == null || e.getFirstRow() == TableModelEvent.HEADER_ROW) {
            // The whole thing changed
            clearSelection();
        } else {
            switch ( e.getType() ) {
            case TableModelEvent.INSERT:
                if (e.getFirstRow() >= 0)
                    getSelectionModel().insertIndexInterval(e.getFirstRow(),
                                                            e.getLastRow(), true);
                break;
                
            case TableModelEvent.DELETE:
                if (e.getFirstRow() >= 0)
                    getSelectionModel().removeIndexInterval(e.getFirstRow(),
                                                            e.getLastRow());
                break;
            }
        }
        reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * Sets the icon used for the buttons that start editing in a cell.
     */
    public void setEditIcon(SIcon newIcon) {
        editIcon = newIcon;
    }

    /**
     * Returns the icon used for the buttons that start editing in a cell.
     */
    public SIcon getEditIcon() {
        return editIcon;
    }

    /**
     * @param style the style of selected cells
     */
    public void setSelectionStyle(Style selectionStyle) {
        this.selectionStyle = selectionStyle;
    }

    /**
     * @return the style of selected cells.
     */
    public Style getSelectionStyle() { return selectionStyle; }


    /**
     * Set the selectionAttributes.
     * @param selectionAttributes the selectionAttributes
     */
    public void setSelectionAttributes(AttributeSet selectionAttributes) {
        if (selectionAttributes == null)
            throw new IllegalArgumentException("null not allowed");

        if (!this.selectionAttributes.equals(selectionAttributes)) {
            this.selectionAttributes = selectionAttributes;
            reload(ReloadManager.RELOAD_STYLE);
        }
    }

    /**
     * @return the current selectionAttributes
     */
    public AttributeSet getSelectionAttributes() {
        return selectionAttributes;
    }

    /**
     * Set the background color.
     * @param c the new background color
     */
    public void setSelectionBackground(Color color) {
        boolean changed = selectionAttributes.putAttributes(CSSStyleSheet.getAttributes(color, "background-color"));
        if (changed)
            reload(ReloadManager.RELOAD_STYLE);
    }

    /**
     * Return the background color.
     * @return the background color
     */
    public Color getSelectionBackground() {
        return CSSStyleSheet.getBackground(selectionAttributes);
    }

    /**
     * Set the foreground color.
     * @param color the foreground color of selected cells
     */
    public void setSelectionForeground(Color color) {
        boolean changed = selectionAttributes.putAttributes(CSSStyleSheet.getAttributes(color, "color"));
        if (changed)
            reload(ReloadManager.RELOAD_STYLE);
    }

    /**
     * Return the foreground color.
     * @return the foreground color
     */
    public Color getSelectionForeground() {
        return CSSStyleSheet.getForeground(selectionAttributes);
    }


    /**
     * TODO: documentation
     *
     */
    protected void createDefaultIcons() {
        setEditIcon(new ResourceImageIcon(STable.class, 
                                          "/org/wings/icons/Pencil.gif"));
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(TableCG cg) {
        super.setCG(cg);
    }
    
    public String getEditParameter(int row, int col) {
        return "e" + row + ":" + col;
    }

    public String getSelectionToggleParameter(int row, int col) {
        return "t" + row + ":" + col;
    }

    public String getSelectParameter(int row, int col) {
        return "s" + row + ":" + col;
    }

    public String getDeselectParameter(int row, int col) {
        return "d" + row + ":" + col;
    }

    public void setRowSelectionColumn(int column) {
        rowSelectionColumn = column;
    }

    public int getRowSelectionColumn() {
        return rowSelectionColumn;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
