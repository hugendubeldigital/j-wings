/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import java.util.ArrayList;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class DefaultGetDispatcher
    implements SGetDispatcher
{
    ArrayList listener = new ArrayList(2);

    long uniquePrefix = 0;

    String uniquePrefixString = "0";

    /**
     * TODO: documentation
     *
     */
    public DefaultGetDispatcher() {
    }

    /**
     * TODO: documentation
     *
     * @param l
     */
    public void register(SGetListener l) {
        if ( !listener.contains(l) )
            listener.add(l);
    }

    /**
     * TODO: documentation
     *
     * @param l
     */
    public void unregister(SGetListener l) {
        listener.remove(l);
    }

    public boolean dispatch(String name, String[] values) {
        boolean erg = false;

        // System.out.print("dispatch " + name + " : ");
        // for ( int i=0; i<values.length; i++ )
        //     System.out.print(values[i] + " , ");
        // System.out.println();

        for ( int i=0; i<listener.size(); i++ ) {
            SGetListener l = (SGetListener)listener.get(i);
            if ( name.startsWith(l.getNamePrefix()) ) {
                for ( int j=0; j<values.length; j++ )
                    l.getPerformed(name, values[j]);
                erg = true;
            }
        }

        return erg;
    }

    /**
     * TODO: documentation
     *
     */
    public void dispatchDone() {
        uniquePrefix++;
        uniquePrefixString = Long.toString(uniquePrefix);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getUniquePrefix() {
        return uniquePrefixString;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
