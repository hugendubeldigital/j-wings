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

import java.util.Stack;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.LineNumberReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.FilterReader;

public class IncludingReader extends Reader {
    /*
     * we cannot use a filtered reader, since that one does not
     * allow to change the value of 'in'. Especially, setting it to 'null'
     * does not work.
     */
    Reader in;
    String currentFile;
    Stack  fileStack;  // Stack<filename>
    Map    openFiles;  // Map<filename,open-reader>

    public IncludingReader() {
        in = null;
        currentFile = null;
	fileStack = new Stack();
	openFiles = new HashMap();
    }

    public IncludingReader(String file) throws IOException {
	this();
	open(file);
    }

    public void open(String fileName) throws IOException {
	File f = new File(fileName);
        fileStack.push(currentFile);
        currentFile = f.getCanonicalPath();
        if (openFiles.containsKey(currentFile))
            throw new IOException ("cannot recursively include files: '"
                                   + fileName + "' at " + getFilePosition());
	in = new LineNumberReader(new BufferedReader(new FileReader(f)));
        openFiles.put(currentFile, in);
    }
    
    public void close() throws IOException {
	if (in != null) {
	    in.close();
            openFiles.remove(currentFile);
	}
        currentFile = (String) fileStack.pop();
	in = (Reader) openFiles.get(currentFile);
    }
    
    public String getFilePosition() {
        if (in == null) return "";
        return currentFile + ":" + ((LineNumberReader) in).getLineNumber();
    }
    
    public String getCurrentFile() {
        return currentFile;
    }

    public int getCurrentLineNumber() {
        return ((LineNumberReader) in).getLineNumber();
    }

    public int read()  throws IOException {
        if (in == null) return -1; // run out of files.
	int result = in.read();
	if (result == -1) {
	    close();
	    if (in == null) return -1; // run out of files.
	    return read(); // recursive retry.
	}
	return result;
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        if (in == null) return -1; // run out of files.
	int result = in.read(cbuf, off, len);
	if (result == -1) {
	    close();
	    if (in == null) return -1; // run out of files.
	    return read(cbuf, off, len); // recursive retry.
	}
	return result;
    }

    // --- implementation of the remaining methods provided by reader.
    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    public boolean ready() throws IOException {
        return in.ready();
    }

    public boolean markSupported() { return true; }

    public void mark(int readAheadLimit) throws IOException {
        in.mark(readAheadLimit);
    }
    
    public void reset() throws IOException {
        in.reset();
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
