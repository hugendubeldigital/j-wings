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

package desktop;

import java.awt.event.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.*;

import org.wings.*;
import org.wings.event.*;
import org.wings.io.Device;
import org.wings.io.ServletDevice;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wings.util.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class Editor
    extends SInternalFrame
    implements SInternalFrameListener
{
    private SMenuBar menuBar;
    private STextArea textArea;

    private String backup;
    private String clip;

    public Editor() {
	menuBar = createMenu();
	getContentPane().add(menuBar);
    SForm form = new SForm();
    textArea = new STextArea();
	textArea.setColumns(80);
	textArea.setRows(24);
    form.add(textArea);
	getContentPane().add(form);

        Icon icon = new ResourceImageIcon(getClass(), "/desktop/penguin.png");
        setIcon(icon);
    }

    protected SMenuBar createMenu() {
        SMenuItem saveItem = new SMenuItem("Save");
	saveItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    save();
		}
	    });
        SMenuItem revertItem = new SMenuItem("Revert");
	revertItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    revert();
		}
	    });
        SMenuItem closeItem = new SMenuItem("Close");
	closeItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    close();
		}
	    });

        SMenu fileMenu = new SMenu("File");
        fileMenu.add(saveItem);
        fileMenu.add(revertItem);
        fileMenu.add(closeItem);

        SMenuItem cutItem = new SMenuItem("Cut");
	cutItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    cut();
		}
	    });
        SMenuItem copyItem = new SMenuItem("Copy");
	copyItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    copy();
		}
	    });
        SMenuItem pasteItem = new SMenuItem("Paste");
	pasteItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    paste();
		}
	    });

        SMenu editMenu = new SMenu("Edit");
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        SMenuBar menuBar = new SMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        return menuBar;
    }

    public void setText(String text) {
	textArea.setText(text);
    }
    public String getText() { return textArea.getText(); }

    public void setBackup(String backup) {
	this.backup = backup;
    }
    public String getBackup() { return backup; }

    public void save() {}

    public void revert() {
	textArea.setText(backup);
    }

    public void close() {
	super.dispose();
    }

    public void cut() {
	clip = textArea.getText();
	textArea.setText("");
    }

    public void copy() {
	clip = textArea.getText();
    }

    public void paste() {
	textArea.setText(textArea.getText() + clip);
    }

    /**
     * Invoked when an internal frame has been opened.
     */
    public void internalFrameOpened(SInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame has been closed.
     */
    public void internalFrameClosed(SInternalFrameEvent e) {
	close();
    }

    /**
     * Invoked when an internal frame is iconified.
     */
    public void internalFrameIconified(SInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is de-iconified.
     */
    public void internalFrameDeiconified(SInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is maximized.
     */
    public void internalFrameMaximized(SInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is unmaximized.
     */
    public void internalFrameUnmaximized(SInternalFrameEvent e) {}
}
