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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.*;

import java.util.ArrayList;

import org.wings.plaf.*;
import org.wings.io.Device;
import org.wings.externalizer.ExternalizeManager;

/**
 * TODO: documentation
 *
 * @author Dominik Bartenstein
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SButton extends SAbstractButton
{
    private static final String cgClassID = "ButtonCG";

    /**
     * TODO: documentation
     */
    protected Icon icon = null;

    /**
     * TODO: documentation
     */
    protected String iconAddress = null;

    /**
     * TODO: documentation
     */
    protected Icon disabledIcon = null;

    /**
     * TODO: documentation
     */
    protected String disabledIconAddress = null;

    /**
     * TODO: documentation
     */
    protected int verticalTextPosition = CENTER;
    /**
     * TODO: documentation
     */
    protected int horizontalTextPosition = RIGHT;

    /**
     * TODO: documentation
     */
    protected  int iconTextGap = 1;

    /**
     * TODO: documentation
     *
     * @param text
     */
    public SButton(String text) {
        super(text);
    }

    /**
     * TODO: documentation
     *
     */
    public SButton() {
        super(null);
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public SButton(Icon i) {
        super(null);
        setIcon(i);
    }

    /**
     * TODO: documentation
     *
     * @param textPosition
     */
    public void setHorizontalTextPosition(int textPosition) {
        horizontalTextPosition = textPosition;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getHorizontalTextPosition() {
        return horizontalTextPosition;
    }

    /**
     * TODO: documentation
     *
     * @param textPosition
     */
    public void setVerticalTextPosition(int textPosition) {
        verticalTextPosition = textPosition;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getVerticalTextPosition() {
        return verticalTextPosition;
    }

    /**
     * TODO: documentation
     *
     * @param gap
     */
    public void setIconTextGap(int gap) {
        iconTextGap = gap;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getIconTextGap() {
        return iconTextGap;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setIcon(Icon i) {
        icon = i;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setIcon(URL i) {
        if ( i!=null)
            setIcon(i.toString());
    }

    /**
     * TODO: documentation
     *
     * @param url
     */
    public void setIcon(String url) {
        iconAddress = url;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getIconAddress() {
        return iconAddress;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledIcon(Icon i) {
        disabledIcon = i;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledIcon(URL i) {
        if ( i!=null)
            setDisabledIcon(i.toString());
    }

    /**
     * TODO: documentation
     *
     * @param url
     */
    public void setDisabledIcon(String url) {
        disabledIconAddress = url;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Icon getDisabledIcon() {
        if(disabledIcon == null)
            if(icon != null && icon instanceof ImageIcon)
                disabledIcon = new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon)icon).getImage()));
        return disabledIcon;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getDisabledIconAddress() {
        return disabledIconAddress;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    protected boolean showIcon() {
        return ( icon!=null || iconAddress!=null );
    }

    /**
     * TODO: documentation
     */
    public static void main(String[] args) {

        SFrame frame = new SFrame();

        SPanel erg = new SPanel(new SFlowDownLayout());
        SForm form = new SForm();
        SButton b = new SButton("form");
        b.setIcon("http://www.mercatis.de/pics/pikto.gif");
        form.add(b);
        erg.add(form);

        b = new SButton();
        b.setIcon("http://www.mercatis.de/pics/pikto.gif");
        erg.add(b);

        frame.getContentPane().add(erg);
        System.out.println(frame.show());
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "ButtonCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
