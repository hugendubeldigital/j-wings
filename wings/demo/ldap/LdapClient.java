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
import java.util.List;
import java.util.logging.*;

import javax.naming.*;

import javax.naming.directory.*;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import org.wings.*;
import org.wings.border.*;
import org.wings.io.Device;
import org.wings.io.ServletDevice;
import org.wings.session.*;
import org.wings.util.*;


public class LdapClient
    implements SConstants, 
	       TreeSelectionListener, 
	       ActionListener,
	       ChangeListener
{  
    private final static Logger logger = Logger.getLogger("ldap");

    SFrame frame;
    public static Locale crtLocale = Locale.GERMAN;

    private final static String NOT_CONNECTED = "not connected";
    private final static String JPEGPATH = "/home/nina/wings/demo/ldap/jpeg/";
    private final static String LF= "LaufendeFortbildung";
    private final static String TAET= "taetigkeitsbereiche";
    private final static String ZUSATZ= "zusatzausbildungen";
    private final static String TEAM= "team";
    private final static String BERGANG= "werdegang";
    private final static String DELIM= ":";
    private final static String CONNECTIONSETTINGS= "Verbindung";
    private final static String SELECTPHOTO= "Foto aussuchen...";
    private final int columns = 50;
    private final int maxcolumns = 100;
    
    ResourceBundle stats;

    private LdapWorker worker = null;
    private TreeModel treeModel; 
    private String dn;
    private LdapTreeNode node;
    
    private STabbedPane tabbedPane;
    private SPanel mainPanel;
    private STree tree;
    
    private AddObjectPanel addPanel;
    private EditObjectPanel editPanel;
    private SPanel treePanel;
    
    private SForm settingsForm;
    private STextField server;
    private STextField baseDN;
    private STextField bindDN;
    private STextField bindDNPassword;
    private SButton connectButton;
    private SButton disconnectButton;

    public LdapClient()
        throws Exception
    {
        frame = new SFrame("LDAP Client");

        stats = ResourceBundle.getBundle("ldap.AttributeResources", crtLocale);
        SContainer contentPane = frame.getContentPane();
        tabbedPane = new STabbedPane();
        contentPane.setLayout(new SFlowLayout());
        //contentPane.setLayout(new STemplateLayout(getClass().getResource("ldapclient.html")));
	
        settingsForm = new SForm(new SGridLayout(2));
        tabbedPane.add(settingsForm, CONNECTIONSETTINGS);
	
        SLabel descServer = new SLabel("sever:port");
        server = new STextField("");
        server.setColumns(columns);
        server.setText((String)SessionManager.getSession().getProperty("java.naming.provider.url"));
        settingsForm.add(descServer);
        settingsForm.add(server);
	
        SLabel descBaseDN = new SLabel("base DN");
        baseDN = new STextField();
        baseDN.setColumns(columns);
        baseDN.setText((String)SessionManager.getSession().getProperty("java.naming.provider.basedn"));
        settingsForm.add(descBaseDN);
        settingsForm.add(baseDN);
	
        SLabel descBindDN = new SLabel("bind DN");
        bindDN= new STextField();
        bindDN.setText((String)SessionManager.getSession().getProperty("java.naming.security.principal"));
        bindDN.setColumns(columns);
        settingsForm.add(descBindDN);
        settingsForm.add(bindDN);
	
        SLabel descBindDNPassword = new SLabel("password");
        bindDNPassword= new SPasswordField();
        bindDNPassword.setText((String)SessionManager.getSession().getProperty("java.naming.security.credentials"));
        bindDNPassword.setColumns(columns);
        settingsForm.add(descBindDNPassword);
        settingsForm.add(bindDNPassword);
	
        connectButton = new SButton("connect");
        disconnectButton = new SButton("disconnect");
        disconnectButton.setVisible(false);
        settingsForm.add(connectButton);
        settingsForm.add(disconnectButton);


        mainPanel = new SPanel();
        try {
            mainPanel.setLayout(new STemplateLayout(getClass().getResource("ldapclientlayout.html")));
        }
        catch(Exception e) {
	    logger.log(Level.WARNING, "no template", e);
        }
        tabbedPane.add(mainPanel, "Browser");

        createTreeModel(null);
        createTree();
        editPanel = new EditObjectPanel();
        mainPanel.add(tree,"tree");
        mainPanel.add(editPanel, "editor");

        addPanel = new AddObjectPanel();
        tabbedPane.add(addPanel, "Neues Objekt");

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
		    }
		    else {
			connectButton.setVisible(true);
			disconnectButton.setVisible(false);
		    }
		    
		    if (success) {
			setLdapWorker(worker);
			createTreeModel(worker);
			tree.setModel(treeModel);
		    }
		}
	    });
	
        disconnectButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    createTreeModel(null);
		    tree.setModel(treeModel);
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

        frame.show();
        System.out.println(SessionManager.getSession().getServletRequest().getAuthType());
    }
    
    
    
    private LdapWorker getLdapWorker() {
	return worker;
    }
    
    private void setLdapWorker(LdapWorker c) {
	this.worker = c;
    }

    private void createTreeModel(final LdapWorker c) {
	TreeNode root = null;

        
	if (c != null) {
	    root = new LdapTreeNode(c.getContext(), null, c.getBaseDN());
        }
	else
	    root = new DefaultMutableTreeNode(NOT_CONNECTED);

	treeModel = new DefaultTreeModel(root);
    }

    private void createTree() {
	tree = new STree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
        //in der session einbinden
        SessionManager.getSession().setProperty("tree",tree);

        
    }

    public void actionPerformed(ActionEvent evt) {
	String newValue = null;
	String oldValue = null;
	String oValue;

        /*
	if (evt.getSource().equals(removeButton)) {
            String dn = getDN();
	    
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
        */
    }


    private void setDN(String dn) {
	this.dn = dn;
        logger.fine("dn: " + dn);

        try {
            editPanel.setDn(dn);
            //editPanel.setTree(tree);
            addPanel.setParent(dn);

        }
	catch (NamingException e) {
	    logger.log(Level.SEVERE, "selection failed", e);
	}
    }
    public String getDN() { return dn; }

    private void setNode(LdapTreeNode node) {
	this.node=node;
    }
    public LdapTreeNode getNode() { return node; }


    public void stateChanged(ChangeEvent ch) {
	System.out.println("switch tab");
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

        // the dn and the node, that might have to be updated later
        setDN(actDN);
	setNode((LdapTreeNode)node);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
