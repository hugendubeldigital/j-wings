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
import org.wings.*;
import org.wings.session.SessionManager;
import org.wings.session.BrowserType;
import org.wings.io.Device;

import java.io.IOException;

/**
 * CG for a scrollbar.
 *
 * @author holger
 */
public class ScrollBarCG
        extends org.wings.plaf.css.AbstractComponentCG
        implements org.wings.plaf.ScrollBarCG
{
    private final static transient Log log = LogFactory.getLog(ScrollBarCG.class);
    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int FORWARD_BLOCK = 2;
    public static final int BACKWARD_BLOCK = 3;
    public static final int FIRST = 4;
    public static final int LAST = 5;
    private final static SIcon[][][] DEFAULT_ICONS = new SIcon[2][6][2];

    static {
        String[] postfixes = new String[6];
        String[] prefixes = new String[6];
        for (int orientation = 0; orientation < 2; orientation++) {
            prefixes[BACKWARD] = "";
            prefixes[FORWARD] = "";
            prefixes[FIRST] = "Margin";
            prefixes[LAST] = "Margin";
            prefixes[FORWARD_BLOCK] = "Block";
            prefixes[BACKWARD_BLOCK] = "Block";
            if (orientation == SConstants.VERTICAL) {
                postfixes[BACKWARD] = "Up";
                postfixes[FORWARD] = "Down";
                postfixes[FIRST] = "Up";
                postfixes[LAST] = "Down";
                postfixes[BACKWARD_BLOCK] = "Up";
                postfixes[FORWARD_BLOCK] = "Down";
            } else {
                postfixes[BACKWARD] = "Left";
                postfixes[FORWARD] = "Right";
                postfixes[FIRST] = "Left";
                postfixes[LAST] = "Right";
                postfixes[BACKWARD_BLOCK] = "Left";
                postfixes[FORWARD_BLOCK] = "Right";
            }

            for (int direction = 0; direction < postfixes.length; direction++) {
                DEFAULT_ICONS[orientation][direction][0] =
                        new SResourceIcon("org/wings/icons/"
                        + prefixes[direction]
                        + "Scroll"
                        + postfixes[direction] + ".gif");
                DEFAULT_ICONS[orientation][direction][1] =
                        new SResourceIcon("org/wings/icons/Disabled"
                        + prefixes[direction]
                        + "Scroll"
                        + postfixes[direction] + ".gif");
            }
        }
    }

    public void writeContent(Device d, SComponent c)
            throws IOException {
        log.debug("write = " + c);
        SScrollBar sb = (SScrollBar) c;

        if (sb.getOrientation() == SConstants.VERTICAL)
            writeVerticalScrollbar(d, sb);
        else
            writeHorizontalScrollbar(d, sb);
    }

    private void writeVerticalScrollbar(Device d, SScrollBar sb) throws IOException {
        final int value = sb.getValue();
        final int blockIncrement = sb.getBlockIncrement();
        final int extent = sb.getExtent();
        final int minimum = sb.getMinimum();
        final int maximum = sb.getMaximum();
        final int last = maximum - extent;

        // Workaround -- enable this renderer for the MSIE.
        // MSIE stretches the rows to the length of the universe if we advise the rows to be 100%/1% height
        // Gecko fails to stretch if we do not advise the preffered sizes.
        final boolean isMsIEBrowser = BrowserType.IE.equals(SessionManager.getSession().getUserAgent().getBrowserType());
        final String rowHeightExpanded = isMsIEBrowser ? "" : " height=\"100%\"";
        final String rowHeightFlattened = isMsIEBrowser ? "" : " height=\"1%\"";
        // Regarding table height it is totally inveser. I love 'em.
        final String tableHeight = isMsIEBrowser ? " height=\"100%\"" : "";

        d.print("<table orientation=\"vertical\" class=\"SLayout\"").print(tableHeight).print("><tbody>\n")
                .print("<tr").print(rowHeightFlattened).print(">\n")
                .print("<td height=\"1%\" class=\"SLayout\"><table class=\"SLayout\" area=\"buttons\"><tbody>\n");

        d.print("<tr><td class=\"SLayout\">");
        writeButton(d, sb, DEFAULT_ICONS[SConstants.VERTICAL][FIRST][0], "" + minimum);
        d.print("</td></tr>\n");
        d.print("<tr><td class=\"SLayout\">");
        writeButton(d, sb, DEFAULT_ICONS[SConstants.VERTICAL][BACKWARD_BLOCK][0], "" + (Math.max(minimum, value - blockIncrement)));
        d.print("</td></tr>\n");
        d.print("<tr><td class=\"SLayout\">");
        writeButton(d, sb, DEFAULT_ICONS[SConstants.VERTICAL][BACKWARD][0], "" + (value - 1));
        d.print("</td></tr>\n");

        d.print("</tbody></table></td>\n")
                .print("</tr>\n")
                .print("<tr").print(rowHeightExpanded).print(">\n")
                .print("<td class=\"SLayout\"><table area=\"slider\" height=\"100%\" class=\"SLayout\"><tbody>\n");

        int range = maximum - minimum;
        int iconWidth = DEFAULT_ICONS[SConstants.VERTICAL][FIRST][0].getIconWidth();
        verticalArea(d, "#eeeeff", value * 100 / range, iconWidth);
        verticalArea(d, "#cccccc", extent * 100 / range, iconWidth);
        verticalArea(d, "#eeeeff", (range - value - extent) * 100 / range, iconWidth);

        d.print("</tbody></table></td>\n")
                .print("</tr>\n")
                .print("<tr").print(rowHeightFlattened).print(">\n")
                .print("<td height=\"1%\" class=\"SLayout\"><table area=\"buttons\" class=\"SLayout\"><tbody>\n");

        d.print("<tr><td class=\"SLayout\">");
        writeButton(d, sb, DEFAULT_ICONS[SConstants.VERTICAL][FORWARD][0], "" + (value + 1));
        d.print("</td></tr>\n");
        d.print("<tr><td class=\"SLayout\">");
        writeButton(d, sb, DEFAULT_ICONS[SConstants.VERTICAL][FORWARD_BLOCK][0], "" + (Math.min(last, value + blockIncrement)));
        d.print("</td></tr>\n");
        d.print("<tr><td class=\"SLayout\">");
        writeButton(d, sb, DEFAULT_ICONS[SConstants.VERTICAL][LAST][0], "" + last);
        d.print("</td></tr>\n");

        d.print("</tbody></table></td>\n")
                .print("</tr>\n")
                .print("</tbody></table>");
    }

    private void verticalArea(Device d, String s, int v, int iconWidth) throws IOException {
        d.print("<tr><td class=\"SLayout\" style=\"background-color: ");
        d.print(s);
        d.print("\" height=\"");
        d.print(v + "%");
        d.print("\" width=\"");
        d.print(iconWidth);
        d.print("\"></td></tr>\n");
    }

    private void writeHorizontalScrollbar(Device d, SScrollBar sb) throws IOException {
        int value = sb.getValue();
        int blockIncrement = sb.getBlockIncrement();
        int extent = sb.getExtent();
        int minimum = sb.getMinimum();
        int maximum = sb.getMaximum();
        int last = maximum - extent;
        boolean backEnabled = value > minimum;
        boolean forwardEnabled = value < maximum - extent;

        d.print("<table orientation=\"horizontal\" class=\"SLayout\"><tbody><tr>\n")
                .print("<td width=\"1%\" class=\"SLayout\"><table area=\"buttons\" class=\"SLayout\"><tbody><tr>\n");

        d.print("<td class=\"SLayout\">");
        writeButton(d, sb, DEFAULT_ICONS[SConstants.HORIZONTAL][FIRST][0], "" + minimum);
        d.print("</td>\n");
        d.print("<td class=\"SLayout\">");
        writeButton(d, sb, DEFAULT_ICONS[SConstants.HORIZONTAL][BACKWARD_BLOCK][0], "" + (Math.max(minimum, value - blockIncrement)));
        d.print("</td>\n");
        d.print("<td class=\"SLayout\">");
        writeButton(d, sb, DEFAULT_ICONS[SConstants.HORIZONTAL][BACKWARD][0], "" + (value - 1));
        d.print("</td>\n");

        d.print("</tr></tbody></table></td>\n")
                .print("<td width=\"100%\" class=\"SLayout\"><table area=\"slider\" width=\"100%\" class=\"SLayout\"><tbody><tr>\n");

        int range = maximum - minimum;
        int iconHeight = DEFAULT_ICONS[SConstants.HORIZONTAL][FIRST][0].getIconHeight();
        horizontalArea(d, "#eeeeff", value * 100 / range, iconHeight);
        horizontalArea(d, "#cccccc", extent * 100 / range, iconHeight);
        horizontalArea(d, "#eeeeff", (range - value - extent) * 100 / range, iconHeight);

        d.print("</tr></tbody></table></td>\n")
                .print("<td width=\"1%\" class=\"SLayout\"><table area=\"buttons\"><tbody><tr>\n");

        d.print("<td class=\"SLayout\">");
        writeButton(d, sb, DEFAULT_ICONS[SConstants.HORIZONTAL][FORWARD][0], "" + (value + 1));
        d.print("</td>\n");
        d.print("<td class=\"SLayout\">");
        writeButton(d, sb, DEFAULT_ICONS[SConstants.HORIZONTAL][FORWARD_BLOCK][0], "" + (Math.min(last, value + blockIncrement)));
        d.print("</td>\n");
        d.print("<td class=\"SLayout\">");
        writeButton(d, sb, DEFAULT_ICONS[SConstants.HORIZONTAL][LAST][0], "" + last);
        d.print("</td>\n");

        d.print("</tr></tbody></table></td>\n")
                .print("</tr></tbody></table>");
    }

    private void horizontalArea(Device d, String s, int v, int iconHeight) throws IOException {
        d.print("<td class=\"SLayout\" style=\"background-color: ");
        d.print(s);
        d.print("\" width=\"");
        d.print(v + "%");
        d.print("\" height=\"");
        d.print(iconHeight);
        d.print("\"></td>\n");
    }

    private void writeButton(Device device, SScrollBar scrollBar, SIcon icon, String event) throws IOException {
        boolean childSelectorWorkaround = !scrollBar.getSession().getUserAgent().supportsCssChildSelector();
        boolean showAsFormComponent = scrollBar.getShowAsFormComponent();

        if (showAsFormComponent) {
            writeButtonStart(device, scrollBar, event);
            device.print(" type=\"submit\" name=\"")
                    .print(Utils.event(scrollBar))
                    .print("\" value=\"")
                    .print(event)
                    .print("\"");
        } else {
            device.print("<a href=\"")
                    .print(scrollBar.getRequestURL()
                    .addParameter(Utils.event(scrollBar) + "=" + event).toString())
                    .print("\"");
        }
        device.print(">");

        device.print("<img");
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());
        device.print(" alt=\"");
        device.print(icon.getIconTitle());
        device.print("\"/>");

        if (showAsFormComponent)
            device.print("</button>\n");
        else
            device.print("</a>\n");
    }

    /**
     * @param device
     * @param event 
     * @param scrollBar 
     * @throws IOException
     */
    protected void writeButtonStart(Device device, SScrollBar scrollBar, String event) throws IOException {
        device.print("<button");
    }
}
