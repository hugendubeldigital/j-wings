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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Insets;
import java.io.IOException;
import java.util.*;

import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.wings.table.*;
import org.wings.io.Device;
import org.wings.plaf.*;
import org.wings.style.*;

/**
 * This is a base implementation from which all other tables are derived.
 * This base table is a simple table. It is used if selection is not
 * required and the table is only used to present tabular data.
 * <p>
 * The advanced stuff like selection is implemented in {@link STable}.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SBaseTable
    extends SComponent
    implements TableModelListener, Scrollable
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "BaseTableCG";

    /**
     * The default model which is used if someone sets the model to null.
     */
    protected final static TableModel defaultModel = new DefaultTableModel();


    /**
     * The default renderer is used if no other renderer is set for the
     * content of a cell.
     */
    protected STableCellRenderer defaultRenderer;

    /** The header renderer is used for the header line */
    protected STableCellRenderer headerRenderer;

    /**
     * The table model.
     */
    protected TableModel model = null;

    /**
     * The renderer for the different classes of cell content. The class is
     * the key, the renderer the value.
     */
    protected final HashMap renderer = new HashMap();

    /**
     * The header is not (yet) implemented like in Swing. But maybe someday.
     * So you can disable it if you like.
     */
    protected boolean headerVisible = true;

    /** The style of header cells */
    protected Style headerStyle;

    /** The dynamic attributes of header cells */
    protected AttributeSet headerAttributes = new SimpleAttributeSet();

    /**
     * TODO: documentation
     */
    protected boolean showHorizontalLines = false;

    /**
     * TODO: documentation
     */
    protected boolean showVerticalLines = false;

    /**
     * TODO: documentation
     */
    protected SDimension intercellSpacing;

    /**
     * TODO: documentation
     */
    protected SDimension intercellPadding = new SDimension("1", "1");

    /**
     * TODO: documentation
     */
    protected Rectangle viewport = null;

    /**
     * TODO: documentation
     */
    protected String width = null;

    /*
     * Vorerst mal ist der Border noch nicht wie im Swing
     * implementiert. Leider wird dies wohl auch nicht moeglich sein, da
     * ein Swing AbstractBorder seine Daten nicht ohne ein uebergebenes
     * JComponent rausrueckt: <BR>
     * AbstractBorder.getBorderInsets(java.awt.Component c)
     */
    /**
     * TODO: documentation
     */
    protected Insets borderLines = null;

    /**
     * TODO: documentation
     *
     * @param tm
     */
    public SBaseTable(TableModel tm){
        setModel(tm);
    }

    public void setPrototype(String p) {
    }

    /**
     * rows, cols
     */
    public String[][]getPrototype() {
        return null;
    }

    /**
     * TODO: documentation
     *
     * @param hv
     */
    public void setHeaderVisible(boolean hv) {
        boolean oldHeaderVisible = headerVisible;
        headerVisible = hv;
        if (oldHeaderVisible != headerVisible)
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isHeaderVisible() {
        return headerVisible;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public void setHeaderStyle(Style style) {
        this.headerStyle = style;
    }

    /**
     * TODO: documentation
     */
    public Style getHeaderStyle() { return headerStyle; }

    /**
     * Set the headerAttributes.
     * @param headerAttributes the headerAttributes
     */
    public void setHeaderAttributes(AttributeSet headerAttributes) {
        if (headerAttributes == null)
            throw new IllegalArgumentException("null not allowed");

        if (!this.headerAttributes.equals(headerAttributes)) {
            this.headerAttributes = headerAttributes;
            reload(ReloadManager.RELOAD_STYLE);
        }
    }

    /**
     * @return the current headerAttributes
     */
    public AttributeSet getHeaderAttributes() {
        return headerAttributes;
    }

    /**
     * Set the background color.
     * @param c the new background color
     */
    public void setHeaderBackground(Color color) {
        boolean changed = headerAttributes.putAttributes(CSSStyleSheet.getAttributes(color, "background-color"));
        if (changed)
            reload(ReloadManager.RELOAD_STYLE);
    }

    /**
     * Return the background color.
     * @return the background color
     */
    public Color getHeaderBackground() {
        return CSSStyleSheet.getBackground(headerAttributes);
    }

    /**
     * Set the foreground color.
     * @param c the new foreground color
     */
    public void setHeaderForeground(Color color) {
        boolean changed = headerAttributes.putAttributes(CSSStyleSheet.getAttributes(color, "color"));
        if (changed)
            reload(ReloadManager.RELOAD_STYLE);
    }

    /**
     * Return the foreground color.
     * @return the foreground color
     */
    public Color getHeaderForeground() {
        return CSSStyleSheet.getForeground(headerAttributes);
    }

    /**
     * TODO: documentation
     *
     * @param b
     */
    public void setShowGrid(boolean b) {
        setShowHorizontalLines(b);
        setShowVerticalLines(b);
    }

    /**
     * TODO: documentation
     *
     * @param w
     * @deprecated use <i>setPreferredSize</i> in {@link SComponent} instead.
     */
    public void setWidth(String w) {
        String oldWidth = width;
        width = w;
        if ((width == null && oldWidth != null) ||
            width != null && !width.equals(oldWidth))
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * TODO: documentation
     *
     * @return
     * @deprecated use <i>getPreferredSize</i> in {@link SComponent} instead.
     */
    public String getWidth() {
        return width;
    }

    /**
     * TODO: documentation
     *
     * @param b
     */
    public void setShowHorizontalLines(boolean b) {
        boolean oldShowHorizontalLines = showHorizontalLines;
        showHorizontalLines = b;
        if (showHorizontalLines != oldShowHorizontalLines)
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean getShowHorizontalLines() {
        return showHorizontalLines;
    }

    /**
     * TODO: documentation
     *
     * @param b
     */
    public void setShowVerticalLines(boolean b) {
        boolean oldShowVerticalLines = showVerticalLines;
        showVerticalLines = b;
        if (showVerticalLines != oldShowVerticalLines)
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean getShowVerticalLines() {
        return showVerticalLines;
    }

    /*
     * Implementiert das cellspacing Attribut des HTML Tables. Da dieses
     * nur eindimensional ist, wird nur der width Wert der Dimension in
     * den HTML Code uebernommen.
     */
    /**
     * TODO: documentation
     *
     * @param d
     */
    public void setIntercellSpacing(SDimension d) {
        SDimension oldIntercellSpacing = intercellSpacing;
        intercellSpacing = d;
        if ((intercellSpacing == null && oldIntercellSpacing != null) ||
            intercellSpacing != null && !intercellSpacing.equals(oldIntercellSpacing))
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SDimension getIntercellSpacing() {
        return intercellSpacing;
    }

    /*
     * Implementiert das cellpadding Attribut des HTML Tables. Da dieses
     * nur eindimensional ist, wird nur der width Wert der Dimension in
     * den HTML Code uebernommen.
     */
    /**
     * TODO: documentation
     *
     * @param d
     */
    public void setIntercellPadding(SDimension d) {
        SDimension oldIntercellPadding = intercellPadding;
        intercellPadding = d;
        if ((intercellPadding == null && oldIntercellPadding != null) ||
            intercellPadding != null && !intercellPadding.equals(oldIntercellPadding))
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SDimension getIntercellPadding() {
        return intercellPadding;
    }

    /**
     * TODO: documentation
     *
     * @param tm
     */
    public void setModel(TableModel tm) {
        if (model != null)
            model.removeTableModelListener(this);

        TableModel oldModel = model;
        model = tm;
        if (model == null)
            model = defaultModel;

        model.addTableModelListener(this);

        if ((model == null && oldModel != null) ||
            model != null && !model.equals(oldModel))
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public TableModel getModel() {
        return model;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getColumnCount() {
        return model.getColumnCount();
    }

    /**
     * TODO: documentation
     *
     * @param col
     * @return
     */
    public String getColumnName(int col) {
        return model.getColumnName(col);
    }

    /**
     * TODO: documentation
     *
     * @param col
     * @return
     */
    public Class getColumnClass(int col) {
        return model.getColumnClass(col);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getRowCount() {
        return model.getRowCount();
    }

    /*
     * gehoert eigentlich nicht hier hin, habs aber trotzdem mit
     * aufgenommen, da dann {@link #prepareRenderer} auch bei
     * {@link STable} funktioniert.
     */
    /**
     * TODO: documentation
     *
     * @param row
     * @return
     */
    public boolean isRowSelected(int row) {
        return false;
    }

    public Object getValueAt(int row, int column) {
        return model.getValueAt(row, column);
    }

    public void setValueAt(Object v, int row, int column) {
        model.setValueAt(v, row, column);
    }

    /**
     * TODO: documentation
     *
     * @param r
     */
    public void setDefaultRenderer(STableCellRenderer r) {
        defaultRenderer = r;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public STableCellRenderer getDefaultRenderer() {
        return defaultRenderer;
    }

    public void setDefaultRenderer(Class columnClass, STableCellRenderer r) {
        renderer.remove(columnClass);
        if (renderer != null)
            renderer.put(columnClass, r);
    }

    /**
     * TODO: documentation
     *
     * @param columnClass
     * @return
     */
    public STableCellRenderer getDefaultRenderer(Class columnClass) {
        if (columnClass == null) {
            return defaultRenderer;
        } else {
            Object r = renderer.get(columnClass);
            if (r != null) {
                return (STableCellRenderer)r;
            } else {
                return getDefaultRenderer(columnClass.getSuperclass());
            }
        }
    }

    /**
     * TODO: documentation
     *
     * @param r
     */
    public void setHeaderRenderer(STableCellRenderer r) {
        headerRenderer = r;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public STableCellRenderer getHeaderRenderer() {
        return headerRenderer;
    }

    public STableCellRenderer getCellRenderer(int row, int column) {
        return getDefaultRenderer(getColumnClass(column));
    }

    public SComponent prepareRenderer(STableCellRenderer r, int row, int col) {
        return r.getTableCellRendererComponent(this,
                                               model.getValueAt(row,col),
                                               isRowSelected(row),
                                               row, col);
    }

    /**
     * TODO: documentation
     *
     * @param col
     * @return
     */
    public SComponent prepareHeaderRenderer(int col) {
        return headerRenderer.getTableCellRendererComponent(this,
                                                            model.getColumnName(col),
                                                            false,
                                                            -1, col);
    }


    /**
     * TODO: documentation
     *
     * @param e
     */
    public void tableChanged(TableModelEvent e) {
        reload(ReloadManager.RELOAD_CODE);
    }


    public void getPerformed(String action, String value) {}

    /**
     * TODO: documentation
     *
     * @param b
     */
    public void setBorderLines(Insets b) {
        borderLines = b;
    }
    /**
     * TODO: documentation
     *
     * @return
     */
    public Insets getBorderLines() {
        return borderLines;
    }

    /**
     * Returns the maximum size of this table.
     *
     * @return maximum size
     */
    public Rectangle getScrollableViewportSize() {
        return new Rectangle(0, 0, getColumnCount(), getRowCount());
    }

    /*
     * Setzt den anzuzeigenden Teil
     */
    /**
     * TODO: documentation
     *
     * @param d
     */
    public void setViewportSize(Rectangle d) {
        if ( isDifferent(viewport, d) ) {
            viewport = d;
            reload(ReloadManager.RELOAD_CODE);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Rectangle getViewportSize() {
        return viewport;
    }

    public Dimension getPreferredExtent() {
        return null;
    }


    public void setParent(SContainer p) {
        super.setParent(p);
        if (getCellRendererPane() != null)
            getCellRendererPane().setParent(p);
    }

    protected void setParentFrame(SFrame f) {
        super.setParentFrame(f);
        if (getCellRendererPane() != null)
            getCellRendererPane().setParentFrame(f);
    }


    private SCellRendererPane cellRendererPane = new SCellRendererPane();

    /**
     * TODO: documentation
     *
     * @return
     */
    public SCellRendererPane getCellRendererPane() {
        return cellRendererPane;
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(BaseTableCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
