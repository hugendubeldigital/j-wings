/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of the wingS demo (http://wings.mercatis.de).
 *
 * The wingS demo is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package wingset;

import java.awt.Color;
import javax.swing.Icon;

import org.wings.*;

/**
 * A basic WingSet Pane, which implements some often needed functions.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
abstract public class WingSetPane
    extends SPanel
    implements SConstants {

    public WingSetPane() {
	setLayout(createResourceTemplate("/wingset/templates/ContentLayout.thtml"));

	add(createExample(), "content"); // content generated by example
	
	SHRef href =  new SHRef("View Source Code");
        href.setReference("/wingset/" +
                          getClass().getName().substring(getClass().getName().indexOf('.') +1) + ".java");
        add(href, "viewSource");
    }

    /**
     * Override this.
     */
    protected abstract SComponent createExample();

    protected static SLayoutManager createResourceTemplate(String templName) {
        try {
            java.net.URL templateURL = WingSet.class.getResource(templName);
	    return new STemplateLayout(templateURL);
        }
        catch ( Exception except ) {
            except.printStackTrace();
        }
	return null;
    }

    protected static SPanel createResourceTemplatePanel(String templateName) {
	SPanel p = new SPanel();
	try {
	    SLayoutManager layout = createResourceTemplate(templateName);
	    p.setLayout(layout);
	}
	catch ( Exception e ) {
	    p.add(new SLabel("Sorry, can't find " + templateName
			     + " in resources"));
	}
	return p;
    }
}
