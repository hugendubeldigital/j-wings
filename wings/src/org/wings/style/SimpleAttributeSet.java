/*
 * $Id$
 * (c) Copyright 2001 wingS development team.
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
package org.wings.style;

import java.util.*;
import java.io.*;

/**
 * A straightforward implementation of AttributeSet using a hash map.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SimpleAttributeSet
    implements AttributeSet, Serializable, Cloneable
{
    private final transient Map map;

    /**
     * create a SimpleAttributeSet from the given HashMap.
     */
    private SimpleAttributeSet(HashMap map) {
        this.map = map;
    }

    /**
     * Creates a new, empty atribute set.
     */
    public SimpleAttributeSet() {
        this(new HashMap(3));
    }
    
    /**
     * Creates a new attribute set based on a supplied set of attributes.
     *
     * @param source the set of attributes
     */
    public SimpleAttributeSet(AttributeSet source) {
        this();
        putAttributes(source);
    }

    /**
     * Checks whether the set of attributes is empty.
     *
     * @return true if the set is empty else false
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Gets a count of the number of attributes.
     *
     * @return the count
     */
    public int size() {
        return map.size();
    }
    
    public void clear() {
	map.clear();
    }
    
    /**
     * Tells whether a given attribute is defined.
     *
     * @param attrName the attribute name
     * @return true if the attribute is defined
     */
    public boolean isDefined(String name) {
	return map.containsKey(name);
    }

    /**
     * Gets the names of the attributes in the set.
     *
     * @return the names as an <code>Enumeration</code>
     */
    public Set names() {
        return map.keySet();
    }

    /**
     * Gets the value of an attribute.
     *
     * @param name the attribute name
     * @return the value
     */
    public String getAttribute(String name) {
        return (String)map.get(name);
    }

    /**
     * Adds an attribute to the list.
     *
     * @param name the attribute name
     * @param value the attribute value
     */
    public String putAttribute(String name, String value) {
        if (value == null)
            return removeAttribute(name);
        return (String)map.put(name, value);
    }

    /**
     * Adds a set of attributes to the list.
     *
     * @param attributes the set of attributes to add
     */
    public boolean putAttributes(AttributeSet attributes) {
	boolean changed = false;
        Iterator names = attributes.names().iterator();
        while (names.hasNext()) {
            String name = (String)names.next();
            changed = changed || (putAttribute(name, attributes.getAttribute(name)) != null);
        }
	return changed;
    }

    /**
     * Removes an attribute from the list.
     *
     * @param name the attribute name
     */
    public String removeAttribute(String name) {
        return (String)map.remove(name);
    }

    // --- Object methods ---------------------------------

    /**
     * Clones a set of attributes.
     *
     * @return the new set of attributes
     */
    public Object clone() {
	SimpleAttributeSet attr;
	try {
	    attr = (SimpleAttributeSet)super.clone();
	    attr.putAttributes(this);
	} catch (CloneNotSupportedException cnse) {
	    attr = null;
	}
        return attr;
    }

    /**
     * Returns a hashcode for this set of attributes.
     * @return     a hashcode value for this set of attributes.
     */
    public int hashCode() {
	return map.hashCode();
    }

    /**
     * Compares two attribute sets.
     *
     * @param attr the second attribute set
     * @return true if the sets are equal, false otherwise
     */
    public boolean equals(Object object) {
	if (!(object instanceof AttributeSet))
	    return false;
	AttributeSet other = (AttributeSet)object;

	if (size() != other.size())
	    return false;

        Iterator names = other.names().iterator();
        while (names.hasNext()) {
            String name = (String)names.next();
            if (!other.getAttribute(name).equals(getAttribute(name)))
		return false;
        }
	return true;
    }

    /**
     * Converts the attribute set to a String.
     *
     * @return the string
     */
    public String toString() {
	StringBuffer b = new StringBuffer();
        Iterator names = map.entrySet().iterator();
        while (names.hasNext()) {
	    Map.Entry next = (Map.Entry)names.next();
	    b.append(next.getKey());
	    b.append(":");
	    b.append(next.getValue());
	    b.append("; ");
	}
	return b.toString();
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
