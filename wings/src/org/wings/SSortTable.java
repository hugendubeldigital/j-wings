/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import javax.swing.table.TableModel;
import javax.swing.ImageIcon;

import org.wings.io.Device;
import org.wings.plaf.*;
import org.wings.externalizer.ExternalizeManager;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SSortTable
    extends STable
    implements SConstants
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "SortTableCG";

    // should be part of look and feel
    /**
     * TODO: documentation
     */
    public static ImageIcon DEFAULT_SORT_UP = null;

    /**
     * TODO: documentation
     */
    public static ImageIcon DEFAULT_SORT_DOWN = null;

    static {
        createImages();
    }

    /**
     * TODO: documentation
     *
     * @param tm
     */
    public SSortTable(TableModel tm){
        super(tm);
        setSelectionMode(SINGLE_SELECTION);
    }


    static void createImages() {
        try {
            BufferedImage image;
            Graphics2D graphics;

            image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            graphics = image.createGraphics();
            graphics.setColor(java.awt.Color.red);
            graphics.drawLine(5, 0, 5, 10);
            graphics.drawLine(5, 0, 2, 4);
            graphics.drawLine(5, 0, 8, 4);
            DEFAULT_SORT_UP = new ImageIcon(image);

            image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            graphics = image.createGraphics();
            graphics.setColor(java.awt.Color.red);
            graphics.drawLine(5, 0, 5, 10);
            graphics.drawLine(5, 10, 2, 6);
            graphics.drawLine(5, 10, 8, 6);
            DEFAULT_SORT_DOWN = new ImageIcon(image);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        catch (Error e) {
            e.printStackTrace();
        }
    }

    public void sort(int col, boolean asc) {
        if ( model instanceof TableSorter )
            ((TableSorter)model).sort(col, asc);
    }

    /**
     * TODO: documentation
     *
     * @param v
     * @return
     */
    protected int calcColumnOfAction(String v) {
        return Integer.parseInt(v.substring(1));
    }

    public void getPerformed(String action, String value) {
        if ( value.startsWith("u") ) {
            sort(calcColumnOfAction(value), true);
        }
        else if ( value.startsWith("d") ) {
            sort(calcColumnOfAction(value), false);
        }
        super.getPerformed(action, value);
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "SortTableCG"
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
