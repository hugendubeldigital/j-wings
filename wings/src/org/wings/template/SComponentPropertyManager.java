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
import java.awt.Font;

import java.util.StringTokenizer;

import org.wings.SComponent;

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

    public void setProperty(Object o, String name, String value) {
        SComponent c = (SComponent)o;
        if ( name.equals("BACKGROUND") )
            c.setBackground(Color.decode(value));
        else if ( name.equals("FOREGROUND") )
            c.setForeground(Color.decode(value));
        else if ( name.equals("FONT") )
            c.setFont(parseFont(value));
    }

    public Class[] getSupportedClasses() {
        return classes;
    }


    protected final Font parseFont(String value) {
        StringTokenizer s = new StringTokenizer(value, ",");
        String fontName = s.nextToken();
        String tmpFontType = s.nextToken().toUpperCase().trim();
        int fontType = Font.PLAIN;
        if ( tmpFontType.startsWith("B") )
            fontType = Font.BOLD;
        else if ( tmpFontType.startsWith("I") )
            fontType = Font.ITALIC;

        int fontSize = 12;
        try {
            fontSize = Integer.parseInt(s.nextToken());
        }
        catch (Exception e) {}

        return new Font(fontName, fontType, fontSize);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
