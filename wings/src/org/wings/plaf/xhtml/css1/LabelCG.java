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

package org.wings.plaf.xhtml.css1;

import java.io.IOException;
import java.awt.Color;
import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.*;

public final class LabelCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.LabelCG, SConstants
{
    public void write(Device d, SComponent component)
        throws IOException
    {
        SLabel label = (SLabel)component;

        SBorder border = label.getBorder();
        String text = label.getText();
        int horizontalTextPosition = label.getHorizontalTextPosition();
        int verticalTextPosition = label.getVerticalTextPosition();
        Style style = label.getStyle();
        String attributeClass = (label.getAttributes().size() > 0) ? ("_" + label.getUnifiedId()) : null;

        if (label.getIcon() == null) {
            int spans = 0;
            if (style != null) {
                d.append("<span");
                style.write(d);
                d.append(">");
                spans++;
            }
            if (attributeClass != null) {
                d.append("<span class=\"");
                d.append(attributeClass);
                d.append("\">");
                spans++;
            }
            writeText(d, label);
            if (spans == 1)
                d.append("</span>");
            else if (spans == 2)
                d.append("</span></span>");
        }
        else if (text == null) {
            int spans = 0;
            if (style != null) {
                d.append("<span");
                style.write(d);
                d.append(">");
                spans++;
            }
            if (attributeClass != null) {
                d.append("<span class=\"");
                d.append(attributeClass);
                d.append("\">");
                spans++;
            }
            writeIcon(d, label);
            if (spans == 1)
                d.append("</span>");
            else if (spans == 2)
                d.append("</span></span>");
        }
        else {
            d.append("<table");
            if (style != null)
                style.write(d);
            d.append(">\n");

            if (verticalTextPosition == TOP) {
                d.append("<tr>");
                writeTD(d, attributeClass);

                if (horizontalTextPosition == LEFT) {
                    writeText(d, label);
                }

                d.append("</td>");
                writeTD(d, attributeClass);

                if (horizontalTextPosition == CENTER) {
                    writeText(d, label);
                }

                d.append("</td>");
                writeTD(d, attributeClass);

                if (horizontalTextPosition == RIGHT) {
                    writeText(d,label);
                }
                d.append("</td></tr>");
            }

            // we always have a center row, since we have to write the icon
            if (verticalTextPosition == CENTER) {
                d.append("<tr>");
                writeTD(d, attributeClass);

                if (horizontalTextPosition == LEFT) {
                    writeText(d,label);
                }

                d.append("</td>");
                writeTD(d, attributeClass);

                if (horizontalTextPosition == CENTER) {
                    writeText(d,label);
                }
                // the rendered icon in the center
                writeIcon(d, label);

                d.append("</td>");
                writeTD(d, attributeClass);

                if (horizontalTextPosition == RIGHT) {
                    writeText(d,label);
                }
                d.append("</td></tr>");
            }
            else {
                d.append("<tr><td></td>");
                writeTD(d, attributeClass);

                writeIcon(d, label);

                d.append("</td><td></td></tr>");
            }

            // ..
            if (verticalTextPosition == BOTTOM) {
                d.append("<tr>");
                writeTD(d, attributeClass);

                if (horizontalTextPosition == LEFT) {
                    writeText(d,label);
                }

                d.append("</td>");
                writeTD(d, attributeClass);

                if (horizontalTextPosition == CENTER) {
                    writeText(d,label);
                }

                d.append("</td>");
                writeTD(d, attributeClass);

                if (horizontalTextPosition == RIGHT) {
                    writeText(d,label);
                }
                d.append("</td></tr>");
            }
            d.append("</table>");
        }
    }

    protected void writeTD(Device d, String attributeClass) {
        if (attributeClass == null)
            d.append("<td>");
        else {
            d.append("<td class=\"");
            d.append(attributeClass);
            d.append("\">");
        }
    }

    protected void writeText(Device d, SLabel label)
        throws IOException
    {
        String text = label.getText();
        if (text != null && text.trim().length() > 0) {
            boolean noBreak = label.isNoBreak();
            boolean escape = label.isEscapeSpecialChars();

            if (noBreak)
                d.append("<nobr>");

            if (escape)
                text = org.wings.plaf.xhtml.Utils.escapeSpecialChars(text);
            d.append(text);
            
            if (noBreak)
                d.append("</nobr>");
        }
    }

    protected void writeIcon(Device d, SLabel label)
        throws IOException
    {
        SIcon icon = label.isEnabled() ? label.getIcon() :
            label.getDisabledIcon();

        if (icon != null) {
            d.append("<img src=\"").
                append(icon.getURL()).
                append("\"");
            if ( icon.getIconWidth() > 0)
                d.append(" width=\"").append(icon.getIconWidth()).append("\"");
            if ( icon.getIconHeight() > 0)
                d.append(" height=\"").append(icon.getIconHeight()).append("\"");
            d.append(" border=\"0\"");

            String text = label.getText();
            String tooltip = label.getToolTipText();

            if (tooltip != null) {
                d.append(" alt=\"").append(tooltip).append("\"");
            } else if (text != null) {
                d.append(" alt=\"").append(text).append("\"");
            }

            d.append(" />");
        }
    }

    protected void writeAttribute(Device d, String name, String value) {
        if (value == null)
            return;
        else {
            d.append(" ");
            d.append(name);
            d.append("=\"");
            d.append(value);
            d.append("\"");
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
