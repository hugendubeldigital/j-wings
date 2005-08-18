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
package wingset;

import org.wings.*;
import org.wings.plaf.MenuBarCG;
import org.wings.plaf.MenuCG;
import org.wings.plaf.MenuItemCG;

import javax.swing.*;
import javax.swing.tree.TreeNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class MenuExample extends WingSetPane {

    private SLabel selectionLabel;
    private SMenuBar menuBar;
    private int shortcutKey = java.awt.event.KeyEvent.VK_A;

    private final ActionListener menuItemListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            selectionLabel.setText(((SMenuItem) e.getSource()).getText());
        }
    };

    SMenuItem createMenuItem(TreeNode node) {
        SMenuItem item = new SMenuItem(node.toString());
        item.setToolTipText(node.toString());
        item.addActionListener(menuItemListener);
        if (shortcutKey != 0) {
            item.setAccelerator(KeyStroke.getKeyStroke(shortcutKey,
                    java.awt.Event.CTRL_MASK));
            if (shortcutKey == java.awt.event.KeyEvent.VK_Z) {
                shortcutKey = 0;
            } else {
                shortcutKey++;
            }
        }
        return item;
    }

    SMenu createMenu(TreeNode root) {
        SMenu menu = new SMenu(root.toString());
        menu.addActionListener(menuItemListener);

        for (int i = 0; i < root.getChildCount(); i++) {
            TreeNode node = root.getChildAt(i);
            if (node.isLeaf()) {
                menu.add(createMenuItem(node));
            } else {
                menu.add(createMenu(node));
            }
        }

        return menu;
    }

    SMenuBar createMenuBar(TreeNode root) {
        SMenuBar menuBar = new SMenuBar();

        for (int i = 0; i < root.getChildCount(); i++) {
            TreeNode node = root.getChildAt(i);
            if (node.isLeaf()) {
                menuBar.add(createMenuItem(node));
            } else {
                menuBar.add(createMenu(node));
            }
        }

        return menuBar;
    }

    public SComponent createExample() {
        ComponentControls controls = new ComponentControls();
        SPanel superPanel = new SPanel(new SBoxLayout(SBoxLayout.VERTICAL));
        superPanel.setPreferredSize(SDimension.FULLWIDTH);
        menuBar = createMenuBar(HugeTreeModel.ROOT_NODE);
        controls.addSizable(menuBar);
        superPanel.add(controls);
        superPanel.add(menuBar);
        
        SPanel mainPanel = new SPanel(new SBoxLayout(SBoxLayout.HORIZONTAL));
        SForm formPanel = new SForm();
        formPanel.setLayout(new SBoxLayout(SBoxLayout.VERTICAL));
        formPanel.setPreferredSize(SDimension.FULLWIDTH);
        mainPanel.add(formPanel);
        SPanel messagePanel = new SPanel(new SBoxLayout(SBoxLayout.VERTICAL));
        mainPanel.add(messagePanel);
        messagePanel.add(new SLabel("<html><br>Form components are overlayed or hidden (in IE). Selected Menu: "));
        selectionLabel = new SLabel("nothing selected");
        messagePanel.add(selectionLabel, "SelectionLabel");
        messagePanel.add(new SLabel("<html><br>Try the menu accelerator keys. Ctrl-A to Ctrl-Z call menuitem actions (doesn't work on Konqueror)"));
        
        
        formPanel.add(new SLabel("<html><hr>combobox :"));
        formPanel.add(new SComboBox(new DefaultComboBoxModel(ListExample.createElements())));
        formPanel.add(new SLabel("<html><br>list:"));
        SList list = new SList(ListExample.createListModel());
        list.setVisibleRowCount(3);
        formPanel.add(list, "List");
        formPanel.add(new SLabel("<html><br>textfield(stay visible):"));
        formPanel.add(new STextField("wingS is great"));
        formPanel.add(new SLabel("<html><br>textarea(stay visible):"));
        formPanel.add(new STextArea("wingS is a great framework for implementing complex web applications"));

        // enable / disable some menuitems
        final SCheckBox switchEnable = new SCheckBox("disable some menuitems");
        controls.add(switchEnable);
        
        switchEnable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMenuItemsEnabled(!switchEnable.isSelected());
            }
        });
        
        superPanel.add(mainPanel);

        return superPanel;
    }

    protected void setMenuItemsEnabled(boolean enabled) {
        if (menuBar.getComponentCount() > 1) {
            SMenuItem first = (SMenuItem)menuBar.getComponent(0);
            SMenuItem last = (SMenuItem)menuBar.getComponent(menuBar.getComponentCount() - 1);
            recursiveMenuItemSwitch(first, last, enabled);
        } else if (menuBar.getComponentCount() == 1) {
            ((SMenuItem)menuBar.getComponent(0)).setEnabled(enabled);
        }
    }

    private void recursiveMenuItemSwitch(SMenuItem first, SMenuItem last, boolean enabled) {
        last.setEnabled(enabled);
        if (first instanceof SMenu) {
            if (((SMenu)first).getChildrenCount() > 1) {
                SMenu parent = (SMenu) first;
                SMenuItem firstChild = (SMenuItem)parent.getChild(0);
                SMenuItem lastChild = (SMenuItem)parent.getChild(parent.getChildrenCount() - 1);
                recursiveMenuItemSwitch(firstChild, lastChild, enabled);
            } else if (((SMenu)first).getChildrenCount() == 1) {
                ((SMenuItem)((SMenu)first).getChild(0)).setEnabled(enabled);
            }
        }
    }
}


