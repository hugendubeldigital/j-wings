/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.style;

import org.wings.Renderable;
import org.wings.io.Device;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * A straightforward implementation of CSSPropertySet using a hash map.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class CSSAttributeSet
        implements Renderable, Serializable, Cloneable {
    public static final CSSAttributeSet EMPTY_ATTRIBUTESET =
            new CSSAttributeSet() {
                private UnsupportedOperationException  doThrow() {
                    return new UnsupportedOperationException("cannot change values for the global EMPTY_ATTRIBUTESET. You attempted to modify this unmodifiable CSSPropertySet: create your own instance of a CSSPropertySet first!");
                }

                public String put(String name, String value) {
                    throw doThrow();
                }

                public boolean putAll(CSSAttributeSet attributes) {
                    throw doThrow();
                }
            };

    private Map map;

    /**
     * create a CSSPropertySet from the given HashMap.
     */
    private CSSAttributeSet(HashMap map) {
        this.map = map;
    }

    /**
     * Creates a new, empty atribute set.
     */
    public CSSAttributeSet() {
    }

    public CSSAttributeSet(CSSProperty cssProperty, String cssPropertyValue) {
        put(cssProperty, cssPropertyValue);
    }

    /**
     * Creates a new attribute set based on a supplied set of attributes.
     *
     * @param source the set of attributes
     */
    public CSSAttributeSet(CSSAttributeSet source) {
        putAll(source);
    }

    /**
     * Checks whether the set of attributes is empty.
     *
     * @return true if the set is empty else false
     */
    public final boolean isEmpty() {
        return map == null || map.isEmpty();
    }

    /**
     * Gets a count of the number of attributes.
     *
     * @return the count
     */
    public final int size() {
        return map == null ? 0 : map.size();
    }

    public final void clear() {
        if (map != null) {
            map.clear();
        }
    }

    /**
     * Tells whether a given attribute is defined.
     *
     * @param name the attribute name
     * @return true if the attribute is defined
     */
    public final boolean contains(CSSProperty name) {
        return map == null ? false : map.containsKey(name);
    }

    /**
     * Gets the defned CSS properties in the set.
     *
     * @return A set of {@link CSSProperty}
     */
    public final Set properties() {
        return map == null ? Collections.EMPTY_SET : map.keySet();
    }

    /**
     * Gets the value of an css property.
     *
     * @param property the attribute property
     * @return the value
     */
    public final String get(CSSProperty property) {
        return map == null ? null : (String) map.get(property);
    }

    /**
     * Adds an attribute to the list.
     *
     * @param name  the attribute name
     * @param value the attribute value
     */
    public String put(CSSProperty name, String value) {
        if (map == null) {
            map = new HashMap(8);
        }

        if (value == null)
            return remove(name);
        return (String) map.put(name, value);
    }

    /**
     * Adds a set of attributes to the list.
     *
     * @param attributes the set of attributes to add
     */
    public boolean putAll(CSSAttributeSet attributes) {
        if (map == null) {
            map = new HashMap(8);
        }

        boolean changed = false;
        Iterator names = attributes.properties().iterator();
        while (names.hasNext()) {
            CSSProperty property = (CSSProperty) names.next();
            changed = changed || (put(property, attributes.get(property)) != null);
        }
        return changed;
    }

    /**
     * Removes an attribute from the list.
     *
     * @param name the attribute name
     */
    public String remove(CSSProperty name) {
        return map == null ? null : (String) map.remove(name);
    }

    // --- Object methods ---------------------------------

    /**
     * Clones a set of attributes.
     *
     * @return the new set of attributes
     */
    public Object clone() {
        /*
        try {
            attr = (CSSPropertySet)super.clone();
            attr.putAll(this);
        } catch (CloneNotSupportedException cnse) {
            attr = null;
        }
        */

        if (isEmpty()) {
            return new CSSAttributeSet();
        } else {
            return new CSSAttributeSet((HashMap) ((HashMap) map).clone());
        }
    }

    /**
     * Returns a hashcode for this set of attributes.
     *
     * @return a hashcode value for this set of attributes.
     */
    public int hashCode() {
        return map == null ? 0 : map.hashCode();
    }

    /**
     * Compares two attribute sets.
     *
     * @param object the second attribute set
     * @return true if the sets are equal, false otherwise
     */
    public boolean equals(Object object) {
        if (!(object instanceof CSSAttributeSet))
            return false;
        CSSAttributeSet other = (CSSAttributeSet) object;

        if (size() != other.size())
            return false;

        Iterator names = other.properties().iterator();
        while (names.hasNext()) {
            CSSProperty property = (CSSProperty) names.next();
            if (!other.get(property).equals(get(property)))
                return false;
        }
        return true;
    }

    /**
     * Write style definition to the device. If include is true, write those
     * contained in the {@link java.util.List}. If include is false, write those not contained
     * in the {@link java.util.List}.
     * Basically this is a filter on the styles, so we can separate styles for
     * one logical component onto multiple real html elements.  
     */
    public void writeFiltered(Device d, List l, boolean include) throws IOException {
        if (l == null) l = Collections.EMPTY_LIST;
        if (map != null) {
            Iterator names = map.entrySet().iterator();
            while (names.hasNext()) {
                Map.Entry next = (Map.Entry) names.next();
                if ( !(l.contains(next.getKey()) ^ include) ) {
                    d.print(next.getKey()).print(":")
                            .print(next.getValue())
                            .print("; ");
                }
            }
        }
    }
    
    /**
     * Write style definition to the device. Write only those not contained
     * in the set.
     */
    public void writeExcluding(Device d, List l) throws IOException {
        writeFiltered(d, l, false);
    }

    /**
     * Write style definition to the device. Write only those  contained
     * in the set.
     */
    public void writeIncluding(Device d, List l) throws IOException {
        writeFiltered(d, l, true);
    }

    /**
     * Write style definition to the device
     */
    public void write(Device d)
            throws IOException {
        if (map != null) {
            Iterator names = map.entrySet().iterator();
            while (names.hasNext()) {
                Map.Entry next = (Map.Entry) names.next();
                d.print(next.getKey()).print(":")
                        .print(next.getValue())
                        .print("; ");
            }
        }
    }

    /**
     * Converts the attribute set to a String.
     *
     * @return the string
     */
    public String toString() {
        StringBuffer b = new StringBuffer();

        if (map != null) {
            Iterator names = map.entrySet().iterator();
            while (names.hasNext()) {
                Map.Entry next = (Map.Entry) names.next();
                b.append(next.getKey());
                b.append(":");
                b.append(next.getValue());
                b.append("; ");
            }
        }
        return b.toString();
    }
}


