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

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;

/**
  * This handles border style for BevelBorderCG and EtchedBorderCG
  * because html-syntax is the same except the border-style
  * @author <a href="mailto:andre@lison.de">Andre Lison</a>
  * @see org.wings.plaf.xhtml.css1.EtchedBorderCG
  * @see #writeBorderStyle(Device,SBorder)
  */
public class BevelBorderCG
    extends DefaultBorderCG
{
	public void writeSpanAttributes( Device d, SBorder border )
    	throws IOException
     {
     	SAbstractBorder b = ( SAbstractBorder ) border;
     	java.awt.Insets insets = b.getInsets();
        java.awt.Color color = b.getColor();
        
        /* thickness & type */
		d.print( "border: ");
        d.print( b.getThickness() );
        d.print("px " );
        writeBorderStyle( d, b );
        d.print( ";" );
        
        /* color */
		d.print( "border-color: #" );
        if ( color != null )
			d.print( org.wings.plaf.xhtml.Utils.toColorString( b.getColor() ) );
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

	/**
      * "outset" or "inset"
      */
	protected void writeBorderStyle( Device d, SBorder b )
    	throws IOException
     {
		d.print( ( ((SBevelBorder) b).getBevelType() == SBevelBorder.RAISED) ? "outset" : "inset" );
     }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
