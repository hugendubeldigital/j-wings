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

package org.wings.template;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import org.wings.io.Device;

import org.wings.SComponent;
import org.wings.STemplateLayout;

import org.wings.template.parser.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public final class TemplateParseContext implements ParseContext
{
    private OutputStream myOut;
    private Device sink;
    private STemplateLayout layout;

    public TemplateParseContext (final Device sink, STemplateLayout layout) {
        this.sink = sink;
        this.layout = layout;
        /**
         * implement an OutputStream on top
         * of a sink.
         */
        myOut = new OutputStream () {
            public final void close() {}
            public final void flush() throws IOException {
                sink.flush();
            }
            public final void write(byte b[],
                                    int off,
                                    int len) throws IOException {
                sink.write (b, off, len);
            }
            public final void write(byte b[]) throws IOException {
                sink.write (b);
            }
            public final void write(int b) throws IOException {
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

    /*
     * important for the template: the components write to this sink
    */
    public Device getDevice () {
        return sink;
    }

    public SComponent getComponent (String name) {
        return (SComponent) layout.getComponentMap().get (name);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
