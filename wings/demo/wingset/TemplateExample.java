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

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:hzeller@to.com">Henner Zeller</a>
 * @version $Revision$
 */
public class TemplateExample
    extends SPanel
    implements SConstants
{
    public TemplateExample () {
        add(createTemplateExample());

        add(new SSeparator());

        SHRef href =  new SHRef("View Source Code");
        href.setReference("/demo/wingset/" +
                          getClass().getName().substring(getClass().getName().indexOf('.') +1) + ".java");
        add(href);
    }

    SForm createTemplateExample() {
        SForm c = new SForm();

        try {
            java.net.URL templateURL = getClass().getResource("/wingset/TemplateExample.thtml");
			if( templateURL == null ){
				c.add(new SLabel("Sorry, can't find TemplateExample.thtml. Are you using a JAR-File?"));
				return c;
			}
            // you can of course directly give files here.
            STemplateLayout layout = new STemplateLayout( templateURL );
			c.setLayout( layout );
        }
        catch ( java.io.IOException except ) {
            except.printStackTrace();
        }

        c.add(new SLabel ((new java.util.Date()).toString()), "theLabel");
        c.add(new SButton ("Button"), "TESTBUTTON");
        c.add(new STextField (), "NAME");
        c.add(new STextField (), "VORNAME");
        c.add(new TreeExample(), "BAUM");
        return c;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
