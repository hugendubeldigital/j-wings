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

import java.awt.Image;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.ImageIcon;

import org.wings.session.SessionManager;
import org.wings.externalizer.ExternalizeManager;

/**
 * An SIcon of this type is externalized globally. It is not bound to a session.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class FileImageIcon
    extends FileResource
    implements SIcon
{
    /**
     * TODO: documentation
     */
    private int width = -1;

    /**
     * TODO: documentation
     */
    private int height = -1;

    /**
     * TODO: documentation
     *
     * @param resourceFileName
     */
    public FileImageIcon(String fileName) throws IOException {
        this(new File(fileName));
    }

    public FileImageIcon(File file) throws IOException {
        super(file);

        if (extension == null || extension.length() == 0) {
            extension = "";
            mimeType = "image/png";
        }
        else if (extension.toUpperCase().equals("JPG"))
            mimeType = "image/jpeg";
        else
            mimeType = "image/" + extension;

        calcDimensions();
    }

    /**
     *
     */
    protected void calcDimensions() {
        try {
            bufferResource();
            
            if ( buffer!=null && buffer.isValid()) {
                ImageIcon icon = new ImageIcon(buffer.getBytes());
                width  = icon.getIconWidth();
                height = icon.getIconHeight();
            }
        } catch ( Throwable e ) {
            // is not possible to calc Dimensions
            // maybe it is not possible to buffer resource, 
            // or resource is not a
            // supported image type
        }
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
