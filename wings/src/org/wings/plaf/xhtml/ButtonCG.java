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
import org.wings.util.*;
import org.wings.externalizer.ExternalizeManager;

public class ButtonCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.ButtonCG, SConstants
{
    private final static String propertyPrefix = "Button";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }
    
    public void write(Device d, SComponent c)
        throws IOException
    {
        SButton button = (SButton)c;
        SBorder border = button.getBorder();

        Utils.writeBorderPrefix(d, border);

        if (!button.getShowAsFormComponent() || !button.isEnabled())
            writeAnchorButton(d, button);
        else
            writeFormButton(d, button);

        Utils.writeBorderPostfix(d, border);
    }

    protected void writeAnchorButton(Device d, SButton button)
        throws IOException
    {
        SIcon icon = button.getIcon();
        String text = button.getText();
        int horizontalTextPosition = button.getHorizontalTextPosition();
        int verticalTextPosition = button.getVerticalTextPosition();

        if (icon == null )
            writeAnchorText(d, button);
        else if (text == null)
            writeAnchorIcon(d, button);
        else {
            // Hauptsache, es funktioniert !!!
            if (verticalTextPosition == TOP && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"top\">");
                writeAnchorText(d, button);
                d.append("</td><td>");
                writeAnchorIcon(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td>");
                writeAnchorText(d, button);
                d.append("</td><td>");
                writeAnchorIcon(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"bottom\">");
                writeAnchorText(d, button);
                d.append("</td><td>");
                writeAnchorIcon(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeAnchorText(d, button);
                d.append("</td></tr><tr><td>");
                writeAnchorIcon(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeAnchorText(d, button);
                d.append("</td><td>");
                writeAnchorIcon(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeAnchorIcon(d, button);
                d.append("</td></tr><tr><td>");
                writeAnchorText(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == RIGHT) {
                writeAnchorIcon(d, button, "top");
                writeAnchorText(d, button);
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == RIGHT) {
                writeAnchorIcon(d, button, "middle");
                writeAnchorText(d, button);
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == RIGHT) {
                writeAnchorIcon(d, button, "bottom");
                writeAnchorText(d, button);
            } else {
                writeAnchorText(d, button);
                writeAnchorIcon(d, button, "middle");
            }
        }
    }

    protected void writeAnchorText(Device d, SButton button)
        throws IOException
    {
        String text = button.getText();
        if (text != null && text.trim().length() > 0) {
            writeAnchorPrefix(d, button);
            writeAnchorBody(d, button);
            writeAnchorPostfix(d, button);
        }
    }

    protected void writeAnchorIcon(Device d, SButton button)
        throws IOException
    {
        writeAnchorIcon(d, button, null);
    }

    protected void writeAnchorIcon(Device d, SButton button, String align)
        throws IOException
    {
        SIcon icon = button.isEnabled() 
            ? button.getIcon() 
            : button.getDisabledIcon();

        if ( icon != null ) {
            writeAnchorPrefix(d, button);
            d.append("<img src=\"").append(icon.getURL()).append("\"");
            if (align != null)
                d.append(" align=\"").append(align).append("\"");

            if ( icon.getIconWidth() > 0)
                d.append(" width=\"").append(icon.getIconWidth()).append("\"");

            if ( icon.getIconHeight() > 0)
                d.append(" height=\"").append(icon.getIconHeight()).append("\"");

            d.append(" border=\"0\"");

            String text = button.getText();
            String tooltip = button.getToolTipText();


            if (tooltip != null) {
                d.append(" alt=\"").append(tooltip).append("\"");
            } else if (text != null) {
                d.append(" alt=\"").append(text).append("\"");
            }

            d.append(" />");

            writeAnchorPostfix(d, button);
        }
    }

    protected void writeAnchorAddress(Device d, SButton button) 
    throws IOException {
        RequestURL addr = button.getRequestURL();
        addr.addParameter(button.getNamePrefix() + "=" + button.getUnifiedId() + SConstants.UID_DIVIDER);
        addr.write(d);
    }

    protected void writeAnchorPrefix(Device d, SButton button)
        throws IOException
    {
        String tooltip = button.getToolTipText();
		java.awt.Color c;
        
        if (button.isEnabled()) 
        {
            d.append("<a href=\"");
            writeAnchorAddress(d, button);
            d.append("\"");

            if (button.getRealTarget() != null)
                d.append(" target=\"").append(button.getRealTarget()).append("\"");

            if (tooltip != null)
                d.append(" title=\"").append(tooltip).append("\"");

            d.append(">");
        }
    }

    protected void writeAnchorBody(Device d, SButton button)
        throws IOException
    {
        String text = button.getText();
        boolean noBreak = button.isNoBreak();

        if (text == null)
            text = "";
        d.append((noBreak) ? StringUtil.replace(text, " ", "&nbsp;") : text);
    }

    protected void writeAnchorPostfix(Device d, SButton button)
        throws IOException
    {
        if (button.isEnabled()) {
            d.append("</a>");
        }

    }

    protected void writeFormButton(Device d, SButton button)
        throws IOException
    {
        SIcon icon = button.getIcon();
        String text = button.getText();
        int horizontalTextPosition = button.getHorizontalTextPosition();
        int verticalTextPosition = button.getVerticalTextPosition();

        if (icon == null )
            writeFormText(d, button);
        else if (text == null)
            writeFormIcon(d, button);
        else {
            // Hauptsache, es funktioniert !!!
            if (verticalTextPosition == TOP && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"top\">");
                writeFormText(d, button);
                d.append("</td><td>");
                writeFormIcon(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td>");
                writeFormText(d, button);
                d.append("</td><td>");
                writeFormIcon(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"bottom\">");
                writeFormText(d, button);
                d.append("</td><td>");
                writeFormIcon(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeFormText(d, button);
                d.append("</td></tr><tr><td>");
                writeFormIcon(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeFormText(d, button);
                d.append("</td><td>");
                writeFormIcon(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeFormIcon(d, button);
                d.append("</td></tr><tr><td>");
                writeFormText(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == RIGHT) {
                d.append("<table><tr><td valign=\"top\">");
                writeFormIcon(d, button);
                d.append("</td><td align=\"right\">");
                writeFormText(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == RIGHT) {
                d.append("<table><tr><td>");
                writeFormIcon(d, button);
                d.append("</td><td align=\"right\">");
                writeFormText(d, button);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == RIGHT) {
                d.append("<table><tr><td valign=\"bottom\">");
                writeFormIcon(d, button);
                d.append("</td></tr><tr><td align=\"right\">");
                writeFormText(d, button);
                d.append("</td></tr></table>");
            } else {
                d.append("<table><tr><td>");
                writeFormIcon(d, button);
                d.append("</td></tr><tr><td>");
                writeFormText(d, button);
                d.append("</td></tr></table>");
            }
        }
    }

    protected void writeFormText(Device d, SButton button)
        throws IOException
    {
        String text = button.getText();
        if (text != null && text.trim().length() > 0) {
            writeFormPrefix(d, button);
            writeFormBody(d, button);
            writeFormPostfix(d, button);
        }
    }

    protected void writeFormIcon(Device d, SButton button)
        throws IOException
    {

        SIcon icon = button.isEnabled() ? button.getIcon() :
            button.getDisabledIcon();

        if (icon != null) {

            d.append("<input type=\"image\"");
            d.append(" src=\"").
                append(icon.getURL()).
                append("\"");

            if ( icon.getIconWidth() > 0)
                d.append(" width=\"").append(icon.getIconWidth()).append("\"");

            if ( icon.getIconHeight() > 0)
                d.append(" height=\"").append(icon.getIconHeight()).append("\"");

            d.append(" border=\"0\"");

            d.append(" name=\"").append(button.getNamePrefix()).append("\"");

            String text = button.getText();
            d.append(" value=\"").append(text).append("\"");
            
            String tooltip = button.getToolTipText();
            if (tooltip != null) {
                d.append(" alt=\"").append(tooltip).append("\"");
                d.append(" title=\"").append(tooltip).append("\"");
            } else if (text != null) {
                d.append(" alt=\"").append(text).append("\"");
                d.append(" title=\"").append(text).append("\"");
            }
            d.append(" />");
        }
    }

    protected void writeFormPrefix(Device d, SButton button)
        throws IOException
    {
        d.append("<input type=\"submit\"");
    }

    protected void writeFormBody(Device d, SButton button)
        throws IOException
    {
        String text = button.getText();

        if (button.isEnabled())
            d.append(" name=\"").
                append(button.getNamePrefix()).append("\"");
        if (text != null)
            d.append(" value=\"").
                append(text).
                append("\"");
    }

    protected void writeFormPostfix(Device d, SButton button)
        throws IOException
    {
        d.append(" />");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
