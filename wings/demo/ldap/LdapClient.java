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
	       TreeSelectionListener 
{  
    private final static Logger logger = Logger.getLogger("ldap");

    SFrame frame;

    private final int columns = 50;
    private final int maxcolumns = 100;

    private ResourceBundle resources;

    private DirContext context;
    private TreeModel treeModel; 
    private String dn;

    private STabbedPane tabbedPane;
    private SPanel mainPanel;
    private STree tree;

    private AddObjectPanel addPanel;
    private EditObjectPanel editPanel;
    private SPanel treePanel;

    private SForm settingsForm;
    private STextField urlTextField;
    private STextField basednTextField;
    private STextField binddnTextField;
    private STextField passwordTextField;
    private SButton connectButton;
    private SButton disconnectButton;

    public LdapClient()
        throws Exception
    {
        try {
            resources = ResourceBundle.getBundle("ldap/LdapClient", SessionManager.getSession().getLocale());
        }
        catch (MissingResourceException e) {
            throw new ServletException("resource bundle not found", e);
        }

        frame = new SFrame("LDAP Client");

        SContainer contentPane = frame.getContentPane();
        tabbedPane = new STabbedPane();
        contentPane.setLayout(null);

        settingsForm = new SForm(new SGridLayout(2));
        tabbedPane.add(settingsForm, resources.getString("provider"));

        urlTextField = new STextField();
        urlTextField.setColumns(columns);
        urlTextField.setText((String)SessionManager.getSession().getProperty("java.naming.provider.url"));
        settingsForm.add(new SLabel(resources.getString("provider.url")));
        settingsForm.add(urlTextField);

        basednTextField = new STextField();
        basednTextField.setColumns(columns);
        basednTextField.setText((String)SessionManager.getSession().getProperty("java.naming.provider.basedn"));
        settingsForm.add(new SLabel(resources.getString("provider.basedn")));
        settingsForm.add(basednTextField);
	
        binddnTextField = new STextField();
        binddnTextField.setColumns(columns);
        binddnTextField.setText((String)SessionManager.getSession().getProperty("java.naming.security.principal"));
        settingsForm.add(new SLabel(resources.getString("provider.binddn")));
        settingsForm.add(binddnTextField);
	
        passwordTextField = new SPasswordField();
        passwordTextField.setColumns(columns);
        passwordTextField.setText((String)SessionManager.getSession().getProperty("java.naming.security.credentials"));
        settingsForm.add(new SLabel(resources.getString("provider.password")));
        settingsForm.add(passwordTextField);
	
        connectButton = new SButton(resources.getString("provider.connect"));
        disconnectButton = new SButton(resources.getString("provider.disconnect"));
        disconnectButton.setVisible(false);
        settingsForm.add(connectButton);
        settingsForm.add(disconnectButton);

        mainPanel = new SPanel();

        try {
            mainPanel.setLayout(new STemplateLayout(getClass().getResource("ldapclientlayout.html")));
        }
        catch(Exception e) {
	    logger.log(Level.WARNING, "no template", e);
            mainPanel.setLayout(new SFlowLayout());
        }

        tabbedPane.add(mainPanel, resources.getString("browser"));

        createTreeModel(null);
	tree = new STree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);

        SessionManager.getSession().setProperty("tree", tree);

        editPanel = new EditObjectPanel();
        mainPanel.add(tree,"tree");
        mainPanel.add(editPanel, "editor");

        addPanel = new AddObjectPanel();
        tabbedPane.add(addPanel, resources.getString("add"));

        contentPane.add(tabbedPane);

        connectButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
                    Session session = SessionManager.getSession();

                    String url = urlTextField.getText();
                    if (url != null && url.length() > 0)
                        session.setProperty("java.naming.provider.url", url);

                    String basedn = basednTextField.getText();
                    if (basedn != null && basedn.length() > 0)
                        session.setProperty("java.naming.provider.basedn", basedn);

                    String binddn = binddnTextField.getText();
                    if (binddn != null && binddn.length() > 0)
                        session.setProperty("java.naming.security.principal", binddn);

                    String password = passwordTextField.getText();
                    if (password != null && password.length() > 0)
                        session.setProperty("java.naming.security.credentials", password);

                    try {
                        context = new InitialDirContext(new Hashtable(session.getProperties()));
                        createTreeModel(context);
			tree.setModel(treeModel);
			tabbedPane.setSelectedIndex(1);

			urlTextField.setVisible(true);
			basednTextField.setVisible(true);
			binddnTextField.setVisible(true);
			passwordTextField.setVisible(true);

                        connectButton.setVisible(true);
			disconnectButton.setVisible(false);
                        passwordTextField.setText(null);
		    }
                    catch (NamingException e) {
                        passwordTextField.setText(null);
                        logger.log(Level.WARNING, "no initial context", e);
		    }
		}
	    });
	
        disconnectButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
		    createTreeModel(null);
		    tree.setModel(treeModel);

                    urlTextField.setVisible(false);
                    basednTextField.setVisible(false);
                    binddnTextField.setVisible(false);
                    passwordTextField.setVisible(false);

                    connectButton.setVisible(false);
                    disconnectButton.setVisible(true);
		    passwordTextField.setText(null);
		}
	    });

        frame.show();
    }

    private void createTreeModel(DirContext context) {
	TreeNode root = null;

	if (context != null) {
            String base = (String)SessionManager.getSession().getProperty("java.naming.provider.basedn");
	    root = new LdapTreeNode(context, null, base);
        }
	else
	    root = new DefaultMutableTreeNode(resources.getString("browser.not_connected"));

	treeModel = new DefaultTreeModel(root);
    }

    private void setDN(String dn) {
	this.dn = dn;
        logger.fine("dn: " + dn);

        try {
            editPanel.setDn(dn);
            addPanel.setParent(dn);
        }
	catch (NamingException e) {
	    logger.log(Level.SEVERE, "selection failed", e);
	}
    }
    public String getDN() { return dn; }


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
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
