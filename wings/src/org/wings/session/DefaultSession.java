/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
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

import javax.servlet.ServletConfig;

import org.wings.plaf.CGManager;
import org.wings.util.WeakPropertyChangeSupport;
import org.wings.externalizer.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DefaultSession
    implements Session, PropertyService
{
    private final Map services = new HashMap();
    private final CGManager cgManager = new CGManager();
    private ExternalizeManager extManager = null;
    private Properties props = null;

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
    }

    /**
     * TODO: documentation
     *
     * @param config
     */
    protected void initProps(ServletConfig config) {
        props = new Properties();
        Enumeration params = config.getInitParameterNames();
        while(params.hasMoreElements()) {
            String name = (String)params.nextElement();
            props.put(name, config.getInitParameter(name));
            // was: props.setProperty(name, config.getInitParameter(name));
        }
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
     * @return
     */
    public ExternalizeManager getExternalizeManager() {
        return extManager;
    }

    /**
     * TODO: documentation
     *
     */
    public void setExternalizeManager( ExternalizeManager em ) {
        extManager = em;
    }

    /**
     * Determines the current session properties.
     * The current set of session properties for use by the
     * {@link #getProperty(String)} method is returned as a
     * <code>Properties</code> object.
     * This set of session properties always includes values
     * for the following keys:
     * <table>
     * <tr><th>Key</th>
     *     <th>Associated Value</th></tr>
     * <tr><td><code>locale</code></td>
     *     <td>The current locale</td></tr>
     * <tr><td><code>lookAndFeel</code></td>
     *     <td>The current look and feel</td></tr>
     * </table>
     *
     * @see java.util.Properties
     */
    public Properties getProperties() {
        return props;
    }

    /**
     * Sets the session properties to the <code>Properties</code>
     * argument.
     * <p>
     * The argument becomes the current set of session properties for use
     * by the {@link #getProperty(String)} method. If the argument is
     * <code>null</code>, then the current set of session properties is
     * forgotten.
     *
     * @param      props   the new session properties.
     * @see        java.util.Properties
     */
    public void setProperties(Properties props) {
        this.props = props;
    }

    /**
     * Gets the session property indicated by the specified key.
     *
     * @param      key   the name of the session property.
     * @return     the string value of the session property,
     *             or <code>null</code> if there is no property with that key.
     * @see        org.wings.session.PropertyService#getProperties()
     */
    public String getProperty(String key) {
        return props.getProperty(key);
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
    public String getProperty(String key, String def) {
        return props.getProperty(key, def);
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
    public String setProperty(String key, String value) {
        String old = (String)props.setProperty(key, value);
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
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
