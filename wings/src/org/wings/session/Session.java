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
import java.io.Serializable;
import java.util.Locale;

import javax.servlet.*;

import org.wings.SRequestDispatcher;
import org.wings.ReloadManager;
import org.wings.plaf.CGManager;
import org.wings.plaf.SuffixManager;
import org.wings.externalizer.ExternalizeManager;

/**
 * A Session contains information which is associated with one
 * user session. More doc needed.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public interface Session
    extends Serializable
{
    String LOCALE_PROPERTY = "locale";
    String LOOK_AND_FEEL_PROPERTY = "lookAndFeel";

    /**
     * used by servlet session to initialize session
     */
    void init(ServletConfig config);

    // do we really need this???
    void setExternalizeManager(ExternalizeManager em);
    ExternalizeManager getExternalizeManager();

    void setReloadManager(ReloadManager reloadManager);
    ReloadManager getReloadManager();

    CGManager getCGManager();
    SuffixManager getSuffixManager();

    /**
     * Retrieve the ServletContext.
     * @return the servlet context
     */
    ServletContext getServletContext();

    void putService(Object key, Service service);
    Service getService(Object key);

    SRequestDispatcher getDispatcher();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String propertyName,
                                   PropertyChangeListener listener);

    void removePropertyChangeListener(String propertyName,
                                      PropertyChangeListener listener);

    void setLocale(Locale l);

    Locale getLocale();

    /**
     * Get char set of this session (i. e. iso-8859-1).
     *
     * @return Char set of this session
     */
    String getCharSet();

    /**
     * create a id unique to the session
     */
    String createUniqueId();

    /**
     * The maximal length of data that is accepted in one POST request in
     * kilobytes.
     * Data can be this big, if your application provides a capability
     * to upload a file (SFileChooser). This constant limits the maximum
     * size that is accepted to avoid denial of service attacks.
     *
     * @return the maximum content length that is accepted by a POST
     *         request in this session in <em>kilobytes</em>
     */
    int getMaxContentLength();

    /**
     * Set the maximum content length, that is handled by a POST request.
     * Usual posted content is not that big, but file-contents may be huge,
     * so limitation is necessary. This limits the maximum size of the
     * content to be posted (Note, that this is not equal the maximum
     * file size, since the posted content contains as well headers and other
     * data; so the maximum filesize to be transmitted is usually smaller
     * than the limit given here).
     * 
     * @param cLength the maximum content lengh in <em>kilobytes</em>
     */
    void setMaxContentLength(int cLength);
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */





