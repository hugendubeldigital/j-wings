package org.wings.plaf.xhtml.old;

import java.io.IOException;
import javax.swing.Icon;

import java.awt.Color;
import java.util.Iterator;

import org.wings.*;
import org.wings.externalizer.ExternalizeManager;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class FrameCG
    extends org.wings.plaf.xhtml.FrameCG
    implements org.wings.plaf.FrameCG
{
    protected void writeBody(Device d, SFrame frame)
        throws IOException
    {
	Icon backgroundImage = frame.getBackgroundImage();
	String backgroundURL = frame.getBackgroundURL();
	
	Color backgroundcolor = frame.getBackground();
	Color textcolor = frame.getTextColor();
	Color linkcolor = frame.getLinkColor();
	Color alinkcolor = frame.getALinkColor();
	Color vlinkcolor = frame.getVLinkColor();
	
	d.append("<body");
	
	if (backgroundURL == null)
	    if (backgroundImage != null) {
		ExternalizeManager ext = frame.getExternalizeManager();
		backgroundURL = ext.externalize(backgroundImage);
	    }
	if (backgroundURL != null) {
	    d.append(" background=\"");
	    d.append(backgroundURL);
	    d.append("\"");
	}
	if (backgroundcolor != null) {
	    d.append(" bgcolor=\"#");
	    d.append(SUtil.toColorString(backgroundcolor));
	    d.append("\"");
	}
	if (textcolor != null) {
	    d.append(" text=\"#");
	    d.append(SUtil.toColorString(textcolor));
	    d.append("\"");
	}
	if (linkcolor != null) {
	    d.append(" link=\"#");
	    d.append(SUtil.toColorString(linkcolor));
	    d.append("\"");
	}
	if (vlinkcolor != null) {
	    d.append(" vlink=\"#");
	    d.append(SUtil.toColorString(vlinkcolor));
	    d.append("\"");
	}
	if (alinkcolor != null) {
	    d.append(" alink=\"#");
	    d.append(SUtil.toColorString(alinkcolor));
	    d.append("\"");
	}
	
	d.append(">\n");
	
	writeContents(d, frame);
	
	d.append("\n</body>\n</html>");
    }
}
