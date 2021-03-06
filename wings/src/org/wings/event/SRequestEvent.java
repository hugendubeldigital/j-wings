/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.event;

import java.awt.AWTEvent;

import org.wings.externalizer.ExternalizedResource;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SRequestEvent extends AWTEvent
{

    /**
     * 
     */
    public static final int DELIVER_START = 50000;

    /**
     * 
     */
    public static final int DELIVER_DONE = DELIVER_START+1;

    /**
     * 
     */
    public static final int DISPATCH_START = DELIVER_START+2;

    /**
     * 
     */
    public static final int DISPATCH_DONE = DELIVER_START+3;

    /**
     * 
     */
    public static final int PROCESS_REQUEST = DELIVER_START+4;

    /**
     *
     */
    public static final int REQUEST_START = DELIVER_START+5;

    /**
     *
     */
    public static final int REQUEST_END = DELIVER_START+6;

    /**
     * if type is {@link #DELIVER_START} or {@link #DELIVER_DONE} this field is
     * filled with info about the resource actually delivered, otherwise it is
     * null. 
     */
    protected transient ExternalizedResource requestedResource;

    /**
     * Constructs a ComponentEvent object.
     * @param aSource the Component object that originated the event
     * @param type an integer indicating the type of event
     */
    public SRequestEvent(Object aSource, int type, ExternalizedResource requestedResource) {
        super(aSource, type);

        this.requestedResource = requestedResource;
    }

    /**
     *
     */
    public int getType() {
        return getID();
    }

    /**
     *
     */
    public ExternalizedResource getRequestedResource() {
        return requestedResource;
    }

    /**
     * Returns a string representing the state of this event. This 
     * method is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between 
     * implementations. The returned string may be empty but may 
     * not be <tt>null</tt>.
     *
     * @return a string representation of this event.          
     */
    public String paramString() {
        if (getSource() == null)
            return "no source";

        String typeStr;

        switch (getID()) {
        case DISPATCH_START:
            typeStr = "DISPATCH_START";
            break;
        case DISPATCH_DONE:
            typeStr = "DISPATCH_DONE";
            break;
        case DELIVER_START:
            typeStr = "DELIVER_START";
            break;
        case DELIVER_DONE:
            typeStr = "DELIVER_DONE";
            break;
        default:
            typeStr = "unknown type";
        }
        return typeStr;
    }

    public String toString() {
        return "SRequestEvent[source=" + source + "; " + paramString() + "]";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
