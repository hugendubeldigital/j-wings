/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of the wingS demo (http://wings.mercatis.de).
 *
 * The wingS demo is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package ldap;

import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.naming.*;
import javax.naming.directory.*;


import javax.servlet.*;
import javax.servlet.http.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.ListSelectionEvent;


import org.wings.*;
import org.wings.io.Device;
import org.wings.io.ServletDevice;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wings.util.*;


public class LdapClientSession 
    extends SessionServlet
    implements SConstants, 
	       TreeSelectionListener, 
	       ActionListener,
	       ListSelectionListener
{
    private final static String NOT_CONNECTED = "not connected";
    private final int columns = 35;

    private LdapWorker worker = null;
    private TreeModel treeModel; 
    private String dn;
    private LdapTreeNode node;

    private STabbedPane tabbedPane;
    private SForm settingsForm;
    private SForm mainPanel;
    private STree tree;

    private SDesktopPane cards;
    private SList otherAttrsL;
    private SForm attrPanel;
    private SForm existentAttrsF;
    private SForm otherAttrsF;
    private SButton select;
    private SButton commitButton;
    private AddObjectsPanel addPanel;
    private SPanel treePanel;


    private Hashtable attrHTable;
    private Hashtable attrHLTable;
    private SPanel oPanel = new SPanel();

    private Hashtable textHashtable = new Hashtable();
    private Hashtable componentTable = new Hashtable();

    private FileInputStream fis;
    private byte[] b;
    private boolean string = false;
    
    private ArrayList obj = new ArrayList();
    private Vector objectAttributes;
    private SFileChooser chooser;


    public LdapClientSession(Session session) {
        super(session);
        System.out.println("I`m starting now");  
    }

    public void postInit(ServletConfig config) {
        initGUI();
    }

    void initGUI() {
	tabbedPane = new STabbedPane();
	getFrame().getContentPane().setLayout(new SFlowLayout());
	getFrame().getContentPane().add(tabbedPane);

	settingsForm = new SForm(new SGridLayout(2));
	tabbedPane.add(settingsForm, "Connection Settings");

	SLabel descServer = new SLabel("sever:port");
	final STextField server = new STextField("");
	server.setColumns(30);
	server.setText(((PropertyService)getSession()).getProperty("ldap.server.host"));
	settingsForm.add(descServer);
	settingsForm.add(server);

	SLabel descBaseDN = new SLabel("base DN");
	final STextField baseDN = new STextField();
	baseDN.setColumns(30);
	baseDN.setText(((PropertyService)getSession()).getProperty("ldap.server.basedn"));
	settingsForm.add(descBaseDN);
	settingsForm.add(baseDN);

	SLabel descBindDN = new SLabel("bind DN");
	final STextField bindDN= new STextField();
	bindDN.setText(((PropertyService)getSession()).getProperty("ldap.server.binddn"));
	bindDN.setColumns(30);
	settingsForm.add(descBindDN);
	settingsForm.add(bindDN);

	SLabel descBindDNPassword = new SLabel("password");
	final SPasswordField bindDNPassword= new SPasswordField();
	bindDNPassword.setColumns(30);
	settingsForm.add(descBindDNPassword);
	settingsForm.add(bindDNPassword);

	final SButton connectButton = new SButton("connect");
	final SButton disconnectButton = new SButton("disconnect");
	disconnectButton.setVisible(false);
	settingsForm.add(connectButton);
	settingsForm.add(disconnectButton);

	mainPanel = new SForm(new SGridLayout(2));
	mainPanel.setEncodingType("multipart/form-data");
	tabbedPane.add(mainPanel, "Browser");

	createTreeModel(null);
	createTree();
	SBorderLayout layout = new SBorderLayout();
	layout.setBorder(5);
	//layout.addComponent(tree, SBorderLayout.NORTH );
	//layout.addComponent(new SLabel(""), SBorderLayout.CENTER );
	treePanel = new SPanel(layout);
	treePanel.add(tree,SBorderLayout.NORTH);
	treePanel.add(new SLabel("dummy"),SBorderLayout.CENTER);
	
	mainPanel.add(treePanel);

	attrPanel = new SForm();
	attrPanel.setLayout(new SGridLayout(2));
	attrPanel.setEncodingType("multipart/form-data");
	
	existentAttrsF = new SForm(new SGridLayout(2));
	//existentAttrsF.setEncodingType("multipart/form-data");
	otherAttrsF = new SForm(new SFlowDownLayout());
	
	select = new SButton("select");
	select.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    int i = 0;
		    ArrayList remElements = new ArrayList();
		    while (obj!=null && i< obj.size()) {
			System.out.println(obj.get(i));
			String o = (String)obj.get(i);
			remElements.add(o);
			//SLabel attrLabel = new SLabel(o);
			STextField attrField = new STextField("");
			attrField.setColumns(columns);
			//componentTable.put(attrLabel,attrField);
			componentTable.put(o,attrField);
			//textHashtable.put(attrLabel,attrField.getText());
			textHashtable.put(o,attrField.getText());
			i++;
		    }
		    existentAttrsF.removeAll();
		    Enumeration compEnum;
		    compEnum = componentTable.keys();
		    
		    addEditableComponents(componentTable);
		    
		    System.out.println("groesse  " + remElements.size());
		    
		    for (int l = 0;l<remElements.size();l++) {
			int j = obj.indexOf(remElements.get(l));
			System.out.println("ausgesucht  " + remElements.get(l));
			objectAttributes.remove(j);
		    }
		    otherAttrsL.setListData(objectAttributes);
		}
		
	    });
	//otherAttrsF.add(select);
	attrPanel.add(existentAttrsF);
	attrPanel.add(otherAttrsF);
	
	mainPanel.add(attrPanel);
	
	commitButton = new SButton("Commit");
	commitButton.addActionListener(this);
		
	addPanel = new AddObjectsPanel();
	tabbedPane.add(addPanel, "Add new Object");
	
	connectButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    worker = new LdapWorker(server.getText(),
					    baseDN.getText(),
					    bindDN.getText(),
					    bindDNPassword.getText());
		    
		    boolean success = worker.getSuccess();
		    if (success) {
			connectButton.setVisible(false);
			disconnectButton.setVisible(true);
			tabbedPane.setSelectedIndex(1);
		    }
		    else {
			connectButton.setVisible(true);
			disconnectButton.setVisible(false);
		    }

		    if (success) {
			setLdapWorker(worker);
			createTreeModel(worker);
			tree.setModel(treeModel);
			addPanel.setWorker(worker);
			addPanel.setTree(tree);
		    }
		}
	    });

	disconnectButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    createTreeModel(null);
		    tree.setModel(treeModel);
		    attrPanel.removeAll();

		    bindDNPassword.setText("");

		    disconnectButton.setVisible(false);
		    connectButton.setVisible(true);
		}
	    });
    }
    


    private LdapWorker getLdapWorker() {
	return worker;
    }

    private void setLdapWorker(LdapWorker c) {
	this.worker = c;
    }

    private void createTreeModel(final LdapWorker c) {
	TreeNode root = null;
	if (c != null)
	    root = new LdapTreeNode(c, null, c.getBaseDN());
	else
	    root = new DefaultMutableTreeNode(NOT_CONNECTED);

	treeModel = new DefaultTreeModel(root);
    }

    private void createTree() {
	tree = new STree(treeModel);	
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
	String newValue = null;
	String oldValue = null;
	String oValue;

	System.out.println(">> commit <<");
	
	LdapWorker worker = getLdapWorker();
	BasicAttributes attributes = new BasicAttributes();
	Enumeration enumer = componentTable.keys();
	while (enumer != null && enumer.hasMoreElements()) {
	    String key = (String)enumer.nextElement();
	    System.out.println(key);

	    oldValue = (String)(textHashtable.get(key));
	    System.out.println("fuer " + key + " old value " +oldValue);
	    
	    if (oldValue != null )
		newValue = ((STextField)componentTable.get(key)).getText();
	    
	    System.out.println("new value " +newValue);
	    System.out.println("old value " +oldValue);
	    
	    if (oldValue!=null) {
		if (!oldValue.equals(newValue)) {
		    BasicAttribute attr = new BasicAttribute((String)key);
		    StringTokenizer st = new StringTokenizer(newValue,",");
		    String atV;
		    boolean b = (st !=null && st.hasMoreTokens());
		    if (b) 
			while (st !=null && st.hasMoreTokens()) 
			    attr.add(st.nextToken());
		    else attr.add(newValue);
		    attributes.put(attr);
		}
	    }
	}
	
	if (attributes !=null && attributes.size() > 0) 
	    worker.modifyAttributes(dn,attributes);
	
	if (chooser.getFilename()!="" && chooser.getFilename()!=null) {
	    String attribut = "jpegPhoto";
	    BasicAttributes attrs = new BasicAttributes();
	    BasicAttribute attr = new BasicAttribute("jpegPhoto");
	    
	    try {
		fis = new FileInputStream("/home/nengels/jpg/" + chooser.getFilename());
		System.out.println("dir" + chooser.getFiledir());
		System.out.println("file" + chooser.getFilename());
		try {
		    int bytesNr = fis.available();
		    b = new byte[bytesNr];
		    System.out.println(bytesNr + "  bytes            " );
		    System.out.println(b.length);
		    int i = fis.read(b);
		    fis.close();
		    componentTable.remove("jpegPhoto");
		    componentTable.put("jpegPhoto", new SLabel(new ImageIcon("/home/nengels/jpg/" + chooser.getFilename())));
		    //existentAttrsF.removeAll();
		    addEditableComponents(componentTable);
		    
		    
		}
		catch (IOException e) {
		}
	    }
	    catch(FileNotFoundException ex){
	    }
	    	    
	    attr.add(b);
	    attrs.put(attr);
	    if (attrs.size() > 0) 
		worker.modifyAttributes(dn,attrs);
	    
	}
	
	
    }
    
    
    private void setDN(String dn) {
	this.dn = dn;
    }

    public String getDN() {
	return dn;
    }

    public LdapTreeNode getNode() {
	return node;
    }

    private void setNode(LdapTreeNode node) {
	this.node =node;
    }


    public void valueChanged(TreeSelectionEvent e) {
	TreeNode node = (TreeNode)tree.getLastSelectedPathComponent();
	if (node == null || !(node instanceof LdapTreeNode)) {
	    System.out.println("nothing selected");
	    return;
	}
	setDN(((LdapTreeNode)node).getDN());
	setNode((LdapTreeNode)node);

	componentTable.clear();
	textHashtable.clear();
	
	existentAttrsF.removeAll();

	LdapWorker worker = getLdapWorker();
	BasicAttributes attributes = (BasicAttributes)worker.getDNAttributes(getDN());

	try {
	    NamingEnumeration en = attributes.getAll();
	    while (en!=null && en.hasMoreElements()) {
		BasicAttribute attr = (BasicAttribute)en.nextElement();
		String label = attr.getID();
		String values = "";
		NamingEnumeration aV = attr.getAll();
		
		Object cl = null;
		while (aV!=null && aV.hasMore()) 
		    cl = aV.next();
		
		if (cl!=null) {
		    if (cl.getClass().getName().equals("java.lang.String")) {
			NamingEnumeration aValues = attr.getAll();
			while (aValues!=null && aValues.hasMore()) {
			    Object o = aValues.next();
			    if(!values.equals("")) {
				values = values + "," + o;
			    }
			    else {
				values = (String)o;
			    }
			}
			//SLabel attrLabel = new SLabel(label);
			STextField attrField = new STextField((String)values);
			attrField.setColumns(columns);
			componentTable.put(label,attrField);
			textHashtable.put(label,values);
			
		    }
		    
		    if (cl.getClass().getName().equals("[B")) {
			NamingEnumeration aValues = attr.getAll();
			Object o = null;
			while (aValues!=null && aValues.hasMore()) {
			     o = aValues.next();
			}
			
			byte hallo [] = (byte [])o;
			if (label.equals("jpegPhoto")) { 
			    SLabel attrL = new SLabel(label);
			    SLabel photo = new SLabel(new ImageIcon((byte [])o));
			    //SButton photo = new SButton(new ImageIcon((byte [])i));
			    //SButton photo = new SButton(new ImageIcon("/home/nengels/Nathan.jpg"));
			    componentTable.put(label,photo);
			}
			if (label.equals("userPassword")) { 
			    SLabel attrLa = new SLabel(label);
			    STextField attrF = new SPasswordField();
			    attrF.setColumns(35);
			    attrF.setText(o.toString());
			    componentTable.put(label,attrF);
			    textHashtable.put(label,o.toString());
			}
		    }
		}
	    }
	    
	    
	    objectAttributes = worker.getObjectAttributes(getDN());
	    Enumeration enum = componentTable.keys();
	    while (enum != null && enum.hasMoreElements()) {
		String keyString = (String)enum.nextElement();
		if (objectAttributes.contains((String)keyString)) 
		    objectAttributes.remove((String)keyString);
	    }
	
	    
	    
	    otherAttrsL = new SList();
	    otherAttrsL.setSelectionMode(MULTIPLE_SELECTION);
	    otherAttrsL.addListSelectionListener(this);
	    otherAttrsL.setVisibleRowCount(12);
	    
	    
	    
	    Vector v = new Vector();
	    Enumeration er  = objectAttributes.elements();
	    while (er!=null && er.hasMoreElements()) {
		Object ob = er.nextElement();
		v.add((String)ob);
	    }  
	    otherAttrsL.setListData(objectAttributes);
	    otherAttrsF.removeAll();
	    otherAttrsF.add(otherAttrsL);
	    otherAttrsF.add(select);
	    
	    
	}
	catch (NamingException ex) {
	    System.out.println(ex);
	}
	
		
	addEditableComponents(componentTable);
    }

    
    
    private void addEditableComponents(Hashtable comp) {
	existentAttrsF.removeAll();
	Enumeration compEnum;
	compEnum = comp.keys();
	while (compEnum!=null && compEnum.hasMoreElements()) {
	    Object key = compEnum.nextElement();
		existentAttrsF.add(new SLabel((String)key));
		existentAttrsF.add((SComponent)comp.get(key));
		if (((String)key).equals("jpegPhoto")) {
		    SLabel label = new SLabel("Select jpegPhoto...");
		    existentAttrsF.add(label);
		    chooser = new SFileChooser();
		    existentAttrsF.add(chooser);  
		    
		}
	}
	existentAttrsF.add(commitButton);
    }


    public void valueChanged(ListSelectionEvent evt) {
	SList source = (SList)evt.getSource();
	Object [] elements = source.getSelectedValues();
	System.out.println("valueChanged ...");
	setSelectedObjects(elements);
    }

    private void setSelectedObjects(Object [] elements) {
	obj.clear();
	for (int i = 0; i < elements.length; i++) {
	    obj.add(elements[i]);
	}
    }


    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "LdapClient $Revision$";
    }
}
