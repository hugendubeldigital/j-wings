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

import java.awt.AWTEvent;
import java.awt.event.*;
import java.lang.Boolean;
import java.util.EventObject;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SDefaultCellEditor
    implements STableCellEditor
{
    /** Event listeners */
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * TODO: documentation
     */
    transient protected ChangeEvent changeEvent = null;

    protected SForm editorForm;
    protected SPanel editorPanel;
    protected SComponent editorComponent;
    protected EditorDelegate delegate;

    /**
     * Constructs a DefaultCellEditor that uses a text field.
     *
     * @param x  a STextField object ...
     */
    public SDefaultCellEditor(STextField x) {
        createDefaultIcons();

        this.editorComponent = x;
        this.editorForm = new SForm();
        this.editorPanel = new SPanel();
        editorForm.add(editorPanel);
        editorPanel.add(editorComponent);
        this.delegate = new EditorDelegate() {
                public void setValue(Object v) {
                    super.setValue(v);

                    if (v != null)
                        ((STextField)editorComponent).setText(v.toString());
                    else
                        ((STextField)editorComponent).setText("");
                }

                public Object getCellEditorValue() {
                    return ((STextField)editorComponent).getText();
                }

                public boolean stopCellEditing() {
                    return true;
                }

                public boolean shouldSelectCell(EventObject anEvent) {
                    return true;
                }
        };

        SButton button = new SButton("ok");
        //button.setIcon(commitIcon);
        button.addActionListener(delegate); // react on button press..
        editorForm.addActionListener(delegate); // .. and if form submitted
        editorPanel.add(button);
    }

    /**
     * Constructs a DefaultCellEditor object that uses a check box.
     *
     * @param x  a SCheckBox object ...
     */
    public SDefaultCellEditor(SCheckBox x) {
        createDefaultIcons();

        this.editorComponent = x;
        this.editorForm = null;
        this.editorPanel = null;
        this.delegate = new EditorDelegate() {
                public void setValue(Object v) {
                    super.setValue(v);

                    // Try my best to do the right thing with v
                    boolean bool;
                    if (v instanceof Boolean) {
                        bool = ((Boolean)v).booleanValue();
                    }
                    else if (v instanceof String) {
                        Boolean b = new Boolean((String)v);
                        bool = b.booleanValue();
                    }
                    else {
                        bool = false;
                    }
                    System.err.println("setValue(" + bool + ")");
                    ((SCheckBox)editorComponent).setSelected(bool);
                    ((SCheckBox)editorComponent).setSelected(!bool);
                }

                public Object getCellEditorValue() {
                    return new Boolean(((SCheckBox)editorComponent).isSelected());
                }

                public boolean stopCellEditing() {
                    return true;
                }

                public boolean shouldSelectCell(EventObject anEvent) {
                    return false;
                }
        };

        ((SCheckBox)editorComponent).addActionListener(delegate);
    }

    /**
     * Returns a reference to the editor component.
     *
     * @return the editor Component
     */
    public SComponent getComponent() {
        return editorComponent;
    }

    /** Icon used for the commit button.*/
    transient protected SIcon commitIcon = null;

    /**
     * Sets the icon used for the commit button.
     */
    public void setCommitIcon(SIcon newIcon) {
        commitIcon = newIcon;
    }

    /**
     * Returns the icon used for the commit button.
     */
    public SIcon getCommitIcon() {
        return commitIcon;
    }

    /**
     * TODO: documentation
     *
     */
    protected void createDefaultIcons() {
        setCommitIcon(new ResourceImageIcon(getClass().getClassLoader(),
                                            "org/wings/icons/TreeLeaf.gif"));
    }


    //
    //  Implementing the CellEditor Interface
    //

    // implements javax.swing.CellEditor
    /**
     * TODO: documentation
     *
     * @return
     */
    public Object getCellEditorValue() {
        return delegate.getCellEditorValue();
    }

    // implements javax.swing.CellEditor
    /**
     * TODO: documentation
     *
     * @param anEvent
     * @return
     */
    public boolean isCellEditable(EventObject anEvent) {
        return delegate.isCellEditable(anEvent);
    }

    /**
     * TODO: documentation
     *
     * @param anEvent
     * @return
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        // By default we want the cell to be selected so
        // we return true
        return delegate.shouldSelectCell(anEvent);
    }

    // implements javax.swing.CellEditor
    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean stopCellEditing() {
        boolean stopped = delegate.stopCellEditing();
        if (stopped) {
            fireEditingStopped();
        }

        return stopped;
    }

    // implements javax.swing.CellEditor
    /**
     * TODO: documentation
     *
     */
    public void cancelCellEditing() {
        delegate.cancelCellEditing();
        fireEditingCanceled();
    }

    //
    //  Handle the event listener bookkeeping
    //
    // implements javax.swing.CellEditor
    /**
     * TODO: documentation
     *
     * @param l
     */
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    // implements javax.swing.CellEditor
    /**
     * TODO: documentation
     *
     * @param l
     */
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireEditingStopped() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new javax.swing.event.ChangeEvent(this);
                ((CellEditorListener)listeners[i+1]).editingStopped(changeEvent);
            }
        }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireEditingCanceled() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new javax.swing.event.ChangeEvent(this);
                ((CellEditorListener)listeners[i+1]).editingCanceled(changeEvent);
            }
        }
    }

    //
    //  Implementing the TreeCellEditor Interface
    //

    // implements javax.swing.tree.TreeCellEditor
    public SComponent getTreeCellEditorComponent(STree tree, Object value,
                                                 boolean isSelected,
                                                 boolean expanded,
                                                 boolean leaf, int row) {

        String stringValue = (value != null)?value.toString():"";

        delegate.setValue(stringValue);
        if (editorForm != null && editorPanel != null) {
            if (containedInForm(tree.getParent()))
                return editorPanel;
            else
                return editorForm;
        }
        else
            return editorComponent;
    }

    /**
     * TODO: documentation
     *
     * @param c
     * @return
     */
    public boolean containedInForm(SComponent c) {
        while (c != null) {
            if (c instanceof SForm)
                return true;
            c = c.getParent();
        }
        return false;
    }

    //
    //  Implementing the CellEditor Interface
    //

    // implements javax.swing.table.TableCellEditor
    public SComponent getTableCellEditorComponent(SBaseTable table, Object value,
                                                  boolean isSelected,
                                                  int row, int column) {

        delegate.setValue(value);
        if (editorForm != null && editorPanel != null) {
            if (containedInForm(table.getParent()))
                return editorPanel;
            else
                return editorForm;
        }
        else
            return editorComponent;
    }


    //
    //  Protected EditorDelegate class
    //

    /**
     * TODO: documentation
     */
    protected class EditorDelegate
        implements ActionListener, ItemListener
    {
        protected Object value;

        /**
         * TODO: documentation
         *
         * @return
         */
        public Object getCellEditorValue() {
            return value;
        }

        /**
         * TODO: documentation
         *
         * @param x
         */
        public void setValue(Object x) {
            this.value = x;
        }

        /**
         * TODO: documentation
         *
         * @param anEvent
         * @return
         */
        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        /**
         * TODO: documentation
         *
         * @return
         */
        public boolean stopCellEditing() {
            return true;
        }

        /**
         * TODO: documentation
         *
         */
        public void cancelCellEditing() {
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        // Implementing ActionListener interface
        /**
         * TODO: documentation
         *
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
        }

        // Implementing ItemListener interface
        /**
         * TODO: documentation
         *
         * @param e
         */
        public void itemStateChanged(ItemEvent e) {
            fireEditingStopped();
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
