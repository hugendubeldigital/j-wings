package org.wingx.beans.beaneditor;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import org.wings.*;
import org.wingx.beans.*;
import org.wingx.beans.editors.*;

/*
 * @author Holger Engels
 * @version $Revision$
 */

public class CustomEditorDialog
    extends SDialog
{
    private static final String TITLE_PREFIX = "Editor: ";

    private final SLabel titleLabel = new SLabel();
    private final SButton closeButton = new SButton("Close");
    private final SResetButton resetButton = new SResetButton("Reset");
    private SComponent editor;

    public CustomEditorDialog(SComponent editor) {
	this.editor = editor;
        setLayout(new SBorderLayout());
        setBorder(new SBevelBorder());

        SFont titleLabelFont = new SFont();
        titleLabelFont.setStyle(BOLD);
        titleLabelFont.setSize(2);
        titleLabel.setText(TITLE_PREFIX);
        titleLabel.setFont(titleLabelFont);

        SHorizontalRule hrule = new SHorizontalRule();
        //hrule.setShade(false);

        SImage spacer = new SImage(new ResourceImageIcon(this.getClass(), "../../ui/resources/nothing.gif"));
        spacer.setWidth(600);
        spacer.setHeight(1);
        spacer.setStyle(null);

        SPanel headerPanel = new SPanel();
        headerPanel.add(titleLabel);
        headerPanel.add(hrule);
        headerPanel.add(spacer);

        closeButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    hide();
		    fireActionPerformed("closed");
		}
	    });

        SPanel buttonPanel = new SPanel();
        buttonPanel.setLayout(new SFlowLayout());
        buttonPanel.setHorizontalAlignment(CENTER);
        buttonPanel.add(closeButton);
        buttonPanel.add(new SSpacer(10));
        buttonPanel.add(resetButton);

        add(headerPanel, SBorderLayout.NORTH);
        add(editor, SBorderLayout.CENTER);
        add(buttonPanel, SBorderLayout.SOUTH);
    }

    public void setTitle(String title) {
	titleLabel.setText(TITLE_PREFIX + title);
    }
    public String getTitle() { return titleLabel.getText().substring(TITLE_PREFIX.length()); }
}
