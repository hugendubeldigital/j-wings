package ldap;

import javax.naming.*;
import javax.naming.directory.*;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Collections;

public class LdapTreeNode implements TreeNode {
    
    private String dn;
    private LdapTreeNode parent;
    ArrayList children = null;
    private LdapWorker worker;

    public LdapTreeNode(LdapWorker worker, LdapTreeNode parent, String dn) {

	this.worker = worker;
	this.dn = dn;
	this.parent = parent;
	
    }
    
    public LdapTreeNode(String dn) {
	this.dn = dn;
    }
    
    
    public TreeNode getChildAt(int childIndex) {
	if (children==null) {
	    getChildren();
	}
	return (TreeNode)children.get(childIndex);
    }
    
    
    public int getChildCount() {
	return children.size();
    }

    
    public TreeNode getParent() {
	return (TreeNode)parent;
	}

    
    public int getIndex(TreeNode node) {
	if (children ==null) {
	    getChildren();
	}
	return children.indexOf(node);
    }

    
    public boolean getAllowsChildren() {
	return true;
    }

    
    public boolean isLeaf() {
	if (children == null) {
	    getChildren();
	}
	if (children.isEmpty()) return true;
	else return false;
	}

    public Enumeration children() {
	if (children == null )getChildren();
	return Collections.enumeration(children);
    };
    
    private void getChildren() {
	NamingEnumeration results = worker.list(dn);
	children = new ArrayList();
	try {
	    while (results != null && results.hasMoreElements()) {
		SearchResult sr  = (SearchResult)results.next(); 
		children.add(new LdapTreeNode(worker,this,sr.getName() + "," + dn));
	    }
	}
	catch (NamingException e) {
	    System.err.println(e);
	    
	}
    }
    
    public String getDN() {
	return dn;
    }


    public String toString() {
	LdapTreeNode parent = (LdapTreeNode)getParent();
	if (parent != null) {
	    String parentDn = new String(parent.getDN());
	    String thisDn = dn;
	    int at = thisDn.indexOf(parentDn);
	    return thisDn.substring(0,at-1);
	}
	return dn;
    }

    
    public void addChild(LdapTreeNode child) {
	child.parent = this;
	children.add(child);
    }
}
                                                                                                               
