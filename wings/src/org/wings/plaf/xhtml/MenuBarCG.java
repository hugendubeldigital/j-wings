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
import org.wings.util.*;
import org.wings.externalizer.ExternalizeManager;

public class MenuBarCG
    extends org.wings.plaf.AbstractComponentCG
    implements org.wings.plaf.MenuBarCG, SConstants
{
    private final static String propertyPrefix = "MenuBar";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SButton b = null;
        SMenuBar mbar = (SMenuBar) c;
        int mcount = mbar.getMenuCount();
        
        d.print("<div class=\"menubar\">");
        d.print("<table cellspacing=\"0\" cellpadding=\"0\" vspace=\"0\" hspace=\"0\"><tr align=\"left\">");
        for (int i = 0; i < mcount; i++) {
            SMenu menu = (SMenu) mbar.getComponentAtIndex( i );
            if ( menu.isActive() ) {
                d.print("<td class=\"amenu\">");
                menu.write(d);
                d.print("<div id=\"MenuBarLayer\" class=\"pdmenu\">");

                int micount = menu.getMenuComponentCount();
                for ( int mi = 0; mi < micount; mi++ ) {
                    SComponent menuitem = menu.getMenuComponent( mi );
                    if (menuitem instanceof SSeparator)
                    {
                        d.print("<br><img src=\"\" class=\"menuseparator\" width=\"");
                        d.print(((SSeparator) menuitem).getWidth());
                        d.print("\" height=\"1\"><br>");
                    }
                    else {
                       d.print("&nbsp;");
                       menuitem.write(d);
                        if ((mi + 1) < micount) {
                            if (!(menu.getMenuComponent(mi + 1) instanceof SSeparator))
                                d.print("&nbsp;<br>");
                        }
                    }
                }
                d.print("</div>");
                d.print("</td>");
            }
            else {
                d.print("<td class=\"menu\">");
                menu.write(d);
                d.print("</td>");
            }
        }
        d.print("</tr></table>");
        d.print("</div>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
