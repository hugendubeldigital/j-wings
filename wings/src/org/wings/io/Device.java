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

/**
 * A general interface for a Output-Device.
 * A Device is the destination, where the HTML-code is written to. This is
 * the 'Graphics' - device.
 *
 * @author <a href="mailto:hzeller@to.com">Henner Zeller</a>
 * @version $Revision$
 */
public interface Device
{
    /**
     * Flush this Device.
     */
    void flush () throws IOException;

    // ------------*
    // Methods which deal with characers using the platform's
    // default character encoding to convert characters into bytes.
    // much like a PrintWriter
    // ------------*/
    
    // -- print basic characters --

    /**
     * Print a character.
     */
    Device print (char c)   throws IOException;

    /**
     * Print a character array.
     */
    Device print (char[] c) throws IOException;

    /**
     * Print len characters from the specified char array starting at offset
     * off to this Device.
     */
    Device print (char[] c, int start, int len) throws IOException;

    //-- print basic objects --

    /**
     * Print a String.
     */
    Device print (String s) throws IOException;

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
     ** THIS WILL NOT BE THERE IN 1.0
     **------------*/

    /**
     * Print a String. For compatibility.
     * @deprecated use print() instead
     */
    Device append (String s);

    /**
     * Print an Integer. For compatibility.
     * @deprecated use print() instead
     */
    Device append (int i);

    /**
     * Print an Object. For compatibility.
     * @deprecated use print() instead
     */
    Device append (Object o);


    /*-------------*
     ** Methods which write raw bytes to the Device. Much like an OutputStream.
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
     * off to this Device.
     */
    Device write(byte b[], int off, int len) throws IOException;
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
