package org.wings.plaf.xhtml;

import java.io.IOException;

import java.awt.Color;
import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class FileChooserCG implements org.wings.plaf.FileChooserCG
{
    private final static String propertyPrefix = "FileChooser" + ".";
    
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
	SFileChooser fileChooser = (SFileChooser)c;
	
	writePrefix(d, fileChooser);
	writeBody(d, fileChooser);
	writePostfix(d, fileChooser);
    }
    
    public void writePrefix(Device d, SFileChooser fileChooser)
	throws IOException
    {
	d.append("<input type=\"file\"");
    }
    
    public void writeBody(Device d, SFileChooser fileChooser)
	throws IOException
    {
	d.append(" name=\"")
	    .append(fileChooser.getNamePrefix())
	    .append("\"");
	
	int columns = fileChooser.getColumns();
	int maxColumns = fileChooser.getMaxColumns();
	String fileNameFilter = fileChooser.getFileNameFilter();
	
	if (columns > 0)
	    d.append(" size=\"")
		.append(columns)
		.append("\"");
	
	if (maxColumns > 0)
	    d.append(" maxlength=\"")
		.append(maxColumns)
		.append("\"");
	
	if (fileNameFilter != null)
	    d.append(" accept=\"")
		.append(fileNameFilter)
		.append("\"");
    }
    
    public void writePostfix(Device d, SFileChooser fileChooser)
	throws IOException
    {
	d.append("/>");
    }
}
