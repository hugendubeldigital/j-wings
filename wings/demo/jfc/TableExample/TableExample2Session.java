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

/**
 *
 */
package jfc.TableExample;

import javax.servlet.*;
import javax.servlet.http.*;

import org.wings.*;
import org.wings.session.*;
import org.wings.servlet.*;

import java.awt.Dimension;

public class TableExample2Session     
extends SessionServlet
{

    public TableExample2Session(Session session, HttpServletRequest req) {
        super(session);
    }

    JDBCAdapter dt = null;

    protected void prepareRequest(HttpServletRequest req,
                                  HttpServletResponse response) {
        String URL = req.getParameter("URL");
        String driver = req.getParameter("driver");
        String user = req.getParameter("user");
        String passwd = req.getParameter("passwd");
        String query = req.getParameter("query");

        try {
          if ( URL!=null && driver!=null && user!=null && passwd!=null &&
               query!=null && dt==null ) {

            dt = new JDBCAdapter(URL, driver, user, passwd);
           
            if ( query!=null )
              dt.executeQuery(query);

            getFrame().getContentPane().removeAll();

            // Create the table
            STable tableView = new STable(dt);
            
            SScrollPane scrollpane = new SScrollPane(tableView);
            
            getFrame().getContentPane().add(scrollpane);
            
          } else {
            if ( query!=null )
              dt.executeQuery(query);
          }
        } catch ( Exception e ) {
          e.printStackTrace();
          dt = null;
          getFrame().getContentPane().removeAll();
          getFrame().getContentPane().add(new SLabel(e.toString()));
        }



    }

    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "TableExample2 ($Revision$)";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
