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

import java.util.Hashtable;
import java.util.ArrayList;
import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.wings.template.parser.*;

import org.wings.*;
import org.wings.plaf.*;
import org.wings.io.*;

import org.wings.template.*;

/*
 * STemplateLayout.java
 *
 * Diese Layout ist dazu gedacht HTML Componenten in ein
 * vordefiniertes HTML Layout (Template) einzusetzten. In dem Template
 * sollten Schluesselwoerter (Template-Tag:
 * <COMPONENT NAME=constraint ICON="image.gif"></COMPONENT> )
 * gesetzt sein, die mit der Componente
 * ersetzt werden, die mit dem Schluesselwort als Constraint
 * zum Layouter hinzugefuegt wurden. <br>
 * Beispiel: template<br>
 * <CODE>
 * &lt;HTML&gt;&lt;BODY&gt;
 * Name der Person: &lt;COMPONENT NAME=NAME&gt;&lt;BR&gt;
 * Vorname der Person: &lt;COMPONENT NAME=VORNAME ICON="vorname.gif"&gt;&lt;BR&gt;
 * Wohnort der Person: &lt;COMPONENT NAME=WOHNORT&gt;&lt;BR&gt;
 * &lt;/BODY&gt;&lt;/HTML&gt;
 * </CODE>
 * <BR>
 * Fuellen dieses Templates schaut dann so aus:<BR>
 * <CODE>
 * templateContainer.addComponent(new SLabel("Haaf"), "NAME");
 * templateContainer.addComponent(new SButton("Armin"), "VORNAME");
 * templateContainer.addComponent(new SLabel("Neu-Ulm"), "WOHNORT");
 * </CODE>
 *
 * @author Armin Haaf, Jochen Woehrle
 * @author Henner Zeller
 */

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author Jochen Woehrle
 * @author <a href="mailto:hzeller@to.com">Henner Zeller</a>
 * @version $Revision$
 */
public class STemplateLayout
    extends SAbstractLayoutManager
{
    /**
     * @see #getCGClassID
     */
    static private final String cgClassID = "TemplateLayoutCG";

    /**
     * TODO: documentation
     */
    public static final String COMPONENT_TAG = "OBJECT";
    static final String INLINE = "INLINE";

    /*
     * Debug Ausgaben aktivieren.
     */
    private static final boolean DEBUG = true;

    /*
     * Dieser PropertyManager behandelt alle Properties, zu denen kein eigener
     * PropertyManager gefunden wurde.
     */
    private static final PropertyManager defaultPropertyManager =
        new PropertyManager() {
                final Class[] empty = new Class[0];
                
                public void setProperty(SComponent c, String name, 
                                        String value) {
                }
                
                public Class[] getSupportedClasses() {
                    return empty;
                }
            };
    
    /*
     * Alle Property Manager
     */
    private static final Hashtable propertyManager = new Hashtable();

    /*
     * some default property Managers. Sets properties of components
     * from parameters given in the template page.
     */
    static {
        addPropertyManager(new SComponentPropertyManager());
        addPropertyManager(new SButtonPropertyManager());
        addPropertyManager(new SLabelPropertyManager());
        addPropertyManager(new STextFieldPropertyManager());
        addPropertyManager(new STextAreaPropertyManager());
        addPropertyManager(new SBaseTablePropertyManager());
        addPropertyManager(new STablePropertyManager());
//        parser = new PageParser();
//        parser.addTagHandler (COMPONENT, TemplateTagHandler.class);
    }

    /**
     * the template in question ..
     */
    private DataSource dataSource = null;


    private Hashtable components = new Hashtable();

    /**
     * TODO: documentation
     *
     */
    public STemplateLayout() {
    }

    /**
     * TODO: documentation
     *
     * @param tmplFileName
     * @throws java.io.IOException
     */
    public STemplateLayout(String tmplFileName) throws java.io.IOException {
        setTemplate(new File(tmplFileName));
    }

    /**
     * TODO: documentation
     *
     * @param tmplFile
     * @return
     * @throws java.io.IOException
     */
    public STemplateLayout (File tmplFile) throws java.io.IOException {
        setTemplate(tmplFile);
    }

    /**
     * Open Template from URL. Reads the content once in a cache.
     *
     * @param tmplFile
     * @return
     * @throws java.io.IOException
     */
    public STemplateLayout (URL url) throws java.io.IOException {
        setTemplate(url);
    }

    /*
     * Sucht einen geeigneten PropertyManager fuer die angegeben
     * Klasse. Vorgehensweise ist dabei so, dass die Ableitungshierarchie herunter
     * gesucht wird.
     */
    /**
     * TODO: documentation
     */
    public static final PropertyManager getPropertyManager(Class c) {
        if ( c == null )
            return defaultPropertyManager;
        
        PropertyManager p = (PropertyManager) propertyManager.get(c);
        
        if ( p == null )
            return getPropertyManager(c.getSuperclass());
        
        return p;
    }
    
    /**
     * Adds a PropertyManager.
     * A Property Manager provides a mapping from properties given in
     * template tags to properties of components.
     * PropertyManager are responsible for a number of component classes; if
     * there is already a mapping for a certain class, then this mapping
     * is <em>not</em> added.
     */
    public static final void addPropertyManager(PropertyManager p) {
        if ( p == null )
            return;

        Class[] cl = p.getSupportedClasses();
        if ( cl == null )
            return;

        for ( int i=0; i<cl.length; i++ ) {
            if ( !propertyManager.containsKey(cl[i]) )
                propertyManager.put(cl[i], p);
        }
    }
    
    /**
     * Set the template to the template given as file name.
     *
     * @param templateFileName
     * @throws java.io.IOException
     */
    public void setTemplate(String templateFileName) 
        throws java.io.IOException {
        setTemplate(new File(templateFileName));
    }

    /**
     * Set the template to the template stored in the given file.
     *
     * @param templateFile
     * @throws java.io.IOException
     */
    public void setTemplate(File templateFile) throws java.io.IOException {
        dataSource = new CachedFileDataSource(templateFile);
    }

    /**
     * Set the template to the template which can be retrieved from the
     * given URL.
     *
     * @param templateFile
     * @throws java.io.IOException
     */
    public void setTemplate(URL templateURL) throws java.io.IOException {
        dataSource = new CachedFileDataSource(templateURL);
    }

    public void addComponent(SComponent c, Object constraint) {
        if ( constraint == null )
            throw new IllegalArgumentException("null constraint not allowed here");
        components.put(constraint.toString(), c);
    }
    
    /**
     * TODO: documentation
     *
     * @param c
     */
    public void removeComponent(SComponent c) {
        // TODO: remove value from Hashtable
    }

    /**
     *
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     *
     */
    public Hashtable getComponents() {
        return components;
    }

    public Map getLabels() {
        return PageParser.getInstance().getLabels(getDataSource());
    }
    
    // testing purposes ..
    /**
     * TODO: documentation
     */
    public static void main(String[] arg) throws Exception {
        STemplateLayout l =  new STemplateLayout("test.thtml");

        SLabel wohnort = new SLabel("Ulm");
        l.addComponent(new SLabel("Haaf"), "NAME");
        l.addComponent(new SButton("Armin"), "VORNAME");
        l.addComponent(wohnort, "WOHNORT");
        // l.removeComponent(wohnort);
        l.addComponent(new SLabel("Neu-Ulm"), "WOHNORT");

        StringBufferDevice erg = new StringBufferDevice();
        l.write(erg);
        System.out.print(erg.toString());
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setContainer(SContainer c) {
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this layout.
     *
     * @return "TemplateLayoutCG"
     * @see SLayoutManager#getCGClassID
     * @see org.wings.plaf.CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
