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

package org.wings;

/**
 * TODO: documentation
 *
 * @author
 * @version $Revision$
 */
public interface SListCellRenderer
{
    SComponent getListCellRendererComponent(SComponent list,
                                            Object value,
                                            boolean isSelected,
                                            int index);
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
