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

package explorer;

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
public class ExplorerSession
    extends SessionServlet
{
    private ExplorerPanel mainPanel;

    public ExplorerSession(Session session, HttpServletRequest req) {
        super(session);
    }

    public void postInit(ServletConfig config) throws ServletException
    {
        String dir = config.getInitParameter("ExploreBaseDir");
        if (dir == null)
            mainPanel = new ExplorerPanel(new String("."));
        else
            mainPanel = new ExplorerPanel(dir);

        initGUI();
    }

    void initGUI() {
        frame.getContentPane().add(mainPanel, SBorderLayout.CENTER);
    }

    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "Explorer ($Revision$)";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
