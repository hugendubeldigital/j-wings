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

import java.awt.*;
import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class ScrollBarCG
    extends org.wings.plaf.AbstractCG
    implements org.wings.plaf.ScrollBarCG
{
    public void installCG(SComponent component)
    {
        
    }

    public void uninstallCG(SComponent component)
    {
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        
        SScrollBar sb = (SScrollBar)c;

        writePrefix(d, sb);
        writePostfix(d, sb);
    }

    protected void writePrefix(Device d, SContainer container)
        throws IOException
    {
    }

    protected void writePostfix(Device d, SContainer container)
        throws IOException
    {
    	SScrollBar sb = ( SScrollBar ) container;
        writeScrollBar( d, sb );
    }
    
    protected void writeScrollBar( Device d, SScrollBar sb )
    	throws IOException
     {
     	int p = (int) ( ( 100.0f - 2.0f ) / ( float) SCROLLBAR_STEPS );
		int s = (int) ( (float) ( 100.0f - SCROLLBAR_STEPS * p ) / 2.0f );
        
		String el_pre;
		String el_post;
        String el_s;
        String fwalign;
        String bwalign;

		if ( sb.getOrientation() == SConstants.HORIZONTAL )
         {
         	el_pre = "";
            el_post = "";
			el_s = " width=\"";
            fwalign = "valign=\"middle\" align=\"right\"";
            bwalign = "valign=\"middle\" align=\"left\"";
         }
		else
         {
         	el_pre = "<tr>";
            el_post = "</tr>";
            el_s = " height=\"";
            fwalign = "align=\"center\" valign=\"bottom\"";
            bwalign = "align=\"center\" valign=\"top\"";
         }

		// bw
		d.append( "<td bgcolor=\"#FFFFFF\" " );
        d.append( bwalign );
        d.append( el_s );
        d.append( "1\">" );
		sb.getComponentAt(0).write( d );
		d.append( "</td>" );
		d.append( el_post );
        
		writeSBBackground( d, sb, el_pre, el_post, el_s + p );
         
		// fw
		d.append( el_pre );
		d.append( "<td bgcolor=\"#FFFFFF\" " );
        d.append( fwalign );
        d.append( el_s );
        d.append( "1\">" );
		sb.getComponentAt(1).write( d );
		d.append( "</td>" );
     }
     
    
    /**
      * Write the scrollbar background.
      */
	public void writeSBBackground( Device d, SScrollBar sb, String prefix, String postfix, String size )
    	throws IOException
     {
     	int mark = (int) ( ( (double) sb.getValue() ) / ( (double) ( sb.getMaximum() - sb.getMinimum() - sb.getVisibleAmount()) / (double) SCROLLBAR_STEPS ) );
        if ( mark < 0 ) mark = 0;
        if ( mark > ( SCROLLBAR_STEPS - 1 ) ) mark = SCROLLBAR_STEPS - 1;
		for ( int i = 0; i < SCROLLBAR_STEPS; i++ )
         {
         	d.append( prefix );
            if ( mark == i )
				d.append( "<td bgcolor=\"#000000\" " );
            else
				d.append( "<td bgcolor=\"#FFFFFF\" " );
			d.append( size );
			d.append( "%\"><img src=\"blind.gif\" width=\"1\" height=\"1\"></td>" );
         	d.append( postfix );
		 }
     }

	public static final int SCROLLBAR_STEPS = 15;
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
