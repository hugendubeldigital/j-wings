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
	    TemplateParser parser = new TemplateParser("Button", "org.foo.bar",
						       "org.wings.SButton");
	    parser.parse(input);
	    parser.generate(new File("/tmp/plaf"));
	}
    }
    
    public static void usage() {
	System.err.println ("usage: org.wings.plaf.compiler.PlafCompiler <plaf-source> [<plaf-source>..]");
    }
}
