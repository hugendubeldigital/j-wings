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

import java.net.URL;

import javax.swing.*;

import org.wings.plaf.*;
import org.wings.io.Device;
import org.wings.externalizer.ExternalizeManager;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SLabel
    extends SComponent
    implements SConstants
{
    private static final String cgClassID = "LabelCG";

    protected String text;

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

    private int verticalTextPosition = CENTER;
    private int horizontalTextPosition = RIGHT;
    private int iconTextGap = 1;
    private boolean noBreak = false;
    private boolean alignText = false;

    // TODO: plaf has to escape the special chars...
    // HEN: I think, this is not necessary here; The label's plaf should 
    // _always_ escape the characters, otherwise the user feels tempted to
    // output formatting information in Labels .. (s)he shouldn't!
    private boolean escapeSpecialChars = false;


    /**
     * TODO: documentation
     *
     * @param t
     */
    public SLabel(String t) {
        this(t, null, LEFT);
    }

    /**
     * TODO: documentation
     *
     */
    public SLabel() {
        this((String)null);
    }

    /**
     * TODO: documentation
     *
     * @param icon
     */
    public SLabel(Icon icon) {
        this(icon, LEFT);
    }

    public SLabel(Icon icon, int horizontalAlignment) {
        this(null, icon, horizontalAlignment);
    }

    public SLabel(String text, Icon icon, int horizontalAlignment) {
        setText(text);
        setIcon(icon);
        setHorizontalAlignment(horizontalAlignment);
    }

    public SLabel(String text, int horizontalAlignment) {
        this(text, null, horizontalAlignment);
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setAlignText(boolean t) {
        alignText = t;
    }

    /**
     * TODO: documentation
     *
     * @param b
     */
    public void setNoBreak(boolean b) {
        noBreak = b;
    }
    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isNoBreak() { return noBreak; }

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
    public void setHorizontalTextPosition(int textPosition) {
        horizontalTextPosition = textPosition;
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
        Icon oldIcon = icon;
        icon = i;
        if ((icon == null && oldIcon != null) ||
            (icon != null && !icon.equals(oldIcon)))
            reload();
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
        String oldIconAddress = iconAddress;
        iconAddress = url;
        if ((iconAddress == null && oldIconAddress != null) ||
            (iconAddress != null && !iconAddress.equals(oldIconAddress)))
            reload();
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
        Icon oldDisabledIcon = disabledIcon;
        disabledIcon = i;
        if ((disabledIcon == null && oldDisabledIcon != null) ||
            (disabledIcon != null && !disabledIcon.equals(oldDisabledIcon)))
            reload();
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
        String oldDisabledIconAddress = disabledIconAddress;
        disabledIconAddress = url;
        if ((disabledIconAddress == null && oldDisabledIconAddress != null) ||
            (disabledIconAddress != null && !disabledIconAddress.equals(oldDisabledIconAddress)))
            reload();
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
    public String getText() {
        return text;
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setText(String t) {
        String oldText = text;
        text = t;
        if ((text == null && oldText != null) ||
            (text != null && !text.equals(oldText)))
            reload();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isEscapeSpecialChars() {
	return escapeSpecialChars;
    }

    /**
     * TODO: documentation
     *
     * @deprecated Never set this - characters are always escaped
     *             in future and BTW, you shouldn't rely on an
     *             HTML output, right ?
     * @param escape
     */
    public void setEscapeSpecialChars(boolean escape) {
	escapeSpecialChars = escape;
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "LabelCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(LabelCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
