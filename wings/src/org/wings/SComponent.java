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

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Locale;
import java.util.Hashtable;
import java.util.Dictionary;

import org.wings.io.Device;
import org.wings.io.StringBufferDevice;
import org.wings.plaf.*;
import org.wings.plaf.ComponentCG;
import org.wings.session.Session;
import org.wings.session.SessionManager;
import org.wings.style.Style;
import org.wings.externalizer.ExternalizeManager;

/*
 * SComponent.java
 *
 * Basiskomponente des HTML Packages. Vergleichbar zu
 * java.awt.Component. Alle HTML Komponenten werden hiervon
 * abgeleitet. Diese Komponente fuehrt die typische Erzeugung von HTML
 * Code ein:
 * <UL>
 * <LI> Erzeugen des Border Prefixes - typischerweise ein Table
 * <LI> Erzeugen eines Prefixes - typischerweise das Oeffnungs Tag mit
 * seinen Attribute
 * <LI> Erzeugen des Body - der Inhalt einer HTML Komponente, z.B. die
 * Listenelemente
 * <LI> Erzeugen des Postfixes - typischerweise des Schliessen Tag.
 * <LI> Erzeugen des Border Postfixes - typischerweise das Schlusstag
 * eines Tables
 * </UL>
 * <P>
 * Eine davon abgeleitete Componente sollte die entsprechenden
 * Methoden ueberschreiben und zu Beginn der Methode (bei Prefix und
 * Body) bzw. am Ende (bei Postfix) die Methode der Super Klasse
 * aufrufen.
 */
/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class SComponent
    implements SConstants, Cloneable
{
    private static final boolean DEBUG = false;

    /*
     * Hieraus wird die eindeutige Id einer jeden Komponente pro VM
     * generiert. Diese wird benoetigt um eindeutige Form Namen erzeugen
     * zu koennen.
     */
    private static int UNIFIED_ID = 0;

    /*
     * Hieraus wird die eindeutige Id einer jeden Komponente pro VM
     * generiert. Diese wird benoetigt um cache Probleme zu umgehen.
     * TO CHANGE: Um reload Probleme zu umgehen, sollte immer nur eine
     * Menge von Prefixen Gueltigkeit haben, so dass vorneweg schon
     * gefiltert werden kann.
     */
    private static int UNIFIED_PREFIX = 0;

    /*
     * Die eindeutige Id der Komponente.
     */
    private transient final int unifiedId = createUnifiedId();

    /*
     * Performance!!!
     */
    private transient String unifiedIdString = null;

    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "ComponentCG";

    /**
     * TODO: documentation
     */
    protected transient ComponentCG cg;

    /*
     * Die Hintergrundfarbe der Komponente. Wird derzeit ignoriert
     */
    protected Color background;

    /*
     * Die Vordergrundfarbe der Komponente. Wird mit dem COLOR Tag
     * gesetzt falls ungleich null.
     */
    protected Color foreground;

    /**
     * TODO: documentation
     */
    protected int verticalAlignment = NO_ALIGN;

    /**
     * TODO: documentation
     */
    protected int horizontalAlignment = NO_ALIGN;

    /**
     * TODO: documentation
     */
    protected int colSpan = 0;

    /**
     * TODO: documentation
     */
    protected int rowSpan = 0;

    /** the style */
    protected Style style = null;

    protected SFont font;

    /**
     * TODO: documentation
     */
    protected boolean visible = true;

    /**
     * TODO: documentation
     */
    protected boolean enabled = true;

    /**
     * TODO: documentation
     */
    protected SContainer parent = null;

    /**
     * TODO: documentation
     */
    protected SFrame parentFrame = null;

    /**
     * TODO: documentation
     */
    protected String name = null;

    /**
     * TODO: documentation
     */
    protected SBorder border = null;

    /**
     * TODO: documentation
     */
    protected String tooltip = null;

    /**
     * TODO: documentation
     *
     */
    public SComponent() {
        updateCG();
    }

    /**
     * TODO: documentation
     */
    public final SBorder getBorder() {
        return border;
    }

    /**
     * TODO: documentation
     */
    public final void setBorder(SBorder b) {
        border = b;
    }

    /**
     * TODO: documentation
     */
    public final SContainer getParent() {
        return parent;
    }

    /**
     * TODO: documentation
     *
     * @param p
     */
    public void setParent(SContainer p) {
        parent = p;
        if ( p!=null )
            setParentFrame(p.getParentFrame());
        else
            setParentFrame(null);
    }

    /**
     * TODO: documentation
     *
     * @param f
     */
    protected void setParentFrame(SFrame f) {
        if ( f!=parentFrame ) {
            unregister();
            parentFrame = f;
            register();
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SGetAddress getServerAddress() {
        SFrame p = getParentFrame(this);
        if ( p!=null )
            return p.getServerAddress();
        return new SGetAddress();
    }

    /**
     * TODO: documentation
     */
    public static final synchronized int createUnifiedId() {
        return UNIFIED_ID++;
    }

    /**
     * TODO: documentation
     *
     */
    protected void addedToFrame() {
    }


    /*
     * Eine in der virtuellen Maschine eindeutige Id.
     */
    /**
     * TODO: documentation
     */
    public final int getUnifiedId() {
        return unifiedId;
    }

    /*
     * Eine in der virtuellen Maschine eindeutige Id. Bringt
     * Performance.
     */
    /**
     * TODO: documentation
     */
    public final String getUnifiedIdString() {
        if ( unifiedIdString==null )
            unifiedIdString = Integer.toString(unifiedId);
        return unifiedIdString;
    }

    /*
     * Eine in der virtuellen Maschine eindeutige Id.
     */
    /**
     * TODO: documentation
     */
    public static final synchronized int getUnifiedPrefix() {
        return UNIFIED_PREFIX++;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Session getSession() {
        if ( parentFrame==null )
            return SessionManager.getSession();
        else
            return parentFrame.getSession();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SGetDispatcher getDispatcher() {
        if ( parentFrame==null )
            return null;
        else
            return parentFrame.getDispatcher();
    }

    /*
     * Setzt das Locale.
     * @param Falls l null ist, wird das default Locale gesetzt.
     */
    /**
     * TODO: documentation
     *
     * @param l
     */
    public void setLocale(Locale l) {
        getSession().setLocale(l);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Locale getLocale() {
        return getSession().getLocale();
    }

    /*
     * Deregistiert Komponente beim dem gesetzten Dispatcher, falls
     * Komponente das Interface {@link SGetListener} implementiert.
     */
    private void unregister() {
        if ( getDispatcher()!=null && this instanceof SGetListener )
            getDispatcher().unregister((SGetListener)this);
    }

    /*
     * Registiert Komponente beim dem gesetzten Dispatcher, falls
     * Komponente das Interface {@link SGetListener} implementiert.
     */
    private void register() {
        if ( getDispatcher()!=null && this instanceof SGetListener )
            getDispatcher().register((SGetListener)this);
    }

    /*
     * Deregistiert Komponente
     */
    /**
     * TODO: documentation
     *
     */
    protected void finalize() {
        if ( DEBUG )
            System.out.println("finalize " + getClass().getName());
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setStyle(Style s) {
        style = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Style getStyle() {
        return style;
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setBackground(Color c) {
        background = c;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Color getBackground() {
        return background;
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setForeground(Color c) {
        foreground = c;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Color getForeground() {
        return foreground;
    }

    /*
     * Der Font der Komponente. Wird mit dem Font Tag gesetzt. Die
     * Groesse des Java Fonts wird durch 4 geteilt und dieser Wert
     * gesetzt. Hier sollte ein besserer Algorithmus gwaehlt werden.
     */
    /**
     * TODO: documentation
     *
     * @param f
     */
    public void setFont(Font f) {
        if ( f==null ) {
            setFont((SFont)null);
            return;
        }

        SFont font = new SFont();

        font.setFace(f.getName());
        font.setStyle(f.getStyle());
        font.setSize(f.getSize()-10);

        setFont(font);
    }

    /**
     * TODO: documentation
     *
     * @param f
     */
    public void setFont(SFont f) {
        font = f;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SFont getFont() {
        return font;
    }

    /**
     * TODO: documentation
     *
     * @param v
     */
    public void setVisible(boolean v) {
        visible = v;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean getVisible() {
        return visible;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * TODO: documentation
     *
     * @param v
     */
    public void setEnabled(boolean v) {
        enabled = v;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * TODO: documentation
     *
     * @param n
     */
    public void setName(String n) {
        name = n;
    }



    /**
     * TODO: documentation
     *
     * @param s
     */
    public void appendPrefix(Device s) {
        if ( font!=null )
            font.appendPrefix(s);
        else if ( foreground!=null || background!=null )
            s.append("<FONT");

        if ( foreground!=null ) {
            s.append(" COLOR=#").append(SUtil.toColorString(foreground));
        }

        // hab keine Idee, wie man das sonst machen kann !!!
        // Mit Table wuerds funktionieren (siehe unten), aber...
        if ( background!=null )
            s.append(" STYLE=\"background-color:#").append(SUtil.toColorString(background)).append(";\"");


        if ( font!=null )
            font.appendBody(s);
        else if ( foreground!=null || background!=null )
            s.append(">");

        //    if ( background!=null )
        //      s.append("<TABLE BGCOLOR=#").append(SUtil.toColorString(background)).append(">");
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void appendPostfix(Device s) {
        //    if ( background!=null )
        //      s.append("</TABLE>");
        if ( font!=null )
            font.appendPostfix(s);
        else if ( foreground!=null || background!=null )
            s.append("</FONT>");

    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void appendBody(Device s) {
        //    s.append("&nbsp;");
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void appendBorderPrefix(Device s) {
        if ( border!=null )
            border.appendPrefix(s);
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void appendBorderPostfix(Device s) {
        if ( border!=null )
            border.appendPostfix(s);
    }


    /*
     * Haengt die HTML Ausgabe dieser Komponente an den Puffer an.
     * Die Vorgehensweise ist die folgende:
     * <OL>
     * <LI> Falls Komponente sichtbar ist
     * <LI> Haenge BorderPrefix an Puffer an
     * <LI> Haenge Prefix an Puffer an
     * <LI> Haenge Body an Puffer an
     * <LI> Haenge Postfix an Puffer an
     * <LI> Haenge BorderPostfix an Puffer an
     * </OL>
     * @param s the Device to write to
     * @exception IOException may be the user stopped loading ..
     */
    /**
     * TODO: documentation
     *
     * @param s
     * @throws IOException
     */
    public void write(Device s)
        throws IOException
    {
        if (visible)
            cg.write(s, this);
    }

    /*
     * Erzeugt die HTML Ausgabe der Komponente.
     * @see #write(Device)
     */
    /**
     * TODO: documentation
     */
    public final String toString() {
        StringBufferDevice erg = new StringBufferDevice();
        try {
            write(erg);
        } catch (IOException e) {}
        return erg.toString();
    }

    /*
     * Erzeugt ein eindeutiges Prefix. Erzeugt wird das durch das
     * Anhaengen eines Zeichens (Text) an die eindeutige Id.
     */
    /**
     * TODO: documentation
     *
     * @return
     */
    public String getNamePrefix() {
        if ( getDispatcher()!=null )
            return getDispatcher().getUniquePrefix() + SConstants.UID_DIVIDER
                + getUnifiedIdString() + SConstants.UID_DIVIDER;
        else
            return getUnifiedIdString() + SConstants.UID_DIVIDER;
    }

    /**
     * TODO: documentation
     */
    public static SFrame getParentFrame(SComponent c) {
        return c.getParentFrame();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SFrame getParentFrame() {
        return parentFrame;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean getResidesInForm() {
        SComponent parent = getParent();

        boolean actuallyDoes = false;
        while (parent != null && !(actuallyDoes = (parent instanceof SForm)))
            parent = parent.getParent();

        return actuallyDoes;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public ExternalizeManager getExternalizeManager() {
        return getSession().getExternalizeManager();
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setToolTipText(String t) {
        tooltip = t;
    }
    /**
     * TODO: documentation
     *
     * @return
     */
    public String getToolTipText() { return tooltip; }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * TODO: documentation
     */
    public final int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * TODO: documentation
     */
    public final void setHorizontalAlignment(int alignment) {
        horizontalAlignment = alignment;
    }

    /**
     * TODO: documentation
     */
    public final void setVerticalAlignment(int alignment) {
        verticalAlignment = alignment;
    }

    /**
     * TODO: documentation
     */
    public final int getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * TODO: documentation
     */
    public final int getColSpan() {
        return colSpan;
    }

    /**
     * TODO: documentation
     */
    public final void setColSpan(int span) {
        colSpan = span;
    }

    /**
     * TODO: documentation
     */
    public final int getRowSpan() {
        return rowSpan;
    }

    /**
     * TODO: documentation
     */
    public final void setRowSpan(int span) {
        rowSpan = span;
    }

    private Hashtable clientProperties;

    /**
     * @return a small Hashtable
     * @see #putClientProperty
     * @see #getClientProperty
     */
    private Dictionary getClientProperties() {
        if (clientProperties == null) {
            clientProperties = new Hashtable(2);
        }
        return clientProperties;
    }


    /**
     * Returns the value of the property with the specified key.  Only
     * properties added with <code>putClientProperty</code> will return
     * a non-null value.
     *
     * @return the value of this property or null
     * @see #putClientProperty
     */
    public final Object getClientProperty(Object key) {
        if(clientProperties == null) {
            return null;
        }
        else {
            return getClientProperties().get(key);
        }
    }

    /**
     * Add an arbitrary key/value "client property" to this component.
     * <p>
     * The <code>get/putClientProperty<code> methods provide access to
     * a small per-instance hashtable. Callers can use get/putClientProperty
     * to annotate components that were created by another module, e.g. a
     * layout manager might store per child constraints this way.  For example:
     * <pre>
     * componentA.putClientProperty("to the left of", componentB);
     * </pre>
     * <p>
     * If value is null this method will remove the property.
     * Changes to client properties are reported with PropertyChange
     * events.  The name of the property (for the sake of PropertyChange
     * events) is <code>key.toString()</code>.
     * <p>
     * The clientProperty dictionary is not intended to support large
     * scale extensions to JComponent nor should be it considered an
     * alternative to subclassing when designing a new component.
     *
     * @see #getClientProperty
     * @see #addPropertyChangeListener
     */
    public final void putClientProperty(Object key, Object value) {
        Object oldValue = getClientProperties().get(key);

        if (value != null) {
            getClientProperties().put(key, value);
        } else {
            getClientProperties().remove(key);
        }

        //firePropertyChange(key.toString(), oldValue, value);
    }

    /**
     * Set the look and feel delegate for this component.
     * JComponent subclasses generally override this method
     * to narrow the argument type, e.g. in JSlider:
     * <pre>
     * public void setCG(SliderCG newCG) {
     *     super.setCG(newCG);
     * }
     *  </pre>
     *
     * @see #updateCG
     * @see CGManager#getLookAndFeel
     * @see CGManager#getCG
     * @beaninfo
     *        bound: true
     *    attribute: visualUpdate true
     *  description: The component's look and feel delegate
     */
    protected void setCG(ComponentCG newCG) {
        /* We do not check that the CG instance is different
         * before allowing the switch in order to enable the
         * same CG instance *with different default settings*
         * to be installed.
         */
        if (cg != null) {
            cg.uninstallCG(this);
        }
        ComponentCG oldCG = cg;
        cg = newCG;
        if (cg != null) {
            cg.installCG(this);
        }
        //firePropertyChange("CG", oldCG, newCG);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public ComponentCG getCG() {
        return cg;
    }

    /**
     * Notification from the CGFactory that the L&F
     * has changed.
     *
     * @see SComponent#updateCG
     */
    public void updateCG() {
        if (SessionManager.getSession() == null)
            System.err.println("noch keine Session - das darf nicht sein!");
        if (SessionManager.getSession().getCGManager() == null)
            System.err.println("kein CGManager - das darf nicht sein!");
        setCG((ComponentCG)SessionManager.getSession().getCGManager().getCG(this));
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "ComponentCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
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
