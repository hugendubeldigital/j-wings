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

public abstract class DynamicResource
{
    /**
     *
     */
    protected String extension;

    /**
     *
     */
    protected String mimeType;

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
    private RequestURL requestURL;

    /**
     *
     */
    private String id;

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
        this.frame = frame;
        this.extension = extension;
        this.mimeType = mimeType;

        // nur Id, ohne session Encoding!
        id = frame.getExternalizeManager().getId(frame.getExternalizeManager().externalize(this));
        System.err.println("Externalize DynamicResource " + id);

    }

    /**
     *
     */
    public final SFrame getFrame() {
        return frame;
    }

    /**
     *
     */
    public final void invalidate() {
        epochCache = StringUtil.toShortestAlphaNumericString(++epoch);
    }
    
    /**
     *
     */
    public final String getId() {
        return id;
    }

    /**
     * returns the file extension of the given object. Some (old) browsers use
     * this information instead of the mime type
     */
    public final String getExtension() {
        return extension;
    }

    /**
     * returns the mime type of the given object
     */
    public final String getMimeType() {
        return mimeType;
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
        return mimeType + " " + getEpoch();
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
    public void setRequestURL(RequestURL r) {
        requestURL = r;
    }

    /**
     *
     */
    public RequestURL getRequestURL() {
        RequestURL result =  (RequestURL)requestURL.clone();
        result.setEpoch(getEpoch());
        result.setContext(getId());
        
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
 * End:
 */
