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
import java.util.logging.*;
import java.util.ArrayList;
import java.util.Collection;
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

    private static final ArrayList DEFAULT_CODE_HEADER = new ArrayList();

    static {
        DEFAULT_CODE_HEADER.add(new HeaderEntry("Expires", new java.util.Date(1000)));
        DEFAULT_CODE_HEADER.add(new HeaderEntry("Cache-Control", "no-store, no-cache, must-revalidate"));
        DEFAULT_CODE_HEADER.add(new HeaderEntry("Cache-Control", "post-check=0, pre-check=0"));
        DEFAULT_CODE_HEADER.add(new HeaderEntry("Pragma", "no-cache"));
    }
            

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

    public Collection getHeaders() {
        return DEFAULT_CODE_HEADER;
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
