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

import java.io.*;
import org.wings.io.Device;

/**
 * This class handles a HTTP GET Address that can be updated
 * with additional GET parameters.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class RequestURL
    implements Cloneable, Serializable
{
    private static final byte[] _delimiter = "_".getBytes();
    private static final byte[] _ampStr    = "&amp;".getBytes();
    private static final byte[] _questMark = "?".getBytes();

    private String baseURL;

    private String baseParameters;

    private boolean hasQuestMark;

    private String epoch;

    private String resource;

    private StringBuffer parameters = null;

    /**
     * 
     */
    public RequestURL() { 
    }

    /**
     * 
     */
    public RequestURL(String baseURL, String encodedBaseURL) { 
        setBaseURL(baseURL, encodedBaseURL);
    }

    /**
     *
     */
    public void setEpoch(String e) {
        epoch = e;
    }

    /**
     *
     */
    public String getEpoch() {
        return epoch;
    }

    /**
     *
     */
    public void setResource(String r) {
        resource = r;
    }

    /**
     *
     */
    public String getResource() {
        return resource;
    }

    /**
     *
     */
    public void setBaseURL(String b, String encoded) {
        baseURL = b;

        baseParameters = encoded.substring(b.length());
        if ( baseParameters.length()==0 )
            baseParameters = null;

        if ( baseParameters!=null )
            hasQuestMark = baseParameters.indexOf('?')>=0;
        else
            hasQuestMark = false;
    }


    /**
     * Add an additional parameter to be included in the GET paramter
     * list. Usually, this paramter will be in the form 'name=value'.
     *
     * @param parameter to be included in the GET parameters.
     * @return a reference to <code>this</code> to simplify 'call chaining'
     */
    public RequestURL addParameter(String parameter) {
        if (parameter!=null) {
            if (parameters == null) 
                parameters = new StringBuffer();
            else
                parameters.append("&amp;");
            parameters.append(parameter);
        }
        return this;
    }

    /**
     * clear all additional paramters given in the {@link #addParameter()} call.
     */
    public void clear() {
        if (parameters != null) {
            parameters.setLength(0);
        }
        setEpoch(null);
        setResource(null);
    }

    /**
     * Writes the context Address to the output Device. Appends all
     * parameters given. Only the context URL is given, since all GET urls generated
     * by wings are relative to the WingS servlet.
     * Tries to avoid charset conversion as much as possible by precalculating the
     * byteArray representation of the non-parameter part.
     *
     * @param d the Device to write to 
     */
    public void write(Device d) throws IOException {
        if ( baseURL!=null ) {
            d.print(baseURL);
        }

        if (resource != null && epoch != null) {
            d.print(epoch);
            d.write(_delimiter);
        }

        if (resource != null) {
            d.print(resource);
        }

        if ( baseParameters!=null ) {
            d.print(baseParameters);
        }

        if (parameters != null && parameters.length() > 0) {
            d.write (hasQuestMark ? _ampStr : _questMark);
            d.print(parameters.toString());
        }
    }

    /**
     * Returns the string representation of the context URL plus
     * all paramters given.
     *
     * @return
     */
    public String toString() {
        StringBuffer erg = new StringBuffer();

        if ( baseURL!=null ) {
            erg.append(baseURL);
        }

        if ( resource!=null && epoch!=null ) {
            erg.append(epoch);
            erg.append("_");
        }

        if ( resource!=null ) {
            erg.append(resource);
        }

        if ( baseParameters!=null ) {
            erg.append(baseParameters);
        }

        if (parameters != null && parameters.length() > 0) {
            erg.append(hasQuestMark ? "&" : "?");
            erg.append(parameters.toString());
        }

        return erg.toString();
    }

    /**
     * Deep copy.
     * @return object with cloned contents
     */
    public Object clone()
    {
        try {
            return super.clone();
        } catch ( CloneNotSupportedException ex ) {
            return new RequestURL();
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