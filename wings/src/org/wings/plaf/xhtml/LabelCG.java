package org.wings.plaf.xhtml;

import java.io.IOException;

import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.externalizer.ExternalizeManager;


public class LabelCG
    implements org.wings.plaf.LabelCG, SConstants
{
    private final static String propertyPrefix = "Label";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent component) {
        component.setStyle(component.getSession().getCGManager().
                           getStyle(getPropertyPrefix() + ".style"));
    }

    public void uninstallCG(SComponent c) {
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

            if (noBreak)
                d.append("<nobr>");
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
                d.append(" valign=").append(align);
            if (ic != null) {
                d.append(" width=").append(ic.getIconWidth());
                d.append(" height=").append(ic.getIconHeight());
            }
            d.append(" border=0");

            if (tooltip != null) {
                d.append(" alt=\"").append(tooltip).append("\"");
            } else if (text != null) {
                d.append(" alt=\"").append(text).append("\"");
            }

            d.append(">");
        }
    }
}
