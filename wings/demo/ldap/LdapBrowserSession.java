package ldap;

import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.AbstractSet;


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
    private Hashtable viewAttributes;

    private LdapWorker worker = null;

    private SPanel tableForm;
    private String server;
    private String baseDN;
    private String bindDN ;
    private String password ;
    private String peopleName;
    ArrayList selList = new ArrayList();
    int count = 0;
    SButton submit;
    STable peopleTable;
    HashMap peopleDN;
    SPanel viewPanel;
    SForm searchForm;
    STextField searchField;
    SLabel searchLabel;
    SButton searchButton;
    String filter = "(cn=*)";
    SButton back;

    public LdapBrowserSession(Session session) {
	
	super(session);
        System.out.println("I`m starting now");
	
    }

    public void postInit(ServletConfig config) {
        initGUI();
    }

    void initGUI() {

	viewAttributes = new Hashtable();
	viewAttributes.put("cn","Name");
	viewAttributes.put("title","Titel");
	viewAttributes.put("mail","e-mail");
	viewAttributes.put("jpegPhoto", "Foto");
	viewAttributes.put("telephoneNumber","Telefon");
	viewAttributes.put("l","Standort");
	//connection ohne dialog
	System.out.println("host" + ((PropertyService)getSession()).getProperty("ldap.server.host"));
	this.server = ((PropertyService)getSession()).getProperty("ldap.server.host");
	this.baseDN = ((PropertyService)getSession()).getProperty("ldap.server.basedn");
	this.bindDN = ((PropertyService)getSession()).getProperty("ldap.server.binddn");
	this.password = ((PropertyService)getSession()).getProperty("ldap.server.password");
	this.peopleName = ((PropertyService)getSession()).getProperty("ldap.server.peoplename");
       
		
	worker = new LdapWorker(server,
				baseDN,
				bindDN,
				password);
	
	boolean success = worker.getSuccess();
	if (!success) {
	    System.out.println("no connection");
	}
	else {
	    setLdapWorker(worker);
	}

	getFrame().getContentPane().setLayout(new SFlowDownLayout());
	
		
	searchForm = new SForm(new SFlowDownLayout());
	searchForm.setBorder(new SLineBorder());
	tableForm = new SPanel(new SFlowDownLayout());
		
	submit = new SButton("submit");
	submit.addActionListener(this);

	back = new SButton("back");
	back.setVisible(false);
	back.addActionListener(this);
	
	viewPanel = new SPanel(new SGridLayout(2));
	
	searchLabel = new SLabel("name");
		
	searchField = new STextField("");

	searchButton = new SButton("search");
	searchButton.addActionListener(this);
	
	searchForm.add(searchLabel);
	searchForm.add(searchField);
	searchForm.add(searchButton);

	tableForm.add(back);
	
	getFrame().getContentPane().add(searchForm);
	getFrame().getContentPane().add(tableForm);	
	getFrame().getContentPane().add(viewPanel);
    }

    
    public void actionPerformed(ActionEvent evt) {
	System.out.println ("source ist " + evt.getSource().toString());
	
	if((SButton)evt.getSource() == back) {
	    back.setVisible(false);
	    peopleTable.setVisible(true);
	    searchForm.setVisible(true);
	    viewPanel.removeAll();
	}
		
	if ((SButton)evt.getSource() == searchButton) {
	    System.out.println("filter is... " + filter);
	    setFilter("(cn=" + searchField.getText() + "*)");
	    tableForm.remove(peopleTable);
	    viewPanel.removeAll();
	    peopleTable = new STable(new LdapTableModel());
	    peopleTable.setBorderLines(new Insets(2,2,2,2));
	    //[REMARK]
	    SelectableTableCellRenderer renderer = new SelectableTableCellRenderer();
	    renderer.setSelectableColumns(new int[] {0});
	    renderer.addCellSelectionListener(new CellSelectionListener() {
		    public void cellSelected(CellSelectionEvent e) {
			System.out.println("Cell at position x="+e.getXPosition()+" y="+e.getYPosition());
			
			peopleDN = getLdapWorker().getAttributeDNValues(peopleName,baseDN);
			
			LdapTableModel model = (LdapTableModel)peopleTable.getModel();
			String value = (String)model.getValueAt(e.getXPosition(),0);
			System.out.println("value is" + value);
			String dn = (String)peopleDN.get(value);
			viewPanel.removeAll();
			peopleTable.setVisible(false);
			searchForm.setVisible(false);
			back.setVisible(true);
			
			BasicAttributes attrs = (BasicAttributes)getLdapWorker().getDNAttributes(dn + "," + baseDN);
			try {
				NamingEnumeration en = attrs.getAll();
				while (en!=null && en.hasMoreElements()) {
				    BasicAttribute attr = (BasicAttribute)en.nextElement();
				    String label = attr.getID();
				    if (viewAttributes.containsKey(label)) {
					NamingEnumeration aValues = attr.getAll();
					while (aValues!=null && aValues.hasMore()) {
					    Object i = aValues.next();
					    if (i.getClass().getName().equals("java.lang.String")) {
						String values = "";
						if(!values.equals("")) {
						    values = values + "," + i;
						}
						else {
						    values = (String)i;
						}
						viewPanel.add(new SLabel((String)viewAttributes.get(label) + "     "));
						viewPanel.add(new SLabel(values));
					    }
					    if (i.getClass().getName().equals("[B")) {
						if (label.equals("jpegPhoto")) {
						    viewPanel.add(new SLabel((String)viewAttributes.get(label)));
						    viewPanel.add(new SLabel(new ImageIcon((byte [])i)));
						}
						if (label.equals("userPassword")) {
						    SLabel attrLabel = new SLabel((String)viewAttributes.get(label));
						    STextField attrField = new STextField(i.toString());
						    viewPanel.add(new SLabel(label));
						    viewPanel.add(new SLabel("*******"));
						}
					    }
					}
				    }
				}
				
			    }
			    catch (NamingException exc){
				System.out.println(exc);
			    }
			    viewPanel.setVisible(true);
		    }
		});
	    peopleTable.setDefaultRenderer(renderer);
	    tableForm.add(peopleTable);
	}
    }

    private void setFilter(String f) {
	filter = f;
    }

    public String getFilter() {
	return filter;
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


	final String[] columnNames = {"cn","mail","telephoneNumber"};
	final int COLS = columnNames.length;
	int ROWS ;
	Object[][] data;
	ArrayList dnList;

	LdapTableModel() {
	    dnList = getLdapWorker().getFilteredAllDN(baseDN,getFilter());
	    ROWS = dnList.size();
	    int i = 0 ;
	    data = new Object[ROWS][COLS];
	    if (ROWS > 0) {
		for (int c=0; c < COLS; c++) {
		for (int r=0; r < ROWS; r++) {
		    if (getLdapWorker()!=null)
    	        data[r][c] = (String)getLdapWorker().getOAttributeValues((String)dnList.get(r) + "," + baseDN , columnNames[c]);
		}
		}
	    }
	    else
		System.out.println("mist");
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
	    return (String)viewAttributes.get(columnNames[column]);
	}

    }



    public void valueChanged(ListSelectionEvent e) {

	peopleDN = getLdapWorker().getAttributeDNValues(peopleName,baseDN);
	if (e.getSource() == peopleTable) {
	    int row = peopleTable.getSelectedRow();
	    LdapTableModel model = (LdapTableModel)peopleTable.getModel();
	    String value = (String)model.getValueAt(row,0);
	    String dn = (String)peopleDN.get(value);
	    viewPanel.removeAll();

	    BasicAttributes attrs = (BasicAttributes)getLdapWorker().getDNAttributes(dn + "," + baseDN);
		try {
		    NamingEnumeration en = attrs.getAll();
		    while (en!=null && en.hasMoreElements()) {
			BasicAttribute attr = (BasicAttribute)en.nextElement();
			String label = attr.getID();
			if (viewAttributes.containsKey(label)) {
			    NamingEnumeration aValues = attr.getAll();
			    while (aValues!=null && aValues.hasMore()) {
				Object i = aValues.next();
				if (i.getClass().getName().equals("java.lang.String")) {
				    String values = "";
				    if(!values.equals("")) {
					values = values + "," + i;
				    }
				    else {
					values = (String)i;
				    }
				    viewPanel.add(new SLabel((String)viewAttributes.get(label) + "     "));
				    viewPanel.add(new SLabel(values));
				}
				if (i.getClass().getName().equals("[B")) {
				    if (label.equals("jpegPhoto")) {
					viewPanel.add(new SLabel((String)viewAttributes.get(label)));
					viewPanel.add(new SLabel(new ImageIcon((byte [])i)));
				    }
				    if (label.equals("userPassword")) {
					SLabel attrLabel = new SLabel((String)viewAttributes.get(label));
					STextField attrField = new STextField(i.toString());
					viewPanel.add(new SLabel(label));
					viewPanel.add(new SLabel("*******"));
				    }
				}
			    }
			}
		    }
		}
		catch (NamingException exc){
		    System.out.println(exc);
		}
	}
    }
}
