/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.table;

import org.wings.SComponent;
import org.wings.STable;

/**
 * Cell Renderer for {@link org.wings.STable}. This is similar (almost the same)
 * to the renderer approach in swing.
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface STableCellRenderer {
    SComponent getTableCellRendererComponent(STable table,
                                             Object value,
                                             boolean isSelected,
                                             int row,
                                             int column);
}


