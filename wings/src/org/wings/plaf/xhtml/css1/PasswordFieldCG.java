package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class PasswordFieldCG
    extends org.wings.plaf.xhtml.PasswordFieldCG
{
    private StringBuffer buffer = new StringBuffer();
    
    public void write(Device d, SComponent c)
        throws IOException
    {
	SPasswordField passwordField = (SPasswordField)c;
	Style style = passwordField.getStyle();
	
	d.append("<input type=\"password\"");
	d.append(" size=\"").append(passwordField.getColumns());
	d.append("\" maxlength=\"").append(passwordField.getMaxColumns());
	d.append("\"");
	Utils.writeStyleAttribute(d, style);
	
	if (!passwordField.isEditable())
	    d.append(" readonly=\"1\"");
	
	String text = passwordField.getText();
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
	
	if (passwordField.isEnabled()) {
	    d.append(" name=\"");
	    d.append(passwordField.getNamePrefix());
	    d.append("\"");
	}
	
	d.append(" />");
    }
}
