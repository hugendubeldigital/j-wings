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

package org.wings.template;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Hashtable;
import org.wings.io.Device;

import org.wings.SComponent;

/**
 * TODO: documentation
 *
 * @author
 * @version $Revision$
 */
public final class TemplateParseContext
    implements org.apache.servlet.ssi.ParseContext
{
    private OutputStream myOut;
    private Device sink;
    private Hashtable components;

    public TemplateParseContext (final Device sink, Hashtable components) {
        this.sink = sink;
        this.components = components;
        myOut = new OutputStream () {
            public void close() {}
            public void flush() throws IOException {
                sink.flush();
            }
            public void write(byte b[],
                              int off,
                              int len) throws IOException {
                sink.write (b, off, len);
            }
            public void write(byte b[]) throws IOException {
                sink.write (b);
            }
            public void write(int b) throws IOException {
                sink.write (b);
            }
        };
    }

    public OutputStream getOutputStream () {
        return myOut;
    }

    public void startTag (int number) {
    }

    public void doneTag (int number) {
    }

    /** --- fuer's Template wichtige Funktionen --- **/
    public Device getDevice () {
        return sink;
    }

    public SComponent getComponent (String name) {
        return (SComponent) components.get (name);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
