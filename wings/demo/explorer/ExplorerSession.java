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
 * @author Holger Engels
 * @author Andreas Gruener
 * @author Armin Haaf
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
        super.postInit( config );

        String dir = config.getInitParameter("ExplorerBaseDir");

	ExplorerPanel mainPanel = new ExplorerPanel(dir);

	// übergeordneter SFrame (Browser-Fenster)
        getFrame().getContentPane().add(mainPanel);
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
