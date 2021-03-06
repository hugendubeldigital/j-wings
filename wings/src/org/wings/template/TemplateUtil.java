/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.template;

import java.util.StringTokenizer;

import org.wings.SFont;

/**
 * TemplateUtil.java
 *
 *
 * Created: Tue Aug  6 16:41:22 2002
 *
 * @author (c) mercatis information systems gmbh, 1999-2002
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class TemplateUtil  {
    
    /**
     * 
     */
    public TemplateUtil() {
	
    }

    public static final SFont parseFont(String value) {
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
    
}// TemplateUtil

/*
   $Log$
   Revision 1.2  2005/01/16 01:01:20  oliverscheck
   Project URL modified to reflect new domain j-wings.org.

   Revision 1.1  2002/08/06 16:45:55  ahaaf
   add DefaultPropertyManager using reflection and bean shell scripting support

*/
