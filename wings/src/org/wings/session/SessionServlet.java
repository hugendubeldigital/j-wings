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

package org.wings.session;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.wings.*;

import org.wings.externalizer.ExternalizeManager;
import org.wings.externalizer.ExternalizedResource;
import org.wings.io.ServletDevice;
import org.wings.io.Device;
import org.wings.io.DeviceFactory;
import org.wings.util.ComponentVisitor;
import org.wings.session.*;
import org.wings.util.DebugUtil;
import org.wings.util.TimeMeasure;
import org.wings.plaf.LookAndFeelFactory;
import org.wings.event.SRequestEvent;

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
final class SessionServlet
    extends HttpServlet
    implements HttpSessionBindingListener
{
    private static Logger logger = Logger.getLogger("org.wings.session");

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
     * This should be a resource ..
     */
    protected String errorTemplateFile;

    /**
     * The session.
     */
    private Session session = null;

    private boolean firstRequest = true;

    /**
     * TODO: documentation
     *
     * @param session
     */
    protected SessionServlet() {
        this.session = new Session();
        SessionManager.setSession(session);
    }

    /**
     * TODO: documentation
     */
    protected final void setParent(HttpServlet p) {
        if (p!=null) parent = p;
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
                logger.throwing(SessionServlet.class.getName(), "setLocaleFromHeader", e);
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
                logger.throwing(SessionServlet.class.getName(), "setLocale", e);
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
                logger.throwing(SessionServlet.class.getName(), "setLocale", e);
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
            Arrays.asList(supportedLocales).contains(l)) {
            session.setLocale(l);
            logger.config("Set Locale " + l);
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
        if (errorTemplateFile == null) {
            errorTemplateFile = config.getInitParameter("wings.error.template");
        }
    }

    /**
     * set the externalize manager
     */
    protected void setExternalizeManager(ExternalizeManager em) {
        session.setExternalizeManager(em);
    }

    /**
     * init
     */
    public final void init(ServletConfig config) throws ServletException {
        try {
            try {
                session.init(config);
                initErrorTemplate(config);
                session.getCGManager().setLookAndFeel(LookAndFeelFactory.createLookAndFeel());
            }
            catch (Exception e) {
                logger.log(Level.SEVERE, "could not load look and feel: " +
                           config.getInitParameter("wings.lookandfeel.factory"), e);
                throw new ServletException(e);
            }

            try {
                String mainClassName = config.getInitParameter("wings.mainclass");
                Class mainClass = null;
                try {
                    mainClass = Class.forName(mainClassName, true,
                                              Thread.currentThread()
                                              .getContextClassLoader());
                }
                catch (ClassNotFoundException e) {
                    // fallback, in case the servlet container fails to set the
                    // context class loader.
                    mainClass = Class.forName(mainClassName);
                }
                Object main = mainClass.newInstance();
            }
            catch (Exception e) {
                logger.log(Level.SEVERE, "could not load wings.mainclass: " +
                           config.getInitParameter("wings.mainclass"), e);
                throw new ServletException(e);
            }
        }
        finally {
            // The session was set by the constructor. After init we
            // expect that only doPost/doGet is called, which set the
            // session also. So remove it here.
            SessionManager.removeSession();
        }
    }

    /**
     * this method references to 
     * {@link #doGet(HttpServletRequest, HttpServletResponse)}
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
        session.setServletRequest(req);
        session.setServletResponse(response);

        TimeMeasure measure = null;
        if (logger.isLoggable(Level.FINER)) {        
            measure = new TimeMeasure(new MessageFormat("{0}: {1} {2}"));
        }
        // in case, the previous thread did not clean up.
        SForm.clearArmedComponents();

        Device outputDevice = null;

        try {
            /*
             * The tomcat 3.x has a bug, in that it does not encode the URL
             * sometimes. It does so, when there is a cookie, containing some
             * tomcat sessionid but that is invalid (because, for instance,
             * we restarted the tomcat in-between). 
             * [I can't think of this being the correct behaviour, so I assume
             *  it is a bug. ]
             *
             * So we have to workaround this here: if we actually got the
             * session id from a cookie, but it is not valid, we don't do
             * the encodeURL() here: we just leave the requestURL as it is
             * in the properties .. and this is url-encoded, since
             * we had set it up in the very beginning of this session 
             * with URL-encoding on  (see WingServlet::newSession()).
             *
             * Vice versa: if the requestedSessionId is valid, then we can
             * do the encoding (which then does URL-encoding or not, depending
             * whether the servlet engine detected a cookie).
             * (hen)
             */
            RequestURL requestURL = null;
            if (req.isRequestedSessionIdValid()) {
                 requestURL = new RequestURL("", getSessionEncoding(response));
                // this will fire an event, if the encoding has changed ..
                ((PropertyService)session).setProperty("request.url", 
                                                       requestURL);
            }

            if (logger.isLoggable(Level.FINER)) {
                logger.finer("RequestURL: " + requestURL);
                logger.finer("\nHEADER:");
                for (Enumeration en = req.getHeaderNames(); en.hasMoreElements();) {
                    String header = (String)en.nextElement();
                    logger.finer("   " + header + ": " + req.getHeader(header));
                }
                logger.finer("");
            }

            handleLocale(req);

            if (logger.isLoggable(Level.FINER)) {
                measure.start("time to dispatch");
            }
            

            Enumeration en = req.getParameterNames();
            
            // are there parameters/low level events to dispatch 
            if ( en.hasMoreElements() ) {
                // only fire DISPATCH_START if we have parameters to dispatch
                session.fireRequestEvent(SRequestEvent.DISPATCH_START);
                
                while (en.hasMoreElements()) {
                    String paramName = (String)en.nextElement();
                    String[] value = req.getParameterValues(paramName);

                    logger.fine("dispatching " + paramName + " = " + value[0]);
                    
                    session.getDispatcher().dispatch(paramName, value);
                }

                if (logger.isLoggable(Level.FINER)) {
                    measure.stop();
                    measure.start("time to fire form events");
                }
            
                SForm.fireEvents();
            
                // only fire DISPATCH DONE if we have parameters to dispatch
                session.fireRequestEvent(SRequestEvent.DISPATCH_DONE);
            }
            
            
            if (logger.isLoggable(Level.FINER)) {
                measure.stop();
                measure.start("time to process request");
            }
            
            // if the user chose to exit the session as a reaction on an
            // event, we got an URL to redirect after the session.
            /*
             * where is the right place?
             * The right place is 
             *    - _after_ we processed the events 
             *        (e.g. the 'Pressed Exit-Button'-event or gave
             *         the user the chance to exit this session in the custom
             *         processRequest())
             *    - but _before_ the rendering of the page,
             *      because otherwise an redirect won't work, since we must
             *      not have sent anything to the output stream).
             */
            if (session.getRedirectAddress() != null) {
                req.getSession().invalidate(); // calls destroy implicitly
                if (session.getRedirectAddress().length() > 0) {
                    // redirect to user requested URL.
                    response.sendRedirect(session.getRedirectAddress());
                }
                else {
                    // redirect to a fresh session.
                    response.sendRedirect(HttpUtils.getRequestURL(req)
                                          .toString());
                }
                return;
            }
            
            // invalidate frames and resources
            getSession().getReloadManager().invalidateResources();
            
            // deliver resource. The
            // externalizer is able to handle static and dynamic resources
            ExternalizeManager extManager = getSession().getExternalizeManager();
            String pathInfo = req.getPathInfo().substring(1);
            logger.fine("pathInfo: " + pathInfo);

            /*
             * if we have no path info, or the special '_' path info
             * (that should be explained somewhere, Holger), then we
             * deliver the toplevel Frame of this application.
             */
            String externalizeIdentifier = null;
            if (pathInfo == null
                || pathInfo.length() == 0 
                || "_".equals(pathInfo) 
                || firstRequest) {
                logger.fine("delivering default frame");

                if (session.frames().size() == 0)
                    throw new ServletException("no frame visible");
                
                // get the first frame from the set and walk up the hierarchy.
                // this should work in most cases. if there are more than one
                // toplevel frames, the developer has to care about the resource
                // ids anyway ..
                SFrame defaultFrame = (SFrame)session.frames().iterator().next();
                while (defaultFrame.getParent() != null)
                    defaultFrame = (SFrame)defaultFrame.getParent();
                
                Resource resource = defaultFrame.getDynamicResource(DynamicCodeResource.class);
                externalizeIdentifier = resource.getId();

                if (firstRequest)
                    response.setDateHeader("Expires", 1000);
                firstRequest = false;
            }
            else {
                externalizeIdentifier = pathInfo;
            }
            
            // externalized this resource.
            ExternalizedResource extInfo = extManager
                .getExternalizedResource(externalizeIdentifier);
            if (extInfo != null) {
                outputDevice = DeviceFactory.createDevice(extInfo);
                //outputDevice = createOutputDevice(req, response, extInfo);

                session.fireRequestEvent(SRequestEvent.DELIVER_START, extInfo);

                extManager.deliver(extInfo, response, outputDevice);

                session.fireRequestEvent(SRequestEvent.DELIVER_DONE, extInfo);
            }
            
            if (logger.isLoggable(Level.FINER)) {
                measure.stop();
                logger.finer(measure.print());
                measure.reset();
            }
        }
        catch (Throwable e) {
            logger.log(Level.SEVERE, "exception: ", e);
            handleException(req, response, e);
        }
        finally {
            if (outputDevice != null) {
                try { outputDevice.close(); } catch (Exception e) {}
            }
            getSession().getReloadManager().clear();

            // make sure that the session association to the thread is removed
            // from the SessionManager
            SessionManager.removeSession();
        }
    }

    /**
     * create a Device that is used to deliver the content, that is
     * session specific.
     * The default
     * implementation just creates a ServletDevice. You can override this
     * method to decide yourself what happens to the output. You might, for
     * instance, write some device, that logs the output for debugging
     * purposes, or one that creates a gziped output stream to transfer
     * data more efficiently. You get the request and response as well as
     * the ExternalizedResource to decide, what kind of device you want to create.
     * You can rely on the fact, that extInfo is not null.
     * Further, you can rely on the fact, that noting has been written yet
     * to the output, so that you can set you own set of Headers.
     *
     * @param request  the HttpServletRequest that is answered
     * @param response the HttpServletResponse.
     * @param extInfo  the externalized info of the resource about to be
     *                 delivered.
     */
    protected Device createOutputDevice(HttpServletRequest request,
                                        HttpServletResponse response,
                                        ExternalizedResource extInfo) 
        throws IOException {
        return new ServletDevice(response.getOutputStream());
    }



    // Exception Handling

    private SFrame errorFrame;

    private SLabel errorStackTraceLabel;
    private SLabel errorMessageLabel;

    protected void handleException(HttpServletRequest req, 
                                   HttpServletResponse res, Throwable e) 
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
        session.destroy();
    }

    /**
     * get the Session Encoding, that is appended to each URL.
     * Basically, this is response.encodeURL(""), but unfortuntatly, this
     * empty encoding isn't supported by Tomcat 4.x anymore.
     */
    public static String getSessionEncoding(HttpServletResponse response) {
        if (response == null) return "";
        String enc = response.encodeURL("foo").substring(3);
        return enc;
    }

    /**
     * TODO: documentation
     *
     */
    public void destroy() {
        logger.info("destroy called");

        try {
            // hint the gc.
            setParent(null);
            session.destroy();
            session = null;
        }
        catch (Exception e) {
            logger.throwing(SessionServlet.class.getName(), "destroy", e);
        }
        finally {
            Runtime rt = Runtime.getRuntime();
            if (logger.isLoggable(Level.FINE))
                logger.fine("free mem before gc: " + rt.freeMemory());
            rt.gc();
            if (logger.isLoggable(Level.FINE))
                logger.fine("free mem after gc: " + rt.freeMemory());
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
