// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Menu.plaf'
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

public class MenuCssCG extends org.wings.plaf.css.MenuItemCG implements SConstants, org.wings.plaf.MenuCG {

    public void installCG(final SComponent comp) {
        super.installCG(comp);
    }

    public void uninstallCG(final SComponent comp) {
    }

//--- code from common area in template.
    public static final SIcon RIGHT_ARROW = new SResourceIcon("org/wings/icons/MenuArrowRight.gif");

    protected void writePopup(final Device device, SMenu menu)
            throws IOException {
        String componentId = menu.getName();
        // calculate max length of children texts for sizing of layer
        int maxLength = 0;
        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
            String text = ((SMenuItem)menu.getMenuComponent(i)).getText();
            if (text != null && text.length() > maxLength) {
                maxLength = ((SMenuItem)menu.getMenuComponent(i)).getText().length();
                if (menu.getMenuComponent(i) instanceof SMenu) {
                        maxLength = maxLength + 2; //graphics
                }
            }
        }
        if (menu.isEnabled()) {
            device.print("<ul");
            device.print(" style=\"width:");
            String stringLength = String.valueOf(maxLength * menu.getWidthScaleFactor());
            device.print(stringLength.substring(0,stringLength.lastIndexOf('.')+2));
            device.print("em;\" class=\"SMenu\">");
            for (int i = 0; i < menu.getMenuComponentCount(); i++) {
                SComponent menuItem = menu.getMenuComponent(i);
    
                if (menuItem.isVisible()) {
                    device.print("<li");
                    if (menuItem instanceof SMenu) {
                        if (menuItem.isEnabled()) {
                            device.print(" class=\"SMenu\"");
                        } else {
                            device.print(" class=\"SMenu_Disabled\"");
                        }
    
                    } else {
                        if (menuItem.isEnabled()) {
                            device.print(" class=\"SMenuItem\"");
                        } else {
    
                            device.print(" class=\"SMenuItem_Disabled\"");
                        }
                    }
    
                    device.print(">");
                    if (menuItem instanceof SMenuItem) {
                            device.print("<a");
                            if (menuItem.isEnabled()) {
                                device.print(" href=\"");
                                writeAnchorAddress(device, (SMenuItem) menuItem);
                                device.print("\"");
                            }
                            if (menuItem instanceof SMenu && menuItem.isEnabled()) {
                                device.print(" class=\"x\"");
                            }
                            device.print(">");
                    }
                    menuItem.write(device);
                    if (menuItem instanceof SMenuItem) {
                        device.print("</a>");
                    }
                    if (menuItem.isEnabled() && menuItem instanceof SMenu) {
                        writePopup(device, (SMenu) menuItem);
                    }
                    device.print("</li>\n");
                }
            }
            device.print("</ul>");
        }
        device.print("\n");
    }

    protected void writeItem(final Device device, SMenuItem menu)
            throws IOException {
        boolean hasParent = menu.getParentMenu() != null;

        // parent, hook, menu
        if (!hasParent) {
            if (menu.isEnabled()) {
                device.print("<span class=\"TopMenu\">");
            } else {
                device.print("<span class=\"TopMenu_Disabled\">");
            }
            
        }
        writeItemContent(device, menu);
        if (!hasParent) {
            device.print("</span>");
        }
    }

    protected void writeAnchorAddress(Device d, SAbstractButton abstractButton)
            throws IOException {
        RequestURL addr = abstractButton.getRequestURL();
        addr.addParameter(abstractButton,
                abstractButton.getToggleSelectionParameter());
        addr.write(d);
    }

    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SMenu component = (SMenu) _c;

        SMenu menu = component;
        writeItem(device, menu);
        if (menu.getParentMenu() == null)
            writePopup(device, menu);
    }
}
