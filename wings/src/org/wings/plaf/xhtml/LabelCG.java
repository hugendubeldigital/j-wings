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

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.externalizer.ExternalizeManager;

public class LabelCG
    extends org.wings.plaf.AbstractComponentCG
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

        SBorder border = label.getBorder();
        String text = label.getText();
        int horizontalTextPosition = label.getHorizontalTextPosition();
        int verticalTextPosition = label.getVerticalTextPosition();


        if (label.getIcon() == null )
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

        SIcon icon = label.isEnabled() ? label.getIcon() :
            label.getDisabledIcon();

        if ( icon != null ) {
            d.append("<img src=\"").
                append(icon.getURL()).
                append("\"");
            if (align != null)
                d.append(" valign=\"").append(align).append("\"");
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
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
