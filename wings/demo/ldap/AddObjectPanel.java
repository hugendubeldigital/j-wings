package ldap;

import java.awt.event.*; 
import java.util.*;
import java.util.logging.*;
import javax.naming.*;
import javax.naming.directory.*;

import org.wings.*;
import org.wings.session.*;

public class AddObjectPanel
    extends SForm
{
    private final static Logger logger = Logger.getLogger("ldap");

    private static String OBJECTCLASS = "objectClass";
    
    ResourceBundle objectClassBundle;
    ResourceBundle attributeBundle;

    Attributes emptyAttributes = new BasicAttributes();

    STextField dnTextField;
    SList objectClassList;
    AttributesEditor editor;
    SButton addButton;
    SButton okButton;
    SButton cancelButton;

    String parent = (String)SessionManager.getSession().getProperty("java.naming.provider.basedn");

    public AddObjectPanel()
        throws NamingException
    {
        super(new SFlowDownLayout());
        setEncodingType("multipart/form-data");
        //objectClassBundle = ResourceBundle.getBundle("ldap.objectclass.names", getSession().getLocale());
        //attributeBundle = ResourceBundle.getBundle("ldap.attribute.names", getSession().getLocale());

        dnTextField = new STextField();
        dnTextField.setAttribute("width", "100%");
        add(dnTextField);

        objectClassList = new SList();
        objectClassList.setAttribute("width", "100%");
        objectClassList.setListData(LDAP.getObjectClasses(getSchema()));
        objectClassList.setSelectionMode(SINGLE_SELECTION);
        //objectClassList.setCellRenderer(new I18NCellRenderer());
        add(objectClassList);

        editor = new AttributesEditor();
        add(editor);

        SPanel buttons = new SPanel(null);
        buttons.setAttribute("align", "right");

        addButton = new SButton("add");
        addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    add();
                }
            });
        buttons.add(addButton);

        okButton = new SButton("ok");
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    ok();
                }
            });
        buttons.add(okButton);

        cancelButton = new SButton("cancel");
        cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    cancel();
                }
            });
        buttons.add(cancelButton);

        add(buttons);

        editor.setVisible(false);
        okButton.setVisible(false);
        cancelButton.setVisible(false);
    }

    public void setParent(String parent) {
        this.parent = parent;
        dnTextField.setText(parent);
    }

    protected void add() {
        String objectClass = (String)objectClassList.getSelectedValue();
        try {
            List definitions = LDAP.getClassDefinitions(getSchema(), objectClass);
            editor.clearClassDefinitions();
            BasicAttribute ocAttr = new BasicAttribute(OBJECTCLASS);
            ocAttr.add(objectClass);
            editor.setObjectClass(ocAttr);
            
            Iterator it = definitions.iterator();
            while (it.hasNext()) {
                Attributes definition = (Attributes)it.next();
                if (!"TOP".equals(definition.get("NAME").get(0)))
                    editor.addClassDefinition(definition);
            }
            addButton.setVisible(false);
            objectClassList.setVisible(false);
            editor.setVisible(true);
            okButton.setVisible(true);
            cancelButton.setVisible(true);
        }
	catch (NamingException e) {
            logger.log(Level.SEVERE, "objectClass: " + objectClass, e);
	}
    }

    protected void ok() {
        try {
            String dn = dnTextField.getText();
            String objectClass = (String)objectClassList.getSelectedValue();

            Attributes attributes = editor.getData();
            Attributes specifiedAttributes = new BasicAttributes();
            NamingEnumeration enum = attributes.getAll();
	    while (enum.hasMore()) {
		Attribute attribute = (Attribute)enum.next();
                if (attribute.size() != 0)
                    specifiedAttributes.put(attribute);
            }

            specifiedAttributes.put(new BasicAttribute("objectClass", objectClass));
	    getContext().createSubcontext(dn, specifiedAttributes);

            editor.setData(emptyAttributes);
            editor.setVisible(false);
            okButton.setVisible(false);
            cancelButton.setVisible(false);
            addButton.setVisible(true);
            objectClassList.setVisible(true);
            
                        
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
	catch (NamingException e) {
            logger.log(Level.SEVERE, null, e);
	}
    }

    protected void cancel() {
        editor.setVisible(false);
        okButton.setVisible(false);
        cancelButton.setVisible(false);
        addButton.setVisible(true);
        objectClassList.setVisible(true);
        try {
            editor.setData(emptyAttributes);
        }
	catch (NamingException e) {
            logger.log(Level.WARNING, "ignorable", e);
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
