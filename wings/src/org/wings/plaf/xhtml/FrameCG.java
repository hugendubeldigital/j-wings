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

import org.wings.*;
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

        d.append("<?xml version=\"1.0\" encoding=\"");
        d.append(frame.getSession().getCharSet());
        d.append("\"?>\n");
        d.append("<!DOCTYPE html\n");
        d.append("   PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n");
        d.append("   \"DTD/xhtml1-transitional.dtd\">\n");
        d.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"");
        d.append(language);
        d.append("\" lang=\"");
        d.append(language);
        d.append("\">\n");
        d.append("<head>\n");
        if (title != null) {
            d.append("<title>").append(title).append("</title>\n");
        }

        if (frame.getBaseTarget() != null)
            d.append("<base target=\"")
                .append(frame.getBaseTarget())
                .append("\" />");

        d.append("<meta http-equiv=\"Content-type\" content='text/html; charset=\"");
        d.append(frame.getSession().getCharSet());
        d.append("\"' />\n");
        d.append("<meta http-equiv=\"expires\" content=\"0\" />\n");
        d.append("<meta http-equiv=\"pragma\" content=\"no-cache\" />\n");

        Iterator it = metas.iterator();
        while (it.hasNext()) {
            d.append("<meta ");
            d.append(it.next());
            d.append(" />\n");
        }

        it = headers.iterator();
        while (it.hasNext()) {
            d.append(it.next());
            d.append("\n");
        }

        writeAdditionalHeaders(d, frame);

        d.append("</head>\n");
    }

    protected void writeAdditionalHeaders(Device d, SFrame frame)
        throws IOException {}

    protected void writeBody(Device d, SFrame frame)
        throws IOException
    {
        d.append("<body>");
        writeContents(d, frame);
        d.append("\n</body>\n</html>");
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
