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

import org.wings.plaf.*;
import org.wings.io.Device;

/*
 * Basis Tabelle in HTML. Hier wird ein wenig vom Swing Konzept
 * abgewichen. Eine Basis Tabelle ist eine simple Tabelle, die
 * keinerlei Besonderheiten zur normalen HTML Tabelle
 * aufweist. Diese Tabelle wird immer dann benutzt, falls keine
 * Selektionen noetig sind und die Tabelle nur zu formatier Zwecken
 * benutzt wird. Die weiterfuehrende Funktionalitaet (Selektion) ist
 * in {@link STable} implementiert. Trotz allem sind die meisten
 * API Aufrufe Swing nachempfunden.
 */
/**
 * TODO: documentation
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

    /*
     * Der default renderer wird verwendet, wenn für die Klasse
     * eines Zellinhalts kein Renderer gesetzt ist
     */
    protected STableCellRenderer defaultRenderer;

    /*
     * Der header renderer wird für die header-Zeile verwendet.
     */
    protected STableCellRenderer headerRenderer;

    /*
     * Das default model, welches gesetzt wird, falls zufaellig ;-) mal
     * ein null Wert gesetzt wird.
     */
    /**
     * TODO: documentation
     */
    protected final static TableModel defaultModel = new DefaultTableModel();

    /**
     * TODO: documentation
     */
    protected TableModel model = null;

    /*
     * Die Renderer fuer die verschiedenen Klassen von Cell Inhalten.
     * Key ist Klasse, value ist Renderer.
     */
    /**
     * TODO: documentation
     */
    protected final HashMap renderer = new HashMap();

    /*
     * Vorerst mal ist der Header noch nicht wie im Swing
     * implementiert. Ich koennte mir aber vorstellen, dass das schon
     * Sinn macht. Seis wies ist, hiermit kann man setzen, ob der Header
     * sichtbar sein soll oder nicht.
     */
    /**
     * TODO: documentation
     */
    protected boolean headerVisible = true;

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
    protected SDimension intercellSpacing = null;

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

    /**
     * TODO: documentation
     *
     * @param hv
     */
    public void setHeaderVisible(boolean hv) {
        boolean oldHeaderVisible = headerVisible;
        headerVisible = hv;
        if (oldHeaderVisible != headerVisible)
            reload();
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
            reload();
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
            reload();
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
            reload();
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
            reload();
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
            reload();
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
     * Set the table model used to handle the data
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
            reload();
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
        reload();
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
    public Dimension getScrollableViewportSize() {
        return new Dimension(getColumnCount(), getRowCount());
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
        Rectangle oldViewport = viewport;
        viewport = d;
        if ((viewport == null && oldViewport != null) ||
            viewport != null && !viewport.equals(oldViewport))
            reload();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Rectangle getViewportSize() {
        return viewport;
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

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "BaseTableCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
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
 * End:
 */
