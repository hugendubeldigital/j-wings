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

import java.io.*;
import java.util.*;

import org.wings.io.Device;
import org.wings.Renderable;

/**
 * A generic interface for a mutable collection of unique attributes.
 *
 * Implementations will probably want to provide a constructor of the
 * form:<tt>
 * public XXXAttributeSet(ConstAttributeSet source);</tt>
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public interface AttributeSet extends Renderable
{
    /**
     * Returns the number of attributes contained in this set.
     *
     * @return the number of attributes
     */
    public int size();

    /**
     * Checks whether the named attribute has a value specified in
     * the set
     *
     * @param attrName the attribute name
     * @return true if the attribute has a value specified
     */
    public boolean isDefined(String name);

    /**
     * Fetches the value of the given attribute.
     * If the attribute is not defined, null is returned.
     *
     * @param key the non-null key of the attribute binding
     * @return the value
     */
    public String getAttribute(String key);

    /**
     * Returns an enumeration over the names of the attributes in the set.
     * The values of the <code>Enumeration</code> may be anything
     * and are not constrained to a particular <code>Object</code> type.
     * The set does not include the resolving parent, if one is defined.
     *
     * @return the names
     */
    public Set names();

    /**
     * Creates a new attribute set similar to this one except that it contains
     * an attribute with the given name and value.  The object must be
     * immutable, or not mutated by any client.
     *
     * @param name the name
     * @param value the value
     */
    public String putAttribute(String name, String value);

    /**
     * Creates a new attribute set similar to this one except that it contains
     * the given attributes and values.
     *
     * @param attributes the set of attributes
     */
    public boolean putAttributes(AttributeSet attributes);

    /**
     * Removes an attribute with the given <code>name</code>.
     *
     * @param name the attribute name
     */
    public String removeAttribute(String name);

    /**
     * clear the contents of this AttributeSet.
     */
    public void clear();

    /**
     * Write attribute definitions to the device
     */
    void write(Device d) throws IOException;
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
