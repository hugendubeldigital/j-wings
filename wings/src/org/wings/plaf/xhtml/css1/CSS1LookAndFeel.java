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

package org.wings.plaf.xhtml.css1;

import java.io.IOException;
import java.util.Properties;
import org.wings.plaf.LookAndFeel;

public class CSS1LookAndFeel
    extends LookAndFeel
{
    public CSS1LookAndFeel() {
	super((Properties)null);
	try {
	    properties = new Properties();
	    properties.load(getClass().getResourceAsStream("/org/wings/plaf/xhtml/css1/default.properties"));
	}
	catch (IOException e) {
	    // assert false, e.getMessage();
	}
    }
}
