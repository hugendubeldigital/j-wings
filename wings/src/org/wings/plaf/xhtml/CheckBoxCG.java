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

public class CheckBoxCG
    implements org.wings.plaf.CheckBoxCG, SConstants
{
    private final static String propertyPrefix = "CheckBox";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent component) {
        component.setStyle(component.getSession().getCGManager().
                           getStyle(getPropertyPrefix() + ".style"));
    }

    public void uninstallCG(SComponent c) {
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
                                   checkBox.getUnifiedIdString() + UID_DIVIDER + "0");
    }

    protected void writeAnchorButton(Device d, SCheckBox checkBox)
        throws IOException
    {
        Icon icon = null;
        String iconAddress = null;
        if (checkBox.isSelected()) {
            icon = checkBox.getSelectedIcon();
            iconAddress = checkBox.getSelectedIconAddress();
        }
        else {
            icon = checkBox.getIcon();
            iconAddress = checkBox.getIconAddress();
        }
        String text = checkBox.getText();
        int horizontalTextPosition = checkBox.getHorizontalTextPosition();
        int verticalTextPosition = checkBox.getVerticalTextPosition();

        if (icon == null && iconAddress == null)
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
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td>");
                writeAnchorText(d, checkBox);
                d.append("</td><td>");
                writeAnchorIcon(d, checkBox);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"bottom\">");
                writeAnchorText(d, checkBox);
                d.append("</td><td>");
                writeAnchorIcon(d, checkBox);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeAnchorText(d, checkBox);
                d.append("</td></tr><tr><td>");
                writeAnchorIcon(d, checkBox);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeAnchorText(d, checkBox);
                d.append("</td><td>");
                writeAnchorIcon(d, checkBox);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeAnchorIcon(d, checkBox);
                d.append("</td></tr><tr><td>");
                writeAnchorText(d, checkBox);
                d.append("</td></tr></table>\n");
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
        String iconAddress = null;
        String disabledIconAddress = null;
        Icon icon = null;
        Icon disabledIcon = null;
        if (checkBox.isSelected()) {
            icon = checkBox.getSelectedIcon();
            iconAddress = checkBox.getSelectedIconAddress();
            disabledIcon = checkBox.getDisabledSelectedIcon();
            disabledIconAddress = checkBox.getDisabledSelectedIconAddress();
        }
        else {
            icon = checkBox.getIcon();
            iconAddress = checkBox.getIconAddress();
            disabledIcon = checkBox.getDisabledIcon();
            disabledIconAddress = checkBox.getDisabledIconAddress();
        }

        String text = checkBox.getText();
        String tooltip = checkBox.getToolTipText();

        String iAdr = null;
        Icon ic = null;

        if (!checkBox.isEnabled()){
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
            ExternalizeManager ext = checkBox.getExternalizeManager();
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
            writeAnchorPrefix(d, checkBox);

            d.append("<img src=\"").append(iAdr).append("\"");
            if (align != null)
                d.append(" align=\"").append(align).append("\"");
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

            writeAnchorPostfix(d, checkBox);
        }
    }

    protected String generateAnchorAddress(SCheckBox checkBox) {
        SGetAddress addr = checkBox.getServerAddress();
        addr.add(checkBox.getNamePrefix() + "=" + checkBox.getUnifiedIdString() + SConstants.UID_DIVIDER);
        return addr.toString();
    }

    protected void writeAnchorPrefix(Device d, SCheckBox checkBox)
        throws IOException
    {
        String tooltip = checkBox.getToolTipText();

        if (checkBox.isEnabled()) {
            d.append("<a href=\"").append(generateAnchorAddress(checkBox)).append("\"");

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
            d.append("</a>\n");
        }
    }

    protected void writeFormButton(Device d, SCheckBox checkBox)
        throws IOException
    {
        Icon icon = null;
        String iconAddress = null;
        if (checkBox.isSelected()) {
            icon = checkBox.getSelectedIcon();
            iconAddress = checkBox.getSelectedIconAddress();
        }
        else {
            icon = checkBox.getIcon();
            iconAddress = checkBox.getIconAddress();
        }
        String text = checkBox.getText();
        int horizontalTextPosition = checkBox.getHorizontalTextPosition();
        int verticalTextPosition = checkBox.getVerticalTextPosition();

        if (icon == null && iconAddress == null) {
            System.err.println("text only");
            writeFormText(d, checkBox);
        }
        else if (text == null) {
            writeFormIcon(d, checkBox);
        }
        else {
            // Hauptsache, es funktioniert !!!
            if (verticalTextPosition == NO_ALIGN || horizontalTextPosition == NO_ALIGN) {
                writeFormIcon(d, checkBox);
                writeFormText(d, checkBox);
            } else if (verticalTextPosition == TOP && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"top\">");
                writeFormText(d, checkBox);
                d.append("</td><td>");
                writeFormIcon(d, checkBox);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td>");
                writeFormText(d, checkBox);
                d.append("</td><td>");
                writeFormIcon(d, checkBox);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == LEFT) {
                d.append("<table><tr><td valign=\"bottom\">");
                writeFormText(d, checkBox);
                d.append("</td><td>");
                writeFormIcon(d, checkBox);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeFormText(d, checkBox);
                d.append("</td></tr><tr><td>");
                writeFormIcon(d, checkBox);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeFormText(d, checkBox);
                d.append("</td><td>");
                writeFormIcon(d, checkBox);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == CENTER) {
                d.append("<table><tr><td>");
                writeFormIcon(d, checkBox);
                d.append("</td></tr><tr><td>");
                writeFormText(d, checkBox);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == RIGHT) {
                d.append("<table><tr><td valign=\"top\">");
                writeFormIcon(d, checkBox);
                d.append("</td><td align=\"right\">");
                writeFormText(d, checkBox);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == RIGHT) {
                d.append("<table><tr><td>");
                writeFormIcon(d, checkBox);
                d.append("</td><td align=\"right\">");
                writeFormText(d, checkBox);
                d.append("</td></tr></table>\n");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == RIGHT) {
                d.append("<table><tr><td valign=\"bottom\">");
                writeFormIcon(d, checkBox);
                d.append("</td></tr><tr><td align=\"right\">");
                writeFormText(d, checkBox);
                d.append("</td></tr></table>\n");
            } else {
                d.append("<table><tr><td>");
                writeFormIcon(d, checkBox);
                d.append("</td></tr><tr><td>");
                writeFormText(d, checkBox);
                d.append("</td></tr></table>\n");
            }
        }
    }

    protected void writeFormText(Device d, SCheckBox checkBox)
        throws IOException
    {
        d.append(checkBox.getText());
    }

    protected void writeFormIcon(Device d, SCheckBox checkBox)
        throws IOException
    {
        writeFormPrefix(d, checkBox);
        writeFormBody(d, checkBox);
        writeFormPostfix(d, checkBox);
    }

    protected void writeFormPrefix(Device d, SCheckBox checkBox)
        throws IOException
    {
        d.append("<input type=\"");
        d.append(checkBox.getType());
        d.append("\"");
    }

    protected void writeFormBody(Device d, SCheckBox checkBox)
        throws IOException
    {
        if (checkBox.isEnabled())
            d.append(" name=\"").
                append(checkBox.getNamePrefix()).append("\"");

        d.append(" value=\"").
            append(checkBox.getUnifiedIdString() + UID_DIVIDER + "1").
            append("\"");

        if (checkBox.isSelected())
            d.append(" checked=\"checked\"");
    }

    protected void writeFormPostfix(Device d, SCheckBox checkBox)
        throws IOException
    {
        d.append(" />\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
