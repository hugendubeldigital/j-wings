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

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class LineBorderCG
    extends DefaultBorderCG
    implements org.wings.plaf.LineBorderCG
{
    public void writeSpanAttributes( Device d, SBorder border )
        throws IOException {
        SLineBorder b = ( SLineBorder ) border;
        java.awt.Color  color  = b.getColor();
        java.awt.Insets insets = b.getInsets();
        
        /* thickness & type */
        d.print( "border: " );
        d.print( b.getThickness() );
        d.print( "px solid;" );
        
        /* color */
        d.print( "border-color: #" );
        if ( color != null )
            d.print( org.wings.plaf.xhtml.Utils.toColorString( color ) );
        else
            d.print( "000000" );
        d.print( ";" );
        
        /* padding */
        if ( insets == null ) return;
        d.print( "padding-top: " );
        d.print( insets.top );
        d.print( "px;padding-right: " );
        d.print( insets.right );
        d.print( "px;padding-left: " );
        d.print( insets.left );
        d.print( "px;padding-bottom: " );
        d.print( insets.bottom );
        d.print( "px;" );
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
