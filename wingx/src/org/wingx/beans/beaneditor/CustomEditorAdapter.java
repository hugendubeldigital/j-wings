/*
 * $Id$
 * (c) 2000 Thinking Objects Software GmbH
 */

package org.wingx.beans.beaneditor;

import java.beans.*;
import org.wings.*;
import org.wingx.beans.*;

/**
 * Base List Editor Adapter
 *
 * @author Holger Engels
 * @version $Revision$
 */

public class CustomEditorAdapter
    extends AbstractEditorAdapter
{
    protected SPanel container;
    protected SComponent custom;

    public void setEditor(SPropertyEditor editor) {
	super.setEditor(editor);
	custom = editor.getCustomEditor();
	container.add(custom);
    }

    public SComponent getComponent() {
        return container;
    }

    public void setValue(Object obj) {
	editor.setValue(obj);
    }
    public Object getValue() {
	return editor.getValue();
    }
}
