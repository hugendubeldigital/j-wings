/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package wingset;

import java.awt.event.*;
import javax.swing.Icon;

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class FileChooserExample
    extends SPanel
    implements SConstants
{
    public FileChooserExample() {
        add(createFileChooserExample());

        add(new SSeparator());

        SHRef href =  new SHRef("View Source Code");
        href.setReference("http://www.mercatis.de/~armin/WingSet/" +
                          getClass().getName() + ".java");
        add(href);
    }

    SForm createFileChooserExample() {
        SForm p = new SForm(new SFlowDownLayout());
        p.setEncodingType("multipart/form-data");

        SLabel label = new SLabel("SFileChooser");
        p.add(label);

        final SFileChooser chooser = new SFileChooser();
        p.add(chooser);

        p.add(new SSpacer(1, VERTICAL));

        final SLabel filename = new SLabel("filename: ");
        p.add(filename);
        final SLabel fileid = new SLabel("fileid: ");
        p.add(fileid);

        p.add(new SSpacer(1, VERTICAL));

        SButton submit = new SButton("upload");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filename.setText("filename: " + chooser.getFilename());
                fileid.setText("fileid: " + chooser.getFileid());
            }});
        p.add(submit);

        return p;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
