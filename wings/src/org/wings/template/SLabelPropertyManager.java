/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.template;

import org.wings.SLabel;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SLabelPropertyManager
    extends SComponentPropertyManager
{
    static final Class[] classes = {SLabel.class};

    public SLabelPropertyManager() {
    }

    public void setProperty(Object o, String name, String value) {
        SLabel c = (SLabel)o;
        if ( name.equals("ICON") )
            c.setIcon(value);
        else if ( name.equals("TEXT") )
            c.setText(value);
        else
            super.setProperty(o, name, value);
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
