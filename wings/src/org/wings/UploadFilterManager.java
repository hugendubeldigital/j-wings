/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import java.io.*;
import java.lang.reflect.*;
import java.util.Hashtable;

/**
 * TODO: documentation
 *
 * @author
 * @version $Revision$
 */
public class UploadFilterManager
{
    private static Hashtable filterMappings = new Hashtable();

    /**
     * TODO: documentation
     */
    public static void registerFilter(String name, Class filter, String errorText) {
        if (!FilterOutputStream.class.isAssignableFrom(filter))
            throw new IllegalArgumentException("class is not a FilterOutputStream!");

        Entry entry = new Entry(filter);
        entry.errorText = errorText;
        filterMappings.put(name, entry);
    }

    /**
     * TODO: documentation
     */
    public static void registerFilter(String name, Class filter) {
        registerFilter(name, filter, null);
    }

    /**
     * TODO: documentation
     */
    public static Class getFilterClass(String name) {
        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        name = name.substring(dividerIndex+1);

        return (Class)((Entry)filterMappings.get(name)).filterClass;
    }

    /**
     * TODO: documentation
     */
    public static FilterOutputStream createFilterInstance(String name, OutputStream out) {
        FilterOutputStream filter = null;
        try {
            Entry entry = getFilterEntry(name);
            if (entry == null)
                filter = new FilterOutputStream(out);
            else {
                Class filterClass = entry.filterClass;
                if (filterClass != null) {
                    System.err.println("using " + filterClass.getName() + " for " + name);
                    Constructor constructor = filterClass.getConstructor(new Class[] { OutputStream.class });
                    filter = (FilterOutputStream)constructor.newInstance(new Object[] { out });
                    entry.filterInstance = filter;
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
        return filter;
    }

    /**
     * TODO: documentation
     */
    public static FilterOutputStream getFilterInstance(String name) {
        try {
            Entry entry = getFilterEntry(name);
            FilterOutputStream filterInstance = entry.filterInstance;
            return filterInstance;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
    }

    private static Entry getFilterEntry(String name) {
        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        name = name.substring(dividerIndex+1);

        return (Entry)filterMappings.get(name);
    }

    private static class Entry
    {
        public Class filterClass;
        public FilterOutputStream filterInstance;
        public String errorText;

        /**
         * TODO: documentation
         *
         * @param filterClass
         */
        public Entry(Class filterClass) {
            this.filterClass = filterClass;
        }

        /**
         * TODO: documentation
         *
         * @param filterInstance
         */
        public void setInstance(FilterOutputStream filterInstance) {
            this.filterInstance = filterInstance;
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
