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
import java.util.Iterator;
import org.wings.template.parser.SGMLTag;

class PlafCompiler {
    final static String varPrefix = "__";

    public static void main(String argv[]) throws Exception {
	if (argv.length == 0) {
	    usage();
	    return;
	}
	for (int arg=0; arg < argv.length; ++arg) {
	    System.err.println (argv[arg]);
	    IncludingReader input = new IncludingReader(argv[arg]);
	    TemplateParser parser;
	    SGMLTag tag = new SGMLTag(input);
	    while (!tag.finished()) {
		String name = tag.getAttribute("NAME", null);
		String forClass = tag.getAttribute("FOR", null);
		if (name == null || forClass == null) {
		    throw new IOException ("'name' and 'for' as template attributes expected");
		}
		parser = new TemplateParser(name,
					    "org.wings.plaf.compiler",
					    forClass);
		parser.parse(input);
		parser.generate(new File("/tmp/plaf"));

		tag = new SGMLTag(input);
	    }
	}
    }
    
    public static void usage() {
	System.err.println ("usage: org.wings.plaf.compiler.PlafCompiler <plaf-source> [<plaf-source>..]");
    }
}
