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

package org.wings.plaf.xhtml.old;

import java.awt.Color;
import java.io.IOException;
import org.wings.SFont;
import org.wings.SLabel;
import org.wings.io.Device;

public final class LabelCG
    extends org.wings.plaf.xhtml.LabelCG
{
    /**
     *
     */
    protected void writeText(Device device, SLabel label) throws IOException {
        final SFont font = label.getFont();
        final Color foreground = label.getForeground();

        Utils.writeFontPrefix(device, font, foreground);
        super.writeText(device, label);
        Utils.writeFontPostfix(device, font, foreground);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
