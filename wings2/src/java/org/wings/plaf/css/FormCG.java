// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Form.plaf'
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
package org.wings.plaf.css;


import org.wings.*;
import org.wings.io.Device;

import java.io.IOException;

public class FormCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.FormCG {

    private static final SIcon BLIND_ICON = new SResourceIcon("org/wings/icons/blind.gif");

    /*
    * we render two icons into the page that captures pressing simple 'return'
    * in the page. Why ? Depending on the Browser, the Browser sends the 
    * first or the last submit-button it finds in the page as 'default'-Submit
    * when we simply press 'return' somewhere.
    *
    * However, we don't want to have this arbitrary behaviour in wingS.
    * So we add these two (invisible image-) submit-Buttons, either of it
    * gets triggered on simple 'return'. No real wingS-Button will then be
    * triggered but only the ActionListener added to the SForm. So we have 
    * a way to distinguish between Forms that have been sent as default and
    * pressed buttons.
    *
    * Watchout: the style of these images once had been changed to display:none;
    * to prevent taking some pixel renderspace. However, display:none; made
    * the Internet Explorer not accept this as an input getting the default-focus,
    * so it fell back to the old behaviour. So changed that style to no-padding,
    * no-margin, no-whatever (HZ).
    */

    protected void writeContent(final Device device,
                                final SComponent _c)
            throws IOException {
        final SForm component = (SForm) _c;

        device.print("<form method=\"");
        if (component.isPostMethod()) {
            device.print("post");
        } else {
            device.print("get");
        }
        device.print("\"");
        device.print(" name=\"");
        Utils.write(device, component.getName());
        device.print("\"");
        org.wings.plaf.css.Utils.writeEvents(device, component);
        Utils.optAttribute(device, "class", component.getStyle());
        Utils.optAttribute(device, "enctype", component.getEncodingType());
        Utils.optAttribute(device, "action", component.getRequestURL());

        Utils.printInnerPreferredSize(device, component.getPreferredSize());

        device.print("><input type=\"image\" name=\"_capture_enter1\" border=\"0\" ");
        Utils.optAttribute(device, "src", BLIND_ICON.getURL());
        device.print(" width=\"0\" height=\"0\" tabindex=\"\" style=\"border:none;padding:0px;margin:0px;position:absolute\"/>");
        device.print("<input type=\"hidden\" name=\"");
        Utils.write(device, Utils.event(component));
        device.print("\" value=\"");
        Utils.write(device, component.getName());
        Utils.write(device, SConstants.UID_DIVIDER);
        device.print("\" />");
        Utils.renderContainer(device, component);
        device.print("<input type=\"image\" name=\"_capture_enter2\" border=\"0\" ");
        Utils.optAttribute(device, "src", BLIND_ICON.getURL());
        device.print(" width=\"0\" height=\"0\" tabindex=\"\" style=\"border:none;padding:0px;margin:0px;position:absolute\"/>");
        device.print("</form>");
        device.print("\n");
    }
}
