package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public class ButtonCG
    extends org.wings.plaf.xhtml.ButtonCG
{
    protected void writeAnchorPrefix(Device d, SButton button)
        throws IOException
    {
	String tooltip = button.getToolTipText();
	
	if (button.isEnabled()) {
	    d.append("<a href=\"").append(generateAnchorAddress(button)).append("\"");
	    
	    Utils.writeStyleAttribute(d, "anchor", button.getStyle());
	    
	    if (button.getRealTarget() != null)
		d.append(" target=\"").append(button.getRealTarget()).append("\"");
	    
	    if (tooltip != null)
		d.append(" title=\"").append(tooltip).append("\"");
	    
	    d.append(">");
	}
    }

    protected void writeFormPrefix(Device d, SButton button)
        throws IOException
    {
	d.append("<input type=\"submit\"");
	Utils.writeStyleAttribute(d, "form", button.getStyle());
    }
}
