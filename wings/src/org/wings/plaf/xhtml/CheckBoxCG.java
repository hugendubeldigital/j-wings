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

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.util.*;

public class CheckBoxCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.CheckBoxCG, SConstants
{
    private final static String propertyPrefix = "CheckBox";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SCheckBox checkBox = (SCheckBox)c;
        SBorder border = checkBox.getBorder();
        boolean selected = checkBox.isSelected();
        boolean enabled = checkBox.isEnabled();
        boolean anchor = !checkBox.getShowAsFormComponent();

        Utils.writeBorderPrefix(d, border);

        if (anchor || !enabled)
            writeAnchorButton(d, checkBox);
        else
            writeFormButton(d, checkBox);

        if (enabled && !anchor)
            writeHiddenComponent(d, checkBox);

        Utils.writeBorderPostfix(d, border);
    }

    protected void writeHiddenComponent(Device d, SCheckBox checkBox)
        throws IOException
    {
        Utils.writeHiddenComponent(d, checkBox.getNamePrefix(),
                                   checkBox.getUnifiedId() + UID_DIVIDER + "0");
    }

    protected void writeAnchorButton(Device d, SCheckBox checkBox)
        throws IOException
    {
        SIcon icon = checkBox.isSelected() ? checkBox.getSelectedIcon() :
            checkBox.getIcon();

        String text = checkBox.getText();
        int horizontalTextPosition = checkBox.getHorizontalTextPosition();
        int verticalTextPosition = checkBox.getVerticalTextPosition();

        if (icon == null )
            writeAnchorText(d, checkBox);
        else if (text == null)
            writeAnchorIcon(d, checkBox);
        else {
            // Hauptsache, es funktioniert !!!
            if (verticalTextPosition == NO_ALIGN || horizontalTextPosition == NO_ALIGN) {
                writeAnchorIcon(d, checkBox);
                writeAnchorText(d, checkBox);
            } else if (verticalTextPosition == TOP && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"top\">");
                writeAnchorText(d, checkBox);
                d.append("</td><td>");
                writeAnchorIcon(d, checkBox);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td>");
                writeAnchorText(d, checkBox);
                d.append("</td><td>");
                writeAnchorIcon(d, checkBox);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"bottom\">");
                writeAnchorText(d, checkBox);
                d.append("</td><td>");
                writeAnchorIcon(d, checkBox);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeAnchorText(d, checkBox);
                d.append("</td></tr><tr><td>");
                writeAnchorIcon(d, checkBox);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeAnchorText(d, checkBox);
                d.append("</td><td>");
                writeAnchorIcon(d, checkBox);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeAnchorIcon(d, checkBox);
                d.append("</td></tr><tr><td>");
                writeAnchorText(d, checkBox);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == RIGHT) {
                writeAnchorIcon(d, checkBox, "top");
                writeAnchorText(d, checkBox);
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == RIGHT) {
                writeAnchorIcon(d, checkBox, "middle");
                writeAnchorText(d, checkBox);
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == RIGHT) {
                writeAnchorIcon(d, checkBox, "bottom");
                writeAnchorText(d, checkBox);
            } else {
                writeAnchorText(d, checkBox);
                writeAnchorIcon(d, checkBox, "middle");
            }
        }
    }

    protected void writeAnchorText(Device d, SCheckBox checkBox)
        throws IOException
    {
        String text = checkBox.getText();
        if (text != null && text.trim().length() > 0) {
            writeAnchorPrefix(d, checkBox);
            writeAnchorBody(d, checkBox);
            writeAnchorPostfix(d, checkBox);
        }
    }

    protected void writeAnchorIcon(Device d, SCheckBox checkBox)
        throws IOException
    {
        writeAnchorIcon(d, checkBox, null);
    }

    protected void writeAnchorIcon(Device d, SCheckBox checkBox, String align)
        throws IOException
    {
        SIcon icon = null;
        SIcon disabledIcon = null;
        if (checkBox.isSelected()) {
            icon = checkBox.getSelectedIcon();
            disabledIcon = checkBox.getDisabledSelectedIcon();
        }
        else {
            icon = checkBox.getIcon();
            disabledIcon = checkBox.getDisabledIcon();
        }

        SIcon actualIcon = null;

        if (!checkBox.isEnabled()){
            actualIcon = disabledIcon;
        } else {
            actualIcon = icon;
        }

        if (icon != null) {
            writeAnchorPrefix(d, checkBox);

            d.append("<img src=\"").
                append(actualIcon.getURL()).
                append("\"");

            if (align != null)
                d.append(" align=\"").append(align).append("\"");

            if ( actualIcon.getIconWidth() > 0)
                d.append(" width=\"").append(actualIcon.getIconWidth()).append("\"");

            if ( actualIcon.getIconHeight() > 0)
                d.append(" height=\"").append(actualIcon.getIconHeight()).append("\"");

            d.append(" border=\"0\"");

            String tooltip = checkBox.getToolTipText();
            String text = checkBox.getText();

            if (tooltip != null) {
                d.append(" alt=\"").append(tooltip).append("\"");
            } else if (text != null) {
                d.append(" alt=\"").append(text).append("\"");
            }

            d.append(" />");

            writeAnchorPostfix(d, checkBox);
        }
    }

    protected void writeAnchorAddress(Device d, SCheckBox checkBox) 
    throws IOException {
        RequestURL addr = checkBox.getRequestURL();
        addr.addParameter(checkBox.getNamePrefix() + "=" + checkBox.getUnifiedId() + SConstants.UID_DIVIDER);
        addr.write(d);
    }

    protected void writeAnchorPrefix(Device d, SCheckBox checkBox)
        throws IOException
    {
        String tooltip = checkBox.getToolTipText();

        if (checkBox.isEnabled()) {
            d.append("<a href=\"");
            writeAnchorAddress(d, checkBox);
            d.append("\"");
            
            if (checkBox.getRealTarget() != null)
                d.append(" target=\"").append(checkBox.getRealTarget()).append("\"");

            if (tooltip != null)
                d.append(" title=\"").append(tooltip).append("\"");

            d.append(">");
        }
    }

    protected void writeAnchorBody(Device d, SCheckBox checkBox)
        throws IOException
    {
        String text = checkBox.getText();
        boolean noBreak = checkBox.isNoBreak();

        if (text == null)
            text = "";
        d.append((noBreak) ? StringUtil.replace(text, " ", "&nbsp;") : text);
    }

    protected void writeAnchorPostfix(Device d, SCheckBox checkBox)
        throws IOException
    {
        if (checkBox.isEnabled()) {
            d.append("</a>");
        }
    }

    protected void writeFormButton(Device d, SCheckBox checkBox)
        throws IOException
    {
        SIcon icon = null;
        if (checkBox.isSelected()) {
            icon = checkBox.getSelectedIcon();
        } else {
            icon = checkBox.getIcon();
        }

        String text = checkBox.getText();
        String id = "_" + checkBox.getUnifiedId();
        int horizontalTextPosition = checkBox.getHorizontalTextPosition();
        int verticalTextPosition = checkBox.getVerticalTextPosition();

        if (icon == null ) {
            writeFormText(d, checkBox, id);
        } else if (text == null) {
            writeFormIcon(d, checkBox, id);
        } else {
            // Hauptsache, es funktioniert !!!
            if (verticalTextPosition == NO_ALIGN || horizontalTextPosition == NO_ALIGN) {
                writeFormIcon(d, checkBox, id);
                writeFormText(d, checkBox, id);
            } else if (verticalTextPosition == TOP && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"top\">");
                writeFormText(d, checkBox, id);
                d.append("</td><td>");
                writeFormIcon(d, checkBox, id);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td>");
                writeFormText(d, checkBox, id);
                d.append("</td><td>");
                writeFormIcon(d, checkBox, id);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"bottom\">");
                writeFormText(d, checkBox, id);
                d.append("</td><td>");
                writeFormIcon(d, checkBox, id);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeFormText(d, checkBox, id);
                d.append("</td></tr><tr><td>");
                writeFormIcon(d, checkBox, id);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeFormText(d, checkBox, id);
                d.append("</td><td>");
                writeFormIcon(d, checkBox, id);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeFormIcon(d, checkBox, id);
                d.append("</td></tr><tr><td>");
                writeFormText(d, checkBox, id);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == RIGHT) {
                d.append("<table><tr><td valign=\"top\">");
                writeFormIcon(d, checkBox, id);
                d.append("</td><td align=\"right\">");
                writeFormText(d, checkBox, id);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == RIGHT) {
                d.append("<table><tr><td>");
                writeFormIcon(d, checkBox, id);
                d.append("</td><td align=\"right\">");
                writeFormText(d, checkBox, id);
                d.append("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == RIGHT) {
                d.append("<table><tr><td valign=\"bottom\">");
                writeFormIcon(d, checkBox, id);
                d.append("</td></tr><tr><td align=\"right\">");
                writeFormText(d, checkBox, id);
                d.append("</td></tr></table>");
            } else {
                d.append("<table><tr><td>");
                writeFormIcon(d, checkBox, id);
                d.append("</td></tr><tr><td>");
                writeFormText(d, checkBox, id);
                d.append("</td></tr></table>");
            }
        }
    }

    protected void writeFormText(Device d, SCheckBox checkBox, String id)
        throws IOException
    {
        d.append("<label for=\"").append(id).append("\">");
        d.append(checkBox.getText());
        d.append("</label>");
    }

    protected void writeFormIcon(Device d, SCheckBox checkBox, String id)
        throws IOException
    {
        writeFormPrefix(d, checkBox, id);
        writeFormBody(d, checkBox);
        writeFormPostfix(d, checkBox);
    }

    protected void writeFormPrefix(Device d, SCheckBox checkBox, String id)
        throws IOException
    {
        d.append("<input type=\"");
        d.append(checkBox.getType());
        d.append("\" id=\"").append(id).append("\"");
    }

    protected void writeFormBody(Device d, SCheckBox checkBox)
        throws IOException
    {
        if (checkBox.isEnabled())
            d.append(" name=\"").
                append(checkBox.getNamePrefix()).append("\"");

        d.append(" value=\"").
            append(checkBox.getUnifiedId() + UID_DIVIDER + "1").
            append("\"");

        if (checkBox.isSelected())
            d.append(" checked=\"1\"");
    }

    protected void writeFormPostfix(Device d, SCheckBox checkBox)
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
