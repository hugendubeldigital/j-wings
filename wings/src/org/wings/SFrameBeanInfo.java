package org.wings;
 
import java.beans.*;
import org.wings.*;
 
public class SFrameBeanInfo extends SimpleBeanInfo
{
    private final static Class beanClass = SFrame.class;

    public BeanInfo[] getAdditionalBeanInfo() {
	return new BeanInfo[] { new SComponentBeanInfo() };
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
	try {
	    PropertyDescriptor resizable       = new PropertyDescriptor("resizable", beanClass);
	    PropertyDescriptor backgroundURL  = new PropertyDescriptor("backgroundURL", beanClass);
	    PropertyDescriptor backgroundImage  = new PropertyDescriptor("backgroundImage", beanClass);
	    PropertyDescriptor styleSheet     = new PropertyDescriptor("styleSheet", beanClass);
	    PropertyDescriptor statusLine     = new PropertyDescriptor("statusLine", beanClass);

	    PropertyDescriptor rv[] = {
		resizable, backgroundURL, backgroundImage,
		styleSheet, statusLine
	    };
	    return rv;
	}
	catch (IntrospectionException e) {
	    e.printStackTrace(System.err);
	    throw new Error(e.toString());
	}
    }
}
