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

package ldap;

import javax.servlet.*;
import javax.servlet.http.*;
 
import org.wings.*;
import org.wings.externalizer.*;
import org.wings.servlet.*;
import org.wings.session.*;


public class LdapClient
    extends WingServlet
{
    public SessionServlet generateSessionServlet(HttpServletRequest req)
        throws Exception
    {
        // create new default session and set plaf
        DefaultSession session = new DefaultSession();
        session.getCGManager().setLookAndFeel(new org.wings.plaf.xhtml.css1.CSS1LookAndFeel());
 
        // return a new wingset session
        return new LdapClientSession(session);        
    }

    /** @link dependency 
     * @stereotype instantiate*/
    /*#LdapClientSession lnkLdapClientSession;*/
}                                                                                                                   

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
