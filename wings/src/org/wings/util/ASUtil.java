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

package org.wings.util;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author Andreas Gruener
 * @version $Revision$
 */
public class ASUtil
{
    /*
     * Tested ob ein Array ein Element enthaelt (mit equlas).
     */
    public static boolean inside(Object o, Object[] array) {
        if ( array==null )
            return false;
        for ( int i=0; i<array.length; i++ )
            if ( o==null && array[i]==null ||
                 o.equals(array[i]) )
                return true;

        return false;
    }

    /*
     * Tested ob ein Array ein Element enthaelt (Referenz).
     */
    public static boolean insideReference(Object o, Object[] array) {
        if ( array==null )
            return false;
        for ( int i=0; i<array.length; i++ )
            if ( o == array[i] )
                return true;

        return false;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
