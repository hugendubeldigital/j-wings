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

import java.awt.Color;
import java.beans.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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
 * The frame is the root component in every component hierarchie.
 * A SessionServlet requires an instance of SFrame to render the page.
 * SFrame consists of some header informaton (metas, headers, style sheet)
 * and a stack of components. The bottommost component of the stack is always
 * the contentPane. When dialogs are to be shown, they are stacked on top of
 * it.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>,
 *         <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SFrame
    extends SContainer
    implements PropertyChangeListener
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
    protected final SContainer contentPane = new SContainer();

    SGetAddress serverAddress = new SGetAddress();

    /**
     * A List containing meta tags for the html header.
     */
    protected final ArrayList metas = new ArrayList(2);

    /**
     * A List containing additional tags for the html header.
     */
    protected final ArrayList headers = new ArrayList(2);

    /**
     * A List containing JavaScript code snippets to be included in the html header.
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

    /** the style sheet used in certain look and feels. */
    protected StyleSheet styleSheet;  // IMPORTANT: initialization with null causes errors

    /**
     * TODO: documentation
     */
    protected String statusLine = null;

    /**
     * TODO: documentation
     */
    protected transient SGetDispatcher dispatcher = null;

    private Session session;

    /**
     * TODO: documentation
     *
     */
    public SFrame() {
        super.setLayout(new SStackLayout());
        super.addComponent(getContentPane(), null);
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
    public String getBackgroundURL() {
        return backgroundURL;
    }

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
    public Icon getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Use getContentPane().addComponent(c) instead.
     */
    public final SComponent addComponent(SComponent c, Object constraint) {
        throw new IllegalArgumentException("use getContentPane().addComponent()");
    }

    /**
     * Use getContentPane().removeComponent(c) instead.
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
    public final void pushDialog(SDialog dialog) {
        super.addComponent(dialog, null);
        dialog.setFrame(this);
    }

    /**
     * TODO: documentation
     */
    public final SDialog popDialog() {
        int count = getComponentCount();
        if (count <= 1)
            throw new IllegalStateException("there's no dialog left!");

        SDialog dialog = (SDialog)getComponent(count - 1);
        super.removeComponent(dialog);
        dialog.setFrame((SFrame)null);
        return dialog;
    }

    /**
     * TODO: documentation
     */
    public final SContainer getContentPane() {
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
        serverAddress.setAbsoluteAddress(path);
    }

    /**
     * TODO: documentation
     */
    public final SGetAddress getServerAddress() {
        if  ( DEBUG && serverAddress.getRelativeAddress()==null && getParent()!=null )
            System.out.println("Frame " + serverAddress);

        return (SGetAddress)serverAddress.clone();
    }

    /**
     * TODO: documentation
     */
    public final SGetAddress getServerAddress(boolean target) {
        if ( DEBUG && serverAddress.getRelativeAddress()==null && getParent()!=null )
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
        metas.add(m);
    }

    /**
     * TODO: documentation
     *
     */
    public void clearMetas() {
        metas.clear();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public List metas() {
        return metas;
    }

    public void addHeader(String m) {
	headers.add(m);
    }

    public void clearHeaders() {
	headers.clear();
    }
    
    public List headers() {
	return headers;
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


    public void propertyChange(PropertyChangeEvent pe) {
        if ("lookAndFeel".equals(pe.getPropertyName())) {
            updateComponentTreeCG(getContentPane());
            System.err.println("lookAndFeel Change: " + pe.getPropertyName());
        }
        else
            System.err.println("propertyChange: " + pe.getPropertyName());
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
        updateCG();
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

    private class SStackLayout extends SAbstractLayoutManager
    {
        private SContainer container = null;

        public SStackLayout() {}

        public void updateCG() {}
        public void addComponent(SComponent c, Object constraint) {}
        public void removeComponent(SComponent c) {}

        public SComponent getComponentAt(int i) {
            return (SComponent)container.getComponent(i);
        }

        public void setContainer(SContainer c) {
            container = c;
        }

        /**
         * Allways write code for the topmost component.
         *
         * @param s
         * @throws IOException
         */
        public void write(Device s)
            throws IOException
        {
            int topmost = container.getComponentCount() - 1;
            SComponent comp = (SComponent)container.getComponent(topmost);
            comp.write(s);
        }
    }

    public void setCG(FrameCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
