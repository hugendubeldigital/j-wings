package ldap;

import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.HashMap;


import javax.naming.*;
import javax.naming.directory.*;


import javax.servlet.*;
import javax.servlet.http.*;

import javax.swing.*;
import javax.swing.event.*; 
import javax.swing.table.*;
import java.awt.Insets;


import org.wings.*;
import org.wings.io.Device;
import org.wings.io.ServletDevice;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wings.util.*;


public class LdapBrowserSession 
    extends SessionServlet
    implements SConstants,
	       ListSelectionListener,
	       ActionListener
{
    private final static String NOT_CONNECTED = "not connected";
    private final static String [] attributes = {"mail","cn"}; 
    private HashMap viewAttributes;

    private LdapWorker worker = null;

    //private STabbedPane tabbedPane;
    private SForm listForm;
    //private SForm tableForm;
    private SForm tableForm;
    private String server;
    final private String baseDN = "dc=engels,dc=de";
    private String bindDN = "";
    private String password = "";
    private String peopleName = "cn";
    ArrayList selList = new ArrayList();
    int count = 0;
    SList personList;
    SButton submit;
    STable peopleTable;
    HashMap peopleDN;
    SButton view;
    SPanel viewPanel;
    SForm searchForm;
    STextField searchField;
    SLabel searchLabel;
    SButton searchButton;
    final String filter = "(cn=*)";

    public LdapBrowserSession(Session session) {
	
	super(session);
        System.out.println("I`m starting now");
	viewAttributes = new HashMap();
	viewAttributes.put("name","cn");
	viewAttributes.put("e-mail","mail");
	viewAttributes.put("foto", "jpegPhoto");
	//connection ohne dialog
	server = ((PropertyService)getSession()).getProperty("ldap.server.host");
	//baseDN = ((PropertyService)getSession()).getProperty("ldap.server.basedn");
	//bindDN = ((PropertyService)getSession()).getProperty("ldap.server.binddn");
	//pasword = ((PropertyService)getSession()).getProperty("ldap.server.password");
	peopleName = ((PropertyService)getSession()).getProperty("ldap.server.peoplename");
       
	
	System.out.println(server);
	System.out.println(baseDN);
	System.out.println(peopleName);
	System.out.println(password);
	
	/*worker = new LdapWorker(server,
				baseDN,
				bindDN,
				password);*/
	worker = new LdapWorker("localhost:389",
				"dc=engels,dc=de",
				"cn=admin,dc=engels,dc=de",
				"secret");
	boolean success = worker.getSuccess();
	if (!success) {
	    System.out.println("no connection");
	}
	else {
	    setLdapWorker(worker);
	}
    }

    public void postInit(ServletConfig config) {
        initGUI();
    }

    void initGUI() {
	getFrame().getContentPane().setLayout(new SFlowDownLayout());
	
		
	searchForm = new SForm(new SFlowDownLayout());
	tableForm = new SForm(new SFlowDownLayout());
	//tableForm = new SPanel(new SFlowDownLayout());
	
	personList = new SList();
	submit = new SButton("submit");
	submit.addActionListener(this);
	peopleTable = new STable(new LdapTableModel());
	peopleTable.setShowGrid(true);
        peopleTable.setBorderLines(new Insets(1,1,1,1));
	peopleTable.setSelectionMode(SINGLE_SELECTION);

	view = new SButton("view");
	view.addActionListener(this); 
	
	viewPanel = new SPanel(new SGridLayout(2));

	
	personList.setSelectionMode(MULTIPLE_SELECTION);
	personList.addListSelectionListener(this);
	personList.setVisibleRowCount(12);
	addListElements(personList);

	searchLabel = new SLabel("name");
		
	searchField = new STextField("");

	searchButton = new SButton("search");
	searchButton.addActionListener(this);
	
	tableForm.add(peopleTable);
	tableForm.add(view);
	searchForm.add(searchLabel);
	searchForm.add(searchField);
	searchForm.add(searchButton);
	
	//listForm.add(personList);
	//listForm.add(submit);
	//getFrame().getContentPane().add(listForm);
	getFrame().getContentPane().add(searchForm);
	getFrame().getContentPane().add(tableForm);	
	getFrame().getContentPane().add(viewPanel);
    }

    
    public void actionPerformed(ActionEvent evt) {
	System.out.println ("source ist " + evt.getSource().toString());
	if ((SButton)evt.getSource() == submit) {
	    tableForm.removeAll();
	    //peopleTable.setModel(new LdapTableModel());
	    peopleTable = new STable(new LdapTableModel());
	    peopleTable.setBorderLines(new Insets(2,2,2,2));
	    peopleTable.setSelectionMode(SINGLE_SELECTION);

	    tableForm.add(peopleTable);
	    tableForm.add(view);
	}

	//else 
	
	if ((SButton)evt.getSource() == view) {
	    System.out.println("juju");
	    int row = peopleTable.getSelectedRow();
	    LdapTableModel model = (LdapTableModel)peopleTable.getModel();
	    System.out.println("value at(" + row+",0)");
	    String value = (String)model.getValueAt(row,0);
	    String dn = (String)peopleDN.get(value);
	    System.out.println("die dn ist" + dn);
	    System.out.println("base DN ist " + baseDN);
	    viewPanel.removeAll();
	    
	    BasicAttributes attrs = (BasicAttributes)getLdapWorker().getDNAttributes(dn + "," + baseDN);
		try {
		    NamingEnumeration en = attrs.getAll();
		    while (en!=null && en.hasMoreElements()) {
			BasicAttribute attr = (BasicAttribute)en.nextElement();
			String label = attr.getID();
			//HashSet c = (HashSet)viewAttributes.keySet();
			AbstractCollection c = (AbstractCollection)viewAttributes.values();
			if (c.contains(label)) {
			    NamingEnumeration aValues = attr.getAll();
			    while (aValues!=null && aValues.hasMore()) {
				Object i = aValues.next();
				if (i.getClass().getName() == "java.lang.String") {
				    String values = "";
				    if(!values.equals("")) {
					values = values + "," + i;
				    }
				    else {
					values = (String)i;
				    }
				    viewPanel.add(new SLabel(label));
				    viewPanel.add(new SLabel(values));
				}
				if (i.getClass().getName() == "[B") {
				//byte hallo [] = (byte [])i;
				//System.out.println(hallo[3]);
				    if (label.equals("jpegPhoto")) { 
					viewPanel.add(new SLabel(label));
					viewPanel.add(new SLabel(new ImageIcon((byte [])i)));
				    }
				    if (label.equals("userPassword")) { 
					SLabel attrLabel = new SLabel(label);
					STextField attrField = new STextField(i.toString());
					viewPanel.add(new SLabel(label));
					viewPanel.add(new SLabel("*******"));
				    }
				//System.out.println("binary");
				}
			    }
			}
		    }
		}
		catch (NamingException exc){
		    System.out.println(exc);
		}
	}
	//else 
	if ((SButton)evt.getSource() == searchButton) {
	    //filter = search.getSelectedItem();
	    System.out.println("filter is... " + filter);
	    //LdapWorker worker = getLdapWorker();
	    //peopleTable.setModel(new LdapTableModel());
	    
	    tableForm.removeAll();
	    peopleTable = new STable(new LdapTableModel());
	    peopleTable.setBorderLines(new Insets(2,2,2,2));
	    peopleTable.setSelectionMode(SINGLE_SELECTION);
	    
	    tableForm.add(peopleTable);
	    tableForm.add(view);
	}
    }
    
    

    private void addListElements(SList list) {
	worker = getLdapWorker();
	peopleDN = worker.getAttributeValues(peopleName,baseDN);
	//ArrayList people = worker.getAttributeValues(peopleName);
	Set keys = peopleDN.keySet(); 
	list.setListData(keys.toArray());
    }

    private void setLdapWorker(LdapWorker worker) {
	this.worker = worker;
    }
    
    public LdapWorker getLdapWorker() {
	return worker;
    }
    
    public String getServletInfo() {
        return "LdapBrowser $Revision$";
    }

    class LdapTableModel extends AbstractTableModel {
	
	
	final String[] columnNames = {"cn","sn"};
	final int COLS = columnNames.length;
	int ROWS ;
	Object[][] data; //= new Object[ROWS][COLS];
	ArrayList dnList;
	
	LdapTableModel() {
	    //ArrayList l = getSelList();
	    dnList = getLdapWorker().getFilteredAllDN("(cn = " + searchField.getText() + "*)",baseDN);
	    ROWS = dnList.size();

	    int i = 0 ;
	    data = new Object[ROWS][COLS];
	    /*while (i < dnList.size()){
		System.out.println(i + "   " + dnList.get(i));
		i++;
		}*/
	    if (ROWS > 0) {
		System.out.println(COLS + "columns");
		System.out.println(ROWS + "rows");

		System.out.println(data.length);
		System.out.println(data[0].length);
	    for (int c=0; c < COLS; c++) {
		for (int r=0; r < ROWS; r++) {
		    //filter  = "(" + filter + " = " + searchField.getText() + ")"; 
		    //data[r][c] = worker.getOAttributeValues((String)peopleDN.get((String)getSelList().get(r)),columnNames[c]);
		    System.out.println("row " + r + "col " + c );
		    System.out.println(columnNames[c]);
		    System.out.println((String)dnList.get(r)); 
		    System.out.println(baseDN);
		    if (getLdapWorker()!=null)
			System.out.println("kein krach");
		    data[r][c] = (String)getLdapWorker().getOAttributeValues((String)dnList.get(r) + "," + baseDN , columnNames[c]);
		    System.out.println("row " + r + "col " + c + "value "+ data[r][c]);
		}
		
	    }
	    }
	} 
	 
	public int getRowCount() {
	    return ROWS;
	} 
	
	public int getColumnCount() {
	    return COLS;
	} 
	
	public Object getValueAt(int row, int column) {
	    return data[row][column];
	}
	
	public String getColumnName(int column) {
	    return columnNames[column];
	}
	
    }
    
    private void setSelList(Object [] people)
    {
	if (selList!=null)
	    selList.clear();
	for (int i=0;i<people.length;i++) {
	    selList.add(people[i]);
	    //System.out.println(selPeople[i]);
	}
    }

    public ArrayList getSelList() {
	return selList;
    }
  
    private void setSelectedPeopleCount(int count) {
	this.count = count;
    }

    public int getSelectedPeopleCount () {
	return count;
    }
    
    
    public void valueChanged(ListSelectionEvent e) {
	SList source = (SList)e.getSource();
	Object [] selPeople = source.getSelectedValues();
	setSelectedPeopleCount(selPeople.length);
	setSelList(selPeople);
    }
}
