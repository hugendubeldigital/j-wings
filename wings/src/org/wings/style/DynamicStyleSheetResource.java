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
        super(frame, ".css", "text/css");
    }

    public void write(Device out)
	throws IOException
    {
	StyleSheetWriter visitor = new StyleSheetWriter(out);
	visitor.visit(getFrame());
    }

    protected static class StyleSheetWriter
	implements ComponentVisitor
    {
	Device out;

	public StyleSheetWriter(Device out) {
	    this.out = out;
	}

	public void visit(SComponent component) {
	    out.append(component.getAttributes().toString());
	}
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
