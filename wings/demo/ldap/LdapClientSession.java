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

import java.awt.*;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


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
	       ListSelectionListener,
	       ChangeListener
{
    private final static String NOT_CONNECTED = "not connected";
    private final static String JPEGPATH = "/home/nina/wings/demo/ldap/jpeg/";
    private final static String DELIM= ":";
    private final int columns = 50;
    private final int maxcolumns = 100;

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
    private SForm existentAttrsF;
    private SForm otherAttrsF;
    private SButton addAttribute;
    private SButton commitButton;
    private SButton removeButton;
    private SButton disconnectButton;
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
    private SFileChooser chooser = null;
    private SPasswordField uPw = null;
    private STextField server;
    private STextField baseDN;
    private STextField bindDN;
    private STextField bindDNPassword;


    public LdapClientSession(Session session) {
        super(session);
        System.out.println("I`m starting now");
    }

    public void postInit(ServletConfig config) {
        initGUI();
    }
    
    void initGUI() {
        SContainer contentPane = getFrame().getContentPane();
        tabbedPane = new STabbedPane();
        contentPane.setLayout(new SFlowLayout());
	
        settingsForm = new SForm(new SGridLayout(2));
        tabbedPane.add(settingsForm, "Connection Settings");
	
        SLabel descServer = new SLabel("sever:port");
        server = new STextField("");
        server.setColumns(columns);
        server.setText((String)((PropertyService)getSession()).getProperty("ldap.server.host"));
        settingsForm.add(descServer);
        settingsForm.add(server);
	
        SLabel descBaseDN = new SLabel("base DN");
        baseDN = new STextField();
        baseDN.setColumns(columns);
        baseDN.setText((String)((PropertyService)getSession()).getProperty("ldap.server.basedn"));
        settingsForm.add(descBaseDN);
        settingsForm.add(baseDN);
	
        SLabel descBindDN = new SLabel("bind DN");
        bindDN= new STextField();
        bindDN.setText((String)((PropertyService)getSession()).getProperty("ldap.server.binddn"));
        bindDN.setColumns(columns);
        settingsForm.add(descBindDN);
        settingsForm.add(bindDN);
	
        SLabel descBindDNPassword = new SLabel("password");
        bindDNPassword= new SPasswordField();
        bindDNPassword.setColumns(columns);
        settingsForm.add(descBindDNPassword);
        settingsForm.add(bindDNPassword);
	
        final SButton connectButton = new SButton("connect");
        final SButton disconnectButton = new SButton("disconnect");
        disconnectButton.setVisible(false);
        settingsForm.add(connectButton);
        settingsForm.add(disconnectButton);
	
        try {
            mainPanel = new SForm(new STemplateLayout(getClass().getResource("ldapclientlayout.html")));
	    mainPanel.setEncodingType("multipart/form-data");
        }
        catch(Exception e) {
            System.err.println("LdapClientSession::initGUI() -> " + e);
        }
	
        //mainPanel.setEncodingType("multipart/form-data");
	
        mainPanel.add(new SLabel( new ResourceImageIcon(LdapClient.class, 
                                                        "images/LDAPworm.gif")), "BOTTOMIMAGE");
	
        tabbedPane.add(mainPanel, "Browser");
	
        createTreeModel(null);
        createTree();
	
        mainPanel.add(tree,"TREE");
        
        existentAttrsF = new SForm(new SGridLayout(2));
        existentAttrsF.setEncodingType("multipart/form-data");
        otherAttrsF = new SForm(new SFlowDownLayout());
	
        addAttribute = new SButton("add Attribute");
        addAttribute.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    int i = 0;
		    ArrayList remElements = new ArrayList();
		    while (obj!=null && i< obj.size()) {
			//System.out.println(obj.get(i));
			String o = (String)obj.get(i);
			remElements.add(o);
			SComponent attrField;
			if (!((String)o).equals("jpegPhoto")) {
			    if (((String)o).equals("userPassword")) {
				attrField = new SPasswordField();
				((SPasswordField)attrField).setColumns(columns);
				((SPasswordField)attrField).setMaxColumns(maxcolumns);
                                ((SPasswordField)attrField).setText("");
				textHashtable.put(o,"");
			    }
			    else {
				attrField = new STextField("");
				((STextField)attrField).setColumns(columns);
                                ((STextField)attrField).setMaxColumns(maxcolumns);
			textHashtable.put(o,"");
			    }
			}
			else {
			    attrField = new SLabel("");
			}
			componentTable.put(o,attrField);
			i++;
		    }
		    existentAttrsF.removeAll();
		    Enumeration compEnum;
		    compEnum = componentTable.keys();
		    
		    addEditableComponents(componentTable);
		    
		    for (int l = 0;l<remElements.size();l++) {
			int j = obj.indexOf(remElements.get(l));
			objectAttributes.remove(j);
		    }
		    otherAttrsL.setListData(objectAttributes);
		}
		
	    });
	
        mainPanel.add(existentAttrsF,"OBJECTATTRIBUTES");
        mainPanel.add(otherAttrsF,"ATTRIBUTELIST");
	
        commitButton = new SButton("Commit");
        commitButton.addActionListener(this);
	
	removeButton = new SButton("Remove entry");
	removeButton.addActionListener(this);
	
        addPanel = new AddObjectsPanel();
        tabbedPane.add(addPanel, "Add new Object");

        contentPane.add(tabbedPane);

        connectButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    worker = new LdapWorker(server.getText(),
					    baseDN.getText(),
					    bindDN.getText(),
					    bindDNPassword.getText());
		    
		    boolean success = worker.getSuccess();
		    if (success) {
			connectButton.setVisible(false);
			server.setVisible(false);
			baseDN.setVisible(false);
			bindDN.setVisible(false);
			bindDNPassword.setVisible(false);
			
			disconnectButton.setVisible(true);
			tabbedPane.setSelectedIndex(1);
			if (existentAttrsF!=null)
			    existentAttrsF.removeAll();
			if (otherAttrsF!=null)
			    otherAttrsF.removeAll();
			
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
		    if (existentAttrsF!= null)
			existentAttrsF.removeAll();
		    if (otherAttrsF!= null)
			otherAttrsF.removeAll();
		    bindDNPassword.setText("");
		    disconnectButton.setVisible(false);
		    connectButton.setVisible(true);
		    server.setVisible(true);
		    bindDN.setVisible(true);
		    bindDNPassword.setVisible(true);
		    baseDN.setVisible(true);
		    bindDNPassword.setText("");
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
	    root = new LdapTreeNode(c.getContext(), null, c.getBaseDN());
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

	if (evt.getSource().equals(commitButton)) {
	    System.out.println(">> commit <<");

	    LdapWorker worker = getLdapWorker();
	    BasicAttributes attributes = new BasicAttributes();
	    Enumeration enumer = componentTable.keys();
	    while (enumer != null && enumer.hasMoreElements()) {
		String key = (String)enumer.nextElement();
		
		oldValue = (String)(textHashtable.get(key));
		
		if (oldValue != null ) {
		    if (componentTable.get(key).getClass().getName().equals("org.wings.SLabel"))
			newValue = ((SLabel)componentTable.get(key)).getText();
		    else 
			newValue = ((STextField)componentTable.get(key)).getText();
		}
		
		if (oldValue!=null) {
		    if (!oldValue.equals(newValue)) {
			boolean uP = false;
			BasicAttribute attr = new BasicAttribute((String)key);
			if (((String)key).toLowerCase().trim().equals("userpassword")) 
			{ 
			    if (uPw.getText().equals(newValue))
				attr.add(newValue);
			    else 
				System.out.println("nicht gleiches Passwort");
			}
			else {
                            StringTokenizer st = new
                                StringTokenizer(newValue,DELIM);
                            String atV;
			    boolean b = ((st !=null) && (st.hasMoreTokens()));
                            if (b)
				while (st !=null && st.hasMoreTokens())
				    {System.out.println("o je");
				    attr.add(st.nextToken());}
			}
			attributes.put(attr);
		    }
		}
	    }
	    
	    if (attributes !=null && attributes.size() > 0)
		worker.modifyAttributes(dn,attributes);
	    if (chooser!=null) {
		if (chooser.getFilename()!="" && chooser.getFilename()!=null) {
		    String attribut = "jpegPhoto";
		    BasicAttributes attrs = new BasicAttributes();
		    BasicAttribute attr = new BasicAttribute("jpegPhoto");
		    
		    try {
			fis = new FileInputStream(JPEGPATH + chooser.getFilename());
			System.out.println("dirrrrrrrrrrrrrrrrrrrrrrrrr" + chooser.getFiledir());
			System.out.println("filerrrrrrrrrrrrrrrrrrrrrrrrr" + chooser.getFilename());
			try {
			    int bytesNr = fis.available();
			    b = new byte[bytesNr];
			    System.out.println(bytesNr + "  bytes            " );
			    System.out.println(b.length);
			    int i = fis.read(b);
			    fis.close();
			    componentTable.remove("jpegPhoto");
			    componentTable.put("jpegPhoto", new SLabel(new
                                SImageIcon(JPEGPATH + chooser.getFilename())));
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
	}
	
	if (evt.getSource().equals(removeButton)) {
	    System.out.println("please remove entry");
	    String dn = getDN();
	    System.out.println("dn is " + dn);
	    LdapWorker worker = getLdapWorker();
	    worker.removeEntry(dn);
	    existentAttrsF.removeAll();
	    otherAttrsF.removeAll();
	    LdapTreeNode nd= (LdapTreeNode)getNode();
	    LdapTreeNode parent = (LdapTreeNode)nd.getParent();
	    DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
	    if (parent!=null) {
		//parent.remove(node);
		model.removeNodeFromParent(nd);
		//model.nodesWereRemoved(parent,new int[]{parent.getIndex(node)}, new Object[] {node});
	    }
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

    public void stateChanged (ChangeEvent ch) {
	System.out.println("anderes tab");
    }

    public void valueChanged(TreeSelectionEvent e) {
	TreeNode node = (TreeNode)tree.getLastSelectedPathComponent();
	if (node == null || !(node instanceof LdapTreeNode)) {
	    System.out.println("nothing selected");
	    return;
	}
        String actDN = ((LdapTreeNode)node).getDN();
        if (actDN.endsWith(",")) {
            int k = actDN.lastIndexOf(",");
            actDN = actDN.substring(0,k);
        }
                
        setDN(actDN);
	setNode((LdapTreeNode)node);
	
	componentTable.clear();
	textHashtable.clear();
	
	existentAttrsF.removeAll();

	LdapWorker worker = getLdapWorker();
        System.err.println("selecting: " + getDN());
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
				values = values + DELIM + o;
			    }
			    else {
				values = (String)o;
			    }
			}
			SComponent attrField; 
			if (!label.equals("objectClass")) {
			    attrField = new STextField((String)values);
			    ((STextField)attrField).setColumns(columns);
			    ((STextField)attrField).setMaxColumns(maxcolumns);
			}
			else { 
			    attrField = new SLabel((String)values);
			}
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
			    SLabel photo = new SLabel(new SImageIcon(new ImageIcon((byte [])o)));
			    componentTable.put(label,photo);
			}
			if (label.equals("userPassword")) {
			    SLabel attrLa = new SLabel(label);
			    STextField attrF = new SPasswordField();
			    attrF.setColumns(columns);
			    attrF.setMaxColumns(maxcolumns);
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
	    otherAttrsF.add(addAttribute);
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
	    if (((String)key).equals("userPassword")) {
		existentAttrsF.add(new SLabel((String)key));
		uPw = new SPasswordField();
		uPw.setText(((SPasswordField)comp.get(key)).getText());
		existentAttrsF.add(uPw);
	    }
	    if (((String)key).equals("jpegPhoto")) {
		SLabel label = new SLabel("Select jpegPhoto...");
		existentAttrsF.add(label);
		chooser = new SFileChooser();
		existentAttrsF.add(chooser);
	    }
	}
	existentAttrsF.add(commitButton);
	existentAttrsF.add(removeButton);
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

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
