package ldap;

import javax.naming.*;
import javax.naming.directory.*;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Collections;

public class LdapTreeNode implements TreeNode
{
    private String dn;

    private LdapTreeNode parent;
    private ArrayList children = null;
    private LdapClient client;

    public LdapTreeNode(LdapClient client, LdapTreeNode parent, String dn) {
	this.client = client;
	this.dn = dn;
	this.parent = parent;
    }

    public LdapTreeNode(String dn) {
	this.dn = dn;
    }

    public TreeNode getChildAt(int childIndex) {
	//System.out.println("getChildAt" + childIndex);
	if (children == null) {
	    getChildren();
	}
	return (TreeNode)children.get(childIndex);
    }

    public int getChildCount() {
	//System.out.println("getChildCount");
	return children.size();
    }

    
    public TreeNode getParent() {
	//System.out.println("getParent");
	return (TreeNode)parent;
    }

    
    public int getIndex(TreeNode node) {
	//System.out.println("getIndex");
	if (children == null) {
	    getChildren();
	}
	return children.indexOf(node);
    }

    
    public boolean getAllowsChildren() {
	//System.out.println("getAllo");
	return true;
    }

    
    public boolean isLeaf() {
	if (children == null) {
	    getChildren();
	}
	return children.isEmpty();
    }

    public Enumeration children() {
	if (children == null)
	    getChildren();
	return Collections.enumeration(children);
    }
    
    private void getChildren() {
	//System.out.println("getChildAt");
	NamingEnumeration results = client.list(dn);
	children = new ArrayList();
	try {
	    while (results != null && results.hasMoreElements()) {
		SearchResult sr  = (SearchResult)results.next(); 
		children.add(new LdapTreeNode(client, this, sr.getName()));
	    }
	}
	catch (NamingException e) {
	    System.err.println(e);
	    
	}
    }

    public String getDN() {
	return dn;
    }

    public String getCompleteDN() {
	if (getParent() == null || !(getParent() instanceof LdapTreeNode))
	    return getDN();
	else
	    return ((LdapTreeNode)getParent()).getCompleteDN() + "," + getDN();
    }

    public String toString() {
	return getDN();
    }
}
