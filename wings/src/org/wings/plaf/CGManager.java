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

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

import org.wings.*;
import org.wings.session.*;
import org.wings.style.*;

public class CGManager
{
    private LookAndFeel lookAndFeel;

    public ComponentCG getCG(SComponent target) {
        return getDefaults().getCG(target);
    }

    public LayoutCG getCG(SLayoutManager target) {
        return getDefaults().getCG(target);
    }

    public BorderCG getCG(SBorder target) {
        return getDefaults().getCG(target);
    }

    /**
     * Returns an object from the defaults table.
     *
     * @param key  an Object specifying the desired object
     * @return the Object
     */
    public Object get(Object key) {
        return getDefaults().get(key);
    }

    public SFont getFont(Object key) {
        return getDefaults().getFont(key);
    }

    public Color getColor(Object key) {
        return getDefaults().getColor(key);
    }

    public Style getStyle(Object key) {
        return getDefaults().getStyle(key);
    }

    public StyleSheet getStyleSheet(Object key) {
        return getDefaults().getStyleSheet(key);
    }

    public Icon getIcon(Object key) {
        return getDefaults().getIcon(key);
    }

    public Object getObject(Object key, Class clazz) {
        return getDefaults().getObject(key, clazz);
    }

    private CGDefaults defaults = null;

    public void setLookAndFeelDefaults(CGDefaults defaults) {
        this.defaults = defaults;
    }
    public CGDefaults getLookAndFeelDefaults() { return defaults; }

    public CGDefaults getDefaults() {
        return defaults;
    }

    /**
     * Provide a little information about an installed LookAndFeel
     * for the sake of configuring a menu or for initial application
     * set up.
     *
     * @see CGManager#getInstalledLookAndFeels
     * @see LookAndFeel
     */
    public class LookAndFeelInfo {
        private String name;
        private String className;
        private ClassLoader classLoader;

        /**
         * Constructs an CGManager$LookAndFeelInfo object.
         *
         * @param name      a String specifying the name of the look and feel
         * @param className a String specifiying the name of the class that
         *                  implements the look and feel
         */
        public LookAndFeelInfo(String name, String className, ClassLoader classLoader) {
            this.name = name;
            this.className = className;
            this.classLoader = classLoader;
        }

        public LookAndFeelInfo(String name, String className) {
            this.name = name;
            this.className = className;
            this.classLoader = this.getClass().getClassLoader();
        }

        public String getName() {
            return name;
        }

        public String getClassName() {
            return className;
        }

        public ClassLoader getClassLoader() {
            return classLoader;
        }

        /**
         * Returns a string that displays and identifies this
         * object's properties.
         *
         * @return a String representation of this object
         */
        public String toString() {
            return this.getClass().getName() + "[" + getName() + " " + getClassName() + "]";
        }
    }


    /**
     * The default value of installedLAFS is used when no swing.properties
     * file is available or if the file doesn't contain a "swing.installedlafs"
     * property.
     *
     * @see #initializeInstalledLAFs
     */
    private LookAndFeelInfo[] installedLAFs = {
        new LookAndFeelInfo("XHTML", "org.wings.plaf.xhtml.XHTMLLookAndFeel")
            // new LookAndFeelInfo("ECMA", "org.wings.plaf.ecma.ECMALookAndFeel")
            };


    /**
     * Return an array of objects that provide some information about the
     * LookAndFeel implementations that have been installed with this
     * java development kit.  The LookAndFeel info objects can be used
     * by an application to construct a menu of look and feel options for
     * the user or to set the look and feel at start up time.  Note that
     * we do not return the LookAndFeel classes themselves here to avoid the
     * cost of unnecessarily loading them.
     * <p>
     * Given a LookAndFeelInfo object one can set the current look and feel
     * like this:
     * <pre>
     * CGManager.setLookAndFeel(info.getClassName());
     * </pre>
     *
     * @see #setLookAndFeel
     */
    public LookAndFeelInfo[] getInstalledLookAndFeels() {
        LookAndFeelInfo[] ilafs = installedLAFs;
        LookAndFeelInfo[] rv = new LookAndFeelInfo[ilafs.length];
        System.arraycopy(ilafs, 0, rv, 0, ilafs.length);
        return rv;
    }


    /**
     * Replaces the current array of installed LookAndFeelInfos.
     *
     * @see #getInstalledLookAndFeels
     */
    public void setInstalledLookAndFeels(LookAndFeelInfo[] infos)
        throws SecurityException
    {
        LookAndFeelInfo[] newInfos = new LookAndFeelInfo[infos.length];
        System.arraycopy(infos, 0, newInfos, 0, infos.length);
        installedLAFs = newInfos;
    }

    /**
     * Adds the specified look and feel to the current array and
     * then calls {@link #setInstalledLookAndFeels}.
     * @param info a LookAndFeelInfo object that names the look and feel
     *        and identifies that class that implements it
     */
    public void installLookAndFeel(LookAndFeelInfo info) {
        LookAndFeelInfo[] infos = getInstalledLookAndFeels();
        LookAndFeelInfo[] newInfos = new LookAndFeelInfo[infos.length + 1];
        System.arraycopy(infos, 0, newInfos, 0, infos.length);
        newInfos[infos.length] = info;
        setInstalledLookAndFeels(newInfos);
    }

    /**
     * Creates a new look and feel and adds it to the current array.
     * Then calls {@link #setInstalledLookAndFeels}.
     *
     * @param name       a String specifying the name of the look and feel
     * @param className  a String specifying the class name that implements the
     *                   look and feel
     */
    public void installLookAndFeel(String name, String className) {
        installLookAndFeel(new LookAndFeelInfo(name, className));
    }


    /**
     * Returns The current default look and feel, or null.
     *
     * @return The current default look and feel, or null.
     * @see #setLookAndFeel
     */
    public LookAndFeel getLookAndFeel() {
        return lookAndFeel;
    }


    /**
     * Set the current default look and feel using a LookAndFeel object.
     * If not explicitely specified, the lookAndFeel will use the context
     * ClassLoader.
     * <p>
     * This is a JavaBeans bound property.
     *
     * @param newLookAndFeel the LookAndFeel object
     * @see #getLookAndFeel
     */
    public void setLookAndFeel(LookAndFeel newLookAndFeel) {
        LookAndFeel oldLookAndFeel = lookAndFeel;
        if (oldLookAndFeel != null) {
            oldLookAndFeel.uninitialize();
        }

        lookAndFeel = newLookAndFeel;
        if (newLookAndFeel != null) {
            newLookAndFeel.initialize();
            setLookAndFeelDefaults(newLookAndFeel.getDefaults());
        }
        else {
            setLookAndFeelDefaults(null);
        }

        // have the session fire a propertyChangeEvent regarding the new lookAndFeel
        if (SessionManager.getSession() != null)
            ((PropertyService)SessionManager.getSession())
                .setProperty("lookAndFeel", newLookAndFeel.getName());
    }


    /**
     * Set the current default look and feel using a class name.
     *
     * @param className  a string specifying the name of the class that implements
     *        the look and feel
     * @exception ClassNotFoundException If the LookAndFeel class could not be found.
     * @exception InstantiationException If a new instance of the class couldn't be creatd.
     * @exception IllegalAccessException If the class or initializer isn't accessible.
     * @deprecated provide an URL instead
     */
    public void setLookAndFeel(String className)
        throws ClassNotFoundException, InstantiationException,
               IllegalAccessException
    {
        Class lnfClass = Class.forName(className);
        setLookAndFeel((LookAndFeel)(lnfClass.newInstance()));
    }

    /**
     * Set the current LookAndFeel
     */
    public void setLookAndFeel(URL classpath)
        throws IOException
    {
        ClassLoader classLoader = new URLClassLoader(new URL[] { classpath },
                                                     getClass().getClassLoader());
        LookAndFeel lookAndFeel = new LookAndFeel(classLoader);

        ClassLoader loader = classLoader;
        System.err.println("cl: " + loader);
        while ((loader = loader.getParent()) != null)
            System.err.println("cl: " + loader);

        if (!hasInstalled(lookAndFeel))
            installLookAndFeel(new LookAndFeelInfo(lookAndFeel.getName(),
                                                   lookAndFeel.getClass().getName(),
                                                   classLoader));
        setLookAndFeel(lookAndFeel);
    }

    public boolean hasInstalled(LookAndFeel lookAndFeel) {
        LookAndFeelInfo[] infos = getInstalledLookAndFeels();
        for (int i=0; i < infos.length; i++)
            if (infos[i].getClassName().equals(lookAndFeel.getClass().getName()) &&
                infos[i].getName().equals(lookAndFeel.getName()))
                return true;

        return false;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
