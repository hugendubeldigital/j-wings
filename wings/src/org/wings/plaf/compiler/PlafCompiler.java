/*
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
 *
 * ... experimental ...
 **** TEST TEST TEST TEST TEST TEST TEST ***
 * Plaf Compiler
 */

package org.wings.plaf.compiler;

import java.io.*;
import java.lang.*;
import java.util.*;

class PlafCompiler {
    final static String varPrefix = "__";
    final static int DECLARE    = 0;
    final static int EXECUTE    = 1;
    final static int EXPRESSION = 2;
    final static int PROPEXPR   = 3;
    final static int DIRECTIVE  = 4;

    public static void main(String argv[]) throws Exception {
	if (argv.length == 0) {
	    usage();
	    return;
	}
	for (int arg=0; arg < argv.length; ++arg) {
	    System.err.println (argv[arg]);
	    IncludingReader input = new IncludingReader(argv[arg]);
	    StringBuffer output = new StringBuffer();
	    Map stringToName = new HashMap();
	    Map nameToString = new HashMap();
	    List names = new Vector();
	    int codeType;
	    String lastFilePos = null;

	    while (input.ready()) {
		String html = consumeTextUntil(input, '<', '%').toString();
		if (!input.getFilePosition().equals(lastFilePos)) {
		    lastFilePos = input.getFilePosition();
		    //output.append ("\n\n// ---------- " + lastFilePos +" \n");
		}
		if (html.length() > 0) {
		    if (!stringToName.containsKey(html)) {
			String name = generateName(varPrefix, html);
			String uniqueName = name;
			int counter = 1;
			while (nameToString.containsKey(uniqueName)) {
			    uniqueName = name + "_" + counter;
			    ++counter;
			}
			stringToName.put(html, uniqueName);
			nameToString.put(uniqueName, html);
			// record new names in the proper sequence.
			names.add(uniqueName);
		    }
		    output.append ("\tdevice.write(")
			.append(stringToName.get(html))
			.append(");\n");
		}


		int c = input.read();
		if (c == -1) break;

		switch (c) {
		case '!':
		    codeType = DECLARE;            //
		    break;
		case '=':
		    codeType = EXPRESSION;         // normal java expression
		    break;
		case '?':
		    codeType = PROPEXPR;           // property expression
		    break;
		case '@':
		    codeType = DIRECTIVE;
		    break;
		default:
		    codeType = EXECUTE;
		    output.append((char) c);  // just give out the extra char
		}

		StringBuffer codeOut = consumeTextUntil(input, '%', '>');
		switch (codeType) {
		case DECLARE: 
		case EXECUTE: 
		    output.append(codeOut);
		    break;

		case EXPRESSION:
		    output.append("\torg.wings.plaf.compiler.Utils.write( device, ")
			.append(codeOut)
			.append(");\n");
		    break;

		case PROPEXPR:
		    output.append("\torg.wings.plaf.compiler.Utils.write( device, component.get")
			.append(capitalize(codeOut.toString()))
			.append("());\n");
		    break;
		case DIRECTIVE: {
		    AttributeParser p =new AttributeParser(codeOut.toString());
		    String filename = p.getAttribute("file");
		    output.append("// include file '" + filename + "'\n");
		    input.open(filename);
		    break;
		}
		}
	    }
	    
	    System.out.println ("import java.io.*;");
	    System.out.println ("final class foobar implements org.wings.SConstants {");
	    /*
	     * write constants in the sequence they're used
	     */
	    System.out.println ("\t/*\n\t * Constants - in sequence of occurrence\n\t */");
	    Iterator n = names.iterator();
	    while (n.hasNext()) {
		String name = (String) n.next();
		System.out.print ("\tprivate final static byte[] ");
		System.out.print (name);
		int fillNumber = 32 + varPrefix.length() - name.length();
		for (int i=0; i < fillNumber; ++i) System.out.print(' ');
		System.out.print ("= ");
		System.out.print (javaStringQuote((String)nameToString.get(name)));
		System.out.println (".getBytes();");
	    }
	    
	    System.out.print ("\n\npublic void write("
			      + "org.wings.io.Device device,"
			      +" org.wings.SComponent _c)\n"
			     + "\tthrows java.io.IOException {\n");
	    //System.out.println("\torg.wings.STextField comp = (org.wings.STextField) _c;\n");
	    System.out.println("\torg.wings.SButton comp = (org.wings.SButton) _c;\n");
	    System.out.println("\torg.wings.io.Device d = device;");
	    System.out.println (output.toString());
	    System.out.println ("}");

	    System.out.println ("}");
	}
    }

    public static void readTemplate(Reader r, String templName, 
				    String forComp) {
    }

    /**
     * consumes the text from the reader, until the two given
     * characters occur consecutively.
     */
    public static StringBuffer consumeTextUntil(Reader r, 
						char first, char second)
    throws IOException {
	StringBuffer consumed = new StringBuffer();
	int c;
	while ((c = r.read()) != -1) {
	    if (c == first) {
		c = r.read();
		if (c == second)
		    return consumed;
		else {
		    consumed.append((char) first);
		    if (c != -1)
			consumed.append((char)c);
		    else
			return consumed;
		}
	    }
	    /*
	     * ignore backslash + newline
	     */
	    else if (c == '\\') {
		c = r.read();
		if (c != '\n') {
		    consumed.append('\\');
		    if (c != -1)
			consumed.append((char)c);
		}
	    }
	    else
		consumed.append((char)c);
	}
	return consumed;
    }

    /**
     * generates a Java variable name with at most 32 characters
     * (plus prefix).
     */
    public static String generateName(String prefix, String s) {
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

    public static String javaStringQuote(String s) {
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

    public static String capitalize(String s) {
	s = s.trim();
	return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    public static void usage() {
	System.err.println ("usage: org.wings.plaf.compiler.PlafCompiler <plaf-source> [<plaf-source>..]");
    }
}
