/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.css;


import org.wings.*;
import org.wings.header.Link;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.resource.DefaultURLResource;
import org.wings.resource.DynamicCodeResource;
import org.wings.script.DynamicScriptResource;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;
import org.wings.session.Browser;
import org.wings.session.BrowserType;
import org.wings.session.SessionManager;
import org.wings.style.DynamicStyleSheetResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

public class FrameCG implements SConstants, org.wings.plaf.FrameCG {

    /**
     * The default DOCTYPE enforcing standard (non-quirks mode) in all current browsers.
     * Please be aware, that changing the DOCTYPE may change the way how browser renders the generate
     * document i.e. esp. the CSS attribute inheritance does not work correctly on <code>table</code> elements.
     * See i.e. http://www.ericmeyeroncss.com/bonus/render-mode.html
     */
    public final static String DEFAULT_DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" " +
            "\"http://www.w3.org/TR/REC-html40/strict.dtd\">";

    //--- properties of this plaf.
    private String documentType;
    private Boolean renderXmlDeclaration;

    /**
     * Initialize properties from config
     */
    public FrameCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();
        final String userDocType = (String) manager.getObject("FrameCG.userDocType", String.class);
        final Boolean userRenderXmlDecl = (Boolean) manager.getObject("FrameCG.renderXmlDeclaration", Boolean.class);
        if (userDocType != null)
            setDocumentType(userDocType);
        else
            setDocumentType(DEFAULT_DOCTYPE);
        if (userRenderXmlDecl != null)
            setRenderXmlDeclaration(userRenderXmlDecl);
        else
            setRenderXmlDeclaration(Boolean.FALSE);
    }


    public void installCG(final SComponent comp) {
        final SFrame component = (SFrame) comp;

        DynamicCodeResource dynamicCodeRessource;
        DynamicStyleSheetResource styleSheetResource;
        DynamicScriptResource scriptResource;
        Link stylesheetLink;

        // dynamic code resource.
        dynamicCodeRessource = new DynamicCodeResource(component);
        component.addDynamicResource(dynamicCodeRessource);

        // dynamic stylesheet resource.
        styleSheetResource = new DynamicStyleSheetResource(component);
        stylesheetLink = new Link("stylesheet", null, "text/css", null, styleSheetResource);
        component.addDynamicResource(styleSheetResource);
        component.addHeader(stylesheetLink);

        // dynamic java script resource.
        scriptResource = new DynamicScriptResource(component);
        component.addDynamicResource(scriptResource);
        component.addHeader(new Script("JavaScript", "text/javascript", scriptResource));

        component.addHeader(new Script("JavaScript", "text/javascript", new DefaultURLResource("../domLib.js")));
        component.addHeader(new Script("JavaScript", "text/javascript", new DefaultURLResource("../domTT.js")));

        Browser browser = component.getSession().getUserAgent();
        String stylesheet = "../gecko.css";
        if (browser.getBrowserType() == BrowserType.GECKO)
            stylesheet = "../gecko.css";
        else if (browser.getBrowserType() == BrowserType.IE)
            stylesheet = "../msie.css";
        else if (browser.getBrowserType() == BrowserType.KONQUEROR)
            stylesheet = "../konqueror.css";

        component.headers().add(0, new Link("stylesheet", null, "text/css", null, new DefaultURLResource(stylesheet)));

        component.addScriptListener(FORM_SCRIPT_LOADER);
        CaptureDefaultBindingsScriptListener.install(component);
    }

    public void uninstallCG(final SComponent comp) {
        final SFrame component = (SFrame) comp;

        component.removeDynamicResource(DynamicCodeResource.class);
        component.removeDynamicResource(DynamicStyleSheetResource.class);
        component.removeDynamicResource(DynamicScriptResource.class);
        component.removeScriptListener(FORM_SCRIPT_LOADER);
        component.clearHeaders();
    }

//--- code from common area in template.
    /*
    public static final JavaScriptListener DATE_CHOOSER_SCRIPT_LOADER =
    new JavaScriptListener("", "", loadScript("org/wings/plaf/css/DateChooser.js"));
    */
    public static final JavaScriptListener FORM_SCRIPT_LOADER =
            new JavaScriptListener("", "", loadScript("org/wings/plaf/css/Form.js"));

    public static String loadScript(String resource) {
        InputStream in = null;
        BufferedReader reader = null;
        try {
            in = FrameCG.class.getClassLoader().getResourceAsStream(resource);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null)
                buffer.append(line).append("\n");
            buffer.append(line).append("\n");

            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                in.close();
            } catch (Exception ign) {
            }
            try {
                reader.close();
            } catch (Exception ign1) {
            }
        }
    }


//--- end code from common area in template.


    public void write(final Device device,
                      final SComponent _c)
            throws IOException {
        if (!_c.isVisible()) return;
        _c.fireRenderEvent(SComponent.START_RENDERING);
        final SFrame component = (SFrame) _c;

        org.wings.session.Browser browser = SessionManager.getSession().getUserAgent();
        SFrame frame = (SFrame) component;
        String language = SessionManager.getSession().getLocale().getLanguage();
        String title = frame.getTitle();
        List headers = frame.headers();
        String encoding = SessionManager.getSession().getCharacterEncoding();


        if (renderXmlDeclaration == null || renderXmlDeclaration.booleanValue()) {
            device.write("<?xml version=\"1.0\" encoding=\"".getBytes());
            org.wings.plaf.Utils.write(device, encoding);
            device.write("\"?>\n".getBytes());
        }

        org.wings.plaf.Utils.writeRaw(device, documentType);
        device.write("\n".getBytes());
        device.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"".getBytes());
        org.wings.plaf.Utils.write(device, language);
        device.write("\" lang=\"".getBytes());
        org.wings.plaf.Utils.write(device, language);
        device.write("\">\n".getBytes());

        device.write("<head>".getBytes());
        if (title != null) {
            device.write("<title>".getBytes());
            org.wings.plaf.Utils.write(device, title);
            device.write("</title>\n".getBytes());
        }

        device.write("<meta http-equiv=\"Content-type\" content=\"text/html; charset=".getBytes());
        org.wings.plaf.Utils.write(device, encoding);
        device.write("\"/>\n".getBytes());

        for (Iterator iterator = headers.iterator(); iterator.hasNext();) {
            Object next = iterator.next();
            if (next instanceof Renderable) {
                ((Renderable) next).write(device);
            } else {
                org.wings.plaf.Utils.write(device, next.toString());
            }
            device.write("\n".getBytes());
        }

        SComponent focus = frame.getFocus();
        Object lastFocus = frame.getClientProperty("focus");
        if (focus != lastFocus) {
            if (lastFocus != null) {
                ScriptListener[] scriptListeners = frame.getScriptListeners();

                for (int i = 0; i < scriptListeners.length; i++) {
                    ScriptListener scriptListener = scriptListeners[i];
                    if (scriptListener instanceof FocusScriptListener)
                        component.removeScriptListener(scriptListener);
                }
            }
            if (focus != null) {
                FocusScriptListener listener = new FocusScriptListener("onload", "requestFocus('" + focus.getName() + "')");
                frame.addScriptListener(listener);
            }
            frame.putClientProperty("focus", focus);
        }

        // TODO: move this to a dynamic script resource
        SToolTipManager toolTipManager = component.getSession().getToolTipManager();
        device
                .print("<script language=\"JavaScript\" type=\"text/javascript\">\n")
                .print("domTT_addPredefined('default', 'caption', false");
        if (toolTipManager.isFollowMouse())
            device.print(", 'trail', true");
        device.print(", 'delay', ").print(toolTipManager.getInitialDelay());
        device.print(", 'lifetime', ").print(toolTipManager.getDismissDelay());
        device
                .print(");\n")
                .print("</script>\n");

        device.write("</head>\n".getBytes());
        device.write("<body ".getBytes());
        org.wings.plaf.Utils.optAttribute(device, "class", frame.getStyle());
        Utils.writeEvents(device, frame);
        device.write(">\n".getBytes());
        if (frame.isVisible()) {
            frame.getLayout().write(device);
        }

        device.write("</body></html>\n".getBytes());
        _c.fireRenderEvent(SComponent.DONE_RENDERING);
    }

    //--- setters and getters for the properties.

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * @return The current rendered DOCTYPE of this document. {@link #DEFAULT_DOCTYPE}
     */
    public Boolean getRenderXmlDeclaration() {
        return renderXmlDeclaration;
    }

    public void setRenderXmlDeclaration(Boolean renderXmlDeclaration) {
        this.renderXmlDeclaration = renderXmlDeclaration;
    }

    public String mapSelector(String selector) {
        return selector;
    }
}
