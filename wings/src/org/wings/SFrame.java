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

import org.wings.plaf.FrameCG;
import org.wings.session.SessionManager;
import org.wings.style.StyleSheet;
import org.wings.util.ComponentVisitor;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.wings.script.JavaScriptListener;

import java.util.Vector;

import org.wings.script.ScriptListener;

/**
 * The frame is the root component in every component hierarchie.
 * A SessionServlet requires an instance of SFrame to render the page.
 * SFrame consists of some header informaton (meta, link, script)
 * and a stack of components. The bottommost component of the stack is always
 * the contentPane. When dialogs are to be shown, they are stacked on top of
 * it.
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>,
 *         <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SFrame
    extends SRootContainer
    implements PropertyChangeListener {
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "FrameCG";

    /**
     *  The Title of the Frame.
     */
    protected String title;

    protected String baseTarget = null;

    /**
     * A Set containing additional tags for the html header.
     */
    protected List headers;


    private Color linkColor;
    private Color vLinkColor;
    private Color aLinkColor;

    /**
     * TODO: documentation
     */
    protected boolean resizable = true;

    /** the style sheet used in certain look and feels. */
    protected StyleSheet styleSheet;  // IMPORTANT: initialization with null causes errors;
    // These: all properties, that are installed by the plaf, are installed during the initialization of
    // SComponent. The null initializations happen afterwards and overwrite the plaf installed values.
    // However: null is the default initialization value, so this is not a problem!
    // The same applies to all descendants of SComponent!

    /**
     * TODO: documentation
     */
    protected String statusLine;

    private RequestURL requestURL = null;
    private String targetResource;

    private HashMap dynamicResources;

    private SComponent focusComponent = null;                //Component which requests the focus

    private JavaScriptListener focus = null;          //Listener which sets the focus onload

    /**
     * TODO: documentation
     *
     */
    public SFrame() {
        getSession().addPropertyChangeListener("lookAndFeel", this);
        getSession().addPropertyChangeListener("request.url", this);
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
     */
    public void addDynamicResource(DynamicResource d) {
        if (dynamicResources == null) {
            dynamicResources = new HashMap();
        }
        dynamicResources.put(d.getClass(), d);
    }

    /**
     * Removes the instance of the dynamic ressource of the given class.
     */
    public void removeDynamicResource(Class dynamicResourceClass) {
        if (dynamicResources != null) {
            dynamicResources.remove(dynamicResourceClass);
        }
    }

    /**
     * TODO: documentation
     *
     */
    public DynamicResource getDynamicResource(Class c) {
        if (dynamicResources == null) {
            dynamicResources = new HashMap();
        }
        return (DynamicResource) dynamicResources.get(c);
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
     * Return <code>this</code>.
     *
     * @return this.
     */
    public SFrame getParentFrame() {
        return this;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getEventEpoch() {
        return getDynamicResource(DynamicCodeResource.class).getEpoch();
    }

    /**
     * Set server address.
     */
    public final void setRequestURL(RequestURL requestURL) {
        this.requestURL = requestURL;
    }

    /**
     * Returns the base URL for a request to the WingsServlet. This URL
     * is used to assemble an URL that trigger events. In order to be used
     * for this purpose, you've to add your parameters here.
     */
    public final RequestURL getRequestURL() {
        RequestURL result = null;
        // first time we are called, and we didn't get any change yet
        if (requestURL == null) {
            requestURL = (RequestURL) SessionManager.getSession().getProperty("request.url");
        }
        if (requestURL != null) {
            result = (RequestURL) requestURL.clone();
            result.setResource(getTargetResource());
        }
        return result;
    }

    /**
     * Set the target resource
     */
    public void setTargetResource(String targetResource) {
        this.targetResource = targetResource;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getTargetResource() {
        if (targetResource == null) {
            targetResource = getDynamicResource(DynamicCodeResource.class).getId();
        }
        return targetResource;
    }

    /**
     * Set the base target. This is the target of any link pressed.
     */
    public void setBaseTarget(String baseTarget) {
        this.baseTarget = baseTarget;
    }

    /**
     * set the base target frame. This frame will receive all klicks
     * in this frame. Usually you want to use this for the ReloadManager
     * frame.
     */
    public void setBaseTarget(SFrame otherFrame) {
        /*
         * this knows, that the frames are usually named "frame" + ID
         */
        setBaseTarget("frame" + otherFrame.getComponentId());
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getBaseTarget() {
        return baseTarget;
    }

    /**
     *
     * @param m is typically a {@link Renderable}.
     */
    public void addHeader(Object m) {
        if (!headers().contains(m))
            headers.add(m);
    }

    public void removeHeader(Object m) {
        headers.remove(m);
    }

    public void clearHeaders() {
        headers().clear();
    }

    public List headers() {
        if (headers == null)
            headers = new ArrayList(2);
        return headers;
    }

    /**
     * TODO: documentation
     *
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * TODO: documentation
     *
     * @param c
     * @deprecated use setForeground instead
     */
    public void setTextColor(Color c) {
        setForeground(c);
    }

    /**
     * TODO: documentation
     *
     * @return
     * @deprecated use getForeground instead
     */
    public Color getTextColor() {
        return getForeground();
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
     * @deprecated don't use
     * @return
     */
    public String getStatusLine() {
        return statusLine;
    }

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    public void setVisible(boolean b) {
        if (b) {
            getSession().addFrame(this);
        } else {
            getSession().removeFrame(this);
        }
        super.setVisible(b);
    }

    public void propertyChange(PropertyChangeEvent pe) {
        if ("lookAndFeel".equals(pe.getPropertyName())) {
            updateComponentTreeCG(getContentPane());
        }
        if ("request.url".equals(pe.getPropertyName())) {
            setRequestURL((RequestURL) pe.getNewValue());
        }
    }

    private void updateComponentTreeCG(SComponent c) {
        c.updateCG();
        if (c instanceof SContainer) {
            SComponent[] children = ((SContainer) c).getComponents();
            for (int i = 0; i < children.length; i++) {
                updateComponentTreeCG(children[i]);
            }
        }
        updateCG();
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(FrameCG cg) {
        super.setCG(cg);
    }

    public void invite(ComponentVisitor visitor)
        throws Exception {
        visitor.visit(this);
    }

    /*
     * Adds a scriptListener to the body-tag, which sets the focus to the Component
     * of the frame which requests the focus at last.
     **/
    public void setFocus() {
        StringBuffer compId;
        String formName;
        
        if (focusComponent != null) {
            try {
                this.removeScriptListener(focus);
            } catch (Exception e) {
            }

            SForm form = (SForm) focusComponent.getParent();
            formName = form.getName();

            compId = new StringBuffer(focusComponent.getEncodedLowLevelEventId());

            focus = new JavaScriptListener("onload", "document." + formName + "." + compId + ".focus()");
            addScriptListener(focus);

            focusComponent = null;
        }

    }

    /*
     * This function is called by SComponent.requestFocus().
     * @param c the component which requests the focus.
     *
     **/
    public void focusRequest(SComponent c) {
        focusComponent = c;
    }

    /*
     * Everytime a frame ist reloaded, this Method is called
     * by org.wings.plaf.css1.Util. Then setFocus must be called,
     * bacause the ids of the Components change at that time.
     */
    public ScriptListener[] getScriptListeners() {
        setFocus();
        return (ScriptListener[]) super.getListeners(ScriptListener.class);
    }


}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
