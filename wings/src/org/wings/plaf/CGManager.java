package org.wings.plaf;

import javax.swing.*;

import org.wings.*;
import org.wings.session.*;
import org.wings.style.*;

public class CGManager
{
    LookAndFeel lookAndFeel;
    
    public CGManager() {
	try {
	    setLookAndFeel(new org.wings.plaf.xhtml.css1.CSS1LookAndFeel());
	}
	catch(Exception e) {}
    }
    
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
    
    public Style getStyle(Object key) {
	return getDefaults().getStyle(key);
    }
    
    public Icon getIcon(Object key) {
	return getDefaults().getIcon(key);
    }
    
    private CGDefaults[] tables = new CGDefaults[2];
    private CGDefaults defaults = null;
    
    CGDefaults getLookAndFeelDefaults() { return tables[0]; }
    void setLookAndFeelDefaults(CGDefaults x) { tables[0] = x; }
    
    CGDefaults getSystemDefaults() { return tables[1]; }
    void setSystemDefaults(CGDefaults x) { tables[1] = x; }
    
    public CGDefaults getDefaults() {
	if (defaults == null)
	    defaults = new MultiCGDefaults(tables);
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
	
        /**
         * Constructs an CGManager$LookAndFeelInfo object.
         *
         * @param name      a String specifying the name of the look and feel
         * @param className a String specifiying the name of the class that
         *                  implements the look and feel
         */
        public LookAndFeelInfo(String name, String className) {
            this.name = name;
            this.className = className;
        }
	
        /**
         * Returns the name of the look and feel in a form sCGtable
         * for a menu or other presentation
         * @return a String containing the name
         * @see LookAndFeel#getName
         */
        public String getName() {
            return name;
        }
	
        /**
         * Returns the name of the class that implements this look and feel.
         * @return the name of the class that implements this LookAndFeel
         * @see LookAndFeel
         */
        public String getClassName() {
            return className;
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
	    //        new LookAndFeelInfo("ECMA", "org.wings.plaf.ecma.ECMALookAndFeel")
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
     * <p>
     * This is a JavaBeans bound property.
     *
     * @param newLookAndFeel the LookAndFeel object
     * @exception UnsupportedLookAndFeelException If <code>lnf.isSupportedLookAndFeel()</code> is false.
     * @see #getLookAndFeel
     */
    public void setLookAndFeel(LookAndFeel newLookAndFeel) 
        throws UnsupportedLookAndFeelException 
    {
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
	    ((PropertyService)SessionManager.getSession()).setProperty("lookAndFeel", newLookAndFeel.getName());
    }

    
    /**
     * Set the current default look and feel using a class name.
     *
     * @param className  a string specifying the name of the class that implements
     *        the look and feel
     * @exception ClassNotFoundException If the LookAndFeel class could not be found.
     * @exception InstantiationException If a new instance of the class couldn't be creatd.
     * @exception IllegalAccessException If the class or initializer isn't accessible. 
     * @exception UnsupportedLookAndFeelException If <code>lnf.isSupportedLookAndFeel()</code> is false.
     */
    public void setLookAndFeel(String className) 
        throws ClassNotFoundException, 
               InstantiationException, 
               IllegalAccessException,
               UnsupportedLookAndFeelException 
    {
        Class lnfClass = Class.forName(className);
	setLookAndFeel((LookAndFeel)(lnfClass.newInstance()));
    }
}
