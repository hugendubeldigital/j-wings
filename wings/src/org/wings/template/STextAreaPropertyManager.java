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

import org.wings.STextArea;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class STextAreaPropertyManager
    extends SComponentPropertyManager
{
    static final Class[] classes = {STextArea.class};

    public STextAreaPropertyManager() {
    }

    public void setProperty(Object o, String name, String value) {
        STextArea c = (STextArea)o;
        if ( name.equals("COLS") )
            c.setColumns(Integer.parseInt(value));
        else if ( name.equals("ROWS") )
            c.setRows(Integer.parseInt(value));
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
