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

import java.awt.Color;
import javax.swing.Icon;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SToggleButton
    extends SRadioButton
{
    private static final String cgClassID = "ToggleButtonCG";

    /**
     * TODO: documentation
     */
    protected Color selectedForeground = Color.red;

    /**
     * TODO: documentation
     */
    protected Color selectedBackground = null;

    /**
     * TODO: documentation
     */
    protected Color backupForeground;

    /**
     * TODO: documentation
     */
    protected Color backupBackground;

    /**
     * TODO: documentation
     *
     * @param text
     */
    public SToggleButton(String text) {
        super(text);

        setIcon((Icon)null);
        setDisabledIcon((Icon)null);
        setSelectedIcon((Icon)null);
        setDisabledSelectedIcon((Icon)null);
    }

    /**
     * TODO: documentation
     *
     */
    public SToggleButton() {
        this(null);
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setSelectedForeground(Color c) {
        selectedForeground = c;
        if ( selected )
            setForeground(c);
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setSelectedBackground(Color c) {
        selectedBackground = c;
        if ( selected )
            setBackground(c);
    }

    /*
     * Da gibt es ein Problem mit der Interpretation von getForeground und
     * getBackground. Sind dies die gerade aktiven Farben, oder die default
     * Farben?? Auf alle Faelle
     * werden diese Methoden von verschiedenen Layoutmanagern benutzt (alle, die
     * eine Table zum layouten nutzen) um die
     * Farben mit Hilfe eines Zell Attributes setzen, um so das FONT bzw STYLE Tag
     * zu umgehen. Eventuell muss um das eindeutig zu bekommen in SComponent
     * die Farben um 2 weitere Farben ergaenzt werden, ActualForeground und
     * ActualBackground.
     */
    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setSelected(boolean s) {
        if ( selected != s ) {
            if ( s ) {
                backupForeground = getForeground();
                backupBackground = getBackground();
                setForeground(selectedForeground);
                setBackground(selectedBackground);
            }
            else {
                setForeground(backupForeground);
                setBackground(backupBackground);
            }
        }
        super.setSelected(s);
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "ToggleButtonCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(ToggleButtonCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
