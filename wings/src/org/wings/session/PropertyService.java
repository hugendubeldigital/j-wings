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

import java.util.Properties;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public interface PropertyService
    extends Service
{
    /**
     * Determines the current session properties.
     * The current set of system properties for use by the
     * {@link #getProperty(String)} method is returned as a
     * <code>Properties</code> object.
     * This set of session properties always includes values
     * for the following keys:
     * <table>
     * <tr><th>Key</th>
     *     <th>Description of Associated Value</th></tr>
     * <tr><td><code>java.version</code></td>
     *     <td>Java Runtime Environment version</td></tr>
     * </table>
     *
     * @see        java.util.Properties
     */
    Properties getProperties();

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
    void setProperties(Properties props);

    /**
     * Gets the session property indicated by the specified key.
     *
     * @param      key   the name of the session property.
     * @return     the string value of the session property,
     *             or <code>null</code> if there is no property with that key.
     * @see        org.wings.session.PropertyService#getProperties()
     */
    String getProperty(String key);

    /**
     * Gets the session property indicated by the specified key.
     *
     * @param      key   the name of the session property.
     * @param      def   a default value.
     * @return     the string value of the session property,
     *             or the default value if there is no property with that key.
     * @see        org.wings.session.PropertyService#getProperties()
     */
    String getProperty(String key, String def);

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
    String setProperty(String key, String value);
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
