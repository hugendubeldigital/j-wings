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

public class AddObjectsPanel
    extends SPanel
    implements ActionListener, ListSelectionListener 
{
    SList objList;
    SPanel attributePanel  =new SPanel();
    ArrayList obj = new ArrayList();
    LdapWorker worker = null;
    SForm attrForm;
    SButton addEntry;
    SButton submit;
    Hashtable comp  =new Hashtable();
    STextField dnText;

    public AddObjectsPanel() {
	setLayout(new SGridLayout(1,2));
	SLabel objLabel = new SLabel("Object Classes");

	SForm objForm = new SForm(new SFlowDownLayout());
	objList = new SList();
	objList.setSelectionMode(MULTIPLE_SELECTION);
	objList.addListSelectionListener(this);
	objList.setVisibleRowCount(9);

	submit = new SButton("submit");
	submit.addActionListener(this);
	objForm.add(objLabel);
	objForm.add(objList) ;
	objForm.add(submit) ;
	    
	attrForm = new SForm(new SGridLayout(0,2));
    
	add(objForm);
	add(attrForm);
    }

    public void setWorker(LdapWorker worker) {
	if (worker != null) {
	    this.worker = worker;

	    ArrayList objects = worker.getObjects();
	    objList.setListData(objects.toArray());
	}
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
		attrForm.add(tf);
		comp.put(al,tf);
	    }
	    SLabel addEntryL = new SLabel("adding new Entry"); 
	    addEntryL.setBackground(Color.blue);
	    addEntry = new SButton("addEntry");
	    addEntry.addActionListener(this);
	    attrForm.add(addEntry);
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
	    worker.addNewEntry(dnText.getText().trim(),vals); 
	}
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
