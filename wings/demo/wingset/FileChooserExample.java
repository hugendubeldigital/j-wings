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

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.Icon;

import org.wings.*;
import org.wings.border.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class FileChooserExample
    extends WingSetPane
{

    SCardLayout contentSwitcher;

    SLabel iconLabel;

    SForm textForm;

    STextArea textArea;

    SLabel unknownLabel;

    SFileChooser chooser;

    public SComponent createExample() {
        SPanel p = new SPanel(new SBorderLayout());

        SLabel maxContentLengthLabel = 
            new SLabel("max content length: " + 
                       getSession().getMaxContentLength()  + "k");
        
        maxContentLengthLabel.setForeground(Color.red);
        
        p.add(maxContentLengthLabel,
              SBorderLayout.NORTH);


        p.add(createUpload(), SBorderLayout.WEST);

        p.add(createPreview(), SBorderLayout.CENTER);

        return p;
    }

    protected String getText(File f) {
        try {
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line = reader.readLine();
            while ( line!=null ) {
                buffer.append(line).append("\n");
                line = reader.readLine();
            }

            return buffer.toString();
        } catch ( Exception ex ) {
            return "got exception " + ex.getMessage();
        }
    }

    protected void adaptPreview() {
        try {
            if ( chooser.getFileType().startsWith("text/") ) {
                textArea.setText(getText(chooser.getFile()));
                contentSwitcher.show(textForm);
            } else if ( "image/gif".equals(chooser.getFileType()) ) {
                iconLabel.setIcon(new FileImageIcon(chooser.getFile()));
                contentSwitcher.show(iconLabel);
            } else {
                contentSwitcher.show(unknownLabel);
            }
        } catch ( Exception ex ) {
            contentSwitcher.show(unknownLabel);
        }
    }


    protected SComponent createPreview() {
        SPanel p = new SPanel(new SFlowDownLayout());
        p.setVerticalAlignment(TOP);

        SLabel previewLabel = new SLabel("Preview");
        previewLabel.setBorder(new SEmptyBorder(0, 20, 0, 0));
        p.add(previewLabel);

        contentSwitcher = new SCardLayout();

        SPanel contentPane = new SPanel(contentSwitcher);

        iconLabel = new SLabel();

        textForm = new SForm();

        textArea = new STextArea();
        textArea.setColumns(50);
        textArea.setRows(20);
        textArea.setEditable(false);

        unknownLabel = new SLabel("Unknown Content");


        contentPane.add(iconLabel, "ICON");

        textForm.add(textArea);
        contentPane.add(textForm, "TEXT");

        contentPane.add(unknownLabel, "UNKNOWN");

        contentSwitcher.show(unknownLabel);
        
        contentPane.setBorder(new SEmptyBorder(10, 20, 0, 0));
        p.add(contentPane);
        return p;
    }

    protected SComponent createUpload() {
        SForm p = new SForm(new SFlowDownLayout());
        p.setEncodingType("multipart/form-data");

        chooser = new SFileChooser();
        p.add(chooser);

        SButton submit = new SButton("upload");
        p.add(submit);

        final SLabel message = new SLabel("message: ");
        p.add(message);

        final SLabel filename = new SLabel("filename: ");
        p.add(filename);
        final SLabel fileid = new SLabel("fileid: ");
        p.add(fileid);
        final SLabel filetype = new SLabel("filetype: ");
        p.add(filetype);


        p.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    message.setText("message: OK");
                    filename.setText("filename: " + chooser.getFileName());
                    fileid.setText("fileid: " + chooser.getFileId());
                    filetype.setText("filetype: " + chooser.getFileType());
                    adaptPreview();
                    chooser.reset();
                } catch ( IOException ex ) {
                    message.setText("message: " + ex.getMessage());
                    filename.setText("filename: ");
                    fileid.setText("fileid: ");
                    filetype.setText("filetype: ");
                    contentSwitcher.show(unknownLabel);
                }
            }});

        p.setVerticalAlignment(TOP);

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
