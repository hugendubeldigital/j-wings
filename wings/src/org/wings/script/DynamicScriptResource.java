package org.wings.script;

import java.io.IOException;
import java.util.*;

import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.DynamicResource;
import org.wings.io.Device;
import org.wings.util.ComponentVisitor;

public class DynamicScriptResource
    extends DynamicResource
{
    public DynamicScriptResource(SFrame frame) {
        super(frame, "js", "application/x-javascript");
    }

    public void write(Device out)
        throws IOException
    {
        ScriptWriter visitor = new ScriptWriter(out);
        getFrame().invite(visitor);
    }

    protected static class ScriptWriter
        implements ComponentVisitor
    {
        Device out;

        public ScriptWriter(Device out) {
            this.out = out;
        }

        public void visit(SComponent component) {
            //System.err.println("StyleSheetWriter.visit(" + component.getClass() + ")");
            Collection listeners = component.getScriptListeners();
            if (listeners.size() == 0)
                return;

            Iterator iterator = listeners.iterator();
            while (iterator.hasNext()) {
                ScriptListener listener = (ScriptListener)iterator.next();
                if (listener.getScript() != null) {
                    out.append("// ");
                    out.append(component.getUnifiedId());
                    out.append(".");
                    out.append(listener.getEvent());
                    out.append("\n");
                    out.append(listener.getScript());
                }
            }
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
