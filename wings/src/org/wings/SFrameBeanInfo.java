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
	    PropertyDescriptor textColor  = new PropertyDescriptor("textColor", beanClass);
	    PropertyDescriptor linkColor        = new PropertyDescriptor("linkColor", beanClass);
	    PropertyDescriptor vLinkColor        = new PropertyDescriptor("vLinkColor", beanClass);
	    PropertyDescriptor aLinkColor        = new PropertyDescriptor("aLinkColor", beanClass);
	    PropertyDescriptor styleSheet     = new PropertyDescriptor("styleSheet", beanClass);
	    PropertyDescriptor statusLine     = new PropertyDescriptor("statusLine", beanClass);

	    PropertyDescriptor rv[] = {
		resizable, backgroundURL, backgroundImage,
		textColor, linkColor, vLinkColor,
		aLinkColor, styleSheet, statusLine
	    };
	    return rv;
	}
	catch (IntrospectionException e) {
	    e.printStackTrace(System.err);
	    throw new Error(e.toString());
	}
    }
}
