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
import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.*;

import org.wings.*;
import org.wings.io.Device;
import org.wings.io.ServletDevice;
import org.wings.io.NullDevice;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wings.util.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class WingSetSession
    extends SessionServlet
    implements SConstants
{
    SLabel timeMeasure;

    public WingSetSession(Session session) {
        super(session);
        System.out.println("new WingSetSession");
    }


    public void postInit(ServletConfig config) {
        initGUI();
    }


    void initGUI() {
        timeMeasure = new SLabel();
        timeMeasure.setEscapeSpecialChars(false);
        getFrame().setTitle("WingSet Demo");

        SContainer contentPane = getFrame().getContentPane();
        try {
            java.net.URL templateURL = 
                getClass().getResource("/wingset/templates/ExampleFrame.thtml");
            if( templateURL == null ){
                contentPane.add(new SLabel("Sorry, can't find ExampleFrame.thtml. Are you using a JAR-File?"));
            }
            STemplateLayout layout = new STemplateLayout( templateURL );
            contentPane.setLayout( layout );
        }
        catch ( java.io.IOException except ) {
            except.printStackTrace();
        }
        
        STabbedPane tab = new STabbedPane();
        tab.setMaxTabsPerLine(8);
        tab.setBackground(new java.awt.Color(206, 206, 206));

        tab.add(new WingsImage(), "wingS!");
        tab.add(new LabelExample(), "Label");
        tab.add(new BorderExample(), "Border");
        tab.add(new TextComponentExample(), "Text Component");
         // a Tab with icon..
        tab.addTab("Tree", new ResourceImageIcon("org/wings/icons/JavaCup.gif"), 
                   new TreeExample(), "Tree Tool Tip");
        tab.add(new OptionPaneExample(getFrame()), "OptionPane");
        tab.add(new TableExample(), "Table");
        tab.add(new ListExample(), "List");
        tab.add(new ButtonExample(), "Button");
        tab.add(new CheckBoxExample(), "CheckBox");
        tab.add(new RadioButtonExample(), "RadioButton");
        tab.add(new Faces(), "Faces");
        tab.add(new FileChooserExample(), "FileChooser");
        tab.add(new ScrollPaneExample(), "ScrollPane");
        tab.add(new PageScrollerExample(), "PageScroller");
        //tab.add(new LayoutExample(), "Simple Layout");
        tab.addTab("Template Layout", 
                   new ResourceImageIcon(WingSetSession.class.getClassLoader(),
                                         "wingset/icons/cowSmall.gif"), 
                   new TemplateExample(), "Template Layout Manager");
        //tab.add(new DateChooserExample(), "DateChooser");

        contentPane.add(tab, "WingSetApp");

        contentPane.add(timeMeasure, "TimeLabel");

        /*
         * we don't have old working style. remember to change
         * the templates/ExampleFrame.thtml on change.
        final SRadioButton old = new SRadioButton("xhtml/old");
        old.setSelected(false);
        final SRadioButton css1 = new SRadioButton("xhtml/css1");
        css1.setSelected(true);

        SButtonGroup group = new SButtonGroup();
        group.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    try {
                        if (css1.isSelected())
                            getSession().getCGManager().setLookAndFeel("xhtml/css1");
                        else
                            System.err.println("sorry no xhtml/old");
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace(System.err);
                    }
                }
            });
        group.add(old);
        group.add(css1);

        contentPane.add(old,  "style=old");
        contentPane.add(css1, "style=css1");
        */
    }

    /**
     * this function handles the request from a HTML-Browser
     * nearly all servlet functionality is in here
     */
    public void processRequest(HttpServletRequest req,
                               HttpServletResponse res)
        throws ServletException, IOException
        {
            measure.start("time to generate HTML Code ");
            // then write the data of the response
            /*
             * This is a dummy call to generate the HTML output, just
             * to measure the time
             */
            NullDevice devNull = new NullDevice();
            getFrame().write(devNull);
            measure.stop();
            timeMeasure.setText(measure.print() + 
                                "(" + devNull.getSize() + " Bytes)");
            measure.reset();
        }

    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "WingSet ($Revision$)";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
