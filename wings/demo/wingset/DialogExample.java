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

import java.awt.event.*;

import org.wings.*;
import org.wings.event.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public class DialogExample
    extends SForm
    implements SConstants, SWindowListener
{
    public DialogExample(SFrame f) {
        add(new SSpacer(1, VERTICAL));
        
        final DialogExample dexample = this;
        
        final SFrame frame = f;
        SButton msg = new SButton("show Dialog with Message");
        msg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SDialog dialog = new SDialog();
                dialog.setTitle("A Message");
                dialog.getContentPane().setLayout(new SFlowLayout());
                dialog.getContentPane().add(new SLabel("This is a simple message"));
                dialog.addWindowListener(dexample);
                dialog.show(frame);
            } });
        add(msg);

        SButton cmplx = new SButton("Show complex dialog");
        cmplx.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final SDialog dialog = new SDialog();
                dialog.setTitle("Tree in dialog");
                
                SButton closeb = new SButton("Close dialog");
                closeb.setHorizontalAlignment(SConstants.CENTER_ALIGN);
                closeb.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent ae) {
                        dialog.hide();
                    }
                });

                SForm form = new SForm(new SBorderLayout());
                form.add(new SLabel("Tree view in dialog"), "North");
                form.add(new TreeExample().createExample() , "Center");
                form.add(closeb, "South");

                dialog.getContentPane().add(form);
                dialog.addWindowListener(dexample);
                dialog.show(frame); 
            } });
        add(cmplx);

        add(new SLabel("<br />"));
        add(new SSeparator());

        SHRef href =  new SHRef("View Source Code");
        href.setReference("/demo/wingset/" +
                          getClass().getName().substring(getClass().getName().indexOf('.') +1) + ".java");
        add(href);
    }
    
    public void windowActivated(SWindowEvent e) {}
    public void windowClosed(SWindowEvent e)
     {
        System.out.println("DialogExample: Dialog closed!");
     }
    public void windowClosing(SWindowEvent e) {}
    public void windowDeactivated(SWindowEvent e) {}
    public void windowDeiconified(SWindowEvent e) {}
    public void windowIconified(SWindowEvent e) {}
    public void windowOpened(SWindowEvent e) {}
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
