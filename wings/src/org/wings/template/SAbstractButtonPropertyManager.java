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

package org.wings.template;

import org.wings.SURLIcon;
import org.wings.SComponent;
import org.wings.SAbstractButton;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SAbstractButtonPropertyManager extends SComponentPropertyManager
{
    static final Class[] classes = {SAbstractButton.class};

    public SAbstractButtonPropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        SAbstractButton c = (SAbstractButton) comp;
        if ( name.equals("TEXT") )
            c.setText(value);
        else if ( name.startsWith("ICON") ) {
            if ( name.equals("ICON") )
                c.setIcon(new SURLIcon(value));
            else if ( name.equals("ICONWIDTH") ) {
                try {
                    int width = Integer.parseInt(value);
                    if ( c.getIcon()!=null ) {
                        c.getIcon().setIconWidth(width);
                    } // end of if ()
                    if ( c.getDisabledIcon()!=null ) {
                        c.getDisabledIcon().setIconWidth(width);
                    } // end of if ()
                    if ( c.getSelectedIcon()!=null ) {
                        c.getSelectedIcon().setIconWidth(width);
                    } // end of if ()
                    if ( c.getRolloverIcon()!=null ) {
                        c.getRolloverIcon().setIconWidth(width);
                    } // end of if ()
                    if ( c.getRolloverSelectedIcon()!=null ) {
                        c.getRolloverSelectedIcon().setIconWidth(width);
                    } // end of if ()
                    if ( c.getPressedIcon()!=null ) {
                        c.getPressedIcon().setIconWidth(width);
                    } // end of if ()
                } catch ( NumberFormatException ex ) {
                } // end of try-catch
            } else if ( name.equals("ICONHEIGHT") ) {
                try {
                    int height = Integer.parseInt(value);
                    if ( c.getIcon()!=null ) {
                        c.getIcon().setIconHeight(height);
                    } // end of if ()
                    if ( c.getDisabledIcon()!=null ) {
                        c.getDisabledIcon().setIconHeight(height);
                    } // end of if ()
                    if ( c.getSelectedIcon()!=null ) {
                        c.getSelectedIcon().setIconHeight(height);
                    } // end of if ()
                    if ( c.getRolloverIcon()!=null ) {
                        c.getRolloverIcon().setIconHeight(height);
                    } // end of if ()
                    if ( c.getRolloverSelectedIcon()!=null ) {
                        c.getRolloverSelectedIcon().setIconHeight(height);
                    } // end of if ()
                    if ( c.getPressedIcon()!=null ) {
                        c.getPressedIcon().setIconHeight(height);
                    } // end of if ()
                } catch ( NumberFormatException ex ) {
                } // end of try-catch
            } 
        } else if ( name.equals("DISABLEDICON") )
            c.setDisabledIcon(new SURLIcon(value));
        else if ( name.equals("SELECTEDICON") )
            c.setSelectedIcon(new SURLIcon(value));
        else if ( name.equals("ROLLOVERSELECTEDICON") )
            c.setRolloverSelectedIcon(new SURLIcon(value));
        else if ( name.equals("PRESSEDICON") )
            c.setPressedIcon(new SURLIcon(value));
        else if ( name.equals("ACCESSKEY") )
            c.setMnemonic(value);
        else if ( name.equals("TARGET") )
            c.setRealTarget(value);
        else
            super.setProperty(comp, name, value);
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
