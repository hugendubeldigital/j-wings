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
import org.wings.externalizer.ExternalizeManager;

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

    private String columns;
    private String rows;

    public SFrameSetLayout(String cols, String rows) {
	setColumns(cols);
	setRows(rows);
    }

    public SFrameSetLayout(String[] cols, String[] rows) {
	setColumns(cols);
	setRows(rows);
    }

    public void setColumns(String[] c) {
	StringBuffer buffer = new StringBuffer(c[0]);
	for (int i=1; i < c.length; i++) {
	    buffer.append(",");
	    buffer.append(c[i]);
	}
	setColumns(buffer.toString());
    }
    public void setColumns(String columns) {
	this.columns = columns;
	if (getContainer() != null)
	    getContainer().reload(SConstants.RELOAD_STATE);
    }

    public String getColumns() { return columns; }

    public void setRows(String[] r) {
	StringBuffer buffer = new StringBuffer(r[0]);
	for (int i=1; i < r.length; i++) {
	    buffer.append(",");
	    buffer.append(r[i]);
	}
	setRows(buffer.toString());
    }

    public void setRows(String rows) {
	this.rows = rows;
	if (getContainer() != null)
	    getContainer().reload(SConstants.RELOAD_STATE);
    }

    public String getRows() { return rows; }

    public void addComponent(SComponent c, Object constraint) {
        components.add(c);
	constraints.add(constraint);
    }

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

	if (columns != null && columns.length() > 0) {
	    d.append(" cols=\"");
	    d.append(columns);
	    d.append("\"");
	}

	if (rows != null && rows.length() > 0) {
	    d.append(" rows=\"");
	    d.append(rows);
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
	    frameSet.getSession().getReloadManager().clearDirtyComponents(SConstants.RELOAD_ALL);
	    //writeFrame(d, (SFrame)frameSet.getSession().getReloadManager().getManagerComponent(), null);
	    d.append("</frameset>\n");
	}
    }

    protected void writeFrame(Device d, SFrame frame, Properties properties)
	throws IOException
    {
	String src = getSession().getExternalizeManager()
	    .externalize(frame.show(), "text/html", ExternalizeManager.REQUEST);

	d.append("<frame src=\"")
	    .append(src)
	    .append("\"");
	d.append(" name=\"frame")
	    .append(frame.getUnifiedId())
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

    public String getCGClassID() {
        return cgClassID;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
