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

package org.wings.style;

import java.io.IOException;
import java.lang.reflect.*;

import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SFrame;
import org.wings.DynamicResource;
import org.wings.io.Device;
import org.wings.util.ComponentVisitor;

/**
 * Traverses the component hierarchy of a frame and gathers the dynamic styles
 * (attributes) of all components in a style sheet.
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DynamicStyleSheetResource
    extends DynamicResource
{
    public DynamicStyleSheetResource(SFrame frame) {
        super(frame, "css", "text/css");
    }

    public void write(Device out)
        throws IOException
    {
        try {
            StyleSheetWriter visitor = new StyleSheetWriter(out);
            getFrame().invite(visitor);
        }
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    protected static class StyleSheetWriter
        implements ComponentVisitor
    {
        Device out;

        public StyleSheetWriter(Device out) {
            this.out = out;
        }
        
        private void writeAttributesFrom(SComponent component) 
            throws IOException {
            AttributeSet attributes = component.getAttributes();
            if (attributes.size() == 0)
                return;
            out.print("._" + component.getUnifiedId());
            out.print(" {");
            attributes.write(out);
            out.print("}\n");
        }
        
        public void visit(SComponent component) throws IOException {
            writeAttributesFrom(component);
        }

        public void visit(SContainer container) throws Exception {
            writeAttributesFrom(container);
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
