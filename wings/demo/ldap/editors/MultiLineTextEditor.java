package ldap.editors;

import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;

import org.wings.*;
import org.wings.session.*;


public class MultiLineTextEditor
    implements Editor
{
    public SComponent createComponent(Attributes attributes) {
	STextArea c = new STextArea();
	c.setColumns(60);
	c.setRows(3);
	return c;
    }

    public void setValue(SComponent component, Attribute attribute)
	throws NamingException
    {
	if (attribute == null) {
	    ((STextArea)component).setText(null);
	    return;
	}

	if (attribute.size() == 1)
	    ((STextArea)component).setText("" + attribute.get(0));
    }

    public Attribute getValue(SComponent component, String id) {
	String value = ((STextArea)component).getText();
	Attribute attribute = new BasicAttribute(id);
	if (value != null && value.length() > 0)
	    attribute.add(value);
	return attribute;
    }
}

