package ldap;

import org.wings.*;
import java.awt.event.*; 
import java.awt.Color;
import java.util.Vector; 
import java.util.ArrayList; 
import java.util.Hashtable; 
import java.util.Enumeration; 
import java.util.StringTokenizer; 
import javax.swing.event.ListSelectionListener; 
import javax.swing.event.ListSelectionEvent;
import javax.swing.tree.DefaultTreeModel;


public class AddObjectsPanel
    extends SPanel

    implements ActionListener, ListSelectionListener 
{
    SList objList;
    SPanel attributePanel  =new SPanel();
    ArrayList obj = new ArrayList();
    LdapWorker worker = null;
    STree tree = null;
    SForm attrForm;
    SForm objForm;
    SButton addEntry;
    SButton submit;
    SButton tisconUser;
    SButton tisconOClasses;
    Hashtable comp  =new Hashtable();
    STextField dnText;
    SButton suspend = new SButton("suspend");
    SForm tisconUserForm;
    SForm tisconUserOClForm;
    
	public AddObjectsPanel() {
	setLayout(new SFlowDownLayout());
	SLabel objLabel = new SLabel("Object Classes");

	objForm = new SForm();
	objForm.setBorder(new SLineBorder());
	tisconUserForm = new SForm();
	tisconUserForm.setBorder(new SLineBorder());
	tisconUserOClForm = new SForm();
	tisconUserOClForm.setBorder(new SLineBorder());
	objList = new SList();
	objList.setSelectionMode(MULTIPLE_SELECTION);
	objList.addListSelectionListener(this);
	objList.setVisibleRowCount(9);

	submit = new SButton("submit");
	tisconUser = new SButton("tisconUser");
	tisconUser.addActionListener(this);
	tisconOClasses = new SButton("tisconUserAllAttributes");
	tisconOClasses.addActionListener(this);
	submit.addActionListener(this);
	objForm.add(objLabel);
	objForm.add(objList) ;
	objForm.add(submit) ;
	tisconUserForm.add(tisconUser);
	tisconUserOClForm.add(tisconOClasses);
	    
	attrForm = new SForm(new SGridLayout(0,2));
    
	add(objForm);
	add(tisconUserForm);
	add(tisconUserOClForm);
	add(attrForm);

	suspend.addActionListener(this);
    }

    public void setWorker(LdapWorker worker) {
	if (worker != null) {
	    this.worker = worker;

	    ArrayList objects = worker.getObjects();
	    objList.setListData(objects.toArray());
	}
    }


    public void setTree(STree tree) {
	if (tree != null) {
	    this.tree = tree;
	}
    }
    
    
    private void fillTisconPanel () {
	attrForm.removeAll();
	attrForm.setLayout(new SGridLayout(0,2));
	SLabel dnLabel = new SLabel();
	dnText = new STextField("");
	STextField val;
	String tval;

	attrForm.add(dnLabel);
	attrForm.add(dnText);
	
	TisconUser tu = new TisconUser();
	String [] attrs = tu.attributes;
	String [] objectclasses  = tu.objectClasses;
	for (int i =0; i<attrs.length;i++)
	    {
		SLabel label = new SLabel(attrs[i]);
		if (attrs[i].toLowerCase().equals("objectclass")) {
		    tval = "";
		    String [] oc = tu.objectClasses;
		    for (int j= 0; j< oc.length; j++) {
			if (!tval.equals(""))
			    tval = tval + "," + oc[j];
			else tval = oc[j];
		    }
		    val = new STextField(tval); 
		}
		else
		    val = new STextField("");
		val.setColumns(35);
		attrForm.add(label);
		attrForm.add(val);
		comp.put(label,val);
	    }
	
	addEntry = new SButton("addEntry");
	addEntry.addActionListener(this);
	attrForm.add(addEntry);
	attrForm.add(suspend);
	
	
	
    }

    private void fillAttributePanel(ArrayList selectedObjects) {
	Hashtable attr;
	ArrayList values;
	attr = worker.getAttributes(selectedObjects);
	
	String atv;
	attrForm.removeAll();
	attrForm.setLayout(new SGridLayout(0,2));

	SLabel dnLabel = new SLabel("dn");
	
	dnText = new STextField("");
	attrForm.add(dnLabel);
	attrForm.add(dnText);
	    
	
	SLabel al;
	if (attr!=null) {
	    Enumeration e = attr.keys();
	    while(e!=null && e.hasMoreElements()) {
		Object key = e.nextElement();
		System.out.println(key + ":");
		String label = (String)key;
		if (label.substring(0,1).equals("*")) {
		    al  =new SLabel(label.substring(1));
		    al.setBorder(new SLineBorder());
		}
		else {
		    al = new SLabel(label);
		}
		atv = new String("");
		values = (ArrayList)attr.get(key);
		int i = 0;
		while(i<values.size()) {
		    atv = atv.concat((String)values.get(i) + ",");
			
			System.out.println(values.get(i));
			System.out.println(atv);
			i++;
		}
		if (atv.length() > 1) {
		    int l = atv.lastIndexOf(",");
		atv = atv.substring(0,l);
		}
		STextField tf = new STextField(atv);
		System.out.println("text field " + atv);
		attrForm.add(al);
		tf.setColumns(33);
		attrForm.add(tf);
		comp.put(al,tf);
	    }
	    SLabel addEntryL = new SLabel("adding new Entry"); 
	    addEntryL.setBackground(Color.blue);
	    addEntry = new SButton("addEntry");
	    addEntry.addActionListener(this);
	    attrForm.add(addEntry);
	    attrForm.add(suspend);
	}
    }
    
    public void actionPerformed(ActionEvent evt) {
	
	if ((SButton)(evt.getSource()) == submit) {
	    System.out.println("submit");
	    int i = 0;
	    while (obj!=null && i< obj.size()) {
		System.out.println(obj.get(i));
		i++;
	}
	    
	    fillAttributePanel(obj);
	    objForm.setVisible(false);
	    tisconUserForm.setVisible(false);
	    tisconUserOClForm.setVisible(false);
	    attrForm.setVisible(true);
    }

	if ((SButton)(evt.getSource()) == tisconUser) {
	    fillTisconPanel();
	    objForm.setVisible(false);
	    tisconUserForm.setVisible(false);
	    tisconUserOClForm.setVisible(false);
	    attrForm.setVisible(true);
	}
	

	if ((SButton)(evt.getSource()) == suspend) {
	    
	    objForm.setVisible(true);
	    tisconUserForm.setVisible(true);
	    tisconUserOClForm.setVisible(true);
	    attrForm.setVisible(false);
	}
	
	

	if ((SButton)(evt.getSource()) == tisconOClasses) {
	    TisconUser tu = new TisconUser();
	    String [] objectclasses  = tu.objectClasses;
	    ArrayList obj = new ArrayList();
	    for (int i = 0;i < objectclasses.length;i++) {
		obj.add(objectclasses[i]);
	    }
	    objForm.setVisible(false);
	    tisconUserForm.setVisible(false);
	    tisconUserOClForm.setVisible(false);
		
	    fillAttributePanel(obj);
	    attrForm.setVisible(true);
	}
	

	if ((SButton)(evt.getSource()) == addEntry) {
	    Hashtable vals = new Hashtable();
	    Enumeration e;
	    e = comp.keys();
	    while (e!=null && e.hasMoreElements()) {
		Object key = e.nextElement();
		String ats = ((SLabel)key).getText();
		System.out.println("label" + ats);
		STextField tf = (STextField)comp.get(key);
		String val = tf.getText();
		System.out.println("mit val" + val);
		if (val.length() > 0) {
		    System.out.println("auch was drin");
		    StringTokenizer tok = new StringTokenizer(ats,"(");
		    String at = tok.nextToken();
		    vals.put(at,val);
		}
	    }
	    System.out.println("dn ist" + dnText.getText());
	    DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
	    LdapTreeNode root = (LdapTreeNode)model.getRoot();
	    LdapTreeNode parent = getParentForNewNode(root, dnText.getText().trim());
	    LdapTreeNode newNode = new LdapTreeNode(worker, parent,dnText.getText().trim());
	    parent.addChild(newNode);
	    model.nodesWereInserted(parent, new int[] {parent.getChildCount()-1});
	    worker.addNewEntry(dnText.getText().trim(),vals); 
	    
	    objForm.setVisible(true);
	    tisconUserForm.setVisible(true);
	    tisconUserOClForm.setVisible(true);
	    attrForm.setVisible(false);
	}
    }

    private LdapTreeNode getParentForNewNode(LdapTreeNode parent,String dn) {
	LdapTreeNode node;
	String parentDN = parent.getDN();
	Enumeration children = parent.children();	
	while (children!=null && children.hasMoreElements()) {
	    node = (LdapTreeNode)children.nextElement();
	    if (partOf(node.getDN(),dn)) {
		parent = node;
		getParentForNewNode(node,dn);
	    }
	}
	
	System.out.println("unterhalb von " + parent.getDN());
	return parent;
    }
    

    private boolean partOf(String a, String inB ) {

	a = a.trim();
	inB = inB.trim();
	StringTokenizer sta = new StringTokenizer(a," ");
	StringTokenizer stb = new StringTokenizer(inB, " ");
	String anotherA = "";
	String anotherInB = "";
	while (sta.hasMoreTokens()) anotherA.concat(sta.nextToken ());
	while (stb.hasMoreTokens()) anotherInB.concat(stb.nextToken());
	if (anotherA.equals(""))
	    anotherA = a;
	if (anotherInB.equals(""))
	    anotherInB = inB;
	System.out.println(" a is "+ anotherA);
	System.out.println("b is " + anotherInB);
	if (anotherInB.endsWith(anotherA)) return true;
	return false;
	
    }


    private void setSelectedObjects(Object [] elements) {
	obj.clear();
	for (int i = 0; i < elements.length; i++) {
	    obj.add(elements[i]);
	}
    }

    public void valueChanged(ListSelectionEvent evt) {
	SList source = (SList)evt.getSource();
	Object [] elements = source.getSelectedValues();
	System.out.println("valueChanged ...");
	setSelectedObjects(elements);
    }
	
}
