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
import javax.swing.event.EventListenerList;
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
 * SOptionPane is used to easy create some standard dialogs.
 * Following example creates two connected dialogs.
 * First an question dialog with "yes"- and "no"-button is shown. If User answers
 * "yes" an dialog with the text "Fine !" is exposed, otherwise 
 * an dialog with the message "No Problem ..." is shown.
 * <blockquote><pre>
 * final ActionListener comment = new ActionListener() {
 *     public void actionPerformed(ActionEvent e) {
 *         if ( e.getActionCommand()==SOptionPane.OK_ACTION )
 *             SOptionPane.showMessageDialog(frame, "Fine !");
 *         else
 *             SOptionPane.showMessageDialog(frame, 
 *                 "No Problem, just look at another site");
 *         }
 *     };
 *
 * SOptionPane.showQuestionDialog(frame, "Do you want to go on?",
 *     "A Question", comment);
 * </pre></blockquote>
 * Not like the swing JOptionPane, these option dialogs are non-blocking. Therefore 
 * you need Events, to get the result.
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
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
    public static final int OK_CANCEL_RESET_OPTION = 999;

    /**
     * TODO: documentation
     */
    public static final int YES_NO_OPTION = JOptionPane.YES_NO_OPTION;

    /**
     * TODO: documentation
     */
    public static final int YES_NO_RESET_OPTION = 998;

    /**
     * TODO: documentation
     */
    public static final int YES_NO_CANCEL_OPTION = JOptionPane.YES_NO_CANCEL_OPTION;

    /**
     * TODO: documentation
     */
    public static final int YES_NO_CANCEL_RESET_OPTION = 997;

    /**
     * TODO: documentation
     */
    public static final String OK_ACTION = "OK";

    /**
     * TODO: documentation
     */
    public static final String CANCEL_ACTION = "CANCEL";

    /**
     * TODO: documentation
     */
    public static final String UNKNOWN_ACTION = "UNKNOWN";

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
     * Der Title des OptionPanes. Wird ganz oben dargestellt abgesetzt
     * durch einen Separator.
     */
    private final SLabel optionTitle = new SLabel();

    /**
     * TODO: documentation
     */
    protected static SImage messageImage =
        new SImage(new ResourceImageIcon("org/wings/icons/Inform.gif"));

    /**
     * TODO: documentation
     */
    protected static SImage questionImage =
        new SImage(new ResourceImageIcon("org/wings/icons/Question.gif"));

    /**
     * TODO: documentation
     */
    protected static SImage yesnoImage =
        new SImage(new ResourceImageIcon("org/wings/icons/Question.gif"));

    /**
     * The container for the contentPane.
     */
    protected final SForm contentPane = new SForm();
    
    /**
      * ActionListeners for this Dialog.
      */
    private java.util.List actionListeners = new java.util.ArrayList();


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
        contentPane.setLayout(layout);
        super.getContentPane().add(contentPane);
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
        messageImage.setAlternativeText("info");
        images.add(questionImage);
        questionImage.setAlternativeText("question");
        images.add(yesnoImage);
        yesnoImage.setAlternativeText("question");

        contentPane.add(optionTitle);
        contents.add(optionData, SBorderLayout.CENTER);
        contents.add(images, SBorderLayout.WEST);
        contentPane.add(contents);

        contentPane.add(optionButtons);
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
        if (c instanceof SContainer)
            frame = (SContainer)c;
        else
            frame = c.getParent();

        while (frame != null && !(frame instanceof SFrame || frame instanceof SInternalFrame) )
            frame = frame.getParent();

        if (frame == null) {
            throw new IllegalArgumentException("No parent Frame");
        }

        if (frame instanceof SFrame)
            ((SFrame)frame).pushDialog(this);
        else
            ((SInternalFrame)frame).pushDialog(this);

        optionTitle.setText(title);

        optionData.removeAll();
        if ( message instanceof SComponent ) {
            optionData.add((SComponent)message);
        }
        else {
            optionData.add(new SLabel(message.toString()));
        }
    }

    /**
     * TODO: documentation
     */
    public static void showMessageDialog(SComponent parent,
                                         Object message,
                                         ActionListener al) {
        showMessageDialog(parent, message, "", 0, al);
    }

    /**
     * TODO: documentation
     */
    public static void showMessageDialog(SComponent parent,
                                         Object message) {
        showMessageDialog(parent, message, "", 0, null);
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
        p.setTitle(title);
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
        p.setTitle(title);
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
     * Show an Question dialog with question image.
     */
    public static void showQuestionDialog(SComponent parent,
                                          Object question, String title,
                                          ActionListener al) {
        SOptionPane p = new SOptionPane();
        p.showPlainQuestionDialog(parent, question, title);
        p.addActionListener(al);
    }

    /**
     * Show an Question dialog without question image.
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
     * Make an question dialog with question image.
     */
    public void showPlainQuestionDialog(SComponent parent,
                                        Object message, String title) {
        showOption(parent, title, message);
        setTitle(title);
        setOptionType(OK_CANCEL_RESET_OPTION);

        questionImage.setVisible(true);
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
      * Gets the contentPane object for this dialog.
      * return a {@link org.wings.SForm}.
      */
    public SContainer getContentPane() {
        return contentPane;
    }


    /**
     * TODO: documentation
     */
    public static void showYesNoDialog(SComponent parent, Object question,
                                       String title, ActionListener al) {
        SOptionPane p = new SOptionPane();
        p.setTitle(title);
        p.addActionListener(al);

        p.showYesNo(parent, question, title);
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "OptionPaneCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(OptionPaneCG cg) {
        super.setCG(cg);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getEncodingType() {
        return contentPane.getEncodingType();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean getMethod() {
        return contentPane.getMethod();
    }
    
    /**
      * Fire action event with given state to all listeners.
      * @see #fireActionPerformed(ActionEvent)
      */
    protected void fireActionPerformed(String state) {
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, state);
        fireActionPerformed(e);
    }

    /**
      * Fire this action event to all listeners.
      */
    protected void fireActionPerformed(ActionEvent e) {
        for (int i=0; i<actionListeners.size(); i++) {
            ((ActionListener) actionListeners.get(i)).actionPerformed(e);
        }
    }

    /**
     * Called, when a button inside the option pane
     * is pressed.
     * @see #fireActionPerformed(String)
     * @param e the event from the calling button
     */
    public void actionPerformed(ActionEvent e) {
        hide();
            
        selected = e.getSource();

        if ( selected==optionOK ) {
            fireActionPerformed(OK_ACTION);
        }
        else if ( selected==optionYes ) {
            fireActionPerformed(YES_ACTION);
        }
        else if ( selected==optionCancel ) {
            fireActionPerformed(CANCEL_ACTION);
        }
        else if ( selected==optionNo ) {
            fireActionPerformed(NO_ACTION);
        }
        else {
            fireActionPerformed(UNKNOWN_ACTION);
        }

        removeAll();
    }

    /**
      * Add the given actionlistener to this option pane.
      * If component receives event from SOptionPane 
      * {@link java.awt.event.ActionEvent.getModifiers()} returns 
      * one of the following values:
      * <li><tt>OK_ACTION</tt>: "OK"-Button was pressed</li>
      * <li><tt>YES_ACTION</tt>: "YES"-Button was pressed</li>
      * <li><tt>CANCEL_ACTION</tt>: "CANCEL"-Button was pressed</li>
      * <li><tt>NO_ACTION</tt>: "NO"-Button was pressed</li>
      * <li><tt>UNKNOWN_ACTION</tt>: unknown button was pressed</li>
      */
    public void addActionListener(ActionListener al) {
        if (al != null)
            actionListeners.add(al);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
