package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class CheckBoxCG
    extends org.wings.plaf.xhtml.CheckBoxCG
{
    protected void writeAnchorPrefix(Device d, SCheckBox checkBox)
        throws IOException
    {
	String tooltip = checkBox.getToolTipText();
	
	if (checkBox.isEnabled()) {
	    d.append("<a href=\"").append(generateAnchorAddress(checkBox)).append("\"");
	    
	    Utils.writeStyleAttribute(d, "anchor", checkBox.getStyle());
	    
	    if (checkBox.getRealTarget() != null)
		d.append(" target=\"").append(checkBox.getRealTarget()).append("\"");
	    
	    if (tooltip != null)
		d.append(" title=\"").append(tooltip).append("\"");
	    
	    d.append(">");
	}
    }
    
    protected void writeFormPrefix(Device d, SCheckBox checkBox)
        throws IOException
    {
	d.append("<input type=\"");
	d.append(checkBox.getType());
	d.append("\"");
	Utils.writeStyleAttribute(d, "form", checkBox.getStyle());
    }
}
