package ldap;

import javax.naming.*;
import javax.naming.directory.*;
import java.util.Hashtable;
import java.lang.Object;
import java.util.ArrayList;
import java.util.Enumeration;

public class LdapClient
{
    private static final int BASE = SearchControls.OBJECT_SCOPE;
    private static final int ONE  = SearchControls.ONELEVEL_SCOPE;
    private static final int SUB  = SearchControls.SUBTREE_SCOPE;

    private DirContext ctx;
    private boolean success = true;
    private String baseDN;
    private String bindDN;
    private String password;
    private String server;
    
    public LdapClient (String server, String baseDN, String bindDN, String password) {
	
	this.baseDN = baseDN;
	this.bindDN = bindDN;
	this.password = password;
	Hashtable env = new Hashtable();
	env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
	env.put(Context.PROVIDER_URL, "ldap://" + server);
	env.put(Context.SECURITY_PRINCIPAL, bindDN);
	env.put(Context.SECURITY_CREDENTIALS, password);
	//env.put(Context.PROVIDER_URL, "ldap://192.168.230.131:389");
	//env.put(Context.SECURITY_PRINCIPAL,"cn=admin,dc=tiscon,dc=de");
	//env.put(Context.SECURITY_PRINCIPAL,"cn=admin,dc=tiscon,dc=de");

	try {
	    ctx = new InitialDirContext(env);
	}
	catch(NamingException e) {
	    setSuccess(false);
	    System.err.println("Problem " + e);
	}
	//setSuccess(true);
    }

    public String getBaseDN() {
	return baseDN;
    }

    public String getBindDN() {
	return bindDN;
    }
    
    public String getServer() {
	return server;
    }

    private void setSuccess(boolean s) {
	success = s;
    }

    public boolean getSuccess() {
	/*String s = new String();
	  if (success==true) s="connected";
	  else if (success == false) s="connection failed";
	  return s;*/

	return success;
    }


    public boolean modifyAttributes(String dn, Hashtable h) {
	Hashtable hashT;
	String DN;
	Object key;
	
	DN = dn;
	hashT = h;
	
	System.out.println("zu aendern bei " +dn);

	try {
	    BasicAttributes a = new BasicAttributes();
	    Enumeration e = h.keys();
	    while (e!=null && e.hasMoreElements()) {
		key = e.nextElement();
		System.out.println("halllllooooo");
		System.out.println((String)(key) + " : " + (String)h.get(key));
		a.put(new BasicAttribute((String)(key),(String)h.get(key)));
		System.out.println((String)(key) + " : " + (String)h.get(key));
	    }
	    ctx.modifyAttributes(dn,ctx.REPLACE_ATTRIBUTE,a);
	    return true;
	}
	catch(Exception e) {
	    System.out.println(e);
	    return false;
	}
    }



    public boolean getWAccess(String dn, String attrName, String attrValue) {
	try {
	    System.out.println("oldValue  " +  attrValue);
	    ModificationItem mods = new ModificationItem(ctx.REPLACE_ATTRIBUTE,
							 new BasicAttribute(attrName, attrValue));
	    BasicAttributes a = new BasicAttributes();
	    a.put(new BasicAttribute(attrName, attrValue));
	    ctx.modifyAttributes(dn, ctx.REPLACE_ATTRIBUTE,a);
	    System.out.println("kann ich aendern");
	    return true;
	}
	catch (Exception e) {
	    System.out.println(e);
	    return(false);
	    //System.out.println(e);
	}
	
    }


    public Hashtable getDNAttributes(String dn)
    {
	ArrayList attrList = new ArrayList();
	Hashtable h = new Hashtable();
	Attributes a;
	NamingEnumeration e;
	try {
	    a = ctx.getAttributes(dn);
	    e = a.getAll();
	    while (e != null && e.hasMoreElements()) {
		BasicAttribute ba = (BasicAttribute)e.next();
		h.put(ba.getID(),ba.get());
	    }
	}
	catch (NamingException exc) {
	}
	Enumeration enum = h.keys();
	while (enum !=null  && enum.hasMoreElements()) {
	    Object key = enum.nextElement();
	    System.out.println(h.get(key));
	}
	return (h);
    }
    
    NamingEnumeration list(String searchbase) {
	BasicAttribute n;
	Integer i;
	String [] attrs = null;
	NamingEnumeration results = null;
	
	attrs = new String [] {"objectclass"};
	try {
	    results = search(searchbase, "(objectclass=*)",attrs, ONE);
	}
	catch (NamingException e) {
	    System.err.println(e);
	}
	return results;
	
    }
    
    private NamingEnumeration search(String dn, String filter, String []attribs,
				     int type) 
	throws NamingException {
	
	/* specify search constraints to search subtree */
	SearchControls constraints = new SearchControls();
	
	constraints.setSearchScope(type);	 
	constraints.setCountLimit(0);
	constraints.setTimeLimit(0);
	constraints.setReturningAttributes(attribs);
	
	NamingEnumeration results = ctx.search(dn, filter, constraints);	
	if (results == null) System.out.println("nichts im result");
	return results;
	
    }
    
    public void addContext(String ctxString) {
	Attributes attrs = new BasicAttributes(true); // case-ignore
	Attribute objclass = new BasicAttribute("objectclass");
	objclass.add("top");
	objclass.add("extensibleObject");
	attrs.put(objclass);
	     
	try {
	    // Create the context
	    Context result = ctx.createSubcontext("cn=Fruits", attrs);
		 
	    // Check that it was created by listing its parent
	    NamingEnumeration list = ctx.list("");
	     
	    // Go through each item in list
	    while (list.hasMore()) {
		NameClassPair nc = (NameClassPair)list.next();
		System.out.println(nc);
	    }
	     
	    // Close the contexts when we're done
	    result.close();
	    ctx.close();
	}
	catch (NamingException e) {
	    System.out.println("Create failed: " + e);
	}
	     
    }

    public void close() {
	try {
	    ctx.close();
	}
	catch (NamingException e) {
	    System.out.println(e);
	}
    }
}
