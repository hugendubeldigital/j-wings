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
 * TODO: doks (holger)
 *
 * @author
 * @version $Revision$
 */
public class SComponentBeanInfo extends SimpleBeanInfo
{
    private final static Class beanClass = SComponent.class;
    private final Object[] horizontalAlignments = new Object[] {
	"left", new Integer(SConstants.LEFT), "SConstants.LEFT",
	"right", new Integer(SConstants.RIGHT), "SConstants.RIGHT"
    };
    private final Object[] verticalAlignments = new Object[] {
	"top", new Integer(SConstants.TOP), "SConstants.TOP",
	"bottom", new Integer(SConstants.BOTTOM), "SConstants.BOTTOM"
    };


    public PropertyDescriptor[] getPropertyDescriptors() {
	try {
	    PropertyDescriptor style       = new PropertyDescriptor("style", beanClass);
	    PropertyDescriptor background  = new PropertyDescriptor("background", beanClass);
	    PropertyDescriptor foreground  = new PropertyDescriptor("foreground", beanClass);
	    PropertyDescriptor font        = new PropertyDescriptor("font", beanClass);
	    PropertyDescriptor visible     = new PropertyDescriptor("visible", beanClass);
	    PropertyDescriptor enabled     = new PropertyDescriptor("enabled", beanClass);
	    PropertyDescriptor name        = new PropertyDescriptor("name", beanClass);
	    PropertyDescriptor toolTipText = new PropertyDescriptor("toolTipText", beanClass);

	    PropertyDescriptor horizontalAlignment = new PropertyDescriptor("horizontalAlignment", beanClass);
	    horizontalAlignment.setValue("enumerationValues", horizontalAlignments);
	    PropertyDescriptor verticalAlignment = new PropertyDescriptor("verticalAlignment", beanClass);
	    verticalAlignment.setValue("enumerationValues", verticalAlignments);
 
	    PropertyDescriptor rv[] = {
		style, background, foreground,
		font, visible, enabled,
		name, toolTipText
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
