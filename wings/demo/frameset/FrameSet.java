package frameset;

import java.io.IOException;
import java.net.URL;

import javax.servlet.*;
import javax.servlet.http.*;

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
public class FrameSet
    extends WingServlet
{
    protected void initExtObjectHandler(ServletConfig config) {
    }

    public SessionServlet generateSessionServlet(HttpServletRequest req)
        throws Exception
    {
        DefaultSession session = new DefaultSession();
        if (!LookAndFeelFactory.isDeployed("xhtml/css1")) {
            try {
                URL url = servletConfig.getServletContext().getResource("css1.jar");
                LookAndFeelFactory.deploy(url);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
            }
        }
        session.getCGManager().setLookAndFeel("xhtml/css1");

        return new FrameSetSession(session, req);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
