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

import java.awt.Insets;
import java.io.IOException;

import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface SBorder {
    void setInsets(Insets insets);
    Insets getInsets();
    
    /**
      * Get the color of the border.
      */
    java.awt.Color getColor();

    /**
      * Get the color of this border.
      */
    void setColor( java.awt.Color color );

    void writePrefix(Device d) throws IOException;
    void writePostfix(Device d) throws IOException;
    void writeSpanAttributes( Device d ) throws IOException;

    String getCGClassID();
    void updateCG();
    /**
      * Get the thickness in pixel for this border.
      * @return thickness
      * @see #setThickness(int)
      */
	public int getThickness();

    /**
      * Set the thickness in pixel for this border.
      * @param thickness
      * @see #getThickness()
      */
	public void setThickness( int thickness );
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
