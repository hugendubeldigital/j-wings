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

package ide;

import java.net.URL;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.wings.*;
import org.wings.externalizer.*;
import org.wings.plaf.*;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wings.style.*;

import org.wingx.beans.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class Ide
    extends WingServlet
{
    static {
        SPropertyEditorManager.registerEditor(Style.class, ide.editors.StyleEditor.class);
        SPropertyEditorManager.registerEditor(AttributeSet.class, ide.editors.AttributeSetEditor.class);
    }
    public SessionServlet generateSessionServlet(HttpServletRequest req)
        throws Exception
    {
        DefaultSession session = new DefaultSession();
        if (!LookAndFeelFactory.isDeployed("xhtml/css1")) {
            try {
                URL url = servletConfig.getServletContext().getResource("/css1.jar");
                LookAndFeelFactory.deploy(url);
            }
            catch (Exception e) {
                logger.log(Level.SEVERE, "cannot deploy lookandfeel", e);
            }
        }
        session.getCGManager().setLookAndFeel("xhtml/css1");
 
        // return a new ide session
        return new IdeSession(session);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
