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

package org.wings.script;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SFrame;
import org.wings.DynamicResource;
import org.wings.io.Device;
import org.wings.util.ComponentVisitor;

/**
 * Traverses the component hierarchy of a frame and gathers the code of
 * the ScriptListeners, that are attached to components in a java script
 * file.
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DynamicScriptResource
    extends DynamicResource
{
    public DynamicScriptResource(SFrame frame) {
        super(frame, "js", "application/x-javascript");
    }

    public void write(Device out)
        throws IOException
    {
        try {
            ScriptWriter visitor = new ScriptWriter(out);
            getFrame().invite(visitor);
        }
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
            throw new IOException(e.getMessage()); // UndeclaredThrowable
        }
    }

    protected static class ScriptWriter
        implements ComponentVisitor
    {
        Device out;

        public ScriptWriter(Device out) {
            this.out = out;
        }

        private void writeListenersFrom(SComponent component) 
            throws IOException {
            Collection listeners = component.getScriptListeners();
            if (listeners.size() == 0)
                return;
            
            Iterator iterator = listeners.iterator();
            while (iterator.hasNext()) {
                ScriptListener listener = (ScriptListener)iterator.next();
                if (listener.getScript() != null) {
                    out.print("// ");
                    out.print(component.getUnifiedId());
                    out.print(".");
                    out.print(listener.getEvent());
                    out.print("\n");
                    out.print(listener.getScript());
                }
            }
        }

        public void visit(SComponent component) throws IOException {
            writeListenersFrom(component);
        }

        public void visit(SContainer container) throws Exception {
            writeListenersFrom(container);
            container.inviteEachComponent(this);
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
