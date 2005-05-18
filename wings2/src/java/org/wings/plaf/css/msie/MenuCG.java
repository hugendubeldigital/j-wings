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
import org.wings.io.Device;
import org.wings.session.SessionManager;

import java.io.IOException;

public class MenuCG extends org.wings.plaf.css.MenuCG {

    public void writePopup(final Device device, SMenu menu)
            throws IOException {
        String componentId = menu.getName();
        if (menu.isEnabled()) {
            device.print("<ul");
            boolean hasParent = menu.getParentMenu() != null;
            // calculate max length of children texts for sizing of layer
            int maxLength = 0;
            for (int i = 0; i < menu.getMenuComponentCount(); i++) {
                if (!(menu.getMenuComponent(i) instanceof SMenuItem))
                    continue;
                String text = ((SMenuItem)menu.getMenuComponent(i)).getText();
                if (text != null && text.length() > maxLength) {
                    maxLength = text.length();
                    if (menu.getMenuComponent(i) instanceof SMenu) {
                            maxLength = maxLength + 2; //graphics
                    }
                }
            }
            device.print(" style=\"width:");
            String stringLength = String.valueOf(maxLength * menu.getWidthScaleFactor());
            device.print(stringLength.substring(0,stringLength.lastIndexOf('.')+2));
            device.print("em;\"");
            device.print(" id=\"");
            device.print(componentId);
            device.print("_pop\"");
            device.print(" class=\"SMenu\">");
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
                            if (menuItem instanceof SMenu) {
                                if (menuItem.isEnabled()) {
                                    device.print(" class=\"x\"");
                                } else {
                                    device.print(" class=\"y\"");
                                }
                            }
                            device.print(">");
                    }
                    menuItem.write(device);
                    if (menuItem instanceof SMenuItem) {
                        device.print("</a>");
                    }
                    if (menuItem.isEnabled() && menuItem instanceof SMenu) {
                        ((SMenu)menuItem).writePopup(device);
                    }
                    device.print("</li>\n");
                }
            }
            device.print("</ul>");
        }
        device.print("\n");
    }
}
