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

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.*;
import java.io.*;
import javax.swing.*;

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.session.*;
import org.wings.style.*;

/**
 * Partial CG implementation that is common to all ComponentCGs.
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public abstract class AbstractComponentCG
    implements ComponentCG, SConstants, Serializable
{
    protected static Logger _wingsLogger = Logger.getLogger("org.wings.plaf");

    protected static final Map cache = new HashMap();

    protected AbstractComponentCG() {
        CGManager manager = SessionManager.getSession().getCGManager();
        String name = getClass().getName();
        name = name.substring(name.lastIndexOf(".") + 1);
        long start = System.currentTimeMillis();
        configure(this, name, manager);

        _wingsLogger.fine("configure CG done in " 
                    + (System.currentTimeMillis()-start) + " ms");
    }
    
    /**
     * Install the appropriate CG for <code>component</code>.
     * @param component the component
     */
    public void installCG(SComponent component) {
        long start = System.currentTimeMillis();
	CGManager manager = component.getSession().getCGManager();
        String className = component.getClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);

        configure(component, className, manager);
        _wingsLogger.fine("install CG done in " + (System.currentTimeMillis()-start) + " ms");
    }

    /**
     * Recursively configure the component and dependant objects.
     */
    protected void configure(Object object, String className, CGManager manager) {
        try {
            Class objectClass = object.getClass();

            long introspection_time = System.currentTimeMillis();
            Method[] setters = (Method[])cache.get(objectClass);
            if (setters == null) {
                synchronized (cache) {
                    setters = (Method[])cache.get(objectClass);
                    if (setters == null) {
                        setters = findRelevantSetters(objectClass);
                        cache.put(objectClass, setters);
                    }
                }
            }
            _wingsLogger.finer(objectClass.getName() + " introspection_time " +
                          (System.currentTimeMillis()-introspection_time) + " ms");

            long configuration_time = System.currentTimeMillis();
            for (int i=0; i < setters.length; i++) {
                Object value = null;

                String propertyName = Introspector.decapitalize(setters[i].getName().substring(3));
                String lookupName = className + "." + propertyName;
                Class propertyType = setters[i].getParameterTypes()[0];

                value = manager.getObject(lookupName, propertyType);
                boolean configurable = !propertyType.isPrimitive();

                _wingsLogger.finest(lookupName + "=" + value);

                if (value != null) {
                    setters[i].invoke(object, new Object[] { value });
                    if (configurable) {
                        configure(value, lookupName, manager);
                    }
                }
            }
            _wingsLogger.finer(objectClass.getName() + " configuration_time " +
                           (System.currentTimeMillis()-configuration_time) + " ms");
        }
        catch (Exception e) {
            _wingsLogger.log(Level.SEVERE, null, e);
        }
    }

    protected Method[] findRelevantSetters(Class clazz) {
        Method[] methods = clazz.getMethods();
        List setterList = new ArrayList(methods.length / 2);
        for (int m=0; m < methods.length; m++) {
            Method method = methods[m];
            if (method.getName().startsWith("set") &&
                method.getParameterTypes().length == 1 &&
                !method.getParameterTypes()[0].isArray() &&
                Arrays.binarySearch(toBeSkipped, method.getName()) < 0)
                setterList.add(method);
        }
        Iterator it = setterList.iterator();
        while (it.hasNext()) {
            Method setter = (Method)it.next();
            Class type = setter.getParameterTypes()[0];
            boolean present = false;
            if (boolean.class.equals(setter.getParameterTypes()[0])) {
                String getterName = "is" + setter.getName().substring(3);
                try {
                    Method getter = clazz.getMethod(getterName, EMPTY_CLASS_ARRAY);
                    if (type.equals(getter.getReturnType()))
                        present = true;
                }
                catch (Exception e) {}
            }
            if (!present) {
                String getterName = "g" + setter.getName().substring(1);
                try {
                    Method getter = clazz.getMethod(getterName, EMPTY_CLASS_ARRAY);
                    if (type.equals(getter.getReturnType()))
                        present = true;
                }
                catch (Exception e) {}
            }
            if (!present)
                it.remove();
        }
        return (Method[])setterList.toArray(new Method[setterList.size()]);
    }

    private static final String[] toBeSkipped = new String[] {
        "setAction",
        "setBaseTarget",
        "setEnabled",
        "setEscapeSpecialChars",
        "setLocale",
        "setModel",
        "setName",
        "setParent",
        "setShowAsFormComponent",
        "setTargetResource",
        "setText",
        "setToolTipText",
        "setVisible"
    };

    private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];

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
