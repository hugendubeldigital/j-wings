package ldap;

import javax.naming.*;
import javax.naming.directory.*;

import org.wings.*;
import ldap.editors.*;

class Row
{
    public String id;
    public SLabel label;
    public Editor editor;
    public SLabel message;
    public SComponent component;
    public int maymust;

    public Row(Attributes attributes, int maymust)
	throws NamingException
    {
	this.id = (String)attributes.get("NAME").get(0);
	this.label = new SLabel(this.id);
	this.editor = EditorFactory.getEditor(attributes);
	this.message = new SLabel();
	this.component = editor.createComponent(attributes);
	this.maymust = maymust;
            
	if (maymust == LDAP.MUST)
	    label.setAttribute("font-weight", "bold");

	this.message.setAttribute("color", "red");
    }
}
