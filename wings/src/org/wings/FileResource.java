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

import org.wings.externalizer.AbstractExternalizeManager;
import org.wings.externalizer.ExternalizeManager;
import org.wings.session.SessionManager;

import java.io.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class FileResource
    extends StaticResource
{
    private File file;

    public FileResource(String name)
        throws IOException
    {
        this(new File(name));
    }

    public FileResource(File file) {
        super(null, "unknown");
        this.file = file;
        int dotIndex = file.getName().lastIndexOf('.');
        if (dotIndex > -1) {
            extension = file.getName().substring(dotIndex + 1);
        }
        externalizerFlags = ExternalizeManager.REQUEST | ExternalizeManager.FINAL;
    }

    public String toString() {
        return getId() + (file != null ? " " + file.getName() : "");
    }

    protected final InputStream getResourceStream()
        throws IOException
    {
        return new FileInputStream(file);
    }
}
