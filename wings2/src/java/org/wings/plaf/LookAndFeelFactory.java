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
package org.wings.plaf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.session.Session;
import org.wings.session.SessionManager;

import java.io.IOException;

public abstract class LookAndFeelFactory {
    private final transient static Log log = LogFactory.getLog(LookAndFeelFactory.class);

    private static String DEFAULT_LOOKANDFEEL_FACTORY = "org.wings.plaf.LookAndFeelFactory$Default";

    private static LookAndFeelFactory factory;

    public static void setLookAndFeelFactory(LookAndFeelFactory factory) {
        LookAndFeelFactory.factory = factory;
    }

    /**
     * Get the lool and feel factory.
     */
    public static LookAndFeelFactory getLookAndFeelFactory() {
        if (factory == null) {
            synchronized (LookAndFeelFactory.class) {
                if (factory == null) {
                    String className = (String) SessionManager.getSession().getProperty("wings.lookandfeel.factory");
                    if (className == null)
                        className = DEFAULT_LOOKANDFEEL_FACTORY;

                    try {
                        Class factoryClass = null;
                        try {
                            factoryClass = Class.forName(className, true,
                                    Thread.currentThread()
                                    .getContextClassLoader());
                        } catch (ClassNotFoundException e) {
                            // fallback, in case the servlet container fails to set the
                            // context class loader.
                            factoryClass = Class.forName(className);
                        }
                        factory = (LookAndFeelFactory) factoryClass.newInstance();
                    } catch (Exception e) {
                        log.fatal("could not load wings.lookandfeel.factory: " +
                                className, e);
                        throw new RuntimeException("could not load" +
                                " wings.lookandfeel.factory: " +
                                className +
                                "(" + e.getMessage() + ")");
                    }
                }
            }
        }
        return factory;
    }

    public abstract LookAndFeel create() throws IOException;

    static class Default extends LookAndFeelFactory {
        private static String DEFAULT_LOOKANDFEEL = "org.wings.plaf.css.LookAndFeel";
        private LookAndFeel laf;

        public LookAndFeel create()
                throws IOException {
            if (laf == null) {
                synchronized (Default.class) {
                    if (laf == null) {
                        Session session = SessionManager.getSession();
                        String lafName = (String) session.getProperty("wings.lookandfeel.default");
                        if (lafName == null)
                            lafName = DEFAULT_LOOKANDFEEL;

                        try {
                            Class lafClass = Class.forName(lafName, true, Thread.currentThread().getContextClassLoader());
                            laf = (LookAndFeel) lafClass.newInstance();
                        } catch (Exception e) {
                            log.fatal("create", e);
                            throw new IOException(e.getMessage());
                        }
                    }
                }
            }
            return laf;
        }
    }
}
