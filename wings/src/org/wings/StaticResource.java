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
import java.util.logging.*;

import org.wings.session.*;
import org.wings.externalizer.ExternalizeManager;
import org.wings.externalizer.AbstractExternalizeManager;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public abstract class StaticResource
    extends Resource
{
    private final static Logger logger = Logger.getLogger("org.wings");
    /**
     * Flags that influence the behaviour of the externalize manager
     */
    protected int externalizerFlags = ExternalizeManager.FINAL;

    /**
     * A buffer for temporal storage of the resource
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
         * returns the _raw_ buffer; i.e. the buffer may be larger than
         * the current size().
         */
        public byte[] getBytes() {
            return buf;
        }
    }

    /**
     * A static resource that is obtained from the specified class loader
     *
     * @param classLoader the classLoader from which the resource is obtained
     * @param resourceFileName the resource relative to the baseClass
     */
    public StaticResource(String extension, String mimeType) {
        super(extension, mimeType);
    }
    /**
     * Get the id that identifies this resource as an externalized object.
     * If the object has not been externalized yet, it will be externalized.
     * @return the externalization id
     */
    public String getId() {
        if (id == null) {
            ExternalizeManager ext = SessionManager.getSession().getExternalizeManager();
            id = ext.getId(ext.externalize(this, externalizerFlags));
            logger.fine("new " + getClass().getName() + " with id " + id);
        }
        return id;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Reads the resource into an LimitedBuffer and returns it. If the
     * size of the resource is larger than 
     * {@link LimitedBuffer#MAX_SIZE_TO_BUFFER}, then the returned Buffer
     * is empty and does not contain the Resource's content (and the
     * isValid() flag is false).
     *
     * @return buffered resource as LimitedBuffer, that may be invalid,
     *         if the size of the resource is beyond MAX_SIZE_TO_BUFFER. It is
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
     * is not larger than {@link LimitedBuffer#MAX_SIZE_TO_BUFFER}, then
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
     * Return the size in bytes of the resource, if known
     *
     * @return
     */
    public final int getLength() {
        if ( buffer!=null && buffer.isValid())
            return buffer.size();
        else
            return -1;
    }

    public SimpleURL getURL() {
        String name = null;
        if (extension != null)
            name = getId() + "." + extension;
        else
            name = getId();

        // append the sessionid, if not global
        if ((externalizerFlags & ExternalizeManager.GLOBAL) > 0) {
            return new SimpleURL(name);
        }
        else {
            RequestURL requestURL = (RequestURL)getPropertyService().getProperty("request.url");
            requestURL = (RequestURL) requestURL.clone();
            requestURL.setResource(name);
            return requestURL;
        }
    }

    private PropertyService propertyService;
    protected PropertyService getPropertyService() {
        if (propertyService == null)
            propertyService = (PropertyService)SessionManager.getSession();
        return propertyService;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        return getId();
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

    protected abstract InputStream getResourceStream() throws IOException;
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
