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


public class HRefCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.HRefCG
{
    private final static String propertyPrefix = "HRef";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

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
        SIcon icon = hRef.getIcon();
        String text = hRef.getText();
        int horizontalTextPosition = hRef.getHorizontalTextPosition();
        int verticalTextPosition = hRef.getVerticalTextPosition();

        if (icon == null )
            writeAnchorText(d, hRef);
        else if (text == null)
            writeAnchorIcon(d, hRef);
        else {
            // Hauptsache, es funktioniert !!!
            if (verticalTextPosition == SHRef.TOP &&
                horizontalTextPosition == SHRef.LEFT) {
                d.print("<table><tr><td valign=\"top\">");
                writeAnchorText(d, hRef);
                d.print("</td><td>");
                writeAnchorIcon(d, hRef);
                d.print("</td></tr></table>\n");
            }
            else if (verticalTextPosition == SHRef.CENTER &&
                     horizontalTextPosition == SHRef.LEFT) {
                d.print("<table><tr><td>");
                writeAnchorText(d, hRef);
                d.print("</td><td>");
                writeAnchorIcon(d, hRef);
                d.print("</td></tr></table>\n");
            }
            else if (verticalTextPosition == SHRef.BOTTOM &&
                     horizontalTextPosition == SHRef.LEFT) {
                d.print("<table><tr><td valign=\"bottom\">");
                writeAnchorText(d, hRef);
                d.print("</td><td>");
                writeAnchorIcon(d, hRef);
                d.print("</td></tr></table>\n");
            }
            else if (verticalTextPosition == SHRef.TOP &&
                     horizontalTextPosition == SHRef.CENTER) {
                d.print("<table><tr><td>");
                writeAnchorText(d, hRef);
                d.print("</td></tr><tr><td>");
                writeAnchorIcon(d, hRef);
                d.print("</td></tr></table>\n");
            }
            else if (verticalTextPosition == SHRef.CENTER &&
                     horizontalTextPosition == SHRef.CENTER) {
                d.print("<table><tr><td>");
                writeAnchorText(d, hRef);
                d.print("</td><td>");
                writeAnchorIcon(d, hRef);
                d.print("</td></tr></table>\n");
            }
            else if (verticalTextPosition == SHRef.BOTTOM &&
                     horizontalTextPosition == SHRef.CENTER) {
                d.print("<table><tr><td>");
                writeAnchorIcon(d, hRef);
                d.print("</td></tr><tr><td>");
                writeAnchorText(d, hRef);
                d.print("</td></tr></table>\n");
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
        SIcon icon = hRef.isEnabled() ? hRef.getIcon() : hRef.getDisabledIcon();

        if ( icon != null ) {
            writeAnchorPrefix(d, hRef);
            d.print("<img src=\"").
                print(icon.getURL()).
                print("\"");
            if (align != null)
                d.print(" align=").print(align);

            if ( icon.getIconWidth() > 0)
                d.print(" width=").print(icon.getIconWidth());

            if ( icon.getIconHeight() > 0)
                d.print(" height=").print(icon.getIconHeight());

            d.print(" border=0");

            String text = hRef.getText();
            String tooltip = hRef.getToolTipText();


            if (tooltip != null) {
                d.print(" alt=\"").print(tooltip).print("\"");
            } else if (text != null) {
                d.print(" alt=\"").print(text).print("\"");
            }

            d.print(" />");

            writeAnchorPostfix(d, hRef);
        }
    }

    protected void writeAnchorPrefix(Device d, SHRef hRef)
        throws IOException
    {
        String tooltip = hRef.getToolTipText();

        if (hRef.isEnabled()) {
            d.print("<a");

            Object ref = hRef.getReference();
            if (ref != null) {
                d.print(" href=\"");

                if (ref instanceof Resource)
                    d.print(((Resource)ref).getURL());
                else if (ref instanceof String)
                    d.print((String)ref);

                d.print("\"");
            }

            if (hRef.getRealTarget() != null)
                d.print(" target=\"").print(hRef.getRealTarget()).print("\"");

            if (tooltip != null)
                d.print(" title=\"").print(tooltip).print("\"");

            d.print(">");
        }
    }

    protected void writeAnchorBody(Device d, SHRef hRef)
        throws IOException
    {
        String text = hRef.getText();
        boolean noBreak = hRef.isNoBreak();

        if (noBreak)
            d.print("<nobr>");
        d.print((text != null) ? text : "");
        if (noBreak)
            d.print("</nobr>");
    }
    
    protected void writeAnchorPostfix(Device d, SHRef hRef)
        throws IOException
    {
        if (hRef.isEnabled()) {
            d.print("</a>\n");
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
