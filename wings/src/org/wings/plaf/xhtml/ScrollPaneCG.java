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

public class ScrollPaneCG
    extends org.wings.plaf.AbstractCG
    implements org.wings.plaf.ScrollPaneCG
{
    public void installCG(SComponent component) {
    }

    public void uninstallCG(SComponent component) {
    }
    
    public void write(Device d, SComponent c)
        throws IOException
    {
        SScrollPane scrollPane = (SScrollPane)c;
        if (scrollPane.getPreferredSize() == null && 
            ((SComponent)scrollPane.getScrollable()).getPreferredSize() != null
            )
            scrollPane.setPreferredSize(((SComponent)scrollPane.getScrollable()).getPreferredSize());
        updateScrollBars(scrollPane);
        
        writePrefix(d, scrollPane);
        ((SComponent) scrollPane.getScrollable()).write( d );
        writePostfix(d, scrollPane);
    }
    
    protected void updateScrollBars(SScrollPane scrollPane) {
        SScrollBar horizontalScroller = scrollPane.getHorizontalScrollBar();
        SScrollBar verticalScroller = scrollPane.getVerticalScrollBar();
        Scrollable scrollable = scrollPane.getScrollable();
        
        horizontalScroller.setBlockIncrement( scrollPane.getHorizontalExtent() - 1 );
        verticalScroller.setBlockIncrement( scrollPane.getVerticalExtent() - 1 );
        
        int policy = 0;
        int extent = 0;
        Dimension dim = scrollable.getScrollableViewportSize();
        
        System.out.println(":::SP.extent=" + scrollPane.getHorizontalExtent() + ", HSB.extents="+horizontalScroller.getVisibleAmount());
        
        // HORIZONTAL ScrollBar
        extent = scrollPane.getHorizontalExtent();
        if (dim.width != horizontalScroller.getMaximum() ||
            extent != horizontalScroller.getVisibleAmount())
        {
            int maximum = dim.width;
            if (maximum < extent)
                extent = maximum;
            int value = horizontalScroller.getValue();
            int maxValue = maximum - extent + 1;
            if (value > maxValue)
                value = maxValue;
            
            horizontalScroller.setValues(value, extent, 0, maximum);
        }
        // decide, whether to display or not
        policy = scrollPane.getHorizontalScrollBarPolicy();
        horizontalScroller.setVisible((dim.width > extent ||
                                       policy == scrollPane.HORIZONTAL_SCROLLBAR_ALWAYS) &&
                                       policy != scrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        
        // VERTICAL ScrollBar
        extent = scrollPane.getVerticalExtent();
        if (dim.height != verticalScroller.getMaximum()) {
            int maximum = dim.height;
            if (maximum < extent)
                extent = maximum;
            int value = verticalScroller.getValue();
            int maxValue = maximum - extent + 1;
            if (value > maxValue)
                value = maxValue;
            
            verticalScroller.setValues(value, extent, 0, maximum);
        }
        // decide, whether to display or not
        policy = scrollPane.getVerticalScrollBarPolicy();
        verticalScroller.setVisible((dim.height > extent ||
                                     policy == scrollPane.VERTICAL_SCROLLBAR_ALWAYS) &&
                                     policy != scrollPane.VERTICAL_SCROLLBAR_NEVER );
    }

    protected void writePrefix(Device d, SScrollPane scrollPane)
        throws IOException
    {
        SScrollBar horizontalScroller = scrollPane.getHorizontalScrollBar();
        SScrollBar verticalScroller = scrollPane.getVerticalScrollBar();
        SBorder border = scrollPane.getBorder();
        
    	int cspan = horizontalScroller.isVisible()
            ? ((ScrollBarCG) horizontalScroller.getCG()).SCROLLBAR_STEPS + 2 
            : 1;
    	int rspan = verticalScroller.isVisible()
            ? ((ScrollBarCG) verticalScroller.getCG()).SCROLLBAR_STEPS + 2 
            : 1;
        
        d.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"");
        if ( border != null ) {
            d.append("style=\"");
            border.writeSpanAttributes( d );
            d.append("\"");
        }
        d.append("><tr><td ");
        if ( cspan > 1 ) {
            d.append( "colspan=\"" );
            d.append( cspan );
            d.append( "\"" );
        }
        if ( rspan > 1 ) {
            d.append( " rowspan=\"" );
            d.append( rspan );
            d.append( "\"" );
        }
        d.append(">");
    }
    
    protected void writePostfix(Device d, SScrollPane scrollPane)
        throws IOException
    {
        SScrollBar horizontalScroller = scrollPane.getHorizontalScrollBar();
        SScrollBar verticalScroller = scrollPane.getVerticalScrollBar();

        d.append( "</td>" );
        verticalScroller.write( d );
        d.append( "</tr>" );
        horizontalScroller.write( d );
        d.append("<td width=\"0%\" height=\"0%\"></td></tr></table>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
