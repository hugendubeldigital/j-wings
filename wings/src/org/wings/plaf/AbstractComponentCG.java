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

/**
 * Partial implementation that is common to all ComponentCGs.
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public abstract class AbstractComponentCG implements ComponentCG
{
    /**
     * Install the appropriate CG for <code>component</code>.
     * @param component the component
     */
    public void installCG(SComponent component) {
	CGManager manager = component.getSession().getCGManager();
        String className = component.getClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);

        configure(component, className, manager);
    }

    /**
     * Recursively configure the component and dependant objects.
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

                value = manager.getObject(propertyName, propertyType);
                configurable = !propertyType.isPrimitive();

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

    /**
     * Uninstall the CG from <code>component</code>.
     * @param component the component
     */
    public void uninstallCG(SComponent c) {
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
