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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.*;
import javax.servlet.http.*;

import org.wings.*;

import org.wings.externalizer.ExternalizeManager;
import org.wings.io.ServletDevice;
import org.wings.session.DefaultSession;
import org.wings.session.Session;
import org.wings.session.SessionManager;
import org.wings.util.ASUtil;
import org.wings.util.DebugUtil;
import org.wings.util.TimeMeasure;
import org.wings.plaf.LookAndFeelFactory;

/**
 * TODO: documentation
Die ServletEngine erzeugt für jeden User eine eigene HttpSession. Auf diese kann
man von allen Servlets, die in der Engine laufen, zugreifen. _Ein_ WingServlet
erzeugt ein SessionServlet pro HttpSession und legt es dort ab. Da das
SessionServlet als Wrapper für das WingServlet fungiert, kann man von dort, wie
gewohnt auf den ServletContext und die HttpSession zugreifen. Zusätzlich hängt
am SessionServlet die wingS-Session mit wichtigen Services und der übergeordnete
SFrame. An diesem Frame hängen alle wingS-Komponenten und somit der gesamte
Zustand der Anwendung. Der Programmierer bekommt über den SessionManager von
jeder Stelle aus eine Referenz auf die wingS-Session. Das SessionServlet bietet
zudem Zugang zur Servlet übergreifenden HttpSession.

 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class SessionServlet
    extends HttpServlet
    implements HttpSessionBindingListener
{
    /**
     * TODO: documentation
     */
    public static final boolean DEBUG = true;

    /**
     * TODO: documentation
     */
    private SRequestDispatcher dispatcher = null;

    /**
     * TODO: documentation
     */
    protected final TimeMeasure measure =
        new TimeMeasure(new MessageFormat("<b>{0}</b>: {1} <i>{2}</i><br />"));

    /**
     * Maximum length of post-/get-submissions in kByte
     */
    private int maxContentLength = 50;

    /**
     * TODO: documentation
     */
    protected HttpServlet parent = this;

    /**
     * All supported Locales.
     */
    private Locale[] supportedLocales = null;

    /**
     * Is locale supplied by the browser?
     */
    private boolean localeFromHeader = true;

    /**
     * The default frame. Mostly the one and only frame.
     */
    private SFrame frame = null;

    /**
     * This should be a resource ..
     */
    protected String errorTemplateFile;

    /**
     * The session.
     */
    private Session session = null;

    /**
     * TODO: documentation
     *
     * @param session
     */
    protected SessionServlet(Session session) {
        this.session = session;
        SessionManager.setSession(session);
        LookAndFeelFactory.registerSession(session);
    }

    /**
     * This default constructor sets the default session.
     * @see org.wings.session.DefaultSession
     */
    protected SessionServlet() {
        this(new DefaultSession());
    }

    /**
     * TODO: documentation
     */
    protected final void setParent(HttpServlet p) {
        if (p!=null)
            parent = p;
    }

    public final Session getSession() {
        return session;
    }

    /**
     * TODO: documentation
     */
    public final void setLocaleFromHeader(String[] args) {
        if (args==null)
            return;

        for (int i=0; i<args.length; i++) {
            try {
                setLocaleFromHeader(new Boolean(args[i]).booleanValue());
            } catch (Exception e) {
                if (DEBUG)
                    log(e.getMessage());
            }
        }
    }

    /**
     * TODO: documentation
     */
    public final void setLocaleFromHeader(boolean b) {
        localeFromHeader = b;
    }

    /**
     * TODO: documentation
     */
    public final boolean getLocaleFromHeader() {
        return localeFromHeader;
    }

    /**
     * TODO: documentation
     */
    public final Locale getLocale() {
        return session.getLocale();
    }

    /*
     * A String, containing a comma separated list of canonical locale names.
     */
    private final void setLocale(String locales) {
        if (locales == null)
            return;
        StringTokenizer tokenizer = new StringTokenizer(locales, ",");

        while (tokenizer.hasMoreTokens()) {
            try {
                setLocale(getLocale(tokenizer.nextToken()));
                return;
            } catch (IllegalArgumentException e) {
                if (DEBUG)
                    log(e.getMessage());
            }
        }
    }

    /*
     * An array of canonical locale names.
     */
    private final void setLocale(String[] locales) {
        if (locales == null)
            return;

        for (int i=0; i<locales.length; i++) {
            try {
                setLocale(locales[i]);
                return;
            } catch (IllegalArgumentException e) {
                if (DEBUG)
                    log(e.getMessage());
            }
        }
    }

    /*
     * Erzeugt aus einem Locale String (de, de-AT, en-US,...) ein Locale Object
     */
    private final Locale getLocale(String localeString) {
        String args[] = {"", "", ""};
        StringTokenizer tokenizer = new StringTokenizer(localeString, "-");
        int index = 0;
        while (tokenizer.hasMoreTokens()) {
            if (index>args.length)
                break;
            args[index++] = tokenizer.nextToken();
        }

        return new Locale(args[0], args[1], args[2]);
    }

    /*
     * Setzt ein neues Locale. Das Locale wird nur dann neu gesetzt, wenn es ein
     * unterstuetztes Locale {@link #setSupportedLocales} ist, ansonsten wird eine
     * IllegalArgumentException geworfen.
     */
    /**
     * TODO: documentation
     */
    protected final void setLocale(Locale l) {
        if (supportedLocales==null ||
             supportedLocales.length==0 ||
             ASUtil.inside(l, supportedLocales)) {
            session.setLocale(l);
            debug("Set Locale " + l);
        } else
            throw new IllegalArgumentException("Locale " + l +" not supported");
    }

    /*
     * Setzt die unterstuetzten Locales. Falls null oder leer, werden alle Locales
     * unterstuetzt.
     */
    /**
     * TODO: documentation
     */
    protected final void setSupportedLocales(Locale[] locales) {
        supportedLocales = locales;
    }

    /*
     * Das Locale des Servlets wird ueber das Locale des Browsers bestimmt und den
     * verfuegbaren Locales bestimmt. Wird jedoch ueber einen Parameter
     * <PRE>Lang</PRE> dem Servlet mitgeteilt, ein spezielles Locale zu setzen,
     * wird der Header ignoriert. Uber den Parameter <PRE>LocaleFromHeader</PRE>
     * mit Werten true/false, kann diese Verhalten gesteuert werden.
     */
    /**
     * TODO: documentation
     */
    protected final void handleLocale(HttpServletRequest req) {
        setLocaleFromHeader(req.getParameterValues("LocaleFromHeader"));

        if (localeFromHeader)
            setLocale(req.getHeader("Accept-Language"));
        if (req.getParameterValues("Lang")!=null) {
            setLocale(req.getParameterValues("Lang"));
            setLocaleFromHeader(false);
        }
    }

    // jetzt kommen alle Servlet Methoden, die an den parent deligiert
    // werden

    /**
     * TODO: documentation
     *
     * @return
     */
    public ServletContext getServletContext() {
        if (parent!=this)
            return parent.getServletContext();
        else
            return super.getServletContext();
    }

    /**
     * TODO: documentation
     *
     * @param name
     * @return
     */
    public String getInitParameter(String name) {
        if (parent!=this)
            return parent.getInitParameter(name);
        else
            return super.getInitParameter(name);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Enumeration getInitParameterNames() {
        if (parent!=this)
            return parent.getInitParameterNames();
        else
            return super.getInitParameterNames();
    }

    /**
     * TODO: documentation
     *
     * @param msg
     */
    public void log(String msg) {
        if (parent!=this)
            parent.log(msg);
        else
            super.log(msg);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getServletInfo() {
        if (parent!=this)
            return parent.getServletInfo();
        else
            return super.getServletInfo();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public ServletConfig getServletConfig() {
        if (parent!=this)
            return parent.getServletConfig();
        else
            return super.getServletConfig();
    }

    // bis hierhin

    /**
     * TODO: documentation
     *
     * @param config
     * @throws ServletException
     */
    protected void initErrorTemplate(ServletConfig config) throws ServletException {
        if (errorTemplateFile==null) {
            String errorTemplate = config.getInitParameter("ErrorTemplateFile");
        }
    }

    /**
     * set the externalize manager
     */
    protected void setExternalizeManager(ExternalizeManager em) {
        session.setExternalizeManager(em);
    }

    /**
     * set the frame
     *
     * @return set the frame(set) for this session
     */
    public final void setFrame(SFrame frame) {
        if (this.frame != null)
          frame.setRequestURL(this.frame.getRequestURL());

        this.frame = frame;
    }

    /**
     * get the frame
     *
     * @return the frame for this session
     */
    public final SFrame getFrame() {
        if (frame == null)
            setFrame(new SFrame());

        return frame;
    }


    /**
     * preInit, called by init before doing something
     */
    protected void preInit(ServletConfig config) throws ServletException {
    }

    /**
     * init
     */
    public final void init(ServletConfig config) throws ServletException {
        try {
            preInit(config);
            session.init(config);
            initErrorTemplate(config);
            postInit(config);
        }
        finally {
            // The session was set by the constructor. After init we
            // expect that only doPost/doGet is called, which set the
            // session also. So remove it here.
            SessionManager.removeSession();
        }
    }

    /**
     * postInit, called by init after it's finished
     */
    protected void postInit(ServletConfig config) throws ServletException {
    }


    /**
     * TODO: documentation
     */
    public final SRequestDispatcher getDispatcher() {
        if (dispatcher == null)
            dispatcher = session.getDispatcher();
        return dispatcher;
    }

    /**
     * Hiermit ist es moeglich in Post Actions auch Parameter einzubetten und
     * danach zu dispatchen.
     * TODO muss noch vervollstaendigt werden.
     */
    private final boolean dispatchPostQuery(String query) {
        if (query == null)
            return false;
        // hier noch get Parameter der Form parsen und dispatchen!!
        debug("Dispatch Form Get");
        String paramName = query.substring(0, query.indexOf("="));
        String value = query.substring(query.indexOf("=")+1);
        String[] values = {value};
        getDispatcher().dispatch(paramName, values);
        return true;
    }


    /**
     * this method references to {@link doGet}
     */
    public final void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        //value chosen to limit denial of service
        if (req.getContentLength() > maxContentLength*1024) {
            res.setContentType("text/html");
            ServletOutputStream out = res.getOutputStream();
            out.println("<html><head><title>Too big</title></head>");
            out.println("<body><h1>Error - content length &gt; " +
                        maxContentLength + "k");
            out.println("</h1></body></html>");
        }
        else {
            doGet(req, res);
        }
        // sollte man den obigen Block nicht durch folgende Zeile ersetzen?
        //throw new RuntimeException("this method must never be called!");
    }


    /**
     * Verarbeitet Informationen vom Browser:
     * <UL>
     * <LI> setzt Locale
     * <LI> Dispatch Get Parameter
     * <LI> feuert Form Events
     * </UL>
     * Ist synchronized, damit nur ein Frame gleichzeitig bearbeitet werden kann.
     * {@link org.wings.SFrameSet}
     * @deprecated
     */
    public final synchronized void doGet(HttpServletRequest req,
                                         HttpServletResponse response)
        throws ServletException, IOException
    {
        SessionManager.setSession(session);

        try {
            try {
                if (DEBUG) {
                    log("\nHEADER:");
                    for (Enumeration en = req.getHeaderNames(); en.hasMoreElements();) {
                        String header = (String)en.nextElement();
                        log("   " + header + ": " + req.getHeader(header));
                    }
                    log("");
                }

                handleLocale(req);

                //evtl. HttpUtils.getRequestURL(req).toString()
                getFrame().setRequestURL(new RequestURL("", response.encodeURL("")));
            }
            finally {
                prepareRequest(req, response);
            }

            try {
                ServletRequest asreq = new ServletRequest(req);

                ExternalizeManager extManager = getSession().getExternalizeManager();

                if (DEBUG)
                    measure.start("time to dispatch");

                // check actuality
                RequestURL request = new RequestURL(req.getPathInfo());

                debug("request " + request);

                DynamicResource context = null;
                if ( request.getContext()==null && request.getEpoch()!=null )
                    context = getFrame().getDynamicResource(DynamicCodeResource.class);
                else 
                    context = 
                        (DynamicResource)extManager.getExternalizedObject(request.getContext());
                
                if ( request.getEpoch()==null || 
                     context.getEpoch().equals(request.getEpoch()) ) {

                    debug("dispatching");

                    Enumeration en = null;
                    en = req.getParameterNames();
                    while (en.hasMoreElements()) {
                        String paramName = (String)en.nextElement();
                        String[] value = req.getParameterValues(paramName);
                        if (!getDispatcher().dispatch(paramName, value))
                            asreq.addParam(paramName,value);
                    }
                    
                    if (req.getMethod().toUpperCase().equals("POST")) {
                        dispatchPostQuery(req.getQueryString());
                    }
                    
                    if (DEBUG) {
                        measure.stop();
                        measure.start("time to fire form events");
                    }
                    
                    SForm.fireEvents();
                }
                    
                if (DEBUG) {
                    measure.stop();
                    measure.start("time to process request");
                }
                    
                // default content ist text/html
                response.setContentType("text/html;charset=" + session.getCharSet());

                // Page must not be cached since higly dynamic
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control",
                                   "max-age=0, no-cache, must-revalidate");
                // 1000 (one second after Jan 01 1970) because of some IE bug:
                response.setDateHeader("Expires", 1000);

                processRequest(asreq, response);


                //                externalizeManager schreibt Resource
                DynamicResource resource = 
                    (DynamicResource)extManager.getExternalizedObject(request.getResource());

                if ( resource==null )
                    resource = getFrame().getDynamicResource(DynamicCodeResource.class);

                resource.write(new ServletDevice(response.getOutputStream()));

                if (DEBUG) {
                    measure.stop();
                    debug(measure.print());
                    measure.reset();
                }

            }
            catch (Exception e) {
                handleException(req, response, e);
            }
            finally {
                finalizeRequest(req, response);
            }
        }
        finally {
            // make sure that the session association to the thread is removed
            SessionManager.removeSession();
        }
    }

    protected void prepareRequest(HttpServletRequest req,
                                  HttpServletResponse response) {
    }

    protected void processRequest(HttpServletRequest req,
                                  HttpServletResponse response)
        throws ServletException, IOException {
    }

    protected void finalizeRequest(HttpServletRequest req,
                                   HttpServletResponse response) {
    }

    // Exception Handling

    private SFrame errorFrame;

    private SLabel errorStackTraceLabel;

    private SLabel errorMessageLabel;

    protected void handleException(HttpServletRequest req,
                                   HttpServletResponse res,
                                   Exception e)
    {
        try {
            if (errorFrame == null) {
                errorFrame = new SFrame();
                /*
                 * if we don't have an errorTemplateFile defined, then this
                 * will throw an Exception, so the StackTrace is NOT exposed
                 * to the user (may be security relevant)
                 */
                errorFrame.getContentPane().
                    setLayout(new STemplateLayout(errorTemplateFile));

                errorStackTraceLabel = new SLabel();
                errorFrame.getContentPane().add(errorStackTraceLabel,
                                                "EXCEPTION_STACK_TRACE");

                errorMessageLabel = new SLabel();
                errorFrame.getContentPane().add(errorMessageLabel,
                                                "EXCEPTION_MESSAGE");
            }

            ServletOutputStream out = res.getOutputStream();
            errorStackTraceLabel.setText(DebugUtil.getStackTraceString(e));
            errorMessageLabel.setText(e.getMessage());
            errorFrame.write(new ServletDevice (out));
        }
        catch (Exception ex) {
            // Ok, seems that we have no luck: write this to the error-Log.
            e.printStackTrace();
        }
    }

    /** --- HttpSessionBindingListener --- **/

    /**
     * TODO: documentation
     *
     * @param event
     */
    public void valueBound(HttpSessionBindingEvent event) {
    }

    /**
     * TODO: documentation
     *
     * @param event
     */
    public void valueUnbound(HttpSessionBindingEvent event) {
        destroy();
    }

    /**
     * TODO: documentation
     *
     */
    public void destroy() {
        debug("destroy called");

        LookAndFeelFactory.unregisterSession(session);

        try {
            SFrame f = getFrame();
            // traverse all frames in a frameset ?
            if (f != null && f.getContentPane() != null)
                f.getContentPane().removeAll();
            // remove all elements in ExternalizerCache ?
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Runtime rt = Runtime.getRuntime();
            if (DEBUG) debug("free mem before gc: " + rt.freeMemory());
            rt.gc();
            if (DEBUG) debug("free mem after gc: " + rt.freeMemory());
        }
    }


    private static final void debug(String msg) {
        if (DEBUG) {
            DebugUtil.printDebugMessage(SessionServlet.class, msg);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
