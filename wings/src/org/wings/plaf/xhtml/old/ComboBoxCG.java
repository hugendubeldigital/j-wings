package org.wings.plaf.xhtml.old;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class ComboBoxCG
    extends org.wings.plaf.xhtml.ComboBoxCG
{
    public void writeFormPrefix(Device d, SComboBox comboBox)
        throws IOException
    {
	SFont font = comboBox.getFont();
	Color foreground = comboBox.getForeground();
	Utils.writeFontPrefix(d, font, foreground);

	super.writeFormPrefix(d, comboBox);
    }

    protected void writeFormPostfix(Device d, SComboBox comboBox)
	throws IOException
    {
	super.writeFormPostfix(d, comboBox);

	SFont font = comboBox.getFont();
	Color foreground = comboBox.getForeground();
	Utils.writeFontPostfix(d, font, foreground);
    }
}
