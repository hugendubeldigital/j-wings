/*
 * $Id$
 */

package org.wings.externalizer;

import java.io.*;

import org.wings.externalizer.*;

public class TextObjectHandler
    implements ObjectHandler
{

    String extension = null;
    String mimeType = null;
    
    public TextObjectHandler(String mimeType, String extension) {
	this.mimeType = mimeType;
	this.extension = extension;
    }
    public TextObjectHandler(String mimeType) {
	this.mimeType = mimeType;
	this.extension = "txt";
    }
    public TextObjectHandler() {
	this.mimeType = "text/plain";
	this.extension = "txt";
    }
    
    public void setExtension(String extension) {
	this.extension = extension;
    }
    public String getExtension(Object obj) {
	return extension;
    }
    
    public  void setMimeType(String mimeType) {
	this.mimeType = mimeType;
    }
    public String getMimeType(Object obj) {
	return mimeType;
    }
    
    public boolean isStable(Object obj) {
	return true;
    }
    
    public void write(Object obj, java.io.OutputStream out)
	throws java.io.IOException
    {
	Reader reader = new StringReader((String)obj);
	Writer writer = new OutputStreamWriter(out);
	char[] buffer = new char[2048];
	int num;
	while ((num = reader.read(buffer)) > 0) {
	    writer.write(buffer, 0, num);
	}
	reader.close();
	writer.close();
    }
    
    public Class getSupportedClass() {
	return String.class;
    }
}
