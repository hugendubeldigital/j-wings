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

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class ScrollPaneCG
    extends org.wings.plaf.AbstractComponentCG
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
        
        Dimension dim = scrollable.getScrollableViewportSize();
        if (dim.width != horizontalScroller.getMaximum()) {
            int maximum = dim.width;
            int extent = scrollPane.getHorizontalExtent();
            if (maximum < extent)
                extent = maximum;
            int value = horizontalScroller.getValue();
            int maxValue = maximum - extent + 1;
            if (value > maxValue)
                value = maxValue;
            
            horizontalScroller.setValues(value, extent, 0, maximum);
            policy = scrollPane.getHorizontalScrollBarPolicy();
            horizontalScroller.setVisible((maximum > extent || 
                                           policy == scrollPane.HORIZONTAL_SCROLLBAR_ALWAYS) &&
                                          policy != scrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        }
        if (dim.height != verticalScroller.getMaximum()) {
            int maximum = dim.height;
            int extent = scrollPane.getVerticalExtent();
            if (maximum < extent)
                extent = maximum;
            int value = verticalScroller.getValue();
            int maxValue = maximum - extent + 1;
            if (value > maxValue)
                value = maxValue;
            
            verticalScroller.setValues(value, extent, 0, maximum);
            policy = scrollPane.getVerticalScrollBarPolicy();
            verticalScroller.setVisible(
                                        ( 
                                         maximum > extent || 
                                         policy == scrollPane.VERTICAL_SCROLLBAR_ALWAYS
                                         ) &&
                                        policy != scrollPane.VERTICAL_SCROLLBAR_NEVER );
        }
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
        
        d.print("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"");
        if ( border != null ) {
            d.print("style=\"");
            border.writeSpanAttributes( d );
            d.print("\"");
        }
        d.print("><tr><td ");
        if ( cspan > 1 ) {
            d.print( "colspan=\"" );
            d.print( cspan );
            d.print( "\"" );
        }
        if ( rspan > 1 ) {
            d.print( " rowspan=\"" );
            d.print( rspan );
            d.print( "\"" );
        }
        d.print(">");
    }
    
    protected void writePostfix(Device d, SScrollPane scrollPane)
        throws IOException
    {
        SScrollBar horizontalScroller = scrollPane.getHorizontalScrollBar();
        SScrollBar verticalScroller = scrollPane.getVerticalScrollBar();

        d.print( "</td>" );
        verticalScroller.write( d );
        d.print( "</tr>" );
        horizontalScroller.write( d );
        d.print("</tr></table>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
