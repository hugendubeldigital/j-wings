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

import java.util.Enumeration;

class MultiCGDefaults extends CGDefaults
{
    private CGDefaults[] tables;

    public MultiCGDefaults(CGDefaults[] defaults) {
        super();
        tables = defaults;
    }

    public MultiCGDefaults() {
        super();
        tables = new CGDefaults[0];
    }

    public Object get(Object key)
    {
        Object value = super.get(key);
        if (value != null) {
            return value;
        }

        for(int i = 0; i < tables.length; i++) {
            CGDefaults table = tables[i];
            value = (table != null) ? table.get(key) : null;
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    public int size() {
        int n = super.size();
        for(int i = 0; i < tables.length; i++) {
            CGDefaults table = tables[i];
            n += (table != null) ? table.size() : 0;
        }
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Enumeration keys()
    {
        Enumeration[] enums = new Enumeration[1 + tables.length];
        enums[0] = super.keys();
        for(int i = 0; i < tables.length; i++) {
            CGDefaults table = tables[i];
            if (table != null) {
                enums[i + 1] = table.keys();
            }
        }
        return new MultiCGDefaultsEnumerator(enums);
    }

    public Enumeration elements()
    {
        Enumeration[] enums = new Enumeration[1 + tables.length];
        enums[0] = super.elements();
        for(int i = 0; i < tables.length; i++) {
            CGDefaults table = tables[i];
            if (table != null) {
                enums[i + 1] = table.elements();
            }
        }
        return new MultiCGDefaultsEnumerator(enums);
    }

    private static class MultiCGDefaultsEnumerator implements Enumeration
    {
        Enumeration[] enums;
        int n = 0;

        MultiCGDefaultsEnumerator(Enumeration[] enums) {
            this.enums = enums;
        }

        public boolean hasMoreElements() {
            for(int i = n; i < enums.length; i++) {
                Enumeration e = enums[i];
                if ((e != null) && (e.hasMoreElements())) {
                    return true;
                }
            }
            return false;
        }

        public Object nextElement() {
            for(; n < enums.length; n++) {
                Enumeration e = enums[n];
                if ((e != null) && (e.hasMoreElements())) {
                    return e.nextElement();
                }
            }
            return null;
        }
    }

    public Object remove(Object key)
    {
        Object value = super.remove(key);
        if (value != null) {
            return value;
        }
        for(int i = 0; i < tables.length; i++) {
            CGDefaults table = tables[i];
            value = (table != null) ? table.remove(key) : null;
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public void clear() {
        super.clear();
        for(int i = 0; i < tables.length; i++) {
            CGDefaults table = tables[i];
            if (table != null) {
                table.clear();
            }
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
