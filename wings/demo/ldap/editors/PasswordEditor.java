package ldap.editors;

import java.util.*;
import java.util.logging.*;
import javax.naming.*;
import javax.naming.directory.*;

import org.wings.*;
import org.wings.session.*;


public class PasswordEditor
    implements Editor
{
    private final static Logger logger = Logger.getLogger("ldap.editors");

    public SComponent createComponent(Attributes attributes) {

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

	SPanel panel = new SPanel(new SFlowDownLayout());
	SPasswordField c = new SPasswordField();
	c.setMaxColumns(cols);
	c.setColumns(Math.min(cols, 60));
	panel.add(c);

	c = new SPasswordField();
	c.setMaxColumns(cols);
	c.setColumns(Math.min(cols, 60));
	panel.add(c);

	return panel;
    }

    public void setValue(SComponent component, Attribute attribute)
	throws NamingException
    {
    }

    public Attribute getValue(SComponent component, String id)
	throws NamingException
    {
	SPanel panel = (SPanel)component;
	SPasswordField p1 = (SPasswordField)panel.getComponent(0);
	SPasswordField p2 = (SPasswordField)panel.getComponent(1);
	String t1 = p1.getText();
	String t2 = p2.getText();
	p1.setText(null);
	p2.setText(null);

	if (t1 == null || t1.length() == 0 ||
	    t2 == null || t2.length() == 0)
	    return null;

	if (!t1.equals(t2)) {
	    logger.info("confirmation differs");
  	    throw new NamingException("confirmation differs");
        }

	return new BasicAttribute(id, t1);
    }
}
