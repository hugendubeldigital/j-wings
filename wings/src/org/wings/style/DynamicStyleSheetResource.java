package org.wings.style;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.net.*;
import java.util.*;

import org.wings.*;
import org.wings.io.*;
import org.wings.util.*;

public class DynamicStyleSheetResource
    extends DynamicResource
{
    protected String extension = "css";
    protected String mimeType = "text/css";
    protected int epoch = 1;

    protected SFrame frame;

    public DynamicStyleSheetResource(SFrame frame) {
	this.frame = frame;
	this.extension = "css";
	this.extension = "text/css";
    }

    public void write(Device out)
	throws IOException
    {
	StyleSheetWriter visitor = new StyleSheetWriter(out);
	visitor.visit(frame);
    }

    protected static class StyleSheetWriter
	implements ComponentVisitor
    {
	Device out;

	public StyleSheetWriter(Device out) {
	    this.out = out;
	}

	public void visit(SComponent component) {
	    out.append(component.getAttributes().toString());
	}
    }

    public void invalidate() {
	epoch++;
    }
    public int getEpoch() {
        return epoch;
    }

    public final String getExtension() {
        return extension;
    }

    public final String getMimeType() {
        return mimeType;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        return mimeType + " " + getEpoch();
    }
}
