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
package org.wings.plaf.css1;

import java.io.IOException;
import java.util.*;

import org.wings.*; 
import org.wings.border.*;
import org.wings.script.*;
import org.wings.style.*;
import org.wings.io.Device;

/**
 * Utils.java
 *
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public final class Utils
{
    private Utils() {}

    /**
     * Renders a container
     */
    static void renderContainer(Device d, SContainer c)
        throws IOException
    {
        SLayoutManager layout = c.getLayout();

        if (layout != null) {
            layout.write(d);
        }
        else {
            for (int i=0; i < c.getComponentCount(); i++)
                c.getComponentAt(i).write(d);
        }
    }

    static void writeEvents(Device d, SComponent c)
        throws IOException
    {
        Map eventScripts = new HashMap();
        Iterator it = c.getScriptListeners().iterator();
        while (it.hasNext()) {
            ScriptListener script = (ScriptListener)it.next();
            String eventScriptCode = script.getCode();
            String event = script.getEvent();
            if (eventScripts.containsKey(event)) {
                String savedEventScriptCode = (String) eventScripts.get(event);
                eventScriptCode = savedEventScriptCode
                    + (savedEventScriptCode.trim().endsWith(";") ? "" : ";")
                    + eventScriptCode;
            }
            eventScripts.put(event,  eventScriptCode);
        }
        it = eventScripts.keySet().iterator();
        while (it.hasNext()) {
            String event = (String) it.next();
            String code = (String) eventScripts.get(event);
            d.print(" ");
            d.print(event);
            d.print("=\"");
            d.print(code);
            d.print("\"");
        }
    }

    static String style(SComponent component) {
        if (component.getAttributes().size() > 0)
            return "_" + component.getComponentId();
        else if (component.getStyle() != null)
            return component.getStyle();
        return null;
    }

    static String selectionStyle(SComponent component) {
        SSelectionComponent sel = (SSelectionComponent)component;
        if (sel.getSelectionAttributes().size() > 0)
            return "__" + component.getComponentId();
        else if (sel.getSelectionStyle() != null)
            return sel.getSelectionStyle();
        return null;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
