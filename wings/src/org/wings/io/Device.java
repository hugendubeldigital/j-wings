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

/**
 * A general interface for a Device
 * -- PROPOSAL --
 * (hen)
 *
 * @author <a href="mailto:hzeller@to.com">Henner Zeller</a>
 * @version $Revision$
 */
public interface Device
{
    /**
     * Flush this Stream.
     */
    void flush () throws IOException;

    // ------------*
    // Methods which deal with characers using the platform's
    // default character encoding to convert characters into bytes.
    // much like a PrintWriter
    // ------------*/

    /**
     * Print a String.
     */
    Device print (String s) throws IOException;

    /**
     * Print a character.
     */
    Device print (char c)   throws IOException;

    /**
     * Print an integer.
     */
    Device print (int i)    throws IOException;

    /**
     * Print any Object
     */
    Device print (Object o) throws IOException;

    /*------------*
     ** Compatibility methods which allow an easy transition between
     ** StringBuffer and Device.
     ** They notably do _not_ throw Exceptions
     ** !! These methods are supposed to be removed after full
     **    transition; this is just for convenience reasons !
     **------------*/

    /**
     * Print a String. For compatibility.
     * @Xdeprecated use print() instead
     */
    Device append (String s);

    /**
     * Print an Integer. For compatibility.
     * @Xdeprecated use print() instead
     */
    Device append (int i);

    /**
     * Print an Object. For compatibility.
     * @Xdeprecated use print() instead
     */
    Device append (Object o);


    /*-------------*
     ** Methods which write bytes to the stream. Much like an OutputStream.
     **-------------*/

    /**
     * Writes the specified byte to this data output stream.
     */
    Device write (int c) throws IOException;

    /**
     * Writes b.length bytes from the specified byte array to this
     * output stream.
     */
    Device write(byte b[]) throws IOException;

    /**
     * Writes len bytes from the specified byte array starting at offset
     * off to this output stream.
     */
    Device write(byte b[], int off, int len) throws IOException;
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
