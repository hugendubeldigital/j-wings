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
 */

package org.wings.plaf.compiler;

import java.io.IOException;

import java.awt.Color;

import org.wings.SConstants;
import org.wings.SGetAddress;
import org.wings.io.Device;
import org.wings.SIcon;

/**
 * Utility functions to be used in generated plaf's.
 *
 * @author Henner Zeller
 * @version $Revision$
 */
public final class Utils implements SConstants {
    // fast conversion: translates directly into bytes (good for OutputStreams)
    private final static byte[] _digits      = "0123456789ABCDEF".getBytes();

    // byte representation of special characters
    private final static int HASH_CHAR       = 0;
    private final static int MINUS_CHAR      = 1;
    private final static byte[] _specialChar = "#-".getBytes();

    /**
     * This is just a collection of static functions, thus not instanciable
     */
    private Utils() {}

    /**
     * writes an {X|HT}ML quoted string according to RFC 1866.
     * '"', '<', '>', '&'  become '&quot;', '&lt;', '&gt;', '&amp;'
     */
    // not optimized yet
    public static void quote(Device d, String s) throws IOException {
	if (s == null) return;
	int len = s.length();
	char c;
	for (int pos = 0; pos < len; ++pos) {
	    switch ((c = s.charAt(pos))) {
	    case '&': d.print("&amp;"); break;
	    case '"': d.print("&quot;");break;
	    case '<': d.print("&lt;");  break;
	    case '>': d.print("&gt;");  break;
	    default: d.print(c);
	    }
	}
    }

    /**
     * writes the given String to the device.
     */
    public static void write(Device d, String s) throws IOException {
	//d.print(s);
	quote(d, s);
    }
    
    /**
     * writes the given integer to the device. Speed optimized; character
     * conversion avoided.
     */
    public static void write(Device d, int num) throws IOException {
	int i = 10;
	byte [] out = new byte[10];

	if (num < 0) {
	    d.write(_specialChar[ MINUS_CHAR ]);
	    num = -(num);
	    if (num < 0) {
		/*
		 * still negative ? Then we had Integer.MIN_VALUE
		 */
		out[--i] = _digits[ - (Integer.MIN_VALUE % 10) ];
		num = - (Integer.MIN_VALUE / 10);
	    }
	}
	do {
	    out[--i] = _digits[num % 10];
	    num /= 10;
	}
	while (num > 0);
	d.write(out, i, 10-i);
    }

    /**
     * writes the given long integer to the device. Speed optimized; character
     * conversion avoided.
     */
    public static void write(Device d, long num) throws IOException {
	int i = 20;
	byte [] out = new byte[20];

	if (num < 0) {
	    d.write(_specialChar[ MINUS_CHAR ]);
	    num = -(num);
	    if (num < 0) {
		/*
		 * still negative ? Then we had Long.MIN_VALUE
		 */
		out[--i] = _digits[ - (int) (Long.MIN_VALUE % 10) ];
		num = - (Long.MIN_VALUE / 10);
	    }
	}
	do {
	    out[--i] = _digits[(int) (num % 10) ];
	    num /= 10;
	}
	while (num > 0);
	d.write(out, i, 20-i);
    }

    /**
     * writes the given java.awt.Color to the device. Speed optimized; 
     * character conversion avoided.
     */
    public static void write(Device d, Color c) throws IOException {
	d.write(_specialChar[ HASH_CHAR ]);
	int rgb = (c == null) ? 0 : c.getRGB();
	int mask = 0xf00000;
	for (int bitPos=20; bitPos >= 0; bitPos -= 4) {
            d.write(_digits[(rgb & mask) >>> bitPos]);
            mask >>>= 4;
	}
    }

    public static void write(Device d, SGetAddress a) throws IOException {
	a.write(d);
    }
    
    /*
     * testing purposes.
     */
    public static void main(String argv[]) throws Exception {
	Color c = new Color(255, 254, 7);
	Device d = new org.wings.io.StringBufferDevice();
	write(d, c);
	quote(d, "\nThis is a <abc> string \"; foo & sons\n");
	write(d, -42);
	System.out.println (d.toString());
    }
}
