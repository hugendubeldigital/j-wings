/*
 * $Id$
 * (c) 2000 Thinking Objects Software GmbH
 */

package org.wingx.beans.beaneditor;

import java.beans.*;
import javax.swing.*;

import org.wings.*;

/**
 * Base List Editor Adapter
 *
 * @author Holger Engels
 * @version $Revision$
 */

public class BooleanEditorAdapter
    extends SComboBoxEditorAdapter
{
    protected ComboBoxModel model;

    public BooleanEditorAdapter() {
	model = new DefaultComboBoxModel() {
		public int getSize() {
		    return 2;
		}
		public Object getElementAt(int i) {
		    return (i == 0) ? Boolean.TRUE : Boolean.FALSE;
		}
	    };
	component = new SComboBox();
	component.setModel(model);
    }

    public void init(PropertyDescriptor descriptor) {}

    public void setValue( Object obj ) {
        component.setSelectedItem( obj );
    }

    public Object getValue() {
        return component.getSelectedItem();
    }

    public SComponent getComponent() {
        return component;
    }
}
