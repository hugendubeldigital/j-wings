package org.wings.plaf.xhtml.old;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class CheckBoxCG
    extends org.wings.plaf.xhtml.CheckBoxCG
{
    protected void writeAnchorBody(Device d, SCheckBox checkBox)
        throws IOException
    {
	String text = checkBox.getText();
	boolean noBreak = checkBox.isNoBreak();
	
	SFont font = checkBox.getFont();
	Color foreground = checkBox.getForeground();
	
	Utils.writeFontPrefix(d, font, foreground);
	if (noBreak)
	    d.append("<nobr>");
	d.append((text != null) ? text : "");
	if (noBreak)
	    d.append("</nobr>");
	Utils.writeFontPostfix(d, font);
    }
    
    protected void writeFormText(Device d, SCheckBox checkBox)
        throws IOException
    {
	SFont font = checkBox.getFont();
	Color foreground = checkBox.getForeground();
	
	Utils.writeFontPrefix(d, font, foreground);
	d.append(checkBox.getText());
	Utils.writeFontPostfix(d, font);
    }
    
    protected void writeFormIcon(Device d, SCheckBox checkBox)
        throws IOException
    {
	SFont font = checkBox.getFont();
	
	Utils.writeFontPrefix(d, font);
	writeFormPrefix(d, checkBox);
	writeFormBody(d, checkBox);
	writeFormPostfix(d, checkBox);
	Utils.writeFontPostfix(d, font);
    }
}
