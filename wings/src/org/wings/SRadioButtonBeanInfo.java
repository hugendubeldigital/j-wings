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

import java.beans.*;
import org.wings.*;

/**
 * TODO: docs (holger)
 *
 * @author
 * @version $Revision$
 */
public class SRadioButtonBeanInfo extends SimpleBeanInfo
{
    private final static Class beanClass = SRadioButton.class;

    public BeanInfo[] getAdditionalBeanInfo() {
	return new BeanInfo[] { new SAbstractButtonBeanInfo() };
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
	try {
	    PropertyDescriptor iconTextGap = new PropertyDescriptor("iconTextGap", beanClass);
	    PropertyDescriptor disabledIcon = new PropertyDescriptor("disabledIcon", beanClass);
	    PropertyDescriptor disabledSelectedIcon = new PropertyDescriptor("disabledSelectedIcon", beanClass);
	    PropertyDescriptor icon = new PropertyDescriptor("icon", beanClass);

	    PropertyDescriptor rv[] = {
		iconTextGap, icon,
                disabledIcon, disabledSelectedIcon
	    };
	    return rv;
	}
	catch (IntrospectionException e) {
	    e.printStackTrace(System.err);
	    throw new Error(e.toString());
	}
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
