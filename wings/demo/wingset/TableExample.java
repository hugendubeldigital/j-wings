/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of the wingS demo (http://wings.mercatis.de).
 *
 * The wingS demo is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package wingset;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class TableExample
    extends WingSetPane
{
    public SComponent createExample() {
        // Netscape 4.0 is dumb: it is very slow, so use default layout here.
        setLayout(null);

        SPanel p = new SPanel (new SGridLayout(1));
        
        STable table = new STable(new MyTableModel());
        SLabel label;
        label = new SLabel("<h4>Table outside a form with multiple selection</h4>");
        label.setEscapeSpecialChars(false);
        p.add(label);
        table.setShowGrid(true);
        table.setBorderLines(new Insets(1,1,1,1));
        table.setSelectionMode(MULTIPLE_SELECTION);
        table.setDefaultRenderer(new MyCellRenderer());
        // table.setShowGrid(false);
        p.add(table);

        SForm form = new SForm();
        STable formTable = new STable(new MyTableModel());
        label = new SLabel("<h4>Table inside a form with single selection</h4>");
        label.setEscapeSpecialChars(false);
        form.add(label);
        formTable.setShowGrid(true);
        formTable.setBorderLines(new Insets(1,1,1,1));
        formTable.setSelectionMode(SINGLE_SELECTION);
        formTable.setDefaultRenderer(new MyCellRenderer());
        form.add(formTable);
        form.add(new SButton("SUBMIT"));
        p.add(form);
        return p;
    }


    final static Color[] colors = {
        Color.black,
        Color.cyan,
        Color.yellow,
        Color.magenta,
        Color.orange,
        Color.pink,
        Color.red,
        Color.darkGray,
        Color.gray,
        Color.green,
        Color.lightGray,
        Color.white,
        Color.blue
    };

    static class MyCellRenderer extends SDefaultTableCellRenderer {
        SLabel colorOut = new SLabel();

        public SComponent getTableCellRendererComponent(SBaseTable baseTable,
                                                        Object value,
                                                        boolean selected,
                                                        int row,
                                                        int col) {
            if (value instanceof Color) {
                Color c = (Color) value;
                colorOut.setText("[" + c.getRed() + "," 
                                 + c.getGreen() + "," + c.getBlue() + "]");
                colorOut.setForeground(c);
                return colorOut;
            }
            else
                return super.getTableCellRendererComponent(baseTable,value,
                                                           selected,row,col);
        }
    }

    class MyTableModel extends AbstractTableModel // implements TableSorter
    {
        final int COLS = 7;
        final int ROWS = 5;

        SIcon image = new ResourceImageIcon("org/wings/icons/JavaCup.gif");

        Object[][] data = new Object[ROWS][COLS];

        boolean asc[] = new boolean[COLS];

        MyTableModel() {
            //color.setParent(TableExample.this);
            //image.setParent(TableExample.this);

            for (int c=0; c < COLS; c++) {
                for (int r=0; r < ROWS; r++)
                    data[r][c] = "cell " + r + ":" + c;
            }
            for (int r=0; r < ROWS; r++)
                data[r][2] = createColor(r);
            for (int r=0; r < ROWS; r++)
                data[r][3] = createImage(r);
            for (int r=0; r < ROWS; r++)
                data[r][4] = createBoolean(r);
        }

        public int getColumnCount() {
            return COLS;
        }

        public int getRowCount() {
            return ROWS;
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public void setValueAt(Object value, int row, int col) {
            if (getColumnClass(col).isAssignableFrom(String.class))
                data[row][col] = value.toString();
            else if (getColumnClass(col).isAssignableFrom(Boolean.class))
                data[row][col] = new Boolean(((Boolean)value).booleanValue());
        }

        public Class getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }

        public Color createColor(int row) {
            return colors[row % colors.length];
        }

        public Object createImage(int row) {
            return image;
        }

        public Boolean createBoolean(int row) {
            if (row % 2 == 1)
                return new Boolean(false);
            else
                return new Boolean(true);
        }

        public void sort(int col, boolean ascending) {
            System.out.println("sort");
            if ( col<asc.length )
                asc[col] = !ascending;
        }

        public boolean isCellEditable(int row, int col) {
            if (getColumnClass(col).isAssignableFrom(String.class) ||
                getColumnClass(col).isAssignableFrom(Boolean.class))
                return true;
            else
                return false;
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
