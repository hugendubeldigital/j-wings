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
import javax.swing.Icon;
import java.util.*;
import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.externalizer.*;

public class ScrollBarCG
    extends org.wings.plaf.AbstractCG
    implements org.wings.plaf.ScrollBarCG
{
    public static final int SCROLLBAR_STEPS        = 15;
    /*
     * these colors should be distinguishable, otherwise the scroller
     * looks wierd on some browsers: some browsers make very large
     * button columns which shouldn't be confused with the actual
     * Scroller.
     */
    public static final String SCROLLER_COLOR      = "\"#888888\" ";
    public static final String SCROLLER_BACKGROUND = "\"#FFFFFF\" ";
    public static final String BUTTON_BACKGROUND   = "\"#C6C6C6\" ";

    Icon transIcon;

    public void installCG(SComponent component) {
        super.installCG(component);
        transIcon = LookAndFeel.makeIcon(ScrollBarCG.class, 
                                         "/org/wings/icons/transdot.gif");
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
         String dummyIconAdr = null;
         
         ExternalizeManager ext = sb.getExternalizeManager();
         if (ext != null &&  transIcon != null) {
             try {
                 dummyIconAdr = ext.externalize(transIcon);
             }
             catch (java.io.IOException e) {
                 System.err.println(e.getMessage());
                 e.printStackTrace(System.err);
             }
         }
         
         if ( sb.getOrientation() == SConstants.HORIZONTAL ) {
             el_pre    = "";
             el_post   = "";
             el_s = " width=\"";
             fwalign = "valign=\"middle\" align=\"right\"";
             bwalign = "valign=\"middle\" align=\"left\"";
         }
         else {
             el_pre    = "<tr>";
             el_post   = "</tr>";
             el_s = " height=\"";
             fwalign = "align=\"center\" valign=\"bottom\"";
             bwalign = "align=\"center\" valign=\"top\"";
         }
         
         // bw
         d.append( "<td bgcolor=").append( BUTTON_BACKGROUND );
         d.append( bwalign );
         d.append( el_s );
         d.append( "1%\">" );
         sb.getComponentAt(0).write( d );
         d.append( "</td>" );
         d.append( el_post );
         
         writeSBBackground( d, sb, dummyIconAdr, el_pre, el_post, el_s + p );
         
         // fw
         d.append( el_pre );
         d.append( "<td bgcolor=").append( BUTTON_BACKGROUND );
         d.append( fwalign );
         d.append( el_s );
         d.append( "1%\">" );
         sb.getComponentAt(1).write( d );
         d.append( "</td>" );
     }
    
    
    private static final SDimension defaultDimensionVertical = new SDimension(7,200);
    private static final SDimension defaultDimensionHorizontal = new SDimension(200,7);
    /**
     * Write the scrollbar background.
     */
    public void writeSBBackground( Device d, SScrollBar sb, String fillIcon,
                                   String prefix, String postfix, String size )
    	throws IOException
    {
        float scrollerWidth = 0f;
        float scrollerHeight = 0f;
        
        SDimension sbDim = sb.getPreferredSize();
        Dimension scrollerDim = getScrollerSize(sb);
        if (sb.getOrientation() == SConstants.HORIZONTAL ) {
            if (sbDim == null ) sbDim = defaultDimensionHorizontal;
            scrollerWidth = ((float) sbDim.getIntWidth()-
                scrollerDim.width) /
                ((float) SCROLLBAR_STEPS);
            scrollerHeight=(float)scrollerDim.height;
        }
        else {
            if (sbDim == null ) sbDim = defaultDimensionVertical;
            scrollerHeight = ((float) sbDim.getIntHeight()-
                scrollerDim.height) /
                ((float) SCROLLBAR_STEPS);
            scrollerWidth=(float)scrollerDim.width;
        }
        
        int range = sb.getMaximum() - sb.getMinimum();
        int mark = (int) ( SCROLLBAR_STEPS * 
                           ((double)(sb.getValue() - sb.getMinimum()) / (double) range));
        int len   = (int) ( SCROLLBAR_STEPS * 
                            ((double) sb.getVisibleAmount() / (double) range));
        int bordercorrection = 0;

        if ( mark < 0 ) mark = 0;
        if ( mark > ( SCROLLBAR_STEPS - 1 ) ) mark = SCROLLBAR_STEPS - 1;
        if ( len < 1) len = 1;
        for ( int i = 0; i < SCROLLBAR_STEPS; ++i ) {
            d.append( prefix );
            if ( i >= mark && len-- > 0 ) {
                d.append( "<td style=\"border-width: 1px; border-style: outset;\" bgcolor=").append( SCROLLER_COLOR );
                bordercorrection = 2;
            }
            else {
                d.append( "<td bgcolor=").append( SCROLLER_BACKGROUND );
                bordercorrection = 0;
            }
            d.append( size ).append("%");
            d.append( "\"><img src=\"").append(fillIcon)
                .append("\" width=\"")
                .append((int) (scrollerWidth * (i+1)) - (int) (scrollerWidth * i) - bordercorrection)
                .append("\" height=\"")
                .append((int) (scrollerHeight * (i+1)) - (int) (scrollerHeight * i) - bordercorrection)
                .append("\"></td>" );
            d.append( postfix );
        }
    }
    
    /**
      * Get overall max width and height of scrollbar buttons.
      */
    protected Dimension getScrollerSize(SScrollBar sb) {
        int width = 0;
        int height = 0;
        for ( int s = 0; s < 2; s++ ) {
            SContainer scrollerp = (SContainer) sb.getComponentAt(s);
            int c = scrollerp.getComponentCount();
            if (sb.getOrientation() == SConstants.HORIZONTAL ) {
                for (int i = 0; i < c; i++ ) {
                    Icon icon = ((SButton) scrollerp.getComponentAt(i)).getIcon();
                    width+=icon.getIconWidth();
                    height=Math.max(icon.getIconHeight(), height);
                }
            }
            else {
                for (int i = 0; i < c; i++ ) {
                    Icon icon = ((SButton) scrollerp.getComponentAt(i)).getIcon();
                    width=Math.max(icon.getIconWidth(), width);
                    height+=icon.getIconHeight();
                }
            }
        }
        
        return new Dimension(width,height);
     }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
