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

import java.awt.event.*;
import java.io.*;
import javax.swing.Icon;

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class FileChooserExample
    extends WingSetPane
{

    public SComponent createExample() {
        SForm p = new SForm(new SFlowDownLayout());
        p.setEncodingType("multipart/form-data");

        SLabel label = new SLabel("SFileChooser");
        p.add(label);

        final SFileChooser chooser = new SFileChooser();
        p.add(chooser);

        final SLabel message = new SLabel("message: ");
        p.add(message);

        final SLabel filename = new SLabel("filename: ");
        p.add(filename);
        final SLabel fileid = new SLabel("fileid: ");
        p.add(fileid);

        SButton submit = new SButton("upload");
        p.add(submit);

        p.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    message.setText("message: OK");
                    filename.setText("filename: " + chooser.getFileName());
                    fileid.setText("fileid: " + chooser.getFileId());
                    chooser.reset();
                } catch ( IOException ex ) {
                    message.setText("message: " + ex.getMessage());
                    filename.setText("filename: ");
                    fileid.setText("fileid: ");
                }
            }});

        return p;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
