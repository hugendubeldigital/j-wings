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

package org.wings;

import java.io.*;
import java.net.URL;
import org.wings.io.Device;
import org.wings.SFrame;

/**
  * Javascript object which can be included in a Page-Header.
  * You can specify one of three sources:
  * <li>{@link java.io.File}</li>
  * <li>{@link java.net.URL}</li>
  * <li>{@link java.lang.String}</li>
  * <li>{@link java.lang.Package} and filename</li>
  * @see org.wings.SFrame#addJavascript(org.wings.Javascript)
  * @author <a href="mailto:andre@lison.de">Andre Lison</a>
  * @version $Revision$
  */
public class Javascript
{
    
    protected File jsfile = null;
    protected String jsstring = null;
    protected URL jsurl = null;
    protected ClassLoader classloader = null;
    protected String jsresourcename = null;
    
    
    /**
      * Deny creation of Javascript with empty parameters
      */
    private Javascript() {
    }

    /**
      * Create a new javascript object from
      * an external js-file.
      * @param jsfile the readable javascript file
      * @exception java.io.IOException if file can not be read
      * @exception java.lang.SecurityException If a security manager exists 
      *     and its SecurityManager.checkRead(java.io.FileDescriptor) method
      *     denies read access to the file
      */
    public Javascript(File jsfile)
    throws
        java.lang.SecurityException,
        java.io.IOException
     {
        if (!jsfile.canRead())
            throw new IOException("Can not read js-file " + jsfile.getAbsolutePath());
        this.jsfile = jsfile;
     }

    /**
      * Create a new javascript object from this string.
      * It is not neccessary to enclose the script
      * in <code><Script ..>...</Script></code>.
      * @param jsstring the javascript
      */
    public Javascript(String jsstring)
     {
        this.jsstring = jsstring;
        if (!this.jsstring.toLowerCase().startsWith("<script"))
            this.jsstring = "<SCRIPT Language=\"Javascript\">\n" + this.jsstring;
        if (!jsstring.toLowerCase().endsWith("</script>"))
            this.jsstring += "\n</SCRIPT>\n";
     }

    /**
      * Create a new javascript object from a resource.
      * @param cl the {@link java.lang.ClassLoader} to use
      * @param jsresourcename the full qualified js-resource-name
      */
    public Javascript(ClassLoader cl, String jsresourcename)
     {
        this.classloader = cl;
        this.jsresourcename = jsresourcename;
     }

    /**
      * Create a new javascript object from
      * an external js-url.
      * @param jsurl the extern url
      */
    public Javascript(URL jsurl)
     {
        this.jsurl = jsurl;
     }
     
    /**
      * Write the js-code to the device.
      * @param d the device to write to
      * @param frame needed to get ExternalizeManager to externalize js-file
      */
    public void write(Device d, SFrame frame)
        throws IOException
     {
        if (jsstring != null) {
                d.append(jsstring);
        }
        if (jsfile != null || jsurl != null || jsresourcename != null) {
            d.append("<script Language=javascript src=\"");
            if (jsfile != null || jsresourcename != null) {
                d.append(frame.getExternalizeManager().externalize(this));
            }
            else
            if (jsurl != null) {
                d.append(jsurl.toExternalForm());
            }
            d.append("\"></script>\n");
        }
     }
     
    /**
     * Get the input stream, if Javascript object was
     * constructed from {@link java.io.File} or resource.
     * @return the InputStream if object was constructed from file
     *      or resource, <code>null</code> otherwise.
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException {
        if ( jsfile != null )
            return new FileInputStream(jsfile);
        else
        if ( jsresourcename != null) {
            InputStream is = classloader.getResourceAsStream(jsresourcename);
            if (is == null)
                throw new IOException("Can not find resource " + jsresourcename);
            return is;
        }
        return null;
    }
    
    
    public String toString() {
        return "Javascript[jsfile: " + jsfile +", " +
            "jsstring: " + jsstring +",  " +
            "jsurl: " + jsurl + ", " +
            "classloader: " + classloader + ", " +
            "jsresourcename: " + jsresourcename +"]";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
