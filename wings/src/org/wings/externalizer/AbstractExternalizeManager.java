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

package org.wings.externalizer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import java.util.logging.*;
import org.wings.RequestURL;
import org.wings.util.StringUtil;

/**
 * 
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class AbstractExternalizeManager
{
    protected static Logger logger = Logger.getLogger("org.wings.externalizer");

    /**
     * The identifier generated, if the {@link ExternalizeManager} did not find
     * an apropriate {@link Externalizer}. 
     */
    public static final String NOT_FOUND_IDENTIFIER = "0";

    
    /*---------------------------------------------------------------
     * The externalized ID is just a counter start starts with zero. This
     * happens with each start of the server, and thus generates the same
     * ID if we restart the application (especially, if we are in the 
     * development phase). Since we externalize the resource with a long
     * caching timeout, the browser might not refetch a resource externalized
     * in a fresh instance of the web-application, since the browser has cached
     * it already.
     * Thus, we need a unique prefix for each externalized resource, that
     * changes with each start of the server. 
     * These static variables create a new ID every UNIQUE_TIMESLICE, which
     * means, that, if we use a 2-character prefix, can offer the browser
     * the timeframe of FINAL_EXPIRES for this resource to be cached (since
     * after that time, we have an roll-over of the ID's).
     *----------------------------------------------------------------*/

    /**
     * in seconds
     */
    public static final int UNIQUE_TIMESLICE = 20;

    /**
     * in seconds; Computed from UNIQUE_TIMESLICE; do not change.
     */
    public static final long FINAL_EXPIRES = 
        (StringUtil.MAX_RADIX*StringUtil.MAX_RADIX - 1) * UNIQUE_TIMESLICE;

    /**
     * Prefix for the externalized ID; long. Computed, do not change.
     */
    protected static final long PREFIX_TIMESLICE = 
        ((System.currentTimeMillis()/1000)%FINAL_EXPIRES)/UNIQUE_TIMESLICE;

    /**
     * String prefix for externalized ID as String. Computed, do not change.
     */
    protected static final String PREFIX_TIMESLICE_STRING = 
        StringUtil.toShortestAlphaNumericString(PREFIX_TIMESLICE, 2);

    static {
        logger.info("final scope expires in " + FINAL_EXPIRES + " seconds");
        logger.info("use prefix " + PREFIX_TIMESLICE_STRING);
    }
    
    // Flags

    /**
     * for an externalized object with the final flag on the expired date
     * header is set to a big value. If the final flag is off, the browser
     * or proxy does not cache the object.
     */
    public static final int FINAL = 8;

    /**
     * for an externalized object with the request flag on, the externalized
     * object is removed from the {@link ExternalizeManager} after one request
     * of the object. 
     */
    public static final int REQUEST = 1;

    /**
     * for an externalized object with the session flag on, the externalized
     * object only available to requests within the session which created the
     * object. The object is not accessible anymore after the session is
     * destroyed (it is garbage collected after the session is garbage 
     * collected)
     */
    public static final int SESSION = 2;

    /**
     * for an externalized object with the gobal flag on, the externalized
     * object is available to all requests. Also it is never garbage collected
     * and available over the lifecycle of the servlet container
     */
    public static final int GLOBAL = 4;

    /**
     * To generate the identifier for a externalized object.
     */
    private long counter = 0;

    /**
     * To search for an already externalized object. This performs way better
     * than search in the value set of the identifier-{@link ExternalizedInfo}
     * map.
     */
    protected final Map reverseExternalized = Collections.synchronizedMap( new HashMap() );

    /**
     * To support Session local externalizing, the {@link ExternalizeManager}
     * needs to encode the session identifier of the servlet container in the
     * URL of the externalized object. This is set in the constructor and should
     * work (I hope so) with all servlet containers.
     */
    protected String sessionEncoding = "";

    protected HttpServletResponse response;

    /**
     *
     */
    public AbstractExternalizeManager(HttpServletResponse response) {
        this.response = response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
        if (response != null)
            sessionEncoding = response.encodeURL("");
    }

    public String encodeURL(String url) {
        return response.encodeURL(url);
    }

    /**
     *
     */
    protected final synchronized long getNextIdentifier() {
        return ++counter;
    }

    /**
     *
     */
    protected String getPrefix() {
        return PREFIX_TIMESLICE_STRING;
    }
    
    /**
     *
     */
    protected final String createIdentifier() {
        return getPrefix() + StringUtil.toShortestAlphaNumericString(getNextIdentifier());
    }

    /**
     * store the {@link ExternalizedInfo} in a map. 
     * The {@link ExternalizedInfo} should later on accessible by the 
     * identifier {@link #getExternalizedInfo}, {@link #removeExternalizedInfo}
     */
    protected abstract void storeExternalizedInfo(String identifier,
                                                  ExternalizedInfo extInfo);

    /**
     * get the {@link ExternalizedInfo} by identifier. 
     * @return null, if not found!!
     */
    public abstract ExternalizedInfo getExternalizedInfo(String identifier);

    /**
     * removes the {@link ExternalizedInfo} by identifier. 
     */
    protected abstract void removeExternalizedInfo(String identifier);

    /**
     * externalizes (make a java object available for a browser) an object with
     * the given {@link Externalizer}. The object is externalized in the 
     * {@link #SESSION} scope.
     *
     * @return a URL for accessing the object relative to the base URL.
     */
    public String externalize(Object obj, Externalizer externalizer)
    {
        return externalize(obj, externalizer, SESSION);
    }

    /**
     * externalizes (make a java object available for a browser) an object with
     * the given {@link Externalizer}. If the given headers are !=null the
     * headers overwrite the headers from the {@link Externalizer}.
     * The object is externalized in the 
     * {@link #SESSION} scope.
     *
     * @return a URL for accessing the object relative to the base URL.
     */
    public String externalize(Object obj, Externalizer externalizer, Set headers)
    {
        return externalize(obj, externalizer, headers, SESSION);
    }

    /**
     * externalizes (make a java object available for a browser) an object with
     * the given {@link Externalizer}. Valid flags are (this may change, look
     * also in the static variable section)
     * <ul>
     * <li>{@link #FINAL}</li>
     * <li>{@link #REQUEST}</li>
     * <li>{@link #SESSION}</li>
     * <li>{@link #GLOBAL}</li>
     * </ul>
     *
     * @return a URL for accessing the object relative to the base URL.
     */
    public String externalize(Object obj, Externalizer externalizer, int flags)
    {
        if ( obj == null || externalizer == null )
            throw new IllegalStateException("no externalizer");

        return externalize(obj, externalizer, externalizer.getMimeType(obj),
                           null, flags);
    }

    /**
     * externalizes (make a java object available for a browser) an object with
     * the given {@link Externalizer}. If the given headers are !=null the
     * headers overwrite the headers from the {@link Externalizer}. 
     * Valid flags are (this may change, look
     * also in the static variable section)
     * <ul>
     * <li>{@link #FINAL}</li>
     * <li>{@link #REQUEST}</li>
     * <li>{@link #SESSION}</li>
     * <li>{@link #GLOBAL}</li>
     * </ul>
     *
     * @return a URL for accessing the object relative to the base URL.
     */
    public String externalize(Object obj, Externalizer externalizer, 
                              Set headers, int flags)
    {
        if (obj == null || externalizer == null)
            throw new IllegalStateException("no externalizer");

        return externalize(obj, externalizer, externalizer.getMimeType(obj),
                           headers, flags);
    }

    /**
     * externalizes (make a java object available for a browser) an object with
     * the given {@link Externalizer}. 
     * If the mimeType!=null, mimeType overwrites the mimeType of the 
     * {@link Externalizer}.
     * The object is externalized in the 
     * {@link #SESSION} scope.
     *
     * @return a URL for accessing the object relative to the base URL.
     */
    public String externalize(Object obj, Externalizer externalizer, 
                              String mimeType)
    {
        return externalize(obj, externalizer, mimeType, null, SESSION);
    }

    /**
     * externalizes (make a java object available for a browser) an object with
     * the given {@link Externalizer}. 
     * If the mimeType!=null, mimeType overwrites the mimeType of the 
     * {@link Externalizer}.
     * If the given headers are !=null the
     * headers overwrite the headers from the {@link Externalizer}. 
     *
     * @return a URL for accessing the object relative to the base URL.
     */
    public String externalize(Object obj, Externalizer externalizer, 
                              String mimeType, Set headers)
    {
        return externalize(obj, externalizer, mimeType, headers, SESSION);
    }

    /**
     * externalizes (make a java object available for a browser) an object with
     * the given {@link Externalizer}. 
     * If the mimeType!=null, mimeType overwrites the mimeType of the 
     * {@link Externalizer}.
     * If the given headers are !=null the
     * headers overwrite the headers from the {@link Externalizer}. 
     * Valid flags are (this may change, look
     * also in the static variable section)
     * <ul>
     * <li>{@link #FINAL}</li>
     * <li>{@link #REQUEST}</li>
     * <li>{@link #SESSION}</li>
     * <li>{@link #GLOBAL}</li>
     * </ul>
     *
     * @return a URL for accessing the object relative to the base URL.
     */
    public String externalize(Object obj, Externalizer externalizer, 
                              String mimeType, Set headers, int flags)
    {
        if ( externalizer == null )
            throw new IllegalStateException("no externalizer");

        if ( mimeType == null )
            mimeType = externalizer.getMimeType(obj);

        if ( headers == null )
            headers = externalizer.getHeaders(obj);

        ExternalizedInfo extInfo = new ExternalizedInfo(obj, externalizer,
                                                        mimeType, headers, flags);

        if ( (flags & GLOBAL) > 0 ) {
            // session encoding is not necessary here
            return SystemExternalizeManager.getSharedInstance().externalize(extInfo);
        } else {
            return externalize(extInfo);
        }
    }


    /**
     * externalizes (make a java object available for a browser) the object in
     * extInfo. 
     *
     * @return a URL for accessing the externalized object relative to the base URL.
     */
    public String externalize(ExternalizedInfo extInfo) {
        String identifier = (String)reverseExternalized.get(extInfo);

        if (identifier == null) {
            identifier = createIdentifier();
            
            storeExternalizedInfo(identifier, extInfo);
            reverseExternalized.put(extInfo, identifier);

            String extension = extInfo.getExtension();
            if (extension != null)
                identifier += ("." + extension);
        }

        return identifier + sessionEncoding;
    }

    /**
     * externalizes (make a java object available for a browser) the object in
     * extInfo. 
     *
     * @return a URL for accessing the externalized object relative to the base URL.
     */
    public String getId(String url)
    {
        String result = url.substring(0, url.length()-sessionEncoding.length());
        int pos = result.lastIndexOf(".");
        if (pos >= 0)
            result = result.substring(0, pos);
        return result;
    }


    /**
     * delivers a externalized object identfied with the given identifier to a
     * client.
     * It sends an error (404), if the identifier is not registered.
     */
    public void deliver(String identifier, HttpServletResponse response) 
        throws IOException 
    {
        ExternalizedInfo extInfo = getExternalizedInfo(identifier);

        if ( extInfo == null ) {
            logger.warning("identifier " + identifier + " not found");
            response.sendError(response.SC_NOT_FOUND);
            return;
        }

        if ( extInfo.deliverOnce() )
            removeExternalizedInfo(identifier);

            
        response.setContentType(extInfo.getMimeType());
        
        int resourceLen = extInfo
            .getExternalizer().getLength(extInfo.getObject());
        if ( resourceLen > 0 ) {
            response.setContentLength( resourceLen );
        }

        Set headers = extInfo.getHeaders();
        if ( headers != null ) {
            for ( Iterator it = headers.iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                response.addHeader( (String) entry.getKey(), 
                                    (String) entry.getValue());
            }
        }

        // non-transient items can be cached by the browser
        if (extInfo.isFinal() ) {
            /*
             * This would be the correct way to do it; alas, that means, that
             * for static resources, after a day or so, no caching could take
             * place, since the last modification was at the start of the
             * application server. .. have to think about it.
             */
            //response.setDateHeader("Expires", FINAL_EXPIRES + extInfo.getLastModified());
            // .. so do this for now, which is the best approximation of what
            // we want.
            response.setDateHeader("Expires", FINAL_EXPIRES + System.currentTimeMillis());
        } else {
            // expire in deep past ..
            response.setDateHeader("Expires", 1000); // 1000 instead of 0: work around IE bug.
        }

        OutputStream out = response.getOutputStream();
        extInfo.getExternalizer().write(extInfo.getObject(), out);
        out.flush();
        out.close();
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
