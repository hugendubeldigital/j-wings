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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.tree.TreeNode;
import org.wings.SComponent;
import org.wings.SLabel;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.SPanel;


/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class MenuExample extends WingSetPane
{

    private SLabel selectionLabel;

    private final ActionListener menuItemListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectionLabel.setText(((SMenuItem)e.getSource()).getText());
            }
        };

    SMenuItem createMenuItem(TreeNode node) {
        SMenuItem item = new SMenuItem(node.toString());
        item.addActionListener(menuItemListener);
        return item;
    }

    SMenu createMenu(TreeNode root) {
        SMenu menu = new SMenu(root.toString());

        for ( int i=0; i<root.getChildCount(); i++ ) {
            TreeNode node = root.getChildAt(i);
            if ( node.isLeaf() ) {
                menu.add(createMenuItem(node));
            } else {
                menu.add(createMenu(node));
            }
        }

        return menu;
    }

    SMenuBar createMenuBar(TreeNode root) {
        SMenuBar menuBar = new SMenuBar();
        
        for ( int i=0; i<root.getChildCount(); i++ ) {
            TreeNode node = root.getChildAt(i);
            if ( node.isLeaf() ) {
                menuBar.add(createMenuItem(node));
            } else {
                menuBar.add(createMenu(node));
            }
        }

        return menuBar;
    }

    public SComponent createExample() {
        SPanel panel = new SPanel();
        selectionLabel = new SLabel("nothing selected");

        panel.add(createMenuBar(TreeExample.ROOT_NODE), "MenuBar");
        panel.add(selectionLabel, "SelectionLabel");

        return panel;
    }


}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
