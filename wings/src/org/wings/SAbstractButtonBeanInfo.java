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
public class SAbstractButtonBeanInfo extends SimpleBeanInfo
{
    private final static Class beanClass = SAbstractButton.class;

    public BeanInfo[] getAdditionalBeanInfo() {
	return new BeanInfo[] { new SComponentBeanInfo() };
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
	try {
	    PropertyDescriptor noBreak       = new PropertyDescriptor("noBreak", beanClass);

	    PropertyDescriptor rv[] = {
		noBreak
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
