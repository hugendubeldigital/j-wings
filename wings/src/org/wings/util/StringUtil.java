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

package org.wings.util;

import java.util.StringTokenizer;

/*
 * Eine Sammlung von Methoden um Strings zu bearbeiten.
 * @see ResourceManager
 */
/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class StringUtil
{
    /*
     * Erzeugt einen String, indem substrings durch andere ersetzt wurde.
     * @param s  Der String, indem ersetzt werden soll.
     * @param toFind  Der Substring, der ersetzt werden soll.
     * @param replace  Der String, mit dem ersetzt werden soll.
     * @return Das Ergebnis des Ersetzungsvorgangs.
     */
    public static final String replace(String s, String toFind, String replace) {
        StringBuffer erg = new StringBuffer();

        int lastindex = 0;
        int indexOf = s.indexOf(toFind);
        if ( indexOf == -1 ) return s;
        while ( indexOf != -1 )
        {
            erg.append(s.substring(lastindex, indexOf)).append(replace);
            lastindex = indexOf + toFind.length();
            indexOf = s.indexOf(toFind, lastindex);
        }

        erg.append(s.substring(lastindex));

        return erg.toString();
    }

    /* ist langsamer, mit jit 1/3, weniger als halb so schnell
     public static String replaceNew(String s, String toFind, String replace) {
     StringBuffer erg = new StringBuffer();

     StringTokenizer t = new StringTokenizer(s, toFind);
     while ( t.hasMoreTokens() ) {
     erg.append(t.nextToken().trim()).append(replace);
     }
     return erg.toString();
     }
     */

    /*
     * Ersetzt newlines durch String. Jede Zeile wird getrimmt, bevor der neue
     * Zeilentrenner angefÅgt wird.
     * @param s  Der Ursprungsstring.
     * @param r  Der neue Zeilentrenner.
     */
    public static final String replaceNewLines(String s, String r) {
        StringBuffer erg = new StringBuffer();

        StringTokenizer t = new StringTokenizer(s, "\n");
        while ( t.hasMoreTokens() ) {
            erg.append(t.nextToken().trim()).append(r);
        }
        return erg.toString();
    }


    public static final String[] concat(String[] s1, String[] s2) {
        String[] erg = new String[s1.length + s2.length];

        System.arraycopy(s1, 0, erg, 0, s1.length);
        System.arraycopy(s2, 0, erg, s1.length, s2.length);

        return erg;
    }

  /**
   * All possible chars for representing a number as a String
   */
  final static char[] digits = {
    '0' , '1' , '2' , '3' , '4' , '5' ,
    '6' , '7' , '8' , '9' , 'a' , 'b' ,
    'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
    'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
    'o' , 'p' , 'q' , 'r' , 's' , 't' ,
    'u' , 'v' , 'w' , 'x' , 'y' , 'z' ,
    'A' , 'B' ,
    'C' , 'D' , 'E' , 'F' , 'G' , 'H' ,
    'I' , 'J' , 'K' , 'L' , 'M' , 'N' ,
    'O' , 'P' , 'Q' , 'R' , 'S' , 'T' ,
    'U' , 'V' , 'W' , 'X' , 'Y' , 'Z'
  };


  /**
   * Geht hoch bis Radix 62 !!
   */
  public static String toString(long i, int radix) {
    char[] buf = new char[65];
    int charPos = 64;
    boolean negative = (i < 0);
    
    if (!negative) {
      i = -i;
    }
    
    while (i <= -radix) {
      buf[charPos--] = digits[(int)(-(i % radix))];
      i = i / radix;
    }
    buf[charPos] = digits[(int)(-i)];
    
    if (negative) {
      buf[--charPos] = '-';
    }
    
    return new String(buf, charPos, (65 - charPos));
  }

  /**
   * 
   */
  public static String toShortestAlphaNumericString(long i) {
    return toString(i, digits.length-1);
  }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
