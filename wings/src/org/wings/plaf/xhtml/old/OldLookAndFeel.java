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
import java.util.Properties;
import org.wings.plaf.LookAndFeel;

public class OldLookAndFeel
    extends LookAndFeel
{
    public OldLookAndFeel() {
	super(null);
	try {
	    properties = new Properties();
	    properties.load(getClass().getResourceAsStream("/org/wings/plaf/xhtml/old/default.properties"));
	}
	catch (IOException e) {
	    // assert false, e.getMessage();
	}
    }
}
