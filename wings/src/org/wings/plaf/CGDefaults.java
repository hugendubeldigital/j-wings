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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;
import java.util.*;
import javax.swing.Icon;

import org.wings.*;
import org.wings.style.*;

public class CGDefaults
    extends HashMap
{
    private PropertyChangeSupport changeSupport;
    private Map cache = new HashMap();
    private LookAndFeel lookAndFeel;

    /**
     * Create an empty defaults table.
     */
    public CGDefaults() {
        super();
    }

    public CGDefaults(Object[] keyValueList) {
        super(keyValueList.length / 2);
        for(int i = 0; i < keyValueList.length; i += 2) {
            super.put(keyValueList[i], keyValueList[i + 1]);
        }
    }

    public void setLookAndFeel(LookAndFeel lookAndFeel) {
        this.lookAndFeel = lookAndFeel;
    }

    /**
     * Set the value of <code>key</code> to <code>value</code>.
     * If <code>key</code> is a string and the new value isn't
     * equal to the old one, fire a PropertyChangeEvent.  If value
     * is null, the key is removed from the table.
     *
     * @param key    the unique Object who's value will be used to
     *               retreive the data value associated with it
     * @param value  the new Object to store as data under that key
     * @return the previous Object value, or null
     * @see #putDefaults
     * @see java.util.Hashtable#put
     */
    public Object put(Object key, Object value) {
        Object oldValue = (value == null) ? super.remove(key) : super.put(key, value);
        if (key instanceof String) {
            firePropertyChange((String)key, oldValue, value);
        }
        return oldValue;
    }


    /**
     * Put all of the key/value pairs in the database and
     * unconditionally generate one PropertyChangeEvent.
     * The events oldValue and newValue will be null and its
     * propertyName will be "UIDefaults".
     *
     * @see #put
     * @see java.util.Hashtable#put
     */
    public void putDefaults(Object[] keyValueList) {
        for(int i = 0; i < keyValueList.length; i += 2) {
            Object value = keyValueList[i + 1];
            if (value == null) {
                super.remove(keyValueList[i]);
            }
            else {
                super.put(keyValueList[i], value);
            }
        }
        firePropertyChange("UIDefaults", null, null);
    }

    /**
     * Returns the L&F instance that renders this component.
     *
     * @return The shared instance for code generation of classes with <code>uidClassID</code>.
     */
    private Object getCGInstance(String cgClassID) {
        try {
            String className = (String)get(cgClassID);
            Object instance = get(className);
            if (instance == null) {
                Class cgClass = Class.forName(className, true, lookAndFeel.getClassLoader());
                if (cgClass != null) {
                    instance = cgClass.newInstance();
                    // Save shared instance for future use
                    put(className, instance);
                }
            }
            return instance;
        }
        catch (ClassNotFoundException e) {
            getCGError("no ComponentCG class for: " + cgClassID);
            return null;
        }
        catch (ClassCastException e) {
            getCGError("ComponentCG class for: " + cgClassID + " does not implement the ComponentCG interface");
            return null;
        }
        catch (InstantiationException e) {
            getCGError("instantiation of shared instance failed for " + cgClassID + " " + e);
            return null;
        }
        catch (IllegalAccessException e) {
            getCGError("Constructor of ComponentCG class for: " + cgClassID + " is not accessible");
            return null;
        }
        catch (NullPointerException e) {
            getCGError("there's no value for " + cgClassID + " in the defaults table");
            return null;
        }
    }


    /**
     * If getCG() fails for any reason, it calls this method before
     * returning null.  Subclasses may choose to do more or
     * less here.
     *
     * @param msg Message string to print.
     * @see #getCG
     */
    protected void getCGError(String msg) {
        System.err.println("CGDefaults.getCG() failed: " + msg);
        try {
            throw new Error();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a ComponentCG implementation for the
     * specified component.  In other words create the look
     * and feel specific delegate object for <code>target</code>.
     * This is done in two steps:
     * <ul>
     * <li> Lookup the name of the ComponentCG implementation
     * class under the value returned by target.getCGClassID().
     * <li> Use the implementation classes static <code>createCG()</code>
     * method to construct a look and feel delegate.
     * </ul>
     */
    public ComponentCG getCG(SComponent target) {
        return (ComponentCG)getCGInstance(target.getCGClassID());
    }

    public LayoutCG getCG(SLayoutManager target) {
        return (LayoutCG)getCGInstance(target.getCGClassID());
    }

    public BorderCG getCG(SBorder target) {
        return (BorderCG)getCGInstance(target.getCGClassID());
    }

    /**
     * Return the Icon for the
     * specified key.
     */
    public Icon getIcon(Object key) {
        Object value = cache.get(key);
        if (value != null)
            return (Icon)value;

        value = get(key);
        if (value == null)
            return null;

        Icon icon = lookAndFeel.makeIcon((String)value);
        cache.put(key, icon);
        return icon;
    }

    /**
     * Return the Font for the
     * specified key.
     */
    public SFont getFont(Object key) {
        Object value = cache.get(key);
        if (value != null)
            return (SFont)value;

        value = get(key);
        if (value == null)
            return null;

        SFont font = lookAndFeel.makeFont((String)value);
        cache.put(key, font);
        return font;
    }

    /**
     * Return the Color for the
     * specified key.
     */
    public Color getColor(Object key) {
        Object value = cache.get(key);
        if (value != null)
            return (Color)value;

        value = get(key);
        if (value == null)
            return null;

        Color color = lookAndFeel.makeColor((String)value);
        cache.put(key, color);
        return color;
    }

    /**
     * Return the Style for the
     * specified key.
     */
    public Style getStyle(Object key) {
        Object value = cache.get(key);
        if (value != null)
            return (Style)value;

        value = get(key);
        if (value == null)
            return null;

        Style style = lookAndFeel.makeStyle((String)value);
        cache.put(key, style);
        return style;
    }

    /**
     * Return the StyleSheet for the
     * specified key.
     */
    public StyleSheet getStyleSheet(Object key) {
        Object value = cache.get(key);
        if (value != null)
            return (StyleSheet)value;

        value = get(key);
        if (value == null)
            return null;

        StyleSheet styleSheet = lookAndFeel.makeStyleSheet((String)value);
        cache.put(key, styleSheet);
        return styleSheet;
    }

    /**
     * Return the Object for the
     * specified key.
     */
    public Object getObject(Object key, Class clazz) {
        Object value = cache.get(key);
        if (value != null)
            return value;

        value = get(key);
        if (value == null)
            return null;

        Object object = lookAndFeel.makeObject((String)value, clazz);
        cache.put(key, object);
        return object;
    }


    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     * <p>
     * A PropertyChangeEvent will get fired whenever a default
     * is changed.
     *
     * @param listener  The PropertyChangeListener to be added
     * @see java.beans.PropertyChangeSupport
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);
    }


    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered
     * for all properties.
     *
     * @param listener  The PropertyChangeListener to be removed
     * @see java.beans.PropertyChangeSupport
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport != null) {
            changeSupport.removePropertyChangeListener(listener);
        }
    }


    /**
     * Support for reporting bound property changes.  If oldValue and
     * newValue are not equal and the PropertyChangeEvent listener list
     * isn't empty, then fire a PropertyChange event to each listener.
     *
     * @param propertyName  The programmatic name of the property that was changed.
     * @param oldValue  The old value of the property.
     * @param newValue  The new value of the property.
     * @see java.beans.PropertyChangeSupport
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
