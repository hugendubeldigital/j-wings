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
import java.beans.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Icon;

import org.wings.*;
import org.wings.io.Device;
import org.wings.io.StringBufferDevice;
import org.wings.io.DeviceBuffer;
import org.wings.plaf.*;
import org.wings.style.StyleSheet;
import org.wings.session.Session;
import org.wings.session.SessionManager;

/*
 * Die Frame Implementierung. Anders als in der Swing Implementierung
 * gibt es keinen Layered Pane (macht in HTML wenig Sinn), aber das
 * Vorgehen ist aehnlich. Es gibt einen ContentPane und einen
 * optionPane. Der normale Inhalt eines Frames wird in den Content
 * Pane gestellt, Nachrichten Dialoge (OptionPanes) in den
 * OptionPane. Je nachdem, was gerade aktiv ist wird das verwaltende
 * {@link SCardLayout} geflippt.
 */

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SFrame extends SContainer implements PropertyChangeListener
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "FrameCG";

    private static final boolean DEBUG = true;

    /**
     *  The Title of the Frame.
     */
    protected String title = "";

    /**
     * The container for the contentPane.
     */
    protected SContainer contentPane = new FrameContainer();

    /**
     * The container for the optionPane, if present.
     */
    protected final SContainer optionPaneContainer =
        new FrameContainer();

    /**
     * The currently active optionPane.
     */
    protected SDialog optionPane = null;

    /**
     * The layout.
     */
    protected final SCardLayout card = new SCardLayout();

    /**
     * Key in the SCardLayout that references the optionPane
     */
    protected final String OPTION_PANEL = "option";

    /**
     * Key in the SCardLayout that references the contentPane
     */
    protected final String CONTENT_PANEL = "content";

    SGetAddress serverAddress = new SGetAddress();

    /**
     * TODO: documentation
     */
    protected final ArrayList meta = new ArrayList(2);

    /**
     * TODO: documentation
     */
    protected final ArrayList javaScript = new ArrayList(2);


    private Color textColor = null;

    private Color linkColor = null;

    private Color vLinkColor = null;

    private Color aLinkColor = null;

    private Icon backgroundImage = null;

    private String backgroundURL = null;

    /**
     * TODO: documentation
     */
    protected boolean resizable = true;

    /**
     * TODO: documentation
     */
    protected StyleSheet styleSheet = null;

    /**
     * TODO: documentation
     */
    protected String statusLine = null;

    private final DeviceBuffer bodyBuffer = new DeviceBuffer();

    // warum steht das hier?
    /**
     * TODO: documentation
     */
    protected transient SGetDispatcher dispatcher = null;

    private Session session = null;

    /**
     * TODO: documentation
     *
     */
    public SFrame() {
        super();

        setLayout(card);
        super.addComponent(getContentPane(), CONTENT_PANEL);
        super.addComponent(optionPaneContainer, OPTION_PANEL);
        card.show(this, CONTENT_PANEL);
        
        getSession().addPropertyChangeListener("lookAndFeel", this);
    }

    /**
     * TODO: documentation
     *
     * @param title
     */
    public SFrame(String title) {
        this();
        setTitle(title);
    }

    /**
     * TODO: documentation
     *
     * @param b
     */
    public void setResizable(boolean b) {
        resizable = b;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isResizable() {
        return resizable;
    }

    /**
     * Set the URL of the background image.
     */
    public void setBackgroundURL(String url) {
        backgroundURL = url;
    }
    /**
     * TODO: documentation
     *
     * @return
     */
    public String getBackgroundURL() { return backgroundURL; }

    /**
     * Set the background image.
     */
    public void setBackgroundImage(Icon icon) {
        backgroundImage = icon;
    }
    /**
     * TODO: documentation
     *
     * @return
     */
    public Icon getBackgroundImage() { return backgroundImage; }

    /**
     * TODO: documentation
     */
    public final SComponent addComponent(SComponent c, Object constraint) {
        throw new IllegalArgumentException("use getContentPane().addComponent()");
    }

    /**
     * TODO: documentation
     */
    public final boolean removeComponent(SComponent c) {
        throw new IllegalArgumentException("use getContentPane().removeComponent()");
    }

    /*
     * Setzt den Dispatcher auch im optionPane und ContentPane .. ja ???
     */
    /**
     * TODO: documentation
     *
     * @param d
     */
    public void setDispatcher(SGetDispatcher d) {
        dispatcher = d;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SGetDispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SFrame getParentFrame() {
        return this;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Session getSession() {
        if (session == null)
            session = SessionManager.getSession();
        return session;
    }

    /**
     * TODO: documentation
     */
    public final void showOptionPane() {
        if ( optionPane!=null ) {
            card.show(this, OPTION_PANEL);
            optionPane.setFrame(this);
        }
    }

    /**
     * TODO: documentation
     */
    public final void showContentPane() {
        card.show(this, CONTENT_PANEL);
        if ( optionPane!=null )
            optionPane.setFrame(null);
    }

    /**
     * TODO: documentation
     */
    public final void setOptionPane(SDialog c) {
        optionPane = c;
        if ( optionPane!=null ) {
            optionPaneContainer.removeAll();
            optionPaneContainer.add(optionPane);
        }
    }

    /**
     * TODO: documentation
     */
    public final SContainer getContentPane() {
        if ( contentPane==null )
            contentPane = new SContainer();
        return contentPane;
    }

    /**
     * TODO: documentation
     */
    public final void setServer(String server, int port, String path) {
        setServer("http", server, port, path);
    }

    /**
     * TODO: documentation
     */
    public final void setServer(String scheme, String server, int port,
                                String path) {
        String addr = scheme + "://"+server;

        if ( port>0 && port!=80 )
            addr += ":" + port;

        addr += path;

        setServer(addr);
    }

    /**
     * TODO: documentation
     */
    public final void setServer(String path) {
        serverAddress.clear();
        serverAddress.setBaseAddress(path);
    }

    /**
     * TODO: documentation
     */
    public final SGetAddress getServerAddress() {
        if  ( DEBUG && serverAddress.getBaseAddress()==null && getParent()!=null )
            System.out.println("Frame " + serverAddress);

        return (SGetAddress)serverAddress.clone();
    }

    /**
     * TODO: documentation
     */
    public final SGetAddress getServerAddress(boolean target) {
        if ( DEBUG && serverAddress.getBaseAddress()==null && getParent()!=null )
            System.out.println("Frame " + serverAddress);

        SGetAddress addr = (SGetAddress)serverAddress.clone();
        return addr;
    }

    /*
     * nur der Inhalt des Tags, also z.B. <PRE>name="keywords" lang="de"
     * content="Ferien, Griechenland, Sonnenschein"<PRE>
     */
    /**
     * TODO: documentation
     *
     * @param m
     */
    public void addMeta(String m) {
        meta.add(m);
    }

    /**
     * TODO: documentation
     *
     */
    public void clearMeta() {
        meta.clear();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Iterator metas() {
        return meta.iterator();
    }

    /**
     * TODO: documentation
     *
     * @param j
     */
    public void addJavaScript(String j) {
        javaScript.add(j);
    }

    /**
     * TODO: documentation
     *
     */
    public void clearJavaScript() {
        javaScript.clear();
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setTitle(String t) {
        if ( t==null )
            title = "";
        else
            title = t;
    }
    /**
     * TODO: documentation
     *
     * @return
     */
    public String getTitle() { return title; }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setTextColor(Color c) {
        textColor = c;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setLinkColor(Color c) {
        linkColor = c;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Color getLinkColor() {
        return linkColor;
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setVLinkColor(Color c) {
        vLinkColor = c;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Color getVLinkColor() {
        return vLinkColor;
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setALinkColor(Color c) {
        aLinkColor = c;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Color getALinkColor() {
        return aLinkColor;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setStyleSheet(StyleSheet s) {
        styleSheet = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public StyleSheet getStyleSheet() {
        return styleSheet;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setStatusLine(String s) {
        statusLine = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String show() {
        StringBufferDevice erg = new StringBufferDevice();
        try {
            write(erg);
        }
        catch (IOException e) {
        }
        return erg.toString();
    }


    private final class FrameContainer extends SContainer {
        public SComponent addComponent(SComponent c, Object constraint) {
            SComponent result = super.addComponent(c, constraint);
            c.addedToFrame();
            return result;
        }
    }

    public void propertyChange(PropertyChangeEvent pe) {
        if ("lookAndFeel".equals(pe.getPropertyName())) {
            updateComponentTreeCG(getContentPane());
        }
    }

    private void updateComponentTreeCG(SComponent c) {
        if (c instanceof SComponent) {
            ((SComponent)c).updateCG();
        }
        if (c instanceof SContainer) {
            SComponent[] children = ((SContainer)c).getComponents();
            for(int i = 0; i < children.length; i++) {
                updateComponentTreeCG(children[i]);
            }
        }
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "FrameCG"
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
