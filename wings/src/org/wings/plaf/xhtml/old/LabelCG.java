// $Id$

package org.wings.plaf.xhtml.old;

import java.awt.Color;
import java.io.IOException;
import org.wings.SFont;
import org.wings.SLabel;
import org.wings.io.Device;

public final class LabelCG extends org.wings.plaf.xhtml.LabelCG {

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
