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

package org.wings.plaf.xhtml;

import java.io.IOException;

import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.externalizer.ExternalizeManager;

public class LabelCG
    extends org.wings.plaf.AbstractCG
    implements org.wings.plaf.LabelCG, SConstants
{
    private final static String propertyPrefix = "Label";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent component)
        throws IOException
    {
        SLabel label = (SLabel)component;

        Icon icon = label.getIcon();
        String text = label.getText();
        SBorder border = label.getBorder();
        int horizontalTextPosition = label.getHorizontalTextPosition();
        int verticalTextPosition = label.getVerticalTextPosition();
        String iconAddress = label.getIconAddress();

        Utils.writeBorderPrefix(d, border);

        if (icon == null && iconAddress == null)
            writeText(d, label);
        else if (text == null)
            writeIcon(d, label);
        else {
            // Hauptsache, es funktioniert !!!
            if (verticalTextPosition == TOP && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"top\">");
                writeText(d, label);
                d.append("</td><td>");
                writeIcon(d, label);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td>");
                writeText(d, label);
                d.append("</td><td>");
                writeIcon(d, label);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"bottom\">");
                writeText(d, label);
                d.append("</td><td>");
                writeIcon(d, label);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeText(d, label);
                d.append("</td></tr><tr><td>");
                writeIcon(d, label);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeText(d, label);
                d.append("</td><td>");
                writeIcon(d, label);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeIcon(d, label);
                d.append("</td></tr><tr><td>");
                writeText(d, label);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == RIGHT) {
                writeIcon(d, label, "top");
                writeText(d, label);
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == RIGHT) {
                writeIcon(d, label, "middle");
                writeText(d, label);
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == RIGHT) {
                writeIcon(d, label, "bottom");
                writeText(d, label);
            } else {
                writeText(d, label);
                writeIcon(d, label, "middle");
            }
        }

        Utils.writeBorderPostfix(d, border);
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
                text = Utils.escapeSpecialChars(text);
            d.append(text);
            
            if (noBreak)
                d.append("</nobr>");
        }
    }

    protected void writeIcon(Device d, SLabel label)
        throws IOException
    {
        writeIcon(d, label, null);
    }

    protected void writeIcon(Device d, SLabel label, String align)
        throws IOException
    {
        String text = label.getText();
        String iconAddress = label.getIconAddress();
        String disabledIconAddress = label.getDisabledIconAddress();
        Icon icon = label.getIcon();
        Icon disabledIcon = label.getDisabledIcon();
        String tooltip = label.getToolTipText();

        String iAdr = null;
        Icon ic = null;

        if (!label.isEnabled()){
            if (disabledIconAddress != null)
                iAdr = disabledIconAddress;
            else if (disabledIcon != null)
                ic = disabledIcon;

            if (ic == null)
                if (iconAddress != null)
                    iAdr = iconAddress;
                else if (icon != null)
                    ic = icon;
        } else {
            if (iconAddress != null)
                iAdr = iconAddress.toString();
            else if (icon != null)
                ic = icon;
        }

        if (ic != null) {
            ExternalizeManager ext = label.getExternalizeManager();
            if (ext != null) {
                try {
                    iAdr = ext.externalize(ic);
                } catch (java.io.IOException e) {
                    // dann eben nicht !!
                    e.printStackTrace();
                }
            }
        }

        if (iAdr != null) {
            d.append("<img src=\"").append(iAdr).append("\"");
            if (align != null)
                d.append(" valign=\"").append(align).append("\"");
            if (ic != null) {
                d.append(" width=\"").append(ic.getIconWidth()).append("\"");
                d.append(" height=\"").append(ic.getIconHeight()).append("\"");
            }
            d.append(" border=\"0\"");

            if (tooltip != null) {
                d.append(" alt=\"").append(tooltip).append("\"");
            } else if (text != null) {
                d.append(" alt=\"").append(text).append("\"");
            }

            d.append(" />");
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
