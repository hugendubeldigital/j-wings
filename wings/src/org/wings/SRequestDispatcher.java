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
import java.util.logging.*;

import org.wings.session.SessionManager;
import org.wings.util.StringUtil;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public final class SRequestDispatcher
    implements java.io.Serializable
{
    // not the package name but event dispatcher subsystem:
    private final static Logger logger = Logger.getLogger("org.wings.event");

    private final HashMap listener = new HashMap();

    protected boolean namedEvents = true;

    public SRequestDispatcher() {}

    private final void addRequestListener(RequestListener gl, 
                                          String namePrefix) {
        ArrayList l = (ArrayList)listener.get(namePrefix);
        if ( l==null ) {
            l = new ArrayList(2);
            l.add(gl);
            listener.put(namePrefix, l);
        }
        else if ( !l.contains(gl) )
            l.add(gl);
    }
    
    private final void removeRequestListener(RequestListener gl,
                                             String namePrefix) {
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
     * HashMap as key. The value is a Set (ArrayList) of 
     * {@link RequestListener}s.
     *
     * @param gl listener
     */
    public void register(RequestListener gl) {
        if ( gl==null )
            return;

        String key = gl.getNamePrefix();
        key = key.substring(key.indexOf(SConstants.UID_DIVIDER) + 1);

        logger.finer("dispatcher: register '" + key + "'");
        addRequestListener(gl, key);

        if (namedEvents) {
            key = gl.getName();
            if ( key!=null && key.trim().length()>0 ) {
                logger.finer("dispatcher: register named '" + key +"'");
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

        logger.finer("unregister '" + key + "'");
        removeRequestListener(gl, key);

        key = gl.getName();
        if ( key!=null && key.trim().length()>0 ) {
            logger.finer("unregister named '" + key + "'");
            removeRequestListener(gl, key);
        }

    }

    /**
     * dispatch the events, encoded as [name/(multiple)values]
     * in the HTTP request.
     */
    public boolean dispatch(String name, String[] values) {
        boolean erg = false;

        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        String epoch = null;

        // kein Alias
        if (dividerIndex > 0) {
            epoch = name.substring(0, dividerIndex);
            name = name.substring(dividerIndex + 1);

            if (logger.isLoggable(Level.FINER)) {
                StringBuffer buffer = new StringBuffer("dispatch ");
                buffer.append(epoch);
                buffer.append(SConstants.UID_DIVIDER);
                buffer.append(name);
                buffer.append(": ");
                buffer.append(StringUtil.delimitedString(values));
                logger.finer(buffer.toString());
            }
        }

        // make ImageButtons work in Forms .. browsers return
        // the click position as .x and .y suffix of the name
        if (name.endsWith(".x") || name.endsWith(".X"))
            name = name.substring(0, name.length()-2);

        ArrayList l = (ArrayList)listener.get(name);
        if (l != null && l.size()>0 ) {
            logger.finer("process event '" + epoch + "_" + name + "'");
            for (int i=0; i<l.size(); i++) {
                RequestListener gl = (RequestListener)l.get(i);
                if ( checkEpoch(epoch, name, gl) ) {
                    gl.processRequest(name, values);
                    erg = true;
                }
            }
        }
        return erg;
    }

    protected boolean checkEpoch(String epoch, String name,RequestListener gl){
        if (epoch != null) {
            SFrame frame = ((SComponent)gl).getParentFrame();
            if ( frame==null ) {
                if (logger.isLoggable(Level.FINE))
                    logger.fine("request for dangling component '" + epoch +
                                "_" + name);
                unregister(gl);
                return false;
            } 
            if (!epoch.equals(frame.getEventEpoch())) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("### got outdated event '" + epoch + "_" + name
                                + "' from frame '"
                                + frame.getUnifiedId() 
                                + "'; expected epoch: " 
                                + frame.getEventEpoch());
                }
                return false;
            }
        }
        return true;
    }

    protected void finalize() {
        logger.fine(SRequestDispatcher.class.getName() + ".finalize");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
