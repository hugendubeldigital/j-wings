/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.io;

import java.io.IOException;
import javax.servlet.ServletOutputStream;

/**
 * A Device encapsulating a ServletOutputStream
 *
 * @author <a href="mailto:hzeller@to.com">Henner Zeller</a>
 * @version $Revision$
 */
public final class ServletDevice
    implements Device
{
    private ServletOutputStream out;

    /**
     * TODO: documentation
     *
     * @param out
     * @return
     */
    public ServletDevice (ServletOutputStream out) {
        this.out = out;
    }

    /**
     * Flush this Stream.
     */
    public void flush () throws IOException {
        out.flush();
    }

    /**
     * Print a String.
     */
    public Device print (String s)  throws IOException {
        if (s == null)
            out.print ("null");
        else
            out.print (s);
        return this;
    }

    /**
     * Print an integer.
     */
    public Device print (int i)    throws IOException {
        out.print (i);
        return this;
    }

    /**
     * Print any Object
     */
    public Device print (Object o) throws IOException {
        if (o == null)
            out.print ("null");
        else
            out.print (o.toString());
        return this;
    }

    /**
     * Print a String. For compatibility.
     * @*deprecated use print() instead
     */
    public Device append (String s) {
        try {
            print (s);
        }
        catch (IOException ignore) {}
        return this;
    }

    /**
     * Print an Integer. For compatibility.
     * @*deprecated use print() instead
     */
    public Device append (int i) {
        try {
            print (i);
        }
        catch (IOException ignore) {}
        return this;
    }

    /**
     * Print any Object. For compatibility.
     * @*deprecated use print() instead
     */
    public Device append (Object o) {
        try {
            print (o);
        }
        catch (IOException ignore) {}
        return this;
    }

    /**
     * Print a character.
     */
    public Device print (char c) throws IOException {
        out.print (c);
        return this;
    }

    /**
     * Writes the specified byte to this data output stream.
     */
    public Device write (int c) throws IOException {
        out.write (c);
        return this;
    }

    /**
     * Writes b.length bytes from the specified byte array to this
     * output stream.
     */
    public Device write(byte b[]) throws IOException {
        out.write (b);
        return this;
    }

    /**
     * Writes len bytes from the specified byte array starting at offset
     * off to this output stream.
     */
    public Device write(byte b[], int off, int len) throws IOException {
        out.write (b, off, len);
        return this;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
