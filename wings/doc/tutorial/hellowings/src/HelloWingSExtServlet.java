import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.wings.servlet.SessionServlet;
import org.wings.servlet.WingServlet;

import org.wings.session.SessionManager;
import org.wings.session.DefaultSession;
import org.wings.session.Session;

import org.wings.plaf.LookAndFeelFactory;

/**
 * <em>HelloWingSExtServlet.java</em>
 * <p>Creates very simple session servlets.</p>
 * <b><p>Created 2002, mercatis GmbH</p></b>
 **/
public class HelloWingSExtServlet extends WingServlet {

    /**
     * <p>generates a new <code>SessionServlet</code></p>
     * @param req the request from the browser
     * @return a newly created <code>SessionServlet</code>
     **/
    public SessionServlet generateSessionServlet( HttpServletRequest req ) 
	throws Exception {

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

	// create a new session to handle sessions
	Session session = new DefaultSession();
	
	// setting the plaf to the session
        session.getCGManager().setLookAndFeel("xhtml/css1");

	// tell the sessionmanager that we have a session
	SessionManager.setSession( session );

	// return a new HelloWingSSession
	return new HelloWingSExtSession ( SessionManager.getSession() );
    }

}// HelloWingSExtServlet





