package org.wings.plaf;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;
import java.util.Hashtable;
import javax.swing.Icon;

import org.wings.*;
import org.wings.style.*;

public class CGDefaults extends Hashtable
{
    private PropertyChangeSupport changeSupport;
    
    /**
     * Create an empty defaults table.
     */
    public CGDefaults() {
        super();
    }
    
    /**
     * Create a defaults table initialized with the specified
     * key/value pairs.  For example:
     * <pre>
        Object[] uiDefaults = {
             "Font", new Font("Dialog", Font.BOLD, 12),
            "Color", Color.red,
             "five", new Integer(5)
        }
        UIDefaults myDefaults = new UIDefaults(uiDefaults);
     * </pre>
     */
    public CGDefaults(Object[] keyValueList) {
        super(keyValueList.length / 2);
        for(int i = 0; i < keyValueList.length; i += 2) {
            super.put(keyValueList[i], keyValueList[i + 1]);
        }
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
		Class cgClass = Class.forName(className);
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
     * Return the Style for the
     * specified key.
     */
    public Style getStyle(Object key) {
        Object value = get(key);
        return (value instanceof Style) ? (Style)value : null;
    }
    
    /**
     * Return the Icon for the
     * specified key.
     */
    public Icon getIcon(Object key) {
        Object value = get(key);
        return (value instanceof Icon) ? (Icon)value : null;
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
