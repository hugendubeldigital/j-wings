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

import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringTokenizer;

/*
 * FastDispatcher.java
 *
 * Diese Implementation eines {@link SGetDispatcher} speichert die
 * Listener in einer HashMap und und deshalb um ein vielfaches
 * schneller, als der {@link DefaultGetDispatcher}.
 */
/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class FastDispatcher
    implements SGetDispatcher
{
    /**
     * TODO: documentation
     */
    public static final boolean DEBUG = false;

    HashMap listener = new HashMap();

    int uniquePrefix = 0;

    String uniquePrefixString = "0";

    protected boolean namedEvents = true;

    /**
     * TODO: documentation
     *
     */
    public FastDispatcher() {
    }

    private final void addGetListener(SGetListener gl, String namePrefix) {
        ArrayList l = (ArrayList)listener.get(namePrefix);
        if ( l==null ) {
            l = new ArrayList(2);
            l.add(gl);
            listener.put(namePrefix, l);
        }
        else if ( !l.contains(gl) )
            l.add(gl);
    }

    private final void removeGetListener(SGetListener gl, String namePrefix) {
        ArrayList l = (ArrayList)listener.get(namePrefix);
        if ( l!=null ) {
            l.remove(gl);
            if ( l.size()==0 )
                listener.remove(namePrefix);
        }
    }

    public final void setNamedEvents(boolean b) {
        namedEvents = b;
    }

    /*
     * Registiert einen Listener. In der HashMap wird das NamePrefix
     * des Listeners als key abgelegt. Der value ist eine Menge (ArrayList)
     * von {@link SGetListener}
     */
    /**
     * TODO: documentation
     *
     * @param gl
     */
    public void register(SGetListener gl) {
        if ( gl==null )
            return;

        String key = gl.getNamePrefix();
        key = key.substring(key.indexOf(SConstants.UID_DIVIDER)+1);

        debug("register " + key);
        addGetListener(gl, key);

        if ( namedEvents ) {
            key = gl.getName();
            if ( key!=null && key.trim().length()>0 ) {
                debug("register " + key);
                addGetListener(gl, key);
            }
        }
    }

    /**
     * TODO: documentation
     *
     * TODO:
     * This should remove the GetListener from the HashMap, not the Names of the
     * GetListener (Names may change)
     *
     * @param gl
     */
    public void unregister(SGetListener gl) {
        if ( gl==null )
            return;

        String key = gl.getNamePrefix();
        key = key.substring(key.indexOf(SConstants.UID_DIVIDER)+1);

        debug("unregister " + key);
        removeGetListener(gl, key);

        key = gl.getName();
        if ( key!=null && key.trim().length()>0 ) {
            debug("unregister " + key);
            removeGetListener(gl, key);
        }

    }

    public boolean dispatch(String name, String[] values) {
        boolean erg = false;

        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        // kein Alias
        if ( dividerIndex>0 ) {
            String prefix = name.substring(0, dividerIndex);

            try {
                long lPrefix = Long.parseLong(prefix);
                if ( lPrefix>=0 && (lPrefix<uniquePrefix ||
                                    lPrefix>uniquePrefix) ) {
                    debug("parameter " + name + " is out of time");
                    return false;
                }
            }
            catch (Exception e) {
                return false;
            }

            name = name.substring(dividerIndex+1);

            // make ImageButtons work in Forms ..
            if (name.endsWith(".x") || name.endsWith(".X"))
                name = name.substring(0, name.length()-2);

            if ( DEBUG ) {
                System.out.print("dispatch " + prefix +
                                 SConstants.UID_DIVIDER + name + " : ");
                System.out.print("prefix " + prefix + " , ");
                System.out.print("name " + name + " , ");
                for ( int i=0; i<values.length; i++ )
                    System.out.print(values[i] + " , ");
                System.out.println();
            }
        }

        ArrayList l = (ArrayList)listener.get(name);
        if ( l!=null )
            for ( int i=0; i<l.size(); i++ ) {
                SGetListener gl = (SGetListener)l.get(i);
                for ( int j=0; j<values.length; j++ )
                    gl.getPerformed(name, values[j]);
                erg = true;
            }

        return erg;
    }

    /**
     * TODO: documentation
     *
     */
    public void dispatchDone() {
        uniquePrefix++;
        if ( uniquePrefix<0 )
            uniquePrefix = 0;

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

    /**
     * TODO: documentation
     *
     */
    protected void finalize() {
        debug("finalize");
    }

    private static final void debug(String mesg) {
        if ( DEBUG ) {
            System.out.println("[" + FastDispatcher.class.getName() + "] " + mesg);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
