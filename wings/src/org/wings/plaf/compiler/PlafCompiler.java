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
 * Plaf Compiler
 */

package org.wings.plaf.compiler;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.wings.template.parser.SGMLTag;

class PlafCompiler {
    final static String varPrefix = "__";

    public static void main(String argv[]) throws Exception {
        File cwd = (new File(".")).getCanonicalFile();
	if (argv.length == 0) {
	    usage();
	    return;
	}
	for (int arg=0; arg < argv.length; ++arg) {
            try {
                PlafReader input = new PlafReader(cwd, argv[arg]);
                TemplateParser parser;
                SGMLTag tag = new SGMLTag(input);
                while (!tag.finished()) {
                    String name = tag.getAttribute("NAME", null);
                    String forClass = tag.getAttribute("FOR", null);
                    if (name == null || forClass == null) {
                        throw new IOException ("'name' and 'for' as template attributes expected");
                    }
                    System.err.println ("template for " + name);
                    parser = new TemplateParser(name, cwd, new File(argv[arg]),
                                                "org.wings.plaf.css1",
                                                forClass);
                    parser.parse(input);
                    parser.generate(cwd);
                    
                    tag = new SGMLTag(input);
                }
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
            }
	}
    }
    
    public static void usage() {
	System.err.println ("usage: org.wings.plaf.compiler.PlafCompiler <plaf-source> [<plaf-source>..]");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
