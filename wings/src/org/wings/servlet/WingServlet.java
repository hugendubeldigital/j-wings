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
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wings.*;
import org.wings.util.*;
import org.wings.externalizer.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class WingServlet
    extends HttpServlet
{
    /**
     * TODO: documentation
     */
    public static final boolean DEBUG = true;

    /**
     * TODO: documentation
     */
    protected int maxContentLength = 64; // in kByte
    protected String uploaddir;

    private ServletConfig servletConfig = null;

    private String lookupName = "SessionServlet";

    private final ExternalizeManager extManager = new ExternalizeManager();


    /**
     * TODO: documentation
     *
     */
    public WingServlet() {
    }


    /**
     * preInit is called by init before doing something. <br>
     * Normally one overwrites postInit...
     *
     * @param config the serlvet configuration
     */
    protected void preInit(ServletConfig config) throws ServletException {
    }


    /**
     * Initializes and registers the externalizer. Called by init(). <br>
     * Overwrite this method if you want to use another externalizer.
     *
     * @param config the serlvet configuration
     */
    protected void initExternalizer(ServletConfig config) {
        extManager.setExternalizer(new FileExternalizer(config));
    }

    /**
     * Initializes and registers the object handlers which are used by the
     * externalizer. <br>
     * Overwrite this method if you want to change the object handlers.
     *
     * @param config the serlvet configuration
     */
    protected void initExtObjectHandler(ServletConfig config) {
        extManager.addObjectHandler(new ImageObjectHandler());
        extManager.addObjectHandler(new ImageIconObjectHandler());
        extManager.addObjectHandler(new ResourceImageIconObjectHandler());
        extManager.addObjectHandler(new ResourceStyleSheetObjectHandler());
    }


    /**
     * TODO: documentation
     *
     * @param config
     */
    protected void initMaxContentLength(ServletConfig config) {
        String maxCL = config.getInitParameter("content.maxlength");
        if ( maxCL!=null ) {
            try {
                maxContentLength = Integer.parseInt(maxCL);
            }
            catch ( Exception e ) {
                System.err.println("invalid MaximumContentLength: " + maxCL);
            }
            debug("MaximumContentLength: " + maxContentLength);
        }
    }

    /*
     * The following init parameters are known by wings.
     *
     * <dl compact>
     * <dt>externalizer.file.path</dt><dd><b>FileExternalizer</b> - path, where the
     *     externalizer stores the files (below documentroot)</dd>
     * <dt>externalizer.file.url</dt><dd><b>FileExternalizer</b> - url, where the client
     *     can access those files</dd>
     * <dt>externalizer.servlet.url</dt><dd><b>ServletExternalizer</b>url, where the client
     *     can access the externalizer servlet</dd>
     * <dt>session.timeout</dt><dd><b>The time, a session is kept alive during inactivity</dd>
     * <dt>content.maxlength</dt><dd>Maximum content lengt for form posts. Remember to increase
     *     this, if you make use of the SFileChooser component</dd>
     * <dt>filechooser.uploaddir</dt><dd><b>The directory, where uploaded files ar stored
     *     temporarily</dd>
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
            for ( Enumeration en=config.getInitParameterNames(); en.hasMoreElements(); ) {
                String param = (String)en.nextElement();
                debug(param + " = " + config.getInitParameter(param));
            }
        }

        initExternalizer(config);
        initExtObjectHandler(config);
        initMaxContentLength(config);

        uploaddir = config.getInitParameter("filechooser.uploaddir");
        if (uploaddir == null || uploaddir.length() == 0)
            uploaddir = "/tmp";

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
     * TODO: documentation
     *
     * @return
     */
    public final ExternalizeManager getExternalizeManager() {
        return extManager;
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

        if ( session != null )
            sessionServlet = (SessionServlet)session.getValue(lookupName);

        if ( sessionServlet!=null )
            debug("sessionServlet: " + sessionServlet.getClass().getName());
        else
            debug("no session yet...");

        // es koennte multipart/form-data sein ..
        try {
            req = new MultipartRequest(req, uploaddir, maxContentLength * 1024);
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

    /**
     * TODO: documentation
     */
    protected TimeMeasure measure = new TimeMeasure();

    private final SessionServlet newSession(HttpServletRequest req)
        throws ServletException
    {
        try {
            log("generating Session Servlet");

            HttpSession session = req.getSession(true);

            SessionServlet sessionServlet = generateSessionServlet(req);
            sessionServlet.setParent(this);
            sessionServlet.setExternalizeManager(getExternalizeManager());
            sessionServlet.init(servletConfig);
            session.putValue(lookupName, sessionServlet);

            return sessionServlet;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e.toString());
        }
    }

    static final Boolean initializer = new Boolean(true);

    /**
     * TODO: documentation
     */
    public final void doGet(HttpServletRequest req,
                            HttpServletResponse response)
        throws ServletException, IOException
    {
        SessionServlet sessionServlet = null;

        synchronized (initializer) {
            HttpSession session = req.getSession(false);
            if ( session != null )
                sessionServlet = (SessionServlet)session.getValue(lookupName);

            if ( sessionServlet == null )
                sessionServlet = newSession(req);
        }

        if ( DEBUG ) {
            if ( req.getParameterValues("exit")!=null ) {
                req.getSession(false).invalidate();
                sessionServlet.destroy();
                sessionServlet = null;
                System.gc();
                try {Thread.sleep(1000);} catch (Exception e){}
                System.exit(0);
            }
        }

        sessionServlet.doGet(req, response);
    }


    private static final void debug(String msg) {
        if ( DEBUG ) {
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
