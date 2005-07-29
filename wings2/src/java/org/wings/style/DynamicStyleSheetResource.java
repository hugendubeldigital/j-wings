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
package org.wings.style;

import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SFrame;
import org.wings.border.SBorder;
import org.wings.io.Device;
import org.wings.plaf.ComponentCG;
import org.wings.resource.DynamicResource;
import org.wings.session.BrowserType;
import org.wings.session.SessionManager;
import org.wings.util.ComponentVisitor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Traverses the component hierarchy of a frame and gathers the dynamic styles
 * (attributes) of all components in a style sheet.
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DynamicStyleSheetResource
        extends DynamicResource {

    /**
     * These CSS properties will not be inherited over tables in IE.
     * Circumvents using quirks mode.
     */
    private final static List tableResistantProperties = Arrays.asList(new CSSProperty[]{
        CSSProperty.COLOR, CSSProperty.FONT, CSSProperty.FONT_FAMILY, CSSProperty.FONT_SIZE,
        CSSProperty.FONT_STYLE, CSSProperty.FONT_VARIANT, CSSProperty.FONT_WEIGHT,
        CSSProperty.TEXT_DECORATION, CSSProperty.TEXT_TRANSFORM, CSSProperty.LETTER_SPACING,
        CSSProperty.LINE_HEIGHT, CSSProperty.BACKGROUND_COLOR});

    public DynamicStyleSheetResource(SFrame frame) {
        super(frame, "css", "text/css");
    }

    public void write(Device out)
            throws IOException {
        try {
            StyleSheetWriter visitor = new StyleSheetWriter(out);
            getFrame().invite(visitor);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage()); // UndeclaredThrowable
        }
    }

    protected static class StyleSheetWriter
            implements ComponentVisitor {
        Device out;

        public StyleSheetWriter(Device out) {
            this.out = out;
        }

        private void writeAttributesFrom(SComponent component)
                throws IOException {
            final Collection dynamicStyles = component.getDynamicStyles();
            if (dynamicStyles != null) {
                final ComponentCG cg = component.getCG();
                final BrowserType currentBrowser = SessionManager.getSession().getUserAgent().getBrowserType();
                final boolean isMSIE = BrowserType.IE.equals(currentBrowser);

                for (Iterator iterator = dynamicStyles.iterator(); iterator.hasNext();) {
                    final CSSStyle style = (CSSStyle) iterator.next();
                    // Map pseudo css selectors to real selectors
                    final CSSSelector selector = cg.mapSelector(component, (CSSSelector) style.getSelector());

                    // Output selector string
                    String selectorString = selector.getSelectorString();

                    if (isMSIE) {
                        // IE Workaround: We need to operate IE in quirks mode.
                        // Hence we have to inherit some props manually over TABLE elements.
                        final Set tableBlockedProperties = new HashSet(style.properties());
                        tableBlockedProperties.retainAll(tableResistantProperties);
                        // If this style containes a table blocked CSS property then add table as additional selector.
                        if (tableBlockedProperties.size() > 0)
                            out.print(selectorString).print(" table ").print(", ");
                    } else {
                        // Non IE Workaround: In all other browsers we surround each component with two DIV
                        // elements. The outer wears the component id, but the inner DIV is the one which
                        // surrounds the component without any space (i.e. for setting component background)
                        // If the ID of the outer DIV is e1 then the ID of the inner div is e1_i
                        // we need to apply our styles to the inner div, not the outer div!

                        // Selector string contains
                        if (selectorString.indexOf('#') >= 0) {
                            int pos = selectorString.indexOf('#')+1;
                            while (pos < selectorString.length() && Character.isLetterOrDigit(selectorString.charAt(pos)))
                                pos++;
                            // make id to id_i
                            selectorString = selectorString.substring(0, pos) + "_i" + selectorString.substring(pos);
                        }
                    }

                    out.print(selectorString);
                    out.print("{");
                    style.write(out);
                    out.print("}\n");
                }
            }

            SBorder border = component.getBorder();
            if (border != null) {
                out.print(CSSSelector.getSelectorString(component)).print("{");
                border.getAttributes().write(out);
                out.print("}\n");
            }
        }

        public void visit(SComponent component) throws Exception {
            writeAttributesFrom(component);
        }

        public void visit(SContainer container) throws Exception {
            writeAttributesFrom(container);
            container.inviteEachComponent(this);
        }
    }
}
