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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Locale;

import javax.servlet.*;

import org.wings.DefaultReloadManager;
import org.wings.ReloadManager;
import org.wings.SRequestDispatcher;
import org.wings.plaf.CGManager;
import org.wings.plaf.SuffixManager;
import org.wings.util.WeakPropertyChangeSupport;
import org.wings.util.StringUtil;
import org.wings.externalizer.ExternalizeManager;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DefaultSession
    implements Session, PropertyService
{
    private ServletContext servletContext;
    private final Map services = new HashMap();
    private final CGManager cgManager = new CGManager();
    private final SuffixManager suffixManager = new SuffixManager();
    private ReloadManager reloadManager = null;
    private ExternalizeManager extManager = null;
    private final SRequestDispatcher dispatcher = new SRequestDispatcher();
    private Map props = new HashMap();

    private int uniqueIdCounter = 1;

    /**
     * TODO: documentation
     *
     */
    public DefaultSession() {
        services.put(PropertyService.class, this);
    }

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
    }

    /**
     * Copy the init parameters. They are accessible by means of
     * the PropertyService.
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

    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Add a Service to the list of available services and associate it
     * with the specified key. The service's interface is a good choice
     * for the key.
     *
     * @param key the key
     * @param service the Service
     */
    public void putService(Object key, Service service) {
        services.put(key, service);
    }

    /**
     * Get a Service by key.
     * with the specified key.
     *
     * @param key the key
     * @param service the Service
     */
    public Service getService(Object key) {
        return (Service)services.get(key);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public CGManager getCGManager() {
        return cgManager;
    }

    /**
     * TODO: documentation
     *
     */
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
    public SRequestDispatcher getDispatcher() {
        return dispatcher;
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
    public void setExternalizeManager(ExternalizeManager em) {
        extManager = em;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SuffixManager getSuffixManager() {
        return suffixManager;
    }

    /**
     * Gets the session property indicated by the specified key.
     *
     * @param      key   the name of the session property.
     * @return     the string value of the session property,
     *             or <code>null</code> if there is no property with that key.
     * @see        org.wings.session.PropertyService#getProperties()
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
    public Object getProperty(String key, Object def) {
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
    public Object setProperty(String key, Object value) {
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

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
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

    /**
     * Get char set depending on a locale (i. e. iso-8859-1).
     *
     * @param locale Locale
     * @return Char set depending on the locale.
     */
    static public String charSetOf(Locale locale) {
        final String language = locale.getLanguage();

        if(language.equals("pl"))
            return "iso-8859-2";

        return "iso-8859-1";
    }

    /**
     * Get char set of this session (i. e. iso-8859-1).
     *
     * @return Char set of this session
     */
    public String getCharSet() {
        return charSetOf(getLocale());
    }

    private final synchronized int getUniqueId() {
        return uniqueIdCounter++;
    }

    public final String createUniqueId() {
        return StringUtil.toShortestAlphaNumericString(getUniqueId());
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
