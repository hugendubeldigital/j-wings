/*
  $Id$
  (c) Copyright 2002 mercatis information systems GmbH

  Part of e-lib 
 
  This file contains unpublished, proprietary trade secret information of
  mercatis information systems GmbH. Use, transcription, duplication and
  modification are strictly prohibited without prior written consent of
  mercatis information systems GmbH.
  See http://www.mercatis.de
*/

package org.wings.template;

import java.awt.Color;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.wings.ClasspathResource;
import org.wings.Resource;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SIcon;
import org.wings.SURLIcon;
import org.wings.style.AttributeSet;
import org.wings.style.CSSStyleSheet;
import org.wings.style.SimpleAttributeSet;
import org.wings.style.StyleSheet;
import org.wings.template.PropertyValueConverter;


/**
 * <!--
 * DefaultPropertyValueConverter.java
 * Created: Tue Aug  6 17:30:51 2002
 * -->
 *
 *
 *
 * <p><b>(c)2002 <a href="http://www.mercatis.de">mercatis information systems GmbH</a></b></p>
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class DefaultPropertyValueConverter  implements PropertyValueConverter  {

    private final static Logger logger = Logger.getLogger("org.wings.template");
    
    /**
     * 
     */
    public DefaultPropertyValueConverter() {
	
    }
    // implementation of org.wings.template.PropertyValueConverter interface

    /**
     * Describe <code>convertPropertyValue</code> method here.
     *
     * @param value an <code>Object</code> value
     * @param targetClass a <code>Class</code> value
     * @return <description>
     * @exception UnsupportedOperationException if an error occurs
     * @exception java.lang.UnsupportedOperationException <description>
     */
    public Object convertPropertyValue(String value, Class targetClass) 
	throws UnsupportedOperationException
    {
	if ( value==null || "null".equals(value) ) {
	    return null;
	} // end of if ()
	

	if ( targetClass==String.class ) {
	    return value;
	} // end of if ()

	if ( targetClass==Boolean.TYPE || targetClass==Boolean.class ) {
	    return Boolean.valueOf(value);
	}

	if ( targetClass==Integer.TYPE || targetClass==Integer.class ) {
	    return Integer.valueOf(value);
	}
	if ( targetClass==Long.TYPE || targetClass==Long.class ) {
	    return Long.valueOf(value);
	}
	if ( targetClass==Short.TYPE || targetClass==Short.class ) {
	    return Short.valueOf(value);
	}
	if ( targetClass==Byte.TYPE || targetClass==Byte.class ) {
	    return Byte.valueOf(value);
	}
	if ( targetClass==Float.TYPE || targetClass==Float.class ) {
	    return Float.valueOf(value);
	}
	if ( targetClass==Double.TYPE || targetClass==Double.class ) {
	    return Double.valueOf(value);
	}
	if ( targetClass==Character.TYPE || targetClass==Character.class ) {
	    return new Character(value.charAt(0));
	}

	if ( targetClass==StringBuffer.class ) {
	    return new StringBuffer(value);
	} // end of if ()

	if ( targetClass==SIcon.class ) {
	    return new SURLIcon(value);
	}

	if ( targetClass==Color.class ) {
	    try {
		return Color.decode(value);
	    } catch ( Exception ex ) {
		logger.log(Level.WARNING, "cannot convert color value", ex);
		return null;
	    }
	}

	if ( targetClass==SDimension.class ) {
            int commaIndex = value.indexOf(',');
            if ( commaIndex>0 ) {
                return new SDimension(value.substring(0, commaIndex),
                                      value.substring(commaIndex+1));
            }
	    return null;
	}

	if ( targetClass==SFont.class ) {
	    return TemplateUtil.parseFont(value);
	}

	if ( Resource.class.isAssignableFrom(targetClass) ) {
	    return new ClasspathResource(value);
	}

	if ( AttributeSet.class.isAssignableFrom(targetClass) ) {
	    AttributeSet attributes = new SimpleAttributeSet();
	    StringTokenizer tokens = new StringTokenizer(value, ";");
	    while (tokens.hasMoreTokens()) {
		String token = tokens.nextToken();
		int pos = token.indexOf(":");
		if (pos >= 0) {
		    attributes.put(token.substring(0, pos), 
				   token.substring(pos + 1));
		}
	    }
	    return attributes;
	}

	if ( StyleSheet.class.isAssignableFrom(targetClass) ) {
	    try {
		CSSStyleSheet styleSheet = new CSSStyleSheet();
		InputStream in = getClass().getClassLoader().getResourceAsStream(value);
		styleSheet.read(in);
		return styleSheet;
	    }
	    catch (Exception ex) {
		logger.log(Level.WARNING, "style sheet resource not found:" + value, ex);
		return null;
	    }
	}

	
	throw new UnsupportedOperationException();
    }
    
}// DefaultPropertyValueConverter

/*
   $Log$
   Revision 1.2  2002/11/01 14:17:50  ahaaf
   o add support for more types

   Revision 1.1  2002/08/06 16:45:55  ahaaf
   add DefaultPropertyManager using reflection and bean shell scripting support

*/
