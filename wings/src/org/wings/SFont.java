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

package org.wings;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SFont implements SConstants
{
    private static final boolean DEBUG = true;

    /**
     * TODO: documentation
     */
    protected int type = FONT;

    /**
     * TODO: documentation
     */
    protected int style = PLAIN;

    /**
     * TODO: documentation
     */
    protected String face = null;

    /**
     * TODO: documentation
     */
    protected int size = Integer.MIN_VALUE;

    /**
     * TODO: documentation
     *
     */
    public SFont() {}

    /*
     * @parameter size if Integer.MIN_VALUE the size is ignored
     */
    public SFont(String face, int style, int size) {
        setFace(face);
        setStyle(style);
        setSize(size);
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setType(int t) {
        type = t;
    }

    /**
     * TODO: documentation
     *
     * @param f
     */
    public void setFace(String f) {
        face = f;
        if ( face!=null && face.trim().length()==0 )
            face = null;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getFace() {
        return face;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setStyle(int s) {
        style = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getStyle() {
        return style;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setSize(int s) {
        size = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void appendPrefix(Device s) {
        switch ( type ) {
        case BASEFONT:
            s.append("<BASEFONT");
            break;

        default:
            s.append("<FONT");
        }

        if ( face!=null )
            s.append(" FACE=\"").append(face).append("\"");

        if ( size>Integer.MIN_VALUE ) {
            s.append(" SIZE=");
            if ( size>0 )
                s.append("+");
            s.append(size);
        }
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void appendBody(Device s) {
        s.append(">");
        switch ( style ) {
        case ITALIC:
            s.append("<I>");
            break;

        case BOLD:
            s.append("<B>");
            break;
        }
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void appendPostfix(Device s) {
        switch ( style ) {
        case ITALIC:
            s.append("</I>");
            break;

        case BOLD:
            s.append("</B>");
            break;
        }

        switch ( type ) {
        case BASEFONT:
            s.append("</BASEFONT>");
            break;

        default:
            s.append("</FONT>");
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
