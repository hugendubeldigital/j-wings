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

package org.wings.template;

import java.awt.Color;

import java.util.StringTokenizer;

import org.wings.SComponent;
import org.wings.SFont;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SComponentPropertyManager
    implements PropertyManager
{
    static final Class[] classes = {SComponent.class};

    public SComponentPropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        if ( "BACKGROUND".equals(name) )
            comp.setBackground(Color.decode(value));
        else if ( "FOREGROUND".equals(name) )
            comp.setForeground(Color.decode(value));
        else if ( "FONT".equals(name) )
            comp.setFont(parseFont(value));
        else if ( "TABINDEX".equals(name) )
            comp.setFocusTraversalIndex(Integer.parseInt(value));
    }

    public Class[] getSupportedClasses() {
        return classes;
    }

    protected final SFont parseFont(String value) {
        StringTokenizer s = new StringTokenizer(value, ",");
        String fontName = s.nextToken();
        String tmpFontType = s.nextToken().toUpperCase().trim();
        int fontType = SFont.PLAIN;
        if ( tmpFontType.startsWith("B") )
            fontType = SFont.BOLD;
        else if ( tmpFontType.startsWith("I") )
            fontType = SFont.ITALIC;

        int fontSize = 12;
        try {
            fontSize = Integer.parseInt(s.nextToken());
        }
        catch (Exception e) {}

        return new SFont(fontName, fontType, fontSize);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
