package de.mercatis.hellowings;

import org.wings.servlet.SessionServlet;
import org.wings.SLabel;
import org.wings.STemplateLayout;
import org.wings.SPanel;
import org.wings.SButton;
import org.wings.SForm;
import org.wings.SCardLayout;
import org.wings.STabbedPane;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import org.wings.session.Session;
import java.io.IOException;
import java.io.File;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Properties;
/**
 * HelloWingsSessionServlet.java
 *
 *
 * Created: Wed Feb 20 17:01:45 2002
 *
 * @author Joachim Karrer
 * @version $Revision$
 */
public class WingSTutorialSessionServlet extends SessionServlet {


    final protected STabbedPane tab = new STabbedPane ();
    final protected SButton prevpage = new SButton ( "previous step" );
    final protected SButton nextpage = new SButton ( "next step" );

    private long configFileDate = 0;

    private String configFileName = null;

    public WingSTutorialSessionServlet ( Session sess )  
	throws ServletException, IOException {
	super ( sess );
	System.out.println("new WingSetSession");
    }

    public void postInit(ServletConfig config) {
      initGUI( config );

    }

    private void initGUI ( ServletConfig config ) {
	String templatefile = config.getInitParameter ( "slides.template" );
        String mainframetemplatefile =
	    getServletContext().getRealPath ( templatefile );  
	log ( "Template file is '"+mainframetemplatefile+"'");

	String slidesconfig = config.getInitParameter ( "slides.config" );
	String slidesconfigfile =
	    getServletContext().getRealPath ( slidesconfig );
	log ( "Slides configuration file is '"+slidesconfigfile+"'");
	configFileName = slidesconfigfile;

	SPanel p = new SPanel (  );
	try {
	  STemplateLayout framelayout = 
	    new STemplateLayout ( mainframetemplatefile );
	  p.setLayout (framelayout);
	} catch ( IOException ioex ) {
	  log ( "Template couldn't be loaded", ioex );
	}

        log ( "template initialized" );

	p.setBackground ( new java.awt.Color (0x2020FF) );

	SPanel menu = new SPanel (  );
    
	p.add ( menu, "menupanel" );

	nextpage.addActionListener ( 
	    new ActionListener () {
		    public void actionPerformed ( ActionEvent ae ) {
			if ( tab.getSelectedIndex()+1 < tab.getTabCount() )
			    tab.setSelectedIndex ( tab.getSelectedIndex()+1 );
		    }
		} );
	prevpage.addActionListener ( 
	    new ActionListener () {
		    public void actionPerformed ( ActionEvent ae ) {
			if ( tab.getSelectedIndex()-1 >= 0 )
			    tab.setSelectedIndex ( tab.getSelectedIndex()-1 );
		    }
		} 
	    );
	
        log ( "init Slides" );

	initSlides();

	p.add ( tab, "textpanel" );
	p.add ( nextpage, "nextstep" );
	p.add ( prevpage, "prevstep" );

	getFrame ( ).getContentPane().add ( p  );
	getFrame ( ).setBackground ( new java.awt.Color ( 192, 192, 255 ) );
	getFrame ( ).setTitle ("WingS Tutorial" );
    }

    private void initSlides ( ) {
	
	Properties slides = new Properties ();
	try {
	    File configFile = new File ( configFileName );
            log ( "load  slides properties" );
	    slides.load ( new java.io.FileInputStream( configFile ));
            log ( "slides properties loaded" );
	    configFileDate = configFile.lastModified();
	    //	    log ( "ConfigFileDate is :"+configFileDate );
	} catch ( IOException ioex ) {
	    log ( "Unable to load '"+configFileName+"'" );
	}
        //	tab.removeAll ();
	int i = 1;
	String heading = null;
	while ( ( heading = slides.getProperty ( "slide."+i+".heading" ) ) != null ) {
	    String template = slides.getProperty ( "slide."+i+".filename" );
	    File templ =
		new File ( getServletContext().getRealPath ( template ) );
	    try {
		i++;
		log ( "trying to add '"+templ.getAbsolutePath()+"' to tabbedpane");
		STemplateLayout textlayout = new STemplateLayout ( templ );
		SPanel textt = new SPanel (  );
		textt.setLayout ( textlayout );
		textt.add ( nextpage, "nextstep" );
		textt.add ( prevpage, "prevstep" );
		tab.add ( heading , textt );
	    } catch ( IOException ioex ) {
		log ( "Unable to add '"+templ.getAbsolutePath()+"' to tabbedpane", ioex );
	    }
	}

        log ( "start with first" );
	tab.setSelectedIndex ( 0 );

    }    

    private void checkConfig () {
	log ( "Checking config" );
	try {
	    File f = new File ( configFileName );
	    //	    log ( "ConfigFileDate is :"+configFileDate );
	    //	    log ( "configFile date is :"+f.lastModified() );
	    if ( f.lastModified()> configFileDate ) {
		log ( "new config!" );
		configFileDate = f.lastModified();
		initSlides();
	    }
	} catch ( Exception exp ) {
	    log ( "Exception in checkConfig()", exp );
	}
    }
    protected void processRequest(javax.servlet.http.HttpServletRequest req,
				  javax.servlet.http.HttpServletResponse response)
	throws javax.servlet.ServletException,
	java.io.IOException {
	checkConfig();
	super.processRequest ( req, response );
    }


    public void log ( String str ) {
	System.out.println ( str );
	super.log ( str );
    }

    public void log ( String str, Exception exp ) {
	System.out.println ( str );
	exp.printStackTrace();
	super.log ( str, exp );
    }
}// HelloWingsSessionServlet




