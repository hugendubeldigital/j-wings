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

import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.wings.session.SessionManager;

/**
 * PENDING (dynamic resources) the reloadmanager shou
 * This implementation of a {@link SRequestDispatcher} saves the listeners
 * in a HashMap and is therefore much faster than the
 * {@link DefaultGetDispatcher}.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public final class SRequestDispatcher
{
    public static final boolean DEBUG = false;

    private final HashMap listener = new HashMap();

    protected boolean namedEvents = true;

    /**
     * creates a new fast dispatcher
     */
    public SRequestDispatcher() {
    }

    private final void addRequestListener(RequestListener gl, String namePrefix) {
        ArrayList l = (ArrayList)listener.get(namePrefix);
        if ( l==null ) {
            l = new ArrayList(2);
            l.add(gl);
            listener.put(namePrefix, l);
        }
        else if ( !l.contains(gl) )
            l.add(gl);
    }
    
    private final void removeRequestListener(RequestListener gl, String namePrefix) {
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

    /**
     * Registers a listener. The NamePrefix of the listener is stored in the
     * HashMap as key. The value is a Set (ArrayList) of {@link RequestListener}.
     *
     * @param gl listener
     */
    public void register(RequestListener gl) {
        if ( gl==null )
            return;

        String key = gl.getNamePrefix();
        key = key.substring(key.indexOf(SConstants.UID_DIVIDER)+1);

        debug("register " + key);
        addRequestListener(gl, key);

        if ( namedEvents ) {
            key = gl.getName();
            if ( key!=null && key.trim().length()>0 ) {
                debug("register " + key);
                addRequestListener(gl, key);
            }
        }
    }

    /**
     * TODO: documentation
     *
     * TODO:
     * This should remove the GetListener from the HashMap, not the Names of
     * the GetListener (Names may change)
     *
     * @param gl
     */
    public void unregister(RequestListener gl) {
        if ( gl==null )
            return;

        String key = gl.getNamePrefix();
        key = key.substring(key.indexOf(SConstants.UID_DIVIDER)+1);

        debug("unregister " + key);
        removeRequestListener(gl, key);

        key = gl.getName();
        if ( key!=null && key.trim().length()>0 ) {
            debug("unregister " + key);
            removeRequestListener(gl, key);
        }

    }

    public boolean dispatch(String name, String[] values) {
        boolean erg = false;

        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        String prefix = null;

        // kein Alias
        if (dividerIndex > 0) {
            prefix = name.substring(0, dividerIndex);
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
        if (l != null) {
            RequestListener gl = (RequestListener)l.get(0);
            SFrame frame = ((SComponent)gl).getParentFrame();
            if (prefix != null && !prefix.equals(frame.getUniquePrefix())) {
                debug("parameter '" + name + "' is out of date");
                // do not fire those outdated events but enforce immediate reload of the frame
                System.err.println("outdated events from frame " + frame.getTitle());
                //                SessionManager.getSession().getReloadManager().markDirty(frame);
                // hack! We claim, that we have fired events although we haven't, because we want
                // send the reload manager frame, not the frameset
                return true;
            }

            for (int i=0; i<l.size(); i++) {
                gl = (RequestListener)l.get(i);
                gl.processRequest(name, values);
                erg = true;
            }
        }
        return erg;
    }

    protected void finalize() {
        debug("finalize");
    }

    private static final void debug(String mesg) {
        if ( DEBUG ) {
            System.out.println("[" + SRequestDispatcher.class.getName() + "] " + mesg);
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
