package org.wings.style;

import java.io.IOException;

import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.DynamicResource;
import org.wings.io.Device;
import org.wings.util.ComponentVisitor;

public class DynamicStyleSheetResource
    extends DynamicResource
{
    public DynamicStyleSheetResource(SFrame frame) {
        super(frame, "css", "text/css");
    }

    public void write(Device out)
	throws IOException
    {
	StyleSheetWriter visitor = new StyleSheetWriter(out);
	getFrame().invite(visitor);
    }

    protected static class StyleSheetWriter
	implements ComponentVisitor
    {
	Device out;

	public StyleSheetWriter(Device out) {
	    this.out = out;
	}

	public void visit(SComponent component) {
            //System.err.println("StyleSheetWriter.visit(" + component.getClass() + ")");
            if (component.getAttributes().size() == 0)
                return;
            out.append("#s_");
            out.append(component.getUnifiedId());
            out.append("{ ");
	    out.append(component.getAttributes().toString());
            out.append("}\n");
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
