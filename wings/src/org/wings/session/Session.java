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

import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.event.EventListenerList;

import org.wings.*;
import org.wings.plaf.CGManager;
import org.wings.util.WeakPropertyChangeSupport;
import org.wings.util.StringUtil;
import org.wings.externalizer.ExternalizeManager;
import org.wings.externalizer.ExternalizedResource;
import org.wings.event.SRequestListener;
import org.wings.event.SRequestEvent;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class Session
    implements PropertyService
{
    public static String LOCALE_PROPERTY = "locale";
    public static String LOOK_AND_FEEL_PROPERTY = "lookAndFeel";
    private static Logger logger = Logger.getLogger("org.wings.session");

    private ServletContext servletContext;
    private CGManager cgManager = new CGManager();
    private ReloadManager reloadManager = null;
    private ExternalizeManager extManager = null;
    private final LowLevelEventDispatcher dispatcher = new LowLevelEventDispatcher();
    private Map props = new HashMap();

    private int uniqueIdCounter = 1;
    private int maxContentLength = 64;

    private final Set frames = new HashSet();

    /**
     * listeners registered for {@link SRequestEvent}
     */
    private List requestListener;

    /**
     * TODO: documentation
     *
     */
    public Session() {}

    /**
     * TODO: documentation
     *
     * @param config
     */
    public void init(ServletConfig config) {
        if (config == null)
            return;
        initProps(config);
        servletContext = config.getServletContext();

        String maxCL = config.getInitParameter("content.maxlength");
        if (maxCL != null) {
            try {
                maxContentLength = Integer.parseInt(maxCL);
            }
            catch (NumberFormatException e) {
                logger.log(Level.WARNING, "invalid content.maxlength: " + maxCL, e);
            }
        }
    }

    /**
     * Copy the init parameters.
     *
     * @param config
     */
    protected void initProps(ServletConfig config) {
        Enumeration params = config.getInitParameterNames();
        while(params.hasMoreElements()) {
            String name = (String)params.nextElement();
            props.put(name, config.getInitParameter(name));
        }
    }

    private HttpServletRequest servletRequest;
    void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }
    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    private HttpServletResponse servletResponse;
    void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }
    public HttpServletResponse getServletResponse() {
        return servletResponse;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public CGManager getCGManager() {
        return cgManager;
    }

    public void setReloadManager(ReloadManager reloadManager) {
        this.reloadManager = reloadManager;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public ReloadManager getReloadManager() {
        if (reloadManager == null)
            reloadManager = new DefaultReloadManager();
        return reloadManager;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public ExternalizeManager getExternalizeManager() {
        return extManager;
    }

    /**
     * TODO: documentation
     *
     */
    void setExternalizeManager(ExternalizeManager em) {
        extManager = em;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public LowLevelEventDispatcher getDispatcher() {
        return dispatcher;
    }

    public void addFrame(SFrame frame) {
        frames.add(frame);
    }
    public void removeFrame(SFrame frame) {
        frames.remove(frame);
    }
    public Set frames() {
        return frames;
    }

    /**
     * The root frame is the first shown frame.
     */
    public SFrame getRootFrame() {
        if ( frames.size() == 0)
            return null;

        SFrame rootFrame = (SFrame)frames.iterator().next();
        while (rootFrame.getParent() != null)
            rootFrame = (SFrame)rootFrame.getParent();

        return rootFrame;
    }

    public Map getProperties() {
        return Collections.unmodifiableMap(props);
    }

    /**
     * Gets the session property indicated by the specified key.
     *
     * @param      key   the name of the session property.
     * @return     the string value of the session property,
     *             or <code>null</code> if there is no property with that key.
     */
    public Object getProperty(String key) {
        return props.get(key);
    }

    /**
     * Gets the session property indicated by the specified key.
     *
     * @param      key   the name of the session property.
     * @param      def   a default value.
     * @return     the string value of the session property,
     *             or the default value if there is no property with that key.
     * @see        org.wings.session.PropertyService#getProperties()
     */
    public synchronized Object getProperty(String key, Object def) {
        Object value = props.get(key);
        if (value == null)
            value = def;
        return value;
    }

    /**
     * Sets the session property indicated by the specified key.
     *
     * @param      key   the name of the session property.
     * @param      value the value of the session property.
     * @return     the previous value of the session property,
     *             or <code>null</code> if it did not have one.
     * @see        org.wings.session.PropertyService#getProperty(java.lang.String)
     * @see        org.wings.session.PropertyService#getProperty(java.lang.String, java.lang.String)
     */
    public synchronized Object setProperty(String key, Object value) {
        //System.err.print("DefaultSession.setProperty");
        Object old = props.put(key, value);
        propertyChangeSupport.firePropertyChange(key, old, value);
        return old;
    }

    private final WeakPropertyChangeSupport propertyChangeSupport = new WeakPropertyChangeSupport(this);

    /**
     * TODO: documentation
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * TODO: documentation
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, 
                                          PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName,listener);
    }

    public void removePropertyChangeListener(String propertyName, 
                                             PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName,listener);
    }
    
    private Locale locale = Locale.getDefault();

    /**
     * TODO: documentation
     *
     * @param l
     */
    public void setLocale(Locale l) {
        if ( l==null || locale.equals(l) )
            return;
        locale = l;
        propertyChangeSupport.firePropertyChange(LOCALE_PROPERTY, locale, l);
    }

    /**
     * TODO: documentation
     */
    public final Locale getLocale() {
        return locale;
    }

    private final synchronized int getUniqueId() {
        return uniqueIdCounter++;
    }

    public final String createUniqueId() {
        return StringUtil.toShortestAlphaNumericString(getUniqueId());
    }

    public final int getMaxContentLength() {
        return maxContentLength;
    }
    public final void setMaxContentLength(int l) {
        maxContentLength = l;
    }

    protected void destroy() {
        Iterator it = frames.iterator();
        while (it.hasNext()) {
            SContainer container = ((SFrame)it.next()).getContentPane();
            if (container != null)
                container.removeAll();
        }

        extManager = null;
        reloadManager = null;
        cgManager = null;
    }

    private String redirectAddress;

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
     * <code>exit()</code> method, especially, if it is an application 
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
    public void exit(String redirectAddress) {
        this.redirectAddress = redirectAddress;
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
     * <code>exit()</code> method, especially, if it is an application 
     * that requires an login and thus handles sensitive information accessible
     * through the session. Usually, you will call this on behalf of an 
     * event within an <code>ActionListener.actionPerformed()</code> like for 
     * a pressed 'EXIT'-Button.
     */
    public void exit() { exit(""); }

    String getRedirectAddress() {
        return redirectAddress;
    }

    /**
     * TODO: documentation
     *
     * @param listener
     */
    public void addRequestListener(SRequestListener listener) {
        if ( requestListener==null ) {
            requestListener = new LinkedList();
        }
        requestListener.add(listener);
    }

    /**
     * TODO: documentation
     *
     * @param listener
     */
    public void removeRequestListener(SRequestListener listener) {
        if ( requestListener==null ) {
            return;
        }
        requestListener.remove(listener);
    }

    /**
     * Fire an RequestEvent at each registered listener.
     */
    final void fireRequestEvent(int type) {
        fireRequestEvent(type, null);
    }

    /**
     * Fire an RequestEvent at each registered listener.
     */
    final void fireRequestEvent(int type, ExternalizedResource resource) {
        if ( requestListener==null || requestListener.size()<=0 )
            return;

        SRequestEvent event = new SRequestEvent(this, type, resource);
        for ( Iterator iter=requestListener.iterator(); iter.hasNext(); ) {
            ((SRequestListener)iter.next()).processRequest(event);
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
