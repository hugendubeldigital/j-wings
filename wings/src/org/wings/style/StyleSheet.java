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

package org.wings.style;

import java.io.InputStream;
import java.io.IOException;
import java.util.Set;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public interface StyleSheet
{
    Set styleSet();

    // ein InputStream ist das, womit ein Externalizer am besten umgehen kann
    InputStream getInputStream() throws IOException;

    // brauchen wir noch ein lastModified() oder so, damit der Externalizer entscheiden
    // kann, ober das File ueberhaupt schreiben muss?

    // we'll also need some getters for getting the Style associated
    // with a key
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
