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
package org.wings.jsp;

import org.wings.externalizer.ExternalizedResource;
import org.wings.io.Device;
import org.wings.io.DeviceFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hengels
 * @version $Revision$
 */
public class ExternalizerServlet extends HttpServlet {
    private ServletConfig servletConfig;

    /* (non-Javadoc)
    * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
    */
    public void init(ServletConfig servletConfig) throws ServletException {
        this.servletConfig = servletConfig;
    }

    /*
    * (non-Javadoc)
    * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WingsSession wingsSession = WingsSession.getSession(servletConfig, request, response);

        synchronized (request.getSession()) {
            String path = request.getServletPath();
            if (path == null)
                return;

            int pos = path.lastIndexOf('/');
            path = path.substring(pos + 1);
            ExternalizedResource extInfo = wingsSession.getExternalizeManager().getExternalizedResource(path);
            Device outputDevice = DeviceFactory.createDevice(extInfo);
            wingsSession.getExternalizeManager().deliver(extInfo, response, outputDevice);
        }
    }
}
