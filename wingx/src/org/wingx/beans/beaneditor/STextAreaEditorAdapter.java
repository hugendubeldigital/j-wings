/*
 * $Id$
 * (c) 2000 Thinking Objects Software GmbH
 */

package org.wingx.beans.beaneditor;

import java.awt.event.*;
import java.beans.*;

import org.wings.*;
import org.wingx.beans.*;

/**
 * Text Area Editor Adapter
 *
 * @author Michael Reinsch, mreinsch@to.com
 * @version $Revision$
 */

public class STextAreaEditorAdapter
    extends STextComponentEditorAdapter
    implements BeanEditorConstants
{
    public STextAreaEditorAdapter() {
        component = new STextArea();
	component.addTextListener(this);
    }

    public void init(PropertyDescriptor descriptor) {
        STextArea field = (STextArea)component;
        Integer val;
        if ((val = (Integer)descriptor.getValue(COLUMNS)) != null)
            field.setColumns(val.intValue());
        if ((val = (Integer)descriptor.getValue(ROWS)) != null)
            field.setRows(val.intValue());
    }
}
