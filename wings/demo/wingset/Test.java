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

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.swing.*;

import org.wings.util.*;
import org.wings.*;
import org.wings.externalizer.*;
import org.wings.plaf.*;
import org.wings.servlet.*;
import org.wings.session.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class Test
    extends SessionServlet
    implements SConstants
{
    public Test() {
        super(new DefaultSession());
        System.out.println("Test servlet's starting now");
        System.err.println("Test servlet's starting now");
    }


    public void preInit(ServletConfig config) {
        DefaultSession session = (DefaultSession)getSession();
        try {
            CGManager cgManager = session.getCGManager();
            cgManager.setLookAndFeel(new org.wings.plaf.xhtml.old.OldLookAndFeel());
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void postInit(ServletConfig config) {
        initGUI();
    }

    void initGUI() {
        SContainer contentPane = getFrame().getContentPane();

        SLabel label = new SLabel("<font face=\"helvetica\">" +
                                  "<font color=\"#990000\">wingS i</font>" +
                                  "<font color=\"#000099\">s </font>" +
                                  "<font color=\"#990000\">n</font>" +
                                  "<font color=\"#000099\">ext </font>" +
                                  "<font color=\"#990000\">g</font>" +
                                  "<font color=\"#000099\">eneration </font>" +
                                  "<font color=\"#990000\">S</font>" +
                                  "<font color=\"#000099\">wing</font>" +
                                  "</font>");
        contentPane.add(label);
    }

    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "Test Servlet ($Revision$)";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
