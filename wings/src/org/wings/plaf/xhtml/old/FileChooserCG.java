package org.wings.plaf.xhtml.old;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class FileChooserCG
    extends org.wings.plaf.xhtml.FileChooserCG
{
    public void writePrefix(Device d, SFileChooser fileChooser)
	throws IOException
    {
	SFont font = fileChooser.getFont();
	Color foreground = fileChooser.getForeground();
	
	Utils.writeFontPrefix(d, font, foreground);
	super.writePrefix(d, fileChooser);
    }
    
    public void writePostfix(Device d, SFileChooser fileChooser)
	throws IOException
    {
	SFont font = fileChooser.getFont();
	
	super.writePostfix(d, fileChooser);
	Utils.writeFontPostfix(d, font);
    }
}
