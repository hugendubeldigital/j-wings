package org.wings.plaf.xhtml;

import java.io.IOException;

import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.externalizer.ExternalizeManager;


public class ResetButtonCG implements org.wings.plaf.ResetButtonCG, SConstants
{
    private final static String propertyPrefix = "ResetButton" + ".";
    
    protected String getPropertyPrefix() {
        return propertyPrefix;
    }
    
    public void installCG(SComponent component) {
	component.setStyle(component.getSession().getCGManager().getStyle(propertyPrefix + "style"));
    }
    public void uninstallCG(SComponent c) {}
    
    public void write(Device d, SComponent c)
        throws IOException
    {
	SResetButton resetButton = (SResetButton)c;
	writeFormButton(d, resetButton);
    }
    
    protected void writeFormButton(Device d, SResetButton resetButton)
        throws IOException
    {
	String text = resetButton.getText();
	writeFormPrefix(d, resetButton);
	writeFormBody(d, resetButton);
	writeFormPostfix(d, resetButton);
    }
    
    protected void writeFormPrefix(Device d, SResetButton resetButton)
        throws IOException
    {
	d.append("<input type=\"reset\"");
    }
    
    protected void writeFormBody(Device d, SResetButton resetButton)
        throws IOException
    {
	String text = resetButton.getText();
	
	//d.append(" name=\"").append(resetButton.getNamePrefix()).append("\"");
	if (text != null)
	    d.append(" value=\"").
		append(text).
		append("\"");
    }
    
    protected void writeFormPostfix(Device d, SResetButton resetButton)
        throws IOException
    {
	d.append(" />\n");
    }
}
