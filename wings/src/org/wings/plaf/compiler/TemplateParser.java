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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Iterator;

/**
 * parses a template for a PlafCG. A Template has the
 * form
 *   <template name="ButtonCG" for="org.wings.SButton">
 *   // common part, _within_ the class, outside the <write> function.
 *   <write>
 *     // write method. 'device' and 'component' are available.
 *   </write>
 *   </template>
 * <%@ import, include %> are supported.
 */
public class TemplateParser {
    private final static String VAR_PREFIX   = "__";

    // tags to look for.
    private final static String END_TEMPLATE = "</template>";
    private final static String START_WRITE  = "<write>";
    private final static String END_WRITE    = "</write>";
    private final static String START_JAVA   = "<%";
    private final static String END_JAVA     = "%>";
    private final static int MAX_LOOKAHEAD   = END_TEMPLATE.length();

    // state machine
    private final static int IN_START_COMMON_JAVA = 0;
    private final static int IN_COMMON_JAVA = 1;
    private final static int IN_COMMON_TMPL = 2;
    private final static int IN_WRITE_JAVA  = 3;
    private final static int IN_WRITE_TMPL  = 4;

    private String templateName;
    private String pkg;
    private String forClassName;
    private StringBuffer writeJavaCode;
    private StringBuffer classJavaCode;
    private int state;
    private StringPool stringPool;

    public TemplateParser(String name, String pkg, String forClass) {
	this.templateName = name;
	this.pkg = pkg;
	this.forClassName = forClass;
        writeJavaCode = new StringBuffer();
        classJavaCode = new StringBuffer();
        stringPool = new StringPool( VAR_PREFIX );
        state = IN_START_COMMON_JAVA;
    }

    /**
     * generates the Java-class that describes this CG.
     */
    public void generate(File base) throws IOException {
        File outFile = new File(base, templateName + ".java");
        PrintWriter out = new PrintWriter(new FileWriter(outFile));
        
        out.println ("// DO NOT EDIT your changes will be lost: generated file.");
        out.println ("package " + pkg + ";\n\n");
        out.println ("import java.io.*;\n");
        out.println ("public final class " + templateName + " {");
        
        // collected HTML snippets
        Iterator n = stringPool.getNames();
        while (n.hasNext()) {
            String name = (String) n.next();
            out.print ("\tprivate final static byte[] ");
            out.print (name);
            int fillNumber = 32 + VAR_PREFIX.length() - name.length();
            for (int i=0; i < fillNumber; ++i) 
                out.print(' ');
            out.print ("= ");
            out.print (stringPool.getQuotedString(name));
            out.println (".getBytes();");
        }
        
        // common stuff.
        out.print( classJavaCode.toString() );
        
        // write function header.
        out.print ("\n\n    public void write("
                   + "org.wings.io.Device device,"
                   +" org.wings.SComponent _c)\n"
                   + "\tthrows java.io.IOException {\n");
        out.println("\t" + forClassName + " component = ("
                    + forClassName + ") _c;");

        // collected write stuff.
        out.print ( writeJavaCode.toString());

        out.println ("\n\t}  /*** end write() ***/ ");
        out.println ("}");
        out.close();
    }

    /**
     * parses this template until </template> is reached.
     */
    public void parse(IncludingReader reader) throws IOException {
        StringBuffer tempBuffer = new StringBuffer();
        String commonTransitions[] = new String[] { END_JAVA, START_WRITE,
                                                    END_TEMPLATE };
        String commonTmplTransitions[] = new String[] { START_JAVA,
                                                        START_WRITE };
        String writeTransitions[]  = new String[] { START_JAVA, END_WRITE };
        for (;;) {
            switch (state) {
            case IN_START_COMMON_JAVA:
                consumeTextUntil(reader, tempBuffer, "%<");
                switch (checkTransitions(reader, commonTransitions)) {
                case 0:  // %>
                    classJavaCode.append(tempBuffer);
                    tempBuffer.setLength(0);
                    state = IN_COMMON_TMPL;
                    break;
                case 1:  // <write>
                    classJavaCode.append(tempBuffer);
                    tempBuffer.setLength(0);
                    state = IN_WRITE_TMPL; 
                    break;
                case 2:
                    return; // </template> -> done.
                default:  // false alarm.
                    tempBuffer.append((char) reader.read());
                }
                break;
                
            case IN_COMMON_TMPL:
                consumeTextUntil(reader, tempBuffer, "<");
                switch (checkTransitions(reader, commonTmplTransitions)) {
                case 0: // <%
                    flushTemplateTo(tempBuffer, classJavaCode);
                    state = IN_COMMON_JAVA;
                    break;
                case 1: // <write>  .. errorhandling
                    System.err.println(reader.getFilePosition() + ":"
                                       + "<write> while common section in "
                                       + "HTML mode; '<%' missing!");
                    flushTemplateTo(tempBuffer, classJavaCode);
                    state = IN_WRITE_TMPL;
                    break;
                default: // false alarm.
                    tempBuffer.append((char) reader.read());
                    break;
                }
                break;

            case IN_WRITE_TMPL:
                consumeTextUntil(reader, tempBuffer, "<"); // <%, </write>
                switch (checkTransitions(reader, writeTransitions)) {
                case 0:  // start java '<%'
                    flushTemplateTo(tempBuffer, writeJavaCode);
                    state = IN_WRITE_JAVA; 
                    break;
                case 1:  // end write '</write>'
                    flushTemplateTo(tempBuffer, writeJavaCode);
                    state = IN_COMMON_TMPL; 
                    break;
                default: 
                    tempBuffer.append((char) reader.read());
                }
                break;
                                
            case IN_WRITE_JAVA:
                consumeJava(reader, writeJavaCode);
                state = IN_WRITE_TMPL;
                break;

            case IN_COMMON_JAVA:
                consumeJava(reader, classJavaCode);
                state = IN_COMMON_TMPL;
                break;
            }
        }
    }

    void flushTemplateTo(StringBuffer template, StringBuffer javaBuffer) {
        if (template.length() == 0) return;
        javaBuffer.append ("\tdevice.write(")
            .append(stringPool.getNameFor(template.toString()))
            .append(");\n");
        template.setLength(0);
    }

    public void consumeJava(Reader in, StringBuffer output) 
        throws IOException {
        int qualifier = in.read();
        StringBuffer codeBuffer;
        switch (qualifier) {
        case '@': case '=': case '?':
            codeBuffer = new StringBuffer(); 
            break;
        case '!':
            codeBuffer = output;
            break;
        default:
            codeBuffer = output;
            codeBuffer.append((char) qualifier);
            break;
        }

        consumeTextUntil(in, codeBuffer, "%<"); // %>, <write>
        switch (qualifier) {
        case '@':
        case '=':
        case '?': 
            output.append ("// later...'" + qualifier + "'\n");
        }
    }

    /*
    public static String capitalize(String s) {
	s = s.trim();
	return s.substring(0,1).toUpperCase() + s.substring(1);
    }

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
    */
    
    /**
     * checks all possible transitions.
     * @param in the reader to read from
     * @param options an array of all expected options, sorted by
     *                length, smallest first. Options must not
     *                start with the same prefix.
     */
    public int checkTransitions(Reader in, String[] options) 
        throws IOException {
        int pos = 0;
        int maxLength = options[options.length-1].length();
        char cbuff[] = new char[ maxLength ];
        in.mark( maxLength );
        for (int i = 0; i < options.length; ++i) {
            String currentOption = options[i];
            int readLen = currentOption.length() - pos;
            int read;
            while ((read = in.read(cbuff, pos, readLen)) > 0) {
                pos += read;
                readLen -= read;
            }
            if (match(currentOption, cbuff))
                return i;
        }
        in.reset();
        return -1; // no matches.
    }
    
    private boolean match(String str, char cbuff[]) {
        int len = str.length();
        int i;
        for (i = 0; i < len; ++i) {
            if (str.charAt(i) != cbuff[i])
                return false;
        }
        return (i == len);
    }

    /**
     * consumes the text from the reader, until the two given
     * characters occur consecutively.
     * Place reader _before_ this character.
     */
    public StringBuffer consumeTextUntil(Reader r, StringBuffer consumed,
					 String stopChars)
	throws IOException {
	int c;
        r.mark(1);
	while ((c = r.read()) != -1) {
            if (stopChars.indexOf((char) c) != -1) {
                r.reset();
                return consumed;
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
     * consumes the text from the reader, until the two given
     * characters occur consecutively (todo: implement with consumeTextUntil())
     * Place reader _before_ these characters.
     */
    public StringBuffer consumeTextUntil(Reader r, StringBuffer consumed,
					 char first, char second)
	throws IOException {
	int c;
        r.mark(2);
	while ((c = r.read()) != -1) {
	    if (c == first) {
		c = r.read();
		if (c == second) {
                    r.reset();
		    return consumed;
                }
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
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
