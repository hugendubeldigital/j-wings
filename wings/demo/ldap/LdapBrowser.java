package ldap;

import javax.servlet.*;
import javax.servlet.http.*;
 
import org.wings.*;
import org.wings.externalizer.*;
import org.wings.servlet.*;
import org.wings.session.*;


public class LdapBrowser
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
        session.getCGManager().setLookAndFeel(new org.wings.plaf.xhtml.css1.CSS1LookAndFeel());
 
        // return a new wingset session
        return new LdapBrowserSession(session);        
    }
}                                                                                                                   
