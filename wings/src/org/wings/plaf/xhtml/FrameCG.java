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

package org.wings.plaf.xhtml;

import java.io.IOException;

import java.awt.Color;
import java.util.*;

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class FrameCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.FrameCG
{
    private final static String propertyPrefix = "Frame";

    public void installCG(SComponent component) {
        super.installCG(component);

        SFrame frame = (SFrame)component;

        frame.addDynamicResource(new DynamicCodeResource(frame));
    }

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SFrame frame = (SFrame)c;
        writeHeader(d, frame);
        writeBody(d, frame);
    }

    protected void writeHeader(Device d, SFrame frame)
        throws IOException
    {
        String language = "en"; // TODO: ???
        String title = frame.getTitle();
        List metas = frame.metas();
        List headers = frame.headers();

        d.print("<?xml version=\"1.0\" encoding=\"");
        d.print(frame.getSession().getCharSet());
        d.print("\"?>\n");
        d.print("<!DOCTYPE html\n");
        d.print("   PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n");
        d.print("   \"DTD/xhtml1-transitional.dtd\">\n");
        d.print("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"");
        d.print(language);
        d.print("\" lang=\"");
        d.print(language);
        d.print("\">\n");
        d.print("<head>\n");
        if (title != null) {
            d.print("<title>").print(title).print("</title>\n");
        }

        if (frame.getBaseTarget() != null)
            d.print("<base target=\"")
                .print(frame.getBaseTarget())
                .print("\" />");

        d.print("<meta http-equiv=\"Content-type\" content='text/html; charset=\"");
        d.print(frame.getSession().getCharSet());
        d.print("\"' />\n");
        d.print("<meta http-equiv=\"expires\" content=\"0\" />\n");
        d.print("<meta http-equiv=\"pragma\" content=\"no-cache\" />\n");

        Iterator it = metas.iterator();
        while (it.hasNext()) {
            d.print("<meta ");
            d.print(it.next());
            d.print(" />\n");
        }

        it = headers.iterator();
        while (it.hasNext()) {
            d.print(it.next());
            d.print("\n");
        }

        writeAdditionalHeaders(d, frame);

        d.print("</head>\n");
    }

    protected void writeAdditionalHeaders(Device d, SFrame frame)
        throws IOException {}

    protected void writeBody(Device d, SFrame frame)
        throws IOException
    {
        d.print("<body>");
        writeContents(d, frame);
        d.print("\n</body>\n</html>");
    }

    protected void writeContents(Device d, SFrame frame)
        throws IOException
    {
        if (frame.isVisible()) {
            frame.getLayout().write(d);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
