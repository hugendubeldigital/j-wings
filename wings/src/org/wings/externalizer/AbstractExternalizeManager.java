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
 * TODO: documentation
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
     *
     */
    public static final String notFoundIdentifier = "0";

    // Flags

    /**
     *
     */
    public static final int FINAL = 8;

    /**
     *
     */
    public static final int REQUEST = 1;

    /**
     *
     */
    public static final int SESSION = 2;

    /**
     *
     */
    public static final int GLOBAL = 4;

    /**
     * TODO: documentation
     *
     */
    private long counter = 1;

    /**
     * TODO: documentation
     */
    protected final Map reverseExternalized = Collections.synchronizedMap( new HashMap() );

    /**
     * TODO: documentation
     */
    protected String sessionEncoding = "";

    /**
     * TODO: documentation
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
     * TODO: documentation
     *
     */
    protected abstract void storeExternalizedInfo(String identifier,
                                                  ExternalizedInfo extInfo);

    /**
     * TODO: documentation
     *
     */
    public abstract ExternalizedInfo getExternalizedInfo(String identifier);

    /**
     * TODO: documentation
     *
     */
    protected abstract void removeExternalizedInfo(String identifier);

    /**
     * TODO: documentation
     *
     * @return
     * @throws java.io.IOException
     */
    public String externalize(Object obj, Externalizer externalizer)
    {
        return externalize(obj, externalizer, SESSION);
    }

    /**
     * TODO: documentation
     *
     * @return
     * @throws java.io.IOException
     */
    public String externalize(Object obj, Externalizer externalizer, Set headers)
    {
        return externalize(obj, externalizer, headers, SESSION);
    }

    /**
     * TODO: documentation
     *
     * @return
     * @throws java.io.IOException
     */
    public String externalize(Object obj, Externalizer externalizer, int flags)
    {
        if ( obj == null || externalizer == null )
            throw new IllegalStateException("no externalizer");

        return externalize(obj, externalizer, externalizer.getMimeType(obj),
                           null, flags);
    }

    /**
     * TODO: documentation
     *
     * @return
     * @throws java.io.IOException
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
     * TODO: documentation
     *
     * @return
     * @throws java.io.IOException
     */
    public String externalize(Object obj, Externalizer externalizer, 
                              String mimeType)
    {
        return externalize(obj, externalizer, mimeType, null, SESSION);
    }

    /**
     * TODO: documentation
     *
     * @return
     * @throws java.io.IOException
     */
    public String externalize(Object obj, Externalizer externalizer, 
                              String mimeType, Set headers)
    {
        return externalize(obj, externalizer, mimeType, headers, SESSION);
    }

    /**
     * TODO: documentation
     *
     * @return
     * @throws java.io.IOException
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
            // session encoding is not available in SysExtMan, so add it here
            return SystemExternalizeManager.getSharedInstance().externalize(extInfo) + 
                sessionEncoding;
        } else {
            return externalize(extInfo);
        }
                               
    }


    /**
     * TODO: documentation
     *
     * @return
     * @throws java.io.IOException
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
     * 
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
            response.setDateHeader("expires", 12*60*60); // 12 h
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
