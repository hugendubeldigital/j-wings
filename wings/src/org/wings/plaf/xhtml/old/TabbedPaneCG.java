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

package org.wings.plaf.xhtml.old;

import java.io.IOException;
import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;
import org.wings.externalizer.ExternalizeManager;

public final class TabbedPaneCG
    extends org.wings.plaf.xhtml.TabbedPaneCG
{
    Icon firstIcon;
    Icon normalIcon;
    Icon selectedIcon;
    Icon lastIcon;

    Icon transIcon;

    public void installCG(SComponent component) {
        super.installCG(component);
        firstIcon = component.getSession().getCGManager().getIcon("TabbedPaneCG.firstIcon");
        normalIcon = component.getSession().getCGManager().getIcon("TabbedPaneCG.normalIcon");
        selectedIcon = component.getSession().getCGManager().getIcon("TabbedPaneCG.selectedIcon");
        lastIcon = component.getSession().getCGManager().getIcon("TabbedPaneCG.lastIcon");
        transIcon = LookAndFeel.makeIcon(TabbedPaneCG.class, "/org/wings/icons/transdot.gif");
    }


    // ignore tab placement for now .. always TOP
    public void write(Device d, SComponent c)
        throws IOException
    {
        SBorder border = c.getBorder();
        STabbedPane pane = (STabbedPane)c;

        String firstAdr   = null;
        String normalAdr  = null;
        String selectAdr  = null;
        String lastAdr    = null;
        String transAdr   = null;
        int maxTabsPerLine = pane.getMaxTabsPerLine();

        ExternalizeManager ext = c.getExternalizeManager();
        if (ext != null && firstIcon != null) {
            try {
                firstAdr   = ext.externalize(firstIcon);
                normalAdr = ext.externalize(normalIcon);
                selectAdr = ext.externalize(selectedIcon);
                lastAdr   = ext.externalize(lastIcon);
                transAdr   = ext.externalize(transIcon);
            }
            catch (java.io.IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
            }
        }

        SContainer buttons;
        SContainer contents;
        if (SBorderLayout.CENTER.equals(pane.getConstraintAt(0))) {
            buttons = (SContainer)pane.getComponentAt(1);
            contents = (SContainer)pane.getComponentAt(0);
        }
        else {
            buttons = (SContainer)pane.getComponentAt(0);
            contents = (SContainer)pane.getComponentAt(1);
        }

        /* for browsers, that do not support the border styles, create a line
         * at top, that starts at position 18 (the first 17 pixels are used up by
         * the rise of the left tab. This gives the correct illusion at least for the
         * first line of tabs.
         */
        // this is actually only really for NS 4.x. Looks very ugly for Mozilla.
        // (dunno for IE).
        /*
        d.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">")
            .append("<tr><td>")
            .append("<img src=\"").append(transAdr).append("\" width=\"17\" height=\"1\" />")
            .append("</td><td width=\"100%\" bgcolor=\"#000000\">") // '100%' hack needed for NS4.x
            .append("<img src=\"").append(transAdr).append("\" width=\"1\" height=\"1\" />")
            .append("</td></tr></table>\n");
        */
        boolean newLine = true;
        boolean selected = false;

        for (int i=0; i < buttons.getComponentCount(); i++) {
            d.append("<img src=\"")
                .append(newLine 
                        ? firstAdr 
                        : (selected) ? selectAdr : normalAdr)
                .append("\" />");
            newLine = false;
            SRadioButton button = (SRadioButton)buttons.getComponentAt(i);
            String text = button.getText();
            if (text != null && !text.endsWith("&nbsp;"))
                button.setText(text + "&nbsp;");

            selected = (i == pane.getSelectedIndex());
            button.write(d);

            if ( maxTabsPerLine > 0 && ((i+1) % maxTabsPerLine == 0) ) {
                d.append("<img src=\"").append(lastAdr).append("\" />");
                d.append("<br />");
                newLine = true;
            }
        }
        if (!newLine) {
            // closed tab if not already written..
            d.append("<img src=\"").append(lastAdr).append("\" />");
        }

        d.append("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"1\"><tr><td>");

        contents.write(d);

        d.append("</td></tr></table>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
