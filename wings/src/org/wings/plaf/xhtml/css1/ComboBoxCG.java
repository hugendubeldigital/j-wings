package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class ComboBoxCG
    extends org.wings.plaf.xhtml.ComboBoxCG
{
    public void writeFormPrefix(Device d, SComboBox comboBox)
        throws IOException
    {
	Style style = comboBox.getStyle();
	
	d.append("<select name=\"");
	d.append(comboBox.getNamePrefix());
	d.append("\" size=\"1\"");
	Utils.writeStyleAttribute(d, style);
	d.append(">\n");
    }	
}
