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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

import org.wings.session.SessionManager;
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
 * An SIcon of this type is externalized globally. It is not bound 
 * to a session. This SIcon gets the content of the image from an image
 * found in the classpath, i.e. an image that is deployed in the 
 * classes/jar/war file.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ResourceImageIcon
    extends ClasspathResource
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
    public ResourceImageIcon(String resourceFileName) {
        this(ResourceImageIcon.class.getClassLoader(), resourceFileName);
    }

    public ResourceImageIcon(ClassLoader classLoader, String resourceFileName){
        super(classLoader, resourceFileName);

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
                BufferedImage image = 
                    ImageIO.read(new ByteArrayInputStream(buffer.getBytes()));
                width = image.getWidth();
                height = image.getHeight();
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
