package skeleton;
 
import org.wings.servlet.SessionServlet;
import org.wings.session.Session;
import org.wings.SContainer;
import org.wings.SLabel;

import javax.servlet.ServletConfig;

/**
 * <h1>WingSSkeletonSession.java</h1>
 * <p>A very simple <code>SessionServlet</code>.</p>
 * <p><b>Created 2002, mercatis GmbH</b></p>
 **/
public class WingSSkeletonSession extends SessionServlet {

    /**
     * <p>creates the GUI of the webapplication</p>
     * @param sess the WingS-Session
     **/
    public WingSSkeletonSession ( Session sess ) {
	// call super-constructor
	super ( sess );
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
	SLabel label = new SLabel ( "Add as you like..." );
	pane.addComponent( label );

	// note: no frame.show() or frame.setSize(..) needed
    }
    
}// WingSSkeletonSession







