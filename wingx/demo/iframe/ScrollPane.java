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

package iframe;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import org.wings.*;
import org.wings.externalizer.*;
import org.wings.plaf.*;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wingx.plaf.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ScrollPane
    extends WingServlet
{
    protected void initExternalizer(ServletConfig config) {
        // we want to use the servlet externalizer
        getExternalizeManager().setExternalizer(new ServletExternalizer(config));
    }

    protected void initExtObjectHandler(ServletConfig config) {
        super.initExtObjectHandler(config);
        extManager.addObjectHandler(new DeviceObjectHandler("text/html", "html"));
    }

    public SessionServlet generateSessionServlet(HttpServletRequest req)
        throws Exception
    {
        // create new default session and set plaf
        DefaultSession session = new DefaultSession();
        session.getCGManager().setLookAndFeel(new org.wings.plaf.xhtml.css1.CSS1LookAndFeel());
        //CGDefaults defaults = session.getCGManager().getDefaults();
        //defaults.put("ScrollPaneCG", "scrollpane.IFrameScrollPaneCG");
        // return a new scrollPane session
        return new ScrollPaneSession(session, req);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
