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

package org.wings.style;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class Style
{
    private String id;

    /**
     * TODO: documentation
     *
     * @param id
     */
    public Style(String id) {
        this.id = id;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getID() { return id; }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() { return id; }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
