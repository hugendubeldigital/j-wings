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

import javax.servlet.*;
import javax.servlet.http.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;


import org.wings.*;
import org.wings.io.Device;
import org.wings.io.ServletDevice;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wings.util.*;


public class LdapClientSession 
    extends SessionServlet
    implements SConstants, TreeSelectionListener, ActionListener
{
    private final static String NOT_CONNECTED = "not connected";

    private LdapWorker worker = null;
    private TreeModel treeModel; 
    private String dn;
    private LdapTreeNode node;

    private STabbedPane tabbedPane;
    private SForm settingsForm;
    private SPanel mainPanel;
    private STree tree;

    private SDesktopPane cards;
    private SForm editorPanel;
    private SPanel attrPanel;
    private SButton commitButton;
    private AddObjectsPanel addPanel;


    private Hashtable attrHTable;
    private Hashtable attrHLTable;
    private SPanel oPanel = new SPanel();

    private Hashtable textHashtable = new Hashtable();
    private Hashtable componentTable = new Hashtable();


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
	final STextField baseDN = new STextField("");
	baseDN.setColumns(30);
	baseDN.setText(((PropertyService)getSession()).getProperty("ldap.server.basedn"));
	settingsForm.add(descBaseDN);
	settingsForm.add(baseDN);

	SLabel descBindDN = new SLabel("bind DN");
	final STextField bindDN= new STextField("");
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

	mainPanel = new SPanel(new SGridLayout(2));
	tabbedPane.add(mainPanel, "Browser");

	createTreeModel(null);
	createTree();
	mainPanel.add(tree);

	editorPanel = new SForm();
	mainPanel.add(editorPanel);

	attrPanel = new SPanel();
	attrPanel.setLayout(new SGridLayout(2));
	editorPanel.add(attrPanel);

	commitButton = new SButton("Commit");
	commitButton.addActionListener(this);
	editorPanel.add(commitButton);

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

	Iterator iterator = componentTable.entrySet().iterator();
	while (iterator.hasNext()) {
	    Map.Entry entry = (Map.Entry)iterator.next();
	    SLabel key = (SLabel)entry.getKey();

	    ArrayList modifiedTextF;
	    ArrayList oldTextF;
	    Hashtable toBeChanged = new Hashtable();

	    modifiedTextF = (ArrayList)(componentTable.get(key));
	    oldTextF = (ArrayList)(textHashtable.get(key));
	    int i = 0;
	    while (i < modifiedTextF.size()) {
		oldValue = (String)(oldTextF.get(i));
		newValue = ((STextField)modifiedTextF.get(i)).getText();
		i++;
		System.out.println("new value " +newValue);
		System.out.println("old value " +oldValue);
		if (!oldValue.equals(newValue)) {
		    toBeChanged.put((key).getText(),newValue);
		    System.out.println((key).getText() + "  " + newValue);
		}
	    }

	    if (!toBeChanged.isEmpty()) {
		LdapWorker worker = getLdapWorker();
		//System.out.println("einige attr. zu aendern");
		System.out.println(getDN());
		worker.modifyAttributes(getDN(), toBeChanged);
		//LdapTreeNode newNode = new LdapTreeNode(c,(LdapTreeNode)node.getParent(),getDN());
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
	attrHTable = new Hashtable();
	attrHLTable = new Hashtable();

	attrPanel.removeAll();

	LdapWorker worker = getLdapWorker();
	Hashtable attributes = worker.getDNAttributes(getDN());

	Enumeration enum = attributes.keys();
	while (enum != null && enum.hasMoreElements()) {
	    Object key = enum.nextElement();

	    ArrayList textAl;
	    ArrayList textFAl;
	    textAl = (ArrayList)(attributes.get(key));
	    textFAl =new ArrayList();
	    int ii = 0;
	    while (ii<textAl.size()) {
		textFAl.add(new STextField((String)(textAl.get(ii))));
		ii++;
	    }

	    ArrayList textContentAl;
	    SLabel label = new SLabel((String)key);
	    boolean wAccess = false;

	    textContentAl = new ArrayList();
	    int i = 0;
	    while (i<textAl.size()) {
		System.out.println((String)(textAl.get(i)));
		textContentAl.add((String)(textAl.get(i)));
		i++;
		
	    }
	    componentTable.put(label,textFAl);
	    textHashtable.put(label,textContentAl);
	    
	}
	
	Enumeration compEnum;
	compEnum = componentTable.keys();
	while (compEnum!=null && compEnum.hasMoreElements()) {
	    Object key = compEnum.nextElement();
	    //System.out.println("put in attrPanel " + ((SLabel)key).getText());
	    //attrForm.add((SLabel)key);
	    ArrayList textFAl = (ArrayList)componentTable.get(key);
	    int i = 0;
	    while (i< textFAl.size()) {
		attrPanel.add((SLabel)key);
		attrPanel.add((STextField)textFAl.get(i));
		i++;
	    }
	    
	    //attrForm.add((SComponent)componentTable.get(key));
	    //if(componentTable.get(key).getClass().getName().equals("org.wings.STextField")) textList.add(((STextField)componentTable.get(key)).getText());
	}
    }


    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "LdapClient $Revision$";
    }
}
