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
import java.io.EOFException;
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
    private final static int    VAR_LEN      = 16;

    // tags to look for.
    private final static String END_TEMPLATE = "</template>";
    private final static String START_WRITE  = "<write>";
    private final static String END_WRITE    = "</write>";
    private final static String START_JAVA   = "<%";
    private final static String END_JAVA     = "%>";
    private final static String INCLUDE      = "<include";

    // state machine
    //private final static int IN_START_COMMON_JAVA = 0;
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
        stringPool = new StringPool( VAR_PREFIX, VAR_LEN );
        state = IN_COMMON_JAVA;
    }

    /**
     * generates the Java-class that describes this CG.
     */
    public void generate(File base) throws IOException {
        File outFile = new File(base, templateName + ".java");
        PrintWriter out = new PrintWriter(new FileWriter(outFile));
        
        out.println ("// DO NOT EDIT! Your changes will be lost: generated file.");
        out.println ("package " + pkg + ";\n\n");
        out.println ("import java.io.*;\n");
        out.println ("public final class " + templateName + " {");
        
        // collected HTML snippets
        out.println ("\n//--- used template snippets.");
        Iterator n = stringPool.getNames();
        while (n.hasNext()) {
            String name = (String) n.next();
            out.print ("\tprivate final static byte[] ");
            out.print (name);
            int fillNumber = VAR_LEN - name.length();
            for (int i=0; i < fillNumber; ++i) 
                out.print(' ');
            out.print ("= ");
            out.print (stringPool.getQuotedString(name));
            out.println (".getBytes();");
        }
        
        // common stuff.
        if (classJavaCode.length() > 0) {
            out.println ("\n//--- code from common area in template.");
            out.print( classJavaCode.toString() );
        }
        
        // write function header.
        out.print ("\n\n    public void write("
                   + "final org.wings.io.Device device,"
                   +" final org.wings.SComponent _c)\n"
                   + "\tthrows java.io.IOException {\n");
        out.println("\tfinal " + forClassName + " component = ("
                    + forClassName + ") _c;");
        
        out.println ("\n//--- code from write-template.");
        // collected write stuff.
        out.print ( writeJavaCode.toString());

        out.println ("\n//--- end code from common area in template.");
        out.println ("\n\t}  /*** end write() ***/ ");
        out.println ("}");
        out.close();
    }

    /**
     * parses this template until </template> is reached.
     */
    public void parse(IncludingReader reader) throws IOException {
        StringBuffer tempBuffer = new StringBuffer();

        int trans;
        String commonJavaTransitions[] = new String[] { END_JAVA,
                                                        START_JAVA,      // err
                                                        START_WRITE,
                                                        END_WRITE,       // err
                                                        INCLUDE,
                                                        END_TEMPLATE };
        String commonTmplTransitions[] = new String[] { START_JAVA,  
                                                        START_WRITE,     // err
                                                        INCLUDE,
                                                        END_TEMPLATE };  // err
        String writeTmplTransitions[]  = new String[] { START_JAVA, 
                                                        END_WRITE,
                                                        INCLUDE,
                                                        END_TEMPLATE };  // err
        String writeJavaTransitions[]  = new String[] { END_JAVA,
                                                        START_JAVA,      // err
                                                        END_WRITE,       // err
                                                        INCLUDE,
                                                        END_TEMPLATE };  // err
        skipWhitespace(reader);
        for (;;) {
            switch (state) {
                /*
                 * states within the common (non-write) area.
                 */
            case IN_COMMON_JAVA:   // (initial Common)
                trans = findTransitions(reader, tempBuffer, 
                                        commonJavaTransitions);
                classJavaCode.append(tempBuffer);
                tempBuffer.setLength(0);                
                switch (trans) {
                case 0:  // %>
                    state = IN_COMMON_TMPL;
                    break;
                case 1:  // ERROR <%  errornous start java
                    System.err.println(reader.getFileStackTrace() + ": " +
                                       "opening scriptlet while in scriptlet");
                    break;
                case 2:  // <write>
                    skipWhitespace(reader);
                    state = IN_WRITE_TMPL;         // --> WRITE
                    break;
                case 3: // </write>
                    System.err.println(reader.getFileStackTrace() + ": " +
                                       "encountered </write> that has not been opened");
                    break;
                case 4: // <include
                    handleIncludeTag(reader);
                    break;
                case 5:
                    return; // </template> -> done.
                }
                break;

            case IN_COMMON_TMPL: 
                trans = findTransitions(reader, tempBuffer,
                                        commonTmplTransitions);
                flushTemplateTo(tempBuffer, classJavaCode);
                switch (trans) {
                case 0: // <%
                    state = IN_COMMON_JAVA;
                    break;
                case 1: // ERROR <write>  .. errorhandling
                    System.err.println(reader.getFileStackTrace() + ": "
                                       + "<write> while still reading "
                                       + "text-template; '<%' missing?!");
                    state = IN_WRITE_TMPL;       // --> WRITE
                    break;
                case 2: // <include
                    handleIncludeTag(reader);
                    break;
                case 3: // ERROR unecpected </template>
                    System.err.println(reader.getFileStackTrace() + ": "
                                       + "</template> while still reading "
                                       + "text-template; '<%' missing?!");
                    break;

                }
                break;
                
                /*
                 * states within the <write></write> area.
                 */
            case IN_WRITE_TMPL:  // (initial Write)
                trans = findTransitions(reader, tempBuffer,
                                        writeTmplTransitions);
                flushTemplateTo(tempBuffer, writeJavaCode);
                switch (trans) {
                case 0:  // start java '<%'
                    state = IN_WRITE_JAVA; 
                    break;
                case 1:  // end write '</write>'
                    skipWhitespace(reader);
                    state = IN_COMMON_JAVA;      // --> COMMON
                    break;
                case 2: // <include
                    handleIncludeTag(reader);
                    break;
                case 3:  // ERROR errornous '</template>'
                    System.err.println(reader.getFileStackTrace() + ": "
                                       + "</template> occured; "
                                       + "missing </write>");
                    return;
                }
                break;
                
            case IN_WRITE_JAVA:
                trans = findTransitions(reader, tempBuffer,
                                        writeJavaTransitions);
                execJava(reader, tempBuffer, writeJavaCode);
                switch (trans) {
                case 0: // end java '%>'
                    state = IN_WRITE_TMPL;
                    break;
                case 1:  // ERROR <%  errornous start java
                    System.err.println(reader.getFileStackTrace() + ": "
                                       + "opening scriptlet while in scriptlet");
                    break;
                case 2: // errornous </write>
                    System.err.println(reader.getFileStackTrace() + ": "
                                       + "</write> while still reading "
                                       + "java-code; '%>' missing?!");
                    flushTemplateTo(tempBuffer, writeJavaCode);
                    state = IN_COMMON_JAVA;  // --> COMMON
                case 3: // <include
                    handleIncludeTag(reader);
                    break;
                case 4: // ERROR </template>
                    System.err.println(reader.getFileStackTrace() + ": "
                                       + "</template> occured; "
                                       + "missing </write>");
                    return;                    
                }
            }
        }
    }
    
    private void handleIncludeTag(IncludingReader reader) 
        throws IOException {
        StringBuffer includeTag = new StringBuffer();
        consumeTextUntil(reader, includeTag, ">");
        reader.read(); // consume last character.
        openIncludeFile(reader, includeTag);
    }

    private void flushTemplateTo(StringBuffer template, 
                                 StringBuffer javaBuffer) {
        if (template.length() == 0) return;
        javaBuffer.append ("\tdevice.write(")
            .append(stringPool.getNameFor(template.toString()))
            .append(");\n");
        template.setLength(0);
    }

    public void openIncludeFile(IncludingReader reader,
                                StringBuffer includeTag)
        throws IOException {
        AttributeParser p = new AttributeParser(includeTag.toString());
        String filename = p.getAttribute("file");
        if (filename != null && filename.length() > 0)
            reader.open(filename);
        else
            System.err.println(reader.getFileStackTrace() + 
                               ": cannot include file");
    }

    public void execJava(IncludingReader reader,
                         StringBuffer input, StringBuffer output) 
        throws IOException {
        char qualifier = input.charAt(0);
        switch (qualifier) {
        case '@':
            // the code describes an include tag.
            openIncludeFile(reader, input);
            break;
            
        case '=':
            input.deleteCharAt(0);
            output.append("\torg.wings.plaf.compiler.Utils.write( device, ")
                .append(input)
                .append(");\n");
            break;
            
        case '?': 
            input.deleteCharAt(0);
            output.append("\torg.wings.plaf.compiler.Utils.write( device, ")
                .append("component.get")
                // todo: make introspection to find out the name of the getter.
                .append(capitalize(input.toString()))
                .append("());\n");
            break;

        default:
            output.append(input);
        }
        input.setLength(0);
    }
    
    public int findTransitions(Reader in, StringBuffer buffer, 
                               String[] options)
        throws IOException {
        char startChars[] = new char [ options.length ];
        for (int i = 0; i < options.length; ++i)
            startChars[i] = options[i].charAt(0);
        String startSet = new String(startChars);
        int result = -1;
        try {
            do {
                consumeTextUntil(in, buffer, startSet);
                result = checkTransitions(in, options);
                if (result == -1) // false alert; append matched char.
                    buffer.append((char) in.read());
            }
            while (result < 0);
        }
        catch (EOFException e) {
            StringBuffer errList = new StringBuffer();
            for (int i = 0; i < options.length; ++i) {
                if (i != 0)
                    errList.append(", ");
                errList.append(options);
            }
            throw new IOException ("End-of-file while expecting "
                                   + ((options.length > 1) ? "one of" : "")
                                   + errList);
        }
        return result;
    }

    /**
     * captialize a string. A String 'foo' becomes 'Foo'. Used to 
     * derive names of getters from the property name.
     */
    private  String capitalize(String s) {
	s = s.trim();
	return s.substring(0,1).toUpperCase() + s.substring(1);
    }
    
    /**
     * checks all possible transitions. If any of the given
     * options matches, the reader is placed after that token.
     * This method assumes, that the options given are given in the
     * order of their length.
     * @param in the reader to read from
     * @param options an array of all expected options, sorted by
     *                length, smallest first. Options must not
     *                start with the same prefix.
     */
    private int checkTransitions(Reader in, String[] options) 
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

    private void skipWhitespace(Reader r)
	throws IOException {
	int c;
	do {
	    r.mark(1);
	    c = r.read();
	} 
	while (c >= 0 && Character.isWhitespace((char) c));
	r.reset();
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
            r.mark(1);
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
