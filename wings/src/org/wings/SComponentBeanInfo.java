package org.wings;

import java.beans.*;
import org.wings.*;
 
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
