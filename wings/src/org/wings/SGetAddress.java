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
import org.wings.io.Device;

/**
 * This class handles a HTTP GET Address that can be updated
 * with additional GET parameters.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SGetAddress
    implements Cloneable
{
    private static final byte[] _ampStr    = "&amp;".getBytes();
    private static final byte[] _questMark = "?".getBytes();

    private String absoluteAddress;

    /*
     * these values are derived from the absolute address
     * and are cached for performance (for output).
     */
    private String contextAddress;
    private byte[] contextAddressByte;
    private boolean hasQuestMark;

    private StringBuffer parameters = null;

    /**
     * creates an empty SGetAddress
     */
    public SGetAddress() { 
        this("");
    }

    /**
     * Creates a SGetAddress with the given absolute URL
     * as base. This could be something like 'http://my.domain.org/blub'.
     * It may already contain parameters, appended after '?'.
     *
     * @param absoulte base address.
     */
    public SGetAddress(String absolute) {
        setAbsoluteAddress(absolute);
    }

    /**
     * Sets the absolute base address of this SGetAddress.
     * This could be something like 'http://my.domain.org/blub'.
     * It may already contain parameters, appended after '?'.
     *
     * @param addr
     */
    public void setAbsoluteAddress(String addr) {
        absoluteAddress = addr;
        contextAddress = null;
        contextAddressByte = null;
    }

    /**
     * Returns the base URL of this SGetAddress.
     * This URL may contain get parameters that were given in 
     * the constructor or the {@link #setAbsoluteAddress()} call.
     *
     * @return String containing the base address.
     */
    public String getAbsoluteAddress() {
        return absoluteAddress;
    }


    /**
     * returns the relative address of this GET-address.
     * For the absoulte address 'http://www.domain.org/wingset/WingSet/?blub',
     * the relative address would be '/wingset/WingSet?blub'.
     * The part returned contains only get paramters that were given in 
     * the constructor or the {@link #setAbsoluteAddress()} call.
     *
     * @return String containing the relative address without host part.
     */
    public String getRelativeAddress() {
        int pos = absoluteAddress.indexOf('/', 8); // after "http://"
        if (pos == -1)
            pos = 0;  // we already got an relative address without host.
        return absoluteAddress.substring(pos);
    }
    
    /**
     * returns the context URL part of this address. This is the part after the
     * last slash. The part returned contains only get paramters that were given in 
     * the constructor or the {@link #setAbsoluteAddress()} call.
     * For the absoulte address 'http://www.domain.org/wingset/WingSet/foo.gif?blub',
     * the relative address would be 'foo.gif?blub'.
     *
     * @return the context URL.
     */
    public String getContextURL() { 
        if (contextAddress == null) {
            int pos = absoluteAddress.lastIndexOf('/') + 1;
            contextAddress     = absoluteAddress.substring(pos);
            contextAddressByte = contextAddress.getBytes();
            hasQuestMark = (contextAddress.indexOf ('?') >= 0);
        }
        return contextAddress;
    }

    /**
     * Add an additional parameter to be included in the GET paramter
     * list. Usually, this paramter will be in the form 'name=value'.
     *
     * @param parameter to be included in the GET parameters.
     * @return this SGetAddress that simplifies 'chaining'
     */
    public SGetAddress addParameter(String parameter) {
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
        if (contextAddressByte == null) {
            getContextURL();
        }
        d.write(contextAddressByte);

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
        StringBuffer erg = new StringBuffer(getContextURL());

        if (parameters != null && parameters.length() > 0) {
            erg.append(hasQuestMark ? "&amp;" : "?");
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
        SGetAddress erg = new SGetAddress(absoluteAddress);

        if (parameters != null)
            erg.parameters = new StringBuffer(parameters.toString());
        
        // pass computed cache
        erg.contextAddress     = contextAddress;
        erg.contextAddressByte = contextAddressByte;
        erg.hasQuestMark       = hasQuestMark;

        return erg;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
