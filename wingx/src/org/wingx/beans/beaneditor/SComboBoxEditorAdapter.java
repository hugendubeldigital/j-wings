/*
 * $Id$
 * (c) 2000 Thinking Objects Software GmbH
 */

package org.wingx.beans.beaneditor;

import java.awt.event.*;
import javax.swing.*;

import org.wings.*;
import org.wingx.beans.*;

/**
 * Base List Editor Adapter
 *
 * @author Holger Engels
 * @version $Revision$
 */

public class SComboBoxEditorAdapter
    extends AbstractEditorAdapter
    implements ActionListener
{
    protected SComboBox component;

    public SComboBoxEditorAdapter() {
        component = new SComboBox();
	component.addActionListener(this);
    }

    public void setEditor(SPropertyEditor editor) {
	super.setEditor(editor);
	component.setModel(new DefaultComboBoxModel(editor.getTags()));
    }    

    public void reset() {
        component.setSelectedItem(null);
    }

    public void setValue(Object obj) {
	editor.setValue(obj);
	component.setSelectedItem(editor.getAsText());
    }

    public Object getValue() {
        editor.setAsText((String)component.getSelectedItem());
	return editor.getValue();
    }

    public SComponent getComponent() {
        return component;
    }

    public void actionPerformed(ActionEvent e) {
	if (component != e.getSource())
	    return;
	editor.setAsText((String)component.getSelectedItem());
    }
}
