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

package wingset;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ListExample
    extends SPanel
{
    public ListExample() {
        super(new SFlowDownLayout());
        SForm form = new SForm(new SFlowDownLayout());

        SPanel p = new SPanel(new SGridLayout(2,2));
        p.add(createListSingleSelExample());
        p.add(createListMultSelExample());
        p.add(createComboBoxExample());
        p.add(createAnchorListExample());

        form.add(p);
        form.add(new SButton("SUBMIT"));
        add(form);

        add(new SSeparator());

        SHRef href =  new SHRef("View Source Code");
        href.setReference("http://www.mercatis.de/~armin/WingSet/" +
                          getClass().getName() + ".java");
        add(href);
    }

    public SContainer createListSingleSelExample() {
        SContainer cont = new SContainer(new SFlowDownLayout());
        cont.add(new SLabel("List with single selection"));
        SList list = new SList();
        list.setSelectionMode(SINGLE_SELECTION);
        addListElements(list);
        cont.add(list);

        return cont;
    }

    public SContainer createListMultSelExample() {
        SContainer cont = new SContainer(new SFlowDownLayout());
        cont.add(new SLabel("List with multiple selection"));
        SList list = new SList();
        list.setSelectionMode(MULTIPLE_SELECTION);
        addListElements(list);
        cont.add(list);

        return cont;
    }

    public SContainer createComboBoxExample() {
        SContainer cont = new SContainer(new SFlowDownLayout());
        cont.add(new SLabel("ComboBox"));
        SComboBox comboBox = new SComboBox();
        addComboBoxElements(comboBox);
        cont.add(comboBox);
        
        return cont;
    }

    public SContainer createAnchorListExample() {
        SContainer cont = new SContainer(new SFlowDownLayout());
        cont.add(new SLabel("List with showAsFormComponent = false"));
        SList list = new SList();
        list.setShowAsFormComponent(false);
        list.setSelectionMode(SConstants.SINGLE_SELECTION);
        addAnchorElements(list);
        cont.add(list);

        return cont;
    }

    public void addListElements(SList list) {
        SImage img = new SImage(SUtil.makeIcon(SLabel.class, "icons/JavaCup.gif"));
        SLabel color = new SLabel("");
        color.setForeground(Color.green);
        color.setText(Color.green.toString());

        Object[] values = {
            "element1",
            color,
            "element3",
            "element4"
        };

        list.setListData(values);
    }

    public void addComboBoxElements(SComboBox comboBox) {
        SImage img = new SImage(SUtil.makeIcon(SLabel.class, "icons/JavaCup.gif"));
        SLabel color = new SLabel("");
        color.setForeground(Color.green);
        color.setText(Color.green.toString());

        Object[] values = {
            "element1",
            color,
            "element3",
            "element4"
        };

        comboBox.setModel(new DefaultComboBoxModel(values));
    }

    public void addAnchorElements(SList list) {
        final SImage img = new SImage(SUtil.makeIcon(SLabel.class, "icons/JavaCup.gif"));

        final SLabel color = new SLabel("");
        color.setForeground(Color.green);
        color.setText(Color.green.toString());

        ListModel listModel = new ListModel() {
            Object[] values = {
                "element1",
                color,
                img,
                "element4"
            };

            public int getSize() {
                return values.length;
            }
            public Object getElementAt(int i) {
                return values[i];
            }
            public void addListDataListener(ListDataListener l) {
            }
            public void removeListDataListener(ListDataListener l) {
            }
        };

        list.setModel(listModel);
        list.setType(ORDER_TYPE_NORMAL);
        // list.setStart(8);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
