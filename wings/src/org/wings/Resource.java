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
import java.util.logging.*;
import java.util.*;

import org.wings.session.*;
import org.wings.externalizer.ExternalizeManager;

/*
 * Diese Klasse ist nur ein Wrapper, um Eingabestroeme von Grafiken mit dem
 * ExternalizeManager mit der richtigen Endung und ohne Umweg einer neuen
 * Codierung (die z.B. keine Transparenz unterstuetzt) uebers WWW zugreifbar zu
 * machen. Zugleich muss diese Klasse aber auch zu der API der Componenten
 * passen, also ein Image bzw. ImageIcon sein. ImageIcon ist einfacher zu
 * benutzen und implementiert schon alles was benoetigt wird...
 */
/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public abstract class Resource implements Serializable, URLResource
{
    private final static Logger logger = Logger.getLogger("org.wings");

    /**
     *
     */
    protected String id;

    /**
     *
     */
    protected RequestURL requestURL = new RequestURL();

    /**
     * TODO: documentation
     */
    protected String extension; 

    /**
     * TODO: documentation
     */
    protected String mimeType; 


    protected Resource(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getLength() {
        return -1;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final String getExtension() {
        return extension;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final String getMimeType() {
        return mimeType;
    }

    protected Set headers;

    public void setHeaders(Set headers) {
        this.headers = headers;
    }

    public Set getHeaders() { return headers; }

    /**
     *
     */
    public abstract String getId();

    public abstract SimpleURL getURL();

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        return getId();
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
