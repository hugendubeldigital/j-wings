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

    private LdapWorker worker = null;

    //private STabbedPane tabbedPane;
    private SForm listForm;
    private SForm tableForm;
    private String server;
    private String baseDN;
    private String bindDN = "";
    private String password = "";
    private String peopleName = "cn";
    ArrayList selList = null;
    int count = 0;
    SList personList;
    SButton submit;
    STable peopleTable;
    HashMap peopleDN;

    public LdapBrowserSession(Session session) {
        super(session);
        System.out.println("I`m starting now");
	//connection ohne dialog
	server = ((PropertyService)getSession()).getProperty("ldap.server.host");
	baseDN = ((PropertyService)getSession()).getProperty("ldap.server.basedn");
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
	worker = new LdapWorker("192.168.230.131:389",
				"dc=tiscon,dc=de",
				"cn=admin,dc=tiscon,dc=de",
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
	getFrame().getContentPane().setLayout(new SFlowLayout());
	
		
	listForm = new SForm(new SFlowDownLayout());
	tableForm = new SForm(new SFlowDownLayout());
	
	personList = new SList();
	submit = new SButton("submit");
	submit.addActionListener(this);
	peopleTable = new STable(new LdapTableModel());
	peopleTable.setShowGrid(true);
        peopleTable.setBorderLines(new Insets(1,1,1,1));
	
	personList.setSelectionMode(MULTIPLE_SELECTION);
	personList.addListSelectionListener(this);
	personList.setVisibleRowCount(12);
	addListElements(personList);
	
	tableForm.add(peopleTable);
	listForm.add(personList);
	listForm.add(submit);
	getFrame().getContentPane().add(listForm);
	getFrame().getContentPane().add(tableForm);	
    }

    
    public void actionPerformed(ActionEvent evt) {
	if ((SButton)evt.getSource() == submit) {
	    peopleTable.setModel(new LdapTableModel());
	}
	
    }
    

    private void addListElements(SList list) {
	worker = getLdapWorker();
	peopleDN = worker.getAttributeValues(peopleName);
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
	
	
	final String[] columnNames = {"mail","cn","sn"};
	final int COLS = columnNames.length;
	final int ROWS = getSelectedPeopleCount();
	Object[][] data = new Object[ROWS][COLS];
	
	LdapTableModel() {
	    if (ROWS > 0) {
	    for (int c=0; c < COLS; c++) {
		for (int r=0; r < ROWS; r++)
		    //data[r][c] = worker.getOAttributeValues((String)getSelList().get(r),columnNames[c]);
		    data[r][c] = worker.getOAttributeValues((String)peopleDN.get((String)getSelList().get(r)),columnNames[c]);
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
    
    private void setSelList(ArrayList l)
    {
	this.selList = l;

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
	ArrayList selList = new ArrayList();
	Object [] selPeople = source.getSelectedValues();
	setSelectedPeopleCount(selPeople.length);
	for (int i=0;i<selPeople.length;i++) {
	    selList.add((String)selPeople[i]);
	    //System.out.println(selPeople[i]);
		
	}
	setSelList(selList);
    }
}
