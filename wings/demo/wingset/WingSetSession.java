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

package wingset;

import java.text.MessageFormat;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.*;

import org.wings.*;
import org.wings.externalizer.ExternalizedInfo;
import org.wings.io.*;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wings.util.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class WingSetSession
    extends SessionServlet
    implements SConstants
{
    static final ClassLoader cl = WingSetSession.class.getClassLoader();
    private final static SIcon brushedMetal = 
        new ResourceImageIcon(cl, "wingset/icons/brushedMetal.gif");

    /*
     * some hack.
     */
    private SLabel timeMeasure;
    private final TimeMeasure stopWatch;

    public WingSetSession(Session session) {
        super(session);
        stopWatch = new TimeMeasure(new MessageFormat("<b>{0}</b>: {1} (<i>x {2}</i>)<br/>"));
        System.out.println("new WingSetSession");
    }

    /**
     * after the servlet is initialized, we can create our GUI.
     */
    public void postInit(ServletConfig config) {
        timeMeasure = new SLabel();
        timeMeasure.setEscapeSpecialChars(false);
        getFrame().setTitle("WingSet Demo");

        SContainer contentPane = getFrame().getContentPane();
        try {
            java.net.URL templateURL = 
                getClass().getResource("/wingset/templates/ExampleFrame.thtml");
            if( templateURL == null ){
                contentPane.add(new SLabel("Sorry, can't find ExampleFrame.thtml. Are you using a JAR-File?"));
            }
            STemplateLayout layout = new STemplateLayout( templateURL );
            contentPane.setLayout( layout );
        }
        catch ( java.io.IOException except ) {
            except.printStackTrace();
        }
        
        STabbedPane tab = new STabbedPane();
        tab.setMaxTabsPerLine(8);
        tab.setBackgroundImage(brushedMetal);

        tab.add(new WingsImage(), "wingS!");
        tab.add(new LabelExample(), "Label");
        tab.add(new BorderExample(), "Border");
        tab.add(new TextComponentExample(), "Text Component");
         // a Tab with icon..
        tab.addTab("Tree", new ResourceImageIcon("org/wings/icons/JavaCup.gif"), 
                   new TreeExample(), "Tree Tool Tip");
        tab.add(new OptionPaneExample(getFrame()), "OptionPane");
        tab.add(new TableExample(), "Table");
        tab.add(new ListExample(), "List");
        tab.add(new ButtonExample(), "Button");
        tab.add(new CheckBoxExample(), "CheckBox");
        tab.add(new RadioButtonExample(), "RadioButton");
        tab.add(new Faces(), "Faces");
        tab.add(new FileChooserExample(), "FileChooser");
        tab.add(new ScrollPaneExample(), "ScrollPane");
        tab.add(new PageScrollerExample(), "PageScroller");
        //tab.add(new LayoutExample(), "Simple Layout");
        tab.addTab("Template Layout", 
                   new ResourceImageIcon(cl, "wingset/icons/cowSmall.gif"), 
                   new TemplateExample(), "Template Layout Manager");
        //tab.add(new DateChooserExample(), "DateChooser");

        contentPane.add(tab, "WingSetApp");

        contentPane.add(timeMeasure, "TimeLabel");

        /*
         * we don't have old working style. remember to change
         * the templates/ExampleFrame.thtml on change.
        final SRadioButton old = new SRadioButton("xhtml/old");
        old.setSelected(false);
        final SRadioButton css1 = new SRadioButton("xhtml/css1");
        css1.setSelected(true);

        SButtonGroup group = new SButtonGroup();
        group.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    try {
                        if (css1.isSelected())
                            getSession().getCGManager().setLookAndFeel("xhtml/css1");
                        else
                            System.err.println("sorry no xhtml/old");
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace(System.err);
                    }
                }
            });
        group.add(old);
        group.add(css1);

        contentPane.add(old,  "style=old");
        contentPane.add(css1, "style=css1");
        */
    }

    public void prepareRequest(HttpServletRequest req, 
                               HttpServletResponse response) {
        stopWatch.start("Event dispatching/action execution");
    }
    
    public void processRequest(HttpServletRequest req,
                               HttpServletResponse res)
        throws ServletException, IOException
    {
        stopWatch.stop();
        /*
         * This is a dummy call to generate the HTML output, just
         * to measure the time. With the result, we modify the
         * timeMeasure label, which may modify the actual code size
         * within a range of some bytes ..
         */
        stopWatch.start("time to generate HTML Code ");
        NullDevice devNull = new NullDevice();
        getFrame().write(devNull);
        stopWatch.stop();
        timeMeasure.setText(stopWatch.toString()
                            + "<b>HTML code size: </b>" 
                            + devNull.getSize() + " Bytes");
        stopWatch.reset();
    }
    
    /**
     * TODO: check, whether this works with InternetExplorer. IE is
     * known to have bugs that prevent it rendering compressed stuff correctly.
     *   -> verified: works with IE 5.0
     */
    protected Device createOutputDevice(HttpServletRequest req,
                                        HttpServletResponse response,
                                        ExternalizedInfo extInfo) 
        throws IOException {
        String mimeType = extInfo.getMimeType();
        // some browsers can handle a gziped stream only for text-files.
        if (mimeType != null && mimeType.startsWith("text/")) {
            String acceptEncoding = req.getHeader("Accept-Encoding");
            int gzipPos;
            if (acceptEncoding != null 
                && (gzipPos = acceptEncoding.indexOf("gzip")) >= 0) {
                // some browsers send 'x-gzip', others just 'gzip'. Our
                // response should be the same.
                boolean isXGZip = (gzipPos >= 2 
                                   && acceptEncoding.charAt(gzipPos-1) == '-'
                                   && acceptEncoding.charAt(gzipPos-2) == 'x');
                response.addHeader("Content-Encoding", 
                                   (isXGZip ? "x-gzip" : "gzip"));
                //System.err.println("GZIPed output for " + mimeType
                // + "accept: " + acceptEncoding);
                return new GZIPCompressingDevice(response.getOutputStream());
            }
        }
        return new ServletDevice(response.getOutputStream());
    }

    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "WingSet WingS demo servlet ($Revision$)";
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
