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

import java.awt.event.*;
import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public class TextComponentExample
    extends SPanel
    implements SConstants
{
    public TextComponentExample() {
        add(createTextComponentExample(new STextField()));
        add(new SSeparator());

        add(createTextComponentExample(new STextArea()));
        add(new SSeparator());

        SHRef href =  new SHRef("View Source Code");
        href.setReference("/demo/wingset/" +
                          getClass().getName().substring(getClass().getName().indexOf('.') +1) + ".java");
        add(href);
    }

    SForm createTextComponentExample(STextComponent textComp) {
        SForm f = new SForm();

        SGridLayout layout = new SGridLayout(2);
        f.setLayout(layout);

        SLabel desc = new SLabel("Enter some text: ");
        SLabel resultDesc = new SLabel("Entered text:");
        final SLabel result = new SLabel("");
        final SLabel buttonDesc = new SLabel("");

        textComp.addTextListener(new TextListener() {
            public void textValueChanged(TextEvent evt) {
                result.setText(((STextComponent)evt.getSource()).getText());
                buttonDesc.setText("button not pressed");
            } } );

        SButton button = new SButton("Submit");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonDesc.setText("button pressed");
            } } );

        f.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonDesc.setText(buttonDesc.getText() + " + form event");
            } } );

        f.add(desc);
        f.add(textComp);
        f.add(resultDesc);
        f.add(result);
        f.add(button);
        f.add(buttonDesc);

        return f;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
