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

package org.wings.session;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.wings.LowLevelEventListener;
import org.wings.SConstants;
import org.wings.SFrame;
import org.wings.SComponent;
import org.wings.util.StringUtil;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public final class LowLevelEventDispatcher
    implements java.io.Serializable
{
    // not the package name but event dispatcher subsystem:
    private final static Logger logger = Logger.getLogger("org.wings.event");

    private final HashMap listener = new HashMap();

    protected boolean namedEvents = true;

    public LowLevelEventDispatcher() {}

    public final void addLowLevelEventListener(LowLevelEventListener gl, 
                                               String eventId) {
        List l = (List)listener.get(eventId);
        if ( l==null ) {
            l = new ArrayList(2);
            l.add(gl);
            listener.put(eventId, l);
        }
        else if ( !l.contains(gl) )
            l.add(gl);
    }
    
    public final void removeLowLevelEventListener(LowLevelEventListener gl,
                                                  String eventId) {
        List l = (List)listener.get(eventId);
        if ( l!=null ) {
            l.remove(gl);
            if ( l.size()==0 )
                listener.remove(eventId);
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
    public void register(LowLevelEventListener gl) {
        if ( gl==null )
            return;

        String key = gl.getLowLevelEventId();

        logger.finer("dispatcher: register '" + key + "' type: " + gl.getClass());
        addLowLevelEventListener(gl, key);

        if (namedEvents) {
            key = gl.getName();
            if ( key!=null && key.trim().length()>0 ) {
                logger.finer("dispatcher: register named '" + key +"'");
                addLowLevelEventListener(gl, key);
            }
        }
    }

    /**
     * TODO:
     * This should remove the GetListener from the HashMap, not the Names of
     * the GetListener (Names may change)
     *
     * @param gl
     */
    public void unregister(LowLevelEventListener gl) {
        if ( gl==null )
            return;

        String key = gl.getLowLevelEventId();

        logger.finer("unregister '" + key + "'");
        removeLowLevelEventListener(gl, key);

        key = gl.getName();
        if ( key!=null && key.trim().length()>0 ) {
            logger.finer("unregister named '" + key + "'");
            removeLowLevelEventListener(gl, key);
        }

    }

    /**
     * dispatch the events, encoded as [name/(multiple)values]
     * in the HTTP request.
     */
    public boolean dispatch(String name, String[] values) {
        if (logger.isLoggable(Level.FINER))
            logger.finer("dispatch " + name + " = " + Arrays.asList(values));

        boolean result = false;
        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        String epoch = null;

        // no Alias
        if (dividerIndex > 0) {
            epoch = name.substring(0, dividerIndex);
            name = name.substring(dividerIndex + 1);
        }

        // make ImageButtons work in Forms .. browsers return
        // the click position as .x and .y suffix of the name
        if (name.endsWith(".x") || name.endsWith(".X")) {
            name = name.substring(0, name.length()-2);
        }
        else if (name.endsWith(".y") || name.endsWith(".Y")) {
            // .. but don't process the same event twice.
            logger.finer("discard '.y' part of image event");
            return false;
        }
        
        // is value encoded in name ? Then append it to the values we have.
        int p = name.indexOf(SConstants.UID_DIVIDER);
        if (p > -1) {
            String v = name.substring(p+1);
            name = name.substring(0, p);
            String[] va = new String[values.length+1];
            System.arraycopy(values, 0, va, 0, values.length);
            va[values.length] = v;
            values = va;
        }
        
        List l = (List)listener.get(name);
        if (l != null && l.size() > 0 ) {
            logger.fine("process event '" + epoch + "_" + name + "'");
            for (int i=0; i < l.size(); ++i) {
                LowLevelEventListener gl = (LowLevelEventListener)l.get(i);
                if ( gl.isEnabled() ) {
                    if ( checkEpoch(epoch, name, gl) ) {
                        logger.fine("process event '" + name + "' by " +
                                    gl.getClass() + "(" + gl.getLowLevelEventId() +
                                    ")" );
                        gl.processLowLevelEvent(name, values);
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    protected boolean checkEpoch(String epoch, String name, 
                                 LowLevelEventListener gl){
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
                                + frame.getName()
                                + "'; expected epoch: " 
                                + frame.getEventEpoch());
                }
                return false;
            }
        }
        return true;
    }

    void clear() {
        listener.clear();
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
