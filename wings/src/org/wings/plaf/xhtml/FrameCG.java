package org.wings.plaf.xhtml;

import java.io.IOException;

import java.awt.Color;
import java.util.*;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class FrameCG
    implements org.wings.plaf.FrameCG
{
    private final static String propertyPrefix = "Frame" + ".";
    
    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent component) {
    }

    public void uninstallCG(SComponent c) {
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SFrame frame = (SFrame)c;
        writeHeader(d, frame);
        writeBody(d, frame);
    }

    protected void writeHeader(Device d, SFrame frame)
        throws IOException
    {
	String language = "en"; // TODO: ???
	String title = frame.getTitle();
	List metas = frame.metas();
	List headers = frame.headers();
	
	d.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n");
	d.append("<!DOCTYPE html\n");
	d.append("   PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n");
	d.append("   \"DTD/xhtml1-strict.dtd\">\n");
	d.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"");
	d.append(language);
	d.append("\" lang=\"");
	d.append(language);
	d.append("\">\n");
	d.append("<head>\n<title>");
	d.append(title);
	d.append("</title>\n");
	
	d.append("<meta http-equiv=\"Content-type\" content='text/html; charset=\"iso-8859-1\"' />\n");
	d.append("<meta http-equiv=\"expires\" content=\"0\" />\n");
	d.append("<meta http-equiv=\"pragma\" content=\"no-cache\" />\n");
	
	Iterator it = metas.iterator();
	while (it.hasNext()) {
	    d.append("<meta ");
	    d.append(it.next());
	    d.append(" />\n");
	}
	
	// JavaScript ist in Kommentaren nicht mehr erlaubt, weil xml-Parser Kommentare entfernen dürfen
	
	it = headers.iterator();
	while (it.hasNext()) {
	    d.append(it.next());
	    d.append("\n");
	}
	
	writeAdditionalHeaders(d, frame);
	
	d.append("</head>\n");
    }
    
    protected void writeAdditionalHeaders(Device d, SFrame frame)
        throws IOException {}
    
    protected void writeBody(Device d, SFrame frame)
        throws IOException
    {
	d.append("<body>");
	writeContents(d, frame);
	d.append("\n</body>\n</html>");
    }
    
    protected void writeContents(Device d, SFrame frame)
        throws IOException
    {
	if (frame.isVisible()) {
	    frame.getLayout().write(d);
	}
    }
}
