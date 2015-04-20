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

import org.wings.*;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.plaf.css.MSIEButtonFix;
import org.wings.plaf.css.Utils;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.session.SessionManager;
import org.wings.util.CGObjectUtil;

import java.io.IOException;

public class TableCG extends org.wings.plaf.css.TableCG implements SParentFrameListener, MSIEButtonFix {

    private static final String FORMS_JS_OBJ = "JScripts.form";

    protected void writeButtonStart(Device device, STable table, String parameter) throws IOException {
        device.print("<button  onclick=\"addHiddenField(this.form,'");
        device.print(table.getParentFrame().getEventEpoch());
        device.print(SConstants.UID_DIVIDER);
        device.print(SConstants.IEFIX_BUTTONACTION);
        device.print("','");
        device.print(table.getName());
        device.print(SConstants.UID_DIVIDER);
        Utils.write(device, parameter);
        device.print("')\"");
    }

    public void installCG(SComponent component) {
        super.installCG(component);
        component.addParentFrameListener(this);
    }

    public void parentFrameAdded(SParentFrameEvent e) {
        SFrame parentFrame = e.getParentFrame();
        ClasspathResource res = new ClasspathResource(CGObjectUtil.getObject(FORMS_JS_OBJ, String.class), "text/javascript");
        String jScriptUrl = SessionManager.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        parentFrame.addHeader(new Script("text/javascript", new DefaultURLResource(jScriptUrl)));
    }

    public void parentFrameRemoved(SParentFrameEvent e) {
    }

    protected void writeLinkStart(Device device, RequestURL selectionAddr) throws IOException {
        device.print("<a onclick=\"javascript:location.href='");
        Utils.write(device, selectionAddr.toString());
        device.print("';\"");
    }
}
