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

public class TestFrame
    extends SFrame
{
    public TestFrame(String name) {
	super("test frame");
	setName(name);
    }

    public void setComponent(SComponent component) {
	getContentPane().removeAll();
	getContentPane().add(component);
    }
}
