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
import java.io.InputStream;

import javax.swing.ImageIcon;

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
 * @version $Revision$
 */
public class ResourceImageIcon
    extends ImageIcon
{
    protected Class baseClass;

    protected String resourceFileName;

    /**
     * TODO: documentation
     *
     * @param resourceFileName
     */
    public ResourceImageIcon(String resourceFileName) {
        this(ResourceImageIcon.class, resourceFileName);
    }

    public ResourceImageIcon(Class baseClass, String resourceFileName) {
        super(getImageData(baseClass, resourceFileName));
        this.baseClass = baseClass;
        this.resourceFileName = resourceFileName;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public InputStream getInputStream() {
        return baseClass.getResourceAsStream(resourceFileName);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getExtension() {
        return ".gif";
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        return resourceFileName;
    }

    private static byte[] getImageData(Class baseClass, String resourceFileName) {
        InputStream resource = null;
        try {
            resource = baseClass.getResourceAsStream(resourceFileName);
            if (resource == null) // not found
                return new byte[0];

            byte[] buffer = new byte[resource.available()];
            resource.read(buffer);

            if ( buffer==null )
                buffer = new byte[0];

            return buffer;
        }
        catch (java.io.IOException ioe) {
            System.err.println(ioe.toString());
            return new byte[0];
        }
        finally {
            try {
                resource.close();
            }
            catch(Exception e) {
                System.err.println("Cannot close " + resourceFileName);
            } // ignore
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
