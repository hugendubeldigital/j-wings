package org.wings.plaf.xhtml;

import java.io.IOException;

import java.awt.Color;
import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class TextFieldCG implements org.wings.plaf.TextFieldCG
{
    private final static String propertyPrefix = "TextField" + ".";
    
    protected String getPropertyPrefix() {
        return propertyPrefix;
    }
    
    public void installCG(SComponent component) {
	component.setStyle(component.getSession().getCGManager().getStyle(propertyPrefix + "style"));
    }
    public void uninstallCG(SComponent c) {}
    
    private StringBuffer buffer = new StringBuffer();
    
    public void write(Device d, SComponent c)
        throws IOException
    {
	STextField textField = (STextField)c;
	
	d.append("<input type=\"text\"");
	d.append(" size=\"").append(textField.getColumns());
	d.append("\" maxlength=\"").append(textField.getMaxColumns());
	d.append("\"");
	
	if (!textField.isEditable())
	    d.append(" readonly=\"1\"");
	
	String text = textField.getText();
	if (text != null) {
	    int pos=-1, lastpos=0;
	    buffer.setLength(0);
	    while ((pos = text.indexOf("\"", pos+1)) != -1) {
		buffer.append(text.substring(lastpos, pos));
		buffer.append("&quot;");
		lastpos = pos+1;
	    }
	    if (buffer.length() > 0) {
		buffer.append(text.substring(lastpos));
		text = buffer.toString();
	    }
	    
	    d.append(" value=\"").append(text).append("\"");
	}
	
	if (textField.isEnabled()) {
	    d.append(" name=\"");
	    d.append(textField.getNamePrefix());
	    d.append("\"");
	}
	
	d.append(" />");
    }
}
