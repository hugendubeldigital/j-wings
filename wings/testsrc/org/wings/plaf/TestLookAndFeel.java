package org.wings.plaf;

import java.awt.Color;
import java.awt.Font;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import org.wings.*;
import org.wings.session.*;
import org.wings.style.*;

import junit.framework.*;

public class TestLookAndFeel
    extends TestCase
{
    boolean notified;

    public TestLookAndFeel(String name) {
	super(name);
    }

    public void setUp()
	throws Exception
    {}

    public void testLookAndFeelFactory()
	throws IOException
    {
	URL url = getClass().getResource("css1.jar");

	// deploy laf
        LookAndFeelFactory.deploy(url);
	Assert.assertEquals(1, LookAndFeelFactory.getInstalledLookAndFeels().size());
	LookAndFeel laf = LookAndFeelFactory.getLookAndFeel("xhtml/css1");
	Assert.assertNotNull(laf);
	Assert.assertEquals("xhtml/css1", laf.getName());

	// register session
	DefaultSession session = new DefaultSession();
	SessionManager.setSession(session);
	session.getCGManager().setLookAndFeel(laf);
	session.addPropertyChangeListener("lookAndFeel", new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent pe) {
		    notified = true;
		}
	    });
	LookAndFeelFactory.registerSession(session);

	// redeploy laf
	notified = false;
	url = getClass().getResource("css1.jar");
        LookAndFeelFactory.deploy(url);
	Assert.assertTrue(notified);

	// unregister session
	LookAndFeelFactory.unregisterSession(session);

	// redeploy laf
	notified = false;
	url = getClass().getResource("css1.jar");
        LookAndFeelFactory.deploy(url);
	Assert.assertTrue(!notified);
    }

    public void testLookAndFeel()
	throws IOException
    {
	URL url = getClass().getResource("css1.jar");
	ClassLoader classLoader = new URLClassLoader(new URL[] { url });
	LookAndFeel laf = new LookAndFeel(classLoader);

	// makeCG
	ComponentCG componentCG = (ComponentCG)laf.makeCG("org.wings.plaf.xhtml.css1.FrameCG");
	Assert.assertNotNull(componentCG);
	LayoutCG layoutCG = (LayoutCG)laf.makeCG("org.wings.plaf.xhtml.css1.BorderLayoutCG");
	Assert.assertNotNull(layoutCG);
	BorderCG borderCG = (BorderCG)laf.makeCG("org.wings.plaf.xhtml.css1.EtchedBorderCG");
	Assert.assertNotNull(borderCG);

	// makeFont
	SFont font = laf.makeFont("sans-serif,PLAIN,12");
	Assert.assertEquals("sans-serif", font.getFace());
	Assert.assertEquals(SConstants.PLAIN, font.getStyle());
	Assert.assertEquals(12, font.getSize());

	// makeColor
	Color color = laf.makeColor("#AABBCC");
	Assert.assertEquals(170, color.getRed());
	Assert.assertEquals(187, color.getGreen());
	Assert.assertEquals(204, color.getBlue());

	// makeStyle
	Style style = laf.makeStyle("blub");
	Assert.assertEquals("blub", style.getID());

	// makeObject
	Object object = laf.makeObject("42", Integer.class);
	Assert.assertEquals(Integer.class, object.getClass());
	Assert.assertEquals(42, ((Integer)object).intValue());
    }

    public void testCGDefaults() {
	CGDefaults parent = new CGDefaults(null);
	parent.put("test", Boolean.TRUE);
	Assert.assertEquals(Boolean.TRUE, parent.get("test", Boolean.class));

	CGDefaults defaults = new CGDefaults(parent);
	Assert.assertEquals(Boolean.TRUE, defaults.get("test", Boolean.class));

	defaults.put("test", Boolean.FALSE);
	Assert.assertEquals(Boolean.FALSE, defaults.get("test", Boolean.class));
    }
}
