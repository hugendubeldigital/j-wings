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

import java.awt.Color;
import java.awt.event.*;
import org.wings.*;

import org.wings.script.JavaScriptListener;
import org.wings.script.JavaScriptEvent;

/**
 * Create some text fields and add two listeners: a standard server side
 * listener, that does a numerical calculation on the fields and a client
 * side JavaScript Listener that allows to calculate these fields as well.
 *
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public class JavaScriptListenerExample
    extends WingSetPane
{
    private final static String JS_ADD_SCRIPT 
        = "{2}.value = ((1.0 * {0}.value) + (1.0 * {1}.value));";

    public SComponent createExample() {
        SPanel p = new SPanel();
        p.add(new SLabel("The client side can handle simple events by JavaScript listeners. In this example, numbers are added locally."));

        final STextField firstField  = createNumberField();
        final STextField secondField = createNumberField();
        final STextField sumField    = createNumberField();
        SButton serverCalcButton = new SButton("sum");

        firstField.setFocusTraversalIndex(1);
        secondField.setFocusTraversalIndex(2);

        SForm form = new SForm(new SGridLayout(2));
        form.add(new SLabel("Value #1"));
        form.add(firstField);
        form.add(new SLabel("Value #2"));
        form.add(secondField);
        form.add(new SLabel("Sum"));
        form.add(sumField);
        form.add(new SLabel("Server calculation"));
        form.add(serverCalcButton);

        /*
         * The server side listener
         */
        serverCalcButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    doCalculation(firstField, secondField, sumField);
                }
            });

        /*
         * add the client side script listener. The variables 
         * in curly braces are replaced by the actual IDs of the components.
         */
        SComponent[] jsParams = new SComponent[]{ firstField, secondField,
                                                  sumField};
        JavaScriptListener jsListener;
        jsListener = new JavaScriptListener(JavaScriptEvent.ON_CHANGE,
                                            JS_ADD_SCRIPT,
                                            jsParams);
        
        firstField.addScriptListener(jsListener);
        secondField.addScriptListener(jsListener);

        // any change to the sum field: no way, recalculate from source fields
        sumField.addScriptListener(jsListener);

        p.add(form);
        return p;
    }

    private void doCalculation(STextField a, STextField b, STextField sum) {
        double aNum = parseNumber(a);
        double bNum = parseNumber(b);
        if (Double.isNaN(aNum) || Double.isNaN(bNum)) {
            sum.setBackground(Color.RED);
            sum.setText("?");
        }
        else {
            sum.setBackground(null);
            sum.setText(String.valueOf(aNum + bNum));
        }
    }

    /**
     * parse a number in a text field. Assume an empty text field
     * to be '0', non-parseable values are NaN.
     */
    private double parseNumber(STextField field) {
        String text = field.getText().trim();
        if (text.length() == 0) {
            text = "0";
        }
        double result = Double.NaN;
        try {
            result = Double.parseDouble(text);
            field.setText(String.valueOf(result)); // normalize output
            field.setBackground(null);
        }
        catch (Exception e) {
            field.setBackground(Color.RED);
        }
        return result;
    }

    private STextField createNumberField() {
        STextField field = new STextField();
        field.setAttribute("text-align", "right");
        return field;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
