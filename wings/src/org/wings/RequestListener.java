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
 * The request listener interface is implemented by all components
 * that take part at the event dispatching process.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface RequestListener
{
    /**
     * Deliver low level event.
     * The name-value-pairs of the HTTPRequest are considered low level events.
     * @param name the name-value-pair's name
     * @param value the name-value-pair's value
     */
    void getPerformed(String name, String value);
    String getNamePrefix();
    String getName();

    /**
     * Events, that update a component's (or its model's) state are <code>immediate</code>
     * events.
     */
    void fireIntermediateEvents();
    /**
     * For example ActionEvents, that are caused by clicking the submit button of a form
     * must be fired <b>after</b> all other events have been fired.
     */
    void fireFinalEvents();
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
