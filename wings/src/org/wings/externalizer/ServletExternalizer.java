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

import java.awt.Image;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

/**
 * This is an externalizer which uses a servlet (ExternalizerServlet) to
 * externalize objects.
 *
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public class ServletExternalizer
    extends AbstractExternalizer
{
    /**
     * TODO: documentation
     */
    protected String httpAddress = null;
    private final static HashMap externalizedNameMap = new HashMap();


    /**
     * TODO: documentation
     *
     */
    public ServletExternalizer(ServletConfig config) {
        httpAddress = config.getInitParameter("externalizer.servlet.url");

        if ( httpAddress == null ) {
            throw new IllegalStateException("externalizer.servlet.url required in initArgs");
        }
    }


    /**
     * TODO: documentation
     */
    protected synchronized void doExternalize(ExternalizedInfo info)
        throws java.io.IOException
    {
        // externalize in this context means: save object in a map
        externalizedNameMap.put(info.extFileName, info);
    }


    /**
     * TODO: documentation
     *
     * @return
     */
    protected String getExternalizedURL(ExternalizedInfo info) {
        return httpAddress + "?NAME=" + info.extFileName;
    }


    /**
     * TODO: documentation
     */
    protected synchronized void doDelete(ExternalizedInfo info) {
        externalizedNameMap.remove(info.extFileName);
        info.extObject = null;
    }


    /** for externalizer servlet */
    public static ExternalizedInfo getExternalizedInfo(HttpServletRequest request) {
        String fname = request.getParameter( "NAME" );
        return (ExternalizedInfo)externalizedNameMap.get(fname);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
