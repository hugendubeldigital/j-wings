/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
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


public class HRefCG
    implements org.wings.plaf.HRefCG
{
    private final static String propertyPrefix = "HRef";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent component) {
        component.setStyle(component.getSession().getCGManager().
                           getStyle(getPropertyPrefix() + ".style"));
    }
    public void uninstallCG(SComponent c) {}

    public void write(Device d, SComponent c)
        throws IOException
    {
        SHRef hRef = (SHRef)c;
        SBorder border = hRef.getBorder();

        Utils.writeBorderPrefix(d, border);
        writeAnchorButton(d, hRef);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writeAnchorButton(Device d, SHRef hRef)
        throws IOException
    {
        Icon icon = hRef.getIcon();
        String text = hRef.getText();
        int horizontalTextPosition = hRef.getHorizontalTextPosition();
        int verticalTextPosition = hRef.getVerticalTextPosition();
        String iconAddress = hRef.getIconAddress();

        if (icon == null && iconAddress == null)
            writeAnchorText(d, hRef);
        else if (text == null)
            writeAnchorIcon(d, hRef);
        else {
            // Hauptsache, es funktioniert !!!
            if (verticalTextPosition == SHRef.TOP &&
                horizontalTextPosition == SHRef.LEFT) {
                d.append("<table><tr><td valign=\"top\">");
                writeAnchorText(d, hRef);
                d.append("</td><td>");
                writeAnchorIcon(d, hRef);
                d.append("</td></tr></table>\n");
            }
            else if (verticalTextPosition == SHRef.CENTER &&
                     horizontalTextPosition == SHRef.LEFT) {
                d.append("<table><tr><td>");
                writeAnchorText(d, hRef);
                d.append("</td><td>");
                writeAnchorIcon(d, hRef);
                d.append("</td></tr></table>\n");
            }
            else if (verticalTextPosition == SHRef.BOTTOM &&
                     horizontalTextPosition == SHRef.LEFT) {
                d.append("<table><tr><td valign=\"bottom\">");
                writeAnchorText(d, hRef);
                d.append("</td><td>");
                writeAnchorIcon(d, hRef);
                d.append("</td></tr></table>\n");
            }
            else if (verticalTextPosition == SHRef.TOP &&
                     horizontalTextPosition == SHRef.CENTER) {
                d.append("<table><tr><td>");
                writeAnchorText(d, hRef);
                d.append("</td></tr><tr><td>");
                writeAnchorIcon(d, hRef);
                d.append("</td></tr></table>\n");
            }
            else if (verticalTextPosition == SHRef.CENTER &&
                     horizontalTextPosition == SHRef.CENTER) {
                d.append("<table><tr><td>");
                writeAnchorText(d, hRef);
                d.append("</td><td>");
                writeAnchorIcon(d, hRef);
                d.append("</td></tr></table>\n");
            }
            else if (verticalTextPosition == SHRef.BOTTOM &&
                     horizontalTextPosition == SHRef.CENTER) {
                d.append("<table><tr><td>");
                writeAnchorIcon(d, hRef);
                d.append("</td></tr><tr><td>");
                writeAnchorText(d, hRef);
                d.append("</td></tr></table>\n");
            }
            else if (verticalTextPosition == SHRef.TOP &&
                     horizontalTextPosition == SHRef.RIGHT) {
                writeAnchorIcon(d, hRef, "top");
                writeAnchorText(d, hRef);
            }
            else if (verticalTextPosition == SHRef.CENTER &&
                     horizontalTextPosition == SHRef.RIGHT) {
                writeAnchorIcon(d, hRef, "middle");
                writeAnchorText(d, hRef);
            }
            else if (verticalTextPosition == SHRef.BOTTOM &&
                     horizontalTextPosition == SHRef.RIGHT) {
                writeAnchorIcon(d, hRef, "bottom");
                writeAnchorText(d, hRef);
            }
            else {
                writeAnchorText(d, hRef);
                writeAnchorIcon(d, hRef, "middle");
            }
        }
    }

    protected void writeAnchorText(Device d, SHRef hRef)
        throws IOException
    {
        String text = hRef.getText();
        if (text != null && text.trim().length() > 0) {
            writeAnchorPrefix(d, hRef);
            writeAnchorBody(d, hRef);
            writeAnchorPostfix(d, hRef);
        }
    }

    protected void writeAnchorIcon(Device d, SHRef hRef)
        throws IOException
    {
        writeAnchorIcon(d, hRef, null);
    }

    protected void writeAnchorIcon(Device d, SHRef hRef, String align)
        throws IOException
    {
        String text = hRef.getText();
        String iconAddress = hRef.getIconAddress();
        String disabledIconAddress = hRef.getDisabledIconAddress();
        Icon icon = hRef.getIcon();
        Icon disabledIcon = hRef.getDisabledIcon();
        String tooltip = hRef.getToolTipText();

        String iAdr = null;
        Icon ic = null;

        if (!hRef.isEnabled()){
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
            ExternalizeManager ext = hRef.getExternalizeManager();
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
            writeAnchorPrefix(d, hRef);
            d.append("<img src=\"").append(iAdr).append("\"");
            if (align != null)
                d.append(" align=").append(align);
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

            writeAnchorPostfix(d, hRef);
        }
    }

    protected void writeAnchorPrefix(Device d, SHRef hRef)
        throws IOException
    {
        String tooltip = hRef.getToolTipText();

        if (hRef.isEnabled()) {
            d.append("<a href=\"").append(hRef.getReference()).append("\"");

            if (hRef.getRealTarget() != null)
                d.append(" target=\"").append(hRef.getRealTarget()).append("\"");

            if (tooltip != null)
                d.append(" title=\"").append(tooltip).append("\"");

            d.append(">");
        }
    }

    protected void writeAnchorBody(Device d, SHRef hRef)
        throws IOException
    {
        String text = hRef.getText();
        boolean noBreak = hRef.isNoBreak();

        if (noBreak)
            d.append("<nobr>");
        d.append((text != null) ? text : "");
        if (noBreak)
            d.append("</nobr>");
    }

    protected void writeAnchorPostfix(Device d, SHRef hRef)
        throws IOException
    {
        if (hRef.isEnabled()) {
            d.append("</a>\n");
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
