package ide;

import java.awt.event.*;
import java.beans.BeanInfo;
import java.beans.BeanDescriptor;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.*;
import javax.xml.parsers.*;

import org.apache.tools.ant.*;
import org.wings.*;
import org.wings.event.*;
import org.wings.plaf.*;
import org.wings.session.*;
import org.w3c.dom.*;

public class LafPanel
    extends SForm
{
    private static Class[] components = new Class[] {
	SAbstractButton.class,
	SAlert.class,
	SAnchor.class,
	SBaseTable.class,
	SBevelBorder.class,
	SBorderLayout.class,
	SBoxLayout.class,
	SButton.class,
	SCardLayout.class,
	SCellRendererPane.class,
	SCheckBox.class,
	SComboBox.class,
	SContainer.class,
	SDefaultComboBoxModel.class,
	SDefaultListCellRenderer.class,
	SDefaultListModel.class,
	SDefaultTableCellRenderer.class,
	SDefaultTreeCellRenderer.class,
	SDesktopPane.class,
	SDialog.class,
	SDivision.class,
	SDummy.class,
	SEmptyBorder.class,
	SEtchedBorder.class,
	SFileChooser.class,
	SFlowDownLayout.class,
	SFlowLayout.class,
	SForm.class,
	SFrame.class,
	SFrameSet.class,
	SFrameSetLayout.class,
	SFullScreenLayout.class,
	SGridLayout.class,
	SHorizontalRule.class,
	SHRef.class,
	SImage.class,
	SInternalFrame.class,
	SLabel.class,
	SLineBorder.class,
	SLineBreak.class,
	SLink.class,
	SList.class,
	SListLayout.class,
	SMenuBar.class,
	SMenuItem.class,
	SMenu.class,
	SOptionPane.class,
	SPageScroller.class,
	SPanel.class,
	SParagraph.class,
	SPasswordField.class,
	SRadioButton.class,
	SResetButton.class,
	SRootContainer.class,
	SScrollBar.class,
	SScrollPane.class,
	SSeparator.class,
	SSpacer.class,
	STabbedPane.class,
	STableCellEditor.class,
	STable.class,
	StaticResource.class,
	STemplateLayout.class,
	STextArea.class,
	STextComponent.class,
	STextField.class,
	SToggleButton.class,
	SToolbar.class,
	STree.class
    };

    Map modules;

    STextArea editor;
    STextField name;
    SFileChooser chooser;
    SComboBox component;
    Properties props;

    public LafPanel(Map modules) {
	super(new SBorderLayout());
	this.modules = modules;

	setEncodingType("multipart/form-data");

	SLabel sourceFrom = new SLabel("source from ..");
	final SRadioButton editorSource = new SRadioButton("editor");
	editorSource.setSelected(true);
	final SRadioButton fileSource = new SRadioButton("file");
	fileSource.setSelected(false);
	final SRadioButton nameSource = new SRadioButton("name");
	nameSource.setSelected(false);

        SButtonGroup group = new SButtonGroup();
        group.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
		    editor.setVisible(editorSource.isSelected());
		    chooser.setVisible(fileSource.isSelected());
		    name.setVisible(nameSource.isSelected());
                }
            });
        group.add(editorSource);
        group.add(fileSource);
        group.add(nameSource);

	SButton apply = new SButton("apply");

	SPanel north = new SPanel();
        north.add(sourceFrom);
        north.add(editorSource);
        north.add(fileSource);
        north.add(nameSource);
        north.add(apply);
	add(north, "North");

	name = new STextField();
	name.setColumns(80);
	name.setVisible(false);

	editor = new STextArea();
	editor.setColumns(80);
	editor.setRows(24);

	chooser = new SFileChooser();
	chooser.setFileNameFilter("*.plaf");
	chooser.setVisible(false);

	component = new SComboBox(components);

	SPanel center = new SPanel(new SGridLayout(1));
	center.add(editor);
	center.add(chooser);
	center.add(name);
	center.add(component);
	add(center, "Center");


	SButton compile = new SButton("compile");
	compile.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
		    System.out.println("compile");
		    compile();
		    test();
		}
	    });
	add(compile, "South");
    }

    public void compile() {
	ByteArrayOutputStream log = new ByteArrayOutputStream();
	try {
	    if (((PropertyService)getSession()).getProperty("build.dir") == null)
		((PropertyService)getSession()).setProperty("build.dir", "/tmp/ide");

	    // make sure we have a build directory
	    File buildDir = new File((String)((PropertyService)getSession()).getProperty("build.dir"));
	    if (!buildDir.exists())
		buildDir.mkdirs();

	    // make sure, the build.xml file is in place
	    File buildFile = new File(buildDir, "build.xml");
	    if (!buildFile.exists()) {
		InputStream in = getSession().getServletContext().getResourceAsStream("/cg-build.xml");
		OutputStream out = new FileOutputStream(buildFile);
		new StreamPumper(in, out).run();
	    }

	    // make sure, all necessary libraries are in place
	    File wingsJar = new File(buildDir, "lib/wings.jar");
	    if (!wingsJar.exists()) {
		InputStream in = getSession().getServletContext().getResourceAsStream("/WEB-INF/lib/wings.jar");
		OutputStream out = new FileOutputStream(wingsJar);
		new StreamPumper(in, out).run();
	    }

	    // build tree
	    Build build = new Build(buildFile);
	    build.executeTarget("prepare");

	    PrintStream out = new PrintStream(log);
	    build.getLogger().setOutputPrintStream(out);
	    build.getLogger().setErrorPrintStream(out);

	    // cg template
	    props = null;
	    File file = null;
	    if (chooser.isVisible()) {
		file = chooser.getFile();
		if (file == null) {
		    System.err.println("no file uploaded");
		}
		props = scanPlaf(new FileInputStream(file));
		File to = new File(buildDir, "build/cg/" + props.getProperty("name") + ".plaf");
		if (to.exists())
		    to.delete();
		file.renameTo(to);
	    }
	    else if (editor.isVisible()) {
		props = scanPlaf(new StringBufferInputStream(editor.getText()));
		file = new File(buildDir, "build/cg/" + props.getProperty("name") + ".plaf");
		PrintWriter writer = new PrintWriter(new FileWriter(file));
		writer.write(editor.getText());
		writer.flush();
	    }
	    else if (name.isVisible()) {
		file = new File(buildDir, "build/cg/" + name.getText() + ".plaf");
		props = scanPlaf(new FileInputStream(file));
	    }
	    System.err.println("file: " + file.getCanonicalPath());

	    selectComponent(props.getProperty("for"));

	    Vector targets = new Vector(2);
	    targets.add("java");
	    targets.add("class");
	    targets.add("plaf");
	    build.executeTargets(targets);
	}
	catch (Exception e) {
	    System.err.println(e.getMessage());
	    e.printStackTrace(System.err);
	}
	finally {
	    System.out.println("LOG");
	    System.out.println("" + log);
	}
    }

    public Properties scanPlaf(InputStream in) {
	Properties props = new Properties();
	try {
	    String forValue = null;
	    String nameValue = null;

	    LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
	    String line = reader.readLine();
	    int pos;

	    boolean done = false;
	    while (line != null && !done) {
		if (line.indexOf("<template") >= 0)
		    done = true;
		else
		    line = reader.readLine();
	    }

	    done = false;
	    while (line != null && !done) {
		if (nameValue == null && (pos = line.indexOf("name")) >= 0) {
		    pos = line.indexOf("=", pos + 4);
		    pos = line.indexOf("\"", pos + 1);
		    int end = line.indexOf("\"", pos + 1);
		    nameValue = line.substring(pos + 1, end);
		}
		if (forValue == null && (pos = line.indexOf("for")) >= 0) {
		    pos = line.indexOf("=", pos + 3);
		    pos = line.indexOf("\"", pos + 1);
		    int end = line.indexOf("\"", pos + 1);
		    forValue = line.substring(pos + 1, end);
		}
		if (nameValue != null && forValue != null)
		    done = true;
		else
		    line = reader.readLine();
	    }

	    props.setProperty("name", nameValue);
	    props.setProperty("for", forValue);
	}
	catch (Exception e) {
	    System.err.println(e.getMessage());
	    e.printStackTrace(System.err);
	}
	return props;
    }

    private void selectComponent(String name) {
	try {
	    Class clazz = Class.forName(name);
	    component.setSelectedItem(clazz);
	}
	catch (ClassNotFoundException e) {
	    System.err.println(e.getMessage());
	}
    }

    public void test() {
	if (((PropertyService)getSession()).getProperty("build.dir") == null)
	    ((PropertyService)getSession()).setProperty("build.dir", "/tmp/ide");

	SPanel builderPanel = (SPanel)modules.get("builder");
	PropertyPanel propertyPanel = (PropertyPanel)modules.get("properties");

	Class componentClass = (Class)component.getSelectedItem();
	try {
	    SComponent test = (SComponent)componentClass.newInstance();
	    builderPanel.removeAll();
	    builderPanel.add(test);

	    String url = "file:" + (String)((PropertyService)getSession()).getProperty("build.dir")
		+ "/dist/plaf/" + "plaf" + "-plaf.jar";
	    System.out.println("url: " + url);
	    ClassLoader loader = new URLClassLoader(new URL[] { new URL(url) },
						    getClass().getClassLoader());
	    Class cgClass = loader.loadClass("plaf." + props.getProperty("name"));
	    ComponentCG cg = (ComponentCG)cgClass.newInstance();
	    
	    Method cgSetter = findCGSetter(componentClass);
	    cgSetter.invoke(test, new Object[] { cg });
	    propertyPanel.setComponent(test);
	}
	catch (Exception e) {
	    System.err.println(e.getMessage());
	    e.printStackTrace(System.err);
	}
    }

    Method findCGSetter(Class clazz)
	throws Exception
    {
	Method[] methods = clazz.getMethods();
	for (int i=0; i < methods.length; i++) {
	    if ("setCG".equals(methods[i].getName()) &&
		!methods[i].getParameterTypes()[0].equals(ComponentCG.class))
		return methods[i];
	}
	return null;
    }
}
