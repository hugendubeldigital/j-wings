package de.mercatis.hellowings;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.wings.session.Session;

import org.wings.servlet.SessionServlet;
import org.wings.servlet.WingServlet;

import org.wings.plaf.LookAndFeelFactory;

/**
 * WingSTutorialServlet.java
 *
 *
 * Created: Wed Feb 20 16:55:13 2002
 *
 * @author Joachim Karrer
 * @version $Revision$
 */

public class WingSTutorialServlet extends WingServlet {
    /**
     * <p>generates a real new SessionServlet</p>
     **/
    public SessionServlet generateSessionServlet( HttpServletRequest req ) 
	throws Exception {
        // create new default session and set plaf
        Session session = new org.wings.session.DefaultSession();

	// load plaf and set it for the factory, if necessary
        if (!LookAndFeelFactory.isDeployed("xhtml/css1")) {
            try {
                URL url =
                    // bug in tomcat 4.0.2 and 4.0.3, getResource returns jndi
                    // URL, which ist not a supported protocol (maybe they
                    // forgot the URLStreamHandler...)
                    // servletConfig.getServletContext().getResource("/css1.jar");
                    new URL("file:" + 
                            servletConfig.getServletContext().getRealPath("/css1.jar"));
                LookAndFeelFactory.deploy(url);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
            }
        }
	
	// setting the plaf to the session
        session.getCGManager().setLookAndFeel("xhtml/css1");

	// return a new SessionServlet
	return new WingSTutorialSessionServlet ( session );
    }

}// WingSTutorialServlet




