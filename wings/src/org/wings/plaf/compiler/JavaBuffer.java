/* -*- java -*-
 * $Id$
 * (c) Copyright 2001 wingS development team.
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

public class JavaBuffer {
    final String indentString;
    final StringBuffer output;
    int braceDepth;
    boolean nextIndent;
    boolean skipWhiteMode;

    public JavaBuffer(int initialDepth, String istring) {
	output = new StringBuffer();
        braceDepth = 0;
        skipWhiteMode = false;
        nextIndent = true;        // first line.
        braceDepth = initialDepth;
        indentString = istring;
    }
    
    /*
     * indent with the current depth.
     */
    private void indent() {
        if (nextIndent) {
            for (int i = 0; i < braceDepth; ++i) {
                output.append( indentString );
            }
            nextIndent = false;
        }
    }

    public JavaBuffer append(StringBuffer b) { return append(b.toString()); }

    public JavaBuffer append(String s) {
	for (int i = 0; i < s.length(); ++i) {
	    char c = s.charAt(i); 
	    switch (c) {
	    case ' ' :
	    case '\t':
		if (!skipWhiteMode) output.append(c);
		break;
	    case '\n':
		output.append(c);
                nextIndent = true;
                skipWhiteMode = true;
		break;
            case '{': if (c == '{') ++braceDepth; // fall through
            case '}': if (c == '}') --braceDepth; // fall through
	    default:
                indent();
		output.append(c);
                skipWhiteMode = false;
	    }
	}
        return this;
    }

    /**
     * removes all whitespace, that are at the end of the current buffer.
     */
    public void removeTailWhitespace() {
        int length = output.length();
        while (Character.isWhitespace(output.charAt(length-1)))
            --length;
        output.setLength(length);
    }

    /**
     * removes a newline at the end of the buffer, if any.
     */
    public void removeTailNewline() {
        int length = output.length();
        if (output.charAt(length-1) == '\n')
            output.setLength(length-1);
    }

    public String toString() { return output.toString(); }
    public int length()      { return output.length(); }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
