package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.style.*;
import org.wings.io.Device;

/**
 * Utils.java
 *
 *
 * Created: Thu Oct 28 16:23:53 1999
 *
 * @author Holger Engels
 * @version $Revision$
 */
public final class Utils implements SConstants
{
    public static void writeStyleAttribute(Device d, String prefix, Style style)
        throws IOException
    {
	if (style == null)
	    return;
	String id = style.getID();
	if (id != null)
	    writeStyleAttribute(d, prefix + id);
    }
    
    public static void writeStyleAttribute(Device d, Style style, String postfix)
        throws IOException
    {
	if (style == null)
	    return;
	String id = style.getID();
	if (id != null)
	    writeStyleAttribute(d, id + postfix);
    }
    
    public static void writeStyleAttribute(Device d, Style style)
        throws IOException
    {
	if (style == null)
	    return;
	writeStyleAttribute(d, style.getID());
    }
    
    public static void writeStyleAttribute(Device d, String id)
        throws IOException
    {
	if (id == null)
	    return;
	
	d.append(" class=\"");
	d.append(id);
	d.append("\"");
    }
} // Utils
