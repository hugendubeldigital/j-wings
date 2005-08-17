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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.LowLevelEventListener;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SPopupMenu;
import org.wings.border.STitledBorder;
import org.wings.dnd.DragSource;
import org.wings.io.Device;

import javax.swing.*;
import java.io.IOException;

/**
 * @author ole
 */
public class PrefixAndSuffixDelegate implements org.wings.plaf.PrefixAndSuffixDelegate {
    private final static transient Log log = LogFactory.getLog(PrefixAndSuffixDelegate.class);

    public PrefixAndSuffixDelegate() {
    }

    public void writePrefix(Device device, SComponent component) throws IOException {
        final SDimension prefSize = component.getPreferredSize();
        final StringBuffer cssInlineStyle = new StringBuffer();

        Utils.printDebugNewline(device, component);
        Utils.printDebug(device, "<!-- ").print(component.getName()).print(" -->");

        //------------------------ OUTER DIV

        // This is the outer DIV element of a component
        // it is responsible for Postioning (i.e. it take up all free space around to i.e. center
        // the inner div inside this free space
        device.print("<div");
        if (component.getStyle() != null && component.getStyle().length() > 0) {
            Utils.optAttribute(device, "class", component.getStyle() + "_Box");
        }
        Utils.optAttribute(device, "id", component.getName());
        if (component instanceof DragSource) {
            cssInlineStyle.append("position:relative;");
        }

        // if sizes are spec'd in percentages, we need the outer box to have full size...
        final boolean isHeightPercentage = prefSize != null && prefSize.height != null && prefSize.height.indexOf("%") != -1;
        final boolean isWidthPercentage = prefSize != null && prefSize.width != null && prefSize.width.indexOf("%") != -1;
        // special case of special case: if the component with relative size is vertically aligned, we must avoid 100% heigth
        final boolean isVAligned = (component.getVerticalAlignment() == SConstants.CENTER
                || component.getVerticalAlignment() == SConstants.BOTTOM);
        if (isHeightPercentage && isVAligned == false) {
            cssInlineStyle.append("height:100%;");
        }
        if (isWidthPercentage) {
            cssInlineStyle.append("width:100%;");
        }

        // Output collected inline CSS style
        Utils.optAttribute(device, "style", cssInlineStyle);
        device.print(">"); // div

        //------------------------ INNER DIV

        // This is the inner DIV around each component.
        // It is responsible for component size, and other styles.
        device.print("<div");
        Utils.optAttribute(device, "id", component.getName()+"_i");
        //id=\"").print(component.getName()).print("\"");
        // Special handling: Mark Titled Borders for styling
        if (component.getBorder() instanceof STitledBorder) {
            Utils.optAttribute(device, "class", component.getStyle() + " STitledBorder");
        } else {
            Utils.optAttribute(device, "class", component.getStyle());
        }
        Utils.optAttribute(device, "style", Utils.generateCSSInlinePreferredSize(prefSize).toString());

        if (component instanceof LowLevelEventListener) {
            LowLevelEventListener lowLevelEventListener = (LowLevelEventListener) component;
            device.print(" eid=\"")
                    .print(lowLevelEventListener.getEncodedLowLevelEventId()).print("\"");
        }

        // Tooltip handling
        final String toolTip = component.getToolTipText();
        if (toolTip != null) {
            device.print(" onmouseover=\"return makeTrue(domTT_activate(this, event, 'content', '")
                    .print(toolTip)
                    .print("', 'predefined', 'default'));\"");
        }

        // Key bindings
        InputMap inputMap = component.getInputMap();
        if (inputMap != null  && inputMap.size() > 0) {
            if (false == (inputMap instanceof VersionedInputMap)) {
                log.debug("inputMap = " + inputMap);
                inputMap = new VersionedInputMap(inputMap);
                component.setInputMap(inputMap);
            }

            final VersionedInputMap versionedInputMap = (VersionedInputMap) inputMap;
            final Integer inputMapVersion = (Integer) component.getClientProperty("inputMapVersion");
            if (inputMapVersion == null || versionedInputMap.getVersion() != inputMapVersion.intValue()) {
                log.debug("inputMapVersion = " + inputMapVersion);
                InputMapScriptListener.install(component);
                component.putClientProperty("inputMapVersion", new Integer(versionedInputMap.getVersion()));
            }
        }

        // Component popup menu
        final SPopupMenu menu = component.getComponentPopupMenu();
        if (menu != null) {
            final String componentId = menu.getName();
            final String popupId = componentId + "_pop";
            device.print(" onContextMenu=\"javascript:return wpm_menuPopup(event, '");
            device.print(popupId);
            device.print("');\" onMouseDown=\"javascript:return wpm_menuPopup(event, '");
            device.print(popupId);
            device.print("');\"");
        }

        device.print(">"); // div

        // Special handling: Render title of STitledBorder
        if (component.getBorder() instanceof STitledBorder) {
            STitledBorder titledBorder = (STitledBorder) component.getBorder();
            device.print("<div class=\"STitledBorderLegend\" style=\"");
            titledBorder.getTitleAttributes().write(device);
            device.print("\">");
            device.print(titledBorder.getTitle());
            device.print("</div>");
        }

        component.fireRenderEvent(SComponent.START_RENDERING);
    }

    public void writeSuffix(Device device, SComponent component) throws IOException {
        component.fireRenderEvent(SComponent.DONE_RENDERING);
        device.print("</div></div>");
        Utils.printDebug(device, "<!-- /").print(component.getName()).print(" -->");
    }

}
