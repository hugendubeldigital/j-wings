package ldap;


import javax.naming.*;
import javax.naming.directory.*;
import java.util.Hashtable;
import java.lang.Object;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.StringTokenizer;

public class LdapWorker
{
    private static final int BASE = SearchControls.OBJECT_SCOPE;
    private static final int ONE  = SearchControls.ONELEVEL_SCOPE;
    private static final int SUB  = SearchControls.SUBTREE_SCOPE;

    private LdapTreeNode root;

    
    DirContext ctx;
    private boolean success = true;
    private String baseDN;
    private String bindDN;
    private String password;
    private String server;
    
    public LdapWorker (String server, String baseDN, String bindDN, String password) {
	
	this.baseDN = baseDN;
	this.bindDN = bindDN;
	this.password = password;
	Hashtable env = new Hashtable();
	env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
	env.put(Context.PROVIDER_URL, "ldap://" + server);
	//env.put(Context.PROVIDER_URL, "ldap://192.168.10.21:389");
	env.put(Context.SECURITY_PRINCIPAL,"cn=admin,dc=tiscon,dc=de");
	env.put(Context.SECURITY_PRINCIPAL,bindDN);
	//env.put(Context.SECURITY_PRINCIPAL,"cn=admin,dc=engels,dc=de");
	env.put(Context.SECURITY_CREDENTIALS,password);
	//env.put(Context.SECURITY_CREDENTIALS,"secret");
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
	return success;
    }



    //veraendert attribute
    public boolean modifyAttributes(String dn, BasicAttributes attributes) {
	try {
	    ctx.modifyAttributes(dn,ctx.REPLACE_ATTRIBUTE,attributes);
	    return true;
	    
	}
	catch(Exception e) {
	    System.out.println(e);
	    return false;
	}
    }


    //gibt einen Vector der unterstuetzten objketklassen zurueck
    public ArrayList getObjects() {
	ArrayList ol = null;
	try {
	    	    
	    DirContext schema = ctx.getSchema("");
	    SearchControls ctrl = new SearchControls();

	    String [] attrs = null;
	    attrs = new String [] {"objectclasses"};
	    
	    ctrl.setSearchScope(SearchControls.OBJECT_SCOPE);
	    ctrl.setReturningAttributes(attrs);
	    
	    // Search for objects that have those matching attributes
	    NamingEnumeration answer = ctx.search("cn=subschema","(objectclass=*)",ctrl);
	    
	    Attribute attr = null;


	    while (answer.hasMore()) {
		SearchResult sr = (SearchResult)answer.next();
		System.out.println(">>>" + sr.getName());
		Attributes attribs = sr.getAttributes();
		for (NamingEnumeration ae = attribs.getAll();
		     ae.hasMore();) {
		    attr = (Attribute)ae.next();
		    System.out.println("attribute: " + attr.getID());
		}

		//ab da
		NamingEnumeration ne = attr.getAll();
		ol = getObjNames(ne);
		int i=0;
		while (i<ol.size()) {
		    System.out.println(ol.get(i));
		    //getObjectDefinition((String)ol.get(i));
		    i++;
		}
	    }
	    // Close the context when we're done
	    schema.close();
	} 
	catch (Exception e) {
	    e.printStackTrace();
	}
	return ol;
    }
    

    public ArrayList getObjNames(NamingEnumeration ne) {
	ArrayList objArray = new ArrayList();

	try {while (ne!=null && ne.hasMore()) {
	    String oString = ne.next().toString();
	    System.out.println(oString);
	    StringTokenizer sto = new StringTokenizer(oString,new String("'"));
	    String obj = sto.nextToken();
	    obj = sto.nextToken();
	    
	    int index = oString.indexOf("SUP") + 4;
	    String rest = null;
	    String sup = null;
	    if (index > 0)
		rest = oString.substring(index);
	    if (rest!=null) { 
		StringTokenizer suptok = new StringTokenizer(rest," ");
		sup = suptok.nextToken();
	    }
	    
	    System.out.println("rest" + rest);
	    if (sup!=null && !sup.equals("top")) obj = obj + "TOP" +"(" + sup + ")";
		
	    System.out.println(obj);	    
	    objArray.add(obj);
	    	    
	}
	}
	catch (NamingException nex) {
	    System.out.println(nex);
	}
	return objArray;
    }
    

    public boolean getWAccess(String dn, String attrName, String attrValue) {
	try {
	    ModificationItem mods = new ModificationItem(ctx.REPLACE_ATTRIBUTE,
                new BasicAttribute(attrName, attrValue));
	    BasicAttributes a = new BasicAttributes();
	    
	    if (!attrValue.equals("")) 
		a.put(new BasicAttribute(attrName, attrValue));
	    else 
		a.put(new BasicAttribute(attrName, "01"));

	    if (!attrValue.equals(""))
		ctx.modifyAttributes(dn, ctx.REPLACE_ATTRIBUTE,a);
	    else {
		ctx.modifyAttributes(dn, ctx.ADD_ATTRIBUTE,a);
		System.out.println("habe " + a.toString() + "geaendert");
		try {
		    ctx.modifyAttributes(dn, ctx.REMOVE_ATTRIBUTE,a);
		    System.out.println("wieder rueckgaengig " + a.toString());
		}
		catch(javax.naming.directory.InvalidSearchFilterException exc) {
		    System.out.println("habe dich entdeckt " + exc);
		    BasicAttributes bat = new BasicAttributes();
		    bat.put(new BasicAttribute(attrName,"aaa"));
		    ctx.modifyAttributes(dn, ctx.REPLACE_ATTRIBUTE,bat);
		    ctx.modifyAttributes(dn, ctx.REMOVE_ATTRIBUTE,bat);
		    
		}
		
	    }
	    System.out.println(a + "kann ich aendern");
	    return true;
	    }
	catch (Exception e) {
	    System.out.println(e);
	    return(false);
	}
	
    }

    
    //gibt die attribute eines dn's zurueck
    public Attributes getDNAttributes(String dn)
    {
	ArrayList attrList = new ArrayList();
	Hashtable h = new Hashtable();
	Attributes a = null;
	NamingEnumeration e;
	try {
	    a = ctx.getAttributes(dn);
	    e = a.getAll();
	    /*while (e != null && e.hasMoreElements()) {
		BasicAttribute ba = (BasicAttribute)e.next();
		int i = 0;
		int size = ba.size();
		if (size > 1) {
		    ArrayList al = new ArrayList();
		    while (i < size) {
			al.add(ba.get(i));
			System.out.println("attribut" + ba.getID() + ba.get(i) + "bei " + i);
			i++;
		    }
		    h.put(ba.getID(),al);
		}
		else {
		    ArrayList al = new ArrayList();
		    System.out.println("attribut " + ba);
		    if (ba.get() == null)
			al.add("");
		    else
			al.add(ba.get());
		    h.put(ba.getID(),al);
		}
		}*/
	}
	catch (NamingException exc) {
	}
	/*Enumeration enum = h.keys();
	while (enum !=null  && enum.hasMoreElements()) {
	    Object key = enum.nextElement();
	    ArrayList al = (ArrayList)(h.get(key));
	    int index = 0;
	    while (index<al.size()) {
		System.out.println("das ist ein attr" + (String)key + al.get(index));
		index++;
	    }
	}
	
	Vector objectAttributes = getObjectAttributes(dn);
	
	Enumeration en = h.keys();
	while (en != null && en.hasMoreElements()) {
	    Object key = en.nextElement();
	    if (objectAttributes.contains((String)key)) 
		objectAttributes.remove((String)key);
	}
	
	Enumeration er  = objectAttributes.elements();
	
	
	
	while (er!=null && er.hasMoreElements()) {
	    ArrayList al = new ArrayList();
	    al.add("");
	    h.put((String)er.nextElement(),al);
	    }*/
	
	
	
	return (a);
    }
    
    /*public Hashtable getDNAttributes(String dn)
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
		int i = 0;
		int size = ba.size();
		if (size > 1) {
		    ArrayList al = new ArrayList();
		    while (i < size) {
			al.add(ba.get(i));
			System.out.println("attribut" + ba.getID() + ba.get(i) + "bei " + i);
			i++;
		    }
		    h.put(ba.getID(),al);
		}
		else {
		    ArrayList al = new ArrayList();
		    System.out.println("attribut " + ba);
		    if (ba.get() == null)
			al.add("");
		    else
			al.add(ba.get());
		    h.put(ba.getID(),al);
		}
	    }
	}
	catch (NamingException exc) {
	}
	Enumeration enum = h.keys();
	while (enum !=null  && enum.hasMoreElements()) {
	    Object key = enum.nextElement();
	    ArrayList al = (ArrayList)(h.get(key));
	    int index = 0;
	    while (index<al.size()) {
		System.out.println("das ist ein attr" + (String)key + al.get(index));
		index++;
	    }
	}
	
	Vector objectAttributes = getObjectAttributes(dn);
	
	Enumeration en = h.keys();
	while (en != null && en.hasMoreElements()) {
	    Object key = en.nextElement();
	    if (objectAttributes.contains((String)key)) 
		objectAttributes.remove((String)key);
	}
	
	Enumeration er  = objectAttributes.elements();
	
	
	
	while (er!=null && er.hasMoreElements()) {
	    ArrayList al = new ArrayList();
	    al.add("");
	    h.put((String)er.nextElement(),al);
	    }
	
	
	
	return (h);
    }
    
    */

    public Vector getObjectAttributes(String dn) {
	
	Vector myNames = null;
	try {

	    DirContext ctx1 = null;
	    DirContext dc = ctx.getSchemaClassDefinition(dn);
	    
	    NamingEnumeration enum = dc.search("", null);
	    myNames = new Vector();
	    while (enum.hasMore()) {
		SearchResult si = (SearchResult)enum.next(); 
		Attributes ocAttrs = si.getAttributes();
		Attribute name = ocAttrs.get("NAME");
		System.out.println("NAME of oc is " + name);
		Attribute must = ocAttrs.get("MUST");
		

		if (must!=null) {
		    NamingEnumeration mt = must.getAll();
		    while (mt!= null && mt.hasMore()) {
			myNames.addElement((String)mt.next());
			System.out.println("must" + myNames);
		    }
		}
		Attribute may = ocAttrs.get("MAY");
		// optional attributes
		if (may !=null) {
		    NamingEnumeration my = may.getAll();
		    while (my !=null && my.hasMoreElements()) {
			myNames.addElement((String)my.next());
			System.out.println("may" + myNames);
		    }
		}
	}
	return myNames;
	}
	catch (NamingException ne) {
	    System.out.println(ne);
	}
	return myNames;
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
    

    public void add(String toAdd) {
	 
    }


    //gibt alle Attribute fuer die Liste zurueck, die must attr. objectclass haben schon ihr value zugeordnet
    
    public Hashtable getAttributes(ArrayList objectClasses) {
	
		
	Hashtable attributes = new Hashtable();
	ArrayList attrValues;
	try {
	    
	    DirContext sch =ctx.getSchema("");
	    
	    	    
	    // From object classes, get list of mandatory and optional attributes
	    Vector[] attrNames = getAttributeLists(sch, objectClasses);
	    
	    Attributes attrs = new BasicAttributes(true);
	    
	    // Remove "objectclasses" from mandatory so that we don't ask user again
	    attrNames[0].removeElement("objectclass");
	    attrValues = new ArrayList();
	    Attribute oc = new BasicAttribute("objectclass");
	    for (int i = 0; i < objectClasses.size(); i++) {
		attrValues.add(objectClasses.get(i));
		oc.add(objectClasses.get(i));
	    }
	    attrs.put(oc);
	    attributes.put("objectclass",attrValues);
	    
	    for (int i = 0;i< attrNames[0].size() ;i++) {
		String name = (String)(attrNames[0].elementAt(i));
		Attributes attrSchema = 
		sch.getAttributes("AttributeDefinition/" + name);
		Attribute syntax = attrSchema.get("SYNTAX");
		
		//die must attribute haben noch ein sternchen
		String msg;
		if (syntax != null) {
		    msg = "*" + name + "(" + syntax.get() + ")";
		} else {
		    msg = "*" + name;
		}
		attrValues  = new ArrayList();
 		attributes.put(msg,attrValues);
	    }
	    
	    for (int i = 0;i< attrNames[1].size() ;i++) {
		String name = (String)(attrNames[1].elementAt(i));
		Attributes attrSchema = 
		sch.getAttributes("AttributeDefinition/" + name);
		Attribute syntax = attrSchema.get("SYNTAX");
		
		String msg;
		if (syntax != null) {
		    msg = name + "(" + syntax.get() + ")";
		} else {
		    msg = name;
		}
		attrValues  = new ArrayList();
 		attributes.put(msg,attrValues);
	    }
	}
	catch (NamingException except) {
	    System.out.println("da fehler");
	}
	return attributes;
    }
    
   
    //gibt die liste von MUST und MAY attribute zurueck 
    private Vector[] getAttributeLists(DirContext schema,ArrayList objectClasses) 
	throws NamingException {
	
        Vector mandatory = new Vector();
	Vector optional = new Vector();

	for (int i = 0; i < objectClasses.size(); i++) {
	    String oc = (String)objectClasses.get(i);
	    Attributes ocAttrs = schema.getAttributes("ClassDefinition/" + objectClasses.get(i));
	    Attribute must = ocAttrs.get("MUST");
	    Attribute may = ocAttrs.get("MAY");
	    if (must !=null) {
		addAttrNameToList(mandatory,must.getAll()); 
	    }
	    if (may!=null) {
		addAttrNameToList(optional,may.getAll());	    
	    }
	}
	return new Vector[] {mandatory,optional};
    }

    private void addAttrNameToList(Vector store,NamingEnumeration vals) 
	throws NamingException {

	while (vals.hasMore()) {
	    Object val = vals.next();
	    if (!store.contains(val)) {
		store.addElement(val);
	    }
	}
	
    }

    public void addNewEntry(String dn, Hashtable vals) {
	try {
	System.out.println("ein neues Entry");
	Attributes attrs = getAttributes(vals);
	System.out.println(dn);
	ctx.createSubcontext(dn,attrs);
	}
	catch (NamingException e){
	}
    }
    
    private Attributes getAttributes(Hashtable vals) {
	Attributes attrs = new BasicAttributes();
	Enumeration e;
	e = vals.keys();
	while (e!=null && e.hasMoreElements()) {
	    Object key = e.nextElement();
	    Attribute attr = new BasicAttribute((String)key);
	    System.out.println("attribut" + (String)key + "values:");
	    String values = (String)vals.get(key);
	    StringTokenizer parser = new StringTokenizer(values, ",");
	    while (parser.hasMoreTokens()) {
		attr.add(parser.nextToken());
	    }
	    attrs.put(attr);
	}
	return attrs;
    }
    
    
}
