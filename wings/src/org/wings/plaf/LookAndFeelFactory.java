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

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

import org.wings.plaf.*;
import org.wings.session.*;

public abstract class LookAndFeelFactory
{
    private static Logger logger = Logger.getLogger("org.wings.plaf");

    private static String DEFAULT_LOOKANDFEEL_FACTORY = "org.wings.plaf.LookAndFeelFactory$Default";

    private static LookAndFeelFactory factory;

    public static void setLookAndFeelFactory(LookAndFeelFactory factory) {
        LookAndFeelFactory.factory = factory;
    }
    public static LookAndFeelFactory getLookAndFeelFactory() {
        if (factory == null) {
	    synchronized (LookAndFeelFactory.class) {
		if (factory == null) {
		    String className = (String)SessionManager.getSession().getProperty("wings.lookandfeel.factory");
		    if (className == null)
			className = DEFAULT_LOOKANDFEEL_FACTORY;

		    try {
			Class factoryClass = null;
			try {
			    factoryClass = Class.forName(className, true,
							 Thread.currentThread()
							 .getContextClassLoader());
			}
			catch (ClassNotFoundException e) {
			    // fallback, in case the servlet container fails to set the
			    // context class loader.
			    factoryClass = Class.forName(className);
			}
			factory = (LookAndFeelFactory)factoryClass.newInstance();
		    }
		    catch (Exception e) {
			logger.log(Level.SEVERE, "could not load wings.lookandfeel.factory: " +
				   className, e);
			throw new RuntimeException("could not load" + 
                                                   " wings.lookandfeel.factory: " +
                                                   className + 
                                                   "(" + e.getMessage() +")");
		    }
		}
	    }
        }
        return factory;
    }

    public static LookAndFeel createLookAndFeel()
	throws IOException
    {
        return getLookAndFeelFactory().create();
    }

    protected abstract LookAndFeel create() throws IOException;

    static class Default
	extends LookAndFeelFactory
    {
        private static String DEFAULT_LOOKANDFEEL = "/css1.jar";
        private LookAndFeel laf;

	protected LookAndFeel create()
	    throws IOException
	{
            if (laf == null) {
                synchronized(Default.class) {
                    if (laf == null) {
                        Session session = SessionManager.getSession();
                        String urlName = (String)session.getProperty("wings.lookandfeel.default");
                        if (urlName == null)
                            urlName = DEFAULT_LOOKANDFEEL;

                        URL url =
                            // bug in tomcat 4.0.2 and 4.0.3, getResource returns jndi
                            // URL, which ist not a supported protocol (maybe they
                            // forgot the URLStreamHandler...)
                            // servletConfig.getServletContext().getResource(lafName);
                            new URL("file:" + session.getServletContext().getRealPath(urlName));
                        ClassLoader classLoader = new URLClassLoader(new URL[] { url },
                                                                     LookAndFeelFactory.class.getClassLoader());
                        laf = new LookAndFeel(classLoader);
                    }
                }
            }
            return laf;
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
