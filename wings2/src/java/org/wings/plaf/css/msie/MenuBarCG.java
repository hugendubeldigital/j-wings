/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.css.msie;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SFrame;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.externalizer.ExternalizeManager;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.script.JavaScriptListener;
import org.wings.session.SessionManager;

import java.io.IOException;

public class MenuBarCG
        extends org.wings.plaf.css.MenuBarCG {

    private final transient static Log log = LogFactory.getLog(MenuBarCG.class);

    /**
     * @param device
     * @throws IOException
     */
    protected void printSpacer(final Device device) throws IOException {
        // do nothing, ie does not need spacers
    }
}
