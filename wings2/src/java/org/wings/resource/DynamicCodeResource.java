/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.Resource;
import org.wings.SFrame;
import org.wings.io.Device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Traverses the component hierarchy of a frame and lets the CGs compose
 * the document.
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DynamicCodeResource extends DynamicResource {
    private final transient static Log log = LogFactory.getLog(DynamicCodeResource.class);

    private static final ArrayList DEFAULT_CODE_HEADER = new ArrayList();
    static {
        DEFAULT_CODE_HEADER.add(new Resource.HeaderEntry("Expires", new Date(1000)));
        DEFAULT_CODE_HEADER.add(new Resource.HeaderEntry("Cache-Control", "no-store, no-cache, must-revalidate"));
        DEFAULT_CODE_HEADER.add(new Resource.HeaderEntry("Cache-Control", "post-check=0, pre-check=0"));
        DEFAULT_CODE_HEADER.add(new Resource.HeaderEntry("Pragma", "no-cache"));
    }


    /**
     * Create a code resource for the specified frame.
     * <p>The MIME-type for this frame will be <code>text/html; charset=<i>current encoding</i></code>
     */
    public DynamicCodeResource(SFrame f) {
        super(f, null, provideMimeType(f));
    }

    /**
     * The MIME-type for this {@link Resource}.
     *
     * @return
     */
    private static String provideMimeType(SFrame frame) {
        return "text/html; charset=" + frame.getSession().getCharacterEncoding();
    }

    /**
     * Renders and write the code of the {@link SFrame} attached to this <code>DynamicCodeResource</code>.
     */
    public void write(Device out) throws IOException {
        try {
            getFrame().write(out);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            log.fatal("resource: " + getId(), e);
            throw new IOException(e.getMessage()); // UndeclaredThrowable
        }
    }

    /**
     * The HTTP header parameteres attached to this dynamic code ressource.
     * This <b>static</b> list will by default contain entries to disable caching
     * on the server side. Call <code>getHeaders().clear()</code> to avoid this
     * i.e. if you want to enable back buttons.
     *
     * @return A <code>Collection</code> of {@link org.wings.Resource.HeaderEntry} objects.
     */
    public Collection getHeaders() {
        if (getFrame().isNoCaching())
            return DEFAULT_CODE_HEADER;
        else
            return Collections.EMPTY_SET;
    }
}


