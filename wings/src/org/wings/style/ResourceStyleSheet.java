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

package org.wings.style;

import java.io.*;
import java.net.*;
import java.util.Set;

import org.wings.Resource;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class ResourceStyleSheet extends Resource
{

    /**
     * TODO: documentation
     *
     * @param classLoader the classLoader from which the stylesheet should be loaded
     * @param fileName the path to the style sheet resource
     * @throws IOException
     */
    public ResourceStyleSheet(ClassLoader classLoader, String fileName) {
        super(classLoader, fileName);
    }

    /**
     * TODO: documentation
     *
     * @param fileName the path to the style sheet resource
     * @throws IOException
     * @deprecated provide the <code>classLoader</code> instead a <code>baseClass</code>
     */
    public ResourceStyleSheet(Class baseClass, String fileName) {
        this(baseClass.getClassLoader(), resolveName(baseClass, fileName));
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Set styleSet() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isFinal() {
        return true;
    } 
    
    public InputStream getInputStream() {
        return getResourceStream();
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
