package frameset;

import java.io.IOException;
import java.net.URL;

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
public class FrameSet
    extends WingServlet
{
    protected void initExtObjectHandler(ServletConfig config) {
    }

    public SessionServlet generateSessionServlet(HttpServletRequest req)
        throws Exception
    {
        DefaultSession session = new DefaultSession();
        session.getCGManager().setLookAndFeel(new URL(new URL(HttpUtils.getRequestURL(req).toString()),
                                                      "../css1.jar"));
        return new FrameSetSession(session, req);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
