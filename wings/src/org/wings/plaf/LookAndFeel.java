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

package org.wings.plaf;

import java.io.*;
import javax.servlet.ServletOutputStream;
import javax.swing.ImageIcon;

import org.wings.*;
import org.wings.io.*;

public abstract class LookAndFeel
{
    /**
     * Return a short string that identifies this look and feel, e.g.
     * "XHTML".
     */
    public abstract String getName();

    /**
     * Return a one line description of this look and feel implementation,
     * e.g. "XHTML Look and Feel".
     */
    public abstract String getDescription();

    /**
     * CGManager.setLookAndFeel calls this method before the first
     * call (and typically the only call) to getDefaults().
     *
     * @see #uninitialize
     * @see CGManager#setLookAndFeel
     */
    public void initialize() {
    }

    /**
     * CGManager.setLookAndFeel calls this method just before we're
     * replaced by a new default look and feel.   Subclasses may
     * choose to free up some resources here.
     *
     * @see #initialize
     */
    public void uninitialize() {
    }

    /**
     * This method is called once by CGManager.setLookAndFeel to create
     * the look and feel specific defaults table.  Other applications,
     * for example an application builder, may also call this method.
     *
     * @see #initialize
     * @see #uninitialize
     * @see CGManager#setLookAndFeel
     */
    public CGDefaults getDefaults() {
        return null;
    }

    /**
     * Return an appropriate Device for code generation.
     * Some lafs can deal with a stream, others rely on a buffered
     * Device, because they produce code that must appear in the header.
     *
     * @return a Device that is suitable for this laf
     */
    public abstract Device createDevice(ServletOutputStream stream);

    /**
     * Utility method that creates an ImageIcon from a resource
     * located realtive to the given base class.
     * @param baseClass
     * @param fileName of gif file
     * @return image icon
     */
    public static ImageIcon makeIcon(Class baseClass, String fileName) {
        InputStream resource = null;
        try {
            resource = baseClass.getResourceAsStream(fileName);
            if (resource == null) // not found
                return null;

            byte[] buffer = new byte[resource.available()];
            resource.read(buffer);

            if (buffer.length <= 1) {  //workaround for windows
                //        System.err.println(fileName + " is empty");
                return new ImageIcon(baseClass.getResource(fileName));
            }
            return new ImageIcon(buffer);
        }
        catch (IOException ioe) {
            System.err.println(ioe.toString());
            return null;
        }
        finally {
            try {
                resource.close();
            }
            catch(Exception e) {} // ignore
        }
    }

    /**
     * Returns a string that displays and identifies this
     * object's properties.
     *
     * @return a String representation of this object
     */
    public String toString() {
        return "[" + getDescription() + " - " + getClass().getName() + "]";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
