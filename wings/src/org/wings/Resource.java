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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class Resource
{
    public static final int MAX_SIZE_TO_BUFFER = 100 * 1024;

    /**
     * TODO: documentation
     */
    protected ClassLoader classLoader;

    /**
     * TODO: documentation
     */
    protected String resourceFileName;

    /**
     * TODO: documentation
     */
    protected String extension; 

    /**
     * TODO: documentation
     */
    protected String mimeType; 

    /**
     * TODO: documentation
     */
    protected String url; 

    /**
     * TODO: documentation
     */
    protected int externalizerFlags = ExternalizeManager.GLOBAL | ExternalizeManager.FINAL; 

    /**
     * TODO: documentation
     */
    protected byte[] buffer; 

    /**
     * TODO: documentation
     *
     * @param resourceFileName
     */
    public Resource(String resourceFileName) {
        this(Resource.class.getClassLoader(), resourceFileName);
    }

    public Resource(Class baseClass, String resourceFileName) {
        this(baseClass.getClassLoader(), resolveName(baseClass, resourceFileName));
    }

    public Resource(ClassLoader classLoader, String resourceFileName) {
        this.classLoader = classLoader;
        this.resourceFileName = resourceFileName;

        int dotIndex = resourceFileName.lastIndexOf('.');
        if ( dotIndex >= 0 )
            extension = resourceFileName.substring(dotIndex + 1);

        mimeType = "unknown";
    }

    /**
     * returns the URL, the icon can be fetched from. This URL may
     * be relative, usually if generated from the externalizer.
     */
    public final String getURL() {
	if (url == null) {
            System.out.println("[Resource] externalize with " + externalizerFlags);
	    url = SessionManager.getSession()
		.getExternalizeManager()
		.externalize(this, externalizerFlags);
	}
	return url;
    }

    public final void write(OutputStream out) throws IOException {
        if ( buffer==null ) {
            InputStream resource = getResourceStream();
            if ( resource!=null ) {
                int size = resource.available();
                if ( size > MAX_SIZE_TO_BUFFER ) {
                    buffer = new byte[2000];
                    while ( resource.available()>0 ) {
                        int read = resource.read(buffer);
                        out.write(buffer, 0, read);
                    }
                    resource.close();
                    // no caching for that big icons
                    buffer = null;
                } else {
                    buffer = new byte[size];
                    resource.read(buffer);
                    resource.close();
                    out.write(buffer);
                }

            }
            
        } else {
            out.write(buffer);
        }

        out.flush();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final String getExtension() {
        return extension;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final int getLength() {
        if ( buffer!=null )
            return buffer.length;
        else 
            return -1;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final String getMimeType() {
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

    protected static String resolveName(Class baseClass, String fileName) {
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

    protected final InputStream getResourceStream() {
        return classLoader.getResourceAsStream(resourceFileName);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
