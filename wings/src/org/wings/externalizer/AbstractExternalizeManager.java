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

import org.wings.util.StringUtil;


/**
 * 
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class AbstractExternalizeManager
{
    /**
     * TODO: documentation
     */
    private static final boolean DEBUG = true;

    /**
     * The identifier generated, if the {@link ExternalizeManager} did not find
     * an apropriate {@link Externalizer}. 
     */
    public static final String NOT_FOUND_IDENTIFIER = "0";

    // Flags

    /**
     * for an externalized object with the final flag on the expired date header
     * is set to a big value. If the final flag is off, the browser or proxy did
     * not cache the object.
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
     * destroyed (it is garbage collected after the session is garbage collected)
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
    private long counter = 1;

    /**
     * To search for an already externalized object. This performs way better
     * than search in the value set of the identifier-{@link ExternalizedInfo} map.
     */
    protected final Map reverseExternalized = Collections.synchronizedMap( new HashMap() );

    /**
     * To support Session local externalizing, the {@link ExternalizeManager}
     * needs to encode the session identifier of the servlet container in the
     * URL of the externalized object. This is set in the constructor and should
     * work (I hope so) with all servlet containers.
     */
    protected String sessionEncoding = "";

    /**
     *
     */
    public AbstractExternalizeManager(HttpServletResponse response) {
        if ( response != null )
            sessionEncoding = response.encodeURL("");
    }

    /**
     *
     */
    protected synchronized long getNextIdentifier() {
        return counter++;
    }

    /**
     *
     */
    protected final String createIdentifier() {
        return StringUtil.toShortestAlphaNumericString(getNextIdentifier());
    }

    /**
     * store the {@link ExternalizedInfo} in a map. The {@link ExternalizedInfo}
     * should later on accessible by the identifier 
     * {@link #getExternalizedInfo}, {@link #removeExternalizedInfo}
     *
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
        if ( obj == null || externalizer == null )
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
    public String externalize(ExternalizedInfo extInfo)
    {
        String identifier = (String)reverseExternalized.get(extInfo);
        
        if ( identifier==null ) {
            identifier = createIdentifier();
            
            String extension = extInfo.getExtension();
            if ( extension!=null )
                identifier += "." + extension;
            
            storeExternalizedInfo(identifier, extInfo);
            reverseExternalized.put(extInfo, identifier);
        }

        return identifier + sessionEncoding;
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
            debug("identifier " + identifier + " not found");
            response.sendError(response.SC_NOT_FOUND);
            return;
        }

        if ( extInfo.deliverOnce() )
            removeExternalizedInfo(identifier);

            
        response.setContentType(extInfo.getMimeType());

        if ( extInfo.getExternalizer().getLength(extInfo.getObject())>0 )
            response.setContentLength(extInfo.getExternalizer().getLength(extInfo.getObject()));
        
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
            response.setDateHeader("expires", 365*24*60*60); // 1 Year
        } else {
            response.setDateHeader("expires", 0);
        }

        OutputStream out = response.getOutputStream();
        extInfo.getExternalizer().write(extInfo.getObject(), out);
        out.flush();
        out.close();
    }

    private static final void debug(String msg) {
        if (DEBUG) {
            org.wings.util.DebugUtil.printDebugMessage(ExternalizeManager.class, msg);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
