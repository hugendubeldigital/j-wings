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

package org.wings;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.Set;
import java.util.logging.*;

import org.wings.io.Device;
import org.wings.util.StringUtil;

/**
 * Traverses the component hierarchy of a frame and lets the CGs compose
 * the document.
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DynamicCodeResource
    extends DynamicResource
{
    private static Logger logger = Logger.getLogger("org.wings");

    /**
     * Create a code resource for the specified frame.
     */
    public DynamicCodeResource(SFrame f) {
        super(f, null, "text/html");
    }

    /**
     * Write the code of the whole frame to the Device.
     */
    public void write(Device out)
        throws IOException
    {
        try {
            getFrame().write(out);
        }
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "resource: " + getId(), e);
            throw new IOException(e.getMessage()); // UndeclaredThrowable
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
