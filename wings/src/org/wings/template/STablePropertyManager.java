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
import org.wings.SComponent;
import org.wings.STable;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class STablePropertyManager
    extends SBaseTablePropertyManager
{
    static final Class[] classes = {STable.class};

    public STablePropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        STable t = (STable)comp;
        if ( name.equals("SELECTION_FOREGROUND") )
            t.setSelectionForeground(Color.decode(value));
        else if ( name.equals("SELECTION_BACKGROUND") )
            t.setSelectionBackground(Color.decode(value));
        else
            super.setProperty(comp, name, value);
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
