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

package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import java.awt.Color;
import java.util.Iterator;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.style.*;
import org.wings.plaf.xhtml.*;
import org.wings.externalizer.ExternalizeManager;

public final class FrameCG
    extends org.wings.plaf.xhtml.FrameCG
{
    public void installCG(SComponent component) {
        CGManager cgm = component.getSession().getCGManager();
        component.setStyle(cgm.getStyle(getPropertyPrefix() + ".style"));
        ((SFrame)component).setStyleSheet((StyleSheet)cgm.get(getPropertyPrefix() + ".stylesheet"));
    }

    protected void writeAdditionalHeaders(Device d, SFrame frame)
        throws IOException
    {
        StyleSheet styleSheet = frame.getStyleSheet();

        if (styleSheet != null) {
            ExternalizeManager ext = frame.getExternalizeManager();
            String link = null;

            if (ext != null) {
                try {
                    link = ext.externalize(styleSheet);
                }
                catch (java.io.IOException e) {
                    // dann eben nicht !!
                    e.printStackTrace(System.err);
                }
            }
            if (link != null) {
                d.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
                d.append(link);
                d.append("\" />");
            }
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
