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
 * This class handles a HTTP GET Address with optional parameters.
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
     * TODO: documentation
     *
     */
    public SGetAddress() { 
        this("");
    }

    /**
     * TODO: documentation
     *
     * @param base
     */
    public SGetAddress(String absolute) {
        setAbsoluteAddress(absolute);
    }

    /**
     * TODO: documentation
     *
     * @param addr
     */
    public void setAbsoluteAddress(String addr) {
        absoluteAddress = addr;
        contextAddress = null;
        contextAddressByte = null;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getAbsoluteAddress() {
        return absoluteAddress;
    }

    private void breakdownAddress() {
    }

    /**
     * returns the relative address of this GET-address.
     * For the absoulte address 'http://www.domain.org/wingset/WingSet/?blub',
     * the relative address would be '/wingset/WingSet?blub'.
     *
     * @return
     */
    public String getRelativeAddress() {
        int pos = absoluteAddress.indexOf('/', 8); // after "http://"
        if (pos == -1)
            pos = 0;  // we already got an relative address without host.
        return absoluteAddress.substring(pos);
    }
    
    /**
     * returns the relative address of this GET-address.
     * For the absoulte address 'http://www.domain.org/wingset/WingSet/?blub',
     * the relative address would be '?blub'.
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
     * TODO: documentation
     *
     * @param parameter
     * @return
     */
    public SGetAddress add(String parameter) {
        return addParameter(parameter);
    }

    /**
     * TODO: documentation
     *
     * @param parameter
     * @return
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
     * removes all parameters.
     */
    public void clear() {
        if (parameters != null) {
            parameters.setLength(0);
        }
    }

    /**
     * writes the context Address to the output Device. Tries to avoid
     * charset conversion as much as possible by precalculating the
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
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        StringBuffer erg = new StringBuffer(getRelativeAddress());

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

    /**
     * TODO: documentation
     */
    public static void main(String args[]) throws Exception {
        SGetAddress adr1 = new SGetAddress("http://localhost/servlet/WingSet/?JServSessionIdroot=8eapsqkqj1");
        SGetAddress adr2 = (SGetAddress)adr1.clone();
        adr2.add("cloned=1");
        System.out.println("adr1 " + adr1);
        System.out.println("adr2 " + adr2);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
