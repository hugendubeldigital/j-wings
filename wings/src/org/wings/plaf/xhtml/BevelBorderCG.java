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

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

/**
  * This handles border style for BevelBorderCG and EtchedBorderCG
  * because html-syntax is the same except the border-style
  * @author <a href="mailto:andre.lison@crosstec.de">Andre Lison</a>
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
		d.append( "border: ");
        d.append( b.getThickness() );
        d.append("px " );
        writeBorderStyle( d, b );
        d.append( ";" );
        
        /* color */
		d.append( "border-color: #" );
        if ( color != null )
			d.append( org.wings.plaf.xhtml.Utils.toColorString( b.getColor() ) );
		else
        	d.append( "000000" );
		d.append( ";" );
		
        /* padding */
        if ( insets == null ) return;
        d.append( "padding-top: " );
        d.append( insets.top );
        d.append( "px;padding-right: " );
        d.append( insets.right );
        d.append( "px;padding-left: " );
        d.append( insets.left );
        d.append( "px;padding-bottom: " );
        d.append( insets.bottom );
        d.append( "px;" );
     }

	/**
      * "outset" or "inset"
      */
	protected void writeBorderStyle( Device d, SBorder b )
    	throws IOException
     {
		d.append( ( ((SBevelBorder) b).getBevelType() == SBevelBorder.RAISED) ? "outset" : "inset" );
     }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
