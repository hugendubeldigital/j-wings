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

package org.wings;

import java.awt.Color;
import java.io.IOException;
import java.util.*;

import org.wings.io.Device;
import org.wings.plaf.*;
import org.wings.session.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SFrameSetLayout
    extends SAbstractLayoutManager
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "FrameSetLayoutCG";

    private List components = new LinkedList();
    private List constraints = new LinkedList();

    private String[] columns;
    private String[] rows;

    public SFrameSetLayout(String cols, String rows) {
	setColumns(cols);
	setRows(rows);
    }

    public SFrameSetLayout(String[] cols, String[] rows) {
	setColumns(cols);
	setRows(rows);
    }

    public void setColumns(String colstring) {
	if (colstring != null) {
	    StringTokenizer tokens = new StringTokenizer(colstring, ",");
	    String[] newcols = new String[tokens.countTokens()];
	    int i=0;
	    while (tokens.hasMoreTokens())
		newcols[i++] = tokens.nextToken().trim();
	    setColumns(newcols);
	}
    }

    public void setColumns(String[] columns) {
	this.columns = columns;
    }

    public void setRows(String rowstring) {
	if (rowstring != null) {
	    StringTokenizer tokens = new StringTokenizer(rowstring, ",");
	    String[] newrows = new String[tokens.countTokens()];
	    int i=0;
	    while (tokens.hasMoreTokens())
		newrows[i++] = tokens.nextToken().trim();
	    setRows(newrows);
	}
    }

    public void setRows(String[] rows) {
	this.rows = rows;
    }

    public void addComponent(SComponent c, Object constraint) {
        components.add(c);
	constraints.add(constraint);
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void removeComponent(SComponent c) {
        if (c == null)
            return;

	int index = components.indexOf(c);
	components.remove(index);
	constraints.remove(index);
    }

    public void write(Device d)
	throws IOException
    {
	SFrameSet frameSet = (SFrameSet)getContainer();

	if (frameSet.getParent() == null) {
	    String language = "en"; // TODO: ???
	    String title = frameSet.getTitle();

	    d.append("<?xml version=\"1.0\" encoding=\"");
	    d.append(frameSet.getSession().getCharSet());
	    d.append("\"?>\n");
	    d.append("<!DOCTYPE html\n");
	    d.append("   PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\"\n");
	    d.append("   \"DTD/xhtml1-frameset.dtd\">\n");
	    d.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"");
	    d.append(language);
	    d.append("\" lang=\"");
	    d.append(language);
	    d.append("\">\n");
	    d.append("<head>\n<title>");
	    d.append(title);
	    d.append("</title>\n");
	    d.append("</head>\n");

	    d.append("<frameset rows=\"*,10\" frameborder=\"0\" noresize=\"noresize\" scrolling=\"no\">\n");
	}

	d.append("<frameset");

	if (columns != null && columns.length > 0) {
	    d.append(" cols=\"");
	    d.append(columns[0]);
	    for (int i=1; i < columns.length; i++) {
		d.append(",");
		d.append(columns[i]);
	    }
	    d.append("\"");
	}

	if (rows != null && rows.length > 0) {
	    d.append(" rows=\"");
	    d.append(rows[0]);
	    for (int i=1; i < rows.length; i++) {
		d.append(",");
		d.append(rows[i]);
	    }
	    d.append("\"");
	}

	d.append(">\n");

	Iterator iterator = components.iterator();
	int i=0;
	while (iterator.hasNext()) {
	    Object component = iterator.next();
	    if (component instanceof SFrameSet)
		((SFrameSet)component).write(d);
	    else if (component instanceof SFrame)
		writeFrame(d, (SFrame)component, (Properties)constraints.get(i));
	    else
		break;
	    i++;
	}

	d.append("</frameset>\n");

	if (frameSet.getParent() == null) {
	    frameSet.getSession().getReloadManager().clearDirtyComponents();
	    writeFrame(d, (SFrame)frameSet.getSession().getReloadManager().getManagerComponent(), null);
	    /*
	    d.append("<frame name=\"")
		.append(frameSet.getSession().getReloadManager().getTarget())
		.append("\" />");
	    */
	    d.append("</frameset>\n");
	}
    }

    protected void writeFrame(Device d, SFrame frame, Properties properties)
	throws IOException
    {
	String src = getSession().getExternalizeManager()
	    .externalize(frame.show(), "text/html");

	d.append("<frame src=\"")
	    .append(src)
	    .append("\"");
	d.append(" name=\"frame")
	    .append(frame.getUnifiedIdString())
	    .append("\"");

	if (properties != null) {
	    Iterator iterator = properties.entrySet().iterator();
	    while (iterator.hasNext()) {
		Map.Entry entry = (Map.Entry)iterator.next();
		d.append(' ');
		d.append((String)entry.getKey());
		d.append("=\"");
		d.append((String)entry.getValue());
		d.append('"');
	    }
	}
	d.append(" />\n");
    }

    private Session session = null;

    protected Session getSession() {
	if (session == null)
	    session = SessionManager.getSession();
	return session;
    }

    public void updateCG() {}

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this layout.
     *
     * @return "BorderLayoutCG"
     * @see SLayoutManager#getCGClassID
     * @see org.wings.plaf.CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }
}
