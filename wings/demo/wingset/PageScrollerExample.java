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
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class PageScrollerExample
    extends WingSetPane
{

    SList list;
    SPageScroller scrollbar;

    public SComponent createExample() {
        SPanel p = new SPanel(new SBorderLayout());

        /*        SLabel label = new SLabel("<h4>List in a PageScroller</h4>");
        label.setEscapeSpecialChars(false);
        p.add(label, SBorderLayout.NORTH);
        */

        list = new SList(createData());
        list.setVisibleRowCount(8);

        scrollbar = new SPageScroller(Adjustable.VERTICAL);

        // its a horizontal scrollbar, but scrolls vertical...
        scrollbar.setLayoutMode(Adjustable.HORIZONTAL);
        scrollbar.setDirectPages(10);

        SScrollPane scroller = new SScrollPane(list);
        scroller.setHorizontalScrollBar(scrollbar);
        scroller.setVerticalScrollBar(null);
        // maximum of 50 visible rows...
        scroller.setVerticalExtent(50);

        p.add(scroller, SBorderLayout.CENTER);

        p.add(createControlForm(), SBorderLayout.NORTH);

        return p;
    }

    private SForm createControlForm() {
        SForm controlForm = new SForm(new SFlowLayout());
        
        /*
         * modify the displayed indentation depth.
         */
        controlForm.add(new SLabel("Visible Rows: "));
        Object[] visRowsValues = {new Integer(4), new Integer(8), new Integer(12), 
                           new Integer(16), new Integer(20), new Integer(50)};
        final SComboBox visRows = new SComboBox(visRowsValues);
        visRows.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    list.setVisibleRowCount(((Integer)visRows.getSelectedItem()).intValue());
                }
            });
        visRows.setSelectedItem(new Integer(list.getVisibleRowCount()));
        controlForm.add(visRows);

        /*
         * modify the displayed indentation depth.
         */
        controlForm.add(new SLabel("Direct Pages: "));
        Object[] values = {new Integer(5), new Integer(10), new Integer(15), 
                           new Integer(20), new Integer(50)};
        final SComboBox comboBox = new SComboBox(values);
        comboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    scrollbar.setDirectPages(((Integer) comboBox.getSelectedItem()).intValue());
                }
            });
        comboBox.setSelectedItem(new Integer(scrollbar.getDirectPages()));
        controlForm.add(comboBox);
        
        SButton submit = new SButton("OK");
        controlForm.add(submit);
        return controlForm;
    }

    void addChildNodes(TreeNode node, ArrayList list, int indent) {
        if ( node!=null ) {
            StringBuffer name = new StringBuffer();
            for ( int i=0; i<indent; i++ ) {
                name.append(".");
            }
            name.append(node.toString());
            list.add(name.toString());
            for ( int i=0; i<node.getChildCount(); i++ ) {
                addChildNodes(node.getChildAt(i), list, indent+1);
            }
        }
    }


    Object[] createData() {
        TreeNode root = TreeExample.generateTree();

        ArrayList data = new ArrayList();
        
        addChildNodes(root, data, 0);

        return data.toArray();
    }


}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */


