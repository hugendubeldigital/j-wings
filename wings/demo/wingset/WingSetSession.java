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

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.wings.util.*;
import org.wings.*;
import org.wings.servlet.*;
import org.wings.session.*;

import org.wings.io.Device;
import org.wings.io.ServletDevice;

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
    SLabel timeMeasure = new SLabel();

    public WingSetSession(Session session) {
        super(session);
        System.out.println("I`m starting now");
    }

    /**
     * init static variables and DB connetion
     */
    public void init(ServletConfig config)
        throws ServletException
    {
        super.init( config );
        initGUI();
    }


    void initGUI() {
        frame.getContentPane().setLayout(new SFlowDownLayout());
        //frame.setBackgroundURL("");

        STabbedPane tab = new STabbedPane();

        tab.add(new LabelExample(), "Label Example");
        tab.add(new TreeExample(), "Tree Example");
        tab.add(new OptionPaneExample(frame), "OptionPane Example");
        tab.add(new TableExample(), "Table Example");
        tab.add(new ListExample(), "List Example");
        tab.add(new ButtonExample(), "Button Example");
        tab.add(new CheckBoxExample(), "CheckBox Example");
        tab.add(new RadioButtonExample(), "RadioButton Example");
        tab.add(new FileChooserExample(), "FileChooser Example");
        tab.add(new ScrollPaneExample(), "ScrollPane Example");
        tab.add(new TemplateExample(), "Template Example");
        tab.add(new DateChooserExample(), "DateChooser Example");

        frame.getContentPane().add(tab);

        frame.getContentPane().add(new SSeparator());
        frame.getContentPane().add(timeMeasure);
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
            // Dies ist ein Dummy-Aufruf, um die Zeit zu messen; es wird
            // sozusagen die Seite zweimal aufgebaut, denn die Ausgabe der
            // Zeit ist ja selbst wieder eine Component.
            String erg = frame.show();
            measure.stop();
            timeMeasure.setText(measure.print());
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
 * End:
 */
