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
import java.io.ByteArrayOutputStream;

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
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public class Resource
{
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
    protected LimitedBuffer buffer;
    
    /**
     * An ByteArrayOutputStream that buffers up to the limit
     * MAX_SIZE_TO_BUFFER.
     */
    protected final static class LimitedBuffer extends ByteArrayOutputStream {
        public static final int MAX_SIZE_TO_BUFFER = 8 * 1024; // 8KByte
        private boolean withinLimit;

        /**
         * creates a new buffer
         */
        LimitedBuffer() { 
            /*
             * don't waste too much memory; most resources (like icons)
             * are tiny, so we should start with a small initial size.
             */
            super(64);
            withinLimit = true;
        }
        
        /**
         * write to the stream. If the output size exceeds the limit,
         * then set the stream to error state.
         */
        public void write(byte[] b, int off, int len) {
            if (!withinLimit) return;
            withinLimit = (count + len < MAX_SIZE_TO_BUFFER);
            if (withinLimit) 
                super.write(b, off, len);
            else
                reset(); // discard all input so far: it would become too large
        }

        // Don't use write(int b)! It does not check the size.

        /**
         * returns, whether the filled buffer is within the limits,
         * and thus, its content is valid and can be used.
         */
        public boolean isValid() { return withinLimit; }
        
        /**
         * returns the <em>raw</em> buffer. This means, that the buffer 
         * may be larger than size() reports.
         */
        public byte[] getBytes() {
            return buf;
        }
    }

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
	    url = SessionManager.getSession()
		.getExternalizeManager()
		.externalize(this, externalizerFlags);
	}
	return url;
    }

    /**
     * Reads the resource into an LimitedBuffer and returns it. If the
     * size of the resource is larger than 
     * {@link LimitedBuffer#MAX_SIZE_BUFFER}, then the returned Buffer
     * is empty and does not contain the Resource's content (and the
     * isValid() flag is false).
     *
     * @return buffered resource as LimitedBuffer, that may be invalid,
     *         if the size of the resource is beyond MAX_SIZE_BUFFER. It is
     *         null, if the Resource returned an invalid stream.
     */
    protected LimitedBuffer bufferResource() throws IOException {
        if ( buffer==null ) {
            InputStream resource = getResourceStream();
            if ( resource!=null ) {
                byte[] copyBuffer = new byte[1024];
                buffer = new LimitedBuffer();
                int read;
                while (buffer.isValid()
                       && (read = resource.read(copyBuffer)) > 0) {
                    buffer.write(copyBuffer, 0, read);
                }
                resource.close();
            }
        }
        return buffer;
    }

    /**
     * writes the Resource to the given Stream. If the resource
     * is not larger than {@link LimitedBuffer#MAX_SIZE_BUFFER}, then
     * an internal buffer caches the content the first time, so that it
     * is delivered as fast as possible at any subsequent calls.
     *
     * @param OutputStream the stream, the content of the resource should
     *                     be written to.
     */
    public final void write(OutputStream out) throws IOException {
        if ( buffer == null ) {
            bufferResource();
            if ( buffer == null )     // no valid bufferable resource available
                return;
        }
        
        if ( buffer.isValid() ) {     // buffered and small enough. buffer->out
            buffer.writeTo(out);
        }
        else {                        // too large to be buffered. res->out
            InputStream resource = getResourceStream();
            if ( resource!=null ) {
                byte[] copyBuffer = new byte[1024];
                int read;
                while ((read = resource.read(copyBuffer)) > 0) {
                    out.write(copyBuffer, 0, read);
                }
                resource.close();
            }
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
        if ( buffer!=null && buffer.isValid())
            return buffer.size();
        else 
            return -1;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final String getMimeType() {
        return mimeType;
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
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
