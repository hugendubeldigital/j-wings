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
    protected ClassLoader classLoader;

    protected String resourceFileName;
    protected String extension; 

    /**
     * TODO: documentation
     *
     * @param resourceFileName
     */
    public ResourceImageIcon(String resourceFileName) {
        this(ResourceImageIcon.class.getClassLoader(), resourceFileName);
    }

    public ResourceImageIcon(Class baseClass, String resourceFileName) {
        this(baseClass.getClassLoader(), resolveName(baseClass, resourceFileName));
    }

    public ResourceImageIcon(ClassLoader classLoader, String resourceFileName) {
        super(getImageData(classLoader, resourceFileName));
        this.classLoader = classLoader;
        this.resourceFileName = resourceFileName;
        extension = resourceFileName.substring(resourceFileName.lastIndexOf('.') + 1);
        if (extension == null || extension.length() == 0)
            extension = "png";
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public InputStream getInputStream() {
        return classLoader.getResourceAsStream(resourceFileName);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getExtension() {
        return extension;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        return resourceFileName;
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

    private static byte[] getImageData(ClassLoader classLoader, String resourceFileName) {
        InputStream resource = null;
        try {
            resource = classLoader.getResourceAsStream(resourceFileName);
            if (resource == null) {
                // not found
                System.err.println("resource not found: " + resourceFileName);
                return new byte[0];
            }

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
                if (resource != null)
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
