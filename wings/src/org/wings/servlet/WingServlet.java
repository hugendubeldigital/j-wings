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

package org.wings.servlet;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wings.*;
import org.wings.util.*;
import org.wings.session.Session;
import org.wings.externalizer.SystemExternalizeManager;
import org.wings.externalizer.ExternalizeManager;
import org.wings.externalizer.AbstractExternalizeManager;
import org.wings.externalizer.ExternalizedInfo;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class WingServlet extends HttpServlet
{

    protected static final Boolean initializer = new Boolean(true);

    static {
        /*
        try {
            LogManager.getLogManager().readConfiguration();
        }
        catch (Throwable t) {}
        */
    }

    protected static Logger logger = Logger.getLogger("org.wings.servlet");

    /**
     * used to init session servlets
     */
    protected ServletConfig servletConfig = null;
    
    /** */
    private String lookupName = "SessionServlet";

    /**
     * TODO: documentation
     */
    public WingServlet() {}

    /**
     *
     */
    protected ExternalizeManager createExternalizeManager(HttpServletResponse response) {
        ExternalizeManager extManager = new ExternalizeManager(response);
        return extManager;
    }

    /**
     * preInit is called by init before doing something. <br>
     * Normally one overwrites postInit...
     *
     * @param config the serlvet configuration
     */
    protected void preInit(ServletConfig config) throws ServletException {}

    protected void initLookupName(ServletConfig config) 
        throws ServletException 
    {
        // with specified lookupname it is possible to handle different sessions
        // for servlet aliases/mappings
        lookupName = config.getInitParameter("wings.servlet.lookupname");

        if ( lookupName==null || lookupName.trim().length()==0 ) {
            lookupName = "SessionServlet:" + getClass().getName();
        }
    }

    /*
     * The following init parameters are known by wings.
     *
     * <dl compact>
     * <dt>externalizer.timeout</dt><dd> - The time, externalized objects 
     *          are kept, before they are removed</dd>
     *
     * <dt>content.maxlength</dt><dd> - Maximum content lengt for form posts. 
     *          Remember to increase this, if you make use of the SFileChooser
     *          component</dd>
     *
     * <dt>filechooser.uploaddir</dt><dd> - The directory, where uploaded
     *          files ar stored temporarily</dd>
     * </dl>
     *
     * <dt>wings.servlet.lookupname</dt><dd> - The name the wings sessions of
     * this servlet instance are stored in the servlet session hashtable</dd>
     * </dl>
     *
     * @param config
     * @throws ServletException
     */
    public final void init(ServletConfig config) throws ServletException {
        servletConfig = config;

        preInit(config);
        super.init(config);
        
        initLookupName();

        if (logger.isLoggable(Level.CONFIG)) {
            logger.config("init-params:");
            for (Enumeration en = config.getInitParameterNames(); en.hasMoreElements();) {
                String param = (String)en.nextElement();
                logger.config(param + " = " + config.getInitParameter(param));
            }
        }

        postInit(config);
    }

    /**
     * postInit is called by init after it's finished. <br>
     * Overwrite this method if you have to initialize something in your
     * servlet.
     *
     * @param config the serlvet configuration
     */
    protected void postInit(ServletConfig config) throws ServletException {
    }

    /**
     * This factory returns a new SessionServlet used to handle
     * all the requests within a usersession. <br>
     * Must be overwritten.
     *
     * @return a SessionServlet
     * @throws Exception everything might happen here.
     */
    public abstract SessionServlet generateSessionServlet(HttpServletRequest req)
        throws Exception;


    /**
     * TODO: documentation
     */
    public final void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        SessionServlet sessionServlet = null;

        synchronized (initializer) {
            sessionServlet = getSessionServlet(req, res);
        }

        if (logger.isLoggable(Level.FINE))
            logger.fine((sessionServlet != null) ?
                        "sessionServlet: " + sessionServlet.getClass().getName() :
                        "no session yet ..");

        // Wrap with MultipartRequest which can handle multipart/form-data
        // (file - upload), otherwise behaves like normal HttpServletRequest
        try {
            int maxContentLength = sessionServlet.getSession().getMaxContentLength();
            req = new MultipartRequest(req, maxContentLength * 1024);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }

        if (logger.isLoggable(Level.FINER)) {
            if (req instanceof MultipartRequest) {
                MultipartRequest multi = (MultipartRequest)req;
                logger.finer("Files:");
                Iterator files = multi.getFileNames();
                while (files.hasNext()) {
                    String name = (String)files.next();
                    String filename = multi.getFileName(name);
                    String type = multi.getContentType(name);
                    File f = multi.getFile(name);
                    logger.finer("name: " + name);
                    logger.finer("filename: " + filename);
                    logger.finer("type: " + type);
                    if (f != null) {
                        logger.finer("f.toString(): " + f.toString());
                        logger.finer("f.getName(): " + f.getName());
                        logger.finer("f.exists(): " + f.exists());
                        logger.finer("f.length(): " + f.length());
                        logger.finer("\n");
                    }
                }
            }
        }

        doGet(req, res);
    }

    private final SessionServlet newSession(HttpServletRequest request,
                                            HttpServletResponse response)
        throws ServletException
    {
        try {
            logger.fine("new session");

            HttpSession session = request.getSession(true);

            SessionServlet sessionServlet = generateSessionServlet(request);
            sessionServlet.setParent(this);
            sessionServlet.setExternalizeManager(createExternalizeManager(response));
            sessionServlet.init(servletConfig);
            session.setAttribute(lookupName, sessionServlet);

            return sessionServlet;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            throw new ServletException(e);
        }
    }

    public final SessionServlet getSessionServlet(HttpServletRequest request,
                                                  HttpServletResponse response) 
        throws ServletException 
    {
        HttpSession session = request.getSession(false);
        SessionServlet sessionServlet = null;

        if (session != null)
            sessionServlet = (SessionServlet)session.getAttribute(lookupName);
        
        if (sessionServlet == null)
            sessionServlet = newSession(request, response);

        return sessionServlet;
    }

    /** -- externalization -- **/

    /**
     * returns, whether this request is to serve an externalize request.
     */
    protected boolean isSystemExternalizeRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();

        return (pathInfo != null && pathInfo.length()>1 && pathInfo.charAt(1)=='-');
    }

    /**
     *
     */
    protected AbstractExternalizeManager getExternalizeManager(HttpServletRequest req) 
        throws ServletException 
    {
        
        AbstractExternalizeManager extManager = null;
        if ( isSystemExternalizeRequest(req) )
            return SystemExternalizeManager.getSharedInstance();
        else {
            SessionServlet sessionServlet = null;
            synchronized (initializer) {
                sessionServlet = getSessionServlet(req, null);
            }
            
            return sessionServlet.getSession().getExternalizeManager();
        }
    }
    
    
    /**
     * TODO: documentation
     */
    public final void doGet(HttpServletRequest req,
                            HttpServletResponse response)
        throws ServletException, IOException
    {
        org.wings.util.TimeMeasure m = new TimeMeasure();
        m.start("doGet");
        try {
            /* 
             * make sure, that our context ends with '/'. Otherwise redirect
             * to the same location with appended slash. 
             *
             * We need a '/' at the
             * end of the servlet, so that relative requests work. Relative
             * requests are either externalization requests, providing the
             * required resource name in the path info (like 'abc_121.gif')
             * or 'normal' requests which are just an empty URL with the 
             * request parameter (like '?12_22=121').
             * The browser assembles the request URL from the current context
             * (the 'directory' it assumes it is in) plus the relative URL.
             * Thus emitted URLs are as short as possible and thus the generated
             * page size.
             */
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.length() == 0) {
                StringBuffer pathUrl = HttpUtils.getRequestURL(req);
                pathUrl.append('/');
                if (req.getQueryString() != null) {
                    pathUrl.append('?').append(req.getQueryString());
                }

                logger.fine("redirect to " + pathUrl.toString());
                response.sendRedirect(pathUrl.toString());
                return;
            }

            /*
             * we either have a request for the system externalizer
             * (if there is something in the path info, that starts with '-')
             * or just a normal request to this servlet.
             */
            if (isSystemExternalizeRequest(req)) {
                String identifier = pathInfo.substring(1);
                logger.fine("system externalizer: " + identifier);

                int pos = identifier.indexOf(".");
                if (pos > -1)
                    identifier = identifier.substring(0, pos);

                logger.fine("system externalizer " + identifier);
                SystemExternalizeManager.getSharedInstance().deliver(identifier, response);
                return;
            }

            logger.fine("session servlet");

            SessionServlet sessionServlet = null;
            synchronized (initializer) {
                sessionServlet = getSessionServlet(req, response);
            }

            sessionServlet.doGet(req, response);
        }
        catch (Throwable e) {
            logger.log(Level.SEVERE, "doGet", e);
            e.printStackTrace(System.err);
            throw new ServletException(e);
        }
        finally {
            m.stop();
            logger.fine(m.print());
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
