
import org.wings.servlet.SessionServlet;
import org.wings.session.Session;
import org.wings.SContainer;
import org.wings.SLabel;

import javax.servlet.ServletConfig;

/**
 * <h1>HelloWingSSession.java</h1>
 * <p>A very simple <code>SessionServlet</code>.
 * </p><p>Only prints out "<code>Hello WingS!</code>".</p>
 * <p><b>Created 2002, mercatis GmbH</b></p>
 **/
public class HelloWingSSession extends SessionServlet {

    /**
     * <p>creates the GUI of the webapplication</p>
     * @param sess the WingS-Session
     **/
    public HelloWingSSession ( Session sess ) {
	// call super-constructor
	super ( sess );
	System.out.println ( "creating new HelloWingSSession" );
    }

  /**
   * <p>does anything necessary after the initialization of the servlet has run.</p>
   * @param config the servlet configuration
   **/
    public void postInit(ServletConfig config) {
      initGUI();
    }
  

  /**
   * <p>initializes the GUI</p>
   **/
    private void initGUI () {
	// get the pane to put components in
	SContainer pane = getFrame ( ).getContentPane();

	// create and add a label to the pane
	SLabel label = new SLabel ( "Hello WingS!" );
	pane.addComponent( label );

	// note: no frame.show() or frame.setSize(..) needed
    }
    
}// HelloWingSSession
