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

package org.wings.plaf;

import java.awt.*;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.IOException;
import javax.swing.*;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.style.*;

public abstract class AbstractCG
    implements ComponentCG
{
    public void installCG(SComponent component) {
	CGManager manager = component.getSession().getCGManager();
        String className = component.getClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);

        configure(component, className, manager);
    }

    /**
     * recursively configure the component and dependant objects
     */
    protected void configure(Object object, String className, CGManager manager) {
        try {
            BeanInfo info = Introspector.getBeanInfo(object.getClass());

            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (int i=0; i < descriptors.length; i++) {
                Object value = null;

                if (descriptors[i] instanceof IndexedPropertyDescriptor ||
                    descriptors[i].getPropertyType() == null)
                    continue;

                Method setter = descriptors[i].getWriteMethod();
                if (setter == null || descriptors[i].getReadMethod() == null)
                    continue;

                String propertyName = className + "." + descriptors[i].getName();
                Class propertyType = descriptors[i].getPropertyType();
                boolean configurable = false;

                if (Icon.class.isAssignableFrom(propertyType))
                    value = manager.getIcon(propertyName);
                else if (SFont.class.isAssignableFrom(propertyType))
                    value = manager.getFont(propertyName);
                else if (Color.class.isAssignableFrom(propertyType))
                    value = manager.getColor(propertyName);
                else if (Style.class.isAssignableFrom(propertyType))
                    value = manager.getStyle(propertyName);
                else if (StyleSheet.class.isAssignableFrom(propertyType))
                    value = manager.getStyleSheet(propertyName);
                else {
                    value = manager.getObject(propertyName, propertyType);
                    configurable = !propertyType.isPrimitive();
                }

                if (value != null) {
                    setter.invoke(object, new Object[] { value });
                    if (configurable)
                        configure(value, propertyName, manager);
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void uninstallCG(SComponent c) {
    }
    
    
	
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
