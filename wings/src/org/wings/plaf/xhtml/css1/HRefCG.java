package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class HRefCG
    extends org.wings.plaf.xhtml.HRefCG
{
    protected void writeAnchorPrefix(Device d, SHRef hRef)
        throws IOException
    {
	String tooltip = hRef.getToolTipText();
	
	if (hRef.isEnabled()) {
	    d.append("<a href=\"").append(hRef.getReference()).append("\"");
	    
	    Utils.writeStyleAttribute(d, hRef.getStyle());
	    
	    if (hRef.getRealTarget() != null)
		d.append(" target=\"").append(hRef.getRealTarget()).append("\"");
	    
	    if (tooltip != null)
		d.append(" title=\"").append(tooltip).append("\"");
	    
	    d.append(">");
	}
    }
}
