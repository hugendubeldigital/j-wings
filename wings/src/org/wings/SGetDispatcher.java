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
 * TODO: documentation
 *
 * @author
 * @version $Revision$
 */
public interface SGetDispatcher
{
    void register(SGetListener l);
    void unregister(SGetListener l);

    /*
     * Verteilt die angebenen values an die Listener, die sich fuer den
     * Namen registriert haben. Die values werden einzeln an die
     * Listener "geschickt".
     */
    boolean dispatch(String name, String[] values);
    void dispatchDone();
    String getUniquePrefix();
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
