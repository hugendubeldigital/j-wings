package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class FileChooserCG
    extends org.wings.plaf.xhtml.FileChooserCG
{
    public void writePrefix(Device d, SFileChooser fileChooser)
	throws IOException
    {
	Style style = fileChooser.getStyle();
	
	super.writePrefix(d, fileChooser);
	Utils.writeStyleAttribute(d, style);
    }
}
