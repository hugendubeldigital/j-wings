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

public class MenuBarCG
    extends org.wings.plaf.AbstractCG
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
        
        d.append("<div class=\"menubar\">");
        d.append("<table cellspacing=\"0\" cellpadding=\"0\" vspace=\"0\" hspace=\"0\"><tr align=\"left\">");
        for (int i = 0; i < mcount; i++) {
            SMenu menu = (SMenu) mbar.getComponentAtIndex( i );
            if ( menu.isActive() ) {
                d.append("<td class=\"amenu\">");
                menu.write(d);
                d.append("<div id=\"MenuBarLayer\" class=\"pdmenu\">");

                int micount = menu.getMenuComponentCount();
                for ( int mi = 0; mi < micount; mi++ ) {
                    SComponent menuitem = menu.getMenuComponent( mi );
                    d.append("&nbsp;");
                    if (menuitem instanceof SSeparator) {
                        d.append("<table class=\"menuseparator\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td></td></tr></table>");
                    }
                    else {
                        menuitem.write(d);
                        if ((mi + 1) < micount) {
                            if (!(menu.getMenuComponent(mi + 1) instanceof SSeparator))
                                d.append("&nbsp;<br>");
                        }
                    }
                }
                d.append("</div>");
                d.append("</td>");
            }
            else {
                d.append("<td class=\"menu\">");
                menu.write(d);
                d.append("</td>");
            }
        }
        d.append("</tr></table>");
        d.append("</div>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
