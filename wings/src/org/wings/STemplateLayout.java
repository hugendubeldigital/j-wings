/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import java.util.Hashtable;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;

import org.wings.template.parser.PageParser;
import org.wings.template.parser.DataSource;

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
     * TODO: documentation
     */
    public static final String COMPONENT = "COMPONENT";
    static final String INLINE = "INLINE";

    /*
     * Debug Ausgaben aktivieren.
     */
    private static final boolean DEBUG = true;

    /*
     * Dieser PropertyManager behandelt alle Properties, zu denen kein eigener
     * PropertyManager gefunden wurde.
     */
    private static final org.wings.template.PropertyManager defaultPropertyManager =
        new org.wings.template.PropertyManager() {
            final Class[] empty = new Class[0];

            public void setProperty(Object o, String name, String value) {
            }

            public Class[] getSupportedClasses() {
                return empty;
            }
        };

    /*
     * Alle Property Manager
     */
    private static final Hashtable propertyManager = new Hashtable();

    /**
     * Der PageParser. Damit der eine chance hat, die Templates
     * zu cachen, ist der static ..
     */
    private static PageParser parser;

    /*
     * Setzen von ein paar default PropertyManagern
     */
    static {
        addPropertyManager(new SComponentPropertyManager());
        addPropertyManager(new SButtonPropertyManager());
        addPropertyManager(new SLabelPropertyManager());
        addPropertyManager(new STextFieldPropertyManager());
        addPropertyManager(new STextAreaPropertyManager());
        addPropertyManager(new SBaseTablePropertyManager());
        addPropertyManager(new STablePropertyManager());
        parser = new PageParser();
        parser.addTagHandler (COMPONENT, TemplateTagHandler.class);
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

    /*
     * Sucht einen geeigneten PropertyManager fuer die angegeben
     * Klasse. Vorgehensweise ist dabei so, dass die Ableitungshierarchie herunter
     * gesucht wird.
     */
    /**
     * TODO: documentation
     */
    public static final org.wings.template.PropertyManager getPropertyManager(Class c) {
        if ( c == null )
            return defaultPropertyManager;

        org.wings.template.PropertyManager p =
            (org.wings.template.PropertyManager)propertyManager.get(c);

        if ( p == null )
            return getPropertyManager(c.getSuperclass());

        return p;
    }

    /*
     * Fuegt einen PropertyManager hinzu. Ist schon ein PropertyManager fuer eine
     * unterstuetzte Klasse vorhanden wird dieser PropertyManager fuer diese Klasse
     * nicht hinzugefuegt.
     */
    /**
     * TODO: documentation
     */
    public static final void addPropertyManager(org.wings.template.PropertyManager p) {
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
     * TODO: documentation
     *
     * @param templateFileName
     * @throws java.io.IOException
     */
    public void setTemplate(String templateFileName) throws java.io.IOException {
        setTemplate(templateFileName);
    }

    /**
     * TODO: documentation
     *
     * @param templateFile
     * @throws java.io.IOException
     */
    public void setTemplate(File templateFile) throws java.io.IOException {
        dataSource = new CachedFileDataSource(templateFile);
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
     * TODO: documentation
     *
     * @param s
     * @throws IOException
     */
    public void write(Device s)
        throws IOException
    {
        if ( dataSource == null ) {
            s.append("unable to open template-file <b>'" + dataSource + "'</b>");
            // use FlowLayout instead.
            return;
        }
        try {
            parser.process(dataSource,
                           new TemplateParseContext(s, components));
        }
        catch ( java.io.IOException e ) {
            // ignore until toString() itself throws IOExceptions
        }
    }

    /*
     * ... muss noch ausgelagert werden. Setzt die Properties unter Verwendung des
     * ... PropertyManagers.
     */
    class TemplateEntry {
        SComponent component;
        Hashtable parameter;

        TemplateEntry(SComponent c, String constr, Hashtable p) {
            component = c;
            parameter = p;

            setComponentProperties();
        }

        void setComponentProperties() {
            if ( component != null ) {
                org.wings.template.PropertyManager p = getPropertyManager(component.getClass());
                System.out.println(component.getClass() + " " + p);
                if ( parameter != null ) {
                    for ( Enumeration en=parameter.keys(); en.hasMoreElements(); ) {
                        String key = (String)en.nextElement();
                        p.setProperty(component, key, (String)parameter.get(key));
                    }
                }
            }
        }

        void write(Device s)
            throws IOException
        {
            setComponentProperties();
            if ( component != null )
                component.write(s);
        }
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
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
