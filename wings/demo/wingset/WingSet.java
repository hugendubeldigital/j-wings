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

import java.net.URL;
import java.util.Properties;
import javax.servlet.*;
import javax.servlet.http.*;

import org.wings.*;
import org.wings.externalizer.*;
import org.wings.servlet.*;
import org.wings.session.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class WingSet
    extends WingServlet
{
    protected void initExternalizer(ServletConfig config) {
        // we want to use the servlet externalizer
        getExternalizeManager().setExternalizer(new ServletExternalizer(config));
    }

    public SessionServlet generateSessionServlet(HttpServletRequest req)
        throws Exception
    {
        // create new default session and set plaf
        DefaultSession session = new DefaultSession();
        session.getCGManager().setLookAndFeel(new URL(new URL(HttpUtils.getRequestURL(req).toString()),
                                                      "css1.jar"));
        //Properties properties = new Properties();
        //properties.load(getClass().getResourceAsStream("/org/wings/plaf/xhtml/css1/default.properties"));
        //session.getCGManager().setLookAndFeel(new org.wings.plaf.LookAndFeel(properties));

        // return a new wingset session
        return new WingSetSession(session);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
