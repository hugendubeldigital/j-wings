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

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class ResourceStyleSheet
    implements StyleSheet
{
    ClassLoader classLoader = null;
    String fileName = null;

    /**
     * TODO: documentation
     *
     * @param classLoader the classLoader from which the stylesheet should be loaded
     * @param fileName the path to the style sheet resource
     * @throws IOException
     */
    public ResourceStyleSheet(ClassLoader classLoader, String fileName) {
        this.fileName = fileName;
        this.classLoader = classLoader;
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
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException {
        InputStream is = classLoader.getResourceAsStream(fileName);
        if ( is == null )
          throw new java.io.IOException( "Can not find StyleSheet " + fileName );
        return is;
    }

    public boolean isStable() {
        return true;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Set styleSet() {
        throw new UnsupportedOperationException();
    }

    private static String resolveName(Class baseClass, String fileName) {
        if (fileName == null) {
            return fileName;
        }
        if (!fileName.startsWith("/")) {
            while (baseClass.isArray()) {
                baseClass = baseClass.getComponentType();
            }
            String baseName = baseClass.getName();
            int index = baseName.lastIndexOf('.');
            if (index != -1) {
                fileName = baseName.substring(0, index).replace('.', '/')
                    + "/" + fileName;
            }
        } else {
            fileName = fileName.substring(1);
        }
        return fileName;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        return fileName;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
