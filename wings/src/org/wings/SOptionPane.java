/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import java.util.Locale;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.wings.plaf.*;

/*
 * SOptionPane.java
 *
 * Da das webdixie package nicht synchron funktioniert, erhaelt die Anwendung
 * die Rueckkopplung, welcher Button gedrueckt wurde durch einen
 * ActionEvent.
 */
/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SOptionPane
    extends SDialog
    implements ActionListener
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "OptionPaneCG";

    /**
     * TODO: documentation
     */
    public static final String YES_ACTION = "YES";

    /**
     * TODO: documentation
     */
    public static final String NO_ACTION = "NO";

    /**
     * TODO: documentation
     */
    public static final int OK_OPTION = JOptionPane.OK_OPTION;

    /**
     * TODO: documentation
     */
    public static final int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;

    /**
     * TODO: documentation
     */
    public static final int YES_OPTION = JOptionPane.YES_OPTION;

    /**
     * TODO: documentation
     */
    public static final int NO_OPTION = JOptionPane.NO_OPTION;

    /**
     * TODO: documentation
     */
    public static final int RESET_OPTION = 999;

    /**
     * Type meaning look and feel should not supply any options -- only
     * use the options from the JOptionPane.
     */
    public static final int DEFAULT_OPTION = JOptionPane.DEFAULT_OPTION;

    /**
     * TODO: documentation
     */
    public static final int OK_CANCEL_OPTION = JOptionPane.OK_CANCEL_OPTION;

    /**
     * TODO: documentation
     */
    public static final int OK_CANCEL_RESET_OPTION = OK_CANCEL_OPTION + 1000;

    /**
     * TODO: documentation
     */
    public static final int YES_NO_OPTION = JOptionPane.YES_NO_OPTION;

    /**
     * TODO: documentation
     */
    public static final int YES_NO_RESET_OPTION = YES_NO_OPTION + 1000;

    /**
     * TODO: documentation
     */
    public static final int YES_NO_CANCEL_OPTION = JOptionPane.YES_NO_CANCEL_OPTION;

    /**
     * TODO: documentation
     */
    public static final int YES_NO_CANCEL_RESET_OPTION = YES_NO_CANCEL_OPTION + 1000;

    /**
     * ContentPane with border layout.
     */
    private final SContainer contents = new SContainer(new SBorderLayout());

    /**
     * Die Message des OptionPanes wird hier rein getan.
     */
    private final SContainer optionData = new SContainer();

    /**
     * Die Message des OptionPanes wird hier rein getan.
     */
    private final SContainer images = new SContainer();

    /**
     * TODO: documentation
     */
    protected final SContainer optionButtons =
        new SContainer(new SFlowLayout(CENTER));

    /**
     * TODO: documentation
     */
    protected final SButton optionOK = createButton("OK");

    /**
     * TODO: documentation
     */
    protected final SButton optionCancel = createButton("Cancel");

    /**
     * TODO: documentation
     */
    protected final SButton optionYes = createButton("Yes");

    /**
     * TODO: documentation
     */
    protected final SButton optionNo = createButton("No");

    /**
     * TODO: documentation
     */
    private final SResetButton optionReset = createResetButton("Reset");

    /**
     * TODO: documentation
     */
    protected static SLabel messageImage =
        new SLabel(new ResourceImageIcon("org/wings/icons/Inform.gif"));

    /**
     * TODO: documentation
     */
    protected static SLabel questionImage =
        new SLabel(new ResourceImageIcon("org/wings/icons/Question.gif"));

    /**
     * TODO: documentation
     */
    protected static SLabel yesnoImage =
        new SLabel(new ResourceImageIcon("org/wings/icons/Question.gif"));

    /**
     * The chosen option
     * @see #OK_OPTION
     * @see #YES_OPTION
     * @see #CANCEL_OPTION
     * @see #NO_OPTION
     */
    protected Object selected = null;

    /**
     * TODO: documentation
     *
     */
    public SOptionPane() {
        SGridLayout layout = new SGridLayout(1);
        layout.setBorder(1);
        setLayout(layout);
        initPanel();
    }

    /*
     * The chosen option.
     * @see #OK_OPTION
     * @see #YES_OPTION
     * @see #CANCEL_OPTION
     * @see #NO_OPTION
     */
    public final Object getValue() {
        return selected;
    }

    private final void initPanel() {
        optionButtons.add(optionOK);
        optionButtons.add(optionYes);
        optionButtons.add(optionCancel);
        optionButtons.add(optionNo);
        optionButtons.add(optionReset);

        images.add(messageImage);
        messageImage.setToolTipText("info");
        images.add(questionImage);
        questionImage.setToolTipText("question");
        images.add(yesnoImage);
        yesnoImage.setToolTipText("question");

        contents.add(optionData, SBorderLayout.CENTER);
        contents.add(images, SBorderLayout.WEST);
        add(contents);

        add(optionButtons);
    }

    /**
     * TODO: documentation
     */
    protected final SButton createButton(String label) {
        SButton b = new SButton(label);
        b.addActionListener(this);
        return b;
    }

    /**
     * TODO: documentation
     */
    protected final SResetButton createResetButton(String label) {
        SResetButton b = new SResetButton(label);
        b.addActionListener(this);
        return b;
    }

    /**
     * TODO: documentation
     *
     * @param l
     */
    public void setLocale(Locale l) {
        super.setLocale(l);
        optionOK.setText("OK");
        optionCancel.setText("Cancel");
        optionYes.setText("Yes");
        optionNo.setText("No");
        optionReset.setText("Reset");
    }

    /**
     * TODO: documentation
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (root != null) {
            root.popDialog();
        }

        selected = e.getSource();

        if ( e.getSource()==optionOK ) {
            fireActionPerformed(OK_ACTION);
        }
        else if ( e.getSource()==optionYes ) {
            fireActionPerformed(YES_ACTION);
        }
        else if ( e.getSource()==optionCancel ) {
            fireActionPerformed(CANCEL_ACTION);
        }
        else if ( e.getSource()==optionNo ) {
            fireActionPerformed(NO_ACTION);
        }
        else {
            fireActionPerformed(UNKNOWN_ACTION);
        }

        removeAll();
    }

    /**
     * TODO: documentation
     *
     */
    protected void resetOptions() {
        optionOK.setVisible(false);
        optionYes.setVisible(false);
        optionCancel.setVisible(false);
        optionNo.setVisible(false);
        optionReset.setVisible(false);

        messageImage.setVisible(false);
        questionImage.setVisible(false);
        yesnoImage.setVisible(false);
    }

    SContainer customButtons = null;

    /**
     * TODO: documentation
     */
    public void setOptions(Object[] options ) {
        resetOptions();

        if ( customButtons==null )
            customButtons = new SContainer();

        for ( int i=0; i<options.length; i++ ) {
            if ( options[i] instanceof SComponent ) {
                if ( options[i] instanceof SAbstractButton )
                    ((SAbstractButton)options[i]).addActionListener(this);
                customButtons.add((SComponent)options[i]);
            }
            else {
                SButton b = new SButton(options[i].toString());
                b.addActionListener(this);
                customButtons.add(b);
            }
        }

        add(customButtons);
    }

    /**
     * returns, whether this OptionPane is cancelable. This may be used
     * from the renderer to add additional cancel buttons.
     */
    public boolean isCancelable() {
        return optionCancel.isVisible();
    }

    /**
     * cancel this option pane programmatically.
     */
    public void cancel() {
        if (isCancelable()) {
            fireActionPerformed(CANCEL_ACTION);
        }
    }

    /**
     * TODO: documentation
     *
     * @param newType
     */
    public void setOptionType(int newType) {
        resetOptions();

        switch ( newType ) {
        case DEFAULT_OPTION:
            optionOK.setVisible(true);
            break;

        case OK_CANCEL_OPTION:
            optionOK.setVisible(true);
            optionCancel.setVisible(true);
            break;

        case OK_CANCEL_RESET_OPTION:
            optionOK.setVisible(true);
            optionCancel.setVisible(true);
            optionReset.setVisible(true);
            break;

        case YES_NO_OPTION:
            optionYes.setVisible(true);
            optionNo.setVisible(true);
            break;

        case YES_NO_RESET_OPTION:
            optionYes.setVisible(true);
            optionNo.setVisible(true);
            optionReset.setVisible(true);
            break;

        case YES_NO_CANCEL_OPTION:
            optionYes.setVisible(true);
            optionNo.setVisible(true);
            optionCancel.setVisible(true);
            break;

        case YES_NO_CANCEL_RESET_OPTION:
            optionYes.setVisible(true);
            optionNo.setVisible(true);
            optionCancel.setVisible(true);
            optionReset.setVisible(true);
            break;
        }
    }

    /**
     * TODO: documentation
     */
    public void showOption(SComponent c, String title, Object message) {
        if (title != null)
            setTitle(title);

        optionData.removeAll();
        if ( message instanceof SComponent ) {
            optionData.add((SComponent)message);
        }
        else {
            optionData.add(new SLabel(message.toString()));
        }
        show(c);
    }

    /**
     * TODO: documentation
     */
    public static void showMessageDialog(SComponent parent,
                                         Object message,
                                         ActionListener al) {
        showMessageDialog(parent, message, null, 0, al);
    }

    /**
     * TODO: documentation
     */
    public static void showMessageDialog(SComponent parent,
                                         Object message) {
        showMessageDialog(parent, message, null, 0, null);
    }

    /**
     * TODO: documentation
     */
    public static void showMessageDialog(SComponent parent,
                                         Object message,
                                         String title) {
        showMessageDialog(parent, message, title, 0, null);
    }

    /**
     * TODO: documentation
     */
    public static void showMessageDialog(SComponent parent,
                                         Object message, String title,
                                         int messageType,
                                         ActionListener al) {
        SOptionPane p = new SOptionPane();

        p.showPlainMessage(parent, message, title);
        p.addActionListener(al);
    }

    /**
     * TODO: documentation
     */
    public static void showPlainMessageDialog(SComponent parent,
                                              Object message,
                                              String title) {
        showPlainMessageDialog(parent, message, title, 0, null);
    }


    /**
     * TODO: documentation
     */
    public static void showPlainMessageDialog(SComponent parent,
                                              Object message, String title,
                                              int messageType,
                                              ActionListener al) {
        SOptionPane p = new SOptionPane();

        p.showPlainMessage(parent, message, title);
        p.addActionListener(al);

        messageImage.setVisible(false);
    }

    /**
     * TODO: documentation
     */
    public void showPlainMessage(SComponent parent, Object message,
                                 String title) {
        showOption(parent, title, message);

        setOptionType(DEFAULT_OPTION);

        messageImage.setVisible(true);
    }


    /**
     * TODO: documentation
     */
    public void showPlainQuestionDialog(SComponent parent,
                                        Object message, String title) {
        showOption(parent, title, message);

        setOptionType(OK_CANCEL_OPTION);

        questionImage.setVisible(true);
    }

    /**
     * TODO: documentation
     *
     * @param message
     * @return
     */
    // protected String getSimpleInput(Object message) {
    //     return JOptionPane.showInputDialog(this, message);
    // }


    /**
     * TODO: documentation
     */
    public static void showQuestionDialog(SComponent parent,
                                          Object question, String title,
                                          ActionListener al) {
        SOptionPane p = new SOptionPane();

        p.showPlainQuestionDialog(parent, question, title);
        p.addActionListener(al);
    }

    /**
     * TODO: documentation
     */
    public static void showPlainQuestionDialog(SComponent parent,
                                               Object question, String title,
                                               ActionListener al) {
        SOptionPane p = new SOptionPane();

        p.showPlainQuestionDialog(parent, question, title);
        p.addActionListener(al);

        questionImage.setVisible(false);
    }


    /**
     * TODO: documentation
     */
    public static void showConfirmDialog(SComponent parent,
                                         Object message, String title) {
        showConfirmDialog(parent, message, title, 0, null);
    }

    /**
     * TODO: documentation
     */
    public static void showConfirmDialog(SComponent parent,
                                         Object message, String title,
                                         ActionListener al) {
        showConfirmDialog(parent, message, title, 0, al);
    }

    /**
     * TODO: documentation
     */
    public static void showConfirmDialog(SComponent parent,
                                         Object message, String title,
                                         int type) {
        showYesNoDialog(parent, message, title, null);
    }

    /**
     * TODO: documentation
     */
    public static void showConfirmDialog(SComponent parent,
                                         Object message, String title,
                                         int type,
                                         ActionListener al) {
        showYesNoDialog(parent, message, title, al);
    }

    public void showYesNo(SComponent parent, Object question,
                          String title) {
        showOption(parent, title, question);
        setOptionType(YES_NO_OPTION);

        yesnoImage.setVisible(true);
    }

    /**
     * TODO: documentation
     */
    public static void showYesNoDialog(SComponent parent, Object question,
                                       String title, ActionListener al) {
        SOptionPane p = new SOptionPane();
        p.addActionListener(al);

        p.showYesNo(parent, question, title);
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(OptionPaneCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
