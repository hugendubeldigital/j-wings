package org.wingx.beans.beaneditor;

import java.beans.*;
import org.wings.*;
import org.wingx.beans.*;

/**
 * Abstract Editor Adapter
 *
 * @author Holger Engels
 * @version$
 */

public abstract class AbstractEditorAdapter
    implements EditorAdapter, PropertyChangeListener
{
    SPropertyEditor editor;

    public void setEditor(SPropertyEditor editor) {
	this.editor = editor;
	editor.addPropertyChangeListener(this);
    }    

    public void init(PropertyDescriptor descriptor) {}
    public void reset() {}

    public void propertyChange(PropertyChangeEvent evt) {
	editor.getAsText();
    }

    public void setValue(Object obj) {
	editor.setValue(obj);
    }
}
