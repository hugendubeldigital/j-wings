/*
 * $Id$
 * (c) 2000 Thinking Objects Software GmbH
 */

package org.wingx.beans.beaneditor;

import java.beans.*;

import org.wings.*;

/**
 * Text Field Editor Adapter
 *
 * @author Michael Reinsch, mreinsch@to.com
 * @version $Revision$
 */

public class StringEditorAdapter
    extends STextComponentEditorAdapter
{

  /** Integer, max length */
  public static final String ATTR_NAME_LENGTH  = "stextfield_length";
  
  /** Integer, visible length */
  public static final String ATTR_NAME_COLUMNS = "stextfield_columns";


    public StringEditorAdapter() {
        component = new STextField();
    }

    public void init( PropertyDescriptor descriptor ) {
        STextField f = (STextField) component;
        Integer val;
        if ( (val = (Integer)descriptor.getValue( ATTR_NAME_COLUMNS )) != null )
            f.setColumns( val.intValue() );
        if ( (val = (Integer)descriptor.getValue( ATTR_NAME_LENGTH )) != null )
            f.setMaxColumns( val.intValue() );
    }
}
