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
public class ScrollPaneExample
    extends SPanel
{
    SBaseTable table = new SBaseTable(new MyTableModel());

    public ScrollPaneExample() {
        super(new SFlowDownLayout());

        add(new SSeparator());
        add(new SLabel("<h4>Table in a ScrollPane</h4>"));

        // table.setSelectionMode(MULTIPLE_SELECTION);
        // table.setShowGrid(false);
        SScrollPane scroller = new SScrollPane(table);
        add(scroller);

        add(new SSeparator());

        SHRef href =  new SHRef("View Source Code");
        href.setReference("/demo/wingset/" +
                          getClass().getName().substring(getClass().getName().indexOf('.') +1) + ".java");
        add(href);
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


    class MyTableModel extends AbstractTableModel implements TableSorter
    {
        final int COLS = 15;
        final int ROWS = 15;

        SImage image =
            new SImage(new ResourceImageIcon(SLabel.class, "icons/JavaCup.gif"));

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

        public SLabel createColor(int row) {
            SLabel color = new SLabel("");
            color.setForeground(colors[row % colors.length]);
            color.setText(colors[row % colors.length].toString());
            return color;
        }

        public SComponent createImage(int row) {
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
 * End:
 */
