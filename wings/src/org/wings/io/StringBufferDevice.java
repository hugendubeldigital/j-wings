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

package org.wings.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

/**
 * A Device encapsulating a StringBuffer
 *
 * @author <a href="mailto:hzeller@to.com">Henner Zeller</a>
 * @version $Revision$
 */
public final class StringBufferDevice
    implements Device
{
    private StringBuffer buffer;
    private ByteArrayOutputStream byteStream = null;

    /**
     * TODO: documentation
     *
     * @return
     */
    public StringBufferDevice () {
        buffer = new StringBuffer();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        flush();
        return buffer.toString();
    }

    /**
     * Flush this Stream.
     */
    public void flush () {
        if (byteStream != null) {
            buffer.append (byteStream.toString());
            byteStream = null;
        }
    }

    private OutputStream getStream() {
        if (byteStream != null)
            return byteStream;
        byteStream = new ByteArrayOutputStream();
        return byteStream;
    }

    /**
     * Print a String.
     */
    public Device print (String s) {
        if (byteStream != null) flush();
        buffer.append (s);
        return this;
    }

    /**
     * Print a character.
     */
    public Device print (char c) {
        if (byteStream != null) flush();
        buffer.append (c);
        return this;
    }

    /**
     * Print an integer.
     */
    public Device print (int i) {
        if (byteStream != null) flush();
        buffer.append (i);
        return this;
    }

    /**
     * Print any Object
     */
    public Device print (Object o) {
        if (byteStream != null) flush();
        buffer.append (o);
        return this;
    }

    /**
     * Print a String. For compatibility.
     * @*deprecated use print() instead
     */
    public Device append (String s) {
        return print (s);
    }

    /**
     * Print an Integer. For compatibility.
     * @*deprecated use print() instead
     */
    public Device append (int i) {
        return print (i);
    }

    /**
     * Print an Object. For compatibility.
     * @Xdeprecated use print() instead
     */
    public Device append (Object o) {
        return print (o);
    }

    /**
     * Writes the specified byte to this data output stream.
     */
    public Device write (int c) throws IOException {
        getStream().write (c);
        return this;
    }

    /**
     * Writes b.length bytes from the specified byte array to this
     * output stream.
     */
    public Device write(byte b[]) throws IOException {
        getStream().write (b);
        return this;
    }

    /**
     * Writes len bytes from the specified byte array starting at offset
     * off to this output stream.
     */
    public Device write(byte b[], int off, int len) throws IOException {
        getStream().write (b, off, len);
        return this;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
