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
import java.util.*;
import java.util.logging.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.wings.*;

import org.wings.externalizer.ExternalizeManager;
import org.wings.io.ServletDevice;
import org.wings.session.*;
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
    protected static Logger _wingsLogger = Logger.getLogger("org.wings.servlet");

    protected final TimeMeasure measure =
        new TimeMeasure(new MessageFormat("<b>{0}</b>: {1} <i>{2}</i><br />"));

    private SRequestDispatcher dispatcher = null;

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
     * when the user exits the session with one of the exitSession() methods,
     * this URL is set to the URL the browser should be redirected after
     * the session. This is set in the exitSession(String) method and
     * evaluated in doGet() after the event dispatching phase.
     */
    private String afterSessionURL = null;

    private boolean firstRequest = true;

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
                _wingsLogger.throwing(SessionServlet.class.getName(), "setLocaleFromHeader", e);
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
                _wingsLogger.throwing(SessionServlet.class.getName(), "setLocale", e);
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
                _wingsLogger.throwing(SessionServlet.class.getName(), "setLocale", e);
            }
        }
    }

    /**
     * creates a locale object from a string (de, de-AT, en-US,...).
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

    /**
     * sets a new locale for this session. The locale is <em>only</em> set,
     * if it is one of the supported locales {@link #setSupportedLocales},
     * otherwise an IllegalArgumentException is thrown.
     * 
     * @param l the locale to be associated with this session.
     * @throws IllegalArgumentException, if this locale is not supported, as
     *         predefined with {@link #setSupportedLocales}.
     */
    protected final void setLocale(Locale l) throws IllegalArgumentException {
        if (supportedLocales==null ||
             supportedLocales.length==0 ||
             ASUtil.inside(l, supportedLocales)) {
            session.setLocale(l);
            _wingsLogger.config("Set Locale " + l);
        } else
            throw new IllegalArgumentException("Locale " + l +" not supported");
    }

    /**
     * sets the locales, supported by this application. If empty or 
     * <em>null</em>, all locales are supported.
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
        _wingsLogger.fine("Dispatch form GET parameter");
        String paramName = query.substring(0, query.indexOf("="));
        String value = query.substring(query.indexOf("=")+1);
        String[] values = {value};
        getDispatcher().dispatch(paramName, values);
        return true;
    }


    /**
     * this method references to 
     * {@link doGet(HttpServletRequest, HttpServletResponse)}
     */
    public final void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        //value chosen to limit denial of service
        if (req.getContentLength() > getSession().getMaxContentLength()*1024) {
            res.setContentType("text/html");
            ServletOutputStream out = res.getOutputStream();
            out.println("<html><head><title>Too big</title></head>");
            out.println("<body><h1>Error - content length &gt; " +
                        getSession().getMaxContentLength() + "k");
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
            RequestURL requestURL = new RequestURL("", response.encodeURL(""));
            // this will fire an event, if the encoding has changed ..
            ((PropertyService)session).setProperty("request.url", requestURL);

            try {
                if (_wingsLogger.isLoggable(Level.FINER)) {
                    _wingsLogger.finer("RequestURL: " + requestURL);
                    _wingsLogger.finer("\nHEADER:");
                    for (Enumeration en = req.getHeaderNames(); en.hasMoreElements();) {
                        String header = (String)en.nextElement();
                        _wingsLogger.finer("   " + header + ": " + req.getHeader(header));
                    }
                    _wingsLogger.finer("");
                }

                handleLocale(req);
            }
            finally {
                prepareRequest(req, response);
            }

            try {
                ServletRequest asreq = new ServletRequest(req);

                if (_wingsLogger.isLoggable(Level.FINER))
                    measure.start("time to dispatch");

                // it's the dispatcher's resposibility to check the event's actuality
                boolean events = false;
                Enumeration en = req.getParameterNames();
                while (en.hasMoreElements()) {
                    String paramName = (String)en.nextElement();
                    String[] value = req.getParameterValues(paramName);
                    _wingsLogger.fine("dispatching " + paramName + " = " + value[0]);

                    // was soll das???
                    if (!getDispatcher().dispatch(paramName, value))
                        asreq.addParam(paramName, value);
                    else
                        events = true;
                }

                if (req.getMethod().toUpperCase().equals("POST")) {
                    events = events || dispatchPostQuery(req.getQueryString());
                }

                if (_wingsLogger.isLoggable(Level.FINER)) {
                    measure.stop();
                    measure.start("time to fire form events");
                }

                if (events)
                    SForm.fireEvents();

                if (_wingsLogger.isLoggable(Level.FINER)) {
                    measure.stop();
                    measure.start("time to process request");
                }

                if (events)
                    processRequest(asreq, response);

                // if the user chose to exit the session as a reaction on an
                // event, we got an URL to redirect after the session.
                /*
                 * where is the right place?
                 * The right place is _after_ we processed the events 
                 * (e.g. the 'Pressed Exit-Button'-event or gave
                 * the user the chance to exit this session in the custom
                 * processRequest(), but _before_ the rendering of the page,
                 * because otherwise an redirect won't work.
                 */
                if (afterSessionURL != null) {
                    req.getSession().invalidate(); // calls destroy implicitly
                    if (afterSessionURL.length() > 0)
                        response.sendRedirect(afterSessionURL);
                    else
                        response.sendRedirect(HttpUtils.getRequestURL(req)
                                              .toString());
                    return;
                }

                // invalidate frames and resources
                if (events)
                    getSession().getReloadManager().invalidateResources();

                // deliver resource
                // the externalizer is able to handle static and dynamic resources
                ExternalizeManager extManager = getSession().getExternalizeManager();
                String pathInfo = req.getPathInfo().substring(1);
                _wingsLogger.fine("pathInfo: " + pathInfo);

                // no pathInfo .. getFrame()
                if (pathInfo == null || pathInfo.length() == 0 || "_".equals(pathInfo) || firstRequest) {
                    _wingsLogger.fine("delivering default frame");
                    firstRequest = false;

                    DynamicResource resource
                        = (DynamicResource)getFrame().getDynamicResource(DynamicCodeResource.class);
                    extManager.deliver(resource.getId(), response);
                }
                else
                    extManager.deliver(pathInfo, response);

                if (_wingsLogger.isLoggable(Level.FINER)) {
                    measure.stop();
                    _wingsLogger.finer(measure.print());
                    measure.reset();
                }
            }
            catch (Throwable e) {
                _wingsLogger.log(Level.SEVERE, "exception: ", e);
                e.printStackTrace(System.err);
                handleException(req, response, e);
                throw new ServletException(e);
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

    protected void prepareRequest(HttpServletRequest req, HttpServletResponse response) {}

    protected void processRequest(HttpServletRequest req, HttpServletResponse response)
        throws ServletException, IOException {}

    protected void finalizeRequest(HttpServletRequest req, HttpServletResponse response) {}

    // Exception Handling

    private SFrame errorFrame;

    private SLabel errorStackTraceLabel;
    private SLabel errorMessageLabel;

    protected void handleException(HttpServletRequest req, HttpServletResponse res, Throwable e) {
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
        catch (Exception ex) {}
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
     * Exit the current session and redirect to other URL.
     *
     * This removes the session and its associated
     * application from memory. The browser is redirected to the given
     * URL. Note, that it is not even possible for the user to re-enter 
     * the application with the BACK-button, since all information is 
     * removed. 
     *
     * <em>Always</em> exit an application by calling an 
     * <code>exitSession()</code> method, especially, if it is an application 
     * that requires a login and thus handles sensitive information accessible
     * through the session. Usually, you will call this on behalf of an 
     * event within an <code>ActionListener.actionPerformed()</code> like for 
     * a pressed 'EXIT'-Button.
     *
     * @param redirectAddress the address, the browser is redirected after
     *                        removing this session. This must be a String
     *                        containing the complete URL (no relative URL)
     *                        to the place to be redirected. If 'null', nothing
     *                        happens.
     */
    protected void exitSession(String redirectAddress) {
        afterSessionURL = redirectAddress;
    }

    /**
     * Exit the current session and redirect to new application instance.
     *
     * This removes the session and its associated
     * application from memory. The browser is redirected to the same
     * application with a fresh session. Note, that it is not even
     * possible for the user to re-enter the old application with the 
     * BACK-button, since all information is removed. 
     * 
     * <em>Always</em> exit an application by calling an 
     * <code>exitSession()</code> method, especially, if it is an application 
     * that requires an login and thus handles sensitive information accessible
     * through the session. Usually, you will call this on behalf of an 
     * event within an <code>ActionListener.actionPerformed()</code> like for 
     * a pressed 'EXIT'-Button.
     */
    protected void exitSession() { exitSession(""); }

    /**
     * TODO: documentation
     *
     */
    public void destroy() {
        _wingsLogger.info("destroy called");

        LookAndFeelFactory.unregisterSession(session);

        try {
            SFrame f = getFrame();
            // traverse all frames in a frameset ?
            if (f != null && f.getContentPane() != null)
                f.getContentPane().removeAll();
            // remove all elements in ExternalizerCache ?
        }
        catch (Exception e) {
            _wingsLogger.throwing(SessionServlet.class.getName(), "destroy", e);
        }
        finally {
            Runtime rt = Runtime.getRuntime();
            if (_wingsLogger.isLoggable(Level.FINE))
                _wingsLogger.fine("free mem before gc: " + rt.freeMemory());
            rt.gc();
            if (_wingsLogger.isLoggable(Level.FINE))
                _wingsLogger.fine("free mem after gc: " + rt.freeMemory());
        }
    }


    public static final void debug(String msg) {
        _wingsLogger.fine(msg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
