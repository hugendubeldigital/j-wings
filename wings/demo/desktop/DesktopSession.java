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
import org.wings.io.Device;
import org.wings.io.ServletDevice;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wings.util.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class DesktopSession
    extends SessionServlet
    implements SConstants
{
    SDesktopPane desktop;

    public DesktopSession(Session session) {
        super(session);
        System.out.println("new DeskTopSession");
    }

    public void postInit(ServletConfig config) {
        initGUI();
    }

    void initGUI() {
        SContainer contentPane = getFrame().getContentPane();

        SMenuBar menuBar = createMenu();
        contentPane.add(menuBar);

        desktop = new SDesktopPane();
        contentPane.add(desktop);

        Editor editor = new Editor();
        editor.setText(getStory());
        desktop.add(editor);
    }

    protected String getStory() {
        return "Ein Philosoph ist jemand, der in einem absolut dunklen Raum " +
            "mit verbundenen Augen nach einer schwarzen Katze sucht, die gar nicht " +
            "da ist. Ein Theologe ist jemand der genau das gleiche macht und ruft: " +
            "\"ich hab sie!\"";
    }

    protected SMenuBar createMenu() {
        SMenuItem newItem = new SMenuItem("New");
	newItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    newEditor();
		}
	    });
        SMenuItem openItem = new SMenuItem("Open");
	openItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    openEditor();
		}
	    });

        SMenu fileMenu = new SMenu("File");
        fileMenu.add(newItem);
        fileMenu.add(openItem);

        SMenuBar menuBar = new SMenuBar();
        menuBar.add(fileMenu);

        return menuBar;
    }

    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "Desktop ($Revision$)";
    }

    public void newEditor() {
        Editor editor = new Editor();
        desktop.add(editor);
    }

    public void openEditor() {
        final Editor editor = new Editor();
        desktop.add(editor);

        final SDialog dialog = new SDialog(new SFlowDownLayout());
        dialog.setEncodingType("multipart/form-data");

        SLabel label = new SLabel("Choose file");
        dialog.add(label);

        final SFileChooser chooser = new SFileChooser();
        dialog.add(chooser);

        dialog.add(new SSpacer(1, VERTICAL));

        SButton submit = new SButton("upload");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    File file = chooser.getFile();
                    Reader reader = new FileReader(file);
                    StringWriter writer = new StringWriter();

                    int b;
                    while ((b = reader.read()) > -1)
                        writer.write(b);

                    editor.setText(writer.toString());
                    dialog.hide();
                }
                catch (Exception e) {
                    SOptionPane.showMessageDialog(editor, "An error occured", e.getMessage());
                }
            }});
        dialog.add(submit);

        dialog.show(editor);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
