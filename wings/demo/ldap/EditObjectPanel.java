package ldap;

import java.awt.event.*; 
import java.util.*;
import java.util.logging.*;
import javax.naming.*;
import javax.swing.tree.*;
import javax.naming.directory.*;

import org.wings.*;
import org.wings.session.*;

public class EditObjectPanel
    extends SForm
{
    private final static Logger logger = Logger.getLogger("ldap");

    ResourceBundle objectClassBundle;
    ResourceBundle attributeBundle;

    Attributes backedAttributes;

    SLabel dnLabel;

    AttributesEditor editor;
    SButton okButton;
    SButton removeButton;
    SResetButton cancelButton;

    String dn;

    public EditObjectPanel()
        throws NamingException
    {
        super(new SFlowDownLayout());
        setEncodingType("multipart/form-data");
        
        //objectClassBundle = ResourceBundle.getBundle("ldap.objectclass.names", getSession().getLocale());
        //attributeBundle = ResourceBundle.getBundle("ldap.attribute.names", getSession().getLocale());

        dnLabel = new SLabel();
        add(dnLabel);
        
        editor = new AttributesEditor();
        add(editor);

        SPanel buttons = new SPanel(null);
        buttons.setAttribute("align", "right");

        okButton = new SButton("ok");
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    ok();
                }
            });
        buttons.add(okButton);

        removeButton = new SButton("remove");
        removeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    remove();
                    //editor.removeAll();
                }
            });
        buttons.add(removeButton);

        cancelButton = new SResetButton("cancel");
        buttons.add(cancelButton);

        add(buttons);
    }

    public void setDn(String dn)
	throws NamingException
    {
        this.dn = dn;

	Attributes attributes = getContext().getAttributes(dn);

	Attribute objectClassAttribute = attributes.get("objectClass");
	String objectClass = (String)objectClassAttribute.get();
	List definitions = LDAP.getClassDefinitions(getSchema(), objectClass);

	editor.clearClassDefinitions();
	Iterator it = definitions.iterator();
	while (it.hasNext()) {
	    Attributes definition = (Attributes)it.next();
	    if (!"TOP".equalsIgnoreCase((String)definition.get("NAME").get(0)))
		editor.addClassDefinition(definition);
	}
	editor.setData(attributes);

        dnLabel.setText(dn + " [" + objectClass + "]");
	backedAttributes = attributes;
    }

    protected void ok() {
	try {
            
            Attributes attributes = editor.getData();

	    List modifications = new LinkedList();
	    NamingEnumeration enum = attributes.getAll();
	    while (enum.hasMore()) {
		Attribute editedAttribute = (Attribute)enum.next();
		Attribute backedAttribute = backedAttributes.get(editedAttribute.getID());

		if (editedAttribute.equals(backedAttribute))
		    continue;

		modifications.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, editedAttribute));
	    }

	    ModificationItem[] items
		= (ModificationItem[])modifications.toArray(new ModificationItem[modifications.size()]);
	    getContext().modifyAttributes(dn, items);
	}
	catch (InvalidAttributeValueException e) {
	    String message = e.getMessage();
	    if (message != null) {
		int minus = message.indexOf("-");
		int colon = message.indexOf(':', minus);
		if (minus != -1 && colon != -1) {
		    String id = message.substring(minus + 2, colon);
		    editor.addMessage(id, "invalid value");
		}
	    }
	}
	catch (AttributeModificationException e) {
	    logger.log(Level.WARNING, "modify failed", e);

	    ModificationItem[] items = e.getUnexecutedModifications();
	    if (items != null)
		for (int i=0; i < items.length; i++)
		    editor.addMessage(items[i].getAttribute().getID(), "schema violation");
	}
	catch (NamingException e) {
	    logger.log(Level.WARNING, "modify failed", e);
	}
        catch (Exception e) {
        }
    }

    protected void remove() {
	try {
            STree tree = (STree)SessionManager.getSession().getProperty("tree");
            
            LdapTreeNode node =
                (LdapTreeNode)tree.getLastSelectedPathComponent();
                        
            if (node.isLeaf()) {
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
                model.removeNodeFromParent(node);
                //model.nodesWereInserted(parentNode, new int[]
                //  {parentNode.getChildCount()-1});
                
                getContext().destroySubcontext(dn);
                editor.clearClassDefinitions();
                editor.removeAll();
            }
            else
                System.out.println("bitte child auswaehlen");
	}
	catch (NamingException e) {
	    logger.log(Level.WARNING, "remove failed", e);
	}
    }

    private DirContext context = null;

    protected DirContext getContext()
        throws NamingException
    {
        if (context == null) {
            Session session = getSession();
	    context = new InitialDirContext(new Hashtable(getSession().getProperties()));
        }
        return context;
    }

    private DirContext schema = null;

    protected DirContext getSchema()
        throws NamingException
    {
        if (schema == null) {
            Session session = getSession();
	    schema = getContext().getSchema("");
        }
        return schema;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */




