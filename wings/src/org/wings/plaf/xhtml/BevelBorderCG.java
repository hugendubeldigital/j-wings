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

package org.wings.plaf.xhtml;

import java.io.IOException;

import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class BevelBorderCG
    extends DefaultBorderCG
{
	public void writeSpanAttributes( Device d, SBorder border )
    	throws IOException
     {
     	SBevelBorder b = ( SBevelBorder ) border;
		String borderStyle = ( b.getBevelType() == SBevelBorder.RAISED) ? "outset" : "inset";
     	java.awt.Insets insets = b.getInsets();
        
        /* thickness & type */
		d.append( "border: 1px " );
        d.append( borderStyle );
        d.append( ";" );
        
        /* padding */
        if ( insets == null ) return;
        d.append( "padding-top: " );
        d.append( insets.top );
        d.append( "px; padding-right: " );
        d.append( insets.right );
        d.append( "px; padding-left: " );
        d.append( insets.left );
        d.append( "px; padding-bottom: " );
        d.append( insets.bottom );
        d.append( "px;" );
     }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
