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
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.FilterReader;

public class IncludingReader extends Reader {
    /**
     * we cannot use a filtered reader, since that one does not
     * allow to change the value..
     */
    Reader in;
    Stack readers;
    //Map   openFiles;  // avoid recursive inclusion.

    public IncludingReader() {
        in = null;
	readers = new Stack();
	//openFiles = new HashMap();
    }

    public IncludingReader(String file) throws IOException {
	this();
	open(file);
    }

    public void open(String fileName) throws IOException {
	File f = new File(fileName);
	readers.push(in);vi
	in = new BufferedReader(new FileReader(f));
    }
    
    public void close() throws IOException {
	if (in != null) {
	    in.close();
	}
	in = (Reader) readers.pop();
    }
    
    public String getFilePosition() {
	return "FILE:LINE";
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
