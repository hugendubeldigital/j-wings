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
 * Partial CG implementation that is common to all ComponentCGs.
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public abstract class AbstractComponentCG implements ComponentCG, SConstants
{
    static Map cache = Collections.synchronizedMap(new HashMap());

    protected AbstractComponentCG() {
    }

    public void reload(SComponent comp, int aspect) {
        if ( comp==null )
            return;

        SFrame parent = comp.getParentFrame();

        if ( parent==null )
            return;

        ReloadManager reloadManager = comp.getSession().getReloadManager();

        switch ( aspect ) {
        case ReloadManager.RELOAD_CODE: 
            reloadManager.markDirty(parent.getDynamicResource(DynamicCodeResource.class));
            break;
        case ReloadManager.RELOAD_STYLE: 
            reloadManager.markDirty(parent.getDynamicResource(DynamicStyleSheetResource.class));
            break;
        case ReloadManager.RELOAD_SCRIPT: 
            // TODO
            //reloadManager.markDirty(parent.getDynamicResource(DynamicScriptResource.class));
            break;
        }
    }

    /**
     * Install the appropriate CG for <code>component</code>.
     * @param component the component
     */
    public void installCG(SComponent component) {
        System.err.println("install CG " + getClass().getName());
        long start = System.currentTimeMillis();
	CGManager manager = component.getSession().getCGManager();
        String className = component.getClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);

        configure(component, className, manager);
        System.err.println("install CG done in " +
                           (System.currentTimeMillis()-start) + " ms");
    }

    /**
     * Recursively configure the component and dependant objects.
     */
    protected void configure(Object object, String className, CGManager manager) {
        try {
            PropertyDescriptor[] descriptors = (PropertyDescriptor[])cache.get(object.getClass());
            if (descriptors == null) {
                BeanInfo info = Introspector.getBeanInfo(object.getClass());
                descriptors = info.getPropertyDescriptors();
                cache.put(object.getClass(), descriptors);
            }

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
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
