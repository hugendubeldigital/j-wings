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

import java.awt.Color;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;

import javax.servlet.*;
import javax.servlet.http.*;

import org.wings.*;
import org.wings.plaf.*;
import org.wings.session.*;
import org.wings.util.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class WingSet
{
    static final ClassLoader cl = WingSet.class.getClassLoader();
    private final static SIcon brushedMetal = 
        new ResourceImageIcon(cl, "wingset/icons/brushedMetal.gif");

    private SFrame frame;
    /*
     * time measurement (a little hacky)
     */
    private SLabel timeMeasure;
    private final TimeMeasure stopWatch;

    public WingSet() {
        frame = new SFrame("WingSet");

        System.out.println("new WingSet");
        stopWatch = new TimeMeasure(new MessageFormat("<b>{0}</b>: {1} (<i>x {2}</i>)<br/>"));

        timeMeasure = new SLabel();
        timeMeasure.setEscapeSpecialChars(false);
        frame.setTitle("WingSet Demo");

        SContainer contentPane = frame.getContentPane();
        try {
            URL templateURL = getClass().getResource("/wingset/templates/ExampleFrame.thtml");
            if (templateURL != null) {
                SRootLayout layout = new SRootLayout(templateURL);
                frame.setLayout(layout);
            }
        }
        catch ( java.io.IOException except ) {
            except.printStackTrace();
        }
        
        //SForm form = new SForm();
        STabbedPane tab = new STabbedPane();
        // tab.setMaxTabsPerLine(9);
        tab.setTabPlacement(STabbedPane.TOP);
		tab.setBackgroundImage(brushedMetal);

        tab.add(new WingsImage(), "wingS!");
        tab.add(new LabelExample(), "Label");
        tab.add(new BorderExample(), "Border");
        tab.add(new TextComponentExample(), "Text Component");
         // a Tab with icon..
        tab.addTab("Tree", new ResourceImageIcon("org/wings/icons/JavaCup.gif"), 
                   new TreeExample(), "Tree Tool Tip");
        tab.add(new OptionPaneExample(frame), "OptionPane");
        tab.add(new TableExample(), "Table");
        tab.add(new ListExample(), "List");
        tab.add(new ButtonExample(), "Button");
        tab.add(new CheckBoxExample(), "CheckBox");
        tab.add(new RadioButtonExample(), "RadioButton");
        tab.add(new Faces(), "Faces");
        tab.add(new FileChooserExample(), "FileChooser");
        tab.add(new ScrollPaneExample(), "ScrollPane");
        tab.add(new PageScrollerExample(), "PageScroller");
        tab.add(new TabbedPaneExample(), "Tabbed Pane");
        //tab.add(new LayoutExample(), "Simple Layout");
        tab.addTab("Template Layout", 
                   new ResourceImageIcon(cl, "wingset/icons/cowSmall.gif"), 
                   new TemplateExample(), "Template Layout Manager");
        //tab.add(new DateChooserExample(), "DateChooser");
        //form.add(tab);
        // contentPane.add(form, "WingSetApp");
        contentPane.add(tab, "WingSetApp");

        contentPane.add(timeMeasure, "TimeLabel");

        frame.show();
    }

    /*
    public void beforeEventDispatching(SessionEvent event)
        throws DispatchVetoException
    {
        
    }

    public void afterEventDispatching(SessionEvent event) {
        
    }

    public void beforeResourceDelivery(SessionEvent event)
        throws DeliveryVetoException
    {
        
    }

    public void afterResourceDelivery(SessionEvent event) {
        
    }

    public void beforeDestroy(SessionEvent event) {
        
    }
    */
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
