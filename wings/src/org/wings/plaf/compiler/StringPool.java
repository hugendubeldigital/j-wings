/* -*- java -*-
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.compiler;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Vector;

/**
 * Stores a pool of strings and gives them unique identifiers
 * to be used in code generation.
 * @author Henner Zeller
 */
public class StringPool {
    private Map    stringToName;
    private Map    nameToString;
    private List   names;
    private String identPrefix;

    /**
     * 
     */
    public StringPool(String prefix) {
	stringToName = new HashMap();
	nameToString = new HashMap();
	names = new Vector();
	identPrefix = prefix;
    }

    /**
     * clears the pool.
     */
    public void clear() {
    }

    /**
     * add a String to the pool and retrieve its unique
     * identifier.
     * @param s the source string
     * @return the unique identifier.
     */
    public String getNameFor(String string) {
	String uniqueName = null;
	if (!stringToName.containsKey(string)) {
	    String identifier = generateName(identPrefix, string);
	    uniqueName = identifier;
	    int counter = 1;
	    while (nameToString.containsKey(uniqueName)) {
		uniqueName = identifier + "_" + counter;
		++counter;
	    }
	    stringToName.put(string, uniqueName);
	    nameToString.put(uniqueName, string);
	    // record new names in the proper sequence.
	    names.add(uniqueName);
	}
	else
	    uniqueName = (String) stringToName.get(string);
	return uniqueName;
    }
    
    /**
     * returns all identifiers.
     * @return iterator of identifiers.
     */
    public Iterator getNames() {
	return names.iterator();
    }

    /**
     * lookup a string by identifier, suitable quoted to be included
     * in java code.
     */
    public String getQuotedString(String name) {
	return javaStringQuote((String)nameToString.get(name));
    }

    /**
     * generates a Java variable name with at most 32 characters
     * (plus prefix).
     */
    private String generateName(String prefix, String s) {
	StringBuffer buffer = new StringBuffer();
	boolean justUnderscore = (prefix.charAt(prefix.length()-1) == '_');
	for (int i = 0; i < s.length() && buffer.length() < 32; ++i) {
	    if (Character.isJavaIdentifierPart(s.charAt(i))) {
		buffer.append(s.charAt(i));
		justUnderscore = false;
	    }
	    else if (!justUnderscore) {
		buffer.append('_');
		justUnderscore = true;
	    }
	}
	if (justUnderscore)
	    buffer.setLength(buffer.length() > 0 ? buffer.length()-1 : 0 );
	return prefix + buffer.toString();
    }

    private String javaStringQuote(String s) {
	StringBuffer buffer = new StringBuffer();
	for (int i = 0; i < s.length(); ++i) {
	    switch (s.charAt(i)) {
	    case '\t': buffer.append ("\\t"); break;
	    case '\r': buffer.append ("\\r"); break;
	    case '\n': buffer.append ("\\n"); break;
	    case '"': buffer.append ("\\\""); break;
	    default: buffer.append(s.charAt(i));
	    }
	}
	return "\"" + buffer.toString() + "\""; // TODO
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
