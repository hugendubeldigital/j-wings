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
import java.beans.*;
import java.net.URL;
import javax.swing.*;

import java.util.ArrayList;

import org.wings.plaf.*;
import org.wings.io.Device;
import org.wings.externalizer.ExternalizeManager;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:andre.lison@crosstec.de">Andre Lison</a>
 * @author Dominik Bartenstein
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SMenuItem extends SButton
{
    private static final String cgClassID = "ButtonCG";

    /**
     * Create a new MenuItem.
     * <i>noBreak</i> is set to true
     * @see org.wings.SAbstractButton#setNoBreak(boolean)
     * @param text is display this text ( as href )
     */
    public SMenuItem( String text )
    {
        super( text );
        setNoBreak( true );
    }

    /**
     * Create a new MenuItem with default test "Button".
     * <i>noBreak</i> is set to true
     * @see org.wings.SAbstractButton#setNoBreak(boolean)
     */
    public SMenuItem( )
    {
        super( "Button" );
        setNoBreak( true );
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
