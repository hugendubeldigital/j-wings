package ldap.editors;

import java.util.*;
import java.util.logging.*;
import javax.naming.*;
import javax.naming.directory.*;

import org.wings.*;
import org.wings.session.*;


public class SingleLineTextEditor
    implements Editor
{
  private final static Logger logger = Logger.getLogger("ldap.editors");
  
  private final String DELIM = "$";
  
  public SComponent createComponent(Attributes attributes) {
    STextField c = new STextField();
    int cols = 80;
	try {
          Attribute syntaxAttribute = attributes.get("SYNTAX");
          if (syntaxAttribute != null) {
		String syntax = (String)syntaxAttribute.get();
		System.err.println("SYNTAX: " + syntax);
		int open = syntax.indexOf("{");
		int close = syntax.indexOf("}");
		if (open != -1 && close != -1) {
		    syntax = syntax.substring(open + 1, close);
		    cols = new Integer(syntax).intValue();
		}
	    }
	}
	catch (NamingException e) {
            logger.log(Level.WARNING, "attributes " + attributes, e);
	}

	c.setMaxColumns(cols);
	c.setColumns(Math.min(cols, 60));
	return c;
    }

    public void setValue(SComponent component, Attribute attribute)
	throws NamingException
    {
	if (attribute == null) {
	    ((STextField)component).setText(null);
	    return;
	}

	if (attribute.size() == 1)
	    ((STextField)component).setText("" + attribute.get(0));
        
	if (attribute.size() > 1) {
          StringBuffer sb = new StringBuffer();
          for (int i = 0;i<attribute.size();i++)
            sb.append(attribute.get(i) + DELIM);
          ((STextField)component).setText(sb.toString().trim());
        }
    }

  public Attribute getValue(SComponent component, String id) {
    String value = ((STextField)component).getText();
    
    Attribute attribute = new BasicAttribute(id);
    
    if (value!=null && value.length() > 0) {
      StringTokenizer stk = new StringTokenizer(value,DELIM);
      while(stk.hasMoreTokens()) {
        attribute.add(stk.nextToken());
       }
    }
    return attribute;
  }
}


