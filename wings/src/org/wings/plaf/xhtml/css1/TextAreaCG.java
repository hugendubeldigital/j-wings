package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class TextAreaCG
    extends org.wings.plaf.xhtml.TextAreaCG
{
    public void writePrefix(Device d, STextArea textArea)
	throws IOException
    {
	Style style = textArea.getStyle();
	
	super.writePrefix(d, textArea);
	Utils.writeStyleAttribute(d, style);
    }
}
