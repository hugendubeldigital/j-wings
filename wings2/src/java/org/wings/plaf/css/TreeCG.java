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
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.session.SessionManager;
import org.wings.tree.SDefaultTreeSelectionModel;
import org.wings.tree.STreeCellRenderer;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.IOException;

public class TreeCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.TreeCG {
    private SIcon collapseControlIcon;
    private SIcon emptyFillIcon;
    private SIcon expandControlIcon;
    private SIcon hashMark;
    private SIcon leafControlIcon;

    /**
     * Initialize properties from config
     */
    public TreeCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();

        setCollapseControlIcon((SIcon) manager.getObject("TreeCG.collapseControlIcon", SIcon.class));
        setEmptyFillIcon((SIcon) manager.getObject("TreeCG.emptyFillIcon", SIcon.class));
        setExpandControlIcon((SIcon) manager.getObject("TreeCG.expandControlIcon", SIcon.class));
        setHashMark((SIcon) manager.getObject("TreeCG.hashMark", SIcon.class));
        setLeafControlIcon((SIcon) manager.getObject("TreeCG.leafControlIcon", SIcon.class));
    }


    public void installCG(final SComponent comp) {
        super.installCG(comp);
        final STree component = (STree) comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value;
        value = manager.getObject("STree.cellRenderer", STreeCellRenderer.class);
        if (value != null) {
            component.setCellRenderer((STreeCellRenderer) value);
        }
        value = manager.getObject("STree.nodeIndentDepth", Integer.class);
        if (value != null) {
            component.setNodeIndentDepth(((Integer) value).intValue());
        }
    }

//--- code from common area in template.
    private final boolean isLastChild(TreeModel model, TreePath path, int i) {
        Object node = path.getPathComponent(i);
        if (i == 0)
            return true;
        Object parent = path.getPathComponent(i - 1);

        return node.equals(model.getChild(parent, model.getChildCount(parent) - 1));
    }

    private void writeIcon(Device device, SIcon icon, int width, int height) throws IOException {

        device.print("<img");
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", width);
        Utils.optAttribute(device, "height", height);
        device.print("/>");
    }

    private void writeIcon(Device device, SIcon icon, boolean nullBorder) throws IOException {
        if (icon == null) return;

        device.print("<img");
        if (nullBorder) {
            device.print(" border=\"0\"");
        }
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());

        device.print("/>");
    }

    private void writeTreeNode(STree component, Device device, int row, int depth)
            throws IOException {
        boolean childSelectorWorkaround = !component.getSession().getUserAgent().supportsCssChildSelector();
        final TreePath path = component.getPathForRow(row);
        final int nodeIndentDepth = component.getNodeIndentDepth();

        final Object node = path.getLastPathComponent();
        final STreeCellRenderer cellRenderer = component.getCellRenderer();

        final boolean isLeaf = component.getModel().isLeaf(node);
        final boolean isExpanded = component.isExpanded(path);
        final boolean isSelected = component.isPathSelected(path);

        final boolean isSelectable = (component.getSelectionModel() != SDefaultTreeSelectionModel.NO_SELECTION_MODEL);

        SComponent cellComp = cellRenderer.getTreeCellRendererComponent(component, node,
                isSelected,
                isExpanded,
                isLeaf, row,
                false);

        device.print("<tr height=\"1\">");

        /*
        * fill the indented area.
        */
        for (int i = ((component.isRootVisible()) ? 0 : 1); i < path.getPathCount() - 1; ++i) {
            device.print("<td indent=\"true\"");
            Utils.optAttribute(device, "width", nodeIndentDepth);
            if (hashMark != null && !isLastChild(component.getModel(), path, i)) {
                device.print(" style=\"background-image:url(");
                Utils.write(device, hashMark.getURL());
                device.print(")\"");
            }
            if (childSelectorWorkaround)
                Utils.childSelectorWorkaround(device, "indent");

            device.print(">");

            if (emptyFillIcon != null) {
                writeIcon(device, emptyFillIcon, nodeIndentDepth, 1);
            } else {
                for (int n = nodeIndentDepth; n > 0; --n) {
                    device.print("&nbsp;");
                }
            }

            device.print("</td>");
        }

        /*
        * now, write the component.
        */
        device.print("<td");
        Utils.optAttribute(device, "colspan", depth - (path.getPathCount() - 1));
        if (isSelected) {
            device.print(" selected=\"true\"");

            if (childSelectorWorkaround)
                Utils.childSelectorWorkaround(device, "selected");
        }
        device.print(">");

        /*
        * in most applications, the is no need to render a control icon for a
        * leaf. So in that case, we can avoid writing the sourrounding 
        * table, that will speed up things in browsers.
        */
        final boolean renderControlIcon = !(isLeaf && leafControlIcon == null);

        if (renderControlIcon) {

            device.print("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td>");
            if (isLeaf) {
                writeIcon(device, leafControlIcon, false);
            } else {
                if (component.getShowAsFormComponent()) {
                    device.print("<button name=\"");
                    Utils.write(device, Utils.event(component));
                    device.print("\" value=\"");
                    Utils.write(device, component.getExpansionParameter(row, false));
                    device.print("\"");
                } else {
                    RequestURL selectionAddr = component.getRequestURL();
                    selectionAddr.addParameter(org.wings.plaf.css.Utils.event(component),
                            component.getExpansionParameter(row, false));

                    device.print("<a href=\"");
                    Utils.write(device, selectionAddr.toString());
                    device.print("\"");
                }
                device.print("\">");

                if (isExpanded) {
                    if (collapseControlIcon == null) {
                        device.print("-");
                    } else {
                        writeIcon(device, collapseControlIcon, true);
                    }
                } else {
                    if (expandControlIcon == null) {
                        device.print("+");
                    } else {
                        writeIcon(device, expandControlIcon, true);
                    }
                }
                if (component.getShowAsFormComponent())
                    device.print("</button>");
                else
                    device.print("</a>");
            }

            device.print("</td><td>");
        }

        SCellRendererPane rendererPane = component.getCellRendererPane();
        if (isSelectable) {
            if (component.getShowAsFormComponent()) {
                device.print("<button name=\"");
                Utils.write(device, Utils.event(component));
                device.print("\" value=\"");
                Utils.write(device, component.getSelectionParameter(row, false));
                device.print("\"");
            } else {
                RequestURL selectionAddr = component.getRequestURL();
                selectionAddr.addParameter(org.wings.plaf.css.Utils.event(component),
                        component.getSelectionParameter(row, false));

                device.print("<a href=\"");
                Utils.write(device, selectionAddr.toString());
                device.print("\"");
            }

            Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
            Utils.writeEvents(device, component);
            device.print(">");

            rendererPane.writeComponent(device, cellComp, component);

            if (component.getShowAsFormComponent())
                device.print("</button>");
            else
                device.print("</a>");
        } else {
            rendererPane.writeComponent(device, cellComp, component);
        }

        if (renderControlIcon)
            device.print("</td></tr></table>");

        device.print("</td><td width=\"100%\"></td></tr>\n");
    }


//--- end code from common area in template.


    public void writeContent(final Device device, final SComponent _c)
            throws IOException {
        final STree component = (STree) _c;

        int start = 0;
        int count = component.getRowCount();

        java.awt.Rectangle viewport = component.getViewportSize();
        if (viewport != null) {
            start = viewport.y;
            count = viewport.height;
        }

        final int depth = component.getMaximumExpandedDepth();

        device.print("<table");
        Utils.printCSSInlinePreferredSize(device, component.getPreferredSize());
        device.print(">");

        for (int i = start; i < count; ++i)
            writeTreeNode(component, device, i, depth);

        device.print("<tr><td");
        Utils.optAttribute(device, "colspan", depth);
        device.print("></td></tr></table>");
    }

//--- setters and getters for the properties.
    public SIcon getCollapseControlIcon() {
        return collapseControlIcon;
    }

    public void setCollapseControlIcon(SIcon collapseControlIcon) {
        this.collapseControlIcon = collapseControlIcon;
    }

    public SIcon getEmptyFillIcon() {
        return emptyFillIcon;
    }

    public void setEmptyFillIcon(SIcon emptyFillIcon) {
        this.emptyFillIcon = emptyFillIcon;
    }

    public SIcon getExpandControlIcon() {
        return expandControlIcon;
    }

    public void setExpandControlIcon(SIcon expandControlIcon) {
        this.expandControlIcon = expandControlIcon;
    }

    public SIcon getHashMark() {
        return hashMark;
    }

    public void setHashMark(SIcon hashMark) {
        this.hashMark = hashMark;
    }

    public SIcon getLeafControlIcon() {
        return leafControlIcon;
    }

    public void setLeafControlIcon(SIcon leafControlIcon) {
        this.leafControlIcon = leafControlIcon;
    }

}
