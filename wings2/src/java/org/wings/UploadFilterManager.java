/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class UploadFilterManager {
    private final static Logger logger = Logger.getLogger("org.wings.servlet");

    private static HashMap filterMappings = new HashMap();

    public static void registerFilter(String name, Class filter, String errorText) {
        if (!FilterOutputStream.class.isAssignableFrom(filter))
            throw new IllegalArgumentException("class is not a FilterOutputStream!");

        Entry entry = new Entry(filter);
        entry.errorText = errorText;
        filterMappings.put(name, entry);
    }

    public static void registerFilter(String name, Class filter) {
        registerFilter(name, filter, null);
    }

    public static Class getFilterClass(String name) {
        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        name = name.substring(dividerIndex + 1);

        return (Class) ((Entry) filterMappings.get(name)).filterClass;
    }

    public static FilterOutputStream createFilterInstance(String name, OutputStream out) {
        FilterOutputStream filter = null;
        try {
            Entry entry = getFilterEntry(name);
            if (entry == null)
                filter = new FilterOutputStream(out);
            else {
                Class filterClass = entry.filterClass;
                if (filterClass != null) {
                    logger.info("using " + filterClass.getName() + " for " + name);
                    Constructor constructor = filterClass.getConstructor(new Class[]{OutputStream.class});
                    filter = (FilterOutputStream) constructor.newInstance(new Object[]{out});
                    entry.filterInstance = filter;
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return filter;
    }

    public static FilterOutputStream getFilterInstance(String name) {
        try {
            Entry entry = getFilterEntry(name);
            FilterOutputStream filterInstance = entry.filterInstance;
            return filterInstance;
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            return null;
        }
    }

    private static Entry getFilterEntry(String name) {
        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        name = name.substring(dividerIndex + 1);

        return (Entry) filterMappings.get(name);
    }

    private static class Entry {
        public Class filterClass;
        public FilterOutputStream filterInstance;
        public String errorText;


        public Entry(Class filterClass) {
            this.filterClass = filterClass;
        }


        public void setInstance(FilterOutputStream filterInstance) {
            this.filterInstance = filterInstance;
        }
    }
}


