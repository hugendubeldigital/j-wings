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

import org.wings.*; import org.wings.border.*;
import org.wings.script.DynamicScriptResource;
import org.wings.script.ScriptListener;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.style.*;
import org.wings.plaf.xhtml.*;
import org.wings.externalizer.ExternalizeManager;

public final class FrameCG
    extends org.wings.plaf.xhtml.FrameCG
{
    public void installCG(SComponent component) {
        super.installCG(component);

        SFrame frame = (SFrame)component;
        
        // FIXME: these links pile up, if we switch the CG's !

        /*
         * dynamic style sheet generated from attributes.
         */
        DynamicResource styleSheetResource = new DynamicStyleSheetResource(frame);
        frame.addDynamicResource(styleSheetResource);
        frame.addLink(new SLink("stylesheet", null, 
                                "text/css", null, 
                                styleSheetResource));

        /*
         * dynamic java script resource.
         */
        DynamicResource scriptResource = new DynamicScriptResource(frame);
        frame.addDynamicResource(scriptResource);
        frame.addLink(new SLink("javascript", null, 
                                "application/x-javascript", null,
                                scriptResource));
        
        /*
         * static stylesheet, coming with the CG
         */
        CGManager cgManager = frame.getSession().getCGManager();
        StaticResource staticResource = (StaticResource)cgManager
            .getObject("lookandfeel.stylesheet",
                       Resource.class);
        staticResource.setMimeType("text/css");
        frame.addLink(new SLink("stylesheet", null, 
                                "text/css", null, 
                                staticResource));
    }

    protected void writeAdditionalHeaders(Device d, SFrame frame)
        throws IOException
    {
        Iterator iterator = frame.links().iterator();
        while (iterator.hasNext()) {
            SLink link = (SLink)iterator.next();
            link.write(d);
        }
    }

    protected void writeBody(Device d, SFrame frame)
        throws IOException
    {
        d.print("<body");
        String style = ((frame.getStyle() != null) 
                              ? frame.getStyle().getName() 
                              : null);
        if ( style == null ) {
            style = ((frame.getAttributes().size() > 0) 
                     ? ("_" + frame.getComponentId()) 
                     : null);
        }
        if (style != null) {
            d.print(" class=\"").print(style).print("\"");
        }
        //System.err.println("blubber");
        Iterator it = frame.getScriptListeners().iterator();
        while (it.hasNext()) {
            ScriptListener script = (ScriptListener)it.next();
            d.print(" ");
            d.print(script.getEvent());
            d.print("=\"");
            d.print(script.getCode());
            d.print("\"");
        }
        d.print(">");
        writeContents(d, frame);
        d.print("\n</body>\n</html>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
