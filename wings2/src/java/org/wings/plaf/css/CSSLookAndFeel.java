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
package org.wings.plaf.css;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.session.Browser;
import org.wings.session.BrowserType;
import org.wings.session.SessionManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CSSLookAndFeel
        extends org.wings.plaf.LookAndFeel {
    private final transient static Log log = LogFactory.getLog(CSSLookAndFeel.class);
    private static final String PROPERTIES_LOCATION_START = "WEB-INF/" + CSSLookAndFeel.class.getPackage().getName();

    public CSSLookAndFeel() throws IOException {
        super(loadProperties());
    }

    private static Properties loadProperties() throws IOException {
        StringBuffer propertiesLocation = new StringBuffer(PROPERTIES_LOCATION_START);
        switch (SessionManager.getSession().getUserAgent().getBrowserType().getId()) {
            case BrowserType.BrowserID.IE: 
                propertiesLocation.append(".msie");
                break;
            default:
        }
        propertiesLocation.append(".properties");
        try {
            Properties properties = new Properties();
            InputStream in = SessionManager.getSession().getServletContext().getResourceAsStream(propertiesLocation.toString());
            properties.load(in);
            in.close();
            return properties;
        } catch (Exception e) {
            final String error = "Unable to open " + propertiesLocation.toString() + " due to "+e+".\nPlease check deployment!";
            log.fatal(error);
            throw new IOException(error);
        }

    }
}


