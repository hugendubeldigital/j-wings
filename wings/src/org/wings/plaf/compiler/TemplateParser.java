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
import java.util.Stack;

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
 * This is a simple parser, that does not yet acknowledge that the tags
 * may occur in string-constants, so don't confuse this parser by adding them.
 */
public class TemplateParser {
    private final static String INDENT       = "    ";
    private final static String VAR_PREFIX   = "__";
    private final static int    VAR_LEN      = 16;
    
    /*
     * All tags, that are relevant for the parsing process. We are always
     * considering all of them. If we encounter a tag, that is not expected,
     * this is reported as an error. This is necessary, since the JSP like
     * syntax is not very readable and it is likely, that the user will make
     * mistakes .. thus we need to have much errorchecking.
     *
     * The tags must be ordered according to their length.
     */
    String stateTransitionTags[] = new String[] { "<%",           // 0 Start J.
                                                  "%>",           // 1 End java
                                                  "<write>",      // 2
                                                  "</write>",     // 3
                                                  "<include",     // 4
                                                  "</template" }; // 5

    // the index for the tags. Yes: c-preprocessor and enumerations would be
    // better.
    private final static int START_JAVA   = 0;
    private final static int END_JAVA     = 1;
    private final static int START_WRITE  = 2;
    private final static int END_WRITE    = 3;
    private final static int INCLUDE      = 4;
    private final static int END_TEMPLATE = 5;

    // state machine states.
    //private final static int IN_START_COMMON_JAVA = 0;
    private final static int IN_COMMON_JAVA = 1;
    private final static int IN_WRITE_JAVA  = 2;
    // everything below JAVA_STATES are states within java code.
    private final static int JAVA_STATES    = 10;

    private final static int IN_COMMON_TMPL = 11;
    private final static int IN_WRITE_TMPL  = 12;

    //-- instance variables.
    private final String templateName;
    private final String pkg;
    private final String forClassName;
    private final JavaBuffer writeJavaCode;
    private final JavaBuffer commonJavaCode;
    private final File sourcefile;
    private final File cwd;
    private final StringPool stringPool;

    private PlafReader in;
    private boolean anyError;
    private int state;

    /*
     * simple java validation. Counts open/closed braces.
     */
    private final Stack openBraces;
    private FilePosition closingBraceInTemplate;
    private FilePosition openingBraceInTemplate;

    public TemplateParser(String name, 
                          File cwd, File sourcefile,
                          String pkg, String forClass) {
	this.templateName = name;
	this.pkg = pkg;
	this.forClassName = forClass;
        this.sourcefile = sourcefile;
        this.cwd = cwd;
        this.openBraces = new Stack();
        this.anyError = false;
        writeJavaCode = new JavaBuffer(2, INDENT);
        commonJavaCode = new JavaBuffer(1, INDENT);
        stringPool = new StringPool( VAR_PREFIX, VAR_LEN );
    }

    /**
     * generates the Java-class that implements this CG. This method can
     * only be called after calling {@link #parse(PlafReader)}
     */
    public void generate(File directory) throws IOException {
        if (anyError)
            return;
        File outFile = new File(directory, templateName + ".java");
        PrintWriter out = new PrintWriter(new FileWriter(outFile));
        
        out.println ("// DO NOT EDIT! Your changes will be lost: generated from '" + sourcefile.getName() + "'");
        out.println ("package " + pkg + ";\n\n");
        //out.println ("import java.io.*;");
        out.println ("import org.wings.*;");
        out.println ("import org.wings.io.Device;\n");
        out.println ("public final class " + templateName 
                     + " implements org.wings.SConstants {");
        
        // collected HTML snippets
        out.println ("\n//--- used template snippets.");
        Iterator n = stringPool.getNames();
        while (n.hasNext()) {
            String name = (String) n.next();
            out.print (INDENT + "private final static byte[] ");
            out.print (name);
            int fillNumber = VAR_LEN - name.length();
            for (int i=0; i < fillNumber; ++i) 
                out.print(' ');
            out.print ("= ");
            out.print (stringPool.getQuotedString(name));
            out.println (".getBytes();");
        }
        
        // common stuff.
        if (commonJavaCode.length() > 0) {
            out.println ("\n//--- code from common area in template.");
            out.print( commonJavaCode.toString() );
            out.println ("\n//--- end code from common area in template.");
        }
        
        // write function header.
        out.print ("\n\n" + INDENT + "public void write("
                   + "final org.wings.io.Device device,\n" + INDENT
                   + "                  final org.wings.SComponent _c)\n"
                   + INDENT + INDENT + "throws java.io.IOException {\n");
        out.println(INDENT + INDENT
                    + "final " + forClassName + " component = ("
                    + forClassName + ") _c;");
        
        out.println ("\n//--- code from write-template.");
        // collected write stuff.
        out.print ( writeJavaCode.toString());

        out.println ("\n" + INDENT + "}  // -- end write() ");
        out.println ("}");
        out.close();
    }

    public void reportError(FilePosition pos, String msg) {
        System.err.println(pos.toString(cwd) + ": " + msg);
        anyError = true;
    }

    public void reportError(String msg) {
        System.err.println(in.getFileStackTrace() + ": " + msg);
        anyError = true;
    }

    /**
     * parses this template until &lt;/template&gt; is reached.
     *
     * @param PlafReader the Reader the source is read from.
     */
    public void parse(PlafReader reader) throws IOException {
        StringBuffer tempBuffer = new StringBuffer();
        in = reader; // for error reporting ..
        int trans;
        /*
         * we are now in the common area in java mode.
         */
        state = IN_COMMON_JAVA;
        skipWhitespace(reader);
        for (;;) {
            switch (state) {
                /*
                 * states within the common (non-write) area.
                 * The common area starts by default in JAVA-mode.
                 */
            case IN_COMMON_JAVA:   // (initial Common)
                trans = findTransitions(reader, tempBuffer, 
                                        stateTransitionTags);
                commonJavaCode.append(tempBuffer);
                tempBuffer.setLength(0);                
                switch (trans) {
                case END_JAVA:  // %>
                    state = IN_COMMON_TMPL;
                    break;
                case START_JAVA:  // ERROR <%  errornous start java
                    reportError("opening scriptlet while in scriptlet");
                    break;
                case START_WRITE:  // <write>
                    if (!openBraces.empty()) {
                        reportError("missing closed '{'.");
                        reportError((FilePosition) openBraces.pop(),
                                    ".. that has been opened here");
                            if (closingBraceInTemplate != null) {
                                reportError(closingBraceInTemplate,
                                            " .. maybe this is the missing brace ? (it is in HTML code)");
                                closingBraceInTemplate = null;
                        }
                        openBraces.clear();
                    }
                    skipWhitespace(reader);
                    state = IN_WRITE_TMPL;         // --> WRITE
                    break;
                case END_WRITE: // </write>
                    reportError("encountered </write> that has not been opened");
                    break;
                case INCLUDE: // <include
                    handleIncludeTag(reader);
                    break;
                case END_TEMPLATE:
                    return; // </template> -> done.
                default:
                    reportError("unexpected tag.");
                }
                break;

            case IN_COMMON_TMPL: 
                trans = findTransitions(reader, tempBuffer,
                                        stateTransitionTags);
                generateTemplateWriteCalls(tempBuffer, commonJavaCode);
                switch (trans) {
                case START_JAVA: // <%
                    state = IN_COMMON_JAVA;
                    break;
                case START_WRITE: // ERROR <write>  .. errorhandling
                    reportError("<write> while still reading "
                                + "text-template; '<%' missing?!");
                    state = IN_WRITE_TMPL;       // --> WRITE
                    break;
                case INCLUDE: // <include
                    handleIncludeTag(reader);
                    break;
                case END_TEMPLATE: // ERROR unecpected </template>
                    reportError("</template> while still reading "
                                + "text-template; '<%' missing?!");
                    return;
                default:
                    reportError("unexpected tag.");
                }
                break;
                
                /*
                 * states within the <write></write> area.
                 */
            case IN_WRITE_TMPL:  // (initial Write)
                trans = findTransitions(reader, tempBuffer,
                                        stateTransitionTags);
                generateTemplateWriteCalls(tempBuffer, writeJavaCode);
                // we flush the template at the
                switch (trans) {
                case START_JAVA:  // start java '<%'
                    state = IN_WRITE_JAVA; 
                    skipWhitespace(reader);
                    break;
                case END_JAVA: // end java '%>' // error
                    reportError("closing java tag in HTML code. Missing '<%' somewhere?");
                    break;
                case END_WRITE:  // end write '</write>'
                    if (!openBraces.empty()) {
                        reportError("missing closed '{'.");
                        reportError((FilePosition)openBraces.pop(),
                                    ".. that has been opened here");
                        if (closingBraceInTemplate != null) {
                            reportError(closingBraceInTemplate,
                                        ".. maybe this is the missing brace ? (it is in HTML code)");
                            closingBraceInTemplate = null;
                        }
                        openBraces.clear();
                    }
                    skipWhitespace(reader);
                    state = IN_COMMON_JAVA;      // --> COMMON
                    break;
                case INCLUDE: // <include
                    handleIncludeTag(reader);
                    break;
                case END_TEMPLATE:  // ERROR errornous '</template>'
                    reportError("</template> occured; missing </write>");
                    return;
                default:
                    reportError("unexpected tag.");
                }
                break;
                
            case IN_WRITE_JAVA:
                trans = findTransitions(reader, tempBuffer,
                                        stateTransitionTags);
                execJava(reader, tempBuffer, writeJavaCode);
                switch (trans) {
                case END_JAVA:    // end java '%>' -> start template
                    writeJavaCode.removeTailNewline();
                    state = IN_WRITE_TMPL;
                    break;
                case START_JAVA: // ERROR <%  errornous start java
                    reportError("opening scriptlet while in scriptlet");
                    break;
                case END_WRITE:  // errornous </write>
                    reportError("</write> while still reading "
                                + "java-code; '%>' missing?!");
                    generateTemplateWriteCalls(tempBuffer, writeJavaCode);
                    state = IN_COMMON_JAVA;  // --> COMMON
                case INCLUDE:   // <include
                    handleIncludeTag(reader);
                    break;
                case END_TEMPLATE: // ERROR </template>
                    reportError("</template> occured; missing </write>");
                    return;
                default:
                    reportError("unexpected tag.");
                }
            }
        }
    }
    
    private void handleIncludeTag(PlafReader reader) 
        throws IOException {
        StringBuffer includeTag = new StringBuffer();
        consumeTextUntil(reader, includeTag, ">");
        reader.read(); // consume last character.
        openIncludeFile(reader, includeTag);
    }

    private void generateTemplateWriteCalls(StringBuffer template, 
                                            JavaBuffer javaBuffer) {
        if (template.length() == 0) return;
        javaBuffer.append ("\ndevice.write(")
            .append(stringPool.getNameFor(template.toString()))
            .append(");\n");
        template.setLength(0);
    }
    
    private void openIncludeFile(PlafReader reader,
                                 StringBuffer includeTag)
        throws IOException {
        AttributeParser p = new AttributeParser(includeTag.toString());
        String filename = p.getAttribute("file");
        if (filename != null && filename.length() > 0)
            reader.open(filename);
        else
            reportError("cannot include file without name; 'file' attribute not set ?");
    }

    /**
     * Creates java source from the scriptlets within the JSP-tags. 
     * By default, it just outputs the given string as java, but there are 
     * some special modifiers like '!', '?', '@' that generates code 'around'.
     */
    private void execJava(PlafReader reader,
                         StringBuffer input, JavaBuffer output) 
        throws IOException {
        char qualifier = input.charAt(0);
        switch (qualifier) {
        case '@':
            // the code describes an include tag (we are ignoring other
            // types like 'import' for now)
            openIncludeFile(reader, input);
            break;
            
        case '=':
            input.deleteCharAt(0);
            output.append("\torg.wings.plaf.compiler.Utils.write( device, ")
                .append(input)
                .append(");\n");
            break;
            
        case '?': 
            // TODO: make introspection to find out the name of the getter.
            input.deleteCharAt(0);
            output.append("\torg.wings.plaf.compiler.Utils.write( device, ")
                .append("component.get")
                .append(capitalize(input.toString()))
                .append("());\n");
            break;

        default:
            output.append(input);
        }
        input.setLength(0);
    }
    
    /**
     * reads from the reader until any of the given strings, given in
     * the options array, occurs in the input stream. Store the text
     * read up to that position in the StringBuffer 'buffer' and return
     * the index in the options array of the found transition-tag.
     */
    public int findTransitions(PlafReader in, StringBuffer buffer, 
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
     * checks all possible transitions, starting from a reader
     * placed just at a possible match.
     * If any of the given options matches, the reader is placed after 
     * that token. This method assumes, that the options given are given 
     * in the order of their length.
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
     * consumes the text from the reader, until any of the given
     * characters in 'stopChars' occurs. Append any text found up to
     * this position in the 'consumed' StringBuffer.
     * Place reader _before_ the found character.
     */
    public StringBuffer consumeTextUntil(PlafReader r, 
                                         StringBuffer consumed,
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
            
            /*
             * do java validation. Count brace depth.
             */
            if (state < JAVA_STATES) {
                if (c == '{') {
                    openBraces.push(r.getFilePosition());
                } 
                else if (c == '}') {
                    if (openBraces.empty()) {
                        reportError("closing '}' that has not been opened.");
                        if (openingBraceInTemplate != null) {
                            reportError(openingBraceInTemplate,
                                        ".. maybe this is the missing brace ? (it is in HTML code)");
                            openingBraceInTemplate = null;
                        }
                    }
                    else
                        openBraces.pop();
                }
            }
            /*
             * ok, we are in template mode. If here are braces, this is
             * probably errronous (braces in HTML code are rare).
             * So save this location, just in case we find a missing brace:
             * we can then report this position as a hint for the user.
             */
            else {
                if (c == '{') {
                    openingBraceInTemplate = r.getFilePosition();
                } else if (c == '}') {
                    closingBraceInTemplate = r.getFilePosition();
                }
            }
            r.mark(1);
	}
	return consumed;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
