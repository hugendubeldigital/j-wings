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

/*
 * SGetDispatcher.java
 *
 * Spezifiziert Verteiler fuer HTML gets. Eingaben von Klienten in
 * HTML werden mit Hilfe der
 * get/put Methode an den Server zurueckgeschickt. Ein
 * Implementation dieses
 * Interfaces ist in der Lage Eingaben an die richtigen (die Eingabemaske
 * erzeugenden) Objekte weiterzuleiten.
 */
/**
 * Delivers low level events to all components, that implement the RequestListener
 * interface and hence are registered with this dispatcher.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface SGetDispatcher
{
    /**
     * Register a RequestListener.
     * @param l an interactive component
     */
    void register(RequestListener l);

    /**
     * Unregister a RequestListener.
     * @param l an interactive component
     */
    void unregister(RequestListener l);

    /**
     * Deliver low level events to the registered RequestListener
     * with the specified name.
     * @param name the name of the event target
     * @param values the values
     */
    boolean dispatch(String name, String[] values);

    /**
     * Event dispatching is done
     */
    void dispatchDone();

    /**
     * Set the target of all links.
     * @param the target
     */
    void setTarget(String target);

    /**
     * Get the target of all links.
     * @return the target
     */
    String getTarget();
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
