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
import javax.swing.event.TreeSelectionEvent;


import org.wings.*;
import org.wings.io.Device;
import org.wings.io.ServletDevice;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wings.util.*;

public class LdapBrowserSession 
    extends SessionServlet
    implements SConstants
{
    private final static String NOT_CONNECTED = "not connected";

    private LdapWorker worker = null;

    private STabbedPane tabbedPane;
    private SForm settingsForm;
    private SForm dataForm;
    

    public LdapBrowserSession(Session session) {
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
	tabbedPane.add(settingsForm, "ConnectionSettings");
	
	dataForm = new SForm(new SGridLayout(2));
	tabbedPane.add(dataForm,"people");

	SLabel descBindDN = new SLabel("name");
	final STextField bindDN= new STextField("");
	bindDN.setColumns(30);
	settingsForm.add(descBindDN);
	settingsForm.add(bindDN);

	SLabel descBindDNPassword = new SLabel("password");
	final SPasswordField bindDNPassword= new SPasswordField();
	bindDNPassword.setColumns(30);
	settingsForm.add(descBindDNPassword);
	settingsForm.add(bindDNPassword);
    }
    
    public String getServletInfo() {
        return "LdapBrowser $Revision$";
    }
}
