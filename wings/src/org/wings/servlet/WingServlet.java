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
    /**
     * TODO: documentation
     */
    public static final boolean DEBUG = true;

    /**
     * The maximal length of data that is accepted in one POST request.
     * Data can be this big, if your application provides a capability
     * to upload a file (SFileChoose). This constant limits the maximum
     * size that is accepted to avoid denial of service attacks.
     */
    protected int maxContentLength = 64; // in kByte

    protected ServletConfig servletConfig = null;

    private String lookupName = "SessionServlet";

    /**
     * TODO: documentation
     *
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

    /**
     * TODO: documentation
     *
     * @param config
     */
    protected void initMaxContentLength(ServletConfig config) {
        String maxCL = config.getInitParameter("content.maxlength");
        if (maxCL != null) {
            try {
                maxContentLength = Integer.parseInt(maxCL);
            }
            catch (NumberFormatException e) {
                System.err.println("invalid content.maxlength: " + maxCL);
            }
            debug("content.maxlength: " + maxContentLength);
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
     * @param config
     * @throws ServletException
     */
    public final void init(ServletConfig config) throws ServletException {
        preInit(config);
        super.init(config);
        lookupName = "SessionServlet:" + getClass().getName();

        if (DEBUG) {
            debug("Init Parameter");
            for (Enumeration en=config.getInitParameterNames(); en.hasMoreElements();) {
                String param = (String)en.nextElement();
                debug(param + " = " + config.getInitParameter(param));
            }
        }

        initMaxContentLength(config);

        servletConfig = config;
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
        HttpSession session = req.getSession(false);

        if (session != null)
            sessionServlet = (SessionServlet)session.getAttribute(lookupName);

        if (sessionServlet!=null)
            debug("sessionServlet: " + sessionServlet.getClass().getName());
        else
            debug("no session yet...");

        // Wrap with MultipartRequest which can handle multipart/form-data
        // (file - upload), otherwise behaves like normal HttpServletRequest
        try {
            req = new MultipartRequest(req, maxContentLength * 1024);
        }
        catch (IOException e) {
            res.getOutputStream().println(e.getMessage());
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }

        if (DEBUG) {
            MultipartRequest multi = (MultipartRequest)req;
            debug("Files:");
            Enumeration files = multi.getFileNames();
            while (files.hasMoreElements()) {
                String name = (String)files.nextElement();
                String filename = multi.getFilename(name);
                String type = multi.getContentType(name);
                File f = multi.getFile(name);
                debug("name: " + name);
                debug("filename: " + filename);
                debug("type: " + type);
                if (f != null) {
                    debug("f.toString(): " + f.toString());
                    debug("f.getName(): " + f.getName());
                    debug("f.exists(): " + f.exists());
                    debug("f.length(): " + f.length());
                    debug("\n");
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
            log("generating new Session Servlet");
            debug("generating new Session Servlet");

            HttpSession session = request.getSession(true);

            SessionServlet sessionServlet = generateSessionServlet(request);
            sessionServlet.setParent(this);
            sessionServlet.setExternalizeManager(createExternalizeManager(response));
            sessionServlet.init(servletConfig);
            session.setAttribute(lookupName, sessionServlet);

            return sessionServlet;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e.toString());
        }
    }

    static final Boolean initializer = new Boolean(true);

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
    protected boolean isExternalizeRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return (pathInfo != null && pathInfo.length() >=2);
    }

    /**
     * returns, whether this request is to serve an externalize request.
     */
    protected boolean isSystemExternalizeRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return (pathInfo != null && pathInfo.length() >=2 && pathInfo.charAt(1)=='-');
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
     * returns the last modification of an externalized resource to allow the
     * browser to cache it. The 'normal' output of this servlet cannot be
     * cached, so this method returns '-1' if this is not an externalize
     * request.
     */
    protected long getLastModified(HttpServletRequest req) {
        try {
            if ( isExternalizeRequest(req) ) {
                ExternalizedInfo info = getExternalizeManager(req).
                    getExternalizedInfo(req.getPathInfo().substring(1));
                
                if (info == null) {
                    debug ("info is null!");
                    return -1;
                }
                return info.getLastModified();
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return -1;
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
         * wanted resource name in the path info (like 'abc_121.gif')
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
            response.sendRedirect(pathUrl.toString());
            return;
        }

        /*
         * we either have a request for externalization
         * (if there is something in the path info) or just a normal
         * request to this servlet.
         */
        if (isExternalizeRequest(req)) {
            getExternalizeManager(req).deliver(pathInfo.substring(1), response);
            return;
        }
        
        SessionServlet sessionServlet = null;
        synchronized (initializer) {
            sessionServlet = getSessionServlet(req, response);
        }

        if (DEBUG) {
            if (req.getParameterValues("exit")!=null) {
                req.getSession(false).invalidate();
                sessionServlet.destroy();
                sessionServlet = null;
                System.gc();
                try {Thread.sleep(1000);} catch (Exception e){}
                System.exit(0);
            }
        }

        sessionServlet.doGet(req, response);

        } finally {
            m.stop();
            debug(m.print());
        }
    }

    private static final void debug(String msg) {
        if (DEBUG) {
            DebugUtil.printDebugMessage(WingServlet.class, msg);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
