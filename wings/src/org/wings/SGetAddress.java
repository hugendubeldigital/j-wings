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

import org.wings.plaf.*;
import org.wings.io.Device;

/*
 * SGetAddress.java
 *
 * Diese Klasse verwaltet get Parameter.
 */
/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SGetAddress
    implements Cloneable
{
    private String baseAddress = "";

    private StringBuffer parameters = null;

    /**
     * TODO: documentation
     *
     */
    public SGetAddress() {
    }

    /**
     * TODO: documentation
     *
     * @param base
     */
    public SGetAddress(String base) {
        setBaseAddress(base);
    }

    /**
     * TODO: documentation
     *
     * @param addr
     */
    public void setBaseAddress(String addr) {
        baseAddress = addr;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getBaseAddress() {
        return baseAddress;
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
        if ( parameter!=null ) {
            if ( parameters==null )
                parameters = new StringBuffer();
            else
                parameters.append("&");
            parameters.append(parameter);
        }
        return this;
    }

    /**
     * TODO: documentation
     *
     */
    public void clear() {
        if ( parameters!=null )
            parameters.setLength(0);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        StringBuffer erg = new StringBuffer(baseAddress);

        boolean qmark = (baseAddress.indexOf ('?') >= 0);

        if ( parameters!=null && parameters.length()>0 ) {
            erg.append (qmark ? "&" : "?");
            erg.append(parameters);
        }

        return erg.toString();
    }

    /**
     * @return object with cloned contents
     */
    public Object clone()
    {
        /*  try {
         return super.clone();
         } catch(java.lang.CloneNotSupportedException e) {
         e.printStackTrace();
         return null;
         }*/

        SGetAddress erg = new SGetAddress(baseAddress);

        if (  parameters!=null )
            erg.parameters = new StringBuffer(parameters.toString());

        return erg;
    }

    /**
     * TODO: documentation
     */
    public static void main(String args[]) {
        SGetAddress adr1 = new SGetAddress("test1");
        SGetAddress adr2 = (SGetAddress)adr1.clone();
        adr2.add("geklont");
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
