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
        SAbstractButton checkBox = (SAbstractButton)c;
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

    protected void writeHiddenComponent(Device d, SAbstractButton checkBox)
        throws IOException
    {
        Utils.writeHiddenComponent(d, checkBox.getNamePrefix(),
                                   checkBox.getDeselectParameter());
    }

    protected void writeAnchorButton(Device d, SAbstractButton checkBox)
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
                d.print("<table><tr><td valign=\"top\">");
                writeAnchorText(d, checkBox);
                d.print("</td><td>");
                writeAnchorIcon(d, checkBox);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == LEFT) {
                d.print("<table><tr><td>");
                writeAnchorText(d, checkBox);
                d.print("</td><td>");
                writeAnchorIcon(d, checkBox);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == LEFT) {
                d.print("<table><tr><td valign=\"bottom\">");
                writeAnchorText(d, checkBox);
                d.print("</td><td>");
                writeAnchorIcon(d, checkBox);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == CENTER) {
                d.print("<table><tr><td>");
                writeAnchorText(d, checkBox);
                d.print("</td></tr><tr><td>");
                writeAnchorIcon(d, checkBox);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == CENTER) {
                d.print("<table><tr><td>");
                writeAnchorText(d, checkBox);
                d.print("</td><td>");
                writeAnchorIcon(d, checkBox);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == CENTER) {
                d.print("<table><tr><td>");
                writeAnchorIcon(d, checkBox);
                d.print("</td></tr><tr><td>");
                writeAnchorText(d, checkBox);
                d.print("</td></tr></table>");
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

    protected void writeAnchorText(Device d, SAbstractButton checkBox)
        throws IOException
    {
        String text = checkBox.getText();
        if (text != null && text.trim().length() > 0) {
            writeAnchorPrefix(d, checkBox);
            writeAnchorBody(d, checkBox);
            writeAnchorPostfix(d, checkBox);
        }
    }

    protected void writeAnchorIcon(Device d, SAbstractButton checkBox)
        throws IOException
    {
        writeAnchorIcon(d, checkBox, null);
    }

    protected void writeAnchorIcon(Device d, SAbstractButton checkBox, String align)
        throws IOException
    {
        SIcon actualIcon = null;
        if ( checkBox.isEnabled() ) {
             actualIcon = 
                 checkBox.isSelected() ? checkBox.getSelectedIcon() :
                 checkBox.getIcon();
        } else {
            actualIcon = checkBox.isSelected() ? checkBox.getDisabledSelectedIcon() :
                 checkBox.getDisabledIcon();

        }
            

        if (actualIcon != null) {

            writeAnchorPrefix(d, checkBox);

            d.print("<img src=\"").
                print(actualIcon.getURL()).
                print("\"");

            if ( checkBox.isEnabled() ) {
                // render rollover
                SIcon rolloverIcon = 
                    checkBox.isSelected() ? checkBox.getRolloverSelectedIcon() :
                    checkBox.getRolloverIcon();

                SIcon pressedIcon = checkBox.getPressedIcon();

                if ( rolloverIcon!=null || pressedIcon!=null ) {
                    String iconName = "Icon_" + checkBox.getUnifiedId();

                    d.print(" name=\"").print(iconName).print("\"");

                    if ( rolloverIcon!=null ) {
                        d.print(" onMouseover=\"if(document.images){document.")
                            .print(iconName).print(".src='").print(rolloverIcon.getURL())
                            .print("';}\"")
                            .print(" onMouseout=\"if(document.images){document.")
                            .print(iconName).print(".src='").print(actualIcon.getURL())
                            .print("';}\"");
                    }
                    
                    // render pressed
                    if ( pressedIcon!=null ) {
                        d.print(" onMousedown=\"if(document.images){document.")
                            .print(iconName).print(".src='").print(pressedIcon.getURL())
                            .print("';}\"")
                            .print(" onMouseUp=\"if(document.images){document.")
                            .print(iconName).print(".src='")
                            .print(rolloverIcon!=null ? rolloverIcon.getURL() : 
                                   actualIcon.getURL())
                            .print("';}\"");
                    }
                }

            }

            if (align != null)
                d.print(" align=\"").print(align).print("\"");

            if ( actualIcon.getIconWidth() > 0)
                d.print(" width=\"").print(actualIcon.getIconWidth()).print("\"");

            if ( actualIcon.getIconHeight() > 0)
                d.print(" height=\"").print(actualIcon.getIconHeight()).print("\"");

            d.print(" border=\"0\"");

            String tooltip = checkBox.getToolTipText();
            String text = checkBox.getText();

            if (tooltip != null) {
                d.print(" alt=\"").print(tooltip).print("\"");
            } else if (text != null) {
                d.print(" alt=\"").print(text).print("\"");
            }

            d.print(" />");

            writeAnchorPostfix(d, checkBox);
        }
    }

    protected void writeAnchorAddress(Device d, SAbstractButton checkBox) 
    throws IOException {
        RequestURL addr = checkBox.getRequestURL();
        addr.addParameter(checkBox.getNamePrefix() + "=" + 
                          checkBox.getSelectionToggleParameter());
        addr.write(d);
    }

    protected void writeAnchorPrefix(Device d, SAbstractButton checkBox)
        throws IOException 
    {
        writeAnchorPrefix(d, checkBox, null);
    }

    protected void writeAnchorPrefix(Device d, SAbstractButton checkBox, String params)
        throws IOException
    {
        String tooltip = checkBox.getToolTipText();

        if (checkBox.isEnabled()) {
            d.print("<a href=\"");
            writeAnchorAddress(d, checkBox);
            d.print("\"");
            
            if (checkBox.getEventTarget() != null)
                d.print(" target=\"").print(checkBox.getEventTarget()).print("\"");

            if (tooltip != null)
                d.print(" title=\"").print(tooltip).print("\"");

            if (params != null) {
                d.print(params);
            }

            d.print(">");
        }
    }

    protected void writeAnchorBody(Device d, SAbstractButton checkBox)
        throws IOException
    {
        String text = checkBox.getText();
        boolean noBreak = checkBox.isNoBreak();

        if (text == null)
            text = "";
        d.print((noBreak) ? StringUtil.replace(text, " ", "&nbsp;") : text);
    }

    protected void writeAnchorPostfix(Device d, SAbstractButton checkBox)
        throws IOException
    {
        if (checkBox.isEnabled()) {
            d.print("</a>");
        }
    }

    protected void writeFormButton(Device d, SAbstractButton checkBox)
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
            writeFormIcon(d, checkBox, id);
        } else if (text == null) {
            writeFormIcon(d, checkBox, id);
        } else {
            // Hauptsache, es funktioniert !!!
            if (verticalTextPosition == NO_ALIGN || horizontalTextPosition == NO_ALIGN) {
                writeFormIcon(d, checkBox, id);
                writeFormText(d, checkBox, id);
            } else if (verticalTextPosition == TOP && horizontalTextPosition == LEFT) {
                d.print("<table><tr><td valign=\"top\">");
                writeFormText(d, checkBox, id);
                d.print("</td><td>");
                writeFormIcon(d, checkBox, id);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == LEFT) {
                d.print("<table><tr><td>");
                writeFormText(d, checkBox, id);
                d.print("</td><td>");
                writeFormIcon(d, checkBox, id);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == LEFT) {
                d.print("<table><tr><td valign=\"bottom\">");
                writeFormText(d, checkBox, id);
                d.print("</td><td>");
                writeFormIcon(d, checkBox, id);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == CENTER) {
                d.print("<table><tr><td>");
                writeFormText(d, checkBox, id);
                d.print("</td></tr><tr><td>");
                writeFormIcon(d, checkBox, id);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == CENTER) {
                d.print("<table><tr><td>");
                writeFormText(d, checkBox, id);
                d.print("</td><td>");
                writeFormIcon(d, checkBox, id);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == CENTER) {
                d.print("<table><tr><td>");
                writeFormIcon(d, checkBox, id);
                d.print("</td></tr><tr><td>");
                writeFormText(d, checkBox, id);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == TOP && horizontalTextPosition == RIGHT) {
                d.print("<table><tr><td valign=\"top\">");
                writeFormIcon(d, checkBox, id);
                d.print("</td><td align=\"right\">");
                writeFormText(d, checkBox, id);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == CENTER && horizontalTextPosition == RIGHT) {
                d.print("<table><tr><td>");
                writeFormIcon(d, checkBox, id);
                d.print("</td><td align=\"right\">");
                writeFormText(d, checkBox, id);
                d.print("</td></tr></table>");
            } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == RIGHT) {
                d.print("<table><tr><td valign=\"bottom\">");
                writeFormIcon(d, checkBox, id);
                d.print("</td></tr><tr><td align=\"right\">");
                writeFormText(d, checkBox, id);
                d.print("</td></tr></table>");
            } else {
                d.print("<table><tr><td>");
                writeFormIcon(d, checkBox, id);
                d.print("</td></tr><tr><td>");
                writeFormText(d, checkBox, id);
                d.print("</td></tr></table>");
            }
        }
    }

    protected void writeFormText(Device d, SAbstractButton checkBox, String id)
        throws IOException
    {
        d.print("<label for=\"").print(id).print("\">");
        d.print(checkBox.getText());
        d.print("</label>");
    }

    protected void writeFormIcon(Device d, SAbstractButton checkBox, String id)
        throws IOException
    {
        writeFormPrefix(d, checkBox, id);
        writeFormBody(d, checkBox);
        writeFormPostfix(d, checkBox);
    }

    protected void writeFormPrefix(Device d, SAbstractButton checkBox, String id)
        throws IOException
    {
        d.print("<input type=\"");
        d.print(checkBox.getType());
        d.print("\" id=\"").print(id).print("\"");
    }

    protected void writeFormBody(Device d, SAbstractButton checkBox)
        throws IOException
    {
        if (checkBox.isEnabled())
            d.print(" name=\"").
                print(checkBox.getNamePrefix()).print("\"");

        d.print(" value=\"").
            print(checkBox.getSelectParameter()).
            print("\"");

        if (checkBox.isSelected())
            d.print(" checked=\"1\"");
    }

    protected void writeFormPostfix(Device d, SAbstractButton checkBox)
        throws IOException
    {
        d.print(" />");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
