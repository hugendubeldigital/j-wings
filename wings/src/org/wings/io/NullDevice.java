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
 * Device, that discards everything. For debugging purposes.
 * @author <a href="mailto:hzeller@to.com">Henner Zeller</a>
 * @version $Revision$
 */
public class NullDevice implements Device
{
    /**
     * Flush this Device.
     */
    public void flush () { }

    /**
     * Print a character.
     */
    public Device print (char c) { return this; }

    /**
     * Print a character array.
     */
    public Device print (char[] c) { return this; }

    /**
     * Print len characters from the specified char array starting at offset
     * off to this Device.
     */
    public Device print (char[] c, int start, int len) { return this; }

    //-- print basic objects --

    /**
     * Print a String.
     */
    public Device print (String s) { return this; }

    /**
     * Print an integer.
     */
    public Device print (int i) { return this; }

    /**
     * Print any Object
     */
    public Device print (Object o) { return this; }

    /*-------------*
     ** Methods which write raw bytes to the Device. Much like an OutputStream.
     **-------------*/

    /**
     * Writes the specified byte to this data output stream.
     */
    public Device write (int c) { return this; }

    /**
     * Writes b.length bytes from the specified byte array to this
     * output stream.
     */
    public Device write(byte b[]) { return this; }

    /**
     * Writes len bytes from the specified byte array starting at offset
     * off to this Device.
     */
    public Device write(byte b[], int off, int len) { return this; }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
