/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of the wingS demo (http://j-wings.org).
 *
 * The wingS demo is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package desktop;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SDesktopPane;
import org.wings.SFileChooser;
import org.wings.SFlowDownLayout;
import org.wings.SFrame;
import org.wings.SInternalFrame;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.SOptionPane;
import org.wings.event.SContainerEvent;
import org.wings.event.SContainerListener;
import org.wings.style.CSSProperty;
import org.wings.util.ComponentVisitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;
/**
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class Desktop
    implements SConstants
{
    SFrame frame;
    BirdsNest desktop;
    SMenu windowMenu;
    int editorNumber;

    public Desktop() {
        frame = new SFrame("Desktop");
        frame.setAttribute(CSSProperty.MARGIN, "4px");

        SContainer contentPane = frame.getContentPane();
        contentPane.setLayout(new SFlowDownLayout());
        editorNumber = 0;
        SMenuBar menuBar = createMenu();
        contentPane.add(menuBar);

        desktop = new BirdsNest();
        // add the frames to the window-menu ..
        desktop.addContainerListener(new SContainerListener() {
                public void componentAdded(SContainerEvent e) {
                    SInternalFrame frame = (SInternalFrame) e.getChild();
                    SMenuItem windowItem = new WindowMenuItem(desktop, frame);
                    windowMenu.add(windowItem);
                }
                public void componentRemoved(SContainerEvent e) {
                    SInternalFrame frame = (SInternalFrame) e.getChild();
                    SMenuItem windowItem = new WindowMenuItem(frame);
                    windowMenu.remove(windowItem);
                }
            });
        contentPane.add(desktop);
        
        newEditor().setText(getStory());

        frame.show();
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

        windowMenu = new SMenu("Window");

        SMenuBar menuBar = new SMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(windowMenu);

        return menuBar;
    }

    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "Desktop ($Revision$)";
    }
    
    /**
     * add a new frame. If there is a frame, that is maximized, then
     * move the maximization to the new frame.
     */
    public void addNewFrame(final SInternalFrame frame) {
        desktop.add(frame, 0);
        try {
            desktop.invite(new ComponentVisitor() {
                    public void visit(SComponent c) { /* ign */ }
                    public void visit(SContainer c) {
                        if (! (c instanceof SInternalFrame))
                            return;
                        SInternalFrame ff = (SInternalFrame) c;
                        if (ff != frame && ff.isMaximized()) {
                            ff.setMaximized(false);
                            // set _our_ frame maximized, then.
                            frame.setMaximized(true);
                        }
                    }
                });
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public Editor newEditor() {
        Editor editor = new Editor();
        editor.setTitle("Editor [" + (++editorNumber) + "]");
        addNewFrame(editor);
        return editor;
    }

    public Editor openEditor() {
        final Editor editor = new Editor();
        addNewFrame(editor);
        
        final SFileChooser chooser = new SFileChooser();
        chooser.setColumns(20);

        final SOptionPane dialog = new SOptionPane();
        dialog.setEncodingType("multipart/form-data");
        dialog.showInput(editor, "Choose file", chooser, "Open file");
        dialog.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if ( evt.getActionCommand()==SOptionPane.OK_ACTION ) {
                        try {
                            File file = chooser.getFile();
                            Reader reader = new FileReader(file);
                            StringWriter writer = new StringWriter();
                            
                            int b;
                            while ((b = reader.read()) >= 0)
                                writer.write(b);
                            
                            editor.setText(writer.toString());
                            editor.setTitle(chooser.getFileName());
                            chooser.reset();
                        }
                        catch (Exception e) {
                            dialog.show(editor); // show again ..
                            // .. but first, show error-message on top ..
                            SOptionPane.showMessageDialog(editor, 
                                                      "Error opening file", 
                                                          e.getMessage());
                        }
                    }
                    else {
                        editor.close();
                    }
                }
            });

        return editor;
    }

    /**
     * A menu item, that handles the position of an internal frame within
     * the desktop - whenever it is clicked, the frame is put on top. This
     * MenuItem is added in some component listener, that gets activated
     * whenever a window is added or removed from the desktop.
     */
    private static class WindowMenuItem extends SMenuItem {
        private final SInternalFrame frame;
        
        public WindowMenuItem(SInternalFrame f) {
            frame = f;
        }

        public WindowMenuItem(final SDesktopPane d, final SInternalFrame f) {
            frame = f;
            /*
             * when clicked, put that frame on top. If some other frame was
             * maximized at that point, then maximize _our_ frame instead.
             */
            addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        d.setPosition(f, 0);
                        if (f.isIconified()) {
                            f.setIconified(false);
                        }
                        /*
                         * if some other frame is maximized, then we want
                         * to toggle this maximization..
                         */
                        try {
                            d.invite(new ComponentVisitor() {
                                    public void visit(SComponent c) { /*ign*/ }
                                    public void visit(SContainer c) {
                                        if (! (c instanceof SInternalFrame))
                                            return;
                                        SInternalFrame ff = (SInternalFrame) c;
                                        if (ff != frame && ff.isMaximized()) {
                                            ff.setMaximized(false);
                                            // set _our_ frame maximized, then.
                                            frame.setMaximized(true);
                                        }
                                    }
                                });
                        }
                        catch (Exception e) {
                            System.err.println(e);
                        }
                    }
                });
        }

        /**
         * returns the title of the frame.
         */
        public String getText() {
            String title = frame.getTitle();
            return (title == null || title.length()==0) ? "[noname]" : title;
        }
        
        /**
         * remove menu item by frame ..
         */
        public boolean equals(Object o) {
            if (o instanceof WindowMenuItem) {
                WindowMenuItem wme = (WindowMenuItem) o;
                return (frame != null && wme.frame == frame);
            }
            return false;
        }
    }
}
