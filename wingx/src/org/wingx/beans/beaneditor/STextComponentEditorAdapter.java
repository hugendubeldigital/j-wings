/*
 * $Id$
 * (c) 2000 Thinking Objects Software GmbH
 */

package org.wingx.beans.beaneditor;

import java.awt.event.*;

import org.wings.*;
import org.wingx.beans.*;

/**
 * Text Component Editor Adapter
 *
 * @author Michael Reinsch, mreinsch@to.com
 * @version $Revision$
 */

public abstract class STextComponentEditorAdapter
    extends AbstractEditorAdapter
    implements TextListener
{
    protected STextComponent component;

    public void reset() {
        component.setText(null);
    }

    public void setValue(Object obj) {
	editor.setValue(obj);
	component.setText(editor.getAsText());
    }

    public Object getValue() {
        editor.setAsText(component.getText());
	return editor.getValue();
    }

    public SComponent getComponent() {
        return component;
    }

    public void textValueChanged(TextEvent e) {
	if (component != e.getSource())
	    return;
	editor.setAsText(component.getText());
    }
}
