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

package org.wings;

import java.io.IOException;
import java.util.Set;

import org.wings.SFrame;
import org.wings.io.Device;
import org.wings.util.StringUtil;
import org.wings.session.SessionManager;

/**
 * Dynamic Resources are web resources representing rendered components 
 * and are individually loaded by Browsers as different 'files'. 
 * Dynamic Resources include therefore frames, cascading stylesheets or 
 * script files.
 * The resources may change in the consequence of some internal change of
 * the components. This invalidation process yields a new 'version', called
 * epoch here.
 */
public abstract class DynamicResource
    extends Resource
{
    /**
     *
     */
    private int epoch = 0;

    /**
     *
     */
    private String epochCache = StringUtil.toShortestAlphaNumericString(epoch);

    /**
     *
     */
    private SFrame frame;

    /**
     *
     */
    public DynamicResource(SFrame frame) {
      this(frame, "", "");
    }

    /**
     *
     */
    public DynamicResource(SFrame frame, String extension, String mimeType) {
        super(extension, mimeType);
        this.frame = frame;

        System.err.println("Externalize DynamicResource " + getId());
    }

    /**
     *
     */
    public final SFrame getFrame() {
        return frame;
    }

    /**
     * Mark this dynamic resource as to be re-rendered. This method is
     * called, whenever any change took place in the frame, so that this
     * dynamic resource is to be externalized with a new version-number.
     */
    public final void invalidate() {
        epochCache = StringUtil.toShortestAlphaNumericString(++epoch);
        if (org.wings.servlet.SessionServlet.DEBUG) {
            String name = getClass().getName();
            name = name.substring(name.lastIndexOf(".") + 1);
            System.err.println("[" + name + "] " +
                               "invalidate - epoch: " + epochCache);
        }
    }

    /**
     *
     */
    public final String getEpoch() {
        return epochCache;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        return getId() + " " + getEpoch();
    }


    /**
      * Get additional http-headers.
      * Returns <tt>null</tt>, if there are no additional headers to be set.
      * @return Set of {@link java.util.Map.Entry} (key-value pairs)
      * @param obj get headers for this object
      */
    public Set getHeaders() {
        return null;
    }

    /**
      * Get additional http-headers.
      * Returns <tt>null</tt>, if there are no additional headers to be set.
      * @return Set of {@link java.util.Map.Entry} (key-value pairs)
      * @param obj get headers for this object
      */
    public Set getCookies() {
        return null;
    }

    /**
     *
     */
    public RequestURL getRequestURL() {
        RequestURL result = super.getRequestURL();
        result.setEpoch(getEpoch());
        return result;
    }

    /**
     *
     */
    public abstract void write(Device out) throws IOException;
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
