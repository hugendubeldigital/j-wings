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

package org.wings.externalizer;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet sends externalized objects to the user. Must be used
 * with the ServletExternalizer.
 *
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @author <a href="mailto:hzeller@to.com">Henner Zeller</a>
 * @version $Revision$
 */
public class ExternalizerServlet
    extends HttpServlet
{
    /**
     * if this is not a transient element, set the expiration header to
     * this timeout, so that the browser is able to cache the item without
     * further request.
     */
    final static int STABLE_EXPIRE = 3600 * 60 * 1000; // one hour

    /**
     * TODO: documentation
     *
     */
    public ExternalizerServlet() {
        super();
    }

    /**
     * Only reload transient elements if they've changed.
     */
    protected long getLastModified(HttpServletRequest request) {
        ExternalizedInfo info;

        info = ServletExternalizer.getExternalizedInfo(request);
        return info.lastModified();
    }

    /**
     * send the to-be-externalized element as stream. Set the 
     * <code>Expires</code> header if possible to allow caching in the client.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException
    {
        ExternalizedInfo info;

        info = ServletExternalizer.getExternalizedInfo(request);
        response.setContentType(info.handler.getMimeType(info.extObject));
        // non-transient items can be cached by the browser
        if (!info.isTransient()) {
            response.setDateHeader("Expires", 
                                   info.lastModified() + STABLE_EXPIRE);
        }
        OutputStream out = response.getOutputStream();
        info.handler.write(info.extObject, out);
        out.flush();
        out.close();
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
